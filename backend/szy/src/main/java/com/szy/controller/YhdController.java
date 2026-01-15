package com.szy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.entity.Yhd;
import com.szy.service.YhdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.dynamic.datasource.annotation.DS;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/zkxt/yhd")
@DS("zkxt")
public class YhdController {

    @Autowired
    private YhdService yhdService;

    @GetMapping("/page")
    public IPage<Yhd> page(@RequestParam(defaultValue = "1") int current,
                          @RequestParam(defaultValue = "10") int size) {
        return yhdService.page(new Page<>(current, size), new QueryWrapper<>());
    }

    @GetMapping
    public List<Yhd> list() {
        return yhdService.list();
    }

    @GetMapping("/{tm}")
    public Yhd getByTm(@PathVariable String tm) {
        return yhdService.getById(tm);
    }

    @PostMapping
    public boolean save(@RequestBody Yhd yhd) {
        return yhdService.save(yhd);
    }

    @PutMapping
    public boolean update(@RequestBody Yhd yhd) {
        return yhdService.updateById(yhd);
    }

    @DeleteMapping("/{tm}")
    public boolean delete(@PathVariable String tm) {
        return yhdService.removeById(tm);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        // TODO: 实现Excel导出逻辑
        // List<Yhd> list = yhdService.list();
        // ExcelUtil.exportExcel(response, list, Yhd.class, "YHD数据");
    }
} 