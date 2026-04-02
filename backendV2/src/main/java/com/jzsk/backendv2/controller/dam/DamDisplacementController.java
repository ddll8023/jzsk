package com.jzsk.backendv2.controller.dam;

import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.dam.DisplacementHistoryVO;
import com.jzsk.backendv2.service.external.DisplacementHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 大坝位移控制器
 * 职责：提供大坝GNSS位移历史数据的查询接口
 * 数据来源：外部GNSS位移监测API
 * V2接口规范：只使用GET和POST
 */
@Slf4j
@RestController
@RequestMapping("/api/displacement-history")
@Tag(name = "大坝位移管理", description = "提供大坝GNSS位移历史数据的查询功能")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "JWT")
public class DamDisplacementController {

    private final DisplacementHistoryService displacementHistoryService;

    /**
     * 分页查询位移历史数据
     * 权限：需要登录认证
     *
     * @param page 页码，默认1
     * @param size 每页大小，默认10
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param sensor 传感器类型
     * @param stationIds 站点ID列表（逗号分隔）
     * @param projectId 项目ID
     * @param statsFreq 统计频率
     * @return 分页结果
     */
    @Operation(summary = "分页查询位移历史数据", description = "查询外部GNSS位移历史监测数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<DisplacementHistoryVO>>> getDisplacementHistoryPage(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "开始时间", example = "2026-03-01 00:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间", example = "2026-03-26 23:59:59")
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @Parameter(description = "传感器类型", example = "L1_GP")
            @RequestParam(required = false) String sensor,
            @Parameter(description = "站点ID列表(逗号分隔)", example = "33210,33214")
            @RequestParam(required = false) String stationIds,
            @Parameter(description = "项目ID", example = "1681")
            @RequestParam(required = false) Integer projectId,
            @Parameter(description = "统计频率", example = "0")
            @RequestParam(required = false) Integer statsFreq) {

        PageResultVO<DisplacementHistoryVO> result = displacementHistoryService.getDisplacementHistoryPageRaw(
                page, size, startTime, endTime, sensor, stationIds, projectId, statsFreq);
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }

    /**
     * 获取所有测站最新位移数据（一张图专用）
     * 权限：需要登录认证
     *
     * @param sensor 传感器类型
     * @param stationIds 站点ID列表（逗号分隔）
     * @param projectId 项目ID
     * @return 各测站最新数据列表
     */
    @Operation(summary = "获取所有测站最新位移数据", description = "一张图模块专用，获取所有GNSS测站的最新数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/latest")
    public ResponseEntity<ApiResult<List<DisplacementHistoryVO>>> getDisplacementLatest(
            @Parameter(description = "传感器类型", example = "L1_GP")
            @RequestParam(required = false) String sensor,
            @Parameter(description = "站点ID列表(逗号分隔)", example = "33210,33214")
            @RequestParam(required = false) String stationIds,
            @Parameter(description = "项目ID", example = "1681")
            @RequestParam(required = false) Integer projectId) {

        List<DisplacementHistoryVO> result = displacementHistoryService.getDisplacementLatest(
                stationIds, sensor, projectId);
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }
}
