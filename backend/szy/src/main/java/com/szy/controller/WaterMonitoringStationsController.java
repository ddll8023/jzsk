package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.WaterMonitoringStations;
import com.szy.service.WaterMonitoringStationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 水质监测站 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@RestController
@RequestMapping("/water-monitoring-stations")
public class WaterMonitoringStationsController {

    @Autowired
    private WaterMonitoringStationsService waterMonitoringStationsService;

    @GetMapping
    public Result<List<WaterMonitoringStations>> getAll() {
        return Result.ok(waterMonitoringStationsService.list());
    }

    @GetMapping("/page")
    public Result<Page<WaterMonitoringStations>> getPage(@RequestParam(defaultValue = "1") int current,
                                                       @RequestParam(defaultValue = "10") int size) {
        return Result.ok(waterMonitoringStationsService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<WaterMonitoringStations> getById(@PathVariable Integer id) {
        return Result.ok(waterMonitoringStationsService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody WaterMonitoringStations entity) {
        return waterMonitoringStationsService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody WaterMonitoringStations entity) {
        entity.setStationId(id);
        return waterMonitoringStationsService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return waterMonitoringStationsService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return waterMonitoringStationsService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
