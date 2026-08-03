package com.jzsk.backendv2.service.impl.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzsk.backendv2.mapper.mcu.DataNewMapper;
import com.jzsk.backendv2.mapper.mcu.SensorPointMapper;
import com.jzsk.backendv2.mapper.monitor.StPptnHourMapper;
import com.jzsk.backendv2.mapper.monitor.StRiversRMapper;
import com.jzsk.backendv2.pojo.entity.mcu.DataNewEntity;
import com.jzsk.backendv2.pojo.entity.mcu.SensorPointEntity;
import com.jzsk.backendv2.pojo.entity.monitor.StPptnHourEntity;
import com.jzsk.backendv2.pojo.entity.monitor.StRiversREntity;
import com.jzsk.backendv2.pojo.vo.dam.DisplacementHistoryVO;
import com.jzsk.backendv2.pojo.vo.dam.DisplacementKeyValueVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceMonitorOverviewVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceStatusVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceTypeStatusVO;
import com.jzsk.backendv2.service.external.DisplacementHistoryService;
import com.jzsk.backendv2.service.monitor.DeviceFaultRecordService;
import com.jzsk.backendv2.service.monitor.DeviceMonitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 设备监控服务实现类
 * 职责: 分别检测GNSS、雨水情、渗流渗压设备状态
 * 遵循KISS原则: 每个检测方法职责单一，独立返回
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceMonitorServiceImpl implements DeviceMonitorService {

    private final DisplacementHistoryService displacementHistoryService;
    private final DataNewMapper dataNewMapper;
    private final SensorPointMapper sensorPointMapper;
    private final StRiversRMapper stRiversRMapper;
    private final StPptnHourMapper stPptnHourMapper;
    private final ObjectMapper objectMapper;
    private final DeviceFaultRecordService deviceFaultRecordService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final Set<String> retryingDeviceTypes = ConcurrentHashMap.newKeySet();

    /** 固定渗流渗压设备清单：设备数量和设备编码不依赖外部数据库可用性。 */
    private static final Map<String, String> SEEPAGE_DEVICE_MAP;
    static {
        Map<String, String> map = new java.util.LinkedHashMap<>();
        map.put("1130221274157547520", "UPR1-1");
        map.put("1130221285905793024", "UPB1-1");
        map.put("1130221296655794176", "UPB2-1");
        map.put("1130221308043329536", "UPA1-1");
        map.put("1130221319053377536", "UPB3-1");
        map.put("1130221331892142080", "UPB4-1");
        map.put("1130221343288066048", "UPB4-4");
        map.put("1130221354100981760", "UPB4-2");
        map.put("1130221364981006336", "UPB4-3");
        map.put("1130221376058163200", "UPB4-5");
        map.put("1130221386883661824", "UPB3-2");
        map.put("1130221397532999680", "UPB3-4");
        map.put("1130221408509493248", "UPB3-3");
        map.put("1130221419490181120", "UPB2-2");
        map.put("1130221430265348096", "UPA1-2");
        map.put("1130221441057292288", "UPB2-3");
        map.put("1130221451794710528", "UPA1-3");
        map.put("1130221462834118656", "UPB2-4");
        map.put("1130221474066464768", "UPA1-4");
        map.put("1130221485206536192", "UPB2-5");
        map.put("1130221496413716480", "UPA1-5");
        map.put("1130221507100803072", "UPB1-5");
        map.put("1130221518182154240", "UPB1-4");
        map.put("1130221528902795264", "UPB1-3");
        map.put("1130221539753459712", "UPB1-2");
        map.put("1130221562159431680", "UPR1-2");
        map.put("P0108206", "UPR2-1");
        map.put("P0108311", "UPR2-2");
        map.put("1130221574088032256", "UPB3-5");
        SEEPAGE_DEVICE_MAP = java.util.Collections.unmodifiableMap(map);
    }

    /** GNSS超时阈值（分钟）: 60分钟采集周期 + 5分钟缓冲 */
    private static final long GNSS_TIMEOUT_MINUTES = 65;
    /** 雨水情超时阈值（分钟）: 5分钟采集周期 + 5分钟缓冲 */
    private static final long RAIN_TIMEOUT_MINUTES = 10;
    /** 渗流渗压超时阈值（分钟）: 60分钟采集周期 + 5分钟缓冲 */
    private static final long SEEPAGE_TIMEOUT_MINUTES = 65;
    /** 网络故障后台重试间隔 */
    private static final long[] NETWORK_RETRY_DELAY_MILLIS = {5000L, 15000L, 30000L};
    private static final String DETAIL_NETWORK_FAULT = "网络故障";
    private static final String DETAIL_COLLECT_ABNORMAL = "采集异常";
    private static final String DETAIL_LATEST_EMPTY = "最新采集时间无数据";

    /** GNSS测站配置 */
    private static final String GNSS_STATION_IDS = "33210,33214,33216,33212,33215,33211,33217,33213";
    private static final String GNSS_SENSOR = "L1_GP";
    private static final Integer GNSS_PROJECT_ID = 1681;

    /** 坝前水位站编码（SQL Server ST_RIVER_R 表） */
    private static final String RAIN_STATION_STCD = "4211820043";

    /** GNSS测站名称映射 */
    private static final Map<Long, String> GNSS_NAME_MAP;
    static {
        Map<Long, String> map = new java.util.HashMap<>();
        map.put(33210L, "LJ1-1"); map.put(33214L, "LJ1-2");
        map.put(33216L, "LJ1-3"); map.put(33212L, "LJ1-4");
        map.put(33215L, "LT2-1"); map.put(33211L, "LT2-2");
        map.put(33217L, "LT2-3"); map.put(33213L, "LT2-4");
        GNSS_NAME_MAP = java.util.Collections.unmodifiableMap(map);
    }

    @Override
    public DeviceTypeStatusVO getGnssStatus() {
        List<DeviceStatusVO> devices = checkGnssDevices(true);
        processFaultRecords(devices);
        return buildTypeResult(devices);
    }

    @Override
    public DeviceTypeStatusVO getRainStatus() {
        List<DeviceStatusVO> devices = checkRainDevices(true);
        processFaultRecords(devices);
        return buildTypeResult(devices);
    }

    @Override
    public DeviceTypeStatusVO getSeepageStatus() {
        List<DeviceStatusVO> devices = checkSeepageDevices(true);
        processFaultRecords(devices);
        return buildTypeResult(devices);
    }

    /**
     * 检测GNSS设备状态
     * 数据源: 外部HTTP API
     */
    private List<DeviceStatusVO> checkGnssDevices(boolean scheduleRetry) {
        List<DeviceStatusVO> devices = new ArrayList<>();
        List<DisplacementHistoryVO> gnssData;

        try {
            gnssData = displacementHistoryService.getDisplacementLatest(
                    GNSS_STATION_IDS, GNSS_SENSOR, GNSS_PROJECT_ID);
        } catch (Exception e) {
            log.error("[DeviceMonitor] GNSS接口调用失败，所有GNSS设备标记为采集异常", e);
            if (scheduleRetry) {
                scheduleNetworkRetry("gnss");
            }
            GNSS_NAME_MAP.forEach((id, name) ->
                    devices.add(buildDevice(String.valueOf(id), name, "gnss", "abnormal", null, DETAIL_NETWORK_FAULT)));
            return devices;
        }

        if (gnssData == null || gnssData.isEmpty()) {
            log.warn("[DeviceMonitor] GNSS接口返回空数据，所有GNSS设备标记为采集异常");
            GNSS_NAME_MAP.forEach((id, name) ->
                    devices.add(buildDevice(String.valueOf(id), name, "gnss", "abnormal", null, DETAIL_COLLECT_ABNORMAL)));
            return devices;
        }

        Map<Long, DisplacementHistoryVO> dataMap = gnssData.stream()
                .filter(d -> d.getStationId() != null)
                .collect(Collectors.toMap(DisplacementHistoryVO::getStationId, d -> d, (a, b) -> a));

        for (Map.Entry<Long, String> entry : GNSS_NAME_MAP.entrySet()) {
            Long stationId = entry.getKey();
            String name = entry.getValue();
            DisplacementHistoryVO data = dataMap.get(stationId);

            if (data == null || data.getCollectTime() == null) {
                devices.add(buildDevice(String.valueOf(stationId), name, "gnss", "abnormal", null, DETAIL_COLLECT_ABNORMAL));
                continue;
            }

            LocalDateTime collectTime = parseCollectTime(data.getCollectTime());
            String status = determineStatus(collectTime, GNSS_TIMEOUT_MINUTES);
            LocalDateTime displayTime = "abnormal".equals(status) ? null : collectTime;
            String detail = "online".equals(status) ? extractGnssDetail(data) : faultDetail(status);

            devices.add(buildDevice(String.valueOf(stationId), name, "gnss", status, displayTime, detail));
        }

        return devices;
    }

    /**
     * 检测雨水情设备状态
     * 数据源: SQL Server (水位ST_RIVERS_R + 雨量ST_PPTN_HOUR)
     */
    private List<DeviceStatusVO> checkRainDevices(boolean scheduleRetry) {
        List<DeviceStatusVO> devices = new ArrayList<>();
        String deviceCode = "RAIN_WATER_MAIN";
        String deviceName = "坝前雨量水位站";

        StRiversREntity latestWater;
        StPptnHourEntity latestRain;
        boolean hasQueryError = false;

        try {
            latestWater = stRiversRMapper.selectLatestByStcd(RAIN_STATION_STCD);
        } catch (Exception e) {
            log.error("[DeviceMonitor] 水位数据查询失败，stcd={}", RAIN_STATION_STCD, e);
            latestWater = null;
            hasQueryError = true;
        }

        try {
            latestRain = stPptnHourMapper.selectAll().stream()
                    .max((a, b) -> a.getTm() != null && b.getTm() != null ? a.getTm().compareTo(b.getTm()) : 0)
                    .orElse(null);
        } catch (Exception e) {
            log.error("[DeviceMonitor] 雨量数据查询失败", e);
            latestRain = null;
            hasQueryError = true;
        }

        if (latestWater == null && latestRain == null) {
            if (hasQueryError && scheduleRetry) {
                scheduleNetworkRetry("rain");
            }
            String detail = hasQueryError ? DETAIL_NETWORK_FAULT : DETAIL_COLLECT_ABNORMAL;
            devices.add(buildDevice(deviceCode, deviceName, "rain", "abnormal", null, detail));
            return devices;
        }

        LocalDateTime waterTime = latestWater != null ? latestWater.getTm() : null;
        LocalDateTime rainTime = latestRain != null ? latestRain.getTm() : null;
        LocalDateTime lastTime = waterTime != null && rainTime != null
                ? waterTime.isAfter(rainTime) ? waterTime : rainTime
                : waterTime != null ? waterTime : rainTime;

        String status = determineStatus(lastTime, RAIN_TIMEOUT_MINUTES);

        StringBuilder detail = new StringBuilder();
        if (latestWater != null && latestWater.getZ1() != null) {
            detail.append("水位: ").append(latestWater.getZ1()).append("m");
        }
        if (latestRain != null && latestRain.getDrp() != null) {
            if (detail.length() > 0) detail.append("; ");
            detail.append("雨量: ").append(latestRain.getDrp()).append("mm");
        }

        LocalDateTime displayTime = "abnormal".equals(status) ? null : lastTime;
        String displayDetail = "online".equals(status) && detail.length() > 0 ? detail.toString() : faultDetail(status);

        devices.add(buildDevice(deviceCode, deviceName, "rain", status, displayTime, displayDetail));
        return devices;
    }

    /**
     * 检测渗流渗压设备状态
     * 数据源: PostgreSQL (data_new表 + sensor_point表编号映射)
     * 设备清单固定为29个，sensor_point仅用于少数P编号测点的数据映射
     */
    private List<DeviceStatusVO> checkSeepageDevices(boolean scheduleRetry) {
        List<DataNewEntity> seepageData;
        try {
            seepageData = dataNewMapper.selectLatestForAllPoints();
        } catch (Exception e) {
            log.error("[DeviceMonitor] 渗压数据查询失败，固定29个渗流渗压设备标记为网络故障", e);
            if (scheduleRetry) {
                scheduleNetworkRetry("seepage");
            }
            return buildFixedSeepageFaultDevices(DETAIL_NETWORK_FAULT);
        }

        List<SensorPointEntity> allPoints;
        try {
            allPoints = sensorPointMapper.selectAll();
        } catch (Exception e) {
            log.error("[DeviceMonitor] 测点映射查询失败，固定29个渗流渗压设备标记为网络故障", e);
            if (scheduleRetry) {
                scheduleNetworkRetry("seepage");
            }
            return buildFixedSeepageFaultDevices(DETAIL_NETWORK_FAULT);
        }

        if (allPoints == null || allPoints.isEmpty()) {
            log.warn("[DeviceMonitor] 测点映射为空，固定29个渗流渗压设备标记为采集异常");
            return buildFixedSeepageFaultDevices(DETAIL_COLLECT_ABNORMAL);
        }

        Map<String, DataNewEntity> dataMap = seepageData == null ? java.util.Collections.emptyMap() :
                seepageData.stream()
                        .filter(entity -> entity != null && entity.getPointId() != null)
                        .collect(Collectors.toMap(DataNewEntity::getPointId, entity -> entity, (a, b) -> a));
        Map<String, String> pointNameToId = allPoints.stream()
                .filter(point -> point != null && point.getId() != null && point.getName() != null)
                .collect(Collectors.toMap(SensorPointEntity::getName,
                        point -> String.valueOf(point.getId()), (a, b) -> a));

        List<DeviceStatusVO> devices = new ArrayList<>();
        for (Map.Entry<String, String> entry : SEEPAGE_DEVICE_MAP.entrySet()) {
            String fixedCode = entry.getKey();
            String deviceName = entry.getValue();
            DataNewEntity entity = dataMap.get(fixedCode);

            // 旧数据源中少数测点以P编号维护，先按P编号查找，找不到时再通过sensor_point映射到数字ID。
            if (entity == null && fixedCode.startsWith("P")) {
                String numericId = pointNameToId.get(fixedCode);
                if (numericId != null) {
                    entity = dataMap.get(numericId);
                }
            }

            if (entity == null) {
                devices.add(buildDevice(fixedCode, deviceName, "seepage",
                        "abnormal", null, DETAIL_COLLECT_ABNORMAL));
                continue;
            }

            OffsetDateTime offsetTime = entity.getTime();
            LocalDateTime collectTime = offsetTime != null
                    ? offsetTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
                    : null;

            String status = determineStatus(collectTime, SEEPAGE_TIMEOUT_MINUTES);
            if ("online".equals(status) && !hasValidSeepageData(entity)) {
                status = "offline";
            }
            LocalDateTime displayTime = "abnormal".equals(status) ? null : collectTime;
            String detail = "online".equals(status) ? extractSeepageDetail(entity) : faultDetail(status);

            devices.add(buildDevice(fixedCode, deviceName, "seepage", status, displayTime, detail));
        }
        return devices;
    }

    /**
     * 按固定设备清单构造故障状态，保证外部数据源不可用时设备总数仍保持一致。
     */
    private List<DeviceStatusVO> buildFixedSeepageFaultDevices(String detail) {
        List<DeviceStatusVO> devices = new ArrayList<>();
        for (Map.Entry<String, String> entry : SEEPAGE_DEVICE_MAP.entrySet()) {
            devices.add(buildDevice(entry.getKey(), entry.getValue(),
                    "seepage", "abnormal", null, detail));
        }
        return devices;
    }

    /**
     * 构建单类型返回结果（stats + devices）
     */
    private DeviceTypeStatusVO buildTypeResult(List<DeviceStatusVO> devices) {
        int total = devices.size();
        int online = (int) devices.stream().filter(d -> "online".equals(d.getStatus())).count();
        int offline = (int) devices.stream().filter(d -> "offline".equals(d.getStatus())).count();
        int abnormal = (int) devices.stream().filter(d -> "abnormal".equals(d.getStatus())).count();
        return new DeviceTypeStatusVO(
                new DeviceMonitorOverviewVO.Stats(total, online, offline, abnormal),
                devices
        );
    }

    /**
     * 判断设备状态
     * online: 已到报，采集时间在阈值内
     * offline: 未到报，设备在线但未按定时采集
     * abnormal: 采集异常，接口失败、网络波动或无有效采集数据
     */
    private String determineStatus(LocalDateTime lastCollectTime, long timeoutMinutes) {
        if (lastCollectTime == null) {
            return "abnormal";
        }
        long minutesDiff = ChronoUnit.MINUTES.between(lastCollectTime, LocalDateTime.now());
        return minutesDiff <= timeoutMinutes ? "online" : "offline";
    }

    private DeviceStatusVO buildDevice(String code, String name, String type, String status,
                                        LocalDateTime lastCollectTime, String detail) {
        return new DeviceStatusVO(code, name, type, status, lastCollectTime, detail);
    }

    /**
     * 获取异常详情文案。
     */
    private String faultDetail(String status) {
        if ("offline".equals(status)) {
            return DETAIL_LATEST_EMPTY;
        }
        if ("abnormal".equals(status)) {
            return DETAIL_COLLECT_ABNORMAL;
        }
        return null;
    }

    /**
     * 遍历设备列表，将状态变化同步到到报情况记录
     */
    private void processFaultRecords(List<DeviceStatusVO> devices) {
        processFaultRecords(devices, false);
    }

    /**
     * 遍历设备列表，将状态变化同步到到报情况记录。
     *
     * @param devices 设备状态列表
     * @param includeNetworkFault 是否写入已确认的网络故障
     */
    private void processFaultRecords(List<DeviceStatusVO> devices, boolean includeNetworkFault) {
        for (DeviceStatusVO device : devices) {
            try {
                if (!includeNetworkFault && isNetworkFaultDevice(device)) {
                    continue;
                }
                deviceFaultRecordService.processDeviceStatus(device);
            } catch (Exception e) {
                log.error("[DeviceMonitor] 到报情况记录处理失败: {}/{}", device.getType(), device.getCode(), e);
            }
        }
    }

    /**
     * 数据源网络故障先返回页面，再后台重试确认。
     */
    private void scheduleNetworkRetry(String deviceType) {
        if (!retryingDeviceTypes.add(deviceType)) {
            return;
        }
        scheduleNetworkRetry(deviceType, 0);
    }

    private void scheduleNetworkRetry(String deviceType, int attemptIndex) {
        long delayMillis = NETWORK_RETRY_DELAY_MILLIS[attemptIndex];
        Date retryTime = Date.from(Instant.now().plusMillis(delayMillis));
        threadPoolTaskScheduler.schedule(() -> executeNetworkRetry(deviceType, attemptIndex), retryTime);
    }

    private void executeNetworkRetry(String deviceType, int attemptIndex) {
        List<DeviceStatusVO> devices = checkDevicesForRetry(deviceType);
        boolean stillNetworkFault = isNetworkFaultResult(devices);
        if (stillNetworkFault && attemptIndex + 1 < NETWORK_RETRY_DELAY_MILLIS.length) {
            scheduleNetworkRetry(deviceType, attemptIndex + 1);
            return;
        }
        try {
            processFaultRecords(devices, true);
        } finally {
            retryingDeviceTypes.remove(deviceType);
        }
    }

    private List<DeviceStatusVO> checkDevicesForRetry(String deviceType) {
        if ("gnss".equals(deviceType)) {
            return checkGnssDevices(false);
        }
        if ("rain".equals(deviceType)) {
            return checkRainDevices(false);
        }
        if ("seepage".equals(deviceType)) {
            return checkSeepageDevices(false);
        }
        return java.util.Collections.emptyList();
    }

    private boolean isNetworkFaultResult(List<DeviceStatusVO> devices) {
        return devices != null && !devices.isEmpty() && devices.stream().allMatch(this::isNetworkFaultDevice);
    }

    private boolean isNetworkFaultDevice(DeviceStatusVO device) {
        return device != null
                && "abnormal".equals(device.getStatus())
                && DETAIL_NETWORK_FAULT.equals(device.getDetail());
    }

    /**
     * 解析GNSS采集时间字符串
     */
    private static final java.time.format.DateTimeFormatter DTF =
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime parseCollectTime(String timeStr) {
        if (timeStr == null) return null;
        try {
            return LocalDateTime.parse(timeStr, DTF);
        } catch (Exception e) {
            try {
                return LocalDateTime.parse(timeStr);
            } catch (Exception e2) {
                try {
                    return OffsetDateTime.parse(timeStr).atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                } catch (Exception ex) {
                    log.warn("[DeviceMonitor] 无法解析GNSS采集时间: {}", timeStr);
                    return null;
                }
            }
        }
    }

    /**
     * 提取GNSS位移详情
     */
    private String extractGnssDetail(DisplacementHistoryVO data) {
        if (data.getKeyValues() == null || data.getKeyValues().isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        for (DisplacementKeyValueVO kv : data.getKeyValues()) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            String name = kv.getSensorName() != null ? kv.getSensorName() : kv.getKey();
            sb.append(name).append(": ").append(kv.getValue());
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    /**
     * 提取渗压数据详情（含温度）
     */
    private String extractSeepageDetail(DataNewEntity entity) {
        if (entity.getResultData() == null && entity.getOriginalData() == null) return null;
        try {
            StringBuilder sb = new StringBuilder();

            if (entity.getResultData() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> result = objectMapper.readValue(entity.getResultData(), Map.class);
                for (String key : new String[]{"水位高程", "水位", "水压"}) {
                    Object val = result.get(key);
                    if (val != null) {
                        if (sb.length() > 0) sb.append("; ");
                        sb.append(key).append(": ").append(val);
                    }
                }
            }

            if (entity.getOriginalData() != null && !entity.getOriginalData().trim().isEmpty()) {
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> original = objectMapper.readValue(entity.getOriginalData(), Map.class);
                    Object temp = original.get("温度");
                    if (temp != null) {
                        if (sb.length() > 0) sb.append("; ");
                        sb.append("温度").append(": ").append(temp);
                    }
                } catch (Exception ignored) {
                }
            }

            return sb.length() > 0 ? sb.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验渗压数据是否有效：resultData 存在且至少包含一个有效字段
     */
    private boolean hasValidSeepageData(DataNewEntity entity) {
        if (entity == null || entity.getResultData() == null) {
            return false;
        }
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.readValue(entity.getResultData(), Map.class);
            String[] keys = {"水位高程", "水位", "水压"};
            for (String key : keys) {
                if (result.get(key) != null) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
