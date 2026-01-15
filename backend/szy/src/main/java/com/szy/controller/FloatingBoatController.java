package com.szy.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.FloatingBoat;
import com.szy.util.EasyPoiUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/floatingboat")
@DS("gcdd")
public class FloatingBoatController extends BaseController{
    /**
     * 查询指定浮船信息
     *
     * @param id 浮船id
     * @return 成功信息，浮船json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        FloatingBoat floatingBoat = floatingBoatService.getById(id);
        if (floatingBoat == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "查询浮船信息不存在");
        }
        return Result.ok(floatingBoat);
    }

    /**
     * 通过浮船名查询
     *
     * @param currentPage 当前页码
     * @param pageSize    页大小
     * @param name        浮船
     * @return 成功信息，浮船查询json
     */
    @GetMapping("/name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<FloatingBoat> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        Page<FloatingBoat> page = new Page<>(currentPage, pageSize);
        Page<FloatingBoat> floatingBoatPage = floatingBoatService.page(page, wrapper);
        return Result.ok(floatingBoatPage);
    }

    /**
     * 浮船列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，浮船json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<FloatingBoat> page = new Page<>(currentPage, pageSize);
        Page<FloatingBoat> floatingBoatPage = floatingBoatService.page(page);
        return Result.ok(floatingBoatPage);
    }

    /**
     * 新增浮船
     *
     * @param floatingBoat 浮船json
     * @return 成功信息，浮船json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@RequestBody FloatingBoat floatingBoat) {
        floatingBoatService.save(floatingBoat);
        return Result.ok(floatingBoat);
    }

    /**
     * 删除浮船
     *
     * @param id 浮船id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        FloatingBoat byId = floatingBoatService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除浮船不存在");
        }
        floatingBoatService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新浮船信息
     *
     * @param floatingBoat 浮船json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody FloatingBoat floatingBoat) {
        FloatingBoat byId = floatingBoatService.getById(floatingBoat.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新浮船不存在");
        }
        floatingBoatService.updateById(floatingBoat);
        return Result.ok(floatingBoat);
    }

    /**
     * 从excel中批量导入浮船
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<FloatingBoat> floatingBoat = EasyPoiUtil.importExcel(multipartFile, 0, 1, FloatingBoat.class);
        for (FloatingBoat i : floatingBoat) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (i.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            floatingBoatService.save(i);
        }
        return Result.ok(floatingBoat);
    }

    /**
     * 导出全部浮船信息
     *
     * @return 全部浮船信息
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportExcel() {
        List<FloatingBoat> floatingBoats = floatingBoatService.exportAll();
        return Result.ok(floatingBoats);
    }
}
