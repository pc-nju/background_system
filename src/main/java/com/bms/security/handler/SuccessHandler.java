package com.bms.security.handler;

import com.bms.dto.ResultDto;
import com.bms.security.UserUtils;
import com.bms.util.CommonUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 咸鱼
 * @date 2019-06-04 19:54
 */
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CommonUtils.writeResponse(response, ResultDto.success("登陆成功！", UserUtils.getCurrentUser()));
    }
}
