package com.jzsk.backendv2.service.auth;

import com.jzsk.backendv2.pojo.dto.auth.LoginRequestDTO;
import com.jzsk.backendv2.pojo.vo.auth.CurrentUserVO;
import com.jzsk.backendv2.pojo.vo.auth.LoginResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "认证服务", description = "提供用户登录认证接口")
public interface AuthService {

    /**
     * 用户登录
     * @param request 登录请求参数（用户名和密码）
     * @return 登录响应，包含 JWT 令牌和用户信息
     */
    @Operation(summary = "用户登录", description = "根据用户名和密码完成登录认证并签发JWT令牌")
    LoginResponseVO login(LoginRequestDTO request);

    @Operation(summary = "获取当前登录用户", description = "获取当前登录用户完整信息")
    CurrentUserVO getCurrentUser();
}
