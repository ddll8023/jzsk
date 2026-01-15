package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.GroundSourceWater;
import com.szy.util.EasyPoiUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 地下水源水 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/ground-source-water")
public class GroundSourceWaterController extends BaseController {
    /**
     * 查询指定地下水源水信息
     *
     * @param id 地下水源水id
     * @return 成功信息，地下水源水json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        GroundSourceWater groundSourceWater = groundSourceWaterService.getById(id);
        if (groundSourceWater == null)
            return Result.fail(ResponseCode.NOT_EXIST, "查询地下水源水信息不存在");
        return Result.ok(groundSourceWater);
    }

    /**
     * 通过地下水源水名查询
     *
     * @param currentPage 当前页码
     * @param pageSize    页大小
     * @param name        地下水源水
     * @return 成功信息，地下水源水查询json
     */
    @GetMapping("/search-list-by-name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<GroundSourceWater> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        Page<GroundSourceWater> page = new Page<>(currentPage, pageSize);
        Page<GroundSourceWater> groundSourceWaterPage = groundSourceWaterService.page(page, wrapper);
        return Result.ok(groundSourceWaterPage);
    }

    /**
     * 地下水源水列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，地下水源水json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<GroundSourceWater> page = new Page<>(currentPage, pageSize);
        Page<GroundSourceWater> groundSourceWaterPage = groundSourceWaterService.page(page);
        return Result.ok(groundSourceWaterPage);
    }

    /**
     * 新增地下水源水
     *
     * @param groundSourceWater 地下水源水json
     * @return 成功信息，地下水源水json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@Validated @RequestBody GroundSourceWater groundSourceWater) {
        groundSourceWaterService.addGroundSourceWater(groundSourceWater);
        return Result.ok(groundSourceWater);

    }

    /**
     * 删除地下水源水
     *
     * @param id 地下水源水id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        GroundSourceWater byId = groundSourceWaterService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除地下水源水不存在");
        }
        groundSourceWaterService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新地下水源水信息
     *
     * @param groundSourceWater 地下水源水json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody GroundSourceWater groundSourceWater) {
        GroundSourceWater byId = groundSourceWaterService.getById(groundSourceWater.getId());
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "待更新地下水源水不存在");
        groundSourceWaterService.updateGroundSourceWater(groundSourceWater);
        return Result.ok(groundSourceWater);
    }

    /**
     * 从excel中批量导入地下水源水
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<GroundSourceWater> groundSourceWaters = EasyPoiUtil.importExcel(multipartFile, 0, 1, GroundSourceWater.class);
        for (GroundSourceWater i : groundSourceWaters) {
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
            groundSourceWaterService.addGroundSourceWater(i);
        }
        return Result.ok();
    }

//    /**
//     * 导出数据到excel
//     * @param response HttpServletResponse response
//     * @return 成功信息
//     * @throws IOException 异常
//     */
//    @GetMapping("/export-excel")
//    @PreAuthorize("hasAuthority('gcxx_jcxx')")
//    public Result exportExcel(HttpServletResponse response) throws IOException {
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
//        String fileName = URLEncoder.encode("地下水源水", "UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//        QueryWrapper<GroundSourceWater> queryWrapper = new QueryWrapper<>();
//        List<GroundSourceWater> groundSourceWaters = groundSourceWaterService.list(queryWrapper);
//        EasyExcel.write(response.getOutputStream(), GroundSourceWater.class).sheet("地表水源水信息").doWrite(groundSourceWaters);
//        return Result.ok();
//    }

    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('xtgl_xzqh')")
    public Result exportExcel() {
        List<GroundSourceWater> groundSourceWaters = groundSourceWaterService.exportAll();
        return Result.ok(groundSourceWaters);
    }

}

