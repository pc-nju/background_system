package com.bms.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 咸鱼
 * @date 2019-06-11 21:40
 */
@Data
public class MenuRoleDto implements Serializable {
    @NotNull
    private Long roleId;
    @NotNull
    private Long[] menuIds;
}
