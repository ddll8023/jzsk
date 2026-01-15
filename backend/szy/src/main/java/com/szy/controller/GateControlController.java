package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.GateControl;
import com.szy.service.GateControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 闸门控制记录表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-16
 */
@RestController
@RequestMapping("/gate-control")
public class GateControlController {

    @Autowired
    private GateControlService gateControlService;

    @GetMapping
    public Result<List<GateControl>> getAll() {
        return Result.ok(gateControlService.list());
    }

    @GetMapping("/page")
    public Result<Page<GateControl>> getPage(@RequestParam(defaultValue = "1") int current,
                                             @RequestParam(defaultValue = "10") int size) {
        return Result.ok(gateControlService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<GateControl> getById(@PathVariable Integer id) {
        return Result.ok(gateControlService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody GateControl entity) {
        return gateControlService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody GateControl entity) {
        entity.setId(id);
        return gateControlService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return gateControlService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return gateControlService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
