package com.jzsk.backendv2.pojo.vo.system.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 角色VO
 * 用途：角色列表展示
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "角色VO", description = "角色视图对象")
public class RoleVO {

    @Schema(description = "角色ID", example = "1")
    private Long id;

    @Schema(description = "角色名称", example = "系统管理员")
    private String name;

    @Schema(description = "角色编码", example = "ADMIN")
    private String code;

    @Schema(description = "角色备注", example = "系统管理员角色")
    private String note;

    @Schema(description = "角色类型", example = "管理角色")
    private String type;

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private String status;

    @Schema(description = "排序号", example = "1")
    private Integer sort;

    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private Date createTime;

    @Schema(description = "修改时间", example = "2024-01-01 10:00:00")
    private Date updateTime;

    @Schema(description = "分配的角色菜单ID列表")
    private List<Long> menuIds;
}
