package com.szy.task;

import com.szy.entity.WarningIndicatorSetting;
import com.szy.entity.WarningInformation;
import com.szy.entity.StRiversR;
import com.szy.entity.StPptnHour;
import com.szy.service.WarningIndicatorSettingService;
import com.szy.service.WarningInformationService;
import com.szy.mapper.StRiversRMapper;
import com.szy.mapper.StPptnHourMapper;
import com.szy.service.DisplacementHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.szy.mapper.DataNewMapper;
import com.szy.entity.DataNew;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szy.mapper.SensorPointMapper;
import com.szy.entity.SensorPoint;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

@Component
public class WarningAutoCheckTask {

    @Autowired
    private WarningIndicatorSettingService indicatorSettingService;
    @Autowired
    private WarningInformationService warningInformationService;
    @Autowired
    private StRiversRMapper stRiversRMapper;
    @Autowired
    private StPptnHourMapper stPptnHourMapper;
    @Autowired
    private DisplacementHistoryService displacementHistoryService;
    @Autowired
    private DataNewMapper dataNewMapper;
    @Autowired
    private SensorPointMapper sensorPointMapper;
    @Autowired
    private RestTemplate restTemplate;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // GNSS八个测站stationId
    private static final List<Long> GNSS_STATION_IDS = Arrays.asList(33210L,33214L,33216L,33212L,33215L,33211L,33217L,33213L);
    private static final int GNSS_PROJECT_ID = 1681;
    private static final String GNSS_SENSOR = "L1_GP";
    private static final int GNSS_PAGE = 1;
    private static final int GNSS_SIZE = 10;
    private static final String GNSS_API = "http://localhost:8081/external-data/latestMonitor?projectId=1681&stationId=";

    // 每5分钟执行一次
    @Scheduled(cron = "0 */5 * * * ?")
    public void checkWarning() {
        System.out.println("[WarningAutoCheckTask] 定时任务开始执行");
        List<WarningIndicatorSetting> settings = indicatorSettingService.getAll();
        for (WarningIndicatorSetting setting : settings) {
            String type = setting.getType();
            String position = setting.getPosition();
            System.out.println("[WarningAutoCheckTask] 检查指标 position=" + position + ", type=" + type);
            if ("坝前雨量水位站".equals(position)) {
                String stcd = "4211820043";
                if ("水位".equals(type)) {
                    StRiversR latest = stRiversRMapper.selectLatestByStcd(stcd);
                    System.out.println("[WarningAutoCheckTask] 最新水位数据: " + (latest != null ? latest.getZ1() : null));
                    if (latest != null && latest.getZ1() != null) {
                        checkAndInsertWarning(setting, latest.getZ1(), toDate(latest.getTm()), "水位");
                    }
                } else if ("雨量".equals(type)) {
                    StPptnHour latest = stPptnHourMapper.selectLatestByStcd(stcd);
                    System.out.println("[WarningAutoCheckTask] 最新雨量数据: " + (latest != null ? latest.getDrp() : null));
                    if (latest != null && latest.getDrp() != null) {
                        checkAndInsertWarning(setting, latest.getDrp(), toDate(latest.getTm()), "雨量");
                    }
                }
            }
        }
        System.out.println("[WarningAutoCheckTask] 定时任务执行结束");
    }

    private void checkAndInsertWarning(WarningIndicatorSetting setting, BigDecimal value, Date time, String type) {
        double v = value.doubleValue();
        String level = null;
        String content = null;
        if (setting.getUpUpLimit() != null && v > setting.getUpUpLimit()) {
            level = "严重预警";
            content = type + "超上上限，当前值：" + v;
        } else if (setting.getUpLimit() != null && v > setting.getUpLimit()) {
            level = "一般预警";
            content = type + "超上限，当前值：" + v;
        } else if (setting.getLowerLimit() != null && v < setting.getLowerLimit()) {
            level = "严重预警";
            content = type + "低于下下限，当前值：" + v;
        } else if (setting.getLowLimit() != null && v < setting.getLowLimit()) {
            level = "一般预警";
            content = type + "低于下限，当前值：" + v;
        }
        System.out.println("[WarningAutoCheckTask] 检查值: " + v + ", 结果level=" + level + ", content=" + content);
        if (level != null) {
            WarningInformation info = new WarningInformation();
            info.setPosition(setting.getPosition());
            info.setProject("智慧荆竹水库管理平台");
            info.setType(type);
            info.setLevel(level);
            info.setContent(content);
            info.setStatus("未解除");
            info.setStartTime(time);
            // 其他字段可为空
            warningInformationService.addWarningInformation(info);
            System.out.println("[WarningAutoCheckTask] 已插入预警信息: position=" + info.getPosition() + ", type=" + info.getType() + ", level=" + info.getLevel() + ", content=" + info.getContent() + ", startTime=" + info.getStartTime());
        }
    }

