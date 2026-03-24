package com.jzsk.backendv2.service.external;

import com.jzsk.backendv2.pojo.dto.external.latestmonitor.LatestMonitorStationDto;

/**
 * 外部监测平台服务接口
 * 职责：封装对外部GNSS位移监测平台的HTTP调用
 * 数据源：外部监测平台（带Bearer Token认证）
 */
public interface ExternalApiService {

    /**
     * 获取指定站点的最新GNSS位移监测数据
     *
     * @param projectId 项目ID
     * @param stationId 站点ID
     * @return 最新监测数据，调用失败或无数据时返回null
     */
    LatestMonitorStationDto getLatestMonitoringData(String projectId, String stationId);
}
