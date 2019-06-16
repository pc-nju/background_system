package com.bms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 咸鱼
 * @date 2019-05-31 17:01
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role implements Serializable {
    private Long id;
    /**
     * 角色的英文名
     */
    @NotBlank
    private String name;
    /**
     * 角色的中文名
     */
    @NotBlank
    private String nameZh;
}
