package com.bms.controller.system;

import com.bms.dto.ResultDto;
import com.bms.dto.UserDto;
import com.bms.dto.UserRoleDto;
import com.bms.entity.User;
import com.bms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author 咸鱼
 * @date 2019-06-12 19:38
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/system/user")
public class SystemUserController {
    private final UserService userService;

    /**
     * PageHelper的起始页从1开始
     */
    @GetMapping("/{keyWords}")
    public ResultDto getUsersByKeyWords(@PathVariable(required = false) String keyWords,
                                        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                        @RequestParam(required = false, defaultValue = "6") Integer pageSize) {
        UserDto userDto = userService.getUsersByKeyWords(keyWords, pageNum, pageSize);
        if (CollectionUtils.isEmpty(userDto.getUsers())) {
            return ResultDto.error("获取用户数据失败！");
        }
        return ResultDto.success().setObj(userDto);
    }

    @GetMapping("/id/{userId}")
    public ResultDto getUserById(@PathVariable Long userId) {
        User userInDb =userService.getUserById(userId);
        if (userInDb == null) {
            log.warn("获取id为[" + userId + "]的用户信息失败！");
            return ResultDto.error("获取用户数据失败！");
        }
        return ResultDto.success().setObj(userInDb);
    }

    @PutMapping("/")
    public ResultDto updateUser(@RequestBody User user) {
        if (!userService.updateUser(user)) {
            log.warn("更新id为[" + user.getId() + "]的用户信息失败！");
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功！");
    }

    @PutMapping("/roles")
    public ResultDto updateUserRoles(@RequestBody @Valid UserRoleDto userRoleDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (!userService.updateUserRoles(userRoleDto.getUserId(), userRoleDto.getRoleIds())) {
            log.warn("更新id为[" + userRoleDto.getUserId() + "]的用户信息失败！");
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功！");
    }
}
