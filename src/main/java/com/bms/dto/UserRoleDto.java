package com.bms.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 咸鱼
 * @date 2019-06-13 20:57
 */
@Data
public class UserRoleDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long[] roleIds;
}
