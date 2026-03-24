package com.jzsk.backendv2.task.warning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzsk.backendv2.mapper.warning.WarningIndicatorMapper;
import com.jzsk.backendv2.pojo.entity.mcu.DataNewEntity;
import com.jzsk.backendv2.pojo.entity.warning.WarningIndicatorEntity;
import com.jzsk.backendv2.pojo.enums.WarningLevel;
import com.jzsk.backendv2.service.MonitorDataService;
import com.jzsk.backendv2.service.warning.WarningAutoCheckService;
import com.jzsk.backendv2.task.AbstractManagedTask;
import com.jzsk.backendv2.task.ManagedTaskDefinition;
import com.jzsk.backendv2.task.TaskModule;
import com.jzsk.backendv2.task.TaskSwitchDecider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MCU自动预警任务
 * 功能：每5分钟检查"mcu测试站"测点的最新传感器数据，
 *      解析JSON中的"模数"字段，与预警指标比对后超阈值则生成预警信息
 * 触发方式：@Scheduled(cron = "0 *5 * * * ?")
 * 开关控制：jzsk.v2.task.task-switches.warning.mcu-check
 */
@Slf4j
@Component
public class McuCheckTask extends AbstractManagedTask {

    private static final String MCU_STATION_NAME = "mcu测试站";
    private static final String MODULUS_TYPE = "模数";
    private static final ManagedTaskDefinition TASK_DEFINITION =
            ManagedTaskDefinition.of(TaskModule.WARNING, WarningTaskCode.MCU_CHECK);

    private final MonitorDataService monitorDataService;
    private final WarningIndicatorMapper warningIndicatorMapper;
    private final WarningAutoCheckService warningAutoCheckService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public McuCheckTask(TaskSwitchDecider taskSwitchDecider,
                        MonitorDataService monitorDataService,
                        WarningIndicatorMapper warningIndicatorMapper,
                        WarningAutoCheckService warningAutoCheckService) {
        super(taskSwitchDecider);
        this.monitorDataService = monitorDataService;
        this.warningIndicatorMapper = warningIndicatorMapper;
        this.warningAutoCheckService = warningAutoCheckService;
    }

    /**
     * 执行MCU预警检查
     * 每5分钟触发一次，查询data_new最新数据，筛选mcu测试站并检查模数
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void check() {
        executeTask(TASK_DEFINITION, this::doCheck);
    }

    private void doCheck() {
        log.info("[McuCheckTask] 定时任务开始执行");
        try {
            WarningIndicatorEntity setting = warningIndicatorMapper.selectByPositionAndType(MCU_STATION_NAME, MODULUS_TYPE);
            if (setting == null) {
                log.info("[McuCheckTask] 未找到mcu测试站模数预警指标配置");
                return;
            }

            List<DataNewEntity> latestList = monitorDataService.getAllLatestMcuData();
            for (DataNewEntity data : latestList) {
                String pointId = data.getPointId();
                String pointName = monitorDataService.getPointName(pointId);
                if (!MCU_STATION_NAME.equals(pointName)) {
                    continue;
                }
                String resultData = data.getResultData();
                if (resultData == null) {
                    continue;
                }
                Date collectTime = data.getTime() != null
                        ? Date.from(data.getTime().toInstant())
                        : new Date();
                LocalDateTime collectLdt = data.getTime() != null
                        ? data.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                        : LocalDateTime.now();

                Map<String, Object> valueMap = parseResultData(resultData);
                if (valueMap == null) {
                    continue;
                }
                Object valueObj = valueMap.get(MODULUS_TYPE);
                if (valueObj == null) {
                    continue;
                }
                double value;
                try {
                    value = Double.parseDouble(valueObj.toString());
                } catch (NumberFormatException e) {
                    log.warn("[McuCheckTask] point_id={} 模数字段解析失败: {}", pointId, valueObj);
                    continue;
                }
                evaluateAndInsert(setting, pointId, value, collectLdt);
            }
        } catch (Exception e) {
            log.error("[McuCheckTask] 定时任务异常: {}", e.getMessage(), e);
        }
        log.info("[McuCheckTask] 定时任务执行结束");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseResultData(String resultData) {
        try {
            return objectMapper.readValue(resultData, Map.class);
        } catch (Exception e) {
            log.warn("[McuCheckTask] 解析result_data失败: {}", e.getMessage());
            return null;
        }
    }

    private void evaluateAndInsert(WarningIndicatorEntity setting, String pointId,
                                   double value, LocalDateTime collectTime) {
        String level = null;
        String content = null;
        if (setting.getUpUpLimit() != null && value > setting.getUpUpLimit()) {
            level = WarningLevel.SERIOUS.getDescription();
            content = "模数超上上限，当前值：" + value;
        } else if (setting.getUpLimit() != null && value > setting.getUpLimit()) {
            level = WarningLevel.GENERAL.getDescription();
            content = "模数超上限，当前值：" + value;
        } else if (setting.getLowerLimit() != null && value < setting.getLowerLimit()) {
            level = WarningLevel.SERIOUS.getDescription();
            content = "模数低于下下限，当前值：" + value;
        } else if (setting.getLowLimit() != null && value < setting.getLowLimit()) {
            level = WarningLevel.GENERAL.getDescription();
            content = "模数低于下限，当前值：" + value;
        }
        log.info("[McuCheckTask] 检查point_id={}, type={}, value={}, level={}", pointId, MODULUS_TYPE, value, level);
        if (level != null) {
            warningAutoCheckService.checkAndInsertWarning(
                    pointId, MODULUS_TYPE,
                    BigDecimal.valueOf(value), collectTime, level, content,
                    setting.getLongitude(), setting.getLatitude());
        }
    }
}
