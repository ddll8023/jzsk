package com.szy.controller;

import com.github.pagehelper.PageInfo;
import com.szy.common.lang.Result;
import com.szy.pojo.dto.PersonDTO;
import com.szy.pojo.dto.PersonQueryDTO;
import com.szy.pojo.vo.PersonVO;
import com.szy.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 人员管理控制器
 */
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
@Tag(name = "人员管理")
public class PersonController {

    private final PersonService personService;

    @GetMapping("/list")
    @Operation(summary = "获取人员列表")
    public Result<Map<String, Object>> list(PersonQueryDTO queryDTO) {
        PageInfo<PersonVO> pageInfo = personService.list(queryDTO);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("current", pageInfo.getPageNum());
        result.put("size", pageInfo.getPageSize());
        result.put("pages", pageInfo.getPages());
        return Result.success(result);
    }

    @GetMapping("/info/{id}")
    @Operation(summary = "获取人员详情")
    public Result<PersonVO> getById(@PathVariable Long id) {
        return Result.success(personService.getById(id));
    }

    @PostMapping("/save")
    @Operation(summary = "新增人员")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<PersonVO> save(@Validated PersonDTO dto) {
        return Result.success(personService.save(dto));
    }

    @PostMapping("/update")
    @Operation(summary = "更新人员")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<PersonVO> update(@Validated PersonDTO dto) {
        return Result.success(personService.update(dto));
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除人员")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<String> delete(@PathVariable Long id) {
        personService.deleteById(id);
        return Result.success("删除成功");
    }
}
