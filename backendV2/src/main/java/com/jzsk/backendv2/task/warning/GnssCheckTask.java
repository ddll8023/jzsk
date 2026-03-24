package com.jzsk.backendv2.task.warning;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzsk.backendv2.mapper.warning.WarningIndicatorMapper;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GNSS位移自动预警任务
 * 功能：每5分钟检查8个固定GNSS测站（stationId: 33210/33211/33212/33213/33214/33215/33216/33217）
 *      的最新位移数据（x/y/z位移、合位移、水平位移），与预警指标比对后超阈值则生成预警信息
 * 触发方式：@Scheduled(cron = "0 *5 * * * ?")
 * 开关控制：jzsk.v2.task.task-switches.warning.gnss-check
 */
@Slf4j
@Component
public class GnssCheckTask extends AbstractManagedTask {

    private static final List<Long> GNSS_STATION_IDS = Arrays.asList(
            33210L, 33214L, 33216L, 33212L, 33215L, 33211L, 33217L, 33213L);

    private static final Map<String, String> KEY_TYPE_MAP;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("gpsTotalY", "y位移");
        map.put("gpsTotalX", "x位移");
        map.put("displacement2d", "水平位移");
        map.put("displacement3d", "合位移");
        map.put("gpsTotalZ", "z位移");
        KEY_TYPE_MAP = Collections.unmodifiableMap(map);
    }

    private static final ManagedTaskDefinition TASK_DEFINITION =
            ManagedTaskDefinition.of(TaskModule.WARNING, WarningTaskCode.GNSS_CHECK);

    private final MonitorDataService monitorDataService;
    private final WarningIndicatorMapper warningIndicatorMapper;
    private final WarningAutoCheckService warningAutoCheckService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GnssCheckTask(TaskSwitchDecider taskSwitchDecider,
                         MonitorDataService monitorDataService,
                         WarningIndicatorMapper warningIndicatorMapper,
                         WarningAutoCheckService warningAutoCheckService) {
        super(taskSwitchDecider);
        this.monitorDataService = monitorDataService;
        this.warningIndicatorMapper = warningIndicatorMapper;
        this.warningAutoCheckService = warningAutoCheckService;
    }

    /**
     * 执行GNSS位移预警检查
     * 每5分钟触发一次，遍历8个GNSS测站，检查位移数据超阈值则生成预警
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void check() {
        executeTask(TASK_DEFINITION, this::doCheck);
    }

    private void doCheck() {
        log.info("[GnssCheckTask] 定时任务开始执行");
        for (Long stationId : GNSS_STATION_IDS) {
            checkStation(stationId);
        }
        log.info("[GnssCheckTask] 定时任务执行结束");
    }

    private void checkStation(Long stationId) {
        JsonNode resp = monitorDataService.getGnssLatestData(stationId);
        if (resp == null) {
            log.warn("[GnssCheckTask] GNSS API调用失败或返回空 stationId={}", stationId);
            return;
        }
        if (!resp.has("code") || resp.get("code").asInt() != 200) {
            log.warn("[GnssCheckTask] GNSS API返回异常 stationId={}", stationId);
            return;
        }
        JsonNode data = resp.get("data");
        if (data == null) {
            log.warn("[GnssCheckTask] GNSS API无data stationId={}", stationId);
            return;
        }
        JsonNode stationNameNode = data.get("stationName");
        JsonNode monDateNodeRaw = data.get("monDate");
        if (stationNameNode == null || monDateNodeRaw == null) {
            log.warn("[GnssCheckTask] GNSS API数据字段缺失 stationId={}", stationId);
            return;
        }
        String stationName = stationNameNode.asText();
        String monDate = monDateNodeRaw.asText();
        JsonNode monDateNode;
        try {
            monDateNode = objectMapper.readTree(monDate);
        } catch (Exception e) {
            log.warn("[GnssCheckTask] 解析monDate失败 stationId={}: {}", stationId, e.getMessage());
            return;
        }
        for (JsonNode sensorNode : monDateNode) {
            JsonNode l1gp = sensorNode.get("L1_GP_GMS");
            if (l1gp == null) {
                continue;
            }
            for (Map.Entry<String, String> entry : KEY_TYPE_MAP.entrySet()) {
                String key = entry.getKey();
                String type = entry.getValue();
                if (!l1gp.has(key)) {
                    continue;
                }
                double value = l1gp.get(key).asDouble();
                WarningIndicatorEntity setting = warningIndicatorMapper.selectByPositionAndType(stationName, type);
                if (setting == null) {
                    continue;
                }
                evaluateAndInsert(setting, value, type);
            }
        }
    }

    private void evaluateAndInsert(WarningIndicatorEntity setting, double value, String type) {
        String level = null;
        String content = null;
        if (setting.getUpUpLimit() != null && value > setting.getUpUpLimit()) {
            level = WarningLevel.EXTRAORDINARY.getDescription();
            content = type + "超上上限，当前值: " + value;
        } else if (setting.getUpLimit() != null && value > setting.getUpLimit()) {
            level = WarningLevel.SERIOUS.getDescription();
            content = type + "超上限，当前值: " + value;
        } else if (setting.getLowLimit() != null && value < setting.getLowLimit()) {
            level = WarningLevel.GENERAL.getDescription();
            content = type + "低于下限，当前值: " + value;
        } else if (setting.getLowerLimit() != null && value < setting.getLowerLimit()) {
            level = WarningLevel.EXTRAORDINARY.getDescription();
            content = type + "低于下下限，当前值: " + value;
        }
        log.info("[GnssCheckTask] 检查station={}, type={}, value={}, level={}",
                setting.getPosition(), type, value, level);
        if (level != null) {
            warningAutoCheckService.checkAndInsertWarning(
                    setting.getPosition(), type,
                    BigDecimal.valueOf(value), LocalDateTime.now(), level, content,
                    setting.getLongitude(), setting.getLatitude());
        }
    }
}
