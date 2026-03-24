package com.jzsk.backendv2.pojo.dto.engineering;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 养护记录分页查询请求
 * 用途：分页查询养护记录列表，支持按工程名称、时间范围筛选
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "养护记录分页查询请求", description = "分页查询养护记录列表")
public class MaintenanceRecordsPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "工程名称（模糊搜索）", example = "水库", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    @Schema(description = "开始维护时间（起）", example = "2025-01-01 00:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "开始维护时间（止）", example = "2025-12-31 23:59:59", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
