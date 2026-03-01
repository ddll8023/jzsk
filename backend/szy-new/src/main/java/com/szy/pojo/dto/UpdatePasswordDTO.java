package com.szy.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码请求DTO
 */
@Data
public class UpdatePasswordDTO {

    @NotBlank(message = "原密码不能为空")
    private String currentPassword;

    @NotBlank(message = "新密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}