package com.jzsk.backendv2.pojo.dto.monitor;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备故障记录分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "设备故障记录分页查询参数", description = "设备故障记录分页查询参数")
public class DeviceFaultPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "设备类型：gnss/rain/seepage", example = "gnss")
    private String deviceType;

    @Schema(description = "故障状态：offline/abnormal", example = "offline")
    private String faultStatus;

    @Schema(description = "处理状态：active/resolved", example = "active")
    private String processStatus;

    @Schema(description = "关键词（设备名称或设备编码模糊查询）", example = "LJ1")
    private String keyword;

    @Schema(description = "故障开始时间-起始", example = "2026-05-01 00:00:00")
    private String startTime;

    @Schema(description = "故障开始时间-结束", example = "2026-05-07 23:59:59")
    private String endTime;
}
