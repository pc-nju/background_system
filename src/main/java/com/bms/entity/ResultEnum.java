package com.bms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 咸鱼
 * @date 2019-05-31 17:21
 */
@AllArgsConstructor
@Getter
public enum ResultEnum {
    ADD_USER_SUCCESS("200", "新增用户成功！"),
    ADD_USER_ERROR("500", "新增用户成功！");

    private String status;
    private String msg;
}
