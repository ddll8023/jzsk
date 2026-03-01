package com.szy.controller;

import com.szy.common.lang.Result;
import com.szy.pojo.dto.LoginDTO;
import com.szy.security.AccountUser;
import com.szy.security.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@Api(tags = "认证管理")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@Validated LoginDTO loginDTO) {
        // 表单登录方式
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        // 执行认证
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT
        AccountUser accountUser = (AccountUser) authentication.getPrincipal();
        String jwt = jwtTokenUtil.generateToken(accountUser);

        // 返回token
        Map<String, Object> data = new HashMap<>();
        data.put("token", jwt);
        return Result.success(data);
    }

    /**
     * 用户登出
     */
    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result logout() {
        SecurityContextHolder.clearContext();
        return Result.success("退出成功");
    }
}