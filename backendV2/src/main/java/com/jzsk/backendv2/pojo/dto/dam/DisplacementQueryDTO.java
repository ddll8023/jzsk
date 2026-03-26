package com.jzsk.backendv2.pojo.dto.dam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 位移历史查询请求DTO
 * 用途：查询外部位移历史数据的筛选条件
 */
@Schema(name = "位移历史查询请求", description = "查询外部位移历史数据的筛选条件")
@Data
public class DisplacementQueryDTO {

    @Schema(description = "开始时间", example = "2026-03-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", example = "2026-03-26 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "传感器类型", example = "L1_GP")
    private String sensor;

    @Schema(description = "站点ID列表(逗号分隔)", example = "33210,33214")
    private String stationIds;

    @Schema(description = "项目ID", example = "1681")
    private Integer projectId;

    @Schema(description = "统计频率", example = "0")
    private Integer statsFreq;
}
