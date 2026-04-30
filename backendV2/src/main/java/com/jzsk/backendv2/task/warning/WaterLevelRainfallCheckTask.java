package com.jzsk.backendv2.task.warning;

import com.jzsk.backendv2.pojo.entity.monitor.StPptnHourEntity;
import com.jzsk.backendv2.pojo.entity.monitor.StRiversREntity;
import com.jzsk.backendv2.pojo.entity.warning.WarningIndicatorEntity;
import com.jzsk.backendv2.service.MonitorDataService;
import com.jzsk.backendv2.service.warning.WarningAutoCheckService;
import com.jzsk.backendv2.service.warning.WarningIndicatorService;
import com.jzsk.backendv2.service.warning.WarningThresholdEvaluator;
import com.jzsk.backendv2.service.warning.WarningThresholdEvaluator.WarningThresholdResult;
import com.jzsk.backendv2.task.AbstractManagedTask;
import com.jzsk.backendv2.task.ManagedTaskDefinition;
import com.jzsk.backendv2.task.TaskModule;
import com.jzsk.backendv2.task.TaskSwitchDecider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 水位/雨量自动预警任务
 * 功能：每5分钟检查"坝前雨量水位站"测点的最新水位和雨量数据，
 *      与预警指标比对后超阈值则生成预警信息
 * 触发方式：@Scheduled(cron = "0 *5 * * * ?")
 * 开关控制：jzsk.v2.task.task-switches.warning.water-level-rainfall-check
 */
@Slf4j
@Component
public class WaterLevelRainfallCheckTask extends AbstractManagedTask {

    private static final String DAM_STATION_STCD = "4211820043";
    private static final String DAM_STATION_NAME = "坝前雨量水位站";
    private static final ManagedTaskDefinition TASK_DEFINITION =
            ManagedTaskDefinition.of(TaskModule.WARNING, WarningTaskCode.WATER_LEVEL_RAINFALL_CHECK);

    private final MonitorDataService monitorDataService;
    private final WarningIndicatorService warningIndicatorService;
    private final WarningAutoCheckService warningAutoCheckService;
    private final WarningThresholdEvaluator warningThresholdEvaluator;

    public WaterLevelRainfallCheckTask(TaskSwitchDecider taskSwitchDecider,
                                        MonitorDataService monitorDataService,
                                        WarningIndicatorService warningIndicatorService,
                                        WarningAutoCheckService warningAutoCheckService,
                                        WarningThresholdEvaluator warningThresholdEvaluator) {
        super(taskSwitchDecider);
        this.monitorDataService = monitorDataService;
        this.warningIndicatorService = warningIndicatorService;
        this.warningAutoCheckService = warningAutoCheckService;
        this.warningThresholdEvaluator = warningThresholdEvaluator;
    }

    /**
     * 执行水位/雨量预警检查
     * 每5分钟触发一次，遍历所有预警指标，对"坝前雨量水位站"测点分别检查水位和雨量
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void check() {
        executeTask(TASK_DEFINITION, this::doCheck);
    }

    private void doCheck() {
        log.info("[WaterLevelRainfallCheckTask] 定时任务开始执行");
        List<WarningIndicatorEntity> settings = warningIndicatorService.listAll();
        for (WarningIndicatorEntity setting : settings) {
            String position = setting.getPosition();
            String type = setting.getType();
            if (!DAM_STATION_NAME.equals(position)) {
                continue;
            }
            log.info("[WaterLevelRainfallCheckTask] 检查指标 position={}, type={}", position, type);
            if ("水位".equals(type)) {
                checkWaterLevel(setting);
            } else if ("雨量".equals(type)) {
                checkRainfall(setting);
            }
        }
        log.info("[WaterLevelRainfallCheckTask] 定时任务执行结束");
    }

    private void checkWaterLevel(WarningIndicatorEntity setting) {
        StRiversREntity latest = monitorDataService.getLatestWaterLevel(DAM_STATION_STCD);
        if (latest == null || latest.getZ1() == null) {
            log.info("[WaterLevelRainfallCheckTask] 无最新水位数据");
            return;
        }
        BigDecimal z1 = latest.getZ1();
        log.info("[WaterLevelRainfallCheckTask] 最新水位数据: {}", z1);
        evaluateAndInsert(setting, z1, latest.getTm(), "水位");
    }

    private void checkRainfall(WarningIndicatorEntity setting) {
        StPptnHourEntity latest = monitorDataService.getLatestRainfall(DAM_STATION_STCD);
        if (latest == null || latest.getDrp() == null) {
            log.info("[WaterLevelRainfallCheckTask] 无最新雨量数据");
            return;
        }
        BigDecimal drp = latest.getDrp();
        log.info("[WaterLevelRainfallCheckTask] 最新雨量数据: {}", drp);
        evaluateAndInsert(setting, drp, latest.getTm(), "雨量");
    }

    private void evaluateAndInsert(WarningIndicatorEntity setting, BigDecimal value,
                                   LocalDateTime time, String type) {
        double v = value.doubleValue();
        WarningThresholdResult result = warningThresholdEvaluator.evaluate(setting, v, type);
        log.info("[WaterLevelRainfallCheckTask] 检查值: {}, 结果={}", v, result == null ? null : result.getLevel());
        if (result != null) {
            warningAutoCheckService.checkAndInsertWarning(
                    setting.getPosition(), type, value, time, result.getLevel(), result.getContent(),
                    setting.getLongitude(), setting.getLatitude());
        }
    }
}
