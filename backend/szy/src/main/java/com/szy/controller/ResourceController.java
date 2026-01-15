package com.szy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Authority;
import com.szy.entity.RoleAuthority;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 资源控制器
 * </p>
 *
 * @author l
 * @since 2022-1-5
 */
@RestController
@RequestMapping("/resource")
public class ResourceController extends BaseController {
    /**
     * 根据资源id查资源信息
     *
     * @param id 资源id
     * @return 成功信息，指定id资源信息
     */
    // TODO: 临时移除权限校验，生产环境需恢复
    // @PreAuthorize("hasAuthority('xtgl_zygl')")
    @GetMapping("/resourceInfo/{id}")
    public Result info(@PathVariable("id") Long id) {
        return Result.ok(authorityService.getById(id));
    }

    /**
     * 查资源列表
     *
     * @return 成功信息，资源列表树json
     */
    // TODO: 临时移除权限校验，生产环境需恢复
    // @PreAuthorize("hasAuthority('xtgl_zygl')")
    @GetMapping("/menuList")
    public Result menuList() {
        List<Authority> menus = authorityService.tree();
        return Result.ok(menus);
    }

    /**
     * 新增资源
     *
     * @param authority 新功能json
     * @return 成功信息，新功能json
     */
    @PostMapping("/save")
    // TODO: 临时移除权限校验，生产环境需恢复
    // @PreAuthorize("hasAuthority('xtgl_zygl')")
    public Result save(@Validated @RequestBody Authority authority) {
        authorityService.save(authority);
        return Result.ok(authority);
    }

    /**
     * 更新资源信息
     *
     * @param authority 资源json
     * @return 成功信息，资源json
     */
    @PostMapping("/update")
    // TODO: 临时移除权限校验，生产环境需恢复
    // @PreAuthorize("hasAuthority('xtgl_zygl')")
    public Result update(@Validated @RequestBody Authority authority) {
        Authority byId = authorityService.getById(authority.getId());
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "更新权限不存在");
        }
        authorityService.updateById(authority);
        // 清除所有与该资源相关的权限缓存
        userService.clearUserAuthorityInfoByAuthorityID(authority.getId());
        return Result.ok(authority);
    }

    /**
     * 删除资源
     *
     * @param id 资源id
     * @return 删除成功信息
     */
    @PostMapping("/delete/{id}")
    // TODO: 临时移除权限校验，生产环境需恢复
    // @PreAuthorize("hasAuthority('xtgl_zygl')")
    public Result delete(@PathVariable("id") Long id) {
        Authority byId = authorityService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除权限不存在");
        }
        //清除缓存
        userService.clearUserAuthorityInfoByAuthorityID(id);
        authorityService.removeById(id);
        roleAuthorityService.remove(new QueryWrapper<RoleAuthority>().eq("authority_id", id));
        return Result.ok("删除成功");
    }
}
