package com.bms.dto;

import com.bms.entity.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 咸鱼
 * @date 2019-05-31 17:12
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {
    private String status;
    private String msg;
    private Object obj;

    public ResultDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public ResultDto setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResultDto setObj(Object obj) {
        this.obj = obj;
        return this;
    }
    public static ResultDto success() {
        return new ResultDto("200", null, null);
    }
    public static ResultDto success(String msg) {
        return new ResultDto("200", msg, null);
    }
    public static ResultDto success(String msg, Object obj) {
        return new ResultDto("200", msg, obj);
    }
    public static ResultDto success(ResultEnum resultEnum) {
        return new ResultDto(resultEnum.getStatus(), resultEnum.getMsg(), null);
    }
    public static ResultDto error(String msg) {
        return new ResultDto("500", msg, null);
    }
    public static ResultDto error(String status, String msg) {
        return new ResultDto(status, msg, null);
    }
    public static ResultDto error(ResultEnum resultEnum) {
        return new ResultDto(resultEnum.getStatus(), resultEnum.getMsg(), null);
    }
}
