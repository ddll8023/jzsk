package com.szy.controller;

import com.github.pagehelper.PageInfo;
import com.szy.common.lang.Result;
import com.szy.pojo.dto.OrganizationDTO;
import com.szy.pojo.dto.OrganizationQueryDTO;
import com.szy.pojo.vo.OrganizationVO;
import com.szy.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 组织机构控制器
 */
@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
@Tag(name = "组织机构管理", description = "组织机构管理接口")
public class OrganizationController {

    private final OrganizationService organizationService;

    /**
     * 分页查询组织机构列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取组织机构列表", description = "分页查询组织机构列表")
    public Result<Map<String, Object>> list(OrganizationQueryDTO queryDTO) {
        PageInfo<OrganizationVO> pageInfo = organizationService.list(queryDTO);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("current", pageInfo.getPageNum());
        result.put("size", pageInfo.getPageSize());
        result.put("pages", pageInfo.getPages());
        return Result.success(result);
    }

    /**
     * 获取组织机构详情
     */
    @GetMapping("/info/{id}")
    @Operation(summary = "获取组织机构详情", description = "根据ID获取组织机构详情")
    public Result<OrganizationVO> info(@PathVariable Long id) {
        return Result.success(organizationService.getInfo(id));
    }

    /**
     * 新增组织机构
     */
    @PostMapping("/save")
    @Operation(summary = "新增组织机构", description = "新增组织机构信息")
    public Result<String> save(@Validated OrganizationDTO dto) {
        organizationService.save(dto);
        return Result.success("新增成功");
    }

    /**
     * 更新组织机构
     */
    @PostMapping("/update")
    @Operation(summary = "更新组织机构", description = "更新组织机构信息")
    public Result<String> update(@Validated OrganizationDTO dto) {
        organizationService.update(dto);
        return Result.success("更新成功");
    }

    /**
     * 删除组织机构
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "删除组织机构", description = "根据ID删除组织机构")
    public Result<String> delete(@PathVariable Long id) {
        organizationService.delete(id);
        return Result.success("删除成功");
    }
}