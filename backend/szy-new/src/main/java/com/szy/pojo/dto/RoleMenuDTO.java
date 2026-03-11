package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 角色菜单分配DTO
 */
@Data
@Schema(description = "角色菜单分配DTO")
public class RoleMenuDTO {

    @Schema(description = "角色ID", example = "1")
    private Long roleId;

    @Schema(description = "菜单ID列表", example = "[1, 2, 3]")
    private List<Long> menuIds;
}
