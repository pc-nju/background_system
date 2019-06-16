package com.bms.security;

import com.bms.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 咸鱼
 * @date 2019-06-04 19:56
 */
public class UserUtils {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
