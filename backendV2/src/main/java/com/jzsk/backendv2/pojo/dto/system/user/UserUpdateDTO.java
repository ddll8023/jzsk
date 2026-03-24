package com.jzsk.backendv2.pojo.dto.system.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户更新请求
 * 用途：更新用户信息的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "用户更新请求", description = "更新用户信息的请求参数")
public class UserUpdateDTO extends UserBaseDTO {

    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "用户名", example = "admin")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50个字符之间")
    private String username;

    @Schema(description = "分配的角色ID列表", example = "[1, 2]")
    private List<Long> roleIds;
}
