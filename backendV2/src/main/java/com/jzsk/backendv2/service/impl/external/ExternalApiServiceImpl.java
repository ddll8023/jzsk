package com.jzsk.backendv2.service.impl.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzsk.backendv2.pojo.dto.external.latestmonitor.LatestMonitorApiResponse;
import com.jzsk.backendv2.pojo.dto.external.latestmonitor.LatestMonitorStationDto;
import com.jzsk.backendv2.service.external.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 外部监测平台服务实现类
 * 职责：调用外部GNSS位移监测平台API，获取最新监测数据
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalApiServiceImpl implements ExternalApiService {

    @Value("${external.api.base-url:}")
    private String externalApiBaseUrl;

    @Value("${external.api.token:}")
    private String externalApiToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public LatestMonitorStationDto getLatestMonitoringData(String projectId, String stationId) {
        if (externalApiBaseUrl == null || externalApiBaseUrl.isEmpty()) {
            log.warn("[ExternalApiService] 外部API配置缺失，请检查 external.api.base-url 配置");
            return null;
        }
        String url = externalApiBaseUrl + "/manager/project/latestMonitoringDataStatistics/" + projectId
                + "?stationId=" + stationId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + externalApiToken);
        headers.set("Language", "zh-CN");
        headers.set("Industry-Code", "DZ");
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            String rawResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            ).getBody();

            if (rawResponse == null) {
                log.warn("[ExternalApiService] 外部API返回空响应 stationId={}", stationId);
                return null;
            }

            LatestMonitorApiResponse apiResponse = objectMapper.readValue(rawResponse, LatestMonitorApiResponse.class);

            if (apiResponse == null || apiResponse.getData() == null || apiResponse.getData().length == 0) {
                log.warn("[ExternalApiService] 外部API返回数据为空 stationId={}, response={}", stationId, rawResponse);
                return null;
            }

            return apiResponse.getData()[0];
        } catch (Exception e) {
            log.error("[ExternalApiService] 调用外部API异常 stationId={}: {}", stationId, e.getMessage());
            return null;
        }
    }
}
