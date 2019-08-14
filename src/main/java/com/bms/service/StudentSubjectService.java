package com.bms.service;


import java.util.List;
import java.util.Map;

/**
 * @author 咸鱼
 * @date 2019-06-11 21:17
 */
public interface StudentSubjectService {
    /**
     * 获取指定学生id所报课程id集合
     * @param studentId 角色id
     * @return 课程id集合
     */
    Long[] getSubjectIdsByStudentId(Long studentId);

    /**
     * 更新学生所报课程
     * @param studentId 学生id
     * @param subjectIds 课程id集合
     * @return 受影响的行数
     */
    int updateStudentSubject(Long studentId, Long[] subjectIds);

    /**
     * 插入学生所报课程
     * @param map <学生id, 课程id数组>
     */
    void insertStudentSubjectsBatch(Map<Long, Long[]> map);
}
