package com.bms.dao;

import com.bms.entity.Role;
import com.bms.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/12/13 22:43
 */
public interface UserDao {
    /**
     * 插入 {@link User}
     * @param user {@link User}
     * @return 受影响的行数
     */
    int insertUser(User user);

    /**
     * 根据用户id
     * @param userId 用户id
     * @return 该用户拥有的 {@link Role} 集合
     */
    List<Role> getRolesById(@Param("userId") Long userId);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return {@link User}
     */
    User findUserByUsername(@Param("username") String username);

    /**
     * 根据关键词搜索用户
     * @param keyWords 关键词
     * @return {@link User}
     */
    List<User> getUsersByKeyWords(@Param("keyWords") String keyWords);

    /**
     * 更新用户信息
     * @param user {@link User}
     * @return 受影响的行数
     */
    int updateUser(User user);

    /**
     * 根据用户id获取用户
     * @param userId 用户id
     * @return {@link User}
     */
    User getUserById(@Param("id") Long userId);

    /**
     * 删除用户所被分配的角色
     * @param userId 用户id
     */
    void deleteRolesByUserId(@Param("userId") Long userId);

    /**
     * 更新用户角色信息
     * @param userId 用户id
     * @param roleIds 角色id数据
     * @return 受影响的行数
     */
    int updateUserRoles(@Param("userId") Long userId, @Param("roleIds") Long[] roleIds);
}
