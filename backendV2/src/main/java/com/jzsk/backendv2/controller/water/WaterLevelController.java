package com.jzsk.backendv2.controller.water;

import com.jzsk.backendv2.pojo.dto.water.WaterLevelQueryDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.water.WaterLevelVO;
import com.jzsk.backendv2.service.water.WaterLevelService;
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

import java.util.List;

/**
 * 水位控制器
 * 用途: 提供水位数据的查询接口
 * 遵循KISS原则: 简单清晰的接口设计
 */
@Slf4j
@RestController
@RequestMapping("/api/water-levels")
@Tag(name = "水位管理", description = "提供水位数据的查询功能")
@RequiredArgsConstructor
@Validated
public class WaterLevelController {

    private final WaterLevelService waterLevelService;

    /**
     * 分页查询水位数据
     * 权限: 需要登录
     */
    @Operation(summary = "分页查询水位数据", description = "按条件分页查询水位数据")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<WaterLevelVO>>> getWaterLevelPage(
        @Parameter(description = "页码", example = "1")
        @RequestParam(defaultValue = "1") int page,
        @Parameter(description = "每页大小", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "测站编码", example = "50102300")
        WaterLevelQueryDTO queryDTO) {

        PageResultVO<WaterLevelVO> result = waterLevelService.getWaterLevelPage(page, size, queryDTO);
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }

    /**
     * 查询水位数据列表
     * 权限: 需要登录
     */
    @Operation(summary = "查询水位数据列表", description = "根据测站编码查询水位数据列表")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<WaterLevelVO>>> getWaterLevelList(
        @Parameter(description = "测站编码", example = "50102300")
        WaterLevelQueryDTO queryDTO) {

        List<WaterLevelVO> list = waterLevelService.getWaterLevelList(queryDTO);
        return ResponseEntity.ok(ApiResult.success(list, "查询成功"));
    }
}