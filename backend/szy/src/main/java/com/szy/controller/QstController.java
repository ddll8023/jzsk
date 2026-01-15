package com.szy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.entity.Qst;
import com.szy.service.QstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.dynamic.datasource.annotation.DS;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/zkxt/qst")
@DS("zkxt")
public class QstController {

    @Autowired
    private QstService qstService;

    @GetMapping("/page")
    public IPage<Qst> page(@RequestParam(defaultValue = "1") int current,
                          @RequestParam(defaultValue = "10") int size) {
        return qstService.page(new Page<>(current, size), new QueryWrapper<>());
    }

    @GetMapping
    public List<Qst> list() {
        // 使用JDBC查询，绕过MyBatis-Plus的SQL Server float类型映射问题
        return qstService.selectAllWithJdbc();
    }

    @GetMapping("/{tm}")
    public Qst getByTm(@PathVariable String tm) {
        return qstService.getById(tm);
    }

    @PostMapping
    public boolean save(@RequestBody Qst qst) {
        return qstService.save(qst);
    }

    @PutMapping
    public boolean update(@RequestBody Qst qst) {
        return qstService.updateById(qst);
    }

    @DeleteMapping("/{tm}")
    public boolean delete(@PathVariable String tm) {
        return qstService.removeById(tm);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        // TODO: 实现Excel导出逻辑
        // List<Qst> list = qstService.list();
        // ExcelUtil.exportExcel(response, list, Qst.class, "QST数据");
    }

    @GetMapping("/test-jdbc")
    public String testJdbc() {
        try {
            // 使用原生JDBC查询，绕过所有ORM框架
            return qstService.testJdbcQuery();
        } catch (Exception e) {
            return "JDBC查询异常: " + e.getMessage() + "\n" + e.getStackTrace()[0];
        }
    }
} 