package com.szy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Pump;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/pump")
public class PumpController extends BaseController {
    /**
     * 查询指定泵站信息
     *
     * @param id 泵站id
     * @return 成功信息，泵站json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        Pump pump = pumpService.getById(id);
        if (pump == null){
            return Result.fail(ResponseCode.NOT_EXIST, "查询泵站信息不存在");
        }
        return Result.ok(pump);
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
        QueryWrapper<Pump> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        Page<Pump> page = new Page<>(currentPage, pageSize);
        Page<Pump> pumpPage = pumpService.page(page, wrapper);
        return Result.ok(pumpPage);
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
        Page<Pump> page = new Page<>(currentPage, pageSize);
        Page<Pump> pumpPage = pumpService.page(page);
        return Result.ok(pumpPage);
    }

    /**
     * 新增泵站
     *
     * @param pump 泵站json
     * @return 成功信息，泵站json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result save(@RequestBody Pump pump) {
        pumpService.save(pump);
        return Result.ok(pump);
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
        Pump byId = pumpService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除泵站不存在");
        }
        pumpService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新泵站信息
     *
     * @param pump 泵站json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody Pump pump) {
        Pump byId = pumpService.getById(pump.getId());
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "待更新泵不存在");
        }
        pumpService.updateById(pump);
        return Result.ok(pump);
    }

    /**
     * 导出到excel的全部信息
     *
     * @return 泵站列表
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result exportAll() {
        List<Pump> pumps = pumpService.exportAll();
        return Result.ok(pumps);
    }

}
