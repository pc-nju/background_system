package com.bms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-03 22:34
 * 备注：这些字段和前端的路由 router 中的配置参数相对应
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu implements Serializable {
    private Long id;
    /**
     * 访问路径pattern
     */
    @NotBlank
    private String url;
    /**
     * 路由地址
     */
    @NotBlank
    private String path;
    /**
     * 组件名
     */
    @NotBlank
    private String component;
    /**
     * 菜单名
     */
    @NotBlank
    private String name;

    private String iconCls;
    /**
     * 路由元数据，对应前端的路由 router 中的配置参数 meta
     */
    private MenuMeta meta;
    /**
     * 父菜单id
     */
    private Long parentId;
    /**
     * 是否激活
     */
    private Boolean enabled;
    /**
     * 角色集合
     */
    private List<Role> roles;
    /**
     * 子菜单集合
     */
    private List<Menu> children;
}
