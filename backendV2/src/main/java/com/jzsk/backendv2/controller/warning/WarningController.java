package com.jzsk.backendv2.controller.warning;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningVO;
import com.jzsk.backendv2.service.warning.WarningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 预警信息控制器
 * 用途：提供预警信息管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("${jzsk.v2.api-prefix:/api}/warnings")
@Tag(name = "预警信息管理", description = "提供预警信息查询、更新、删除接口")
public class WarningController {

    private final WarningService warningService;

    /**
     * 分页查询预警信息列表
     * 权限：需要预警管理权限
     */
    @Operation(summary = "分页查询预警信息列表", description = "分页查询预警信息列表，支持按地点、状态、等级、类型、时间范围筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<WarningVO>>> page(
            @Parameter(description = "分页查询条件", required = true)
            @Valid WarningPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(warningService.page(queryDTO), "查询成功"));
    }

    /**
     * 更新预警信息（解除预警）
     * 权限：需要预警管理权限
     */
    @Operation(summary = "更新预警信息", description = "更新指定预警信息，主要用于解除预警操作（设置overTime并计算stayTime）")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<WarningVO>> update(
            @Parameter(description = "预警信息更新请求", required = true, example = "{\"id\": 1, \"status\": \"已解除\", \"overTime\": \"2024-06-01 12:00:00\"}")
            @Valid @RequestBody WarningUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(warningService.update(request), "更新成功"));
    }

    /**
     * 删除预警信息（物理删除）
     * 权限：需要预警管理权限
     */
    @Operation(summary = "删除预警信息", description = "删除指定预警信息（物理删除）")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "预警信息删除请求", required = true, example = "{\"id\": 1}")
            @Valid @RequestBody IdRequestDTO request) {
        warningService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }
}
