package com.jzsk.backendv2.controller.auth;

import com.jzsk.backendv2.pojo.dto.auth.LoginRequestDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.auth.CurrentUserVO;
import com.jzsk.backendv2.pojo.vo.auth.LoginResponseVO;
import com.jzsk.backendv2.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 认证控制器
 * 用途：提供用户登录的 RESTful API 接口
 * 遵循 KISS 原则：简单清晰的接口设计，只做请求转发
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/auth")
@Tag(name = "认证管理", description = "提供登录接口")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录", description = "根据用户名和密码完成登录认证并签发JWT令牌")
    @ApiResponse(responseCode = "200", description = "登录成功")
    @PostMapping("/login")
    public ResponseEntity<ApiResult<LoginResponseVO>> login(
            @Parameter(description = "登录请求参数", required = true)
            @Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(ApiResult.success(authService.login(request), "登录成功"));
    }

    @Operation(summary = "获取当前登录用户", description = "获取当前登录用户完整信息")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/current-user")
    public ResponseEntity<ApiResult<CurrentUserVO>> getCurrentUser() {
        return ResponseEntity.ok(ApiResult.success(authService.getCurrentUser(), "查询成功"));
    }
}
