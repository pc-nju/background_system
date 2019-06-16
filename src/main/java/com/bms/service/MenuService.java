package com.bms.service;

import com.bms.entity.Menu;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-04 16:50
 */
public interface MenuService {
    /**
     * 获取所有 {@link Menu}
     * @return {@link Menu}集合
     */
    List<Menu> getAllMenus();

    /**
     * 根据用户id获取该用户能展示的菜单
     * @return {@link Menu}集合
     * @param userId 用户id
     */
    List<Menu> getMenusByUserId(Long userId);

    /**
     * 获取菜单树
     * @return {@link Menu}
     */
    List<Menu> getMenuTree();

    /**
     * 新增菜单
     * @param menu {@link Menu}
     * @return true：成功 false：失败
     */
    boolean addMenu(Menu menu);

    /**
     * 根据菜单id删除菜单
     * @param id 菜单id
     * @return {@code true}成功 {@code false}失败
     */
    boolean removeMenusById(Long id);
}
