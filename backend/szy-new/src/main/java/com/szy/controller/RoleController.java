package com.szy.controller;

import com.github.pagehelper.PageInfo;
import com.szy.common.lang.Result;
import com.szy.pojo.dto.RoleDTO;
import com.szy.pojo.dto.RoleQueryDTO;
import com.szy.pojo.entity.Role;
import com.szy.pojo.vo.RoleVO;
import com.szy.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Api(tags = "角色管理")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/list")
    @ApiOperation("获取角色列表")
    public Result<PageInfo<Role>> list(RoleQueryDTO queryDTO) {
        return Result.success(roleService.list(queryDTO));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取角色详情")
    public Result<RoleVO> info(@PathVariable Long id) {
        return Result.success(roleService.getInfo(id));
    }

    @PostMapping("/save")
    @ApiOperation("新增角色")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Void> save(@Validated @RequestBody RoleDTO dto) {
        roleService.save(dto);
        return Result.success("操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("更新角色")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Void> update(@Validated @RequestBody RoleDTO dto) {
        roleService.update(dto);
        return Result.success("操作成功");
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除角色")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success("操作成功");
    }

    @PostMapping("/menu/{roleId}")
    @ApiOperation("分配菜单权限")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Void> allocateMenu(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        roleService.allocateMenu(roleId, menuIds);
        return Result.success("操作成功");
    }

    @GetMapping("/menus/{roleId}")
    @ApiOperation("获取角色菜单ID列表")
    public Result<List<Long>> getMenus(@PathVariable Long roleId) {
        return Result.success(roleService.getRoleMenus(roleId));
    }
}