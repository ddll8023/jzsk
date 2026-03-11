package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 修改密码请求DTO
 */
@Schema(name = "修改密码DTO", description = "修改密码请求参数")
@Data
public class UpdatePasswordDTO {

    @Schema(description = "原密码", example = "Jzsk@123456", required = true)
    @NotBlank(message = "原密码不能为空")
    private String currentPassword;

    @Schema(description = "新密码", example = "New@123456", required = true)
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    @Schema(description = "确认密码", example = "New@123456", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
