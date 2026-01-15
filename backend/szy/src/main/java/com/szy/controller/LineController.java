package com.szy.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.common.vo.CoordinateVO;
import com.szy.common.vo.TransformedLineVO;
import com.szy.entity.Line;
import com.szy.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 巡检记录 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@RestController
@RequestMapping("/line")
public class LineController extends BaseController {
    /**
     * 根据id查询管道信息
     * @param id
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/08/11 15:54
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        Line line = lineService.getById(id);
        if (line == null){
            return Result.fail(ResponseCode.NOT_EXIST, "查询管道不存在");
        }
        return Result.ok(line);
    }

    /**
     * 查询管道信息列表
     * @param currentPage
     * @param pageSize
     * @param name
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/08/11 15:58
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage,
                       @RequestParam("pageSize") Integer pageSize,
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "name", required = false) String name) {
        QueryWrapper<Line> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isBlank(type)) {
            queryWrapper.like("type", type);
        }
        Page<Line> page = new Page<>(currentPage, pageSize);
        Page<Line> linePage = lineService.page(page, queryWrapper);
        return Result.ok(linePage);
    }


    /**
     * 新增管道数据
     * @param line
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/08/11 16:02
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@RequestBody Line line){
        if (lineService.existsByName(line.getName())) {
            return Result.fail("管道名称已存在");
        }
        lineService.save(line);
        return Result.ok(line);
    }


    /**
     * 删除管道数据
     * @param id
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/08/11 16:09
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        Line byId = lineService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "待删除管道信息不存在");
        }
        lineService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新管道
     * @param line
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/08/11 16:10
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody Line line) {
        Line byId = lineService.getById(line.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新管道数据不存在");
        }
        lineService.updateById(line);
        return Result.ok(line);
    }

    /**
     * 从excel中批量导入管道数据
     * @param multipartFile
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/08/11 16:11
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<Line> line = EasyPoiUtil.importExcel(multipartFile, 0, 1, Line.class);
        for (Line i : line) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (i.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            lineService.save(i);
        }
        return Result.ok();
    }

    /**
     * 导出全部
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/08/11 16:11
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<Line> lineList = lineService.exportAll();
        return Result.ok(lineList);
    }

    @GetMapping("/deal-with-list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result dealWithList() {
        List<Line> lineList = lineService.exportAll();
        for (Line line : lineList) {
            // 解析 points 字段为使用冒号分隔 key 和 value 的字符串列表
            List<String> points = line.parsePoints();
            // 将列表转换为字符串并重新设置到 line 对象中
            line.setPoints(points.toString());  // 这里返回的是符合要求的字符串
        }
        return Result.ok(lineList);
    }

}
