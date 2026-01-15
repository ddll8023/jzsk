package com.szy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.PumpStation;
import com.szy.util.EasyPoiUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p>
 * 泵站（工程站点）控制器
 * </p>
 *
 * @author l
 * @since 2022-01-20
 */
@RestController
@RequestMapping("/pump-station")
public class PumpStationController extends BaseController {
    /**
     * 查询指定泵站信息
     *
     * @param id 泵站id
     * @return 成功信息，泵站json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        PumpStation pumpStation = pumpStationService.getById(id);
        if (pumpStation == null){
            return Result.fail(ResponseCode.NOT_EXIST, "查询泵站信息不存在");
        }
        return Result.ok(pumpStation);
    }

    /**
     * 通过泵站名查询
     *
     * @param currentPage 当前页码
     * @param pageSize    页大小
     * @param name        泵站
     * @return 成功信息，泵站查询json
     */
    @GetMapping("/name")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result searchByProjectList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name) {
        QueryWrapper<PumpStation> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        Page<PumpStation> page = new Page<>(currentPage, pageSize);
        Page<PumpStation> pumpStationPage = pumpStationService.page(page, wrapper);
        return Result.ok(pumpStationPage);
    }

    /**
     * 泵站列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，泵站json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Page<PumpStation> page = new Page<>(currentPage, pageSize);
        Page<PumpStation> pumpStationPage = pumpStationService.page(page);
        return Result.ok(pumpStationPage);
    }

    /**
     * 新增泵站
     *
     * @param pumpStation 泵站json
     * @return 成功信息，泵站json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@Validated @RequestBody PumpStation pumpStation) {
        pumpStationService.save(pumpStation);
        return Result.ok(pumpStation);
    }

    /**
     * 删除泵站
     *
     * @param id 泵站id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result delete(@PathVariable Long id) {
        PumpStation byId = pumpStationService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除泵站不存在");
        }
        pumpStationService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新泵站信息
     *
     * @param pumpStation 泵站json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody PumpStation pumpStation) {
        PumpStation byId = pumpStationService.getById(pumpStation.getId());
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "待更新泵站不存在");
        }
        pumpStationService.updateById(pumpStation);
        return Result.ok(pumpStation);
    }

    /**
     * 从excel中批量导入泵站
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) throws ParseException {
        List<PumpStation> pumpStations = EasyPoiUtil.importExcel(multipartFile, 0, 1,PumpStation.class);
        for (PumpStation pumpStation:pumpStations) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if(pumpStation.checkForEmptyFields()){
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            if(pumpStation.getLongitude().compareTo(new BigDecimal("180")) >= 0 ||
                    pumpStation.getLongitude().compareTo(new BigDecimal("-180")) <= 0){
                return Result.fail(ResponseCode.FAIL, "经度输入有误，请重新输入");
            }
            if(pumpStation.getLatitude().compareTo(new BigDecimal("90")) >= 0 ||
                    pumpStation.getLatitude().compareTo(new BigDecimal("-90")) <= 0){
                return Result.fail(ResponseCode.FAIL, "纬度输入有误，请重新输入");
            }
            pumpStationService.addPumpStation(pumpStation);
        }
        return Result.ok(pumpStations);
    }

    /**
     * 导出到excel的全部信息
     *
     * @return 泵站列表
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportAll() {
        List<PumpStation> pumpStations = pumpStationService.exportAll();
        return Result.ok(pumpStations);
    }

}
