package com.bms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author 咸鱼
 * @date 2019-07-12 18:31
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Lesson {
    private Long id;
    @NotNull
    private Long subjectId;
    private Subject subject;
    @NotNull
    private Long userId;
    private User user;

    @NotNull
    private Long classroomId;
    private Classroom classroom;

    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;

    private Integer lessonStatistics;

    private String week;
    @NotNull
    private Long campusId;
    private Campus campus;

    private LocalDateTime createTime;
}
