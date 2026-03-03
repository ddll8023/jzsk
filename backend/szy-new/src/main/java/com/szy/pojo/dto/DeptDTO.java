package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 部门请求参数
 */
@Schema(name = "部门DTO", description = "部门创建/更新请求参数")
@Data
public class DeptDTO {

    @Schema(description = "部门ID", example = "1", required = false)
    private Long id;

    @Schema(description = "部门名称", example = "技术部", required = true)
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    private String departmentName;

    @Schema(description = "部门职责", example = "负责系统开发与维护", required = true)
    @NotBlank(message = "部门职责不能为空")
    @Size(max = 500, message = "部门职责长度不能超过500个字符")
    private String departmentResponsibility;

    @Schema(description = "部门级别", example = "一级部门", required = false)
    @Size(max = 50, message = "部门级别长度不能超过50个字符")
    private String level;

    @Schema(description = "所属公司", example = "水利局", required = false)
    @Size(max = 100, message = "所属公司长度不能超过100个字符")
    private String company;
}
