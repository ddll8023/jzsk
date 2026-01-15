package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.entity.DailyRainfall;
import com.szy.service.DailyRainfallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 逐日降雨查询表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-15
 */
@RestController
@RequestMapping("/daily-rainfall")
public class DailyRainfallController extends BaseController {

    @Autowired
    private DailyRainfallService dailyRainfallService;

    /**
     * 查询所有逐日降雨数据
     * @return 逐日降雨数据列表
     */
    @GetMapping
    public Result<List<DailyRainfall>> getAllDailyRainfalls() {
        List<DailyRainfall> list = dailyRainfallService.list();
        return Result.ok(list);
    }

    /**
     * 分页查询逐日降雨数据
     * @param current 当前页
     * @param size 每页大小
     * @return 分页数据
     */
    @GetMapping("/page")
    public Result<Page<DailyRainfall>> getDailyRainfallPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        Page<DailyRainfall> page = dailyRainfallService.page(new Page<>(current, size));
        return Result.ok(page);
    }

    /**
     * 根据ID查询逐日降雨数据
     * @param id 数据ID
     * @return 逐日降雨数据
     */
    @GetMapping("/{id}")
    public Result<DailyRainfall> getDailyRainfallById(@PathVariable Integer id) {
        DailyRainfall dailyRainfall = dailyRainfallService.getById(id);
        return Result.ok(dailyRainfall);
    }

    /**
     * 根据测站名称查询逐日降雨数据
     * @param stationName 测站名称
     * @return 逐日降雨数据列表
     */
    @GetMapping("/station/{stationName}")
    public Result<List<DailyRainfall>> getDailyRainfallByStationName(@PathVariable String stationName) {
        List<DailyRainfall> list = dailyRainfallService.lambdaQuery()
                .eq(DailyRainfall::getStationName, stationName)
                .list();
        return Result.ok(list);
    }

    /**
     * 添加逐日降雨数据
     * @param dailyRainfall 逐日降雨数据
     * @return 操作结果
     */
    @PostMapping
    public Result<Boolean> addDailyRainfall(@RequestBody DailyRainfall dailyRainfall) {
        boolean success = dailyRainfallService.save(dailyRainfall);
        return success ? Result.ok(true) : Result.fail("添加失败");
    }

    /**
     * 更新逐日降雨数据
     * @param id 数据ID
     * @param dailyRainfall 逐日降雨数据
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Boolean> updateDailyRainfall(@PathVariable Integer id, @RequestBody DailyRainfall dailyRainfall) {
        dailyRainfall.setId(id);
        boolean success = dailyRainfallService.updateById(dailyRainfall);
        return success ? Result.ok(true) : Result.fail("更新失败");
    }

    /**
     * 删除逐日降雨数据
     * @param id 数据ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDailyRainfall(@PathVariable Integer id) {
        boolean success = dailyRainfallService.removeById(id);
        return success ? Result.ok(true) : Result.fail("删除失败");
    }

    /**
     * 批量删除逐日降雨数据
     * @param ids ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteDailyRainfall(@RequestBody List<Integer> ids) {
        boolean success = dailyRainfallService.removeByIds(ids);
        return success ? Result.ok(true) : Result.fail("批量删除失败");
    }

    /**
     * 根据测站名称查询逐日降雨数据
     * @param stationName 测站名称
     * @return 逐日降雨数据列表
     */
    @GetMapping("/search")
    public Result<List<DailyRainfall>> searchDailyRainfall(
            @RequestParam(required = false) String stationName) {
        
        List<DailyRainfall> list = dailyRainfallService.lambdaQuery()
                .eq(stationName != null, DailyRainfall::getStationName, stationName)
                .list();
        
        return Result.ok(list);
    }
}
