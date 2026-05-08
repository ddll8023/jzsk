package com.jzsk.backendv2.pojo.vo.monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 设备状态视图对象
 * 用途: 返回给前端的设备监控状态数据
 */
@Schema(name = "设备状态VO", description = "设备监控状态视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatusVO {

    @Schema(description = "设备编码", example = "33210")
    private String code;

    @Schema(description = "设备名称", example = "LJ1-1")
    private String name;

    @Schema(description = "设备类型: gnss/rain/seepage", example = "gnss")
    private String type;

    @Schema(description = "设备状态: online/offline/abnormal", example = "online")
    private String status;

    @Schema(description = "最后采集时间", example = "2026-04-29 10:00:00")
    private LocalDateTime lastCollectTime;

    @Schema(description = "设备详情（如位移值、水位值等）", example = "合位移: 2.34mm")
    private String detail;
}
