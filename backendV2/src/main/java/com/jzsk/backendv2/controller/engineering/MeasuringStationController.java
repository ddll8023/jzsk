package com.jzsk.backendv2.controller.engineering;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringStationCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringStationPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringStationUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringStationOptionVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringStationVO;
import com.jzsk.backendv2.service.engineering.MeasuringStationService;
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

import javax.validation.Valid;
import java.util.List;

/**
 * 监测站点控制器
 * 用途：提供监测站点管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/measuring-stations")
@Tag(name = "监测站点管理", description = "提供监测站点的CRUD和列表查询功能")
@SecurityRequirement(name = "JWT")
public class MeasuringStationController {

    private final MeasuringStationService measuringStationService;

    /**
     * 分页查询监测站点
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询监测站点", description = "分页查询监测站点列表，支持按站点名称模糊搜索")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<MeasuringStationVO>>> page(
            @Valid MeasuringStationPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(measuringStationService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询监测站点详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询监测站点详情", description = "根据ID查询单个监测站点详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<MeasuringStationVO>> getById(
            @Parameter(description = "监测站点ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(measuringStationService.getById(id), "查询成功"));
    }

    /**
     * 创建监测站点
     * 权限：登录即可访问
     */
    @Operation(summary = "创建监测站点", description = "创建新的监测站点")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<MeasuringStationVO>> create(
            @Parameter(description = "监测站点创建请求", required = true)
            @Valid @RequestBody MeasuringStationCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(measuringStationService.create(request), "创建成功"));
    }

    /**
     * 更新监测站点
     * 权限：登录即可访问
     */
    @Operation(summary = "更新监测站点", description = "更新指定监测站点信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<MeasuringStationVO>> update(
            @Parameter(description = "监测站点更新请求", required = true)
            @Valid @RequestBody MeasuringStationUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(measuringStationService.update(request), "更新成功"));
    }

    /**
     * 删除监测站点
     * 权限：登录即可访问
     */
    @Operation(summary = "删除监测站点", description = "删除指定的监测站点")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "监测站点删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        measuringStationService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }

    /**
     * 查询监测站点名称选项列表
     * 权限：登录即可访问
     */
    @Operation(summary = "查询监测站点名称选项", description = "查询所有监测站点名称，用于下拉选择")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/options")
    public ResponseEntity<ApiResult<List<MeasuringStationOptionVO>>> listNames() {
        return ResponseEntity.ok(ApiResult.success(measuringStationService.listNames(), "查询成功"));
    }
}
