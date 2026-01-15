package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.entity.StPextremumB;
import com.szy.service.StPextremumBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * rain std criterion 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-05-15
 */
@RestController
@RequestMapping("/st-pextremum-b")
public class StPextremumBController {

    @Autowired
    private StPextremumBService stPextremumBService;

    // 查询所有雨量极值信息
    @GetMapping
    public List<StPextremumB> getAllStPextremumB() {
        return stPextremumBService.list();
    }
    @GetMapping(params = "stcd")
    public List<StPextremumB> getByStcd(@RequestParam String stcd) {
        return stPextremumBService.lambdaQuery().eq(StPextremumB::getStcd, stcd).list();
    }
    // 分页查询雨量极值信息
    @GetMapping("/page")
    public Page<StPextremumB> getStPextremumBPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        return stPextremumBService.page(new Page<>(current, size));
    }

    // 根据 STCD 查询雨量极值信息
    @GetMapping("/{STCD}")
    public StPextremumB getStPextremumBBySTCD(@PathVariable String STCD) {
        return stPextremumBService.getById(STCD);
    }

    // 新增雨量极值信息
    @PostMapping
    public boolean addStPextremumB(@RequestBody StPextremumB stPextremumB) {
        return stPextremumBService.save(stPextremumB);
    }

    // 更新雨量极值信息
    @PutMapping("/{STCD}")
    public boolean updateStPextremumB(@PathVariable String STCD, @RequestBody StPextremumB stPextremumB) {
        stPextremumB.setStcd(STCD); // 设置主键字段
        return stPextremumBService.updateById(stPextremumB);
    }

    // 删除雨量极值信息
    @DeleteMapping("/{STCD}")
    public boolean deleteStPextremumB(@PathVariable String STCD) {
        return stPextremumBService.removeById(STCD);
    }
}
