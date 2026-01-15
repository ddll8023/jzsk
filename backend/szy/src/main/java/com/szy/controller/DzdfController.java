package com.szy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.entity.Dzdf;
import com.szy.service.DzdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.dynamic.datasource.annotation.DS;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/zkxt/dzdf")
@DS("zkxt")
public class DzdfController {

    @Autowired
    private DzdfService dzdfService;

    @GetMapping("/page")
    public IPage<Dzdf> page(@RequestParam(defaultValue = "1") int current,
                          @RequestParam(defaultValue = "10") int size) {
        return dzdfService.page(new Page<>(current, size), new QueryWrapper<>());
    }

    @GetMapping
    public List<Dzdf> list() {
        return dzdfService.list();
    }

    @GetMapping("/{tm}")
    public Dzdf getByTm(@PathVariable String tm) {
        return dzdfService.getById(tm);
    }

    @PostMapping
    public boolean save(@RequestBody Dzdf dzdf) {
        return dzdfService.save(dzdf);
    }

    @PutMapping
    public boolean update(@RequestBody Dzdf dzdf) {
        return dzdfService.updateById(dzdf);
    }

    @DeleteMapping("/{tm}")
    public boolean delete(@PathVariable String tm) {
        return dzdfService.removeById(tm);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        // TODO: 实现Excel导出逻辑
        // List<Dzdf> list = dzdfService.list();
        // ExcelUtil.exportExcel(response, list, Dzdf.class, "DZDF数据");
    }
} 