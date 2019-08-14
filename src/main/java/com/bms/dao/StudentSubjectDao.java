package com.bms.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 咸鱼
 * @date 2019-06-11 21:18
 */
public interface StudentSubjectDao {
    /**
     * 获取指定学生id所报课程id集合
     * @param studentId 角色id
     * @return 课程id集合
     */
    Long[] getSubjectIdsByStudentId(@Param("studentId") Long studentId);

    /**
     * 根据学生id，删除该学生所报的所有课程
     * @param studentId 学生id
     */
    void deleteStudentSubject(@Param("studentId") Long studentId);
    /**
     * 更新角色所能访问的菜单
     * @param studentId 学生id
     * @param subjectIds 课程id集合
     * @return 受影响的行数
     */
    int insertStudentSubjects(@Param("studentId") Long studentId, @Param("subjectIds") Long[] subjectIds);


    /**
     * 插入学生所报课程
     * @param map <学生id, 课程id数组>
     */
    void insertStudentSubjectsBatch(@Param("paramsMap") Map<Long, Long[]> map);
}
