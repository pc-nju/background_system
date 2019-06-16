package com.bms.controller.system;

import com.bms.dto.MenuRoleDto;
import com.bms.dto.ResultDto;
import com.bms.entity.Menu;
import com.bms.entity.Role;
import com.bms.service.MenuRoleService;
import com.bms.service.MenuService;
import com.bms.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 咸鱼
 * @date 2019-06-10 21:28
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/system/basic")
public class SystemBasicController {
    private final RoleService roleService;
    private final MenuService menuService;
    private final MenuRoleService menuRoleService;

    @PostMapping("/addRole")
    public ResultDto addRole(@RequestBody @Valid Role role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (roleService.addNewRole(role)) {
            return ResultDto.success("添加成功！");
        }
        log.warn("添加[" + role.getNameZh() + "]角色失败！");
        return ResultDto.error("添加失败！");
    }

    @DeleteMapping("/role/{roleId}")
    public ResultDto deleteRole(@PathVariable Long roleId) {
        if (roleService.deleteRoleById(roleId)) {
            return ResultDto.success("删除成功！");
        }
        log.warn("删除ID为[" + roleId + "]的角色失败！");
        return ResultDto.error("删除失败！");
    }
    @GetMapping("/menuTree/{roleId}")
    public ResultDto getMenuTreeByRoleId(@PathVariable Long roleId) {
        List<Menu> menus = menuService.getMenuTree();
        if (CollectionUtils.isEmpty(menus)) {
            return ResultDto.error("获取所有菜单失败！");
        }
        List<Long> menuIds = menuRoleService.getMenuIdsByRoleId(roleId);
        Map<String, Object> map = new HashMap<>();
        map.put("menus", menus);
        map.put("menuIds", menuIds);
        return ResultDto.success().setObj(map);
    }
    @GetMapping("/allMenusTree")
    public ResultDto getAllMenusTree() {
        List<Menu> menus = menuService.getMenuTree();
        if (CollectionUtils.isEmpty(menus)) {
            return ResultDto.error("获取所有菜单失败！");
        }
        return ResultDto.success().setObj(menus);
    }

    @PutMapping("/updateMenuRole")
    public ResultDto updateMenuRole(@RequestBody @Valid MenuRoleDto menuRoleDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (menuRoleDto.getMenuIds().length != menuRoleService.updateMenuRole(menuRoleDto.getRoleId(), menuRoleDto.getMenuIds())) {
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功！");
    }

    @GetMapping("/roles")
    public ResultDto getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            return ResultDto.success().setObj(roles);
        }
        return ResultDto.error("获取所有角色失败！");
    }
    @PostMapping("/menu")
    public ResultDto addMenu(@RequestBody @Valid Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (!menuService.addMenu(menu)) {
            return ResultDto.error("新增失败！");
        }
        return ResultDto.success().setObj(menu);
    }
    @DeleteMapping("/menu/{id}")
    public ResultDto removeMenus(@PathVariable Long id) {
        if (!menuService.removeMenusById(id)) {
            return ResultDto.error("删除失败！");
        }
        return ResultDto.success("删除成功！");
    }
}
