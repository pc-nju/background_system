package com.bms.service;

import com.bms.dto.UserDto;
import com.bms.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-05-31 17:42
 */
public interface UserService {
    /**
     * 保存用户
     * @param user {@link User}
     * @return true：成功 false：失败
     */
    boolean save(User user);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return {@link User}
     */
    User findUserByUsername(@Param("username") String username);

    /**
     * 根据关键词搜索用户
     * @param keyWords 关键词
     * @param pageNum 起始页码
     *@param pageSize 每页数量
     * @return {@link UserDto}
     */
    UserDto getUsersByKeyWords(String keyWords, Integer pageNum, Integer pageSize);

    /**
     * 更新用户信息
     * @param user {@link User}
     * @return true：成功 false：失败
     */
    boolean updateUser(User user);

    /**
     * 根据用户id获取用户
     * @param userId 用户id
     * @return {@link User}
     */
    User getUserById(Long userId);

    /**
     * 更新用户角色信息
     * @param userId 用户id
     * @param roleIds 角色id数据
     * @return true：成功 false：失败
     */
    boolean updateUserRoles(Long userId, Long[] roleIds);

    /**
     * 根据用户id删除用户
     * @param id 用户id
     * @return {@code true}成功 {@code false}失败
     */
    boolean removeUser(Long id);

    /**
     * 新增用户
     * @param user {@link User}
     * @return {@code true} 成功 {@code false} 失败
     */
    boolean addUser(User user);

    /**
     * 获取所有{@link User}
     * @return {@link User}集合
     */
    List<User> getAllUsers();
}
