package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.AdministrationDivision;
import com.szy.entity.SurfaceWaterSources;
import com.szy.entity.WarningIndicatorSetting;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 地表水源水 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
@RestController
@RequestMapping("/surface-water-sources")
public class SurfaceWaterSourcesController extends BaseController {
    /**
     * 查询指定地表水源水信息
     *
     * @param id 地表水源水id
     * @return 成功信息，地表水源水json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        SurfaceWaterSources surfaceWaterSources = surfaceWaterSourcesService.getById(id);
        if (surfaceWaterSources == null)
            return Result.fail(ResponseCode.NOT_EXIST, "查询地表水源水信息不存在");
        return Result.ok(surfaceWaterSources);
    }

    /**
     * 通过地表水源水名查询
     *
     * @param currentPage 当前页码
     * @param pageSize    页大小
     * @param name        地表水源水
     * @return 成功信息，地表水源水查询json
     */
    @GetMapping("/search-list-by-name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<SurfaceWaterSources> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        Page<SurfaceWaterSources> page = new Page<>(currentPage, pageSize);
        Page<SurfaceWaterSources> surfaceWaterSourcesPage = surfaceWaterSourcesService.page(page, wrapper);
        return Result.ok(surfaceWaterSourcesPage);
    }

    /**
     * 地表水源水列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，地表水源水json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<SurfaceWaterSources> page = new Page<>(currentPage, pageSize);
        Page<SurfaceWaterSources> surfaceWaterSourcesPage = surfaceWaterSourcesService.page(page);
        return Result.ok(surfaceWaterSourcesPage);
    }

    /**
     * 新增地表水源水
     *
     * @param surfaceWaterSources 地表水源水json
     * @return 成功信息，地表水源水json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@Validated @RequestBody SurfaceWaterSources surfaceWaterSources) {
        surfaceWaterSourcesService.saveSurfaceWaterSources(surfaceWaterSources);
        return Result.ok(surfaceWaterSources);
    }

    /**
     * 删除地表水源水
     *
     * @param id 地表水源水id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        SurfaceWaterSources byId = surfaceWaterSourcesService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除地表水源水不存在");
        }
        surfaceWaterSourcesService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新地表水源水信息
     *
     * @param surfaceWaterSources 地表水源水json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody SurfaceWaterSources surfaceWaterSources) {
        SurfaceWaterSources byId = surfaceWaterSourcesService.getById(surfaceWaterSources.getId());
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "待更新地表水源水不存在");
        surfaceWaterSourcesService.updateSurfaceWaterSources(surfaceWaterSources);
        return Result.ok(surfaceWaterSources);
    }

    /**
     * 从excel中批量导入地表水源水
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<SurfaceWaterSources> surfaceWaterSources = EasyPoiUtil.importExcel(multipartFile, 0, 1, SurfaceWaterSources.class);

        for (SurfaceWaterSources i : surfaceWaterSources) {
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
            surfaceWaterSourcesService.saveSurfaceWaterSources(i);
        }
        return Result.ok(surfaceWaterSources);
    }

//    /**
//     * 导出数据到excel
//     * @param response HttpServletResponse response
//     * @return surfaceWaterSources列表
//     * @throws IOException 异常
//     */
//    @GetMapping("/export-excel")
//    @PreAuthorize("hasAuthority('gcxx_jcxx')")
//    public Result exportExcel(HttpServletResponse response) throws IOException {
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
//        String fileName = URLEncoder.encode("地表水源水", "UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//        QueryWrapper<SurfaceWaterSources> queryWrapper = new QueryWrapper<>();
//        List<SurfaceWaterSources> surfaceWaterSources = surfaceWaterSourcesService.list(queryWrapper);
//        EasyExcel.write(response.getOutputStream(), SurfaceWaterSources.class).sheet("地表水源水").doWrite(surfaceWaterSources);
//        return Result.ok(surfaceWaterSources);
//    }

    /**
     * 导出全部地表水源水
     *
     * @return 地表水源水信息
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<SurfaceWaterSources> surfaceWaterSources = surfaceWaterSourcesService.exportAll();
        return Result.ok(surfaceWaterSources);
    }
}
