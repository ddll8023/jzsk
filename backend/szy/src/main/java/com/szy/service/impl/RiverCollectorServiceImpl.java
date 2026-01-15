package com.szy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.*;
import com.szy.mapper.FlowMapper;
import com.szy.mapper.RiverCollectorMapper;
import com.szy.mapper.WaterLevelCollectorMapper;
import com.szy.mapper.WaterLevelMapper;
import com.szy.service.FlowService;
import com.szy.service.RiverCollectorService;
import com.szy.service.WaterLevelCollectorService;
import com.szy.service.WaterLevelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import static com.szy.common.lang.Const.*;

@Service
@DS("collector")
@Slf4j
public class RiverCollectorServiceImpl extends ServiceImpl<RiverCollectorMapper, RiverCollector> implements RiverCollectorService {
    @Autowired
    private RiverCollectorMapper riverCollectorMapper;
    @Autowired
    private FlowMapper flowMapper;
    @Autowired
    private WaterLevelMapper waterLevelMapper;
    @Autowired
    private FlowService flowService;
    @Autowired
    private WaterLevelService waterLevelService;
    @Override
    public void syncFlowData() {
        // 查询最新的流量监测记录
        List<RiverCollector> collectorList = riverCollectorMapper.selectLastRecord();
        if (collectorList.isEmpty()) {
            log.info("未获取到所有站点的数据");
        }
        for (RiverCollector collector : collectorList) {
            if (collector != null && collector.getQ1() != null) {
                Flow flow = new Flow();
                // 站码，监测时间和 瞬时流量 累计流量
                flow.setMpCd(collector.getStcd());
                if (collector.getQ1() <= 0) {
                    flow.setMpQ((double) 0);
                }
                else {
                    flow.setMpQ(collector.getQ1());
                }
                // 插入累计流量
                if (collector.getSl1() != null) {
                    flow.setAccW(collector.getSl1());
                }
                else {
                    flow.setAccW((double) 0);
                }
                flow.setTm(collector.getTm());

                LocalDateTime flowTm = Instant.ofEpochMilli(collector.getTm().getTime())
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDateTime();

                // 查询是否存在相同mpCd和tm的记录
                LambdaQueryWrapper<Flow> flowQueryWrapper = new LambdaQueryWrapper<>();
                flowQueryWrapper.eq(Flow::getMpCd, flow.getMpCd())
                                .eq(Flow::getTm, flowTm);
                Flow existingFlow = flowService.getOne(flowQueryWrapper);

                if (existingFlow != null) {
                    flow.setId(existingFlow.getId()); // 设置ID，以便saveOrUpdate执行更新
                }
                flowService.saveOrUpdate(flow);
                flowService.getDeterInformation(flow);
                log.info("已处理流量站" + collector.getStcd() + "的数据。");

            }
            else {
                log.info("暂无流量数据");
            }
            if (collector != null && collector.getZ1() != null) {
                WaterLevel waterLevel = new WaterLevel();
                // 站点，编码，水位，监测时间
                waterLevel.setPosition(RESERVIOR);
                waterLevel.setCode(collector.getStcd());
                waterLevel.setValue(collector.getZ1());
                waterLevel.setMonitorTime(collector.getTm());

                LocalDateTime waterLevelTm = Instant.ofEpochMilli(collector.getTm().getTime())
                                                    .atZone(ZoneId.systemDefault())
                                                    .toLocalDateTime();

                // 查询是否存在相同code和monitorTime的记录
                LambdaQueryWrapper<WaterLevel> waterLevelQueryWrapper = new LambdaQueryWrapper<>();
                waterLevelQueryWrapper.eq(WaterLevel::getCode, waterLevel.getCode())
                                      .eq(WaterLevel::getMonitorTime, waterLevelTm);
                WaterLevel existingWaterLevel = waterLevelService.getOne(waterLevelQueryWrapper);

                if (existingWaterLevel != null) {
                    waterLevel.setId(existingWaterLevel.getId()); // 设置ID，以便saveOrUpdate执行更新
                }
                waterLevelService.saveOrUpdate(waterLevel);
                waterLevelService.getDeterInformation(waterLevel);
                log.info("已处理荆竹水库水位站的数据。");

            }
        }
    }
}
