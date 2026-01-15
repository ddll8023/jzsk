package com.szy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.common.lang.Const;
import com.szy.common.vo.WaterLevelVO;
import com.szy.entity.*;
import com.szy.mapper.RiverCollectorMapper;
import com.szy.mapper.WaterLevelCollectorMapper;
import com.szy.mapper.WaterLevelMapper;
import com.szy.service.*;
import com.szy.util.TencentSMSServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.szy.common.lang.Const.RESERVIOR;

@Service
@Slf4j
@DS("gcdd")
public class WaterLevelServiceImpl extends ServiceImpl<WaterLevelMapper, WaterLevel> implements WaterLevelService {

    @Autowired
    private ReservoirService reservoirService;
    @Autowired
    private WarningIndicatorSettingService warningIndicatorSettingService;
    @Autowired
    private WarningInformationService warningInformationService;
    @Resource
    WaterLevelMapper waterLevelMapper;

    @Autowired
    private WaterLevelCollectorMapper waterLevelCollectorMapper;
    @Autowired
    private RiverCollectorMapper riverCollectorMapper;
    @Autowired
    private TencentSMSServiceUtil smsService;
    @Autowired
    private UserService userService;
    @Override
    public WaterLevelVO getWaterLevelStatistics(DateTime startTime, DateTime endTime, String position) {
        //时间，设计洪水位，正常蓄水位，死水位，监测水位
        //获取到该监测点对应的设计洪水位，正常蓄水位，死水位
        QueryWrapper<Reservoir> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(position)) {
            wrapper.eq("name", position);
        }
        // 使用 selectList 而不是 getOne
        List<Reservoir> reservoirList = reservoirService.list(wrapper);
        // 检查列表是否为空，并返回第一个元素（如果存在）
        if (reservoirList != null && !reservoirList.isEmpty()) {
            Reservoir reservoir = reservoirList.get(0);
            List<DateTime> dateTimeList = getTimes(startTime, endTime, position);
            List<Double> valueList = getValues(startTime, endTime, position);
            List<Double> designFloodList = IntStream.range(0, valueList.size())
                    .mapToObj(i -> reservoir.getDesignFloodLevel()) // 假设getDesignFloodLevel()是获取设计洪水位的方法
                    .collect(Collectors.toList());
            List<Double> normalStorageList = IntStream.range(0, valueList.size())
                    .mapToObj(i -> reservoir.getNormalStorageLevel()) // 假设getDesignFloodLevel()是获取设计洪水位的方法
                    .collect(Collectors.toList());
            List<Double> deadWaterList = IntStream.range(0, valueList.size())
                    .mapToObj(i -> reservoir.getDeadWaterLevel()) // 假设getDesignFloodLevel()是获取设计洪水位的方法
                    .collect(Collectors.toList());
            return WaterLevelVO
                    .builder()
                    .dateTimeList(dateTimeList)
                    .designFloodList(designFloodList)
                    .normalStorageList(normalStorageList)
                    .deadWaterList(deadWaterList)
                    .valueList(valueList)
                    .build();
        } else {
            return null; // 如果没有找到匹配的水库，返回 null
        }
    }

    /**
     * 导出全部内容到excel文件
     * @return java.util.List<com.szy.entity.WaterLevel>
     * @author admin
     * @date 2024/06/19 11:27
     */
    @Override
    public List<WaterLevel> exportAll(DateTime start, DateTime end, String position) {
        QueryWrapper<WaterLevel> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(position)) {
            wrapper.eq("position", position);
        }
        if (start != null && end != null) {
            wrapper.between("monitor_time", start, end); // 使用between代替gt和lt，更明确表示时间范围
        }
        // 添加排序条件，按照监测时间逆序
        wrapper.orderByAsc("monitor_time");
        List<WaterLevel> waterLevels = waterLevelMapper.selectList(wrapper);
        return waterLevels;
    }

    @Override
    public void getDeterInformation(WaterLevel waterLevel) {
        Double levelValue = waterLevel.getValue();
        WarningIndicatorSetting warningIndicatorSetting = warningIndicatorSettingService.getByPosition(waterLevel.getPosition() , Const.WATER_LEVEL);
        if(warningIndicatorSetting != null){
            String content = "";
            String level = "";
            double upUpLimit = warningIndicatorSetting.getUpUpLimit();
            double upLimit = warningIndicatorSetting.getUpLimit();
            double lowLimit = warningIndicatorSetting.getLowLimit();
            double lowerLimit = warningIndicatorSetting.getLowerLimit();
            if (levelValue > upUpLimit) {
                content = String.format("水位高于上上限%.2f米", levelValue - upUpLimit);
                level = Const.SERIVOUS_WARNING;
            } else if (levelValue > upLimit) {
                content = String.format("水位超过上限%.2f米", levelValue - upLimit);
                level = Const.GENERAL_WARNING;
            } else if (levelValue > lowLimit) {
                content = Const.NORMAL_WATERLEVEL;
            } else if (levelValue > lowerLimit) {
                content = String.format("水位低于下限%.2f米", lowLimit - levelValue);
                level = Const.GENERAL_WARNING;
            } else {
                content = String.format("水位低于下下限%.2f米", lowerLimit - levelValue);
                level = Const.SERIVOUS_WARNING;
            }
            if (!level.isEmpty()) {
                BigDecimal originalLongitude = warningIndicatorSetting.getLongitude();
                BigDecimal additionalValueLongitude = BigDecimal.valueOf(0.001);
                BigDecimal newLongitude = originalLongitude.add(additionalValueLongitude);

                BigDecimal originalLatitude = warningIndicatorSetting.getLatitude();
                BigDecimal additionalValueLatitude = BigDecimal.valueOf(0.0015);
                BigDecimal newLatitude = originalLatitude.add(additionalValueLatitude);

                WarningInformation warningInformation = WarningInformation
                        .builder()
                        .position(waterLevel.getPosition())
                        .project(Const.PROJECT_NAME)
                        .content(content)
                        .type(Const.WATER_LEVEL)
                        .level(level)
                        .status(Const.UNLIFTED)
                        .longitude(newLongitude)
                        .latitude(newLatitude)
                        .startTime(waterLevel.getMonitorTime())
                        .createTime(DateTime.now())
                        .updateTime(DateTime.now())
                        .build();
                // 如果过去30分钟内没有该预警信息，则进行添加
                boolean flag = warningInformationService.getLastThirty(waterLevel.getPosition(), Const.WATER_LEVEL, waterLevel.getMonitorTime());
                if (flag){
                    warningInformationService.save(warningInformation);
                }
            }
        }
    }

    @Override
    public void syncLevelData() {
        // 查询最新的水位监测记录
        WaterLevelCollector collector = waterLevelCollectorMapper.selectLastRecord();
        if (collector != null) {
            WaterLevel waterLevel = new WaterLevel();
            // 设置特定的属性值
            waterLevel.setPosition(RESERVIOR);
            waterLevel.setCode(collector.getMpCd());
            waterLevel.setValue(collector.getMpZ());
            waterLevel.setMonitorTime(collector.getTm());
            // 插入水位数据
            try {
                waterLevelMapper.insert(waterLevel);
                log.info("已获取最新的水位数据");
            } catch (Exception e) {
                // 处理插入失败的情况，例如违反唯一索引约束
                log.error("未获取到最新的水位数据", e);
            }
        } else {
            log.info("暂无水位数据");
        }
    }

    private List<Double> getValues(DateTime startTime, DateTime endTime, String position){
        Map map = new HashMap();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("position",position);
        return waterLevelMapper.getValuesByMap(map);
    }

    private List<DateTime> getTimes(DateTime startTime, DateTime endTime, String position){
        Map map = new HashMap();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("position",position);
        return waterLevelMapper.getTimesByMap(map);
    }
}
