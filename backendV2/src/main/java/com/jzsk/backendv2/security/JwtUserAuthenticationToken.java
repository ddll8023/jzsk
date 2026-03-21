package com.jzsk.backendv2.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtUserAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public JwtUserAuthenticationToken(Object principal,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(principal, null, authorities);
    }
}
