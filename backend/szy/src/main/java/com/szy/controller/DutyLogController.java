package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.DutyLog;
import com.szy.service.DutyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用于记录值班日志的表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-25
 */
@RestController
@RequestMapping("/duty-log")
public class DutyLogController {

    @Autowired
    private DutyLogService dutyLogService;

    @GetMapping
    public Result<List<DutyLog>> getAll() {
        return Result.ok(dutyLogService.list());
    }

    @GetMapping("/page")
    public Result<Page<DutyLog>> getPage(@RequestParam(defaultValue = "1") int current,
                                       @RequestParam(defaultValue = "10") int size) {
        return Result.ok(dutyLogService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<DutyLog> getById(@PathVariable Integer id) {
        return Result.ok(dutyLogService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody DutyLog entity) {
        return dutyLogService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody DutyLog entity) {
        entity.set值班日志id(id);
        return dutyLogService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return dutyLogService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return dutyLogService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
