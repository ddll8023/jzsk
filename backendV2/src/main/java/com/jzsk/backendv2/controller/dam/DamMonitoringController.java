package com.jzsk.backendv2.controller.dam;

import com.jzsk.backendv2.pojo.dto.dam.DamTimeQueryDTO;
import com.jzsk.backendv2.pojo.dto.dam.SeepageQueryDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.dam.*;
import com.jzsk.backendv2.service.dam.DamMonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

/**
 * 大坝监测控制器
 * 用途: 提供大坝监测数据的查询接口
 * 遵循KISS原则: 简单清晰的接口设计
 */
@Slf4j
@RestController
@RequestMapping("/api/dam-monitoring")
@Tag(name = "大坝监测管理", description = "提供大坝监测数据的查询功能")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "JWT")
public class DamMonitoringController {

    private final DamMonitoringService damMonitoringService;

    /**
     * 获取所有监测点列表
     * 权限: 需要登录
     */
    @Operation(summary = "获取监测点列表", description = "获取所有大坝监测点列表")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/points")
    public ResponseEntity<ApiResult<List<DamPointVO>>> getPoints() {
        List<DamPointVO> list = damMonitoringService.getPoints();
        return ResponseEntity.ok(ApiResult.success(list, "查询成功"));
    }

    /**
     * 分页查询渗压数据
     * 权限: 需要登录
     */
    @Operation(summary = "分页查询渗压数据", description = "按条件分页查询渗压监测数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/seepage/page")
    public ResponseEntity<ApiResult<PageResultVO<SeepageVO>>> getSeepagePage(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "测点编号", example = "P01")
            @ModelAttribute @Valid SeepageQueryDTO queryDTO) {

        PageResultVO<SeepageVO> result = damMonitoringService.getSeepagePage(page, size, queryDTO);
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }

    /**
     * 查询水位高程时序数据
     * 权限: 需要登录
     */
    @Operation(summary = "查询水位高程时序数据", description = "查询指定测点的水位高程时序数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/time-water-elevation")
    public ResponseEntity<ApiResult<List<TimeSeriesVO>>> getTimeWaterElevation(
            @Parameter(description = "测点编号", example = "P01")
            @ModelAttribute @Valid DamTimeQueryDTO queryDTO) {

        List<TimeSeriesVO> list = damMonitoringService.getTimeWaterElevation(queryDTO);
        return ResponseEntity.ok(ApiResult.success(list, "查询成功"));
    }

    /**
     * 查询水位时序数据
     * 权限: 需要登录
     */
    @Operation(summary = "查询水位时序数据", description = "查询指定测点的水位时序数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/time-water-level")
    public ResponseEntity<ApiResult<List<TimeSeriesVO>>> getTimeWaterLevel(
            @Parameter(description = "测点编号", example = "P01")
            @ModelAttribute @Valid DamTimeQueryDTO queryDTO) {

        List<TimeSeriesVO> list = damMonitoringService.getTimeWaterLevel(queryDTO);
        return ResponseEntity.ok(ApiResult.success(list, "查询成功"));
    }

    /**
     * 查询温度时序数据
     * 权限: 需要登录
     */
    @Operation(summary = "查询温度时序数据", description = "查询指定测点的温度时序数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/time-temperature")
    public ResponseEntity<ApiResult<List<TimeSeriesVO>>> getTimeTemperature(
            @Parameter(description = "测点编号", example = "P01")
            @ModelAttribute @Valid DamTimeQueryDTO queryDTO) {

        List<TimeSeriesVO> list = damMonitoringService.getTimeTemperature(queryDTO);
        return ResponseEntity.ok(ApiResult.success(list, "查询成功"));
    }

    /**
     * 查询水压时序数据
     * 权限: 需要登录
     */
    @Operation(summary = "查询水压时序数据", description = "查询指定测点的水压时序数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/time-water-pressure")
    public ResponseEntity<ApiResult<List<TimeSeriesVO>>> getTimeWaterPressure(
            @Parameter(description = "测点编号", example = "P01")
            @ModelAttribute @Valid DamTimeQueryDTO queryDTO) {

        List<TimeSeriesVO> list = damMonitoringService.getTimeWaterPressure(queryDTO);
        return ResponseEntity.ok(ApiResult.success(list, "查询成功"));
    }

    /**
     * 获取各测点最新水位高程
     * 权限: 需要登录
     */
    @Operation(summary = "获取各测点最新水位高程", description = "获取所有测点的最新水位高程数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/latest-water-elevation")
    public ResponseEntity<ApiResult<List<LatestWaterElevationVO>>> getLatestWaterElevation() {
        List<LatestWaterElevationVO> list = damMonitoringService.getLatestWaterElevation();
        return ResponseEntity.ok(ApiResult.success(list, "查询成功"));
    }

    /**
     * 分页查询河道水位数据
     * 权限: 需要登录
     */
    @Operation(summary = "分页查询河道水位数据", description = "按条件分页查询河道水位监测数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/river-water-level/page")
    public ResponseEntity<ApiResult<PageResultVO<RiverWaterLevelVO>>> getRiverWaterLevelPage(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "测站编码", example = "50102300")
            @ModelAttribute @Valid SeepageQueryDTO queryDTO) {

        PageResultVO<RiverWaterLevelVO> result = damMonitoringService.getRiverWaterLevelPage(page, size, queryDTO);
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }

    /**
     * 分页查询渗流量数据
     * 权限: 需要登录
     */
    @Operation(summary = "分页查询渗流量数据", description = "按条件分页查询渗流量监测数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/seepage-flow/page")
    public ResponseEntity<ApiResult<PageResultVO<SeepageFlowVO>>> getSeepageFlowPage(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "测点编号", example = "P01")
            @ModelAttribute @Valid SeepageQueryDTO queryDTO) {

        PageResultVO<SeepageFlowVO> result = damMonitoringService.getSeepageFlowPage(page, size, queryDTO);
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }

    /**
     * 获取所有渗压测站最新数据（批量接口）
     * 用于一张图模块一次性获取所有测站数据，减少请求数量
     * 权限: 需要登录
     */
    @Operation(summary = "获取所有渗压测站最新数据", description = "一次性获取所有渗压测站的最新数据，用于一张图模块")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/seepage/latest-all")
    public ResponseEntity<ApiResult<List<SeepageVO>>> getSeepageLatestAll() {
        List<SeepageVO> list = damMonitoringService.getSeepageLatestAll();
        return ResponseEntity.ok(ApiResult.success(list, "查询成功"));
    }
}
