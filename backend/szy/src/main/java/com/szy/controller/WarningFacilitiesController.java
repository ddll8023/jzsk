package com.szy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.szy.entity.WarningFacilities;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author l
 * @since 2025-06-19
 */
@RestController
@RequestMapping("/warning-facilities")
public class WarningFacilitiesController {
    @Autowired
    private IService<WarningFacilities> warningFacilitiesService;

    @PostMapping("/add")
    public boolean add(@RequestBody WarningFacilities entity) {
        return warningFacilitiesService.save(entity);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Integer id) {
        return warningFacilitiesService.removeById(id);
    }

    @PutMapping("/update")
    public boolean update(@RequestBody WarningFacilities entity) {
        return warningFacilitiesService.updateById(entity);
    }

    @GetMapping("/get/{id}")
    public WarningFacilities get(@PathVariable Integer id) {
        return warningFacilitiesService.getById(id);
    }

    @GetMapping("/list")
    public List<WarningFacilities> list() {
        return warningFacilitiesService.list(new QueryWrapper<>());
    }
}
