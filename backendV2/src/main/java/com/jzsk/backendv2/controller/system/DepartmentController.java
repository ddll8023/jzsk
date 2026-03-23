package com.jzsk.backendv2.controller.system;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.system.department.DepartmentCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.department.DepartmentPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.department.DepartmentUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.department.DepartmentVO;
import com.jzsk.backendv2.service.system.DepartmentService;
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
 * 部门控制器
 * 用途：提供部门管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/departments")
@Tag(name = "部门管理", description = "提供部门增删改查接口")
@SecurityRequirement(name = "JWT")
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * 分页查询部门列表
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询部门列表", description = "分页查询部门列表，支持按部门名称、级别、公司等条件筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<DepartmentVO>>> page(
            @Parameter(description = "查询条件", required = false)
            @Valid DepartmentPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(departmentService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询部门详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询部门详情", description = "根据ID查询单个部门详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<DepartmentVO>> getById(
            @Parameter(description = "部门ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(departmentService.getById(id), "查询成功"));
    }

    /**
     * 创建部门
     * 权限：登录即可访问
     */
    @Operation(summary = "创建部门", description = "创建新部门")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<DepartmentVO>> create(
            @Parameter(description = "部门创建请求", required = true)
            @Valid @RequestBody DepartmentCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(departmentService.create(request), "创建成功"));
    }

    /**
     * 更新部门
     * 权限：登录即可访问
     */
    @Operation(summary = "更新部门", description = "更新指定部门信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<DepartmentVO>> update(
            @Parameter(description = "部门更新请求", required = true)
            @Valid @RequestBody DepartmentUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(departmentService.update(request), "更新成功"));
    }

    /**
     * 删除部门
     * 权限：登录即可访问
     */
    @Operation(summary = "删除部门", description = "删除指定部门")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "部门删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        departmentService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }
}
