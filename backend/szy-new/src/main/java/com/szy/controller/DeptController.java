package com.szy.controller;

import com.github.pagehelper.PageInfo;
import com.szy.common.lang.Result;
import com.szy.pojo.dto.DeptDTO;
import com.szy.pojo.dto.DeptQueryDTO;
import com.szy.pojo.vo.DeptVO;
import com.szy.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 部门管理控制器
 */
@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@Tag(name = "部门管理")
public class DeptController {

    private final DeptService deptService;

    @GetMapping("/list")
    @Operation(summary = "获取部门列表")
    public Result<Map<String, Object>> list(DeptQueryDTO queryDTO) {
        PageInfo<DeptVO> pageInfo = deptService.list(queryDTO);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("current", pageInfo.getPageNum());
        result.put("size", pageInfo.getPageSize());
        result.put("pages", pageInfo.getPages());
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取部门详情")
    public Result<DeptVO> getById(@PathVariable Long id) {
        return Result.success(deptService.getById(id));
    }

    @PostMapping("/save")
    @Operation(summary = "新增部门")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<DeptVO> save(@Validated DeptDTO dto) {
        return Result.success(deptService.save(dto));
    }

    @PutMapping("/update")
    @Operation(summary = "更新部门")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<DeptVO> update(@Validated DeptDTO dto) {
        return Result.success(deptService.update(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<String> delete(@PathVariable Long id) {
        deptService.deleteById(id);
        return Result.success("删除成功");
    }
}
