package com.jzsk.backendv2.controller.system;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserPasswordUpdateDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserRoleAssignDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.user.UserVO;
import com.jzsk.backendv2.service.system.UserService;
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

/**
 * 用户控制器
 * 用途：提供用户管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/users")
@Tag(name = "用户管理", description = "提供用户增删改查、角色分配、密码管理接口")
@SecurityRequirement(name = "JWT")
public class UserController {

    private final UserService userService;

    /**
     * 分页查询用户列表
     * 权限：需要系统管理权限
     */
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表，支持按用户名、姓名、部门等条件筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<UserVO>>> page(
            @Valid UserPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(userService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询用户详情
     * 权限：需要系统管理权限
     */
    @Operation(summary = "查询用户详情", description = "根据ID查询单个用户详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<UserVO>> getById(
            @Parameter(description = "用户ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(userService.getById(id), "查询成功"));
    }

    /**
     * 创建用户
     * 权限：需要系统管理权限
     */
    @Operation(summary = "创建用户", description = "创建新用户，默认密码为123456")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<UserVO>> create(
            @Parameter(description = "用户创建请求", required = true)
            @Valid @RequestBody UserCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(userService.create(request), "创建成功"));
    }

    /**
     * 更新用户
     * 权限：需要系统管理权限
     */
    @Operation(summary = "更新用户", description = "更新指定用户信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<UserVO>> update(
            @Parameter(description = "用户更新请求", required = true)
            @Valid @RequestBody UserUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(userService.update(request), "更新成功"));
    }

    /**
     * 删除用户
     * 权限：需要系统管理权限
     */
    @Operation(summary = "删除用户", description = "删除指定用户")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "用户删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        userService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }

    /**
     * 分配角色
     * 权限：需要系统管理权限
     */
    @Operation(summary = "分配角色", description = "给用户分配角色，同时更新用户类型")
    @ApiResponse(responseCode = "200", description = "分配成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/roles/assign")
    public ResponseEntity<ApiResult<Void>> assignRoles(
            @Parameter(description = "用户角色分配请求", required = true)
            @Valid @RequestBody UserRoleAssignDTO request) {
        userService.assignRoles(request);
        return ResponseEntity.ok(ApiResult.successMessage("分配成功"));
    }

    /**
     * 重置密码
     * 权限：需要系统管理权限
     */
    @Operation(summary = "重置密码", description = "将用户密码重置为默认密码（123456）")
    @ApiResponse(responseCode = "200", description = "重置成功")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResult<Void>> resetPassword(
            @Parameter(description = "用户ID", required = true, example = "1")
            @Valid @RequestBody IdRequestDTO request) {
        userService.resetPassword(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("密码重置成功"));
    }

    /**
     * 修改当前用户密码
     * 权限：需要登录
     */
    @Operation(summary = "修改当前用户密码", description = "修改当前登录用户的密码，需要校验旧密码")
    @ApiResponse(responseCode = "200", description = "修改成功")
    @PostMapping("/password/update")
    public ResponseEntity<ApiResult<Void>> updatePassword(
            @Parameter(description = "密码修改请求", required = true)
            @Valid @RequestBody UserPasswordUpdateDTO request) {
        userService.updatePassword(request);
        return ResponseEntity.ok(ApiResult.successMessage("密码修改成功"));
    }
}
