package com.jzsk.backendv2.pojo.dto.system.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 密码修改请求
 * 用途：修改当前用户密码的请求参数
 */
@Data
@Schema(name = "密码修改请求", description = "修改当前用户密码的请求参数")
public class UserPasswordUpdateDTO {

    @Schema(description = "旧密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @Schema(description = "新密码", example = "abcdef", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 50, message = "新密码长度必须在6-50个字符之间")
    private String newPassword;
}
