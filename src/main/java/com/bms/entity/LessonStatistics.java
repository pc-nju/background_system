package com.bms.entity;

import lombok.Data;

/**
 * @author 咸鱼
 * @date 2019-08-07 10:57
 */
@Data
public class LessonStatistics {
    private Long userId;
    private String userName;
    private String subjectName;
    private Integer lessonStatistics;
}
