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

    String DEFAULT_USER_FACE = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515233756&di=0856d923a0a37a87fd26604a2c871370&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.qqzhi.com%2Fuploadpic%2F2014-09-27%2F041716704.jpg";

    String DATE_FORMAT = "yyyy-m-d";
    int ARR_LENGTH = 2;
    int ARR_CAPACITY = 7;
}
