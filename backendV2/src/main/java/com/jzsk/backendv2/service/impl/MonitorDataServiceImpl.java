package com.jzsk.backendv2.service.impl;

import com.jzsk.backendv2.mapper.mcu.DataNewMapper;
import com.jzsk.backendv2.mapper.mcu.SensorPointMapper;
import com.jzsk.backendv2.mapper.monitor.StPptnHourMapper;
import com.jzsk.backendv2.mapper.monitor.StRiversRMapper;
import com.jzsk.backendv2.pojo.entity.mcu.DataNewEntity;
import com.jzsk.backendv2.pojo.entity.mcu.SensorPointEntity;
import com.jzsk.backendv2.pojo.entity.monitor.StPptnHourEntity;
import com.jzsk.backendv2.pojo.entity.monitor.StRiversREntity;
import com.jzsk.backendv2.service.MonitorDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 监测数据服务实现类
 * 职责：封装各监测数据源的查询逻辑，供自动预警任务调用
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorDataServiceImpl implements MonitorDataService {

    private final DataNewMapper dataNewMapper;
    private final SensorPointMapper sensorPointMapper;
    private final StRiversRMapper stRiversRMapper;
    private final StPptnHourMapper stPptnHourMapper;

    @Override
    public List<DataNewEntity> getAllLatestMcuData() {
        return dataNewMapper.selectLatestForAllPoints();
    }

    @Override
    public String getPointName(String pointId) {
        try {
            SensorPointEntity sp = sensorPointMapper.selectById(Long.valueOf(pointId));
            return sp != null ? sp.getName() : null;
        } catch (Exception e) {
            log.warn("[MonitorDataService] 查询测点名称失败 pointId={}: {}", pointId, e.getMessage());
            return null;
        }
    }

    @Override
    public StRiversREntity getLatestWaterLevel(String stcd) {
        return stRiversRMapper.selectLatestByStcd(stcd);
    }

    @Override
    public StPptnHourEntity getLatestRainfall(String stcd) {
        return stPptnHourMapper.selectLatestByStcd(stcd);
    }
}
