package com.jzsk.backendv2.service.monitor;

import com.jzsk.backendv2.pojo.vo.monitor.DeviceTypeStatusVO;

/**
 * 设备监控服务接口
 * 职责: 提供设备状态检测和监控数据查询功能
 */
public interface DeviceMonitorService {

    /**
     * 获取GNSS设备状态
     *
     * @return GNSS设备统计和设备列表
     */
    DeviceTypeStatusVO getGnssStatus();

    /**
     * 获取雨水情设备状态
     *
     * @return 雨水情设备统计和设备列表
     */
    DeviceTypeStatusVO getRainStatus();

    /**
     * 获取渗流渗压设备状态
     *
     * @return 渗流渗压设备统计和设备列表
     */
    DeviceTypeStatusVO getSeepageStatus();
}
