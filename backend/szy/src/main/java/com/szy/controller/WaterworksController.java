package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.AdministrationDivision;
import com.szy.entity.Reservoir;
import com.szy.entity.Waterworks;
import com.szy.util.EasyPoiUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 水厂（工程站点）控制器
 * </p>
 *
 * @author l
 * @since 2022-01-24
 */
@RestController
@RequestMapping("/waterworks")
public class WaterworksController extends BaseController {
    /**
     * 查询指定水厂信息
     *
     * @param id 水厂id
     * @return 成功信息，水厂json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        Waterworks waterworks = waterworksService.getById(id);
        if (waterworks == null){
            return Result.fail(ResponseCode.NOT_EXIST, "查询水厂信息不存在");
        }
        return Result.ok(waterworks);
    }

    /**
     * 通过水厂名查询
     *
     * @param currentPage 当前页码
     * @param pageSize    页大小
     * @param name        水厂
     * @return 成功信息，水厂查询json
     */
    @GetMapping("/name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<Waterworks> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        Page<Waterworks> page = new Page<>(currentPage, pageSize);
        Page<Waterworks> waterworksPage = waterworksService.page(page, wrapper);
        return Result.ok(waterworksPage);
    }

    /**
     * 水厂列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，水厂json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<Waterworks> page = new Page<>(currentPage, pageSize);
        Page<Waterworks> waterworksPage = waterworksService.page(page);
        return Result.ok(waterworksPage);
    }

    /**
     * 新增水厂
     *
     * @param waterworks 水厂json
     * @return 成功信息，水厂json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@Validated @RequestBody Waterworks waterworks) {
        waterworksService.addWaterworks(waterworks);
        return Result.ok(waterworks);
    }

    /**
     * 删除水厂
     *
     * @param id 水厂id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        Waterworks byId = waterworksService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除水厂不存在");
        }
        waterworksService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新水厂信息
     *
     * @param waterworks 水厂json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody Waterworks waterworks) {
        Waterworks byId = waterworksService.getById(waterworks.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新水厂不存在");
        }
        waterworksService.updateWaterworks(waterworks);
        return Result.ok(waterworks);
    }

    /**
     * 从excel中批量导入水厂
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<Waterworks> waterworks = EasyPoiUtil.importExcel(multipartFile, 0, 1, Waterworks.class);
        for (Waterworks i : waterworks) {
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
            waterworksService.addWaterworks(i);
        }
        return Result.ok(waterworks);
    }

//    /**
//     * 导出数据到excel
//     * @param response HttpServletResponse response
//     * @return waterworks列表
//     * @throws IOException 异常
//     */
//    @GetMapping("/export-excel")
//    @PreAuthorize("hasAuthority('gcxx_jcxx')")
//    public Result exportExcel(HttpServletResponse response) throws IOException {
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
//        String fileName = URLEncoder.encode("水厂", "UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//        QueryWrapper<Waterworks> queryWrapper = new QueryWrapper<>();
//        List<Waterworks> waterworks = waterworksService.list(queryWrapper);
//        EasyExcel.write(response.getOutputStream(), Waterworks.class).sheet("水厂").doWrite(waterworks);
//        return Result.ok(waterworks);
//    }

    /**
     * 导出全部水厂信息
     *
     * @return 全部水厂信息
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<Waterworks> waterworks = waterworksService.exportAll();
        return Result.ok(waterworks);
    }
}
