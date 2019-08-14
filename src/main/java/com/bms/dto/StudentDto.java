package com.bms.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author 咸鱼
 * @date 2019-06-23 21:46
 */
@Data
public class StudentDto {
    private Integer pageNum;
    private Integer pageSize;
    private String searchWords;
    private String subjectIds;
    private String beginDateScope;

    private String[] ids;
    private LocalDate startBeginDate;
    private LocalDate endBeginDate;
}
