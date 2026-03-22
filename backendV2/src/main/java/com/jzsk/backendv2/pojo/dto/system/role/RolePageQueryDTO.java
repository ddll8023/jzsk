package com.jzsk.backendv2.pojo.dto.system.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色分页查询请求
 * 用途：分页查询角色列表的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "角色分页查询请求", description = "分页查询角色列表的请求参数")
public class RolePageQueryDTO extends com.jzsk.backendv2.pojo.dto.BasePageQueryDTO {

    @Schema(description = "角色名称（模糊搜索）", example = "管理员")
    private String name;

    @Schema(description = "角色编码（模糊搜索）", example = "ADMIN")
    private String code;

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private String status;

    @Schema(description = "角色类型", example = "管理角色")
    private String type;
}
