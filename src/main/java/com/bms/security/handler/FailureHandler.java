package com.bms.security.handler;

import com.bms.dto.ResultDto;
import com.bms.util.CommonUtils;
import com.bms.util.FinalName;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 咸鱼
 * @date 2019-06-04 19:25
 * 认证失败（用户名，密码登录失败时）的处理器
 */
@Component
public class FailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ResultDto resultDto;
        if (exception instanceof BadCredentialsException ||
                exception instanceof UsernameNotFoundException) {
            resultDto = ResultDto.error("账户名或者密码输入错误！");
        } else if (exception instanceof LockedException) {
            resultDto = ResultDto.error("账户被锁定，请联系管理员！");
        } else if (exception instanceof CredentialsExpiredException) {
            resultDto = ResultDto.error("密码过期，请联系管理员！");
        } else if (exception instanceof AccountExpiredException) {
            resultDto = ResultDto.error("账户过期，请联系管理员！");
        } else if (exception instanceof DisabledException) {
            resultDto = ResultDto.error("账户被禁用，请联系管理员！");
        } else {
            resultDto = ResultDto.error("登录失败！");
        }
        CommonUtils.writeResponse(response, resultDto, HttpServletResponse.SC_UNAUTHORIZED);
    }
}
