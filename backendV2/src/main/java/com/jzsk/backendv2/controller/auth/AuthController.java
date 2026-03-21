package com.jzsk.backendv2.controller.auth;

import com.jzsk.backendv2.pojo.dto.auth.LoginRequestDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.auth.CurrentUserVO;
import com.jzsk.backendv2.pojo.vo.auth.LoginResponseVO;
import com.jzsk.backendv2.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/auth")
@Tag(name = "认证管理", description = "提供登录和当前用户信息接口")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录", description = "根据用户名和密码完成登录认证并签发JWT令牌")
    @ApiResponse(responseCode = "200", description = "登录成功")
    @PostMapping("/login")
    public ResponseEntity<ApiResult<LoginResponseVO>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(ApiResult.success(authService.login(request), "登录成功"));
    }

    @Operation(summary = "获取当前用户", description = "根据当前请求中的JWT令牌返回登录用户上下文")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/current-user")
    public ResponseEntity<ApiResult<CurrentUserVO>> currentUser() {
        return ResponseEntity.ok(ApiResult.success(authService.getCurrentUser(), "查询成功"));
    }
}
