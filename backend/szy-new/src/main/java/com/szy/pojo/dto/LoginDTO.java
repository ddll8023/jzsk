package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 登录请求DTO
 */
@Schema(name = "登录请求", description = "用户登录请求参数")
@Data
public class LoginDTO {

    @Schema(description = "用户名", example = "admin01", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "Jzsk@123456", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
