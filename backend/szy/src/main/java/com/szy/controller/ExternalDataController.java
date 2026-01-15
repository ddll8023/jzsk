package com.szy.controller;

import com.szy.common.lang.Result;
import com.szy.external.dto.ExternalStationDto;
import com.szy.external.dto.latestmonitor.LatestMonitorStationDto;
import com.szy.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/external-data")
public class ExternalDataController extends BaseController {

    @Autowired
    private ExternalApiService externalApiService;

    @GetMapping("/stations")
    public Result<List<ExternalStationDto>> getExternalStations(@RequestParam String projectId) {
        List<ExternalStationDto> stations = externalApiService.findAllStations(projectId);
        if (stations != null && !stations.isEmpty()) {
            return Result.ok(stations);
        } else {
            return Result.fail("Failed to retrieve external stations or no data found.");
        }
    }

    @GetMapping("/latestMonitor")
    public Result<LatestMonitorStationDto> getLatestMonitorData(@RequestParam String projectId, @RequestParam String stationId) {
        LatestMonitorStationDto stationData = externalApiService.getLatestMonitoringData(projectId, stationId);
        if (stationData != null) {
            return Result.ok(stationData);
        } else {
            return Result.fail("Failed to retrieve latest monitoring data or no data found for stationId: " + stationId);
        }
    }
} 