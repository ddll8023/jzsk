package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Reservoir;
import com.szy.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 水库（工程站点）控制器
 * </p>
 *
 * @author l
 * @since 2022-01-23
 */
@RestController
@RequestMapping("/reservoir")
public class ReservoirController extends BaseController {
    /**
     * 查询指定水库信息
     *
     * @param id 水库id
     * @return 成功信息，水库json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        Reservoir reservoir = reservoirService.getById(id);
        if (reservoir == null)
            return Result.fail(ResponseCode.NOT_EXIST, "查询水库信息不存在");
        return Result.ok(reservoir);
    }

    /**
     * 通过水库名查询
     *
     * @param currentPage 当前页码
     * @param pageSize    页大小
     * @param name        水库
     * @return 成功信息，水库查询json
     */
    @GetMapping("/name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<Reservoir> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        Page<Reservoir> page = new Page<>(currentPage, pageSize);
        Page<Reservoir> reservoirPage = reservoirService.page(page, wrapper);
        return Result.ok(reservoirPage);
    }

    /**
     * 水库列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，水库json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<Reservoir> page = new Page<>(currentPage, pageSize);
        Page<Reservoir> reservoirPage = reservoirService.page(page);
        return Result.ok(reservoirPage);
    }

    /**
     * 新增水库
     *
     * @param reservoir 水库json
     * @return 成功信息，水库json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@Validated @RequestBody Reservoir reservoir) {
        reservoirService.addReservoir(reservoir);
        return Result.ok(reservoir);
    }

    /**
     * 删除水库
     *
     * @param id 水库id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        Reservoir byId = reservoirService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除水库不存在");
        }
        reservoirService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新水库信息
     *
     * @param reservoir 水库json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody Reservoir reservoir) {
        Reservoir byId = reservoirService.getById(reservoir.getId());
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "待更新水库不存在");
        reservoirService.updateReservoir(reservoir);
        return Result.ok(reservoir);
    }

    /**
     * 从excel中批量导入水库
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<Reservoir> reservoirs = EasyPoiUtil.importExcel(multipartFile, 0, 1, Reservoir.class);
        for (Reservoir i : reservoirs) {
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
            reservoirService.addReservoir(i);
        }
        return Result.ok(reservoirs);
    }


    /**
     * 导出全部水库信息
     *
     * @return 全部水库信息
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<Reservoir> reservoirs = reservoirService.exportAll();
        return Result.ok(reservoirs);
    }


    /**
     * 导出全部水库信息
     *
     * @return 全部水库信息
     */
    @GetMapping("/getone")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result getOne(@RequestParam(value = "position", required = false) String position) {
        QueryWrapper<Reservoir> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(position)) {
            wrapper.eq("name", position);
        }
        Reservoir reservoir = reservoirService.getOne(wrapper);
        return Result.ok(reservoir);
    }

    /**
     * 获取所有的水库名称
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/18 20:10
     */
    @GetMapping("/getallnames")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result getAllReservoirs(){
        List<String> reservoirs = reservoirService.getAllReservoirs();
        return Result.ok(reservoirs);
    }

}
