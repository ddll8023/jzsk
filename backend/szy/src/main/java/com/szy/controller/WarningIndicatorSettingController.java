package com.szy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.WarningIndicatorSetting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 预警指标设定 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@RestController
@RequestMapping("/warning-indicator-setting")
public class WarningIndicatorSettingController extends BaseController {
    /**
     * 查询指定预警指标设定
     *
     * @param id 预警指标设定id
     * @return 成功信息，预警指标设定json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('yjgl_yjsd')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        WarningIndicatorSetting warningIndicatorSetting = warningIndicatorSettingService.getById(id);
        if (warningIndicatorSetting == null){
            return Result.fail(ResponseCode.NOT_EXIST, "查询预警指标设定不存在");
        }
        return Result.ok(warningIndicatorSetting);
    }

    @GetMapping("types")
    @PreAuthorize("hasAuthority('yjgl_yjsd')")
    public Result types() {
        List<String> types = warningIndicatorSettingService.getAllTypes();
        return Result.ok(types);
    }

    /**
     * 预警指标设定列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，预警指标设定json
     */
    @GetMapping("/search-list")
    @PreAuthorize("hasAuthority('yjgl_yjsd')")
    public Result searchList(@RequestParam("currentPage") Integer currentPage,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("type") String type
                             ) {
        QueryWrapper<WarningIndicatorSetting> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(type)){
            wrapper.eq("type",type);
        }
        Page<WarningIndicatorSetting> page = new Page<>(currentPage, pageSize);
        Page<WarningIndicatorSetting> warningIndicatorSettingPage = warningIndicatorSettingService.page(page,wrapper);
        return Result.ok(warningIndicatorSettingPage);
    }

    /**
     * 预警指标设定列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，预警指标设定json
     */
    @GetMapping("/search-position")
    @PreAuthorize("hasAuthority('yjgl_yjsd')")
    public Result searchName(@RequestParam("currentPage") Integer currentPage,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("position") String position
    ) {
        QueryWrapper<WarningIndicatorSetting> wrapper = new QueryWrapper<>();
        if(!StringUtils.isBlank(position)){
            wrapper.like("position",position);
        }
        Page<WarningIndicatorSetting> page = new Page<>(currentPage, pageSize);
        Page<WarningIndicatorSetting> warningIndicatorSettingPage = warningIndicatorSettingService.page(page,wrapper);
        return Result.ok(warningIndicatorSettingPage);
    }

//    /**
//     * 通过不同的工程类型对预警指标分页查询 水库，水厂，泵站...
//     *
//     * @param currentPage 当前页码
//     * @param pageSize    页大小
//     * @param proType     工程类型
//     * @return 成功信息，预警指标查询json
//     */
//    @GetMapping("/search-list-by-project-type")
//    @PreAuthorize("hasAuthority('yjgl_yjsd')")
//    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("projectType") String proType) {
//        QueryWrapper<WarningIndicatorSetting> wrapper = new QueryWrapper<>();
//        wrapper.eq("project_type", proType);
//        Page<WarningIndicatorSetting> page = new Page<>(currentPage, pageSize);
//        Page<WarningIndicatorSetting> warningIndicatorSettingPage = warningIndicatorSettingService.page(page, wrapper);
//        return Result.ok(warningIndicatorSettingPage);
//    }


