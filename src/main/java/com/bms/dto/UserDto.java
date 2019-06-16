package com.bms.dto;

import com.bms.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-12 20:18
 */
@Data
public class UserDto {
    private List<User> users;
    /**
     * 查询结果的总数
     */
    private Long total;
}
