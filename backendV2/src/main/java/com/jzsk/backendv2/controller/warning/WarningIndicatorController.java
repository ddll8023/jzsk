package com.jzsk.backendv2.controller.warning;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningIndicatorCreateDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningIndicatorPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningIndicatorUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningIndicatorOptionsVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningIndicatorVO;
import com.jzsk.backendv2.service.warning.WarningIndicatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 预警指标控制器
 * 用途：提供预警指标管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("${jzsk.v2.api-prefix:/api}/warning-indicators")
@Tag(name = "预警指标管理", description = "提供预警指标查询、创建、更新、删除接口")
public class WarningIndicatorController {

    private final WarningIndicatorService warningIndicatorService;

    /**
     * 分页查询预警指标列表
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询预警指标列表", description = "分页查询预警指标列表，支持按监测类型、监测点筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<WarningIndicatorVO>>> page(
            @Parameter(description = "分页查询条件", required = true)
            @Valid WarningIndicatorPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(warningIndicatorService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询预警指标页面选项
     * 权限：登录即可访问
     */
    @Operation(summary = "查询预警指标页面选项", description = "查询预警指标页面所需的测点、监测项及绑定关系选项")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/options")
    public ResponseEntity<ApiResult<WarningIndicatorOptionsVO>> options() {
        return ResponseEntity.ok(ApiResult.success(warningIndicatorService.getOptions(), "查询成功"));
    }

    /**
     * 获取监测类型列表
     * 权限：登录即可访问
     */
    @Operation(summary = "获取监测类型列表", description = "获取所有预警指标的监测类型")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/types")
    public ResponseEntity<ApiResult<List<String>>> listTypes() {
        return ResponseEntity.ok(ApiResult.success(warningIndicatorService.listAllTypes(), "查询成功"));
    }

    /**
     * 查询预警指标详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询预警指标详情", description = "根据ID查询单个预警指标详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<WarningIndicatorVO>> getById(
            @Parameter(description = "预警指标ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(warningIndicatorService.getById(id), "查询成功"));
    }

    /**
     * 创建预警指标
     * 权限：登录即可访问
     */
    @Operation(summary = "创建预警指标", description = "创建新的预警指标")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<WarningIndicatorVO>> create(
            @Parameter(description = "预警指标创建请求", required = true)
            @Valid @RequestBody WarningIndicatorCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(warningIndicatorService.create(request), "创建成功"));
    }

    /**
     * 更新预警指标
     * 权限：登录即可访问
     */
    @Operation(summary = "更新预警指标", description = "更新指定预警指标")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<WarningIndicatorVO>> update(
            @Parameter(description = "预警指标更新请求", required = true)
            @Valid @RequestBody WarningIndicatorUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(warningIndicatorService.update(request), "更新成功"));
    }

    /**
     * 删除预警指标
     * 权限：登录即可访问
     */
    @Operation(summary = "删除预警指标", description = "删除指定预警指标（物理删除）")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "预警指标删除请求", required = true, example = "{\"id\": 1}")
            @Valid @RequestBody IdRequestDTO request) {
        warningIndicatorService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }
}
