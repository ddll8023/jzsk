package com.jzsk.backendv2.controller.engineering;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringItemCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringItemPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringItemUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.ExcelExportData;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringItemOptionVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringItemVO;
import com.jzsk.backendv2.service.engineering.MeasuringItemService;
import com.jzsk.backendv2.utils.ExcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 测项控制器
 * 用途：提供测项管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/measuring-items")
@Tag(name = "测项管理", description = "提供测项的CRUD和列表查询功能")
@SecurityRequirement(name = "JWT")
public class MeasuringItemController {

    private final MeasuringItemService measuringItemService;

    /**
     * 分页查询测项
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询测项", description = "分页查询测项列表，支持按测项名称模糊搜索")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<MeasuringItemVO>>> page(
            @Valid MeasuringItemPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(measuringItemService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询测项详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询测项详情", description = "根据ID查询单个测项详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<MeasuringItemVO>> getById(
            @Parameter(description = "测项ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(measuringItemService.getById(id), "查询成功"));
    }

    /**
     * 创建测项
     * 权限：登录即可访问
     */
    @Operation(summary = "创建测项", description = "创建新的测项")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<MeasuringItemVO>> create(
            @Parameter(description = "测项创建请求", required = true)
            @Valid @RequestBody MeasuringItemCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(measuringItemService.create(request), "创建成功"));
    }

    /**
     * 更新测项
     * 权限：登录即可访问
     */
    @Operation(summary = "更新测项", description = "更新指定测项信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<MeasuringItemVO>> update(
            @Parameter(description = "测项更新请求", required = true)
            @Valid @RequestBody MeasuringItemUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(measuringItemService.update(request), "更新成功"));
    }

    /**
     * 删除测项
     * 权限：登录即可访问
     */
    @Operation(summary = "删除测项", description = "删除指定的测项")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "测项删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        measuringItemService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }

    /**
     * 查询测项名称选项列表
     * 权限：登录即可访问
     */
    @Operation(summary = "查询测项名称选项", description = "查询所有测项名称，用于下拉选择")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/options")
    public ResponseEntity<ApiResult<List<MeasuringItemOptionVO>>> listNames() {
        return ResponseEntity.ok(ApiResult.success(measuringItemService.listNames(), "查询成功"));
    }

    /**
     * 导出测项列表
     * 权限：登录即可访问
     */
    @Operation(summary = "导出测项列表", description = "导出所有测项信息为Excel")
    @ApiResponse(responseCode = "200", description = "导出成功")
    @GetMapping("/export")
    public void export(
            @Parameter(description = "HTTP响应", required = true)
            HttpServletResponse response) {
        ExcelExportData exportData = measuringItemService.generateExportData();
        ExcelUtils.writeToResponse(exportData, exportData.getFileName(), response);
    }
}
