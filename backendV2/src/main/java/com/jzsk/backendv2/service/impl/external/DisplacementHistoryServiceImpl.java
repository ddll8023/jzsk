package com.jzsk.backendv2.service.impl.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.pojo.dto.dam.DisplacementQueryDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.dam.DisplacementHistoryVO;
import com.jzsk.backendv2.pojo.vo.dam.DisplacementKeyValueVO;
import com.jzsk.backendv2.service.external.DisplacementHistoryService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 外部位移历史服务实现类
 * 职责: 提供外部GNSS位移历史数据的查询功能
 * 遵循KISS原则: 方法简洁,职责单一
 * 数据来源: 外部GNSS API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DisplacementHistoryServiceImpl implements DisplacementHistoryService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${external.api.base-url:}")
    private String baseUrl;

    @Value("${external.api.token:}")
    private String token;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public PageResultVO<DisplacementHistoryVO> getDisplacementHistoryPage(int page, int size,
            DisplacementQueryDTO queryDTO) {
        return getDisplacementHistoryPageRaw(page, size,
                queryDTO.getStartTime(),
                queryDTO.getEndTime(),
                queryDTO.getSensor(),
                queryDTO.getStationIds(),
                queryDTO.getProjectId(),
                queryDTO.getStatsFreq());
    }

    @Override
    public PageResultVO<DisplacementHistoryVO> getDisplacementHistoryPageRaw(int page, int size,
            LocalDateTime startTime, LocalDateTime endTime,
            String sensor, String stationIds, Integer projectId, Integer statsFreq) {
        log.info("分页查询位移历史数据,页码: {}, 每页大小: {}", page, size);

        // 解析站点ID列表
        List<Long> stationIdList = parseStationIds(stationIds);

        // 调用外部API获取数据（支持真实分页）
        PageResultVO<DisplacementHistoryVO> pageResult = fetchDisplacementHistoryFromExternal(
                startTime,
                endTime,
                sensor,
                stationIdList,
                projectId != null ? projectId : 0,
                statsFreq != null ? statsFreq : 0,
                page,
                size);

        log.info("分页查询位移历史数据成功,总记录数: {}, 当前页记录数: {}",
                pageResult.getTotal(), pageResult.getList().size());
        return pageResult;
    }

    /**
     * 从外部API获取位移历史数据（逐站点循环调用，参照旧项目实现）
     */
    private PageResultVO<DisplacementHistoryVO> fetchDisplacementHistoryFromExternal(
            LocalDateTime startTime,
            LocalDateTime endTime,
            String sensor,
            List<Long> stationIds,
            int projectId,
            int statsFreq,
            int page,
            int size) {

        // 如果外部API配置为空，返回空分页结果
        if (!StringUtils.hasText(baseUrl)) {
            log.warn("外部API配置为空,返回空结果");
            return PageResultVO.empty(page, size);
        }

        // 站点列表为空时返回空结果
        if (stationIds == null || stationIds.isEmpty()) {
            log.warn("站点ID列表为空,返回空结果");
            return PageResultVO.empty(page, size);
        }

        List<DisplacementHistoryVO> allData = new ArrayList<>();
        long totalCount = 0L;

        // 格式化时间参数
        String startTimeStr = startTime != null ? startTime.format(DATE_TIME_FORMATTER) : "";
        String endTimeStr = endTime != null ? endTime.format(DATE_TIME_FORMATTER) : "";

        try {
            String url = baseUrl + "/manager/posPlane/allHistoricalMonitoringData";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Language", "zh-CN");
            headers.set("Industry-Code", "DZ");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            // 参照旧项目实现：逐站点循环调用外部API
            for (Long stationId : stationIds) {
                String requestUrl = url + "?statsFreq=" + statsFreq
                        + "&start_time=" + startTimeStr
                        + "&end_time=" + endTimeStr
                        + "&sensor=" + (sensor != null ? sensor : "")
                        + "&stationId=" + stationId
                        + "&projectId=" + projectId
                        + "&page=" + page
                        + "&size=" + size;

                log.debug("调用外部位移API,站点: {}, URL: {}", stationId, requestUrl);

                ResponseEntity<Map> response = restTemplate.exchange(
                        requestUrl,
                        HttpMethod.GET,
                        entity,
                        Map.class);

                if (response.getBody() != null) {
                    ExternalApiResult result = parseExternalResponseWithTotal(response.getBody());
                    allData.addAll(result.getData());
                    totalCount += result.getTotal();
                }
            }
        } catch (HttpClientErrorException e) {
            // HTTP 4xx 客户端错误
            log.warn("外部位移API返回客户端错误,状态码: {}, 错误信息: {}", e.getStatusCode(), e.getMessage());
            throw new BusinessException(502, "外部位移API请求失败：" + e.getMessage());
        } catch (HttpServerErrorException e) {
            // HTTP 5xx 服务器错误
            log.warn("外部位移API返回服务端错误,状态码: {}, 错误信息: {}", e.getStatusCode(), e.getMessage());
            throw new BusinessException(502, "外部位移API服务异常：" + e.getMessage());
        } catch (RestClientException e) {
            // RestTemplate 底层异常（连接超时、网络不可达等）
            log.error("调用外部位移API网络异常: {}", e.getMessage(), e);
            throw new BusinessException(502, "外部位移API调用失败：" + e.getMessage());
        } catch (Exception e) {
            // 兜底异常
            log.error("调用外部位移API未知异常: {}", e.getMessage(), e);
            throw new BusinessException(500, "外部位移API调用异常：" + e.getMessage());
        }

        if (totalCount <= 0L) {
            return PageResultVO.empty(page, size);
        }

        return PageUtils.buildPage(allData, totalCount, page, size);
    }

    /**
     * 解析外部API响应（包含总数）
     */
    @SuppressWarnings("unchecked")
    private ExternalApiResult parseExternalResponseWithTotal(Map<String, Object> response) {
        ExternalApiResult result = new ExternalApiResult();

        try {
            Integer code = (Integer) response.get("code");
            if (code == null || code != 200) {
                log.warn("外部API返回错误码: {}", code);
                return result;
            }

            // 尝试从响应中获取总数
            Object totalObj = response.get("total");
            if (totalObj instanceof Number) {
                result.setTotal(((Number) totalObj).longValue());
            }

            List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
            if (dataList == null || dataList.isEmpty()) {
                return result;
            }

            for (Map<String, Object> data : dataList) {
                DisplacementHistoryVO vo = new DisplacementHistoryVO();

                Object collectTime = data.get("collectTime");
                vo.setCollectTime(collectTime != null ? collectTime.toString() : null);

                Object azimuth = data.get("azimuth");
                vo.setAzimuth(azimuth != null ? ((Number) azimuth).doubleValue() : null);

                vo.setStationName((String) data.get("stationName"));

                Object deviceId = data.get("deviceId");
                vo.setDeviceId(deviceId != null ? deviceId.toString() : null);

                vo.setDeviceSn((String) data.get("deviceSn"));

                Object stationId = data.get("stationId");
                vo.setStationId(stationId != null ? ((Number) stationId).longValue() : null);

                // 解析keyValues
                List<Map<String, Object>> keyValuesList = (List<Map<String, Object>>) data.get("keyValues");
                if (keyValuesList != null) {
                    List<DisplacementKeyValueVO> keyValueVOList = new ArrayList<>();
                    for (Map<String, Object> kv : keyValuesList) {
                        DisplacementKeyValueVO kvVO = new DisplacementKeyValueVO();
                        kvVO.setSensorName((String) kv.get("sensorName"));
                        kvVO.setSensor((String) kv.get("sensor"));
                        Object val = kv.get("value");
                        kvVO.setValue(val != null ? val.toString() : null);
                        kvVO.setKey((String) kv.get("key"));
                        keyValueVOList.add(kvVO);
                    }
                    vo.setKeyValues(keyValueVOList);
                }

                result.getData().add(vo);
            }

            // 如果响应中没有total字段，则使用数据列表大小
            if (result.getTotal() == 0 && !result.getData().isEmpty()) {
                // 需要根据实际情况估算或请求总数
                result.setTotal(result.getData().size());
            }
        } catch (Exception e) {
            log.error("解析外部API响应失败: {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 解析站点ID列表
     */
    private List<Long> parseStationIds(String stationIds) {
        List<Long> result = new ArrayList<>();
        if (!StringUtils.hasText(stationIds)) {
            return result;
        }

        try {
            String[] ids = stationIds.split(",");
            for (String id : ids) {
                if (StringUtils.hasText(id.trim())) {
                    result.add(Long.parseLong(id.trim()));
                }
            }
        } catch (NumberFormatException e) {
            log.warn("解析站点ID失败: {}", e.getMessage());
        }

        return result;
    }

    @Override
    public List<DisplacementHistoryVO> getDisplacementLatest(String stationIds, String sensor, Integer projectId) {
        log.info("获取所有测站最新位移数据, 站点IDs: {}", stationIds);

        // 解析站点ID列表
        List<Long> stationIdList = parseStationIds(stationIds);
        if (stationIdList == null || stationIdList.isEmpty()) {
            log.warn("站点ID列表为空, 返回空结果");
            return Collections.emptyList();
        }

        // 近1小时时间范围，减少外部API数据量
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusHours(1);
        String startTimeStr = startTime.format(DATE_TIME_FORMATTER);
        String endTimeStr = endTime.format(DATE_TIME_FORMATTER);

        // 如果外部API配置为空，返回空结果
        if (!StringUtils.hasText(baseUrl)) {
            log.warn("外部API配置为空, 返回空结果");
            return Collections.emptyList();
        }

        // 并行调用所有站点（优化：8次调用并行执行）
        List<CompletableFuture<List<DisplacementHistoryVO>>> futures = new ArrayList<>();
        for (Long stationId : stationIdList) {
            CompletableFuture<List<DisplacementHistoryVO>> future = CompletableFuture.supplyAsync(() -> {
                return fetchSingleStationLatest(
                        stationId,
                        startTimeStr,
                        endTimeStr,
                        sensor,
                        projectId != null ? projectId : 0);
            });
            futures.add(future);
        }

        // 等待所有并行任务完成（最多等待30秒）
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.warn("并行获取测站数据超时或中断: {}", e.getMessage());
        }

        // 收集所有结果
        List<DisplacementHistoryVO> result = new ArrayList<>();
        for (CompletableFuture<List<DisplacementHistoryVO>> future : futures) {
            try {
                List<DisplacementHistoryVO> data = future.get();
                if (data != null && !data.isEmpty()) {
                    result.addAll(data);
                }
            } catch (Exception e) {
                log.warn("获取测站数据失败: {}", e.getMessage());
            }
        }

        log.info("获取所有测站最新位移数据成功, 返回{}条记录", result.size());
        return result;
    }

    /**
     * 获取单个站点的最新数据
     */
    private List<DisplacementHistoryVO> fetchSingleStationLatest(
            Long stationId,
            String startTimeStr,
            String endTimeStr,
            String sensor,
            int projectId) {

        try {
            String url = baseUrl + "/manager/posPlane/allHistoricalMonitoringData";
            String requestUrl = url + "?statsFreq=0"
                    + "&start_time=" + startTimeStr
                    + "&end_time=" + endTimeStr
                    + "&sensor=" + (sensor != null ? sensor : "")
                    + "&stationId=" + stationId
                    + "&projectId=" + projectId
                    + "&page=1"
                    + "&size=1"; // 只取最新一条

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Language", "zh-CN");
            headers.set("Industry-Code", "DZ");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.GET,
                    entity,
                    Map.class);

            if (response.getBody() != null) {
                ExternalApiResult result = parseExternalResponseWithTotal(response.getBody());
                return result.getData();
            }
        } catch (Exception e) {
            log.warn("获取站点 {} 最新数据失败: {}", stationId, e.getMessage());
        }

        return Collections.emptyList();
    }

    /**
     * 内部类：封装外部API响应结果
     */
    private static class ExternalApiResult {
        private List<DisplacementHistoryVO> data = new ArrayList<>();
        private long total = 0L;

        public List<DisplacementHistoryVO> getData() {
            return data;
        }

        public void setData(List<DisplacementHistoryVO> data) {
            this.data = data;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }
    }
}
