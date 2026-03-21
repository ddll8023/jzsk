package com.jzsk.backendv2.pojo.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(name = "登录请求", description = "登录接口请求参数")
public class LoginRequestDTO {

    @Schema(description = "用户名", example = "admin", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "admin123", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
