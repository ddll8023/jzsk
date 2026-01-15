package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.GateAlert;
import com.szy.service.GateAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 闸门报警记录表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-16
 */
@RestController
@RequestMapping("/gate-alert")
public class GateAlertController {

    @Autowired
    private GateAlertService gateAlertService;

    @GetMapping
    public Result<List<GateAlert>> getAll() {
        return Result.ok(gateAlertService.list());
    }

    @GetMapping("/page")
    public Result<Page<GateAlert>> getPage(@RequestParam(defaultValue = "1") int current,
                                           @RequestParam(defaultValue = "10") int size) {
        return Result.ok(gateAlertService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<GateAlert> getById(@PathVariable Integer id) {
        return Result.ok(gateAlertService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody GateAlert entity) {
        return gateAlertService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody GateAlert entity) {
        entity.setId(id);
        return gateAlertService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return gateAlertService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return gateAlertService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
