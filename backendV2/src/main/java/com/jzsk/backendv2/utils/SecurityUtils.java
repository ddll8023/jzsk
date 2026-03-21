package com.jzsk.backendv2.utils;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static LoginUser getCurrentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return (LoginUser) authentication.getPrincipal();
    }

    public static String getCurrentUsername() {
        return getCurrentLoginUser().getUsername();
    }

    public static List<String> toAuthorityNames(Iterable<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return Collections.emptyList();
        }
        return toStream(authorities)
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private static java.util.stream.Stream<? extends GrantedAuthority> toStream(
            Iterable<? extends GrantedAuthority> authorities) {
        return authorities == null
                ? java.util.stream.Stream.empty()
                : java.util.stream.StreamSupport.stream(authorities.spliterator(), false);
    }
}
