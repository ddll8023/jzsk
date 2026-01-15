package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.GateReport;
import com.szy.service.GateReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 闸门报表表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-16
 */
@RestController
@RequestMapping("/gate-report")
public class GateReportController {

    @Autowired
    private GateReportService gateReportService;

    @GetMapping
    public Result<List<GateReport>> getAll() {
        return Result.ok(gateReportService.list());
    }

    @GetMapping("/page")
    public Result<Page<GateReport>> getPage(@RequestParam(defaultValue = "1") int current,
                                            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(gateReportService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<GateReport> getById(@PathVariable Integer id) {
        return Result.ok(gateReportService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody GateReport entity) {
        return gateReportService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody GateReport entity) {
        entity.setId(id);
        return gateReportService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return gateReportService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return gateReportService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
