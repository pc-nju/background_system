package com.bms.service;

import com.bms.dto.MenuRoleDto;
import com.bms.entity.Menu;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-11 21:17
 */
public interface MenuRoleService {
    /**
     * 获取指定角色id所能访问的{@link Menu} 的id
     * @param roleId 角色id
     * @return 角色id集合
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 更新角色所能访问的菜单
     * @param roleId 角色id
     * @param menuIds 菜单id集合
     * @return 受影响的行数
     */
    int updateMenuRole(Long roleId, Long[] menuIds);
}
