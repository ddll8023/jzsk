package com.szy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.MaintenceRecord;
import com.szy.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/maintence-records")
public class MtenceRecordController extends BaseController{
    /**
     * 根据id查询工程记录
     * @param id
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/10 20:47
     */
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        MaintenceRecord maintenceRecord = maintenceRecordService.getById(id);
        if (maintenceRecord == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "查询工程记录不存在");
        }
        return Result.ok(maintenceRecord);
    }

    /**
     * 根据工程名称（可选）分页查询工程记录
     * @param currentPage
     * @param pageSize
     * @param name
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/11 10:17
     */
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    @GetMapping("/list")
    public Result list(@RequestParam("currentPage") Integer currentPage,
                       @RequestParam("pageSize") Integer pageSize,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "startTime", required = false)
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                       @RequestParam(value = "overTime", required = false)
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end){
        QueryWrapper<MaintenceRecord> wrapper = new QueryWrapper<>();
        if(!StringUtils.isBlank(name)){
            wrapper.like("name",name);
        }
        if (start != null && end != null) {
            wrapper.between("start_time", start, end); // 使用between代替gt和lt，更明确表示时间范围
        }
        // 添加排序条件，按照监测时间逆序
        wrapper.orderByDesc("start_time");
        Page<MaintenceRecord> page = new Page<>(currentPage, pageSize);
        Page<MaintenceRecord> maintenceRecordPage = maintenceRecordService.page(page, wrapper);
        return Result.ok(maintenceRecordPage);
    }

    /**
     * 新增工程记录
     * @param maintenceRecord
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/11 10:39
     */
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    @PostMapping("/save")
    public Result save(@RequestBody MaintenceRecord maintenceRecord) {
        maintenceRecordService.save(maintenceRecord);
        return Result.ok("新增工程记录成功");
    }

    /**
     * 根据id删除工程记录
     * @param id
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/11 10:42
     */
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    @PostMapping ("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        MaintenceRecord maintenceRecord = maintenceRecordService.getById(id);
        if (maintenceRecord == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "待删除记录不存在");
        }
        maintenceRecordService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新工程记录信息
     * @param maintenceRecord
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/11 10:59
     */
    @PreAuthorize("hasAuthority('yjgl_yjxx')")
    @PostMapping("/update")
    public Result update(@RequestBody MaintenceRecord maintenceRecord) {
        MaintenceRecord mR = maintenceRecordService.getById(maintenceRecord.getId());
        if (mR == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新工程记录不存在");
        }
        maintenceRecordService.updateById(maintenceRecord);
        return Result.ok("更新成功");
    }

    /**
     * 从excel中导入工程信息
     * @param multipartFile
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/06/11 11:40
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @PostMapping("/import-excel")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<MaintenceRecord> records = EasyPoiUtil.importExcel(multipartFile, 0, 1, MaintenceRecord.class);
        for (MaintenceRecord i : records) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (i.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }
            maintenceRecordService.save(i);
        }
        return Result.ok(records);
    }

    /**
     * 导出全部水库信息
     *
     * @return 全部水库信息
     */
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    @GetMapping("/export-excel")
    public Result exportExcel() {
        List<MaintenceRecord> records = maintenceRecordService.exportAll();
        return Result.ok(records);
    }
}
