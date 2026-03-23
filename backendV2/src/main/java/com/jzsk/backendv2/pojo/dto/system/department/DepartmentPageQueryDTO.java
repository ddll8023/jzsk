package com.jzsk.backendv2.pojo.dto.system.department;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门分页查询请求
 * 用途：分页查询部门列表的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "部门分页查询请求", description = "分页查询部门列表的请求参数")
public class DepartmentPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "部门名称（模糊搜索）", example = "技术部")
    private String name;

    @Schema(description = "部门级别", example = "一级")
    private String level;

    @Schema(description = "所属公司", example = "总公司")
    private String company;
}
