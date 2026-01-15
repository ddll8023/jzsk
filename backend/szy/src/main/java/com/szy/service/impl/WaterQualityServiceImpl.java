package com.szy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.common.lang.Const;
import com.szy.common.vo.WaterQualityVO;
import com.szy.entity.*;
import com.szy.mapper.PumpMapper;
import com.szy.mapper.PumpStationMapper;
import com.szy.mapper.WaterQualityCollectorMapper;
import com.szy.mapper.WaterQualityMapper;
import com.szy.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.szy.common.lang.Const.*;


@Service
@Slf4j
@DS("gcdd")
public class WaterQualityServiceImpl extends ServiceImpl<WaterQualityMapper, WaterQuality> implements WaterQualityService {
    @Resource
    WaterQualityMapper waterQualityMapper;
    @Autowired
    private WarningIndicatorSettingService warningIndicatorSettingService;
    @Autowired
    private WarningInformationService warningInformationService;
    @Autowired
    private WaterQualityCollectorMapper waterQualityCollectorMapper;
    @Autowired
    private PumpStationService pumpStationService;
    @Autowired
    private PumpMapper pumpMapper;
    @Autowired
    private PumpService pumpService;
    @Qualifier("sqlSessionFactory")
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public WaterQualityVO getWaterQualityStatistics(DateTime startTime, DateTime endTime, String position) {
        QueryWrapper<WaterQuality> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(position)) {
            wrapper.eq("position", position);
        }
        if (startTime != null && endTime != null) {
            wrapper.between("monitor_time", startTime, endTime); // 使用between代替gt和lt，更明确表示时间范围
        }
        // 添加排序条件，按照监测时间逆序
        wrapper.orderByDesc("monitor_time");
        // 检查列表是否为空，并返回第一个元素（如果存在）
        List<WaterQuality> waterQualities = waterQualityMapper.selectList(wrapper);
        List<Date> dateTimeList = new ArrayList<>();
        List<Double> temperatureList = new ArrayList<>();
        List<Double> turbidityList = new ArrayList<>();
        List<Double> phList = new ArrayList<>();
        List<Double> conductivityList = new ArrayList<>();
        List<Double> oxygenList = new ArrayList<>();
        List<Double> nitrogenList = new ArrayList<>();
        List<Double> codList = new ArrayList<>();
        List<Double> chlorineList = new ArrayList<>();
        if (waterQualities != null && !waterQualities.isEmpty()){
            for (WaterQuality waterQuality : waterQualities) {
                dateTimeList.add(waterQuality.getMonitorTime());
                temperatureList.add(waterQuality.getSwt());
                turbidityList.add(waterQuality.getZd());
                phList.add(waterQuality.getPh());
                conductivityList.add(waterQuality.getDdl());
                oxygenList.add(waterQuality.getRjy());
                nitrogenList.add(waterQuality.getAd());
                codList.add(waterQuality.getCod());
                chlorineList.add(waterQuality.getYl());
            }
        }
        return WaterQualityVO
                .builder()
                .dateTimeList(dateTimeList)
                .temperatureList(temperatureList)
                .turbidityList(turbidityList)
                .phList(phList)
                .conductivityList(conductivityList)
                .oxygenList(oxygenList)
                .nitrogenList(nitrogenList)
                .codList(codList)
                .chlorineList(chlorineList)
                .build();
    }

    /**
     * 导出全部内容到excel文件
     * @return java.util.List<com.szy.entity.WaterQuality>
     * @author admin
     * @date 2024/06/23 20:53
     */
    @Override
    public List<WaterQuality> exportAll(DateTime start, DateTime end, String position) {
        QueryWrapper<WaterQuality> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(position)) {
            wrapper.eq("position", position);
        }
        if (start != null && end != null) {
            wrapper.between("monitor_time", start, end); // 使用between代替gt和lt，更明确表示时间范围
        }
        // 添加排序条件，按照监测时间逆序
        wrapper.orderByAsc("monitor_time");
        List<WaterQuality> waterQualities = waterQualityMapper.selectList(wrapper);
        return waterQualities;
    }

    @Override
    public void getDeterInformation(WaterQuality waterQuality) {
        StringBuilder content = new StringBuilder(); // 创建一个StringBuilder对象来构建content字符串
        String level = "";
        boolean seriousFlag = false;
        boolean generalFlag = false;
        BigDecimal longitude = BigDecimal.valueOf(1.0);
        BigDecimal latitude = BigDecimal.valueOf(1.0);
        // 获取水质监测值
        Double temperature = waterQuality.getSwt();
        Double turbidity = waterQuality.getZd();
        Double ph = waterQuality.getPh();
        Double conductivity = waterQuality.getDdl();
        Double dissolvedOxygen = waterQuality.getRjy();
        Double ammoniaNitrogen = waterQuality.getAd();
        Double cod = waterQuality.getCod();
        Double residualChlorine = waterQuality.getYl();

        // 假设参数和对应的预警设置服务已经定义好
        String[] parameters = {"水温", "浊度", "PH", "电导率", "溶解氧", "氨氮", "化学需氧量", "余氯"};
        Map<String, Double> monitoredValues = new HashMap<>();
        monitoredValues.put("水温", temperature);
        monitoredValues.put("浊度", turbidity);
        monitoredValues.put("PH", ph);
        monitoredValues.put("电导率", conductivity);
        monitoredValues.put("溶解氧", dissolvedOxygen);
        monitoredValues.put("氨氮", ammoniaNitrogen);
        monitoredValues.put("化学需氧量", cod);
        monitoredValues.put("余氯", residualChlorine);

        for (String parameter : parameters) {
            // 从服务中获取该位置和水质参数的预警设置
            WarningIndicatorSetting warningIndicatorSetting = warningIndicatorSettingService.getByPosition(waterQuality.getPosition(), parameter);
            if (warningIndicatorSetting != null) {
                // 获取预警范围
                double upperLimit = warningIndicatorSetting.getUpUpLimit();
                double upLimit = warningIndicatorSetting.getUpLimit();
                double lowLimit = warningIndicatorSetting.getLowLimit();
                double lowerLimit = warningIndicatorSetting.getLowerLimit();
                String unit = warningIndicatorSetting.getUnit();

                // 获取监测值
                Double levelValue = monitoredValues.get(parameter);
                String newContent = "";
                if (levelValue > upperLimit) {
                    newContent = String.format("%s高于上上限%.2f%s;", parameter, (levelValue - upperLimit), unit);
                    seriousFlag = true;
                } else if (levelValue > upLimit) {
                    newContent = String.format("%s高于上限%.2f%s;", parameter, (levelValue - upLimit), unit);
                    generalFlag = true;
                } else if (levelValue > lowLimit) {
                    newContent = String.format("%s值为%.2f%s,正常;", parameter, levelValue, (unit != null && !unit.isEmpty()) ? unit : "");
                } else if (levelValue >= lowerLimit) {
                    newContent = String.format("%s低于下限%.2f%s;", parameter, (lowLimit - levelValue), unit);
                    generalFlag = true;
                } else {
                    newContent = String.format("%s低于下下限%.2f%s;", parameter, (lowerLimit - levelValue), unit);
                    seriousFlag = true;
                }
                content.append(newContent);
            }
        }
        if(seriousFlag){
            level = Const.SERIVOUS_WARNING;
            if(generalFlag){
                level = Const.GENERAL_WARNING;
            }
        }
        WarningIndicatorSetting warningIndicatorSetting = warningIndicatorSettingService.getByPosition(waterQuality.getPosition(), "水温");
        if(warningIndicatorSetting != null){
            WarningInformation warningInformation = WarningInformation
                    .builder()
                    .position(waterQuality.getPosition())
                    .project(Const.PROJECT_NAME)
                    .content(String.valueOf(content))
                    .type(Const.WATER_QUALITY)
                    .level(level)
                    .status(Const.UNLIFTED)
                    .longitude(warningIndicatorSetting.getLongitude())
                    .latitude(warningIndicatorSetting.getLatitude())
                    .startTime(waterQuality.getMonitorTime())
                    .createTime(DateTime.now())
                    .updateTime(DateTime.now())
                    .build();
            boolean flag = warningInformationService.getLastThirty(waterQuality.getPosition(), Const.WATER_QUALITY, waterQuality.getMonitorTime());
            if (!level.isEmpty() && flag){
                warningInformationService.save(warningInformation);
            }
        }
    }

    @Override
    public void getDeterInformationYl(WaterQuality waterQuality) {
        StringBuilder content = new StringBuilder(); // 创建一个StringBuilder对象来构建content字符串
        String level = "";
        boolean seriousFlag = false;
        boolean generalFlag = false;

        // 获取余氯监测值
        Double residualChlorine = waterQuality.getYl();

        // 从服务中获取该位置和“余氯”参数的预警设置
        WarningIndicatorSetting warningIndicatorSetting = warningIndicatorSettingService.getByPosition(waterQuality.getPosition(), "余氯");
        if (warningIndicatorSetting != null) {
            // 获取预警范围
            double upperLimit = warningIndicatorSetting.getUpUpLimit();
            double upLimit = warningIndicatorSetting.getUpLimit();
            double lowLimit = warningIndicatorSetting.getLowLimit();
            double lowerLimit = warningIndicatorSetting.getLowerLimit();
            String unit = warningIndicatorSetting.getUnit();

            // 获取监测值
            Double levelValue = residualChlorine;

            String newContent = "";
            if (levelValue > upperLimit) {
                newContent = String.format("余氯高于上上限%.2f%s;", (levelValue - upperLimit), unit);
                seriousFlag = true;
            } else if (levelValue > upLimit) {
                newContent = String.format("余氯高于上限%.2f%s;", (levelValue - upLimit), unit);
                generalFlag = true;
            } else if (levelValue > lowLimit) {
                newContent = String.format("余氯值为%.2f%s,正常;", levelValue, (unit != null && !unit.isEmpty()) ? unit : "");
            } else if (levelValue >= lowerLimit) {
                newContent = String.format("余氯低于下限%.2f%s;", (lowLimit - levelValue), unit);
                generalFlag = true;
            } else {
                newContent = String.format("余氯低于下下限%.2f%s;", (lowerLimit - levelValue), unit);
                seriousFlag = true;
            }
            content.append(newContent);
        }

        if(seriousFlag){
            level = Const.SERIVOUS_WARNING;
        } else if(generalFlag){
            level = Const.GENERAL_WARNING;
        }

        if(!level.isEmpty()){
            BigDecimal originalLongitude = warningIndicatorSetting.getLongitude();
            BigDecimal additionalValueLongitude = BigDecimal.valueOf(-0.0009); // 创建一个值为0.003的BigDecimal对象
            BigDecimal newLongitude = originalLongitude.add(additionalValueLongitude); // 将原有的longitude与0.003相加

            BigDecimal originalLatitude = warningIndicatorSetting.getLatitude();
            BigDecimal additionalValueLatitude = BigDecimal.valueOf(0.0005); // 创建一个值为0.005的BigDecimal对象
            BigDecimal newLatitude = originalLatitude.add(additionalValueLatitude);
            WarningInformation warningInformation = WarningInformation
                    .builder()
                    .position(waterQuality.getPosition())
                    .project(Const.PROJECT_NAME)
                    .content(String.valueOf(content))
                    .type(Const.WATER_QUALITY)
                    .level(level)
                    .status(Const.UNLIFTED)
                    .longitude(newLongitude)
                    .latitude(newLatitude)
                    .startTime(waterQuality.getMonitorTime())
                    .createTime(DateTime.now())
                    .updateTime(DateTime.now())
                    .build();
            boolean flag = warningInformationService.getLastThirty(waterQuality.getPosition(), Const.WATER_QUALITY, waterQuality.getMonitorTime());
            if (flag){
                warningInformationService.save(warningInformation);
            }
        }
    }
    @Override
    public void syncQualityData() {
        // 查询最新的水质监测记录
        WaterQualityCollector collector = waterQualityCollectorMapper.selectLastRecord();
        if (collector != null) {
            WaterQuality waterQuality = new WaterQuality();
            BeanUtil.copyProperties(collector, waterQuality, "id");
            // 设置特定的属性值
            waterQuality.setPosition(WATERWORK);
            waterQuality.setCode(WATERWORK_CODE);
            waterQuality.setMonitorTime(collector.getDateTime());
            // 插入水位数据
            try {
                waterQualityMapper.insert(waterQuality);
                getDeterInformationYl(waterQuality);
                log.info("已获取最新的水质数据");
            } catch (Exception e) {
                // 处理插入失败的情况，例如违反唯一索引约束
                log.error("未获取到最新的水质数据", e);
            }
        } else {
            log.info("暂无水质数据");
        }
        // 更新泵站状态
        if (collector != null) {
            // 查询出所有需要更新的 PumpStation 对象
            List<Pump> pumps = pumpMapper.selectList(null);

            // 创建一个映射，将 code 编码映射到对应的 company 值
            Map<String, Integer> codeToCompanyMap = new HashMap<>();
            codeToCompanyMap.put("1", collector.getJy1Run1());
            codeToCompanyMap.put("2", collector.getJy1Run2());
            codeToCompanyMap.put("3", collector.getJy2Run1());
            codeToCompanyMap.put("4", collector.getJy2Run2());
            codeToCompanyMap.put("5", collector.getJy3Run1());
            codeToCompanyMap.put("6", collector.getJy3Run2());
            codeToCompanyMap.put("7", collector.getJy4Run1());
            codeToCompanyMap.put("8", collector.getJy4Run2());
            codeToCompanyMap.put("9", collector.getJy5Run1());
            codeToCompanyMap.put("10", collector.getJy5Run2());

            // 遍历 pumpStations，更新 company 属性
            for (Pump pump : pumps) {
                String code = pump.getCode();
                int temp = codeToCompanyMap.get(code);
                if (codeToCompanyMap.containsKey(code)) {
                    if (temp == 0) {
                        pump.setStatus("关闭");
                    }
                    else if (temp == 1) {
                        pump.setStatus("打开");
                    }
                    else {
                        pump.setStatus("未知");
                    }
                }
            }

            // 使用MyBatis的批量操作
//            for (PumpStation pumpStation : pumpStations) {
//                pumpStationService.updatePumpStation(pumpStation);
//            }
            SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
            try {
                for (Pump pump : pumps) {
                    pumpService.updateById(pump);
                }
                sqlSession.commit();
            } catch (Exception e) {
                sqlSession.rollback();
                throw e;
            } finally {
                sqlSession.close();
            }
        }
    }
}
