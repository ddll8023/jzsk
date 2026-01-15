package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Herb;
import com.szy.util.EasyPoiUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 药材控制器
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
@RestController
@RequestMapping("/herb")
public class HerbController extends BaseController {
    /**
     * 查询指定药材信息
     *
     * @param id 药材id
     * @return 成功信息，药材json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        Herb herb = herbService.getById(id);
        if (herb == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "查询药材信息不存在");
        }
        return Result.ok(herb);
    }

    /**
     * 通过药材名查询
     *
     * @param currentPage 当前页码
     * @param pageSize    页大小
     * @param name        药材
     * @return 成功信息，药材查询json
     */
    @GetMapping("/name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<Herb> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        Page<Herb> page = new Page<>(currentPage, pageSize);
        Page<Herb> herbPage = herbService.page(page, wrapper);
        return Result.ok(herbPage);
    }

    /**
     * 药材列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，药材json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<Herb> page = new Page<>(currentPage, pageSize);
        Page<Herb> herbPage = herbService.page(page);
        return Result.ok(herbPage);
    }

    /**
     * 新增药材
     *
     * @param herb 药材json
     * @return 成功信息，药材json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@Validated @RequestBody Herb herb) {
        herbService.save(herb);
        return Result.ok(herb);
    }

    /**
     * 删除药材
     *
     * @param id 药材id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        Herb byId = herbService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除药材不存在");
        }
        herbService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新药材信息
     *
     * @param herb 药材json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody Herb herb) {
        Herb byId = herbService.getById(herb.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新药材不存在");
        }
        herbService.updateById(herb);
        return Result.ok(herb);
    }

    /**
     * 从excel中批量导入药材
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<Herb> herb = EasyPoiUtil.importExcel(multipartFile, 0, 1, Herb.class);
        for (Herb i : herb) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (i.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            herbService.save(i);
        }
        return Result.ok(herb);
    }

    /**
     * 导出全部药材信息
     *
     * @return 全部药材信息
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<Herb> herbs = herbService.exportAll();
        return Result.ok(herbs);
    }

}
