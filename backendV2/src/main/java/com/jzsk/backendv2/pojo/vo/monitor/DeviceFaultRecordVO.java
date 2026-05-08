package com.jzsk.backendv2.pojo.vo.monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 设备故障记录视图对象
 * 用途: 返回给前端的历史故障记录数据
 */
@Schema(name = "设备故障记录VO", description = "设备故障记录视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceFaultRecordVO {

    @Schema(description = "故障记录ID", example = "1")
    private Long id;

    @Schema(description = "设备类型", example = "gnss")
    private String deviceType;

    @Schema(description = "设备编码", example = "33210")
    private String deviceCode;

    @Schema(description = "设备名称", example = "LJ1-1")
    private String deviceName;

    @Schema(description = "首次故障状态", example = "abnormal")
    private String firstFaultStatus;

    @Schema(description = "当前故障状态", example = "offline")
    private String currentFaultStatus;

    @Schema(description = "故障类型", example = "collect_timeout")
    private String faultType;

    @Schema(description = "故障详情", example = "采集超时")
    private String faultDetail;

    @Schema(description = "最后采集时间", example = "2026-05-07 09:00:00")
    private LocalDateTime lastCollectTime;

    @Schema(description = "故障开始时间", example = "2026-05-07 10:00:00")
    private LocalDateTime startTime;

    @Schema(description = "故障解除时间", example = "2026-05-07 12:00:00")
    private LocalDateTime endTime;

    @Schema(description = "故障持续分钟数", example = "120")
    private Integer durationMinutes;

    @Schema(description = "处理状态", example = "resolved")
    private String processStatus;
}
