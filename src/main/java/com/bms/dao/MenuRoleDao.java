package com.bms.dao;

import com.bms.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-11 21:18
 */
public interface MenuRoleDao {
    /**
     * 获取指定角色id所能访问的{@link Menu} 的id
     * @param roleId 角色id
     * @return 角色id集合
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 根据角色id，删除该角色id所能访问的所有菜单的id
     * @param roleId 角色id
     */
    void deleteMenuRole(@Param("roleId") Long roleId);
    /**
     * 更新角色所能访问的菜单
     * @param roleId 角色id
     * @param menuIds 菜单id集合
     * @return 受影响的行数
     */
    int updateMenuRole(@Param("roleId") Long roleId, @Param("menuIds") Long[] menuIds);


}
