package com.szy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.entity.Dgq;
import com.szy.service.DgqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.dynamic.datasource.annotation.DS;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.TableField;

@RestController
@RequestMapping("/zkxt/dgq")
@DS("zkxt")
public class DgqController {

    @Autowired
    private DgqService dgqService;

    @GetMapping("/page")
    public IPage<Dgq> page(@RequestParam(defaultValue = "1") int current,
                          @RequestParam(defaultValue = "10") int size) {
        return dgqService.page(new Page<>(current, size), new QueryWrapper<>());
    }

    @GetMapping
    public List<Dgq> list() {
        // 使用JDBC查询，绕过MyBatis-Plus的SQL Server float类型映射问题
        return dgqService.selectAllWithJdbc();
    }

    @GetMapping("/{tm}")
    public Dgq getByTm(@PathVariable String tm) {
        return dgqService.getById(tm);
    }

    @PostMapping
    public boolean save(@RequestBody Dgq dgq) {
        return dgqService.save(dgq);
    }

    @PutMapping
    public boolean update(@RequestBody Dgq dgq) {
        return dgqService.updateById(dgq);
    }

    @DeleteMapping("/{tm}")
    public boolean delete(@PathVariable String tm) {
        return dgqService.removeById(tm);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        // TODO: 实现Excel导出逻辑
        // List<Dgq> list = dgqService.list();
        // ExcelUtil.exportExcel(response, list, Dgq.class, "DGQ数据");
    }

    @GetMapping("/test-sql")
    public String testSql() {
        try {
            // 直接执行SQL查询，查看原始数据
            List<Dgq> list = dgqService.list();
            if (list.isEmpty()) {
                return "查询结果为空";
            }
            
            Dgq first = list.get(0);
            StringBuilder result = new StringBuilder();
            result.append("=== DGQ数据测试 ===\n");
            result.append("TM: ").append(first.getTM()).append("\n");
            result.append("DGQ_M1_Ia: ").append(first.getDGQ_M1_Ia()).append("\n");
            result.append("DGQ_M1_Ib: ").append(first.getDGQ_M1_Ib()).append("\n");
            result.append("DGQ_M1_Ic: ").append(first.getDGQ_M1_Ic()).append("\n");
            result.append("DGQ_M1_Ua: ").append(first.getDGQ_M1_Ua()).append("\n");
            result.append("DGQ_M1_Ub: ").append(first.getDGQ_M1_Ub()).append("\n");
            result.append("DGQ_M1_Uc: ").append(first.getDGQ_M1_Uc()).append("\n");
            result.append("DGQ_M1_Uab: ").append(first.getDGQ_M1_Uab()).append("\n");
            result.append("DGQ_M1_Ubc: ").append(first.getDGQ_M1_Ubc()).append("\n");
            result.append("DGQ_M1_Uca: ").append(first.getDGQ_M1_Uca()).append("\n");
            result.append("DGQ_M1_KD: ").append(first.getDGQ_M1_KD()).append("\n");
            result.append("DGQ_M1_KDSD: ").append(first.getDGQ_M1_KDSD()).append("\n");
            
            return result.toString();
        } catch (Exception e) {
            return "查询异常: " + e.getMessage() + "\n" + e.getStackTrace()[0];
        }
    }

    @GetMapping("/test-custom-sql")
    public String testCustomSql() {
        try {
            // 使用自定义SQL查询
            List<Dgq> list = dgqService.selectTop10WithCustomSql();
            if (list.isEmpty()) {
                return "自定义SQL查询结果为空";
            }
            
            Dgq first = list.get(0);
            StringBuilder result = new StringBuilder();
            result.append("=== 自定义SQL查询测试 ===\n");
            result.append("TM: ").append(first.getTM()).append("\n");
            result.append("DGQ_M1_Ia: ").append(first.getDGQ_M1_Ia()).append("\n");
            result.append("DGQ_M1_Ib: ").append(first.getDGQ_M1_Ib()).append("\n");
            result.append("DGQ_M1_Ic: ").append(first.getDGQ_M1_Ic()).append("\n");
            result.append("DGQ_M1_Ua: ").append(first.getDGQ_M1_Ua()).append("\n");
            result.append("DGQ_M1_Ub: ").append(first.getDGQ_M1_Ub()).append("\n");
            result.append("DGQ_M1_Uc: ").append(first.getDGQ_M1_Uc()).append("\n");
            result.append("DGQ_M1_Uab: ").append(first.getDGQ_M1_Uab()).append("\n");
            result.append("DGQ_M1_Ubc: ").append(first.getDGQ_M1_Ubc()).append("\n");
            result.append("DGQ_M1_Uca: ").append(first.getDGQ_M1_Uca()).append("\n");
            result.append("DGQ_M1_KD: ").append(first.getDGQ_M1_KD()).append("\n");
            result.append("DGQ_M1_KDSD: ").append(first.getDGQ_M1_KDSD()).append("\n");
            
            return result.toString();
        } catch (Exception e) {
            return "自定义SQL查询异常: " + e.getMessage() + "\n" + e.getStackTrace()[0];
        }
    }

    @GetMapping("/test-jdbc")
    public String testJdbc() {
        try {
            // 使用原生JDBC查询，绕过所有ORM框架
            return dgqService.testJdbcQuery();
        } catch (Exception e) {
            return "JDBC查询异常: " + e.getMessage() + "\n" + e.getStackTrace()[0];
        }
    }
} 