package com.jzsk.backendv2.service.impl.auth;

import com.jzsk.backendv2.pojo.dto.auth.LoginRequestDTO;
import com.jzsk.backendv2.pojo.vo.auth.CurrentUserVO;
import com.jzsk.backendv2.pojo.vo.auth.LoginResponseVO;
import com.jzsk.backendv2.security.JwtTokenService;
import com.jzsk.backendv2.security.LoginUser;
import com.jzsk.backendv2.service.auth.AuthService;
import com.jzsk.backendv2.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 * 职责：提供用户登录认证和当前用户信息查询功能
 * 遵循 KISS 原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Override
    public LoginResponseVO login(LoginRequestDTO request) {
        log.info("用户登录，username={}", request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        LoginResponseVO response = LoginResponseVO.builder()
                .token(jwtTokenService.generateToken(loginUser))
                .user(toCurrentUser(loginUser))
                .build();
        log.info("用户登录成功，username={}", request.getUsername());
        return response;
    }

    @Override
    public CurrentUserVO getCurrentUser() {
        return toCurrentUser(SecurityUtils.getCurrentLoginUser());
    }

    private CurrentUserVO toCurrentUser(LoginUser loginUser) {
        return CurrentUserVO.builder()
                .userId(loginUser.getUserId())
                .username(loginUser.getUsername())
                .displayName(loginUser.getDisplayName())
                .authorities(SecurityUtils.toAuthorityNames(loginUser.getAuthorities()))
                .build();
    }
}
