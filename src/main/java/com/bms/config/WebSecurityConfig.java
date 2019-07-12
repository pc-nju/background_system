package com.bms.config;

import com.bms.security.CustomMetadataSource;
import com.bms.security.DbUserDetailServiceImpl;
import com.bms.security.UrlAccessDecisionManager;
import com.bms.security.handler.DeniedHandler;
import com.bms.security.handler.FailureHandler;
import com.bms.security.handler.LogoutHandler;
import com.bms.security.handler.SuccessHandler;
import com.bms.util.FinalName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author 咸鱼
 * @date 2019-06-03 20:55
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final DbUserDetailServiceImpl userDetailService;
    private final CustomMetadataSource metadataSource;
    private final UrlAccessDecisionManager urlAccessDecisionManager;
    private final SuccessHandler successHandler;
    private final FailureHandler failureHandler;
    private final DeniedHandler deniedHandler;
    private final LogoutHandler logoutHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置 UserDetailsService 实现类，完成用户认证（用户名、密码匹配）
        auth.userDetailsService(userDetailService)
                // 配置密码加密方法（不配置会报错，同时再将密码存入数据库时，也需要使用该加密方式）
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/index.html", "/static/**", "/login_p");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        // 配置访问路径应该具备的权限集合
                        object.setSecurityMetadataSource(metadataSource);
                        // 配置权限认证管理器
                        object.setAccessDecisionManager(urlAccessDecisionManager);
                        return object;
                    }
                })
                .and()
                /*
                 * 注意点一：
                 *     loginPage("/login_p")：一般配置的是登录页面，但由于这里是前后端分离项目，所以这里这是一个请求路径，
                 *                            返回的是Json数据。这也就是为什么当我们访问需要登录权限的界面时，未跳转到登
                 *                            录页面的原因。
                 * 注意点二：
                 *     loginProcessingUrl("/login")：该配置项就是处理登录请求的，Spring Security框架会拦截到该请求，也就
                 *                                   是在 AbstractAuthenticationProcessingFilter.doFilter() 中进行处理。
                 *                                   这样，我们就无需自定义拦截“/login”请求了！
                 */
                .formLogin().loginPage("/login_p").loginProcessingUrl("/login")
                // 规定前端传递过来的用户名、密码的字段
                .usernameParameter("username").passwordParameter("password")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
                .and()
                // 注销的请求路径，这里是SpringSecurity框架规定的请求路径，写成该样式，才可由框架自行进行处理
                .logout().logoutUrl("/logout")
                .logoutSuccessHandler(logoutHandler)
                .permitAll()
                .and()
                //关闭 csfr，解决POST报403错误
                .csrf().disable()
                .exceptionHandling().accessDeniedHandler(deniedHandler);
                http.sessionManagement().maximumSessions(1).expiredUrl("/login");
    }
}
