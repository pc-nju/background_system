package com.bms.service;

import com.bms.dto.StudentDto;
import com.bms.entity.Student;

import java.util.List;
import java.util.Map;

/**
 * @author 咸鱼
 * @date 2019-06-19 21:24
 */
public interface StudentService {

    /**
     * 新增学生
     * @param students {@link Student}
     * @return {@code true}成功 {@code false}失败
     */
    boolean addStudents(List<Student> students);

    /**
     * 查询所有学生
     * @return {@link Student}
     */
    List<Student> getAllStudents();

    /**
     * 根据指定条件查询{@link Student}
     * @param studentDto 待查询条件对象
     * @return 结果集
     */
    Map<String, Object> getStudents(StudentDto studentDto);

    /**
     * 根据学生id删除学生
     * @param ids id组成的字符串，形如 1,2,3,4
     * @return {@code true}成功 {@code false}失败
     */
    boolean removeStudents(String ids);
    
    /**
     * 新增学生
     * @param student {@link Student}
     * @return {@code true}成功 {@code false}失败
     */
    boolean addStudent(Student student);

    /**
     * 更新学生信息
     * @param student {@link Student}
     * @return {@code true}成功 {@code false}失败
     */
    boolean updateStudent(Student student);

    /**
     * 更新学生学习状态
     * @param id 学生id
     * @param studyState 学习状态
     * @param updateEndDate 是否更新结束日期
     * @return {@code true}成功 {@code false}失败
     */
    boolean updateStudentStudyState(Long id, Boolean studyState, Boolean updateEndDate);
}
