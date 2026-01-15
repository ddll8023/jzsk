package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Impoundment;
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
 * 蓄水池控制器
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
@RestController
@RequestMapping("/impoundment")
public class ImpoundmentController extends BaseController {
    /**
     * 查询指定蓄水池信息
     *
     * @param id 蓄水池id
     * @return 成功信息，蓄水池json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        Impoundment impoundment = impoundmentService.getById(id);
        if (impoundment == null)
            return Result.fail(ResponseCode.NOT_EXIST, "查询蓄水池信息不存在");
        return Result.ok(impoundment);
    }

    /**
     * 通过蓄水池名查询
     *
     * @param currentPage 当前页码
     * @param pageSize    页大小
     * @param name        蓄水池
     * @return 成功信息，蓄水池查询json
     */
    @GetMapping("/name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<Impoundment> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        Page<Impoundment> page = new Page<>(currentPage, pageSize);
        Page<Impoundment> impoundmentPage = impoundmentService.page(page, wrapper);
        return Result.ok(impoundmentPage);
    }

    /**
     * 蓄水池列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，蓄水池json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<Impoundment> page = new Page<>(currentPage, pageSize);
        Page<Impoundment> impoundmentPage = impoundmentService.page(page);
        return Result.ok(impoundmentPage);
    }

    /**
     * 新增蓄水池
     *
     * @param impoundment 蓄水池json
     * @return 成功信息，蓄水池json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@Validated @RequestBody Impoundment impoundment) {
        impoundmentService.addImpoundment(impoundment);
        return Result.ok(impoundment);
    }

    /**
     * 删除蓄水池
     *
     * @param id 蓄水池id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        Impoundment byId = impoundmentService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除蓄水池不存在");
        }
        impoundmentService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新蓄水池信息
     *
     * @param impoundment 蓄水池json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody Impoundment impoundment) {
        Impoundment byId = impoundmentService.getById(impoundment.getId());
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "待更新蓄水池不存在");
        impoundmentService.updateImpoundment(impoundment);
        return Result.ok(impoundment);
    }

    /**
     * 从excel中批量导入蓄水池
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<Impoundment> impoundment = EasyPoiUtil.importExcel(multipartFile, 0, 1, Impoundment.class);
        for (Impoundment i : impoundment) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (i.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            if (i.getLongitude().compareTo(new BigDecimal("180")) >= 0 ||
                    i.getLongitude().compareTo(new BigDecimal("-180")) <= 0) {
                return Result.fail(ResponseCode.FAIL, "经度输入有误，请重新输入");
            }
            if (i.getLatitude().compareTo(new BigDecimal("90")) >= 0 ||
                    i.getLatitude().compareTo(new BigDecimal("-90")) <= 0) {
                return Result.fail(ResponseCode.FAIL, "纬度输入有误，请重新输入");
            }
            impoundmentService.addImpoundment(i);
        }
        return Result.ok(impoundment);
    }

    /**
     * 导出全部蓄水池信息
     *
     * @return 全部蓄水池信息
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<Impoundment> impoundments = impoundmentService.exportAll();
        return Result.ok(impoundments);
    }

}
