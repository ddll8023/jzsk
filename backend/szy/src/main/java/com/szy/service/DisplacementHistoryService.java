package com.szy.service;

import com.szy.external.dto.DisplacementHistoryDto;
import com.szy.external.dto.DisplacementHistoryResponse;
import com.szy.external.dto.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import java.util.*;

@Service
public class DisplacementHistoryService {

    @Value("${external.api.base-url}")
    private String baseUrl;

    @Value("${external.api.token}")
    private String token;

    @Autowired
    private RestTemplate restTemplate;

    public PageResult<DisplacementHistoryDto> getHistory(
            String startTime, String endTime, String sensor, List<Long> stationIds, int projectId, int statsFreq, int page, int size) {

        String url = baseUrl + "/manager/posPlane/allHistoricalMonitoringData";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Language", "zh-CN");
        headers.set("Industry-Code", "DZ");

        List<DisplacementHistoryDto> allData = new ArrayList<>();
        long total = 0;

        for (Long stationId : stationIds) {
            Map<String, Object> params = new HashMap<>();
            params.put("statsFreq", statsFreq);
            params.put("start_time", startTime);
            params.put("end_time", endTime);
            params.put("sensor", sensor);
            params.put("stationId", stationId);
            params.put("projectId", projectId);
            params.put("page", page);
            params.put("size", size);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<DisplacementHistoryResponse> response = restTemplate.exchange(
                url + "?statsFreq={statsFreq}&start_time={start_time}&end_time={end_time}&sensor={sensor}&stationId={stationId}&projectId={projectId}&page={page}&size={size}",
                HttpMethod.GET,
                entity,
                DisplacementHistoryResponse.class,
                params
            );
            if (response.getBody() != null && response.getBody().getData() != null) {
                allData.addAll(response.getBody().getData());
                total += response.getBody().getData().size();
            }
        }
        PageResult<DisplacementHistoryDto> result = new PageResult<>();
        result.setRecords(allData);
        result.setTotal(total);
        return result;
    }
} 