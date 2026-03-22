package com.jzsk.backendv2.pojo.dto.system.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户创建请求
 * 用途：创建新用户的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "用户创建请求", description = "创建新用户的请求参数")
public class UserCreateDTO extends UserBaseDTO {

    @Schema(description = "用户名", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50个字符之间")
    private String username;

    @Schema(description = "初始密码", example = "123456", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(min = 6, max = 50, message = "密码长度必须在6-50个字符之间")
    private String password;

    @Schema(description = "分配的角色ID列表", example = "[1, 2]")
    private List<Long> roleIds;
}
