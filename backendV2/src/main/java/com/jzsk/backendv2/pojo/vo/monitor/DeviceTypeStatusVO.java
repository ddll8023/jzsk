package com.jzsk.backendv2.pojo.vo.monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 单类型设备状态视图对象
 * 用途: 返回单个设备类型（GNSS/雨水情/渗流渗压）的统计和设备列表
 */
@Schema(name = "设备类型状态VO", description = "单个设备类型的状态统计和设备列表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTypeStatusVO {

    @Schema(description = "该类型设备统计")
    private DeviceMonitorOverviewVO.Stats stats;

    @Schema(description = "该类型设备列表")
    private List<DeviceStatusVO> devices;
}
