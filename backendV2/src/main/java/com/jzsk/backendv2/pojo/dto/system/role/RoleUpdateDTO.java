package com.jzsk.backendv2.pojo.dto.system.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 角色更新请求
 * 用途：更新角色信息的请求参数
 */
@Data
@Schema(name = "角色更新请求", description = "更新角色信息的请求参数")
public class RoleUpdateDTO {

    @Schema(description = "角色ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色ID不能为空")
    private Long id;

    @Schema(description = "角色名称", example = "系统管理员")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    private String name;

    @Schema(description = "角色编码", example = "ADMIN")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    private String code;

    @Schema(description = "角色备注", example = "系统管理员角色")
    @Size(max = 200, message = "角色备注长度不能超过200个字符")
    private String note;

    @Schema(description = "角色类型", example = "管理角色")
    private String type;

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private String status;

    @Schema(description = "排序号", example = "1")
    private Integer sort;
}
