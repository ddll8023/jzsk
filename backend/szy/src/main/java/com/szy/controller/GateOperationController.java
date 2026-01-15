package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.GateOperation;
import com.szy.service.GateOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 闸门操作记录表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-16
 */
@RestController
@RequestMapping("/gate-operation")
public class GateOperationController {

    @Autowired
    private GateOperationService gateOperationService;

    @GetMapping
    public Result<List<GateOperation>> getAll() {
        return Result.ok(gateOperationService.list());
    }

    @GetMapping("/page")
    public Result<Page<GateOperation>> getPage(@RequestParam(defaultValue = "1") int current,
                                               @RequestParam(defaultValue = "10") int size) {
        return Result.ok(gateOperationService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<GateOperation> getById(@PathVariable Integer id) {
        return Result.ok(gateOperationService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody GateOperation entity) {
        return gateOperationService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody GateOperation entity) {
        entity.setId(id);
        return gateOperationService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return gateOperationService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return gateOperationService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
