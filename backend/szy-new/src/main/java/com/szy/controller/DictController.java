package com.szy.controller;

import com.github.pagehelper.PageInfo;
import com.szy.common.lang.Result;
import com.szy.pojo.dto.DictDTO;
import com.szy.pojo.dto.DictQueryDTO;
import com.szy.pojo.vo.DictVO;
import com.szy.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典管理控制器
 */
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
@Tag(name = "字典管理")
public class DictController {

    private final DictService dictService;

    @GetMapping("/list")
    @Operation(summary = "分页查询字典列表")
    public Result<Map<String, Object>> list(DictQueryDTO queryDTO) {
        PageInfo<DictVO> pageInfo = dictService.list(queryDTO);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("current", pageInfo.getPageNum());
        result.put("size", pageInfo.getPageSize());
        result.put("pages", pageInfo.getPages());
        return Result.success(result);
    }

    @GetMapping("/info/{id}")
    @Operation(summary = "获取字典详情")
    public Result<DictVO> getById(@PathVariable Long id) {
        return Result.success(dictService.getById(id));
    }

    @PostMapping("/save")
    @Operation(summary = "新增字典")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<DictVO> save(@Validated DictDTO dto) {
        return Result.success(dictService.save(dto));
    }

    @PostMapping("/update")
    @Operation(summary = "更新字典")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<DictVO> update(@Validated DictDTO dto) {
        return Result.success(dictService.update(dto));
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除字典")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<String> delete(@PathVariable Long id) {
        dictService.deleteById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/kinds")
    @Operation(summary = "获取字典选项（树形结构）")
    public Result<List<String>> getKinds() {
        return Result.success(dictService.findAllNames());
    }

    @GetMapping("/LVs")
    @Operation(summary = "获取字典选项（扁平结构）")
    public Result<List<DictVO>> getLVs(@RequestParam("name") String name) {
        List<DictVO> list = dictService.findByDictName(name);
        if (list == null || list.isEmpty()) {
            return Result.fail(name + "数据项名称错误");
        }
        return Result.success(list);
    }
}
