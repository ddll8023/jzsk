package com.szy.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.common.lang.Const;
import com.szy.common.vo.FlowVO;
import com.szy.common.vo.WaterLevelVO;
import com.szy.entity.*;
import com.szy.mapper.FlowMapper;
import com.szy.mapper.MeasuringStationMapper;
import com.szy.mapper.WaterLevelMapper;
import com.szy.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@DS("gcdd")
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements FlowService {

    private static final Logger log = LoggerFactory.getLogger(FlowServiceImpl.class);

    @Autowired
    private ReservoirService reservoirService;
    @Autowired
    private WarningIndicatorSettingService warningIndicatorSettingService;
    @Autowired
    private WarningInformationService warningInformationService;
    @Autowired
    private MeasuringStationMapper measuringStationMapper;
    @Resource
    FlowMapper flowMapper;
    @Override
    public FlowVO getFlowStatistics(DateTime startTime, DateTime endTime, String mpCd) {
        //时间，设计洪水位，正常蓄水位，死水位，监测水位
        //获取到该监测点对应的设计洪水位，正常蓄水位，死水位
        List<DateTime> dateTimeList = getTimes(startTime, endTime, mpCd);
        List<Double> valueList = getValues(startTime, endTime, mpCd);
        return FlowVO
                .builder()
                .dateTimeList(dateTimeList)
                .valueList(valueList)
                .build();
    }

    /**
     * 导出全部内容到excel文件
     * @return java.util.List<com.szy.entity.WaterLevel>
     * @author admin
     * @date 2024/06/19 11:27
     */
    @Override
    public List<Flow> exportAll(DateTime start, DateTime end, String mpCd) {
        QueryWrapper<Flow> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(mpCd)) {
            wrapper.eq("MP_CD", mpCd);
        }
        if (start != null && end != null) {
            wrapper.between("TM", start, end); // 使用between代替gt和lt，更明确表示时间范围
        }
        // 添加排序条件，按照监测时间逆序
        wrapper.orderByAsc("TM");
        List<Flow> flows = flowMapper.selectList(wrapper);
        return flows;
    }

    @Override
    public void getDeterInformation(Flow flow) {
        Double levelValue = flow.getMpQ();
        MeasuringStation measuringStation = measuringStationMapper.getByCode(flow.getMpCd());
        if (measuringStation == null) {
            log.warn("No MeasuringStation found for code: {}", flow.getMpCd());
            return; // Exit if no station is found to prevent NullPointerException
        }
        WarningIndicatorSetting warningIndicatorSetting = warningIndicatorSettingService.getByPosition(measuringStation.getName() , Const.FLOW_LEVEL);
        if(warningIndicatorSetting != null){
            String content = "";
            String level = "";
            double upUpLimit = warningIndicatorSetting.getUpUpLimit();
            double upLimit = warningIndicatorSetting.getUpLimit();
            double lowLimit = warningIndicatorSetting.getLowLimit();
            double lowerLimit = warningIndicatorSetting.getLowerLimit();
            if (levelValue > upUpLimit) {
                content = String.format("流量高于上上限%.2f米", levelValue - upUpLimit);
                level = Const.SERIVOUS_WARNING;
            } else if (levelValue > upLimit) {
                content = String.format("流量超过上限%.2f米", levelValue - upLimit);
                level = Const.GENERAL_WARNING;
            } else if (levelValue > lowLimit) {
                content = Const.NORMAL_FLOWLEVEL;
            } else if (levelValue > lowerLimit) {
                content = String.format("流量低于下限%.2f米", lowLimit - levelValue);
                level = Const.GENERAL_WARNING;
            } else {
                content = String.format("流量低于下下限%.2f米", lowerLimit - levelValue);
                level = Const.SERIVOUS_WARNING;
            }
            if (!level.isEmpty()) {
                BigDecimal originalLongitude = warningIndicatorSetting.getLongitude();
                BigDecimal additionalValueLongitude = BigDecimal.valueOf(0.0019);
                BigDecimal newLongitude = originalLongitude.add(additionalValueLongitude);

//                BigDecimal originalLatitude = warningIndicatorSetting.getLatitude();
//                BigDecimal additionalValueLatitude = BigDecimal.valueOf(0.0015);
//                BigDecimal newLatitude = originalLatitude.add(additionalValueLatitude);

                WarningInformation warningInformation = WarningInformation
                        .builder()
                        .position(measuringStation.getName())
                        .project(Const.PROJECT_NAME)
                        .content(content)
                        .type(Const.FLOW_LEVEL)
                        .level(level)
                        .status(Const.UNLIFTED)
                        .longitude(newLongitude)
                        .latitude(warningIndicatorSetting.getLatitude())
                        .startTime(flow.getTm())
                        .createTime(DateTime.now())
                        .updateTime(DateTime.now())
                        .build();
                boolean flag = warningInformationService.getLastThirty(flow.getMpCd(), Const.FLOW_LEVEL, flow.getTm());
                if (flag){
                    warningInformationService.save(warningInformation);
                }
            }
        }
    }

    private List<Double> getValues(DateTime startTime, DateTime endTime, String mpCd){
        Map map = new HashMap();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("mpCd",mpCd);
        return flowMapper.getValuesByMap(map);
    }

    private List<DateTime> getTimes(DateTime startTime, DateTime endTime, String mpCd){
        Map map = new HashMap();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("MP_CD",mpCd);
        return flowMapper.getTimesByMap(map);
    }
}

