package com.szy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.MeasuringItem;
import com.szy.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * <p>
 * 测项信息 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/measuring-item")
public class MeasuringItemController extends BaseController {
    /**
     * 查询指定测项信息信息
     *
     * @param id 测项信息id
     * @return 成功信息，测项信息json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        MeasuringItem measuringItem = measuringItemService.getById(id);
        if (measuringItem == null){
            return Result.fail(ResponseCode.NOT_EXIST, "查询测项信息不存在");
        }
        return Result.ok(measuringItem);
    }

    /**
     * 测项信息列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，测项信息json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result list(@RequestParam(value = "name", required = false) String name,
                       @RequestParam("currentPage") Integer currentPage,
                       @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<MeasuringItem> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            wrapper.eq("name", name);
        }
        Page<MeasuringItem> page = new Page<>(currentPage, pageSize);
        Page<MeasuringItem> measuringItemPage = measuringItemService.page(page, wrapper);
        return Result.ok(measuringItemPage);
    }

    /**
     * 新增测项信息
     *
     * @param measuringItem 测站信息
     * @return 成功
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result save(@RequestBody MeasuringItem measuringItem) {
        measuringItemService.saveMeasuringItem(measuringItem);
        return Result.ok(measuringItem);
    }

    /**
     * 删除测项信息
     *
     * @param id 测项信息id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result delete(@PathVariable Long id) {
        MeasuringItem byId = measuringItemService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除测项信息不存在");
        }
        measuringItemService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新测项信息信息
     *
     * @param measuringItem 测站信息
     * @return 成功
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result update(@RequestBody MeasuringItem measuringItem) {
        MeasuringItem byId = measuringItemService.getById(measuringItem.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新测项信息不存在");
        }
        measuringItemService.updateMeasuringItem(measuringItem);
        return Result.ok(measuringItem);
    }

    /**
     * 从excel中批量导入测项信息
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<MeasuringItem> measuringItems = EasyPoiUtil.importExcel(multipartFile, 0, 1, MeasuringItem.class);
        for (MeasuringItem i : measuringItems) {
            if (i.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            measuringItemService.saveMeasuringItem(i);
        }
        return Result.ok(measuringItems);
    }

    /**
     * 导出全部测项信息
     *
     * @return 全部测项信息
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jczd')")
    public Result exportExcel() {
         List<MeasuringItem> measuringItems = measuringItemService.exportAll();
        return Result.ok(measuringItems);
    }

}
