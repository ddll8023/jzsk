package com.jzsk.backendv2.controller.system;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.system.organization.OrganizationCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.organization.OrganizationPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.organization.OrganizationUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.organization.OrganizationVO;
import com.jzsk.backendv2.service.system.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 组织机构控制器
 * 用途：提供组织机构管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/organizations")
@Tag(name = "组织机构管理", description = "提供组织机构增删改查接口")
@SecurityRequirement(name = "JWT")
public class OrganizationController {

    private final OrganizationService organizationService;

    /**
     * 分页查询组织机构列表
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询组织机构列表", description = "分页查询组织机构列表，支持按名称、行政区划等条件筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<OrganizationVO>>> page(
            @Parameter(description = "查询条件", required = false)
            @Valid OrganizationPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(organizationService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询组织机构详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询组织机构详情", description = "根据ID查询单个组织机构详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<OrganizationVO>> getById(
            @Parameter(description = "组织机构ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(organizationService.getById(id), "查询成功"));
    }

    /**
     * 创建组织机构
     * 权限：登录即可访问
     */
    @Operation(summary = "创建组织机构", description = "创建新组织机构")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<OrganizationVO>> create(
            @Parameter(description = "组织机构创建请求", required = true)
            @Valid @RequestBody OrganizationCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(organizationService.create(request), "创建成功"));
    }

    /**
     * 更新组织机构
     * 权限：登录即可访问
     */
    @Operation(summary = "更新组织机构", description = "更新指定组织机构信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<OrganizationVO>> update(
            @Parameter(description = "组织机构更新请求", required = true)
            @Valid @RequestBody OrganizationUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(organizationService.update(request), "更新成功"));
    }

    /**
     * 删除组织机构
     * 权限：登录即可访问
     */
    @Operation(summary = "删除组织机构", description = "删除指定组织机构")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "组织机构删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        organizationService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }
}
