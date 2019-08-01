package com.bms.service;

import com.bms.dto.LessonDto;
import com.bms.entity.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-12 19:11
 */
public interface LessonService {
    /**
     * 新增{@link Lesson}
     * @param lessonVo {@link LessonVo}
     * @return {@code true}成功 {@code false}失败
     */
    boolean addLesson(LessonVo lessonVo);

    /**
     * 获取所有排课计划
     * @param startTime 开始时间
     * @param userId {@link User}id
     * @param subjectId {@link Subject}id
     * @param campusId {@link Campus}id
     * @param classroomId {@link Classroom}id
     * @return {@link LessonDto}集合
     */
    List<LessonDto> getLessons(Date startTime, Long userId, Long subjectId, Long campusId, Long classroomId);

    /**
     * 更新{@link Lesson}
     * @param lesson {@link Lesson}
     * @return {@code true}成功 {@code false}失败
     */
    boolean updateLesson(Lesson lesson);

    /**
     * 根据id删除课程安排
     * @param ids id组成的字符串，如：1,2,3
     * @return {@code true}成功 {@code false}失败
     */
    boolean removeLesson(String ids);
}
