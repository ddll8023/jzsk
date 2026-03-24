package com.jzsk.backendv2.controller.external;

import com.jzsk.backendv2.pojo.dto.external.latestmonitor.LatestMonitorStationDto;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.service.external.ExternalApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 外部数据控制器
 * 用途：提供外部监测平台数据的本地代理接口，供内部定时任务无认证调用
 * 遵循KISS原则：只做外部API代理转发，不含业务逻辑
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/external-data")
@Tag(name = "外部数据代理", description = "代理外部GNSS监测平台接口，供内部定时任务使用")
public class ExternalDataController {

    private final ExternalApiService externalApiService;

    /**
     * 获取指定站点的最新GNSS位移监测数据
     * 此接口为外部平台代理，供定时任务内部调用，不经过JWT认证
     *
     * @param projectId 项目ID
     * @param stationId 站点ID
     * @return 最新监测数据
     */
    @Operation(summary = "获取站点最新GNSS监测数据", description = "代理外部GNSS位移监测平台，返回指定站点的最新位移数据")
    @ApiResponse(responseCode = "200", description = "获取成功")
    @GetMapping("/latestMonitor")
    public ResponseEntity<ApiResult<LatestMonitorStationDto>> getLatestMonitorData(
            @Parameter(description = "项目ID", required = true, example = "1681")
            @RequestParam String projectId,
            @Parameter(description = "站点ID", required = true, example = "33210")
            @RequestParam String stationId) {
        LatestMonitorStationDto data = externalApiService.getLatestMonitoringData(projectId, stationId);
        if (data == null) {
            return ResponseEntity.ok(ApiResult.error(500, "获取监测数据失败或无数据"));
        }
        return ResponseEntity.ok(ApiResult.success(data));
    }
}
