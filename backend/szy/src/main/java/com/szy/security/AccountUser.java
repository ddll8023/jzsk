package com.szy.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;

public class AccountUser implements UserDetails {
    private Long userId;

    private String password;

    private final String username;

    /**
     * 用户的授权信息
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * 用户是否过期
     */
    private final boolean accountNonExpired;

    /**
     * 用户是否被锁定
     */
    private final boolean accountNonLocked;

    /**
     * 用户凭证是否过期
     */
    private final boolean credentialsNonExpired;

    /**
     * 用户是否被启用
     */
    private final boolean enabled;

    public AccountUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(userId, username, password, true, true, true, true, authorities);
    }

    public AccountUser(Long userId, String username, String password, boolean enabled, boolean accountNonExpired,
                       boolean credentialsNonExpired, boolean accountNonLocked,
                       Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(username != null && !"".equals(username) && password != null,
                "Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }



    /**
     *
     * @return 权限
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     *
     * @return 密码
     */

    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
