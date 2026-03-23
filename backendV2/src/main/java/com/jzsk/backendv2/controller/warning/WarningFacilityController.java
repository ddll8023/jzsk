package com.jzsk.backendv2.controller.warning;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningFacilityCreateDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningFacilityPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningFacilityUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningFacilityVO;
import com.jzsk.backendv2.service.warning.WarningFacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 预警设施控制器
 * 用途：提供预警设施管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/warning-facilities")
@Tag(name = "预警设施管理", description = "提供预警设施查询、创建、更新、删除接口")
public class WarningFacilityController {

    private final WarningFacilityService warningFacilityService;

    /**
     * 分页查询预警设施列表
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询预警设施列表", description = "分页查询预警设施列表，支持按设施名称、类型、状态筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<WarningFacilityVO>>> page(
            @Parameter(description = "分页查询条件", required = true)
            @Valid WarningFacilityPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(warningFacilityService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询预警设施详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询预警设施详情", description = "根据ID查询单个预警设施详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<WarningFacilityVO>> getById(
            @Parameter(description = "预警设施ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(warningFacilityService.getById(id), "查询成功"));
    }

    /**
     * 创建预警设施
     * 权限：登录即可访问
     */
    @Operation(summary = "创建预警设施", description = "创建新的预警设施")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<WarningFacilityVO>> create(
            @Parameter(description = "预警设施创建请求", required = true)
            @Valid @RequestBody WarningFacilityCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(warningFacilityService.create(request), "创建成功"));
    }

    /**
     * 更新预警设施
     * 权限：登录即可访问
     */
    @Operation(summary = "更新预警设施", description = "更新指定预警设施")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<WarningFacilityVO>> update(
            @Parameter(description = "预警设施更新请求", required = true)
            @Valid @RequestBody WarningFacilityUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(warningFacilityService.update(request), "更新成功"));
    }

    /**
     * 删除预警设施
     * 权限：登录即可访问
     */
    @Operation(summary = "删除预警设施", description = "删除指定预警设施（物理删除）")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "预警设施删除请求", required = true, example = "{\"id\": 1}")
            @Valid @RequestBody IdRequestDTO request) {
        warningFacilityService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }
}