    private Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 每5分钟执行一次
    @Scheduled(cron = "0 */5 * * * ?")
    public void checkGnssWarning() {
        for (Long stationId : GNSS_STATION_IDS) {
            try {
                String url = GNSS_API + stationId;
                JsonNode resp = restTemplate.getForObject(url, JsonNode.class);
                if (resp == null || resp.get("code").asInt() != 200) continue;
                JsonNode data = resp.get("data");
                if (data == null) continue;
                String stationName = data.get("stationName").asText();
                String monDate = data.get("monDate").asText();
                JsonNode monDateNode = objectMapper.readTree(monDate);
                // 只取第一个key（一般只有一个）
                for (JsonNode sensorNode : monDateNode) {
                    JsonNode l1gp = sensorNode.get("L1_GP_GMS");
                    if (l1gp == null) continue;
                    Map<String, String> keyMap = new java.util.HashMap<>();
                    keyMap.put("gpsTotalY", "y位移");
                    keyMap.put("gpsTotalX", "x位移");
                    keyMap.put("displacement2d", "水平位移");
                    keyMap.put("displacement3d", "合位移");
                    keyMap.put("gpsTotalZ", "z位移");
                    for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                        String key = entry.getKey();
                        String type = entry.getValue();
                        if (!l1gp.has(key)) continue;
                        double value = l1gp.get(key).asDouble();
                        WarningIndicatorSetting setting = indicatorSettingService.getByPosition(stationName, type);
                        if (setting == null) continue;
                        String level = null, content = null;
                        if (setting.getUpUpLimit() != null && value > setting.getUpUpLimit()) {
                            level = "特别严重预警";
                            content = type + "超上上限，当前值: " + value;
                        } else if (setting.getUpLimit() != null && value > setting.getUpLimit()) {
                            level = "严重预警";
                            content = type + "超上限，当前值: " + value;
                        } else if (setting.getLowLimit() != null && value < setting.getLowLimit()) {
                            level = "一般预警";
                            content = type + "低于下限，当前值: " + value;
                        } else if (setting.getLowerLimit() != null && value < setting.getLowerLimit()) {
                            level = "特别严重预警";
                            content = type + "低于下下限，当前值: " + value;
                        }
                        if (level != null) {
                            WarningInformation info = new WarningInformation();
                            info.setPosition(stationName);
                            info.setProject("智慧荆竹水库管理平台");
                            info.setType(type);
                            info.setLevel(level);
                            info.setContent(content);
                            info.setStatus("未处理");
                            info.setStartTime(new java.util.Date());
                            warningInformationService.addWarningInformation(info);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 每5分钟执行一次
    @Scheduled(cron = "0 */5 * * * ?")
    public void checkMcuWarning() {
        System.out.println("[WarningAutoCheckTask] MCU定时任务开始执行");
        try {
            java.util.List<DataNew> latestList = dataNewMapper.selectLatestForAllPoints();
            for (DataNew data : latestList) {
                String pointId = data.getPointId();
                // 查出测点名称
                String pointName = null;
                try {
                    SensorPoint sp = sensorPointMapper.selectById(Long.valueOf(pointId));
                    if (sp != null) pointName = sp.getName();
                } catch (Exception e) { pointName = null; }
                if (!"mcu测试站".equals(pointName)) continue; // 只处理mcu测试站
                java.util.Date collectTime = data.getTime() != null ? java.util.Date.from(data.getTime().toInstant()) : new java.util.Date();
                String resultData = data.getResultData();
                if (resultData == null) continue;
                java.util.Map<String, Object> valueMap = null;
                try {
                    valueMap = objectMapper.readValue(resultData, java.util.Map.class);
                } catch (Exception e) {
                    System.err.println("[WarningAutoCheckTask][MCU] 解析result_data失败: " + e.getMessage());
                    continue;
                }
                // 直接用"mcu测试站"查指标
                WarningIndicatorSetting setting = indicatorSettingService.getByPosition("mcu测试站", "模数");
                if (setting == null) continue;
                // 解析resultData，取"模数"
                Object valueObj = valueMap.get("模数");
                if (valueObj == null) continue;
                double value;
                try { value = Double.parseDouble(valueObj.toString()); } catch (Exception e) { continue; }
                String level = null, content = null;
                if (setting.getUpUpLimit() != null && value > setting.getUpUpLimit()) {
                    level = "严重预警";
                    content = "模数超上上限，当前值：" + value;
                } else if (setting.getUpLimit() != null && value > setting.getUpLimit()) {
                    level = "一般预警";
                    content = "模数超上限，当前值：" + value;
                } else if (setting.getLowerLimit() != null && value < setting.getLowerLimit()) {
                    level = "严重预警";
                    content = "模数低于下下限，当前值：" + value;
                } else if (setting.getLowLimit() != null && value < setting.getLowLimit()) {
                    level = "一般预警";
                    content = "模数低于下限，当前值：" + value;
                }
                System.out.println("[WarningAutoCheckTask][MCU] 检查point_id=" + pointId + ", type=" + "模数" + ", value=" + value + ", level=" + level);
                if (level != null) {
                    WarningInformation info = new WarningInformation();
                    info.setPosition(pointId); // 用编号
                    info.setProject("智慧荆竹水库管理平台");
                    info.setType("模数");
                    info.setLevel(level);
                    info.setContent(content);
                    info.setStatus("未处理");
                    info.setStartTime(new java.util.Date());
                    warningInformationService.addWarningInformation(info);
                    System.out.println("[WarningAutoCheckTask][MCU] 已插入预警信息: position=" + info.getPosition() + ", type=" + info.getType() + ", level=" + info.getLevel() + ", content=" + info.getContent() + ", startTime=" + info.getStartTime());
                }
            }
        } catch (Exception e) {
            System.err.println("[WarningAutoCheckTask][MCU] 定时任务异常: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("[WarningAutoCheckTask] MCU定时任务执行结束");
    }
} 