package com.jzsk.backendv2.pojo.dto.system.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色菜单分配请求
 * 用途：给角色分配菜单权限的请求参数
 */
@Data
@Schema(name = "角色菜单分配请求", description = "给角色分配菜单权限的请求参数")
public class RoleMenuAssignDTO {

    @Schema(description = "角色ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Schema(description = "菜单ID列表", example = "[1, 2, 3]", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "菜单ID列表不能为空")
    private List<Long> menuIds;
}
