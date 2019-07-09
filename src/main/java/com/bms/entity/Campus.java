package com.bms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author 咸鱼
 * @date 2019-07-08 22:01
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Campus {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
    private LocalDateTime createTime;
}
