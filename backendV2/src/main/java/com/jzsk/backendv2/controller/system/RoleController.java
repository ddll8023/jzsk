package com.jzsk.backendv2.controller.system;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.system.role.RoleCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.role.RoleMenuAssignDTO;
import com.jzsk.backendv2.pojo.dto.system.role.RolePageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.role.RoleUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.role.RoleMenuVO;
import com.jzsk.backendv2.pojo.vo.system.role.RoleVO;
import com.jzsk.backendv2.service.system.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色控制器
 * 用途：提供角色管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/roles")
@Tag(name = "角色管理", description = "提供角色增删改查、菜单权限分配接口")
@SecurityRequirement(name = "JWT")
public class RoleController {

    private final RoleService roleService;

    /**
     * 分页查询角色列表
     * 权限：需要系统管理权限
     */
    @Operation(summary = "分页查询角色列表", description = "分页查询角色列表，支持按角色名称、编码等条件筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<RoleVO>>> page(
            @Valid RolePageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(roleService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询角色详情
     * 权限：需要系统管理权限
     */
    @Operation(summary = "查询角色详情", description = "根据ID查询单个角色详情，包含菜单ID列表")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<RoleVO>> getById(
            @Parameter(description = "角色ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(roleService.getById(id), "查询成功"));
    }

    /**
     * 创建角色
     * 权限：需要系统管理权限
     */
    @Operation(summary = "创建角色", description = "创建新角色")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<RoleVO>> create(
            @Parameter(description = "角色创建请求", required = true)
            @Valid @RequestBody RoleCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(roleService.create(request), "创建成功"));
    }

    /**
     * 更新角色
     * 权限：需要系统管理权限
     */
    @Operation(summary = "更新角色", description = "更新指定角色信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<RoleVO>> update(
            @Parameter(description = "角色更新请求", required = true)
            @Valid @RequestBody RoleUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(roleService.update(request), "更新成功"));
    }

    /**
     * 删除角色
     * 权限：需要系统管理权限
     */
    @Operation(summary = "删除角色", description = "删除指定角色，同时删除角色-菜单关联和用户-角色关联")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "角色删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        roleService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }

    /**
     * 分配菜单权限
     * 权限：需要系统管理权限
     */
    @Operation(summary = "分配菜单权限", description = "给角色分配菜单权限，先删后插")
    @ApiResponse(responseCode = "200", description = "分配成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/menus/assign")
    public ResponseEntity<ApiResult<Void>> assignMenus(
            @Parameter(description = "角色菜单分配请求", required = true)
            @Valid @RequestBody RoleMenuAssignDTO request) {
        roleService.assignMenus(request);
        return ResponseEntity.ok(ApiResult.successMessage("分配成功"));
    }

    /**
     * 获取角色菜单ID列表
     * 权限：需要系统管理权限
     */
    @Operation(summary = "获取角色菜单ID列表", description = "根据角色ID查询该角色已分配的菜单ID列表")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @GetMapping("/{roleId}/menus")
    public ResponseEntity<ApiResult<RoleMenuVO>> getRoleMenus(
            @Parameter(description = "角色ID", required = true, example = "1")
            @PathVariable Long roleId) {
        return ResponseEntity.ok(ApiResult.success(roleService.getRoleMenus(roleId), "查询成功"));
    }

    /**
     * 获取启用角色选项列表
     * 权限：需要登录
     */
    @Operation(summary = "获取启用角色选项列表", description = "查询所有启用的角色，用于下拉选择")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/options")
    public ResponseEntity<ApiResult<List<OptionVO>>> listOptions() {
        return ResponseEntity.ok(ApiResult.success(roleService.listEnabledOptions(), "查询成功"));
    }
}
