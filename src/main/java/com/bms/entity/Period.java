package com.bms.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author 咸鱼
 * @date 2019-07-10 19:25
 */
@Data
public class Period {
    private Long id;
    @NotBlank
    private String name;
    private Date createTime;
}
