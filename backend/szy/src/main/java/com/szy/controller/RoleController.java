package com.szy.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.dto.MenuDto;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色控制器
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {
    /**
     * 查角色信息
     *
     * @param id 角色id
     * @return 成功信息，角色json
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable(name = "id") Long id) {
        Role role = roleService.getById(id);
        // 获取角色相关联的菜单id
        List<RoleAuthority> roleMenus = roleAuthorityService.list(new QueryWrapper<RoleAuthority>().eq("role_id", id));
        List<Long> menuIds = roleMenus.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toList());
        role.setMenusIds(menuIds);
        return Result.ok(role);
    }

    /**
     * 分页返回根据输入角色名字查询的信息
     *
     * @param name 角色名
     * @return 成功信息，分页角色信息json
     */
    @GetMapping("/list-by-name")
    public Result listByName(String name) {
        Page<Role> pageData = roleService.page(getPage(), new QueryWrapper<Role>().like(StrUtil.isNotBlank(name), "name", name)); // 数据中没有info函数里的menuIds
        return Result.ok(pageData);
    }

    /**
     * 角色列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，角色json
     */
    @GetMapping("/list")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<Role> page = new Page<>(currentPage, pageSize);
        Page<Role> rolePage = roleService.page(page);
        return Result.ok(rolePage);
    }

    /**
     * 数据维护人员角色列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，角色json
     */
    @GetMapping("/list-data")
    public Result listdata(@RequestParam("currentPage") Integer currentPage,
                           @RequestParam("pageSize") Integer pageSize) {
        Page<Role> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("name", "系统管理员"); // 排除名称为“系统管理员”的角色
        Page<Role> rolePage = roleService.page(page, queryWrapper);
        return Result.ok(rolePage);
    }

    /**
     * 新增角色(只存角色，不分配权限)
     *
     * @param role 角色json
     * @return 成功信息，角色json
     */
    @PostMapping("/save")
    public Result save(@Validated @RequestBody Role role) {
        roleService.save(role);
        return Result.ok(role);
    }

    /**
     * 更新角色信息
     *
     * @param role 角色json
     * @return 成功信息，角色json
     */
    @PostMapping("/update")
    public Result update(@Validated @RequestBody Role role) {
        Role byId = roleService.getById(role.getId());
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "修改角色不存在");
        }
        roleService.updateById(role);
        //根据角色id清空用户权限
        userService.clearUserAuthorityInfoByAuthorityID(role.getId());
        return Result.ok(role);
    }

    /**
     * 删除角色
     *
     * @param id 角色id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        Role byId = roleService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除角色不存在");
        }
        roleService.removeById(id);
        // 删除中间表
        userRoleService.remove(new QueryWrapper<UserRole>().eq("role_id", id));
        roleAuthorityService.remove(new QueryWrapper<RoleAuthority>().eq("role_id", id));
        // 删除缓存
        userService.clearUserAuthorityInfoByAuthorityID(id);

        return Result.ok("删除成功");
    }

    /**
     * 给角色分配权限
     *
     * @param roleId  角色id
     * @param menuIds 权限id数组
     * @return 成功信息
     */
    @PostMapping("/distribute/{roleId}")
    @Transactional
    public Result distribute(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {
        Role byId = roleService.getById(roleId);
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "分配角色不存在");
        List<RoleAuthority> roleAuthorities = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId -> {
                    RoleAuthority roleAuthority = new RoleAuthority();
                    roleAuthority.setAuthorityId(menuId);
                    roleAuthority.setRoleId(roleId);
                    roleAuthorities.add(roleAuthority);
                }
        );
        // 先删除原来的记录，再保存新的记录
        roleAuthorityService.remove(new QueryWrapper<RoleAuthority>().eq("role_id", roleId));
        roleAuthorityService.saveBatch(roleAuthorities);
        // 删除缓存
        userService.clearUserAuthorityInfoByRoleID(roleId);
        return Result.ok();
    }

    //根据角色id获取角色已有的权限
    @GetMapping("/authority/{roleId}")
    public Result authority(@PathVariable("roleId") Long roleId) {
        //根据角色id获取权限信息
        String userAuthorityInfo = roleAuthorityService.getUserAuthorityInfo(roleId);
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(userAuthorityInfo,",");

        //根据角色id获取导航栏信息
        List<MenuDto> navs = authorityService.getCurrentNavByRoleID(roleId);
        return Result.ok(MapUtil.builder()
                .put("authority",authorityInfoArray)
                .put("nav",navs)
                .map());
    }
}
