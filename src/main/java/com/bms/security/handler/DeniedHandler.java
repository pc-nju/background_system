package com.bms.security.handler;

import com.bms.dto.ResultDto;
import com.bms.util.CommonUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 咸鱼
 * @date 2019-06-04 19:59
 */
@Component
public class DeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        CommonUtils.writeResponse(response, ResultDto.error("权限不足，请联系管理员！"), HttpServletResponse.SC_FORBIDDEN);
    }
}
