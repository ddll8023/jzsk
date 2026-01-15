package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.AdministrationDivision;
import com.szy.util.EasyPoiUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
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
 * 行政区划控制器
 * </p>
 *
 * @author l
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/administration-division")
@Api(tags="行政区域划分")
public class AdministrationDivisionController extends BaseController {
    /**
     * 查询指定行政区划信息
     *
     * @param id 行政区划id
     * @return 成功信息，行政区划json；失败信息，错误提示
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('xtgl_xzqh')")
    public Result info(@PathVariable Long id) {
        AdministrationDivision administrationDivision = administrationDivisionService.getById(id);
        if (administrationDivision == null)
            return Result.fail(ResponseCode.NOT_EXIST, "查询行政区划信息不存在");
        return Result.ok(administrationDivision);
    }

    /**
     * 行政区划列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，行政区划json
     */
    @PreAuthorize("hasAuthority('xtgl_xzqh')")
    @GetMapping("/list")
    public Result list(@RequestParam("currentPage") Integer currentPage,
                       @RequestParam("pageSize") Integer pageSize,
                       @RequestParam(value = "administrative", required = false) String administrative
                       ) {
        QueryWrapper<AdministrationDivision> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(administrative)){
            queryWrapper.like("village_belong",administrative);
        }
        Page<AdministrationDivision> page = new Page<>(currentPage, pageSize);
        Page<AdministrationDivision> administrationDivisionPage = administrationDivisionService.page(page,queryWrapper);
        return Result.ok(administrationDivisionPage);
    }

    /**
     * 新增行政区划
     *
     * @param administrationDivision 行政区划json
     * @return 成功信息，行政区划json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('xtgl_xzqh')")
    public Result save(@Validated @RequestBody AdministrationDivision administrationDivision) {
        administrationDivisionService.save(administrationDivision);
        return Result.ok(administrationDivision);
    }

    /**
     * 删除行政区划
     *
     * @param id 行政区划id数组
     * @return 成功信息
     */
    @PreAuthorize("hasAuthority('xtgl_xzqh')")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        AdministrationDivision byId = administrationDivisionService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除行政区划不存在");
        }
        administrationDivisionService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新行政区划信息
     *
     * @param administrationDivision 行政区划json，要id
     * @return 成功信息
     */
    @PreAuthorize("hasAuthority('xtgl_xzqh')")
    @PostMapping("/update")
    public Result update(@RequestBody AdministrationDivision administrationDivision) {
        AdministrationDivision byId = administrationDivisionService.getById(administrationDivision.getId());
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "待更新行政区划不存在");
        administrationDivisionService.updateById(administrationDivision);
        return Result.ok(administrationDivision);
    }

    /**
     * 从excel中批量导入行政区划
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('xtgl_xzqh')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<AdministrationDivision> administrationDivisions = EasyPoiUtil.importExcel(multipartFile, 0, 1, AdministrationDivision.class);
        for (AdministrationDivision i : administrationDivisions) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (i.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            administrationDivisionService.save(i);
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
//    @PreAuthorize("hasAuthority('xtgl_xzqh')")
//    public Result exportExcel(HttpServletResponse response) throws IOException {
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
//        String fileName = URLEncoder.encode("行政区划信息", "UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//        QueryWrapper<AdministrationDivision> queryWrapper = new QueryWrapper<>();
//        List<AdministrationDivision> administrationDivisions = administrationDivisionService.list(queryWrapper);
//        EasyExcel.write(response.getOutputStream(), AdministrationDivision.class).sheet("行政区划信息").doWrite(administrationDivisions);
//        return Result.ok();
//    }

    /**
     * 导出全部行政区划
     *
     * @return 全部行政区划
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('xtgl_xzqh')")
    public Result exportExcel() {
        return Result.ok();
    }


}
