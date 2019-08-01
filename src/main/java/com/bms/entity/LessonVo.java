package com.bms.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 咸鱼
 * @date 2019-07-31 16:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LessonVo extends Lesson {
    private Boolean isWeekCycle;
}
