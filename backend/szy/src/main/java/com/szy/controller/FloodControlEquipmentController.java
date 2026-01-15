package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.FloodControlEquipment;
import com.szy.service.FloodControlEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 防汛设备管理表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-25
 */
@RestController
@RequestMapping("/flood-control-equipment")
public class FloodControlEquipmentController {

    @Autowired
    private FloodControlEquipmentService floodControlEquipmentService;

    @GetMapping
    public Result<List<FloodControlEquipment>> getAll() {
        return Result.ok(floodControlEquipmentService.list());
    }

    @GetMapping("/page")
    public Result<Page<FloodControlEquipment>> getPage(@RequestParam(defaultValue = "1") int current,
                                                     @RequestParam(defaultValue = "10") int size) {
        return Result.ok(floodControlEquipmentService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<FloodControlEquipment> getById(@PathVariable Integer id) {
        return Result.ok(floodControlEquipmentService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody FloodControlEquipment entity) {
        return floodControlEquipmentService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody FloodControlEquipment entity) {
        entity.setEquipmentId(id);
        return floodControlEquipmentService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return floodControlEquipmentService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return floodControlEquipmentService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
