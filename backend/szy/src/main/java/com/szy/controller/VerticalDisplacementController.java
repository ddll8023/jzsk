package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.VerticalDisplacement;
import com.szy.service.VerticalDisplacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 垂直位移监测 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@RestController
@RequestMapping("/vertical-displacement")
public class VerticalDisplacementController {

    @Autowired
    private VerticalDisplacementService verticalDisplacementService;

    @GetMapping
    public Result<List<VerticalDisplacement>> getAll() {
        return Result.ok(verticalDisplacementService.list());
    }

    @GetMapping("/page")
    public Result<Page<VerticalDisplacement>> getPage(@RequestParam(defaultValue = "1") int current,
                                                    @RequestParam(defaultValue = "10") int size) {
        return Result.ok(verticalDisplacementService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<VerticalDisplacement> getById(@PathVariable Integer id) {
        return Result.ok(verticalDisplacementService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody VerticalDisplacement entity) {
        return verticalDisplacementService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody VerticalDisplacement entity) {
        entity.setRecordId(id);
        return verticalDisplacementService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return verticalDisplacementService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return verticalDisplacementService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
