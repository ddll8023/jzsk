package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.WaterDistributor;
import com.szy.util.EasyPoiUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 分水口控制器
 * </p>
 *
 * @author l
 * @since 2022-01-22
 */
@RestController
@RequestMapping("/water-distributor")
public class WaterDistributorController extends BaseController {
    /**
     * 查询指定分水口信息
     *
     * @param id 分水口id
     * @return 成功信息，分水口json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        WaterDistributor waterDistributor = waterDistributorService.getById(id);
        if (waterDistributor == null)
            return Result.fail(ResponseCode.NOT_EXIST, "查询分水口信息不存在");
        return Result.ok(waterDistributor);
    }

    /**
     * 通过分水口名查询
     *
     * @param currentPage 当前页码
     * @param pageSize    页大小
     * @param name        分水口
     * @return 成功信息，分水口查询json
     */
    @GetMapping("/search-list-by-name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<WaterDistributor> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        Page<WaterDistributor> page = new Page<>(currentPage, pageSize);
        Page<WaterDistributor> waterDistributorPage = waterDistributorService.page(page, wrapper);
        return Result.ok(waterDistributorPage);
    }

    /**
     * 分水口列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，分水口json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<WaterDistributor> page = new Page<>(currentPage, pageSize);
        Page<WaterDistributor> waterDistributorPage = waterDistributorService.page(page);
        return Result.ok(waterDistributorPage);
    }

    /**
     * 新增分水口
     *
     * @param waterDistributor 分水口json
     * @return 成功信息，分水口json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@Validated @RequestBody WaterDistributor waterDistributor) {
        waterDistributorService.addWaterDistributor(waterDistributor);
        return Result.ok(waterDistributor);
    }

    /**
     * 删除分水口
     *
     * @param id 分水口id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        WaterDistributor byId = waterDistributorService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除分水口不存在");
        }
        waterDistributorService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新分水口信息
     *
     * @param waterDistributor 分水口json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody WaterDistributor waterDistributor) {
        WaterDistributor byId = waterDistributorService.getById(waterDistributor.getId());
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "待更新分水口不存在");
        waterDistributorService.updateWaterDistributor(waterDistributor);
        return Result.ok(waterDistributor);
    }

    /**
     * 从excel中批量导入分水口
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<WaterDistributor> waterDistributors = EasyPoiUtil.importExcel(multipartFile, 0, 1, WaterDistributor.class);
        for (WaterDistributor i : waterDistributors) {
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
            waterDistributorService.addWaterDistributor(i);
        }
        return Result.ok(waterDistributors);
    }

//    /**
//     * 导出数据到excel
//     * @param response HttpServletResponse response
//     * @return waterDistributors列表
//     * @throws IOException 异常
//     */
//    @GetMapping("/export-excel")
//    @PreAuthorize("hasAuthority('gcxx_jcxx')")
//    public Result exportExcel(HttpServletResponse response) throws IOException {
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
//        String fileName = URLEncoder.encode("分水口", "UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//        QueryWrapper<WaterDistributor> queryWrapper = new QueryWrapper<>();
//        List<WaterDistributor> waterDistributors = waterDistributorService.list(queryWrapper);
//        EasyExcel.write(response.getOutputStream(), WaterDistributor.class).sheet("分水口").doWrite(waterDistributors);
//        return Result.ok(waterDistributors);
//    }


    /**
     * 导出全部分水口信息
     *
     * @return 全部分水口信息
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<WaterDistributor> waterDistributors = waterDistributorService.exportAll();
        return Result.ok(waterDistributors);
    }
}
