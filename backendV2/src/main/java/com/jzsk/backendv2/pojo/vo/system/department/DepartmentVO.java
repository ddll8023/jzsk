package com.jzsk.backendv2.pojo.vo.system.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 部门VO
 * 用途：部门视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "部门VO", description = "部门视图对象")
public class DepartmentVO {

    @Schema(description = "部门ID", example = "1")
    private Long id;

    @Schema(description = "部门名称", example = "技术部")
    private String departmentName;

    @Schema(description = "部门职责", example = "负责系统研发")
    private String departmentResponsibility;

    @Schema(description = "部门级别", example = "一级")
    private String level;

    @Schema(description = "所属公司", example = "总公司")
    private String company;

    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "修改时间", example = "2024-01-01 10:00:00")
    private LocalDateTime updateTime;
}
