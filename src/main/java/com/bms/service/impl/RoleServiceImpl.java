package com.bms.service.impl;

import com.bms.dao.RoleDao;
import com.bms.entity.Role;
import com.bms.service.RoleService;
import com.bms.util.FinalName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-10 21:29
 */
@Primary
@Service
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    @Override
    public boolean addNewRole(Role role) {
        if (!role.getName().startsWith(FinalName.ROLE_PREFIX)) {
            role.setName(FinalName.ROLE_PREFIX + role.getName());
        }
        return roleDao.insertRole(role) == 1;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.selectAllRoles();
    }

    @Override
    public boolean deleteRoleById(Long roleId) {
        // 因为在建 menu_role 表的时候，规定了 menu_role 表的列 role_id 与 role 表
        // 的 id 列是外键级联删除的，所以这里删除的时候，无需考虑 menu_role 表中还存在的关联关系
        return roleDao.deleteRoleById(roleId) == 1;
    }
}
