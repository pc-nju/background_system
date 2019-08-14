package com.bms.service.impl;

import com.bms.dao.StudentDao;
import com.bms.dto.StudentDto;
import com.bms.entity.Student;
import com.bms.exception.BaseException;
import com.bms.service.StudentService;
import com.bms.service.StudentSubjectService;
import com.bms.util.FinalName;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author 咸鱼
 * @date 2019-06-19 21:24
 */
@Service
@Primary
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = RuntimeException.class)
public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao;
    private final StudentSubjectService studentSubjectService;

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = studentDao.selectAllStudents();
        students.forEach(student -> student.setSubjectIds(studentSubjectService.getSubjectIdsByStudentId(student.getId())));
        return studentDao.selectAllStudents();
    }

    @Override
    public Map<String, Object> getStudents(StudentDto studentDto) {
        // 设置默认起始页和每页数量
        if (studentDto.getPageNum() == null) {
            studentDto.setPageNum(1);
        }
        if (studentDto.getPageSize() == null) {
            studentDto.setPageSize(10);
        }
        String beginDateScope = studentDto.getBeginDateScope();
        if (beginDateScope != null && beginDateScope.contains(FinalName.SEPARATOR)) {
            String[] dates = beginDateScope.split(FinalName.SEPARATOR);
            studentDto.setStartBeginDate(LocalDate.parse(dates[0], FinalName.DATE_TIME_FORMATTER));
            studentDto.setEndBeginDate(LocalDate.parse(dates[1], FinalName.DATE_TIME_FORMATTER));
        }
        String subjectIds = studentDto.getSubjectIds();
        if (StringUtils.isNoneBlank(subjectIds)) {
            String[] ids = new String[subjectIds.length()];
            if (subjectIds.contains(FinalName.SEPARATOR)) {
                ids = subjectIds.split(FinalName.SEPARATOR);
            } else {
                ids[0] = subjectIds;
            }
            studentDto.setIds(ids);
        }
        PageHelper.startPage(studentDto.getPageNum(), studentDto.getPageSize());
        List<Student> students = studentDao.getStudents(studentDto);

        PageInfo<Student> studentPageInfo = new PageInfo<>(students);
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("total", studentPageInfo.getTotal());
        // 查询选修科目
        students.forEach(student -> student.setSubjectIds(studentSubjectService.getSubjectIdsByStudentId(student.getId())));
        resultMap.put("students", students);
        return resultMap;
    }

    @Override
    public boolean removeStudents(String ids) {
        if (StringUtils.isEmpty(ids.trim())) {
            throw new BaseException("前端参数有误！");
        }
        String[] arrIds = ids.split(FinalName.SEPARATOR);
        if (studentDao.deleteStudents(arrIds) == arrIds.length) {
            return true;
        }
        // 如果删除数量和id数量不等，则说明前端参数有误，应触发事务回滚，否则会删除一部分数据
        throw new RuntimeException();
    }

    @Override
    public boolean updateStudent(Student student) {
        Student tempStudent = new Student();
        tempStudent.setId(student.getId());
        Student studentInDb= selectStudent(tempStudent);
        if (studentInDb == null) {
            throw new BaseException("不存在该用户！");
        }
        if (!studentInDb.getStudyState().equals(student.getStudyState())) {
            // 由正在学习切换成暂停学习
            if (studentInDb.getStudyState() && !student.getStudyState()) {
                if (student.getPauseDate() == null ||
                        student.getPauseDate().isBefore(studentInDb.getBeginDate())
                        || student.getPauseDate().isAfter(studentInDb.getEndDate())) {
                    throw new BaseException("暂停时间有误，请仔细检查后再输入！");
                }
            } else {
                // 结束日期 = 暂停恢复日期 - 暂停日期 + 结束日期（PS：暂停日期得在开始日期之后，结束日期之前）
                student.setEndDate(studentInDb.getEndDate().plusDays(Period.between(studentInDb.getPauseDate(),
                        student.getPauseDate()).getDays()));
                // 暂停日期置空
                student.setPauseDate(null);
            }
        }
        int affectedRow = -1;
        if (studentDao.updateStudent(student) == 1) {
            affectedRow = studentSubjectService.updateStudentSubject(student.getId(), student.getSubjectIds());
        }
        return affectedRow == student.getSubjectIds().length;
    }

    @Override
    public boolean updateStudentStudyState(Long id, Boolean studyState, Boolean updateEndDate) {
        Student tempStudent = new Student();
        tempStudent.setId(id);
        Student studentInDb= selectStudent(tempStudent);
        if (studentInDb == null) {
            throw new BaseException("不存在该用户！");
        }
        // 若需要更新结束日期，则业务逻辑为：结束日期 = 当前日期 - 暂停日期 + 结束日期
        if (updateEndDate) {
            // 当前日期 - 暂停日期 + 结束日期
            long days = ChronoUnit.DAYS.between(studentInDb.getPauseDate(), LocalDate.now());
            studentInDb.setEndDate(studentInDb.getEndDate().plusDays(days));
            // 置空
            studentInDb.setPauseDate(null);
        } else {
            // 若不需要更新结束日期，说明学生暂停学习，则需要更新暂停时间为当前时间
            studentInDb.setPauseDate(LocalDate.now());
        }
        studentInDb.setStudyState(studyState);
        return studentDao.updateStudent(studentInDb) == 1;
    }

    @Override
    public boolean addStudents(List<Student> students) {
        if (!CollectionUtils.isEmpty(students)) {
            // 先删除和数据库中重复的学生
            for (Iterator<Student> studentIterator = students.iterator(); studentIterator.hasNext(); ){
                Student student = studentIterator.next();
                Student studentTemp = new Student();
                studentTemp.setName(student.getName());
                studentTemp.setParentName(student.getParentName());
                Student studentInDb = selectStudent(studentTemp);
                if (studentInDb != null) {
                    studentIterator.remove();
                }
            }
            if (!CollectionUtils.isEmpty(students)) {
                if (studentDao.insertStudents(students) == students.size()){
                    Map<Long, Long[]> map = new HashMap<>(students.size());
                    students.forEach(student -> map.put(student.getId(), student.getSubjectIds()));
                    // 插入学生和课程之间的对应关系
                    studentSubjectService.insertStudentSubjectsBatch(map);
                } else {
                    throw new BaseException("导入失败！");
                }
            } else {
                throw new BaseException("数据库中已存在这些学员信息，无需重复导入！");
            }
        }
        return true;
    }

    @Override
    public boolean addStudent(Student student) {
        Student tempStudent = new Student();
        // 只用姓名查，可能查出来多个，但既同名，父母也同名的肯定唯一
        tempStudent.setName(student.getName());
        tempStudent.setParentName(student.getParentName());
        Student studentInDb= selectStudent(tempStudent);
        if (studentInDb != null) {
            throw new BaseException("该用户已存在！");
        }
        if (student.getPauseDate() != null){
            if (student.getPauseDate().isBefore(student.getBeginDate()) ||
                    student.getPauseDate().isAfter(student.getEndDate())) {
                throw new BaseException("暂停时间有误，请仔细检查后再输入！");
            }
        }
        int affectedRow = -1;
        synchronized (this) {
            // 生成学号，用0补足8位
            student.setIdCard(String.format("%08d", Integer.parseInt(studentDao.selectMaxId() == null ? "0" : studentDao.selectMaxId()) + 1));
            if (studentDao.insertStudent(student) == 1) {
                affectedRow = studentSubjectService.updateStudentSubject(student.getId(), student.getSubjectIds());
            }
        }
        return affectedRow == student.getSubjectIds().length;
    }
    
    private Student selectStudent(Student student) {
        return studentDao.selectStudent(student);
    }

}
