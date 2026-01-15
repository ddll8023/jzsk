package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.HorizontalDisplacement;
import com.szy.service.HorizontalDisplacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 水平位移监测 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@RestController
@RequestMapping("/horizontal-displacement")
public class HorizontalDisplacementController {

    @Autowired
    private HorizontalDisplacementService horizontalDisplacementService;

    @GetMapping
    public Result<List<HorizontalDisplacement>> getAll() {
        return Result.ok(horizontalDisplacementService.list());
    }

    @GetMapping("/page")
    public Result<Page<HorizontalDisplacement>> getPage(@RequestParam(defaultValue = "1") int current,
                                                      @RequestParam(defaultValue = "10") int size) {
        return Result.ok(horizontalDisplacementService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<HorizontalDisplacement> getById(@PathVariable Integer id) {
        return Result.ok(horizontalDisplacementService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody HorizontalDisplacement entity) {
        return horizontalDisplacementService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody HorizontalDisplacement entity) {
        entity.setRecordId(id);
        return horizontalDisplacementService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return horizontalDisplacementService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return horizontalDisplacementService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
