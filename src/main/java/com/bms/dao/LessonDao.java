package com.bms.dao;

import com.bms.entity.Lesson;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-12 19:13
 */
public interface LessonDao {
    /**
     * 查询{@link Lesson}
     * @param lesson 待查询{@link Lesson}
     * @return {@link Lesson}集合
     */
    List<Lesson> selectLessons(Lesson lesson);

    /**
     * 批量新增课程
     * @param lessons {@link Lesson}集合
     * @return 受影响的行数
     */
    int insertLessons(@Param("lessons") List<Lesson> lessons);

    /**
     * 更新课程
     * @param lesson {@link Lesson}
     * @return 受影响的行数
     */
    int updateLesson(Lesson lesson);

    /**
     * 查询{@link Lesson}
     * @param id 课程id
     * @return {@link Lesson}
     */
    Lesson selectLessonById(@Param("id") Long id);

    /**
     * 根据id删除课程安排
     * @param idArr id数组
     * @return 受影响的行数
     */
    int deleteLessons(@Param("ids") String[] idArr);
}
