package com.bms.service.impl;

import com.bms.dao.MenuRoleDao;
import com.bms.dao.StudentSubjectDao;
import com.bms.exception.BaseException;
import com.bms.service.MenuRoleService;
import com.bms.service.StudentSubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author 咸鱼
 * @date 2019-06-11 21:17
 */
@Service
@Primary
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = RuntimeException.class)
public class StudentSubjectServiceImpl implements StudentSubjectService {
    private final StudentSubjectDao studentSubjectDao;

    @Override
    public Long[] getSubjectIdsByStudentId(Long studentId) {
        return studentSubjectDao.getSubjectIdsByStudentId(studentId);
    }

    @Override
    public int updateStudentSubject(Long studentId, Long[] subjectIds) {
        if (subjectIds.length == 0) {
            return 0;
        }
        //先删除该学生所报的所有课程
        studentSubjectDao.deleteStudentSubject(studentId);
        int affectedRows = studentSubjectDao.insertStudentSubjects(studentId, subjectIds);
        if (affectedRows != subjectIds.length) {
            throw new BaseException("更新科目失败！");
        }
        return affectedRows;
    }

    @Override
    public void insertStudentSubjectsBatch(Map<Long, Long[]> map) {
        if (!CollectionUtils.isEmpty(map)) {
            studentSubjectDao.insertStudentSubjectsBatch(map);
        }
    }

}
