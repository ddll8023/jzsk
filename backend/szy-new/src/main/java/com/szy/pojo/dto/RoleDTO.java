package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 角色请求参数
 */
@Schema(name = "角色DTO", description = "角色创建/更新请求参数")
@Data
public class RoleDTO {

    @Schema(description = "角色ID", example = "1", required = false)
    private Long id;

    @Schema(description = "角色名称", example = "系统管理员", required = true)
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    private String name;

    @Schema(description = "角色编码", example = "sys", required = true)
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    private String code;

    @Schema(description = "备注", example = "系统最高权限角色", required = false)
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String note;

    /**
     * 角色描述（前端使用description，后端映射到note）
     */
    @Schema(description = "角色描述", example = "系统最高权限角色", required = false)
    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;

    @Schema(description = "状态", example = "1", required = false)
    private String status;

    @Schema(description = "菜单ID列表", example = "[1,2,3]", required = false)
    private List<Long> menuIds;
}
