package com.szy.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码参数
 */
@Data
public class ResetPasswordDTO {

    /** 旧密码 */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}