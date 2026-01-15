package com.szy.controller;

import com.szy.external.dto.DisplacementHistoryDto;
import com.szy.external.dto.PageResult;
import com.szy.service.DisplacementHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/external-data/displacement-history")
public class DisplacementHistoryController {

    @Autowired
    private DisplacementHistoryService service;

    @GetMapping
    public PageResult<DisplacementHistoryDto> getHistory(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam String sensor,
            @RequestParam String stationIds, // 逗号分隔
            @RequestParam int projectId,
            @RequestParam(defaultValue = "0") int statsFreq,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Long> stationIdList = Arrays.stream(stationIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return service.getHistory(startTime, endTime, sensor, stationIdList, projectId, statsFreq, page, size);
    }
} 