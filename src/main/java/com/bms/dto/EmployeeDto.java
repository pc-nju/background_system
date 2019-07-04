package com.bms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author 咸鱼
 * @date 2019-06-23 21:46
 */
@Data
public class EmployeeDto {
    private Integer pageNum;
    private Integer pageSize;
    private String searchWords;
    private Long nationId;
    private Long politicId;
    private String engageForm;
    private String beginDateScope;

    private LocalDate startBeginDate;
    private LocalDate endBeginDate;
}
