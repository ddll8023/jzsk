package com.jzsk.backendv2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jzsk.backendv2.pojo.entity.mcu.DataNewEntity;
import com.jzsk.backendv2.pojo.entity.mcu.SensorPointEntity;
import com.jzsk.backendv2.pojo.entity.monitor.StPptnHourEntity;
import com.jzsk.backendv2.pojo.entity.monitor.StRiversREntity;

import java.util.List;

/**
 * 监测数据服务接口
 * 职责：封装各监测数据源的查询逻辑，供自动预警任务调用
 * 遵循分层原则：Task 层不直接依赖 Mapper，通过 Service 间接访问数据
 */
public interface MonitorDataService {

    /**
     * 查询所有测点的最新传感器数据（MCU数据）
     *
     * @return 各测点最新数据列表
     */
    List<DataNewEntity> getAllLatestMcuData();

    /**
     * 根据测点ID查询测点名称
     *
     * @param pointId 测点编号
     * @return 测点名称，查询失败返回null
     */
    String getPointName(String pointId);

    /**
     * 查询指定测站的最新水位数据
     *
     * @param stcd 测站编码
     * @return 最新水位数据
     */
    StRiversREntity getLatestWaterLevel(String stcd);

    /**
     * 查询指定测站的最新雨量数据
     *
     * @param stcd 测站编码
     * @return 最新雨量数据
     */
    StPptnHourEntity getLatestRainfall(String stcd);

    /**
     * 调用外部GNSS监测API获取指定站点的最新位移数据
     *
     * @param stationId 站点ID
     * @return API返回的JSON数据
     */
    JsonNode getGnssLatestData(Long stationId);
}
