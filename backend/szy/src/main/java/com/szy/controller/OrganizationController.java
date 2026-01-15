package com.szy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Organization;
import com.szy.entity.Role;
import com.szy.entity.RoleAuthority;
import com.szy.entity.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 组织机构控制器
 * </p>
 *
 * @author l
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController extends BaseController {
    /**
     * 查询指定组织机构信息
     *
     * @param id 组织机构id
     * @return 成功信息，组织机构json；失败信息，错误提示
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('glxx_jgxx')")
    public Result info(@PathVariable Long id) {
        Organization organization = organizationService.getById(id);
        if (organization == null)
            return Result.fail(ResponseCode.NOT_EXIST, "查询组织机构信息不存在");
        return Result.ok(organization);
    }

    /**
     * 组织机构列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，组织机构json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('glxx_jgxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage,
                       @RequestParam("pageSize") Integer pageSize,
                       @RequestParam("name") String name
    ) {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            queryWrapper.like("organization_name", name);
        }
        Page<Organization> page = new Page<>(currentPage, pageSize);
        Page<Organization> organizationPage = organizationService.page(page, queryWrapper);
        return Result.ok(organizationPage);
    }

    /**
     * 新增组织机构
     *
     * @param organization 组织机构json
     * @return 成功信息，组织机构json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('glxx_jgxx')")
    public Result save(@RequestBody Organization organization) {
        organizationService.save(organization);
        return Result.ok(organization);
    }

    /**
     * 删除组织机构
     *
     * @param id 组织机构id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('glxx_jgxx')")
    public Result delete(@PathVariable Long id) {
        Organization byId = organizationService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除组织机构不存在");
        }
        organizationService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新组织机构信息
     *
     * @param organization 组织机构json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('glxx_jgxx')")
    public Result update(@RequestBody Organization organization) {
        Organization byId = organizationService.getById(organization.getId());
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "待更新组织机构不存在");
        organizationService.updateById(organization);
        return Result.ok(organization);
    }
}
