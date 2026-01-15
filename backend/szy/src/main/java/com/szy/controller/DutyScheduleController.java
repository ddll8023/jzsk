package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.DutySchedule;
import com.szy.service.DutyScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用于记录值班安排的表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-25
 */
@RestController
@RequestMapping("/duty-schedule")
public class DutyScheduleController {

    @Autowired
    private DutyScheduleService dutyScheduleService;

    @GetMapping
    public Result<List<DutySchedule>> getAll() {
        return Result.ok(dutyScheduleService.list());
    }

    @GetMapping("/page")
    public Result<Page<DutySchedule>> getPage(@RequestParam(defaultValue = "1") int current,
                                            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(dutyScheduleService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<DutySchedule> getById(@PathVariable Integer id) {
        return Result.ok(dutyScheduleService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody DutySchedule entity) {
        return dutyScheduleService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody DutySchedule entity) {
        entity.set值班安排id(id);
        return dutyScheduleService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return dutyScheduleService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return dutyScheduleService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