//    /**
//     * 在不同工程类型下根据监测点分页查询
//     *
//     * @param currentPage 当前页码
//     * @param pageSize    页大小
//     * @param proType     工程类型
//     * @param position    检测点
//     * @return 成功信息，预警指标查询json
//     */
//    @GetMapping("/search-list-by-position")
//    @PreAuthorize("hasAuthority('yjgl_yjsd')")
//    public Result searchByPositionList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("projectType") String proType, @RequestParam("position") String position) {
//        QueryWrapper<WarningIndicatorSetting> wrapper = new QueryWrapper<>();
//        wrapper.eq("position", position).eq("project_type", proType);
//        Page<WarningIndicatorSetting> page = new Page<>(currentPage, pageSize);
//        Page<WarningIndicatorSetting> warningIndicatorSettingPage = warningIndicatorSettingService.page(page, wrapper);
//        return Result.ok(warningIndicatorSettingPage);
//    }
//
//    /**
//     * 在不同工程类型下根据预警指标分页查询
//     *
//     * @param currentPage 当前页码
//     * @param pageSize    页大小
//     * @param proType     工程类型
//     * @param type        预警指标类型
//     * @return 成功信息，预警指标查询json
//     */
//    @GetMapping("/search-list-by-indicator")
//    @PreAuthorize("hasAuthority('yjgl_yjsd')")
//    public Result searchByIndicatorList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("projectType") String proType, @RequestParam("type") String type) {
//        QueryWrapper<WarningIndicatorSetting> wrapper = new QueryWrapper<>();
//        wrapper.eq("type", type).eq("project_type", proType);
//        Page<WarningIndicatorSetting> page = new Page<>(currentPage, pageSize);
//        Page<WarningIndicatorSetting> warningIndicatorSettingPage = warningIndicatorSettingService.page(page, wrapper);
//        return Result.ok(warningIndicatorSettingPage);
//    }

//    /**
//     * 在不同工程类型下根据预警指标和检测点分页查询
//     *
//     * @param currentPage 当前页码
//     * @param pageSize    页大小
//     * @param proType     工程类型
//     * @param type        预警指标类型
//     * @param position    检测点
//     * @return 成功信息，预警指标查询json
//     */
//    @GetMapping("/search-list-by-both")
//    @PreAuthorize("hasAuthority('yjgl_yjsd')")
//    public Result searchByPositionIndicatorList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("projectType") String proType, @RequestParam("type") String type, @RequestParam("position") String position) {
//        QueryWrapper<WarningIndicatorSetting> wrapper = new QueryWrapper<>();
//        wrapper.eq("type", type).eq("project_type", proType).eq("position", position);
//        Page<WarningIndicatorSetting> page = new Page<>(currentPage, pageSize);
//        Page<WarningIndicatorSetting> warningIndicatorSettingPage = warningIndicatorSettingService.page(page, wrapper);
//        return Result.ok(warningIndicatorSettingPage);
//    }

//    /**
//     * 在不同工程类型下根据关键字 name分页查询
//     *
//     * @param currentPage 当前页码
//     * @param pageSize    页大小
//     * @param proType     工程类型
//     * @param key         关键字 name
//     * @return
//     */
//    @GetMapping("/search-list-by-key")
//    @PreAuthorize("hasAuthority('yjgl_yjsd')")
//    public Result searchByPositionIndicatorList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("projectType") String proType, @RequestParam("key") String key) {
//        QueryWrapper<WarningIndicatorSetting> wrapper = new QueryWrapper<>();
//        wrapper.eq("project_type", proType).like("name", key);
//        Page<WarningIndicatorSetting> page = new Page<>(currentPage, pageSize);
//        Page<WarningIndicatorSetting> warningIndicatorSettingPage = warningIndicatorSettingService.page(page, wrapper);
//        return Result.ok(warningIndicatorSettingPage);
//    }

    /**
     * 新增预警指标设定
     *
     * @param warningIndicatorSetting 预警指标设定json
     * @return 成功信息，预警指标设定json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('yjgl_yjsd')")
    public Result save(@Validated @RequestBody WarningIndicatorSetting warningIndicatorSetting) {
        warningIndicatorSettingService.save(warningIndicatorSetting);
        return Result.ok(warningIndicatorSetting);
    }

    /**
     * 删除预警指标设定
     *
     * @param id 预警指标设定id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('yjgl_yjsd')")
    public Result delete(@PathVariable Long id) {
        WarningIndicatorSetting byId = warningIndicatorSettingService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除预警指标设定不存在");
        }

        warningIndicatorSettingService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新预警指标设定
     *
     * @param warningIndicatorSetting 预警指标设定json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('yjgl_yjsd')")
    public Result update(@RequestBody WarningIndicatorSetting warningIndicatorSetting) {
        WarningIndicatorSetting byId = warningIndicatorSettingService.getById(warningIndicatorSetting.getId());
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "待更新预警指标设定不存在");
        warningIndicatorSettingService.updateById(warningIndicatorSetting);
        return Result.ok(warningIndicatorSetting);
    }
}
