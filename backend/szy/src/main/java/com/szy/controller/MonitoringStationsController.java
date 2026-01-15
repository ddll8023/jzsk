package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.MonitoringStations;
import com.szy.service.MonitoringStationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 监测站点 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@RestController
@RequestMapping("/monitoring-stations")
public class MonitoringStationsController {

    @Autowired
    private MonitoringStationsService monitoringStationsService;

    @GetMapping
    public Result<List<MonitoringStations>> getAll() {
        return Result.ok(monitoringStationsService.list());
    }

    @GetMapping("/page")
    public Result<Page<MonitoringStations>> getPage(@RequestParam(defaultValue = "1") int current,
                                                  @RequestParam(defaultValue = "10") int size) {
        return Result.ok(monitoringStationsService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<MonitoringStations> getById(@PathVariable Integer id) {
        return Result.ok(monitoringStationsService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody MonitoringStations entity) {
        return monitoringStationsService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody MonitoringStations entity) {
        entity.setStationId(id);
        return monitoringStationsService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return monitoringStationsService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return monitoringStationsService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
