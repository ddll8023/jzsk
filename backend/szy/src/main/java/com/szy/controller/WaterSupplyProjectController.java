package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.*;
import com.szy.util.EasyPoiUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  供水工程控制器
 * </p>
 *
 * @author l
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/water-supply-project")
public class WaterSupplyProjectController extends BaseController{
    /**
     * 查询指定供水工程信息
     * @param id 供水工程id
     * @return 成功信息，供水工程json；失败信息，错误提示
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result info(@PathVariable Long id){
        WaterSupplyProject waterSupplyProject = waterSupplyProjectService.getById(id);
        if(waterSupplyProject==null)
            return Result.fail(ResponseCode.NOT_EXIST,"查询供水工程信息不存在");
        return Result.ok(waterSupplyProject);
    }
    /**
     * 供水工程列表
     * @param currentPage 当前页
     * @param pageSize 每页记录数
     * @return 成功信息，供水工程json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize){
        Page<WaterSupplyProject> page = new Page<>(currentPage,pageSize);
        Page<WaterSupplyProject> waterSupplyProjectPage = waterSupplyProjectService.page(page);
        return Result.ok(waterSupplyProjectPage);
    }

    /**
     * 通过供水工程名查询
     * @param currentPage 当前页码
     * @param pageSize 页大小
     * @param name 工程名
     * @return 成功信息，供水工程查询json
     */
    @GetMapping("/search-list-by-name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("name") String name){
        QueryWrapper<WaterSupplyProject> wrapper = new QueryWrapper<>();
        wrapper.like("project_name",name);
        Page<WaterSupplyProject> page = new Page<>(currentPage,pageSize);
        Page<WaterSupplyProject> waterSupplyProjectPage = waterSupplyProjectService.page(page, wrapper);
        return Result.ok(waterSupplyProjectPage);
    }

    /**
     * 新增供水工程
     * @param waterSupplyProject 供水工程json
     * @return 成功信息，供水工程json
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody WaterSupplyProject waterSupplyProject){
        waterSupplyProjectService.save(waterSupplyProject);
        return Result.ok(waterSupplyProject);
    }

    /**
     * 删除供水工程
     * @param id 供水工程id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id){
        WaterSupplyProject byId = waterSupplyProjectService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除供水工程不存在");
        }
        waterSupplyProjectService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新供水工程信息
     * @param waterSupplyProject 供水工程json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody WaterSupplyProject waterSupplyProject){
        WaterSupplyProject byId = waterSupplyProjectService.getById(waterSupplyProject.getId());
        if(byId==null)
            return Result.fail(ResponseCode.NOT_EXIST,"待更新供水工程不存在");
        waterSupplyProjectService.updateById(waterSupplyProject);
        return Result.ok(waterSupplyProject);
    }

    /**
     * 从excel中批量导入供水工程
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<WaterSupplyProject> waterSupplyProjects = EasyPoiUtil.importExcel(multipartFile, 0, 1, WaterSupplyProject.class);
        for (WaterSupplyProject i:waterSupplyProjects) {
            if(i.checkForEmptyFields()){
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            waterSupplyProjectService.save(i);
        }
        return Result.ok(waterSupplyProjects);
    }

//    /**
//     * 导出数据到excel
//     * @param response HttpServletResponse response
//     * @return waterSupplyProjects列表
//     * @throws IOException 异常
//     */
//    @GetMapping("/export-excel")
//    @PreAuthorize("hasAuthority('gcxx_jcxx')")
//    public Result exportExcel(HttpServletResponse response) throws IOException {
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
//        String fileName = URLEncoder.encode("供水工程", "UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//        QueryWrapper<WaterSupplyProject> queryWrapper = new QueryWrapper<>();
//        List<WaterSupplyProject> waterSupplyProjects = waterSupplyProjectService.list(queryWrapper);
//        EasyExcel.write(response.getOutputStream(), WaterSupplyProject.class).sheet("供水工程").doWrite(waterSupplyProjects);
//        return Result.ok(waterSupplyProjects);
//    }

    /**
     * 导出全部供水工程
     * @return 全部供水工程
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<WaterSupplyProject> waterSupplyProjects = waterSupplyProjectService.exportAll();
        return Result.ok(waterSupplyProjects);
    }

}
