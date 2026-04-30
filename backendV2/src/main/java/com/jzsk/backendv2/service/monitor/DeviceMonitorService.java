package com.jzsk.backendv2.service.monitor;

import com.jzsk.backendv2.pojo.vo.monitor.DeviceMonitorOverviewVO;

import io.swagger.v3.oas.annotations.Operation;

/**
 * 设备监控服务接口
 * 职责: 提供设备状态检测和监控数据查询功能
 */
public interface DeviceMonitorService {

    /**
     * 获取所有设备监控状态
     * 聚合GNSS、雨水情、渗流渗压三种设备的状态信息
     *
     * @return 设备监控总览（含统计和设备列表）
     */
    @Operation(summary = "获取所有设备监控状态", description = "聚合三种设备类型的在线/离线/采集异常状态")
    DeviceMonitorOverviewVO getDeviceMonitorStatus();
}
