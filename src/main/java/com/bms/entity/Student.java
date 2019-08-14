package com.bms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-08-08 15:08
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Integer age;
    @NotBlank
    private String gender;
    @NotBlank
    private String parentName;
    @NotBlank
    private String parentPhone;
    /**
     * 学号
     */
    private String idCard;
    @NotNull
    private Integer registerPrice;
    @NotNull
    private LocalDate registerDate;
    @NotNull
    private LocalDate beginDate;
    @NotNull
    private LocalDate endDate;
    /**
     * 是否正在学习
     */
    private Boolean studyState;
    private LocalDate pauseDate;
    private LocalDate createDate;
    private Long[] subjectIds;
}
