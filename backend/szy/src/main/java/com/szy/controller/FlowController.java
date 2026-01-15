package com.szy.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.common.vo.FlowVO;
import com.szy.entity.Flow;
import com.szy.entity.VideoConfiguration;
import com.szy.mapper.VideoConfigurationMapper;
import com.szy.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/flow")
@DS("gcdd")
public class FlowController extends BaseController {
    @Autowired
    private VideoConfigurationMapper videoConfigurationMapper;

    /**
     * 根据id查询流量信息
     * @param id
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/16 22:02
     */
    @PreAuthorize("hasAuthority('spjk_lljc')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        Flow flow = flowService.getById(id);
        if (flow == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "查询流量信息不存在");
        }
        return Result.ok(flow);
    }

    /**
     * 根据监测点查询流量信息
     * @param currentPage
     * @param pageSize
     * @param mpCd
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/16 22:06
     */
    @PreAuthorize("hasAuthority('spjk_lljc')")
    @GetMapping("/list")
    public Result getlist(@RequestParam("currentPage") Integer currentPage,
                          @RequestParam("pageSize") Integer pageSize,
                          @RequestParam(value = "mpCd", required = false) String mpCd,
                          @RequestParam(value = "startTime", required = false)
                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                          @RequestParam(value = "endTime", required = false)
                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end){
        QueryWrapper<Flow> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(mpCd)) {
            wrapper.like("MP_CD", mpCd);
        }
        if (start != null && end != null) {
            wrapper.between("TM", start, end); // 使用between代替gt和lt，更明确表示时间范围
        }
        // 添加排序条件，按照监测时间逆序
        wrapper.orderByDesc("TM");

        Page<Flow> page = new Page<>(currentPage, pageSize);
        Page<Flow> flowPage = flowService.page(page, wrapper);
        return Result.ok(flowPage );
    }

    /**
     * 获取最新的流量信息
     * @param project
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/22 16:34
     */
    @PreAuthorize("hasAuthority('spjk_lljc')")
    @GetMapping("/last")
    public Result getlast(@RequestParam(value = "position", required = false) String project){
        QueryWrapper<VideoConfiguration> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(project)) {
            wrapper.eq("town", project);
        }
        List<VideoConfiguration> list = videoConfigurationService.list(wrapper);
        String mpCd = "";
        if (!list.isEmpty()) {
            mpCd = list.get(0).getVideoCode();
        }
        QueryWrapper<Flow> wrapper2 = new QueryWrapper<>();
        if (!StringUtils.isBlank(mpCd)) {
            wrapper2.eq("MP_CD", mpCd);
        }
        // 添加排序条件，按照监测时间逆序
        wrapper2.orderByDesc("TM");
        // 直接使用selectList来获取所有匹配的结果，并取第一个
        List<Flow> flowList = flowService.list(wrapper2);
        if (flowList != null && !flowList.isEmpty()) {
            List<Flow> newFlow = new ArrayList<>();
            newFlow.add(flowList.get(0));
            // 假设Result.ok接受一个对象作为参数，而不是Page对象
            return Result.ok(newFlow); // 返回列表中的第一个元素
        } else {
            // 如果没有找到匹配的水位信息，返回一个空对象
            return Result.ok(flowList);
        }
    }

    /**
     * 删除流量信息
     * @param id
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/16 22:23
     */
    @PreAuthorize("hasAuthority('spjk_lljc')")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        Flow flow = flowService.getById(id);
        if (flow == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除流量信息不存在");
        }
        flowService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 根据监测点和时间对水位信息进行统计
     * @param start
     * @param end
     * @param mpCd
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/16 22:32
     */
    @PreAuthorize("hasAuthority('spjk_lljc')")
    @GetMapping("/statistics")
    public Result getLevelStatistics(@RequestParam(value = "startTime", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") DateTime start,
                                     @RequestParam(value = "endTime", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") DateTime end,
                                     @RequestParam(value = "mpCd", required = false) String mpCd){
        FlowVO flowVO = flowService.getFlowStatistics(start, end, mpCd);
        return Result.ok(flowVO);
    }

    /**
     * 从excel中批量导入水位信息
     * @param multipartFile
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/19 11:18
     */
    @PreAuthorize("hasAuthority('spjk_lljc')")
    @PostMapping("/import-excel")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) throws ParseException {
        List<Flow> flows = EasyPoiUtil.importExcel(multipartFile, 0, 1, Flow.class);
        for (Flow flow : flows) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (flow.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            flowService.getDeterInformation(flow);
        }
        flowService.saveBatch(flows);
        return Result.ok(flows);
    }

    /**
     * 导出到excel文件
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/19 11:25
     */
    @PreAuthorize("hasAuthority('spjk_lljc')")
    @GetMapping("/export-excel")
    public Result exportAll(@RequestParam(value = "startTime", required = false)
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") DateTime start,
                            @RequestParam(value = "endTime", required = false)
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") DateTime end,
                            @RequestParam(value = "mpCd", required = false) String mpCd) {
        List<Flow> flows = flowService.exportAll(start, end, mpCd);
        return Result.ok(flows);
    }

}
