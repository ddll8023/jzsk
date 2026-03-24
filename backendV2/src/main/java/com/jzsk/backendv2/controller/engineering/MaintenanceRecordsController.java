package com.jzsk.backendv2.controller.engineering;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MaintenanceRecordsCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MaintenanceRecordsPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MaintenanceRecordsUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.MaintenanceRecordsExcelVO;
import com.jzsk.backendv2.pojo.vo.engineering.MaintenanceRecordsVO;
import com.jzsk.backendv2.service.engineering.MaintenanceRecordsService;
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
 * 养护记录控制器
 * 用途：提供养护记录管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/maintenance-records")
@Tag(name = "养护记录管理", description = "提供养护记录的CRUD和导出功能")
@SecurityRequirement(name = "JWT")
public class MaintenanceRecordsController {

    private final MaintenanceRecordsService maintenanceRecordsService;

    /**
     * 分页查询养护记录
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询养护记录", description = "分页查询养护记录列表，支持按工程名称、时间范围筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<MaintenanceRecordsVO>>> page(
            @Valid MaintenanceRecordsPageQueryDTO queryDTO) {
        log.info("分页查询养护记录，请求参数：{}", queryDTO);
        return ResponseEntity.ok(ApiResult.success(maintenanceRecordsService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询养护记录详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询养护记录详情", description = "根据ID查询单个养护记录详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<MaintenanceRecordsVO>> getById(
            @Parameter(description = "养护记录ID", required = true, example = "1")
            @PathVariable Long id) {
        log.info("查询养护记录详情，ID：{}", id);
        return ResponseEntity.ok(ApiResult.success(maintenanceRecordsService.getById(id), "查询成功"));
    }

    /**
     * 创建养护记录
     * 权限：登录即可访问
     */
    @Operation(summary = "创建养护记录", description = "创建新的养护记录")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<MaintenanceRecordsVO>> create(
            @Parameter(description = "养护记录创建请求", required = true)
            @Valid @RequestBody MaintenanceRecordsCreateDTO request) {
        log.info("创建养护记录，请求参数：{}", request);
        return ResponseEntity.ok(ApiResult.success(maintenanceRecordsService.create(request), "创建成功"));
    }

    /**
     * 更新养护记录
     * 权限：登录即可访问
     */
    @Operation(summary = "更新养护记录", description = "更新指定养护记录信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<MaintenanceRecordsVO>> update(
            @Parameter(description = "养护记录更新请求", required = true)
            @Valid @RequestBody MaintenanceRecordsUpdateDTO request) {
        log.info("更新养护记录，请求参数：{}", request);
        return ResponseEntity.ok(ApiResult.success(maintenanceRecordsService.update(request), "更新成功"));
    }

    /**
     * 删除养护记录
     * 权限：登录即可访问
     */
    @Operation(summary = "删除养护记录", description = "删除指定的养护记录")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "养护记录删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        log.info("删除养护记录，ID：{}", request.getId());
        maintenanceRecordsService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }

    /**
     * 导出养护记录
     * 权限：登录即可访问
     * 说明：返回养护记录数据列表，前端自行生成Excel文件
     */
    @Operation(summary = "导出养护记录", description = "导出全部养护记录数据，前端自行生成Excel")
    @ApiResponse(responseCode = "200", description = "导出成功")
    @GetMapping("/export")
    public ResponseEntity<ApiResult<List<MaintenanceRecordsExcelVO>>> export() {
        log.info("导出养护记录");
        List<MaintenanceRecordsExcelVO> data = maintenanceRecordsService.listForExport();
        return ResponseEntity.ok(ApiResult.success(data, "导出成功"));
    }
}
