package com.bms.dao;

import com.bms.dto.StudentDto;
import com.bms.entity.Student;
import com.bms.entity.Nation;
import com.bms.entity.PoliticsStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-19 21:25
 */
public interface StudentDao {
    /**
     * 新增雇员
     * @param students {@link Student}
     * @return 受影响的行数
     */
    int insertStudents(List<Student> students);
    /**
     * 查询所有雇员
     * @return {@link Student}
     */
    List<Student> selectAllStudents();

    /**
     * 根据指定条件查询{@link StudentDto}
     * @param studentDto 待查询条件对象
     * @return {@link StudentDto}
     */
    List<Student> getStudents(StudentDto studentDto);

    /**
     * 根据雇员id删除雇员
     * @param arrIds 雇员id组成的数组
     * @return 受影响的行数
     */
    int deleteStudents(@Param("ids") String[] arrIds);

    /**
     * 更新雇员信息
     * @param student {@link Student}
     * @return 受影响的行数
     */
    int updateStudent(Student student);

    /**
     * 根据条件查询{@link Student}
     * @param student 待查询条件
     * @return {@link Student}
     */
    Student selectStudent(Student student);

    /**
     * 查询当前数据库中记录的最大id
     * @return 最大id
     */
    String selectMaxId();

    /**
     * 插入{@link Student}
     * @param student {@link Student}
     * @return 受影响的行数
     */
    int insertStudent(Student student);
}
