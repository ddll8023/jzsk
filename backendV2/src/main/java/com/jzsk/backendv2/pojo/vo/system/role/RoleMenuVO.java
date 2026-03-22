package com.jzsk.backendv2.pojo.vo.system.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色菜单ID列表VO
 * 用途：获取角色已分配的菜单ID列表及完整菜单树
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "角色菜单ID列表VO", description = "角色已分配的菜单ID列表及完整菜单树")
public class RoleMenuVO {

    @Schema(description = "角色ID", example = "1")
    private Long roleId;

    @Schema(description = "菜单ID列表", example = "[1, 2, 3]")
    private List<Long> menuIds;

    @Schema(description = "菜单树", example = "[{...}]")
    private List<MenuTreeVO> menuTree;
}
