package com.jzsk.backendv2.service.impl.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzsk.backendv2.mapper.mcu.DataNewMapper;
import com.jzsk.backendv2.mapper.monitor.StPptnHourMapper;
import com.jzsk.backendv2.mapper.monitor.StRiversRMapper;
import com.jzsk.backendv2.pojo.entity.mcu.DataNewEntity;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final StRiversRMapper stRiversRMapper;
    private final StPptnHourMapper stPptnHourMapper;
    private final ObjectMapper objectMapper;
    private final DeviceFaultRecordService deviceFaultRecordService;

    /** GNSS超时阈值（分钟）: 60分钟采集周期 + 1分钟缓冲 */
    private static final long GNSS_TIMEOUT_MINUTES = 61;
    /** 雨水情超时阈值（分钟）: 5分钟采集周期 + 1分钟缓冲 */
    private static final long RAIN_TIMEOUT_MINUTES = 6;
    /** 渗流渗压超时阈值（分钟）: 10分钟采集周期 + 1分钟缓冲 */
    private static final long SEEPAGE_TIMEOUT_MINUTES = 11;

    /** GNSS测站配置 */
    private static final String GNSS_STATION_IDS = "33210,33214,33216,33212,33215,33211,33217,33213";
    private static final String GNSS_SENSOR = "L1_GP";
    private static final Integer GNSS_PROJECT_ID = 1681;

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
        List<DeviceStatusVO> devices = checkGnssDevices();
        processFaultRecords(devices);
        return buildTypeResult(devices);
    }

    @Override
    public DeviceTypeStatusVO getRainStatus() {
        List<DeviceStatusVO> devices = checkRainDevices();
        processFaultRecords(devices);
        return buildTypeResult(devices);
    }

    @Override
    public DeviceTypeStatusVO getSeepageStatus() {
        List<DeviceStatusVO> devices = checkSeepageDevices();
        processFaultRecords(devices);
        return buildTypeResult(devices);
    }

    /**
     * 检测GNSS设备状态
     * 数据源: 外部HTTP API
     */
    private List<DeviceStatusVO> checkGnssDevices() {
        List<DeviceStatusVO> devices = new ArrayList<>();
        List<DisplacementHistoryVO> gnssData;

        try {
            gnssData = displacementHistoryService.getDisplacementLatest(
                    GNSS_STATION_IDS, GNSS_SENSOR, GNSS_PROJECT_ID);
        } catch (Exception e) {
            log.error("[DeviceMonitor] GNSS接口调用失败，所有GNSS设备标记为离线", e);
            GNSS_NAME_MAP.forEach((id, name) ->
                    devices.add(buildDevice(String.valueOf(id), name, "gnss", "offline", null, "接口连接失败")));
            return devices;
        }

        if (gnssData == null || gnssData.isEmpty()) {
            log.warn("[DeviceMonitor] GNSS接口返回空数据，所有GNSS设备标记为离线");
            GNSS_NAME_MAP.forEach((id, name) ->
                    devices.add(buildDevice(String.valueOf(id), name, "gnss", "offline", null, "无数据返回")));
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
                devices.add(buildDevice(String.valueOf(stationId), name, "gnss", "offline", null, "无采集数据"));
                continue;
            }

            LocalDateTime collectTime = parseCollectTime(data.getCollectTime());
            String status = determineStatus(collectTime, GNSS_TIMEOUT_MINUTES);
            String detail = extractGnssDetail(data);

            devices.add(buildDevice(String.valueOf(stationId), name, "gnss", status, collectTime, detail));
        }

        return devices;
    }

    /**
     * 检测雨水情设备状态
     * 数据源: SQL Server (水位ST_RIVERS_R + 雨量ST_PPTN_HOUR)
     */
    private List<DeviceStatusVO> checkRainDevices() {
        List<DeviceStatusVO> devices = new ArrayList<>();
        String deviceCode = "RAIN_WATER_MAIN";
        String deviceName = "坝前雨量水位站";

        StRiversREntity latestWater;
        StPptnHourEntity latestRain;

        try {
            latestWater = stRiversRMapper.selectAll().stream()
                    .max((a, b) -> a.getTm() != null && b.getTm() != null ? a.getTm().compareTo(b.getTm()) : 0)
                    .orElse(null);
        } catch (Exception e) {
            log.error("[DeviceMonitor] 水位数据查询失败", e);
            latestWater = null;
        }

        try {
            latestRain = stPptnHourMapper.selectAll().stream()
                    .max((a, b) -> a.getTm() != null && b.getTm() != null ? a.getTm().compareTo(b.getTm()) : 0)
                    .orElse(null);
        } catch (Exception e) {
            log.error("[DeviceMonitor] 雨量数据查询失败", e);
            latestRain = null;
        }

        if (latestWater == null && latestRain == null) {
            devices.add(buildDevice(deviceCode, deviceName, "rain", "offline", null, "水位和雨量接口均无数据"));
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

        devices.add(buildDevice(deviceCode, deviceName, "rain", status, lastTime,
                detail.length() > 0 ? detail.toString() : null));
        return devices;
    }

    /**
     * 检测渗流渗压设备状态
     * 数据源: PostgreSQL (data_new表)
     */
    private List<DeviceStatusVO> checkSeepageDevices() {
        List<DeviceStatusVO> devices = new ArrayList<>();
        List<DataNewEntity> seepageData;

        try {
            seepageData = dataNewMapper.selectLatestForAllPoints();
        } catch (Exception e) {
            log.error("[DeviceMonitor] 渗压数据查询失败，所有渗压设备标记为离线", e);
            devices.add(buildDevice("SEEPAGE_ALL", "渗压设备(全部)", "seepage", "offline", null, "数据库连接失败"));
            return devices;
        }

        if (seepageData == null || seepageData.isEmpty()) {
            devices.add(buildDevice("SEEPAGE_ALL", "渗压设备(全部)", "seepage", "offline", null, "无数据"));
            return devices;
        }

        for (DataNewEntity entity : seepageData) {
            String pointId = entity.getPointId();
            OffsetDateTime offsetTime = entity.getTime();
            LocalDateTime collectTime = offsetTime != null
                    ? offsetTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
                    : null;

            String status = determineStatus(collectTime, SEEPAGE_TIMEOUT_MINUTES);
            String detail = extractSeepageDetail(entity);

            devices.add(buildDevice(pointId, pointId, "seepage", status, collectTime, detail));
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
     * online: 采集时间在阈值内
     * abnormal: 采集时间超过阈值（采集异常）
     * offline: 无采集时间（已在调用方处理）
     */
    private String determineStatus(LocalDateTime lastCollectTime, long timeoutMinutes) {
        if (lastCollectTime == null) {
            return "offline";
        }
        long minutesDiff = ChronoUnit.MINUTES.between(lastCollectTime, LocalDateTime.now());
        return minutesDiff <= timeoutMinutes ? "online" : "abnormal";
    }

    private DeviceStatusVO buildDevice(String code, String name, String type, String status,
                                        LocalDateTime lastCollectTime, String detail) {
        return new DeviceStatusVO(code, name, type, status, lastCollectTime, detail);
    }

    /**
     * 遍历设备列表，将状态变化同步到故障记录
     */
    private void processFaultRecords(List<DeviceStatusVO> devices) {
        for (DeviceStatusVO device : devices) {
            try {
                deviceFaultRecordService.processDeviceStatus(device);
            } catch (Exception e) {
                log.error("[DeviceMonitor] 故障记录处理失败: {}/{}", device.getType(), device.getCode(), e);
            }
        }
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
     * 提取渗压数据详情
     */
    private String extractSeepageDetail(DataNewEntity entity) {
        if (entity.getResultData() == null) return null;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.readValue(entity.getResultData(), Map.class);
            StringBuilder sb = new StringBuilder();
            String[] keys = {"水位高程", "水位", "水压"};
            for (String key : keys) {
                Object val = result.get(key);
                if (val != null) {
                    if (sb.length() > 0) sb.append("; ");
                    sb.append(key).append(": ").append(val);
                }
            }
            return sb.length() > 0 ? sb.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
