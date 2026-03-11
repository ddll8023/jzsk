package com.szy.controller;

import com.szy.common.lang.Result;
import com.szy.pojo.dto.DictDetailDTO;
import com.szy.pojo.vo.DictDetailVO;
import com.szy.service.DictDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 字典详情管理控制器
 */
@RestController
@RequestMapping("/dict-detail")
@RequiredArgsConstructor
@Tag(name = "字典详情管理")
public class DictDetailController {

    private final DictDetailService dictDetailService;

    @GetMapping("/info/{id}")
    @Operation(summary = "获取字典详情")
    public Result<DictDetailVO> getById(@PathVariable Long id) {
        return Result.success(dictDetailService.getById(id));
    }

    @PostMapping("/save")
    @Operation(summary = "新增字典详情")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<DictDetailVO> save(@Validated DictDetailDTO dto) {
        return Result.success(dictDetailService.save(dto));
    }

    @PostMapping("/update")
    @Operation(summary = "更新字典详情")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<DictDetailVO> update(@Validated DictDetailDTO dto) {
        return Result.success(dictDetailService.update(dto));
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除字典详情")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<String> delete(@PathVariable Long id) {
        dictDetailService.deleteById(id);
        return Result.success("删除成功");
    }
}
