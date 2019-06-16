package com.bms.service.impl;

import com.bms.dao.MenuRoleDao;
import com.bms.dto.MenuRoleDto;
import com.bms.service.MenuRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-11 21:17
 */
@Service
@Primary
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = RuntimeException.class)
public class MenuRoleServiceImpl implements MenuRoleService {
    private final MenuRoleDao menuRoleDao;

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return menuRoleDao.getMenuIdsByRoleId(roleId);
    }

    @Override
    public int updateMenuRole(Long roleId, Long[] menuIds) {
        // 先删除该角色所能访问的菜单
        menuRoleDao.deleteMenuRole(roleId);
        if (menuIds.length == 0) {
            return 0;
        }
        //更新该角色所能访问的菜单
        return menuRoleDao.updateMenuRole(roleId, menuIds);
    }

}
