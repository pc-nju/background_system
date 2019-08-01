package com.bms.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author 咸鱼
 * @date 2019-07-31 18:42
 */
@Data
public class Classroom {
    private Long id;
    @NotBlank
    private String name;
    private Date createTime;
}
