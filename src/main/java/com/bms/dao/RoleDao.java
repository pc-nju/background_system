package com.bms.dao;

import com.bms.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-02 9:59
 */
public interface RoleDao {
    /**
     * 插入 {@link Role}
     * @param role {@link Role}
     * @return 受影响的行数
     */
    int insertRole(Role role);

    /**
     * 获取所有角色
     * @return {@link Role}集合
     */
    List<Role> selectAllRoles();

    /**
     * 根据角色id，删除角色
     * @param roleId 角色id
     * @return 受影响的行数
     */
    int deleteRoleById(@Param("id") Long roleId);
}
