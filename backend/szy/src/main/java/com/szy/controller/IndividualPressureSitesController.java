package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.IndividualPressureSites;
import com.szy.util.EasyPoiUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
 *  单独压力站点 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/individual-pressure-sites")
public class IndividualPressureSitesController extends BaseController{
    /**
     * 查询指定单独压力站点信息
     * @param id 单独压力站点id
     * @return 成功信息，单独压力站点json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        IndividualPressureSites individualPressureSites = individualPressureSitesService.getById(id);
        if(individualPressureSites==null)
            return Result.fail(ResponseCode.NOT_EXIST,"查询单独压力站点信息不存在");
        return Result.ok(individualPressureSites);
    }
    /**
     * 通过单独压力站点名查询
     * @param currentPage 当前页码
     * @param pageSize 页大小
     * @param name 单独压力站点
     * @return 成功信息，单独压力站点查询json
     */
    @GetMapping("/search-list-by-name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize,@RequestParam("name") String name){
        QueryWrapper<IndividualPressureSites> wrapper = new QueryWrapper<>();
        wrapper.like("name",name);
        Page<IndividualPressureSites> page = new Page<>(currentPage,pageSize);
        Page<IndividualPressureSites> individualPressureSitesPage = individualPressureSitesService.page(page, wrapper);
        return Result.ok(individualPressureSitesPage);
    }
    /**
     * 单独压力站点列表
     * @param currentPage 当前页
     * @param pageSize 每页记录数
     * @return 成功信息，单独压力站点json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize){
        Page<IndividualPressureSites> page = new Page<>(currentPage,pageSize);
        Page<IndividualPressureSites> individualPressureSitesPage = individualPressureSitesService.page(page);
        return Result.ok(individualPressureSitesPage);
    }

    /**
     * 新增单独压力站点
     * @param individualPressureSites 单独压力站点json
     * @return 成功信息，单独压力站点json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@Validated @RequestBody IndividualPressureSites individualPressureSites){
        individualPressureSitesService.addIndividualPressureSites(individualPressureSites);
        return Result.ok(individualPressureSites);
    }

    /**
     * 删除单独压力站点
     * @param id 单独压力站点id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id){
        IndividualPressureSites byId = individualPressureSitesService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除单独压力站点不存在");
        }
        individualPressureSitesService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新单独压力站点信息
     * @param individualPressureSites 单独压力站点json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody IndividualPressureSites individualPressureSites){
        IndividualPressureSites byId = individualPressureSitesService.getById(individualPressureSites.getId());
        if(byId==null)
            return Result.fail(ResponseCode.NOT_EXIST,"待更新单独压力站点不存在");
        individualPressureSitesService.updateIndividualPressureSites(individualPressureSites);
        return Result.ok(individualPressureSites);
    }

    /**
     * 从excel中批量导入单独压力测站
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @Transactional
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<IndividualPressureSites> individualPressureSites = EasyPoiUtil.importExcel(multipartFile, 0, 2,IndividualPressureSites.class);
        for (IndividualPressureSites i:individualPressureSites) {
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
            individualPressureSitesService.addIndividualPressureSites(i);
        }
        return Result.ok(individualPressureSites);
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
//        String fileName = URLEncoder.encode("单独压力测站", "UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//        QueryWrapper<IndividualPressureSites> queryWrapper = new QueryWrapper<>();
//        List<IndividualPressureSites> individualPressureSites = individualPressureSitesService.list(queryWrapper);
//        EasyExcel.write(response.getOutputStream(), IndividualPressureSites.class).sheet("单独压力测站").doWrite(individualPressureSites);
//        return Result.ok(individualPressureSites);
//    }

    /**
     * 导出全部单独压力测站
     * @return 全部单独压力测站
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<IndividualPressureSites> individualPressureSites = individualPressureSitesService.exportAll();
        return Result.ok(individualPressureSites);
    }

}
