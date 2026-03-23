package com.jzsk.backendv2.pojo.dto.system.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 部门创建请求
 * 用途：创建新部门的请求参数
 */
@Data
@Schema(name = "部门创建请求", description = "创建新部门的请求参数")
public class DepartmentCreateDTO {

    @Schema(description = "部门名称", example = "技术部", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    private String departmentName;

    @Schema(description = "部门职责", example = "负责系统研发", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "部门职责不能为空")
    @Size(max = 500, message = "部门职责长度不能超过500个字符")
    private String departmentResponsibility;

    @Schema(description = "部门级别", example = "一级", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 50, message = "部门级别长度不能超过50个字符")
    private String level;

    @Schema(description = "所属公司", example = "总公司", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 100, message = "所属公司长度不能超过100个字符")
    private String company;
}
