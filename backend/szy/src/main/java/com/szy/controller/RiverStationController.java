package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.RiverStation;
import com.szy.service.RiverStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 河道测站 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@RestController
@RequestMapping("/river-station")
public class RiverStationController {

    @Autowired
    private RiverStationService riverStationService;

    @GetMapping
    public Result<List<RiverStation>> getAll() {
        return Result.ok(riverStationService.list());
    }

    @GetMapping("/page")
    public Result<Page<RiverStation>> getPage(@RequestParam(defaultValue = "1") int current,
                                            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(riverStationService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<RiverStation> getById(@PathVariable Integer id) {
        return Result.ok(riverStationService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody RiverStation entity) {
        return riverStationService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody RiverStation entity) {
        entity.setStationId(id);
        return riverStationService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return riverStationService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return riverStationService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
