package com.bms.dto;

import com.bms.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-13 16:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanDto {
    private String period;
    private List<Lesson> mon;
    private List<Lesson> tue;
    private List<Lesson> wed;
    private List<Lesson> thurs;
    private List<Lesson> fri;
    private List<Lesson> sat;
    private List<Lesson> sun;
}
