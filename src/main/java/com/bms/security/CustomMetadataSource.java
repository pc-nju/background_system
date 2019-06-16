package com.bms.security;

import com.bms.entity.Menu;
import com.bms.service.MenuService;
import com.bms.util.FinalName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-03 22:06
 * 返回权限资源
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomMetadataSource implements FilterInvocationSecurityMetadataSource {
    private final MenuService menuService;
    /**
     * @param o {@link FilterInvocation}
     *     getAttributes方法返回本次访问需要的权限，可以有多个权限。在上面的实现中如果没有匹配的url直接返回
     * ROLE_LOGIN（登录），也就是没有配置权限的url默认都需要登录。
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        /*
         *     该操作中，涉及到一个优先级问题，比如我的地址是/employee/basic/hello,这个地址既能
         * 被/employee/**匹配，也能被/employee/basic/**匹配，这就要求我们从数据库查询的时候对数
         * 据进行排序，将/employee/basic/**类型的url pattern放在集合的前面去比较。
         */
        List<Menu> allMenus = menuService.getAllMenus();

        for (Menu menu : allMenus) {
            if (FinalName.ANT_PATH_MATCHER.match(menu.getUrl(), requestUrl)
                    && menu.getRoles().size() > 0) {
                int size = menu.getRoles().size();
                String[] roles = new String[size];
                for (int i = 0; i < roles.length; i++) {
                    roles[i] = menu.getRoles().get(i).getName();
                }
                return SecurityConfig.createList(roles);
            }
        }
        /*
         *     如果getAttributes(Object o)方法返回null的话，意味着当前这个请求不需要任何角色就
         * 能访问，甚至不需要登录。但是在我的整个业务中，并不存在这样的请求，我这里的要求是，
         * 所有未匹配到的路径，都是认证(登录)后可访问，因此我在这里返回一个ROLE_LOGIN的角色，
         * 这种角色在我的角色数据库中并不存在，因此我将在下一步的角色比对过程中特殊处理这种
         * 角色。
         */
        return SecurityConfig.createList(FinalName.ROLE_LOGIN);
    }
    /**
     *     getAllConfigAttributes方法如果返回了所有定义的权限资源，Spring Security会在启动时校验每个ConfigAttribute
     * 是否配置正确，不需要校验直接返回null。
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
    /**
     * supports方法返回类对象是否支持校验，web项目一般使用FilterInvocation来判断，或者直接返回true。
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
