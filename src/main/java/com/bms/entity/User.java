package com.bms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/12/13 22:36
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    private String telephone;
    @NotBlank
    private String address;
    private boolean enabled;
    @NotBlank
    private String username;
//    @JsonIgnore
    @NotBlank
    private String password;
    private String userface;
    private String remark;
    private List<Role> roles;
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(this.roles)) {
            this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        }
        return authorities;
    }

    /**
     * 账户是否过期
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /**
     * 账户是否被锁定
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * 账户是否认证过期
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    /**
     * 账户是否被禁用
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
