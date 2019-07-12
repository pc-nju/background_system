package com.bms.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author 咸鱼
 * @date 2019-07-10 19:25
 */
@Data
public class Period {
    private Long id;
    @NotBlank
    private String name;
    private LocalDateTime createTime;
}
