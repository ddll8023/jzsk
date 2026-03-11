package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色查询参数
 */
@Schema(name = "角色查询DTO", description = "角色列表查询请求参数")
@Data
public class RoleQueryDTO {

    @Schema(description = "当前页", example = "1", required = false)
    private Integer currentPage;

    @Schema(description = "每页条数", example = "10", required = false)
    private Integer pageSize;

    @Schema(description = "角色名称（模糊搜索）", example = "管理员", required = false)
    private String name;
}
