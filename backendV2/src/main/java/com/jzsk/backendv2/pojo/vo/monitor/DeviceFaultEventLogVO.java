package com.jzsk.backendv2.pojo.vo.monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 设备故障事件明细视图对象
 * 用途：返回给前端的故障事件时间线数据
 */
@Schema(name = "故障事件明细VO", description = "故障事件明细视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceFaultEventLogVO {

    @Schema(description = "事件ID", example = "1")
    private Long id;

    @Schema(description = "事件状态：abnormal/offline/online", example = "abnormal")
    private String eventStatus;

    @Schema(description = "事件类型：fault_start/status_change/fault_recover", example = "fault_start")
    private String eventType;

    @Schema(description = "事件详情", example = "采集超时")
    private String eventDetail;

    @Schema(description = "最后采集时间", example = "2026-05-07 09:00:00")
    private LocalDateTime lastCollectTime;

    @Schema(description = "事件发生时间", example = "2026-05-07 10:00:00")
    private LocalDateTime eventTime;
}
