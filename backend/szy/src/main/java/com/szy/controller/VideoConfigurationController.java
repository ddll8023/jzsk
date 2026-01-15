package com.szy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.common.vo.TownVillageVO;
import com.szy.common.vo.VideoCheckVO;
import com.szy.entity.VideoConfiguration;
import com.szy.util.FileNameUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 视频配置 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@RestController
@RequestMapping("/video-configuration")
public class VideoConfigurationController extends BaseController {
    @Autowired
    private FileNameUtil fileNameUtil;
    /**
     * 查询指定视频配置
     *
     * @param id 视频配置id
     * @return 成功信息，视频配置json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('spjk')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        VideoConfiguration videoConfiguration = videoConfigurationService.getById(id);
        if (videoConfiguration == null)
            return Result.fail(ResponseCode.NOT_EXIST, "查询视频配置不存在");
        return Result.ok(videoConfiguration);
    }

    /**
     * 视频配置列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，视频配置json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('spjk')")
    public Result list(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("town") String town,
            @RequestParam("village") String village
    ) {
        QueryWrapper<VideoConfiguration> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(town)) {
            wrapper.eq("town", town);
        }
        if (!StringUtils.isBlank(village)) {
            wrapper.eq("village", village);
        }
        Page<VideoConfiguration> page = new Page<>(currentPage, pageSize);
        Page<VideoConfiguration> videoConfigurationPage = videoConfigurationService.page(page, wrapper);
        return Result.ok(videoConfigurationPage);
    }

    /**
     * 根据设备类型分页查询监控设备
     *
     * @param currentPage 当前页码
     * @param pageSize    单页大小
     * @param type        设备类型
     * @return 成功信息，视频配置json
     */
    @GetMapping("/type-name-list")
    @PreAuthorize("hasAuthority('spjk')")
    public Result typeList(@RequestParam("currentPage") Integer currentPage,
                           @RequestParam("pageSize") Integer pageSize,
                           @RequestParam(value = "type", required = false) String type,
                           @RequestParam(value = "name", required = false) String name

    ) {
        QueryWrapper<VideoConfiguration> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            wrapper.eq("name", name);
        }
        if (!StringUtils.isBlank(type)) {
            wrapper.eq("type", type);
        }
        Page<VideoConfiguration> page = new Page<>(currentPage, pageSize);
        Page<VideoConfiguration> videoConfigurationPage = videoConfigurationService.page(page, wrapper);
        return Result.ok(videoConfigurationPage);
    }

    /***
     * 根据名字分页查询监控设备
     * @param currentPage 当前页码
     * @param pageSize 单页大小
     * @param name 名字
     * @return 成功信息
     */
    @GetMapping("/name-list")
    @PreAuthorize("hasAuthority('spjk')")
    public Result nameList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<VideoConfiguration> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        Page<VideoConfiguration> page = new Page<>(currentPage, pageSize);
        Page<VideoConfiguration> videoConfigurationPage = videoConfigurationService.page(page, wrapper);
        return Result.ok(videoConfigurationPage);
    }

    /**
     * 新增视频配置
     *
     * @param videoConfiguration 视频配置json
     * @return 成功信息，视频配置json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('spjk')")
    public Result save(@RequestBody VideoConfiguration videoConfiguration) {
        videoConfigurationService.save(videoConfiguration);
        return Result.ok(videoConfiguration);
    }

    /**
     * 删除视频配置
     *
     * @param id 视频配置id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('spjk')")
    public Result delete(@PathVariable Long id) {
        VideoConfiguration byId = videoConfigurationService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除视频配置不存在");
        }
        videoConfigurationService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新视频配置信息
     *
     * @param videoConfiguration 视频配置json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('spjk')")
    public Result update(@RequestBody VideoConfiguration videoConfiguration) {
        VideoConfiguration byId = videoConfigurationService.getById(videoConfiguration.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新视频配置不存在");
        }
        videoConfigurationService.updateById(videoConfiguration);
        return Result.ok(videoConfiguration);
    }

    /**
     * 获取监控设备的类型
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/07/06 20:52
     */
    @PreAuthorize("hasAuthority('spjk')")
    @GetMapping("/getalltypes")
    public Result getAllTypes(){
        List<String> types = videoConfigurationService.getAllTypes();
        return Result.ok(types);
    }

    /**
     * 获取所有镇子和村子
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/07/06 21:28
     */
    @PreAuthorize("hasAuthority('spjk')")
    @GetMapping("/towns-villages")
    public Result getTownVillage(){
        List<TownVillageVO> townVillageList = new ArrayList<>();
        List<String> towns = videoConfigurationService.getAllTowns();
        for (String town : towns) {
            List<String> villages = videoConfigurationService.getVillagesByTown(town);
            TownVillageVO townVillage = new TownVillageVO(town, villages);
            townVillageList.add(townVillage);
        }
        return Result.ok(townVillageList);
    }

    @GetMapping("/tree")
    public Result getCameraTree() {
        List<Map<String, Object>> treeList = videoConfigurationService.getTree();
        return Result.ok(treeList);
    }

    /**
     * 把对应的目录下文件名返回
     * @return
     */
    @GetMapping("/photos")
    public Result getPhotoByDateCode(@RequestParam("code") String code,
                                     @RequestParam("queryDate")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date queryDate) {
        List<String> imageList = fileNameUtil.getPhotos(code, queryDate);
        return Result.ok(imageList);
    }

//    @GetMapping("/getCodes")
//    public Result getCodes() {
//        Map<String, String> map = videoConfigurationService.getAllNamesAndCodes();
//        return Result.ok(map);
//    }

    @GetMapping("/getNames")
    public Result getNames() {
        List<String> nameList = videoConfigurationService.getAllNames();
        return Result.ok(nameList);
    }
}
