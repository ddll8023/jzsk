package com.jzsk.backendv2.controller.engineering;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsSolveDTO;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.InspectionRecordsExcelVO;
import com.jzsk.backendv2.pojo.vo.engineering.InspectionRecordsVO;
import com.jzsk.backendv2.service.engineering.InspectionRecordsService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 巡检记录控制器
 * 用途：提供巡检记录管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/inspection-records")
@Tag(name = "巡检记录管理", description = "提供巡检记录的CRUD和处理功能")
@SecurityRequirement(name = "JWT")
public class InspectionRecordsController {

    private final InspectionRecordsService inspectionRecordsService;

    /**
     * 分页查询巡检记录
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询巡检记录", description = "分页查询巡检记录列表，支持按站点、异常情况、负责人、处理状态、日期范围筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<InspectionRecordsVO>>> page(
            @Valid InspectionRecordsPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(inspectionRecordsService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询巡检记录详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询巡检记录详情", description = "根据ID查询单个巡检记录详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<InspectionRecordsVO>> getById(
            @Parameter(description = "巡检记录ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(inspectionRecordsService.getById(id), "查询成功"));
    }

    /**
     * 创建巡检记录
     * 权限：登录即可访问
     */
    @Operation(summary = "创建巡检记录", description = "创建新的巡检记录，若异常情况为无异常则自动标记为已处理")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<InspectionRecordsVO>> create(
            @Parameter(description = "巡检记录创建请求", required = true)
            @Valid @RequestBody InspectionRecordsCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(inspectionRecordsService.create(request), "创建成功"));
    }

    /**
     * 更新巡检记录
     * 权限：登录即可访问
     */
    @Operation(summary = "更新巡检记录", description = "更新指定巡检记录信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<InspectionRecordsVO>> update(
            @Parameter(description = "巡检记录更新请求", required = true)
            @Valid @RequestBody InspectionRecordsUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(inspectionRecordsService.update(request), "更新成功"));
    }

    /**
     * 删除巡检记录
     * 权限：登录即可访问
     */
    @Operation(summary = "删除巡检记录", description = "删除指定的巡检记录")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "巡检记录删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        inspectionRecordsService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }

    /**
     * 处理巡检记录（标记为已处理）
     * 权限：登录即可访问
     */
    @Operation(summary = "处理巡检记录", description = "将巡检记录标记为已处理状态")
    @ApiResponse(responseCode = "200", description = "处理成功")
    @PostMapping("/solve")
    public ResponseEntity<ApiResult<InspectionRecordsVO>> solve(
            @Parameter(description = "巡检记录处理请求", required = true)
            @Valid @RequestBody InspectionRecordsSolveDTO request) {
        return ResponseEntity.ok(ApiResult.success(inspectionRecordsService.solve(request), "处理成功"));
    }

    /**
     * 导出巡检记录
     * 权限：登录即可访问
     * 说明：返回巡检记录数据列表，前端自行生成Excel文件
     */
    @Operation(summary = "导出巡检记录", description = "导出全部巡检记录数据，前端自行生成Excel")
    @ApiResponse(responseCode = "200", description = "导出成功")
    @GetMapping("/export")
    public ResponseEntity<ApiResult<List<InspectionRecordsExcelVO>>> export() {
        List<InspectionRecordsExcelVO> data = inspectionRecordsService.listForExport();
        return ResponseEntity.ok(ApiResult.success(data, "导出成功"));
    }

    /**
     * 上传巡检图片
     * 权限：登录即可访问
     */
    @Operation(summary = "上传巡检图片", description = "上传巡检记录图片到阿里云OSS，返回文件访问URL")
    @ApiResponse(responseCode = "200", description = "上传成功")
    @PostMapping("/upload")
    public ResponseEntity<ApiResult<String>> upload(
            @Parameter(description = "图片文件", required = true)
            @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(ApiResult.success(inspectionRecordsService.uploadImage(image), "上传成功"));
    }
}
