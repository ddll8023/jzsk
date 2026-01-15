package com.szy.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.common.vo.WaterLevelVO;
import com.szy.common.vo.WaterQualityVO;
import com.szy.entity.WarningInformation;
import com.szy.entity.WaterLevel;
import com.szy.entity.WaterQuality;
import com.szy.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/water-quality")
@DS("gcdd")
public class WaterQualityController extends BaseController {

    /**
     * 根据id查询水质信息
     * @param id
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/16 22:02
     */
    @PreAuthorize("hasAuthority('spjk_szjc')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        WaterQuality waterQuality = waterQualityService.getById(id);
        if (waterQuality == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "查询水位信息不存在");
        }
        return Result.ok(waterQuality);
    }

    /**
     * 根据监测点查询水质信息
     * @param currentPage
     * @param pageSize
     * @param position
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/16 22:06
     */
    @PreAuthorize("hasAuthority('spjk_szjc')")
    @GetMapping("/list")
    public Result getlist(@RequestParam("currentPage") Integer currentPage,
                          @RequestParam("pageSize") Integer pageSize,
                          @RequestParam(value = "position", required = false) String position,
                          @RequestParam(value = "startTime", required = false)
                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                          @RequestParam(value = "endTime", required = false)
                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end){
        QueryWrapper<WaterQuality> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(position)) {
            wrapper.like("position", position);
        }
        if (start != null && end != null) {
            wrapper.between("monitor_time", start, end); // 使用between代替gt和lt，更明确表示时间范围
        }
        // 添加排序条件，按照监测时间逆序
        wrapper.orderByDesc("monitor_time");
        Page<WaterQuality> page = new Page<>(currentPage, pageSize);
        Page<WaterQuality> waterQualityPage = waterQualityService.page(page, wrapper);
        return Result.ok(waterQualityPage);
    }

    /**
     * 获取最新的水质信息
     * @param position
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/22 16:34
     */
    @PreAuthorize("hasAuthority('spjk_szjc')")
    @GetMapping("/last")
    public Result getlast(@RequestParam(value = "position", required = false) String position){
        QueryWrapper<WaterQuality> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(position)) {
            wrapper.like("position", position);
        }
        // 添加排序条件，按照监测时间逆序
        wrapper.orderByDesc("monitor_time");
        // 直接使用selectList来获取所有匹配的结果，并取第一个
        List<WaterQuality> waterQualityList = waterQualityService.list(wrapper);
        if (waterQualityList != null && !waterQualityList.isEmpty()) {
            List<WaterQuality> newWaterQuality = new ArrayList<>();
            newWaterQuality.add(waterQualityList.get(0));
            // 假设Result.ok接受一个对象作为参数，而不是Page对象
            return Result.ok(newWaterQuality); // 返回列表中的第一个元素
        } else {
            return Result.ok(waterQualityList);
        }
    }

    /**
     * 删除水质信息
     * @param id
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/16 22:23
     */
    @PreAuthorize("hasAuthority('spjk_szjc')")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        WaterQuality waterQuality = waterQualityService.getById(id);
        if (waterQuality == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除水质信息不存在");
        }
        waterQualityService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 根据监测点和时间对水质信息进行统计
     * @param start
     * @param end
     * @param position
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/16 22:32
     */
    @PreAuthorize("hasAuthority('spjk_szjc')")
    @GetMapping("/statistics")
    public Result getStatistics(@RequestParam(value = "startTime", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") DateTime start,
                                     @RequestParam(value = "endTime", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") DateTime end,
                                     @RequestParam(value = "position", required = false) String position){
        WaterQualityVO waterQualityVO = waterQualityService.getWaterQualityStatistics(start, end, position);
        return Result.ok(waterQualityVO);
    }

    /**
     * 从excel中批量导入水质信息
     * @param multipartFile
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/19 11:18
     */
    @PreAuthorize("hasAuthority('spjk_szjc')")
    @PostMapping("/import-excel")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) throws ParseException {
        List<WaterQuality> waterQualities = EasyPoiUtil.importExcel(multipartFile, 0, 1, WaterQuality.class);
        for (WaterQuality waterQuality : waterQualities) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (waterQuality.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            waterQualityService.getDeterInformation(waterQuality);
        }
        waterQualityService.saveBatch(waterQualities);
        return Result.ok(waterQualities);
    }

    /**
     * 导出到excel文件
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/19 11:25
     */
    @PreAuthorize("hasAuthority('spjk_szjc')")
    @GetMapping("/export-excel")
    public Result exportAll(@RequestParam(value = "startTime", required = false)
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") DateTime start,
                            @RequestParam(value = "endTime", required = false)
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") DateTime end,
                            @RequestParam(value = "position", required = false) String position) {
        List<WaterQuality> waterQualities = waterQualityService.exportAll(start, end, position);
        return Result.ok(waterQualities);
    }

}
