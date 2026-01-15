package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.AnnualWaterSituation;
import com.szy.service.AnnualWaterSituationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 年度水情 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@RestController
@RequestMapping("/annual-water-situation")
public class AnnualWaterSituationController {

    @Autowired
    private AnnualWaterSituationService annualWaterSituationService;

    @GetMapping
    public Result<List<AnnualWaterSituation>> getAll() {
        return Result.ok(annualWaterSituationService.list());
    }

    @GetMapping("/page")
    public Result<Page<AnnualWaterSituation>> getPage(@RequestParam(defaultValue = "1") int current,
                                                    @RequestParam(defaultValue = "10") int size) {
        return Result.ok(annualWaterSituationService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<AnnualWaterSituation> getById(@PathVariable Integer id) {
        return Result.ok(annualWaterSituationService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody AnnualWaterSituation entity) {
        return annualWaterSituationService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody AnnualWaterSituation entity) {
        entity.setRecordId(id);
        return annualWaterSituationService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return annualWaterSituationService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return annualWaterSituationService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
