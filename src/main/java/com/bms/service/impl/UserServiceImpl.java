package com.bms.service.impl;

import com.bms.dao.UserDao;
import com.bms.dto.UserDto;
import com.bms.entity.User;
import com.bms.service.UserService;
import com.bms.util.FinalName;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/12/13 22:41
 */
@Service
@Primary
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public boolean save(User user) {
        return userDao.insertUser(user) == 1;
    }

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public UserDto getUsersByKeyWords(String keyWords, Integer pageNum, Integer pageSize) {
        if (StringUtils.isEmpty(keyWords.trim())) {
            keyWords = FinalName.USER_SEARCH_WORD_ALL;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userDao.getUsersByKeyWords(keyWords);
        PageInfo<User> userInfo = new PageInfo<>(users);
        UserDto userDto = new UserDto();
        userDto.setUsers(users);
        userDto.setTotal(userInfo.getTotal());
        return userDto;
    }

    @Override
    public boolean updateUser(User user) {
        return userDao.updateUser(user) == 1;
    }

    @Override
    public User getUserById(Long userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public boolean updateUserRoles(Long userId, Long[] roleIds) {
        userDao.deleteRolesByUserId(userId);
        return userDao.updateUserRoles(userId, roleIds) == roleIds.length;
    }
}
