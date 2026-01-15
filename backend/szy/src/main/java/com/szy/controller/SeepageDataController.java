package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.SeepageData;
import com.szy.service.SeepageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 渗流监测数据 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@RestController
@RequestMapping("/seepage-data")
public class SeepageDataController {

    @Autowired
    private SeepageDataService seepageDataService;

    @GetMapping
    public Result<List<SeepageData>> getAll() {
        return Result.ok(seepageDataService.list());
    }

    @GetMapping("/page")
    public Result<Page<SeepageData>> getPage(@RequestParam(defaultValue = "1") int current,
                                           @RequestParam(defaultValue = "10") int size) {
        return Result.ok(seepageDataService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<SeepageData> getById(@PathVariable Integer id) {
        return Result.ok(seepageDataService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody SeepageData entity) {
        return seepageDataService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody SeepageData entity) {
        entity.setRecordId(id);
        return seepageDataService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return seepageDataService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return seepageDataService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
