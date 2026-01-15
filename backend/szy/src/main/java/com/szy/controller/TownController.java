package com.szy.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Line;
import com.szy.entity.Town;
import com.szy.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/town")
@DS("gcdd")
public class TownController extends BaseController{

    /**
     * 新增村庄
     * @param town
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result saveTown(@RequestBody Town town){
        List<String> names = townService.getAllNames();
        if(names.contains(town.getName())){
            return Result.fail("该村庄已存在");
        }
        townService.create(town);
        return Result.ok();
    }

    /**
     * 根据id删除村庄
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result deleteById(@PathVariable("id") Long id){
        townService.removeById(id);
        return Result.ok();
    }

    /**
     * 修改
     * @param town
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result updateById(@RequestBody Town town){
        townService.updateById(town);
        return Result.ok();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result getById(@PathVariable("id") Long id){
        Town town = townService.getById(id);
        if(town == null){
            return Result.fail("该村庄不存在");
        }
        return Result.ok(town);
    }

    /**
     * 查询
     * @param name
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result queryDict(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "currentPage") Integer currentPage,
                            @RequestParam(value = "pageSize") Integer pageSize){
        QueryWrapper<Town> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            wrapper.like("name", name);
        }
        Page<Town> page = new Page<>(currentPage, pageSize);
        Page<Town> townPage = townService.page(page, wrapper);
        return Result.ok(townPage);
    }

    /**
     * 从excel中批量导入村庄数据
     * @param multipartFile
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/08/11 16:11
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<Town> town = EasyPoiUtil.importExcel(multipartFile, 0, 1, Town.class);
        for (Town i : town) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (i.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            townService.save(i);
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
        List<Town> townList = townService.exportAll();
        return Result.ok(townList);
    }
}
