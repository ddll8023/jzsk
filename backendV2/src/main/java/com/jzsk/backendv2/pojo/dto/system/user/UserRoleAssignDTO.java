package com.jzsk.backendv2.pojo.dto.system.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 用户角色分配请求
 * 用途：给用户分配角色的请求参数
 */
@Data
@Schema(name = "用户角色分配请求", description = "给用户分配角色的请求参数")
public class UserRoleAssignDTO {

    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "角色ID列表", example = "[1, 2]", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "角色ID列表不能为空")
    private List<Long> roleIds;
}
