package com.bms.dao;

import com.bms.entity.Menu;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-04 16:54
 */
public interface MenuDao {
    /**
     * 获取所有 {@link Menu}
     * @return {@link Menu}集合
     */
    List<Menu> getAllMenus();
    /**
     * 根据用户id获取该用户能展示的菜单
     * @param userId 用户id
     * @return {@link Menu}集合
     */
    List<Menu> getMenusByUserId(@Param("userId") Long userId);
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
    int insertMenu(Menu menu);

    /**
     * 根据条件查询Menu
     * @param menu {@link Menu}
     * @return {@link Menu}
     */
    Menu selectMenu(Menu menu);

    /**
     * 根据菜单id删除菜单
     * @param id 菜单id
     * @return 受影响的行数
     */
    int deleteMenu(@Param("id") Long id);
}
