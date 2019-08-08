package com.bms.entity;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author 咸鱼
 * @date 2019-08-08 15:08
 */
@Data
public class Student {
    private Long id;
    private String name;
    private Integer age;
    private String sex;
    private String parentName;
    private String parentPhone;
    private Double registerPrice;
    private LocalDate registerDate;
    private LocalDate startDate;
    private LocalDate endDate;
    /**
     * 是否暂停
     */
    private Boolean isPaused;
    private LocalDate pauseDate;
}
