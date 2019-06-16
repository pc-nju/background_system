package com.bms.util;

import org.springframework.util.AntPathMatcher;

/**
 * @author 咸鱼
 * @date 2019-06-03 20:54
 */
public interface FinalName {
    String USERNAME_NOT_FOUND = "用户名不对";
    String UN_LOGIN = "未登录";
    String INSUFFICIENT_AUTH = "权限不足";


    AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    String ROLE_LOGIN = "ROLE_LOGIN";

    String CONTENT_TYPE = "application/json;charset=utf-8";

    String ROLE_PREFIX = "ROLE_";

    String USER_SEARCH_WORD_ALL = "all";

    String ROLE_ADMIN = "ROLE_admin";
}
