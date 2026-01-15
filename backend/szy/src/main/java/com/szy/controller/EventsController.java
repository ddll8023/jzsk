package com.szy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Events;
import com.szy.entity.WarningInformation;
import com.szy.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * <p>
 *  事件中心 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@RestController
@RequestMapping("/events")
public class EventsController extends BaseController{
    /**
     * 查询指定事件中心信息
     * @param id 事件中心id
     * @return 成功信息，事件中心json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxj_sjjl')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        Events events = eventsService.getById(id);
        if(events==null)
            return Result.fail(ResponseCode.NOT_EXIST,"查询事件中心信息不存在");
        return Result.ok(events);
    }
    /**
     * 事件中心列表
     * @param currentPage 当前页
     * @param pageSize 每页记录数
     * @return 成功信息，事件中心json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxj_sjjl')")
    public Result list(@RequestParam("currentPage") Integer currentPage,
                       @RequestParam("pageSize") Integer pageSize,
                       @RequestParam("startTime")
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String startTime,
                       @RequestParam("endTime")
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String endTime,
                       @RequestParam("eventstatus") String eventstatus

    ){
        QueryWrapper<Events> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(startTime) && !StringUtils.isBlank(endTime)){
            queryWrapper.gt("time",startTime);
            queryWrapper.lt("time",endTime);
        }
        if (!StringUtils.isBlank(eventstatus)){
            queryWrapper.eq("status",eventstatus);
        }

        Page<Events> page = new Page<>(currentPage,pageSize);
        Page<Events> eventsPage = eventsService.page(page,queryWrapper);
        return Result.ok(eventsPage);
    }

    /**
     * 新增事件中心
     * @param events 事件中心json
     * @return 成功信息，事件中心json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxj_sjjl')")
    public Result save(@Validated @RequestBody Events events){
        Date date = new Date(); // 获取当前系统时间
        events.setTime(date);
        eventsService.save(events);
        return Result.ok(events);
    }

    /**
     * 删除事件中心
     * @param id 事件中心id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxj_sjjl')")
    public Result delete(@PathVariable Long id){
        Events byId = eventsService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除事件中心信息不存在");
        }
        eventsService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新事件中心信息
     * @param events 事件中心json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxj_sjjl')")
    public Result update(@RequestBody Events events){
        Events byId = eventsService.getById(events.getId());
        if(byId==null)
            return Result.fail(ResponseCode.NOT_EXIST,"待更新事件中心信息不存在");
        eventsService.updateById(events);
        return Result.ok(events);
    }

    /**
     * 导出到excel的全部信息
     *
     * @return 预警信息列表
     */

    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    public Result exportAll() {
        List<Events> exportAll = eventsService.exportAll();
        return Result.ok(exportAll);
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
        List<Events> eventsList = EasyPoiUtil.importExcel(multipartFile, 0, 1, Events.class);
        for (Events events : eventsList) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (events.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }

            eventsService.save(events);
        }
        return Result.ok(eventsList);
    }

}
