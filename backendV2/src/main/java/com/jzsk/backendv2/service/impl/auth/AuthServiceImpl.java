package com.jzsk.backendv2.service.impl.auth;

import com.jzsk.backendv2.pojo.dto.auth.LoginRequestDTO;
import com.jzsk.backendv2.pojo.vo.auth.CurrentUserVO;
import com.jzsk.backendv2.pojo.vo.auth.LoginResponseVO;
import com.jzsk.backendv2.security.JwtTokenService;
import com.jzsk.backendv2.security.LoginUser;
import com.jzsk.backendv2.service.auth.AuthService;
import com.jzsk.backendv2.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Override
    public LoginResponseVO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return LoginResponseVO.builder()
                .token(jwtTokenService.generateToken(loginUser))
                .user(toCurrentUser(loginUser))
                .build();
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
