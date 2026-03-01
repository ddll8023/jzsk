package com.szy.security;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 扩展的UserDetails实现，携带用户ID
 */
@Getter
public class AccountUser extends User {

    /** 用户ID */
    private final Long userId;

    public AccountUser(Long userId, String username, String password,
                       Collection<? extends org.springframework.security.core.GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }
}