package com.jzsk.backendv2.service.dam.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzsk.backendv2.mapper.dam.DamMonitoringMapper;
import com.jzsk.backendv2.mapper.dam.SeepageDataMapper;
import com.jzsk.backendv2.mapper.mcu.DataNewMapper;
import com.jzsk.backendv2.mapper.mcu.SensorPointMapper;
import com.jzsk.backendv2.mapper.monitor.StRiversRMapper;
import com.jzsk.backendv2.pojo.dto.dam.DamTimeQueryDTO;
import com.jzsk.backendv2.pojo.dto.dam.SeepageQueryDTO;
import com.jzsk.backendv2.pojo.entity.dam.SeepageDataEntity;
import com.jzsk.backendv2.pojo.entity.mcu.DataNewEntity;
import com.jzsk.backendv2.pojo.entity.mcu.SensorPointEntity;
import com.jzsk.backendv2.pojo.entity.monitor.StRiversREntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.dam.*;
import com.jzsk.backendv2.service.dam.DamMonitoringService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 大坝监测服务实现类
 * 职责: 提供大坝监测数据的查询功能
 * 遵循KISS原则: 方法简洁,职责单一
 * 数据源: gcdd(MySQL) 和 pgsql(PostgreSQL)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DamMonitoringServiceImpl implements DamMonitoringService {

    private final SensorPointMapper sensorPointMapper;
    private final DamMonitoringMapper damMonitoringMapper;
    private final SeepageDataMapper seepageDataMapper;
    private final StRiversRMapper stRiversRMapper;
    private final DataNewMapper dataNewMapper;
    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<DamPointVO> getPoints() {
        log.info("查询所有监测点列表");

        List<SensorPointEntity> entities = sensorPointMapper.selectAll();

        List<DamPointVO> voList = entities.stream()
                .map(entity -> new DamPointVO(
                        entity.getId(),
                        String.valueOf(entity.getId()),
                        entity.getName()
                ))
                .collect(Collectors.toList());

        log.info("查询监测点列表成功,返回{}条记录", voList.size());
        return voList;
    }

    @Override
    public PageResultVO<SeepageVO> getSeepagePage(int page, int size, SeepageQueryDTO queryDTO) {
        log.info("分页查询渗压数据,页码: {}, 每页大小: {}, 查询条件: {}", page, size, queryDTO);

        String pointId = queryDTO != null ? queryDTO.getPointId() : null;
        String startTime = formatDateTime(queryDTO != null ? queryDTO.getStartTime() : null);
        String endTime = formatDateTime(queryDTO != null ? queryDTO.getEndTime() : null);

        // 将测点名称转换为数字ID（参考旧后端实现）
        String numericPointId = convertPointNameToId(pointId);
        if (pointId != null && numericPointId == null) {
            log.warn("未找到测点: {}, 返回空结果", pointId);
            return PageResultVO.empty(page, size);
        }

        // 查询总数（使用data_new表）
        long total = dataNewMapper.countPage(numericPointId, startTime, endTime);

        if (total <= 0L) {
            return PageResultVO.empty(page, size);
        }

        // 分页查询（使用data_new表）
        long offset = (page - 1L) * size;
        List<DataNewEntity> entities = dataNewMapper.selectPage(numericPointId, startTime, endTime, offset, size);

        if (entities.isEmpty()) {
            return PageResultVO.empty(page, size);
        }

        // 获取测点名称映射
        Set<String> pointIdSet = entities.stream()
                .map(DataNewEntity::getPointId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<String, String> nameMap = buildPointIdNameMap(pointIdSet);

        // Entity转VO
        List<SeepageVO> voList = entities.stream()
                .map(entity -> convertToSeepageVO(entity, nameMap))
                .collect(Collectors.toList());

        log.info("分页查询渗压数据成功,总记录数: {}, 当前页记录数: {}", total, voList.size());
        return PageUtils.buildPage(voList, total, page, size);
    }

    @Override
    public List<TimeSeriesVO> getTimeWaterElevation(DamTimeQueryDTO queryDTO) {
        log.info("查询水位高程时序数据,查询条件: {}", queryDTO);

        String mpCd = queryDTO != null ? queryDTO.getPointId() : null;
        String startTime = formatDateTime(queryDTO != null ? queryDTO.getStartTime() : null);
        String endTime = formatDateTime(queryDTO != null ? queryDTO.getEndTime() : null);

        // 将测点名称转换为数字ID（参考旧后端实现）
        String numericPointId = convertPointNameToId(mpCd);
        if (mpCd != null && numericPointId == null) {
            log.warn("未找到测点: {}, 返回空结果", mpCd);
            return Collections.emptyList();
        }

        List<DataNewEntity> entities = dataNewMapper.selectByTimeRange(numericPointId, startTime, endTime);

        // 获取测点名称
        String pointName = getPointNameByMpCd(mpCd);

        List<TimeSeriesVO> voList = entities.stream()
                .map(entity -> new TimeSeriesVO(
                        convertToLocalDateTime(entity.getTime()),
                        parseFieldFromJson(entity.getResultData(), "水位高程"),
                        pointName
                ))
                .collect(Collectors.toList());

        log.info("查询水位高程时序数据成功,返回{}条记录", voList.size());
        return voList;
    }

    @Override
    public List<TimeSeriesVO> getTimeWaterLevel(DamTimeQueryDTO queryDTO) {
        log.info("查询水位时序数据,查询条件: {}", queryDTO);

        String mpCd = queryDTO != null ? queryDTO.getPointId() : null;
        String startTime = formatDateTime(queryDTO != null ? queryDTO.getStartTime() : null);
        String endTime = formatDateTime(queryDTO != null ? queryDTO.getEndTime() : null);

        // 将测点名称转换为数字ID（参考旧后端实现）
        String numericPointId = convertPointNameToId(mpCd);
        if (mpCd != null && numericPointId == null) {
            log.warn("未找到测点: {}, 返回空结果", mpCd);
            return Collections.emptyList();
        }

        List<DataNewEntity> entities = dataNewMapper.selectByTimeRange(numericPointId, startTime, endTime);

        // 获取测点名称
        String pointName = getPointNameByMpCd(mpCd);

        List<TimeSeriesVO> voList = entities.stream()
                .map(entity -> new TimeSeriesVO(
                        convertToLocalDateTime(entity.getTime()),
                        parseFieldFromJson(entity.getResultData(), "水位"),
                        pointName
                ))
                .collect(Collectors.toList());

        log.info("查询水位时序数据成功,返回{}条记录", voList.size());
        return voList;
    }

    @Override
    public List<TimeSeriesVO> getTimeTemperature(DamTimeQueryDTO queryDTO) {
        log.info("查询温度时序数据,查询条件: {}", queryDTO);

        String mpCd = queryDTO != null ? queryDTO.getPointId() : null;
        String startTime = formatDateTime(queryDTO != null ? queryDTO.getStartTime() : null);
        String endTime = formatDateTime(queryDTO != null ? queryDTO.getEndTime() : null);

        // 将测点名称转换为数字ID（参考旧后端实现）
        String numericPointId = convertPointNameToId(mpCd);
        if (mpCd != null && numericPointId == null) {
            log.warn("未找到测点: {}, 返回空结果", mpCd);
            return Collections.emptyList();
        }

        List<DataNewEntity> entities = dataNewMapper.selectByTimeRange(numericPointId, startTime, endTime);

        // 获取测点名称
        String pointName = getPointNameByMpCd(mpCd);

        // 温度字段从originalData中提取（参考旧后端getTimeTemperature）
        List<TimeSeriesVO> voList = entities.stream()
                .map(entity -> new TimeSeriesVO(
                        convertToLocalDateTime(entity.getTime()),
                        parseFieldFromJson(entity.getOriginalData(), "温度"),
                        pointName
                ))
                .collect(Collectors.toList());

        log.info("查询温度时序数据成功,返回{}条记录", voList.size());
        return voList;
    }

    @Override
    public List<TimeSeriesVO> getTimeWaterPressure(DamTimeQueryDTO queryDTO) {
        log.info("查询水压时序数据,查询条件: {}", queryDTO);

        String mpCd = queryDTO != null ? queryDTO.getPointId() : null;
        String startTime = formatDateTime(queryDTO != null ? queryDTO.getStartTime() : null);
        String endTime = formatDateTime(queryDTO != null ? queryDTO.getEndTime() : null);

        // 将测点名称转换为数字ID（参考旧后端实现）
        String numericPointId = convertPointNameToId(mpCd);
        if (mpCd != null && numericPointId == null) {
            log.warn("未找到测点: {}, 返回空结果", mpCd);
            return Collections.emptyList();
        }

        List<DataNewEntity> entities = dataNewMapper.selectByTimeRange(numericPointId, startTime, endTime);

        // 获取测点名称
        String pointName = getPointNameByMpCd(mpCd);

        List<TimeSeriesVO> voList = entities.stream()
                .map(entity -> new TimeSeriesVO(
                        convertToLocalDateTime(entity.getTime()),
                        parseFieldFromJson(entity.getResultData(), "水压"),
                        pointName
                ))
                .collect(Collectors.toList());

        log.info("查询水压时序数据成功,返回{}条记录", voList.size());
        return voList;
    }

    @Override
    public List<LatestWaterElevationVO> getLatestWaterElevation() {
        log.info("查询各测点最新水位高程");

        List<com.jzsk.backendv2.pojo.entity.dam.DataNewEntity> latestList = damMonitoringMapper.selectLatestForAllPoints();

        // 收集所有pointId并去重
        Set<Long> pointIds = latestList.stream()
                .map(com.jzsk.backendv2.pojo.entity.dam.DataNewEntity::getPointId)
                .filter(Objects::nonNull)
                .filter(pid -> {
                    try {
                        Long.parseLong(pid);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        // 查询测点名称映射
        Map<Long, String> idNameMap = new HashMap<>();
        if (pointIds != null && !pointIds.isEmpty()) {
            List<SensorPointEntity> sensorPoints = sensorPointMapper.selectNameByIds(new ArrayList<>(pointIds));
            for (SensorPointEntity sp : sensorPoints) {
                idNameMap.put(sp.getId(), sp.getName());
            }
        }

        List<LatestWaterElevationVO> result = new ArrayList<>();
        for (com.jzsk.backendv2.pojo.entity.dam.DataNewEntity data : latestList) {
            LatestWaterElevationVO vo = new LatestWaterElevationVO();
            vo.setPointId(data.getPointId());

            // 获取测点名称
            String name = data.getPointId();
            if (data.getPointId() != null) {
                try {
                    Long pid = Long.parseLong(data.getPointId());
                    name = idNameMap.getOrDefault(pid, data.getPointId());
                } catch (NumberFormatException e) {
                    // 不是数字，直接用原值
                }
            }
            vo.setPointName(name);

            // 解析resultData中的水位高程
            Double waterElevation = parseWaterElevation(data.getResultData());
            vo.setWaterElevation(waterElevation);

            // 转换时间（直接赋值，已经是LocalDateTime）
            if (data.getTime() != null) {
                vo.setTime(data.getTime());
            }

            result.add(vo);
        }

        log.info("查询各测点最新水位高程成功,返回{}条记录", result.size());
        return result;
    }

    @Override
    public PageResultVO<RiverWaterLevelVO> getRiverWaterLevelPage(int page, int size, SeepageQueryDTO queryDTO) {
        log.info("分页查询河道水位数据,页码: {}, 每页大小: {}, 查询条件: {}", page, size, queryDTO);

        String stcd = queryDTO != null ? queryDTO.getStcd() : null;

        // 查询总数
        long total = stRiversRMapper.countPage(stcd);

        if (total <= 0L) {
            return PageResultVO.empty(page, size);
        }

        // 分页查询
        long offset = (page - 1L) * size;
        List<StRiversREntity> entities = stRiversRMapper.selectPage(stcd, offset, size);

        if (entities.isEmpty()) {
            return PageResultVO.empty(page, size);
        }

        // Entity转VO
        List<RiverWaterLevelVO> voList = entities.stream()
                .map(this::convertToRiverWaterLevelVO)
                .collect(Collectors.toList());

        log.info("分页查询河道水位数据成功,总记录数: {}, 当前页记录数: {}", total, voList.size());
        return PageUtils.buildPage(voList, total, page, size);
    }

    @Override
    public PageResultVO<SeepageFlowVO> getSeepageFlowPage(int page, int size, SeepageQueryDTO queryDTO) {
        log.info("分页查询渗流量数据,页码: {}, 每页大小: {}, 查询条件: {}", page, size, queryDTO);

        Integer stationId = null;
        if (queryDTO != null && queryDTO.getPointId() != null) {
            try {
                stationId = Integer.parseInt(queryDTO.getPointId());
            } catch (NumberFormatException e) {
                log.warn("测点编号格式错误: {}", queryDTO.getPointId());
            }
        }
        String startTime = formatDateTime(queryDTO != null ? queryDTO.getStartTime() : null);
        String endTime = formatDateTime(queryDTO != null ? queryDTO.getEndTime() : null);

        // 查询总数（使用 seepage_data 表，gcdd数据源）
        long total = seepageDataMapper.countPage(stationId, startTime, endTime);

        if (total <= 0L) {
            return PageResultVO.empty(page, size);
        }

        // 分页查询（使用 seepage_data 表，gcdd数据源）
        long offset = (page - 1L) * size;
        List<SeepageDataEntity> entities = seepageDataMapper.selectPage(stationId, startTime, endTime, offset, size);

        if (entities.isEmpty()) {
            return PageResultVO.empty(page, size);
        }

        // Entity转VO
        List<SeepageFlowVO> voList = entities.stream()
                .map(this::convertToSeepageFlowVO)
                .collect(Collectors.toList());

        log.info("分页查询渗流量数据成功,总记录数: {}, 当前页记录数: {}", total, voList.size());
        return PageUtils.buildPage(voList, total, page, size);
    }

    @Override
    public List<SeepageVO> getSeepageLatestAll() {
        log.info("批量查询所有渗压测站最新数据");

        // 查询所有测点最新数据
        List<DataNewEntity> entities = dataNewMapper.selectLatestForAllPoints();

        if (entities.isEmpty()) {
            log.info("未查询到渗压测站数据");
            return Collections.emptyList();
        }

        // 获取测点名称映射（用于将数字ID转换为名称）
        Map<String, String> idToNameMap = new HashMap<>();
        Set<String> pointIdSet = entities.stream()
                .map(DataNewEntity::getPointId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!pointIdSet.isEmpty()) {
            // 收集所有有效的数字ID
            List<Long> ids = new ArrayList<>();
            for (String pid : pointIdSet) {
                try {
                    ids.add(Long.parseLong(pid));
                } catch (NumberFormatException e) {
                    // 不是数字，跳过
                }
            }

            // 批量查询测点信息
            if (!ids.isEmpty()) {
                List<SensorPointEntity> sensorPoints = sensorPointMapper.selectNameByIds(ids);
                for (SensorPointEntity sp : sensorPoints) {
                    String numId = String.valueOf(sp.getId());
                    String name = sp.getName();
                    idToNameMap.put(numId, name);
                }
            }
        }

        // Entity转VO，使用测点名称作为pointId以匹配前端
        final Map<String, String> finalIdToNameMap = idToNameMap;
        List<SeepageVO> voList = entities.stream()
                .map(entity -> {
                    // 优先使用名称，如果没有则使用数字ID
                    String pointId = finalIdToNameMap.getOrDefault(entity.getPointId(), entity.getPointId());
                    // 转换时间类型
                    LocalDateTime localTime = convertToLocalDateTime(entity.getTime());
                    return new SeepageVO(
                            pointId,  // 使用测点名称
                            localTime,
                            entity.getOriginalData(),
                            entity.getResultData(),
                            pointId   // pointName 也用名称
                    );
                })
                .collect(Collectors.toList());

        log.info("批量查询渗压测站最新数据成功,返回{}条记录", voList.size());
        return voList;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 格式化日期时间
     *
     * @param dateTime 日期时间
     * @return 格式化后的字符串，格式：yyyy-MM-dd HH:mm:ss
     */
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * 构建测点编码到名称的映射
     *
     * @param mpCdSet 测点编码集合
     * @return 编码到名称的映射Map
     */
    private Map<String, String> buildMpCdNameMap(Set<String> mpCdSet) {
        Map<String, String> nameMap = new HashMap<>();
        if (mpCdSet == null || mpCdSet.isEmpty()) {
            return nameMap;
        }

        // 收集所有有效的数字ID
        List<Long> ids = new ArrayList<>();
        for (String mpCd : mpCdSet) {
            try {
                ids.add(Long.parseLong(mpCd));
            } catch (NumberFormatException e) {
                // 不是数字，跳过
            }
        }

        if (ids.isEmpty()) {
            return nameMap;
        }

        // 一次性批量查询测点名称
        List<SensorPointEntity> entities = sensorPointMapper.selectNameByIds(ids);
        for (SensorPointEntity entity : entities) {
            nameMap.put(String.valueOf(entity.getId()), entity.getName());
        }

        return nameMap;
    }

    /**
     * 构建测点ID到名称的映射（用于data_new表）
     *
     * @param pointIdSet 测点ID集合
     * @return ID到名称的映射Map
     */
    private Map<String, String> buildPointIdNameMap(Set<String> pointIdSet) {
        Map<String, String> nameMap = new HashMap<>();
        if (pointIdSet == null || pointIdSet.isEmpty()) {
            return nameMap;
        }

        // 收集所有有效的数字ID
        List<Long> ids = new ArrayList<>();
        for (String pointId : pointIdSet) {
            try {
                ids.add(Long.parseLong(pointId));
            } catch (NumberFormatException e) {
                // 不是数字，跳过
            }
        }

        if (ids.isEmpty()) {
            return nameMap;
        }

        // 一次性批量查询测点名称
        List<SensorPointEntity> entities = sensorPointMapper.selectNameByIds(ids);
        for (SensorPointEntity entity : entities) {
            nameMap.put(String.valueOf(entity.getId()), entity.getName());
        }

        return nameMap;
    }

    /**
     * 将测点名称转换为数字ID
     * 参考旧后端 DataNewController 实现：先用名称查 sensor_point 表得到数字ID，再用数字ID查询 data_new 表
     *
     * @param pointName 测点名称（如 "P0108043"）
     * @return 数字ID（如 "10108243"），如果名称为空或查不到则返回null
     */
    private String convertPointNameToId(String pointName) {
        if (!StringUtils.hasText(pointName)) {
            return null;
        }
        // 先尝试将字符串直接转为数字ID（如果前端传的就是数字ID）
        try {
            Long.parseLong(pointName);
            return pointName;
        } catch (NumberFormatException e) {
            // 不是数字，尝试按名称查询
        }
        // 通过名称查询测点表，获取数字ID
        SensorPointEntity sensorPoint = sensorPointMapper.selectByName(pointName);
        if (sensorPoint != null) {
            return String.valueOf(sensorPoint.getId());
        }
        return null;
    }

    /**
     * 根据测点编码获取测点名称
     *
     * @param mpCd 测点编码
     * @return 测点名称，如果未找到则返回原始编码
     */
    private String getPointNameByMpCd(String mpCd) {
        if (!StringUtils.hasText(mpCd)) {
            return null;
        }
        try {
            Long id = Long.parseLong(mpCd);
            SensorPointEntity entity = sensorPointMapper.selectById(id);
            return entity != null ? entity.getName() : mpCd;
        } catch (NumberFormatException e) {
            return mpCd;
        }
    }

    /**
     * 解析resultData中的水位高程
     *
     * @param resultData JSON字符串
     * @return 水位高程值，如果解析失败返回null
     */
    private Double parseWaterElevation(String resultData) {
        return parseFieldFromJson(resultData, "水位高程");
    }

    /**
     * 从JSON字符串中解析指定字段值
     *
     * @param jsonData JSON字符串（可为null）
     * @param fieldKey 要提取的字段key
     * @return 字段值，如果解析失败或字段不存在返回null
     */
    private Double parseFieldFromJson(String jsonData, String fieldKey) {
        if (!StringUtils.hasText(jsonData)) {
            return null;
        }
        try {
            Map<String, Object> parsed = objectMapper.readValue(jsonData, Map.class);
            Object val = parsed.get(fieldKey);
            if (val != null) {
                if (val instanceof Number) {
                    return ((Number) val).doubleValue();
                }
                return Double.parseDouble(val.toString());
            }
        } catch (Exception e) {
            log.warn("解析字段{}失败: {}", fieldKey, e.getMessage());
        }
        return null;
    }

    /**
     * 将OffsetDateTime转换为LocalDateTime
     *
     * @param offsetDateTime OffsetDateTime对象
     * @return LocalDateTime，如果输入为null则返回null
     */
    private LocalDateTime convertToLocalDateTime(java.time.OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.atZoneSameInstant(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Entity转SeepageVO（用于data_new表）
     *
     * @param entity 渗压数据实体
     * @param nameMap 测点名称映射
     * @return 渗压数据VO
     */
    private SeepageVO convertToSeepageVO(DataNewEntity entity, Map<String, String> nameMap) {
        if (entity == null) {
            return null;
        }

        // 将OffsetDateTime转换为LocalDateTime
        LocalDateTime localDateTime = null;
        if (entity.getTime() != null) {
            localDateTime = entity.getTime().atZoneSameInstant(java.time.ZoneId.systemDefault()).toLocalDateTime();
        }

        return new SeepageVO(
                entity.getPointId(),
                localDateTime,
                entity.getOriginalData(),
                entity.getResultData(),
                nameMap.getOrDefault(entity.getPointId(), entity.getPointId())
        );
    }

    /**
     * Entity转SeepageFlowVO（用于 seepage_data 表）
     *
     * @param entity 渗流量数据实体
     * @return 渗流量数据VO
     */
    private SeepageFlowVO convertToSeepageFlowVO(SeepageDataEntity entity) {
        if (entity == null) {
            return null;
        }
        return new SeepageFlowVO(
                entity.getRecordId(),
                String.valueOf(entity.getStationId()),
                entity.getRecordTime(),
                entity.getSeepageFlow(),
                entity.getRemarks()
        );
    }

    /**
     * Entity转RiverWaterLevelVO
     *
     * @param entity 河道水位数据实体
     * @return 河道水位数据VO
     */
    private RiverWaterLevelVO convertToRiverWaterLevelVO(StRiversREntity entity) {
        if (entity == null) {
            return null;
        }
        return new RiverWaterLevelVO(
                entity.getStcd(),
                entity.getTm(),
                entity.getZ1(),
                entity.getQ1()
        );
    }
}
