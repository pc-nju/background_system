package com.bms.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 咸鱼
 * @date 2019-06-03 22:32
 * 路由元数据，对应前端的路由 router 中的配置参数 meta
 */
@Data
public class MenuMeta implements Serializable {
    /**
     * 是否保持长连接
     */
    private Boolean keepAlive;
    /**
     * 是否需要权限
     */
    private Boolean requireAuth;
}
