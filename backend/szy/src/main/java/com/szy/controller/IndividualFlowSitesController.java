package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Impoundment;
import com.szy.entity.IndividualFlowSites;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 单独流量测站 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-22
 */
@RestController
@RequestMapping("/individual-flow-sites")
public class IndividualFlowSitesController extends BaseController {
    /**
     * 查询指定单独流量测站信息
     *
     * @param id 单独流量测站id
     * @return 成功信息，单独流量测站json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        IndividualFlowSites individualFlowSites = individualFlowSitesService.getById(id);
        if (individualFlowSites == null)
            return Result.fail(ResponseCode.NOT_EXIST, "查询单独流量测站信息不存在");
        return Result.ok(individualFlowSites);
    }

    /**
     * 通过单独流量站点名查询
     *
     * @param currentPage 当前页码
     * @param pageSize    页大小
     * @param name        单独流量站点
     * @return 成功信息，单独流量站点查询json
     */
    @GetMapping("/search-list-by-name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<IndividualFlowSites> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        Page<IndividualFlowSites> page = new Page<>(currentPage, pageSize);
        Page<IndividualFlowSites> individualFlowSitesPage = individualFlowSitesService.page(page, wrapper);
        return Result.ok(individualFlowSitesPage);
    }

    /**
     * 单独流量测站列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，单独流量测站json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<IndividualFlowSites> page = new Page<>(currentPage, pageSize);
        Page<IndividualFlowSites> individualFlowSitesPage = individualFlowSitesService.page(page);
        return Result.ok(individualFlowSitesPage);
    }

    /**
     * 新增单独流量测站
     *
     * @param individualFlowSites 单独流量测站json
     * @return 成功信息，单独流量测站json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@Validated @RequestBody IndividualFlowSites individualFlowSites) {
        individualFlowSitesService.addIndividualFlowSites(individualFlowSites);
        return Result.ok(individualFlowSites);
    }

    /**
     * 删除单独流量测站
     *
     * @param id 单独流量测站id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        IndividualFlowSites byId = individualFlowSitesService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除单独流量测站不存在");
        }
        individualFlowSitesService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新单独流量测站信息
     *
     * @param individualFlowSites 单独流量测站json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody IndividualFlowSites individualFlowSites) {
        IndividualFlowSites byId = individualFlowSitesService.getById(individualFlowSites.getId());
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "待更新单独流量测站不存在");
        individualFlowSitesService.updateIndividualFlowSites(individualFlowSites);
        return Result.ok(individualFlowSites);
    }

    /**
     * 从excel中批量导入单独流量测站
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<IndividualFlowSites> individualFlowSites = EasyPoiUtil.importExcel(multipartFile, 0, 2, IndividualFlowSites.class);

        for (IndividualFlowSites i : individualFlowSites) {
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
            individualFlowSitesService.addIndividualFlowSites(i);
        }
        return Result.ok(individualFlowSites);
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
//        String fileName = URLEncoder.encode("单独流量测站", "UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//        QueryWrapper<IndividualFlowSites> queryWrapper = new QueryWrapper<>();
//        List<IndividualFlowSites> individualFlowSites = individualFlowSitesService.list(queryWrapper);
//        EasyExcel.write(response.getOutputStream(), IndividualFlowSites.class).sheet("单独流量测站").doWrite(individualFlowSites);
//        return Result.ok(individualFlowSites);
//    }

    /**
     * 导出全部单独流量测站
     *
     * @return 全部单独流量测站
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<IndividualFlowSites> individualFlowSites = individualFlowSitesService.exportAll();
        return Result.ok(individualFlowSites);
    }
}
