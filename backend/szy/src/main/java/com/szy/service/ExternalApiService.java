package com.szy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szy.external.dto.ExternalApiResponse;
import com.szy.external.dto.ExternalStationDto;
import com.szy.external.dto.latestmonitor.LatestMonitorApiResponse;
import com.szy.external.dto.latestmonitor.LatestMonitorStationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Added for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ExternalApiService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);

    @Value("${external.api.base-url}")
    private String externalApiBaseUrl;

    @Value("${external.api.token}")
    private String externalApiToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ExternalApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<ExternalStationDto> findAllStations(String projectId) {
        String url = externalApiBaseUrl + "/manager/station/findAllStation";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("projectId", projectId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + externalApiToken);
        headers.set("Language", "zh-CN");
        headers.set("Industry-Code", "DZ");
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            // First, fetch the raw string response
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (rawResponse.getStatusCode() == HttpStatus.OK && rawResponse.getBody() != null) {
                logger.info("Raw external API response: {}", rawResponse.getBody());

                // Manually deserialize the raw string to ExternalApiResponse using ObjectMapper
                ExternalApiResponse apiResponse = objectMapper.readValue(rawResponse.getBody(), ExternalApiResponse.class);

                if (apiResponse != null && apiResponse.getSuccess() && apiResponse.getData() != null) {
                    // Convert array to List before returning
                    return Arrays.asList(apiResponse.getData());
                } else {
                    logger.error("External API response indicates failure or missing data: {}", rawResponse.getBody());
                    return Collections.emptyList();
                }
            } else {
                logger.error("External API returned non-OK status or empty body: {}", rawResponse.getStatusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            logger.error("Error calling or parsing external API: ", e);
            return Collections.emptyList();
        }
    }

    public LatestMonitorStationDto getLatestMonitoringData(String projectId, String stationId) {
        String url = externalApiBaseUrl + "/manager/project/latestMonitoringDataStatistics/" + projectId;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("stationId", stationId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + externalApiToken);
        headers.set("Language", "zh-CN");
        headers.set("Industry-Code", "DZ");
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (rawResponse.getStatusCode() == HttpStatus.OK && rawResponse.getBody() != null) {
                logger.info("Raw external latest monitoring API response: {}", rawResponse.getBody());

                LatestMonitorApiResponse apiResponse = objectMapper.readValue(rawResponse.getBody(), LatestMonitorApiResponse.class);

                if (apiResponse != null && apiResponse.getSuccess() && apiResponse.getData() != null && apiResponse.getData().length > 0) {
                    return apiResponse.getData()[0];
                } else {
                    logger.error("External latest monitoring API response indicates failure or missing data: {}", rawResponse.getBody());
                    return null;
                }
            } else {
                logger.error("External latest monitoring API returned non-OK status or empty body: {}", rawResponse.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error calling or parsing external latest monitoring API: ", e);
            return null;
        }
    }
} 