package com.bms.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-13 16:00
 */
@Data
public class LessonDto {
    private String week;
    private Date monday;
    private List<PlanDto> plans;
}
