package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.entity.RainfallStations;
import com.szy.service.RainfallStationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 雨量监测站数据表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-13
 */
@RestController
@RequestMapping("/rainfall-stations")
public class RainfallStationsController {

    @Autowired
    private RainfallStationsService rainfallStationsService;

    // 查询所有雨量监测站数据
    @GetMapping
    public List<RainfallStations> getAllRainfallStations() {
        return rainfallStationsService.list();
    }

    // 分页查询雨量监测站数据
    @GetMapping("/page")
    public Page<RainfallStations> getRainfallStationsPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        return rainfallStationsService.page(new Page<>(current, size));
    }

    // 根据 ID 查询雨量监测站数据
    @GetMapping("/{id}")
    public RainfallStations getRainfallStationById(@PathVariable Integer id) {
        return rainfallStationsService.getById(id);
    }

    // 添加雨量监测站数据
    @PostMapping
    public boolean addRainfallStation(@RequestBody RainfallStations rainfallStations) {
        return rainfallStationsService.save(rainfallStations);
    }

    // 更新雨量监测站数据
    @PutMapping("/{id}")
    public boolean updateRainfallStation(@PathVariable Integer id, @RequestBody RainfallStations rainfallStations) {
        rainfallStations.setId(id);
        return rainfallStationsService.updateById(rainfallStations);
    }

    // 删除雨量监测站数据
    @DeleteMapping("/{id}")
    public boolean deleteRainfallStation(@PathVariable Integer id) {
        return rainfallStationsService.removeById(id);
    }
}
