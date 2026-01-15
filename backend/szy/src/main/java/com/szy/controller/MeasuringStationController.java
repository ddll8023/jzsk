package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.javaparser.utils.Log;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.AdministrationDivision;
import com.szy.entity.MeasuringStation;
import com.szy.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.annotation.Nullable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监测站点 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/measuring-station")
public class MeasuringStationController extends BaseController {
    /**
     * 查询指定监测站点信息
     *
     * @param id 监测站点id
     * @return 成功信息，监测站点json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        MeasuringStation measuringStation = measuringStationService.getById(id);
        if (measuringStation == null){
            return Result.fail(ResponseCode.NOT_EXIST, "查询监测站点信息不存在");
        }
        return Result.ok(measuringStation);
    }

    /**
     * 监测站点列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，监测站点json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result list(@RequestParam(value = "name", required = false) String name,
                       @RequestParam("currentPage") Integer currentPage,
                       @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<MeasuringStation> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            wrapper.like("name", name);
        }
        Page<MeasuringStation> page = new Page<>(currentPage, pageSize);
        Page<MeasuringStation> measuringStationPage = measuringStationService.page(page, wrapper);
        return Result.ok(measuringStationPage);
    }

    /**
     * 新增监测站点
     *
     * @param measuringStation 测站信息
     * @return 成功
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result save(@RequestBody MeasuringStation measuringStation) {
        measuringStationService.saveMeasuringStation(measuringStation);
        return Result.ok(measuringStation);
    }

    /**
     * 删除监测站点
     *
     * @param id 监测站点id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result delete(@PathVariable Long id) {
        MeasuringStation byId = measuringStationService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除监测站点不存在");
        }
        measuringStationService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新监测站点信息
     *
     * @param measuringStation 测站信息
     * @return 成功
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result update(@RequestBody MeasuringStation measuringStation) {
        MeasuringStation byId = measuringStationService.getById(measuringStation.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新监测站点不存在");
        }
        measuringStationService.updateMeasuringStation(measuringStation);
        return Result.ok(measuringStation);
    }

    /**
     * 从excel中批量导入监测站点
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<MeasuringStation> measuringStations = EasyPoiUtil.importExcel(multipartFile, 0, 1, MeasuringStation.class);
        for (MeasuringStation i : measuringStations) {
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
            measuringStationService.saveMeasuringStation(i);
        }
        return Result.ok(measuringStations);
    }

    /**
     * 导出全部监测站点
     *
     * @return 全部监测站点
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result exportExcel() {
        List<MeasuringStation> measuringStations = measuringStationService.exportAll();
        return Result.ok(measuringStations);
    }

    @GetMapping("/getCodes")
    public Result getCodes() {
        Map<String, String> map = measuringStationService.getAllNamesAndCodes();
        return Result.ok(map);
    }

}
