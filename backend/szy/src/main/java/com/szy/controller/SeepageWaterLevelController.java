package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.SeepageWaterLevel;
import com.szy.service.SeepageWaterLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 渗流水位监测 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@RestController
@RequestMapping("/seepage-water-level")
public class SeepageWaterLevelController {

    @Autowired
    private SeepageWaterLevelService seepageWaterLevelService;

    @GetMapping
    public Result<List<SeepageWaterLevel>> getAll() {
        return Result.ok(seepageWaterLevelService.list());
    }

    @GetMapping("/page")
    public Result<Page<SeepageWaterLevel>> getPage(@RequestParam(defaultValue = "1") int current,
                                                 @RequestParam(defaultValue = "10") int size) {
        return Result.ok(seepageWaterLevelService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<SeepageWaterLevel> getById(@PathVariable Integer id) {
        return Result.ok(seepageWaterLevelService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody SeepageWaterLevel entity) {
        return seepageWaterLevelService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody SeepageWaterLevel entity) {
        entity.setRecordId(id);
        return seepageWaterLevelService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return seepageWaterLevelService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return seepageWaterLevelService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
