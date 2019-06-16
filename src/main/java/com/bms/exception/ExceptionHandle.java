package com.bms.exception;

import com.bms.dto.ResultDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 咸鱼
 * @date 2019-06-15 22:53
 */
@ControllerAdvice
public class ExceptionHandle {
    /**
     * 只拦截BaseException异常
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public ResultDto handle(Exception e) {
        return ResultDto.error(e.getMessage());
    }
}
