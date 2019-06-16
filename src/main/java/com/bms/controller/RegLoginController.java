package com.bms.controller;

import com.bms.dto.ResultDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 咸鱼
 * @date 2019-06-06 20:54
 */
@RestController
public class RegLoginController {
    @GetMapping("/login_p")
    public ResultDto login() {
        return ResultDto.error("尚未登录，请登录！");
    }
}
