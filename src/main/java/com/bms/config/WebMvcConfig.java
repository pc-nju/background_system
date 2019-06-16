package com.bms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 咸鱼
 * @date 2019-06-06 20:00
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 拦截所有请求
        registry.addMapping("/**")
                // 容许返回cookie
                .allowCredentials(true)
                .allowedHeaders("*")
                // 容许所有来源（解决跨域问题）
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}
