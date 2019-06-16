package com.bms.service.impl;

import com.bms.dao.MenuDao;
import com.bms.entity.Menu;
import com.bms.exception.BaseException;
import com.bms.security.UserUtils;
import com.bms.service.MenuService;
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
 * @date 2019-06-04 16:52
 */
@Service
@Primary
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = RuntimeException.class)
public class MenuServiceImpl implements MenuService {
    private final MenuDao menuDao;

    @Override
    public List<Menu> getAllMenus() {
        return menuDao.getAllMenus();
    }

    @Override
    public List<Menu> getMenusByUserId(Long userId) {
        return menuDao.getMenusByUserId(userId);
    }

    @Override
    public List<Menu> getMenuTree() {
        return menuDao.getMenuTree();
    }

    @Override
    public boolean addMenu(Menu menu) {
        if (selectMenu(menu) != null) {
            throw new BaseException("该菜单名称已经存在！");
        }
        menu.setEnabled(true);
        return menuDao.insertMenu(menu) == 1;
    }

    @Override
    public boolean removeMenusById(Long id) {
        Menu menu = new Menu();
        menu.setId(id);
        if (selectMenu(menu) == null) {
            throw new BaseException("该菜单不存在，无法删除！");
        }
        /*
         * 备注1：不用关心 parentId = #{id}的情况，因为 parentId 和 id 是级联删除的
         * 备注1：不用关心menu_role中与之相关联的条目，因为是级联删除的
         */
        return menuDao.deleteMenu(id) > 0;
    }

    private Menu selectMenu(Menu menu) {
        return menuDao.selectMenu(menu);
    }
}
