package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.WaterStorage;
import com.szy.service.WaterStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 水库水量表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-15
 */
@RestController
@RequestMapping("/water-storage")
public class WaterStorageController {

    @Autowired
    private WaterStorageService waterStorageService;

    /**
     * 查询所有水库水量数据
     */
    @GetMapping
    public Result<List<WaterStorage>> getAllWaterStorages() {
        List<WaterStorage> list = waterStorageService.list();
        return Result.ok(list);
    }

    /**
     * 分页查询水库水量数据
     */
    @GetMapping("/page")
    public Result<Page<WaterStorage>> getWaterStoragePage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        Page<WaterStorage> page = waterStorageService.page(new Page<>(current, size));
        return Result.ok(page);
    }

    /**
     * 根据ID查询水库水量数据
     */
    @GetMapping("/{id}")
    public Result<WaterStorage> getWaterStorageById(@PathVariable Integer id) {
        WaterStorage waterStorage = waterStorageService.getById(id);
        return Result.ok(waterStorage);
    }

    /**
     * 添加水库水量数据
     */
    @PostMapping
    public Result<Boolean> addWaterStorage(@RequestBody WaterStorage waterStorage) {
        boolean success = waterStorageService.save(waterStorage);
        return success ? Result.ok(true) : Result.fail("添加失败");
    }

    /**
     * 更新水库水量数据
     */
    @PutMapping("/{id}")
    public Result<Boolean> updateWaterStorage(@PathVariable Integer id, @RequestBody WaterStorage waterStorage) {
        waterStorage.setId(id);
        boolean success = waterStorageService.updateById(waterStorage);
        return success ? Result.ok(true) : Result.fail("更新失败");
    }

    /**
     * 删除水库水量数据
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteWaterStorage(@PathVariable Integer id) {
        boolean success = waterStorageService.removeById(id);
        return success ? Result.ok(true) : Result.fail("删除失败");
    }

    /**
     * 批量删除水库水量数据
     */
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteWaterStorage(@RequestBody List<Integer> ids) {
        boolean success = waterStorageService.removeByIds(ids);
        return success ? Result.ok(true) : Result.fail("批量删除失败");
    }

    /**
     * 根据测站名称查询水库水量数据
     */
    @GetMapping("/station/{stationName}")
    public Result<List<WaterStorage>> getByStationName(@PathVariable String stationName) {
        List<WaterStorage> list = waterStorageService.lambdaQuery()
            .eq(WaterStorage::getStationName, stationName)
            .orderByDesc(WaterStorage::getRecordTime)
            .list();
        return Result.ok(list);
    }
}
