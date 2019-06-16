package com.bms.security;

import com.bms.util.FinalName;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author 咸鱼
 * @date 2019-06-04 18:50
 * 权限认证
 */
@Component
public class UrlAccessDecisionManager implements AccessDecisionManager {
    /**
     *
     * @param authentication 保存了当前登录用户的角色信息
     * @param o 是 {@link FilterInvocation} 对象，可以得到 request 等 web 资源
     * @param collection 是{@link CustomMetadataSource}中的 getAttributes() 方法传来的，表示当前请求需要的角色（可能有多个）。
     * @throws AccessDeniedException 权限认证失败异常
     * @throws InsufficientAuthenticationException 权限不足异常
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        // 遍历角色
        for (ConfigAttribute configAttribute : collection) {
            // 获取当前遍历对象的角色
            String needRole = configAttribute.getAttribute();
            if (FinalName.ROLE_LOGIN.equals(needRole)) {
                /*
                 *     如果当前请求URL需要的权限为ROLE_LOGIN则表示登录即可访问，和角色没有关系，此时我需要判断authentication
                 * 是不是AnonymousAuthenticationToken的一个实例：
                 *     如果是，则表示当前用户没有登录，没有登录就抛一个BadCredentialsException异常；
                 *     如果不是，则说明是已经登录过的用户，只是当前请求路径需要登录权限而已，所以就直接返回，则这个请求将被成功执行。
                 */
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException(FinalName.UN_LOGIN);
                } else {
                    // 直接放行
                    return;
                }
            }
            /*
             *     遍历collection，同时查看当前用户的角色列表中是否具备需要的权限，如果具备就直接返回，否则就抛异常。
             *     里涉及到一个all和any的问题：假设当前用户具备角色A、角色B，当前请求需要角色B、角色C，那么是要当前用
             * 户要包含所有请求角色才算授权成功还是只要包含一个就算授权成功？我这里采用了第二种方案，即只要包含一个即可。
             * 可根据自己的实际情况调整decide方法中的逻辑。
             */
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (needRole.equals(authority.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException(FinalName.INSUFFICIENT_AUTH);
    }
    /**
     *     getAllConfigAttributes方法如果返回了所有定义的权限资源，Spring Security会在启动时校
     * 验每个ConfigAttribute是否配置正确，不需要校验直接返回null。
     */
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }
    /**
     *     supports方法返回类对象是否支持校验，web项目一般使用FilterInvocation来判断，
     * 或者直接返回true。
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
