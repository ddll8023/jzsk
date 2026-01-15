package com.szy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Const;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.WarningInformation;
import com.szy.mapper.WarningInformationMapper;
import com.szy.util.EasyPoiUtil;
import com.szy.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 预警信息 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@RestController
@RequestMapping("/warning-information")
public class WarningInformationController extends BaseController {
    @Autowired
    private WarningInformationMapper warningInformationMapper;
    /**
     * 根据id查询指定预警信息
     *
     * @param id 预警信息
     * @return 成功信息，预警信息json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        WarningInformation warningInformation = warningInformationService.getById(id);
        if (warningInformation == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "查询预警信息不存在");
        }
        return Result.ok(warningInformation);
    }

    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    @GetMapping("/position")
    public Result getByPosition(@RequestParam("currentPage") Integer currentPage,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam(value = "position") String position) {
        QueryWrapper<WarningInformation> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(position)) {
            wrapper.like("position", position);
        }
        Page<WarningInformation> page = new Page<>(currentPage, pageSize);
        Page<WarningInformation> warningInformationPage = warningInformationService.page(page, wrapper);
        return Result.ok(warningInformationPage);
    }

    /**
     * 根据预警状态、等级和事件来查询预警列表
     *
     * @param status      预警状态
     * @param currentPage 当前页
     * @param pageSize    单页大小
     * @param level       预警等级
     * @param type        预警类型
     * @param start       预警开始时间
     * @param end         预警结束时间
     * @return 成功信息
     */
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    @GetMapping("/list")
    public Result listByStatusLevelDate(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "level", required = false) String level,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "position", required = false) String position,
            @RequestParam(value = "startTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
            @RequestParam(value = "endTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end
    ) {
        QueryWrapper<WarningInformation> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(status)) {
            wrapper.eq("status", status);
        }
        if (!StringUtils.isBlank(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isBlank(type)) {
            wrapper.eq("type", type);
        }
        if (!StringUtils.isBlank(position)) {
            wrapper.like("position", position);
        }
        if (start != null && end != null) {
            wrapper.between("start_time", start, end); // 使用between代替gt和lt，更明确表示时间范围
        }
        // 添加排序条件，按照监测时间逆序
        wrapper.orderByDesc("start_time");
        Page<WarningInformation> page = new Page<>(currentPage, pageSize);
        Page<WarningInformation> warningInformationPage = warningInformationService.page(page, wrapper);
        return Result.ok(warningInformationPage);
    }

    @GetMapping("/listNoPage")
    public Result list(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "level", required = false) String level,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "position", required = false) String position,
            @RequestParam(value = "startTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
            @RequestParam(value = "endTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end
    ) {
        LambdaQueryWrapper<WarningInformation> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(status)) {
            queryWrapper.eq(WarningInformation::getStatus, status);
        }
        if (!StringUtils.isBlank(level)) {
            queryWrapper.eq(WarningInformation::getLevel, level);
        }
        if (!StringUtils.isBlank(type)) {
            queryWrapper.eq(WarningInformation::getType, type);
        }
        if (!StringUtils.isBlank(position)) {
            queryWrapper.eq(WarningInformation::getPosition, position);
        }
        if (start != null && end != null) {
            queryWrapper.between(WarningInformation::getStartTime, start, end); // 使用between代替gt和lt，更明确表示时间范围
        }
        // 添加排序条件，按照监测时间逆序
        queryWrapper.orderByDesc(WarningInformation::getStartTime);
        List<WarningInformation> warningInformations = warningInformationMapper.selectList(queryWrapper);
        return Result.ok(warningInformations);
    }

    /**
     * 新增预警信息
     * @param warningInformation
     * @return com.szy.common.lang.Result
     * @author lzq
     * @date 2024/06/03 15:46
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
//    @Validated
    public Result save(@RequestBody WarningInformation warningInformation) {
        if (warningInformation.getOverTime() != null) {
            String stayTime = TimeUtils.calStayTime(warningInformation);
            warningInformation.setStayTime(stayTime);
        }
        warningInformationService.addWarningInformation(warningInformation);
        return Result.ok("新增预警信息成功");
    }

    /**
     * 删除预警信息
     *
     * @param id 预警信息id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    public Result delete(@PathVariable Long id) {
        WarningInformation byId = warningInformationService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除预警信息不存在");
        }
        warningInformationService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新预警信息
     * @param warningInformation  预警信息
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/03 15:57
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    public Result update(@RequestBody WarningInformation warningInformation) {
        WarningInformation byId = warningInformationService.getById(warningInformation.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新预警信息不存在");
        }
        Date dateNow = new Date();
        warningInformation.setOverTime(dateNow);
        warningInformation.setStatus(Const.LIFTED);
        if (warningInformation.getOverTime() != null) {
            String stayTime = TimeUtils.calStayTime(warningInformation);
            warningInformation.setStayTime(stayTime);
        }
        warningInformationService.updateWarningInformation(warningInformation);
        return Result.ok(warningInformation);
    }

    /**
     * 导出到excel的全部信息
     *
     * @return 预警信息列表
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    public Result exportAll() {
        List<WarningInformation> warningInformations = warningInformationService.exportAll();
        return Result.ok(warningInformations);
    }

    /**
     * 从excel中批量导入预警信息
     * @param multipartFile
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/19 11:17
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) throws ParseException {
        List<WarningInformation> warningInformations = EasyPoiUtil.importExcel(multipartFile, 0, 1, WarningInformation.class);
        for (WarningInformation warningInformation : warningInformations) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (warningInformation.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            if (warningInformation.getLongitude().compareTo(new BigDecimal("180")) >= 0 ||
                    warningInformation.getLongitude().compareTo(new BigDecimal("-180")) <= 0) {
                return Result.fail(ResponseCode.FAIL, "经度输入有误，请重新输入");
            }
            if (warningInformation.getLatitude().compareTo(new BigDecimal("90")) >= 0 ||
                    warningInformation.getLatitude().compareTo(new BigDecimal("-90")) <= 0) {
                return Result.fail(ResponseCode.FAIL, "纬度输入有误，请重新输入");
            }
            warningInformationService.addWarningInformation(warningInformation);
        }
        return Result.ok(warningInformations);
    }

    /**
     * 获取所有的监测对象
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/18 20:10
     */
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    @GetMapping("/getalltypes")
    public Result getAllTypes(){
        List<String> types = warningInformationService.getAllTypes();
        return Result.ok(types);
    }

    /**
     * 获取每种预警的数量
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/19 22:35
     */
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    @GetMapping("/getlevels")
    public Result getWarningLevels(@RequestParam(value = "status", required = false) String status,
                                   @RequestParam(value = "startTime", required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                                   @RequestParam(value = "endTime", required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end){
        List<Integer> levels = warningInformationService.getWarningLevels(start,end,status);
        return Result.ok(levels);
    }
    @GetMapping("/getByPositionTime")
    public Result getByPositionTime(@RequestParam(value = "position", required = false) String position,
                                   @RequestParam(value = "startTime", required = false) String start){
        LambdaQueryWrapper<WarningInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WarningInformation::getPosition, position).eq(WarningInformation::getStartTime, start);

        List<WarningInformation> warningInformations = warningInformationMapper.selectList(queryWrapper);
        return Result.ok(warningInformations);
    }
}
