package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.GateStatus;
import com.szy.service.GateStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 闸门状态表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-16
 */
@RestController
@RequestMapping("/gate-status")
public class GateStatusController {

    @Autowired
    private GateStatusService gateStatusService;

    @GetMapping
    public Result<List<GateStatus>> getAll() {
        return Result.ok(gateStatusService.list());
    }

    @GetMapping("/page")
    public Result<Page<GateStatus>> getPage(@RequestParam(defaultValue = "1") int current,
                                            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(gateStatusService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<GateStatus> getById(@PathVariable Integer id) {
        return Result.ok(gateStatusService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody GateStatus entity) {
        return gateStatusService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody GateStatus entity) {
        entity.setId(id);
        return gateStatusService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return gateStatusService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return gateStatusService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
