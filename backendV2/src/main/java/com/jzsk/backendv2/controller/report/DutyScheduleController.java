package com.jzsk.backendv2.controller.report;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.IdsRequestDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyScheduleCreateDTO;
import com.jzsk.backendv2.pojo.dto.report.DutySchedulePageQueryDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyScheduleUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.report.DutyScheduleVO;
import com.jzsk.backendv2.service.report.DutyScheduleService;
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
import java.util.List;

/**
 * 值班安排控制器
 * 职责：提供值班安排管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("${jzsk.v2.api-prefix:/api}/duty-schedules")
@Tag(name = "值班安排管理", description = "提供值班安排查询和管理功能")
public class DutyScheduleController {

    private final DutyScheduleService dutyScheduleService;

    /**
     * 分页查询值班安排
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询值班安排", description = "分页查询值班安排列表，支持按日期范围筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<DutyScheduleVO>>> page(
            @Parameter(description = "分页查询参数", required = true)
            @Valid DutySchedulePageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(dutyScheduleService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询值班安排详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询值班安排详情", description = "根据ID查询单个值班安排详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<DutyScheduleVO>> getById(
            @Parameter(description = "值班安排ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(dutyScheduleService.getById(id), "查询成功"));
    }

    /**
     * 创建值班安排
     * 权限：登录即可访问
     */
    @Operation(summary = "创建值班安排", description = "创建新的值班安排")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<DutyScheduleVO>> create(
            @Parameter(description = "值班安排创建请求", required = true)
            @Valid @RequestBody DutyScheduleCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(dutyScheduleService.create(request), "创建成功"));
    }

    /**
     * 更新值班安排
     * 权限：登录即可访问
     */
    @Operation(summary = "更新值班安排", description = "更新指定值班安排")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<DutyScheduleVO>> update(
            @Parameter(description = "值班安排更新请求", required = true)
            @Valid @RequestBody DutyScheduleUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(dutyScheduleService.update(request), "更新成功"));
    }

    /**
     * 删除值班安排
     * 权限：登录即可访问
     */
    @Operation(summary = "删除值班安排", description = "删除指定值班安排")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "值班安排删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        dutyScheduleService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }

    /**
     * 批量删除值班安排
     * 权限：登录即可访问
     */
    @Operation(summary = "批量删除值班安排", description = "批量删除值班安排")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/batch-delete")
    public ResponseEntity<ApiResult<Void>> batchDelete(
            @Parameter(description = "批量删除请求", required = true)
            @Valid @RequestBody IdsRequestDTO request) {
        dutyScheduleService.deleteByIds(request.getIds());
        return ResponseEntity.ok(ApiResult.successMessage("批量删除成功"));
    }
}
