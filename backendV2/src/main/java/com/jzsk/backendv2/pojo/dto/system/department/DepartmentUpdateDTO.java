package com.jzsk.backendv2.pojo.dto.system.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 部门更新请求
 * 用途：更新部门信息的请求参数
 */
@Data
@Schema(name = "部门更新请求", description = "更新部门信息的请求参数")
public class DepartmentUpdateDTO {

    @Schema(description = "部门ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "部门ID不能为空")
    private Long id;

    @Schema(description = "部门名称", example = "技术部", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    private String departmentName;

    @Schema(description = "部门职责", example = "负责系统研发", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 500, message = "部门职责长度不能超过500个字符")
    private String departmentResponsibility;

    @Schema(description = "部门级别", example = "一级", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 50, message = "部门级别长度不能超过50个字符")
    private String level;

    @Schema(description = "所属公司", example = "总公司", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 100, message = "所属公司长度不能超过100个字符")
    private String company;
}
