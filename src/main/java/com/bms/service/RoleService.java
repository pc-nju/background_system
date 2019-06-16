package com.bms.service;

import com.bms.entity.Role;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-10 21:29
 */
public interface RoleService {
    /**
     * 添加角色
     * @param role {@link Role}
     * @return 受影响的行数
     */
    boolean addNewRole(Role role);

    /**
     * 获取所有角色
     * @return {@link Role}集合
     */
    List<Role> getAllRoles();

    /**
     * 根据角色id，删除角色
     * @param roleId 角色id
     * @return true：成功 false：失败
     */
    boolean deleteRoleById(Long roleId);
}
