package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.GateInfo;
import com.szy.service.GateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 闸门基本信息表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-16
 */
@RestController
@RequestMapping("/gate-info")
public class GateInfoController {

    @Autowired
    private GateInfoService gateInfoService;

    @GetMapping
    public Result<List<GateInfo>> getAll() {
        return Result.ok(gateInfoService.list());
    }

    @GetMapping("/page")
    public Result<Page<GateInfo>> getPage(@RequestParam(defaultValue = "1") int current,
                                          @RequestParam(defaultValue = "10") int size) {
        return Result.ok(gateInfoService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<GateInfo> getById(@PathVariable Integer id) {
        return Result.ok(gateInfoService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody GateInfo entity) {
        return gateInfoService.save(entity) ? Result.ok(true) : Result.fail("添加失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Integer id, @RequestBody GateInfo entity) {
        entity.setId(id);
        return gateInfoService.updateById(entity) ? Result.ok(true) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return gateInfoService.removeById(id) ? Result.ok(true) : Result.fail("删除失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return gateInfoService.removeByIds(ids) ? Result.ok(true) : Result.fail("批量删除失败");
    }
}
