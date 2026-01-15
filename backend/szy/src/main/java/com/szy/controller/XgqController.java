package com.szy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.entity.Xgq;
import com.szy.service.XgqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.dynamic.datasource.annotation.DS;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/zkxt/xgq")
@DS("zkxt")
public class XgqController {

    @Autowired
    private XgqService xgqService;

    @GetMapping("/page")
    public IPage<Xgq> page(@RequestParam(defaultValue = "1") int current,
                          @RequestParam(defaultValue = "10") int size) {
        return xgqService.page(new Page<>(current, size), new QueryWrapper<>());
    }

    @GetMapping
    public List<Xgq> list() {
        // 使用JDBC查询，绕过MyBatis-Plus的SQL Server float类型映射问题
        return xgqService.selectAllWithJdbc();
    }

    @GetMapping("/{tm}")
    public Xgq getByTm(@PathVariable String tm) {
        return xgqService.getById(tm);
    }

    @PostMapping
    public boolean save(@RequestBody Xgq xgq) {
        return xgqService.save(xgq);
    }

    @PutMapping
    public boolean update(@RequestBody Xgq xgq) {
        return xgqService.updateById(xgq);
    }

    @DeleteMapping("/{tm}")
    public boolean delete(@PathVariable String tm) {
        return xgqService.removeById(tm);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        // TODO: 实现Excel导出逻辑
        // List<Xgq> list = xgqService.list();
        // ExcelUtil.exportExcel(response, list, Xgq.class, "XGQ数据");
    }

    @GetMapping("/test-jdbc")
    public String testJdbc() {
        try {
            // 使用原生JDBC查询，绕过所有ORM框架
            return xgqService.testJdbcQuery();
        } catch (Exception e) {
            return "JDBC查询异常: " + e.getMessage() + "\n" + e.getStackTrace()[0];
        }
    }
} 