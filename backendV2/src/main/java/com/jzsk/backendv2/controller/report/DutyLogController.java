package com.jzsk.backendv2.controller.report;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.IdsRequestDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyLogCreateDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyLogPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyLogUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.report.DutyLogVO;
import com.jzsk.backendv2.service.report.DutyLogService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 值班日志控制器
 * 职责：提供值班日志管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("${jzsk.v2.api-prefix:/api}/duty-logs")
@Tag(name = "值班日志管理", description = "提供值班日志查询和管理功能")
public class DutyLogController {

    private final DutyLogService dutyLogService;

    /**
     * 分页查询值班日志
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询值班日志", description = "分页查询值班日志列表，支持按日期范围筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<DutyLogVO>>> page(
            @Parameter(description = "分页查询参数", required = true)
            @Valid DutyLogPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(dutyLogService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询值班日志详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询值班日志详情", description = "根据ID查询单个值班日志详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<DutyLogVO>> getById(
            @Parameter(description = "值班日志ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(dutyLogService.getById(id), "查询成功"));
    }

    /**
     * 创建值班日志
     * 权限：登录即可访问
     */
    @Operation(summary = "创建值班日志", description = "创建新的值班日志")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<DutyLogVO>> create(
            @Parameter(description = "值班日志创建请求", required = true)
            @Valid @RequestBody DutyLogCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(dutyLogService.create(request), "创建成功"));
    }

    /**
     * 更新值班日志
     * 权限：登录即可访问
     */
    @Operation(summary = "更新值班日志", description = "更新指定值班日志")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<DutyLogVO>> update(
            @Parameter(description = "值班日志更新请求", required = true)
            @Valid @RequestBody DutyLogUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(dutyLogService.update(request), "更新成功"));
    }

    /**
     * 删除值班日志
     * 权限：登录即可访问
     */
    @Operation(summary = "删除值班日志", description = "删除指定值班日志")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "值班日志删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        dutyLogService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }

    /**
     * 批量删除值班日志
     * 权限：登录即可访问
     */
    @Operation(summary = "批量删除值班日志", description = "批量删除值班日志")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/batch-delete")
    public ResponseEntity<ApiResult<Void>> batchDelete(
            @Parameter(description = "批量删除请求", required = true)
            @Valid @RequestBody IdsRequestDTO request) {
        dutyLogService.deleteByIds(request.getIds());
        return ResponseEntity.ok(ApiResult.successMessage("批量删除成功"));
    }
}
