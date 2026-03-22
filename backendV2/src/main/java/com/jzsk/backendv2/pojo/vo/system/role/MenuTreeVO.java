package com.jzsk.backendv2.pojo.vo.system.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 菜单树VO
 * 用途：角色菜单权限分配时返回的菜单树结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "菜单树VO", description = "菜单树形结构")
public class MenuTreeVO {

    @Schema(description = "菜单ID", example = "1")
    private Long id;

    @Schema(description = "菜单名称", example = "系统管理")
    private String name;

    @Schema(description = "菜单编码", example = "SYSTEM")
    private String code;

    @Schema(description = "菜单路径", example = "/system")
    private String path;

    @Schema(description = "菜单图标", example = "fa fa-cog")
    private String icon;

    @Schema(description = "排序号", example = "1")
    private Integer orderNum;

    @Schema(description = "子菜单列表")
    private List<MenuTreeVO> children;
}
