package com.szy.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Const;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.config.UploadConfig;
import com.szy.entity.InspectionRecords;
import com.szy.util.AliOSSUtils;
import com.szy.util.EasyPoiUtil;
import com.szy.util.TencentSMSServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.web.servlet.oauth2.login.OAuth2LoginSecurityMarker;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 巡检记录 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@RestController
@RequestMapping("/inspection-records")
public class InspectionRecordsController extends BaseController {
    @Autowired
    private AliOSSUtils aliOSSUtils;
    @Autowired
    private TencentSMSServiceUtil smsService;
    @Autowired
    private UploadConfig uploadConfig;
    /**
     * 查询指定巡检记录信息
     *
     * @param id 巡检记录id
     * @return 成功信息，巡检记录json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('gcxj_xcjl')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        InspectionRecords inspectionRecords = inspectionRecordsService.getById(id);
        if (inspectionRecords == null){
            return Result.fail(ResponseCode.NOT_EXIST, "查询巡检记录信息不存在");
        }
        return Result.ok(inspectionRecords);
    }

    /**
     * 巡检记录列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @param type    巡检类型
     * @return 成功信息，巡检记录json
     */
//    @GetMapping("/list")
//    @PreAuthorize("hasAuthority('gcxj_xcjl')")
//    public Result list(@RequestParam("currentPage") Integer currentPage,
//                       @RequestParam("pageSize") Integer pageSize,
//                       @RequestParam(value = "type", required = false) String type,
//                       @RequestParam(value = "project", required = false) String project) {
//        QueryWrapper<InspectionRecords> queryWrapper = new QueryWrapper<>();
//        if (!StringUtils.isBlank(type)) {
//            queryWrapper.like("type", type);
//        }
//        if (!StringUtils.isBlank(project)) {
//            queryWrapper.like("project", project);
//        }
//        queryWrapper.orderByDesc("date");
//        Page<InspectionRecords> page = new Page<>(currentPage, pageSize);
//        Page<InspectionRecords> inspectionRecordsPage = inspectionRecordsService.page(page, queryWrapper);
//        return Result.ok(inspectionRecordsPage);
//    }

    /**
     * 巡检记录列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，巡检记录json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gcxj_xcjl')")
    public Result listByName(@RequestParam("currentPage") Integer currentPage,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam(value = "person", required = false) String person,
                             @RequestParam(value = "abnormal", required = false) String abnormal,
                             @RequestParam(value = "project", required = false) String project,
                             @RequestParam(value = "solve", required = false) String solve,
                             @RequestParam(value = "startTime", required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                             @RequestParam(value = "endTime", required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
        QueryWrapper<InspectionRecords> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(person)) {
            queryWrapper.like("person", person);
        }
        if (!StringUtils.isBlank(abnormal)) {
            queryWrapper.like("abnormal", abnormal);
        }
        if (!StringUtils.isBlank(project)) {
            queryWrapper.like("project", project);
        }
        if (!StringUtils.isBlank(solve)) {
            queryWrapper.eq("solve", solve);
        }
        if (start != null && end != null) {
            queryWrapper.between("date", start, end); // 使用between代替gt和lt，更明确表示时间范围
        }
//        queryWrapper.like("position", name);
        queryWrapper.orderByDesc("date");
        Page<InspectionRecords> page = new Page<>(currentPage, pageSize);
        Page<InspectionRecords> inspectionRecordsPage = inspectionRecordsService.page(page, queryWrapper);
        return Result.ok(inspectionRecordsPage);
    }

    /**
     * 新增巡检记录
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/07/11 16:27
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('gcxj_xcjl')")
    public Result save(@RequestBody InspectionRecords inspectionRecords){
        if (inspectionRecords.getAbnormal().equals("无异常")) {
            inspectionRecords.setSolve("已处理");
            inspectionRecords.setSituation("一切正常");
        }
        else {
            inspectionRecords.setSolve("未处理");
        }
        inspectionRecordsService.saveInspectionRecords(inspectionRecords);
        return Result.ok(inspectionRecords);
    }

    /**
     * 测试图片上传
     * @param image
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/07/10 22:10
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("image") MultipartFile image) throws IOException {
        String url = aliOSSUtils.upload(image);
        return Result.ok(url);
    }

    @PostMapping("/upload2")
    public Result upload2(@RequestParam("image") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return Result.fail("图片上传失败");
        }
        //file校验重命名（a:1.png  b:1.png）
        String originalFilename = image.getOriginalFilename();//原来的图片名
        String ext = "." + originalFilename.split("\\.")[1];//1.png
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //地址连接
        String fileName = uuid + ext;

//        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
//        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() +
//                "\\src\\main\\resources\\static\\images\\";
//        String pre = "D:\\tencent\\szy_project\\photo\\";
        String pre = uploadConfig.getUploadPath();
        String path = pre + fileName;

        try {
            image.transferTo(new File(path));
            return Result.ok(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.fail("图片上传失败");
    }

    /**
     * 删除巡检记录
     *
     * @param id 巡检记录id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('gcxj_xcjl')")
    public Result delete(@PathVariable Long id) {
        InspectionRecords byId = inspectionRecordsService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除巡检记录不存在");
        }
        inspectionRecordsService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新巡检记录信息
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxj_xcjl')")
    public Result update(@RequestBody InspectionRecords inspectionRecords) {
        InspectionRecords byId = inspectionRecordsService.getById(inspectionRecords.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新巡检记录不存在");
        }
        inspectionRecordsService.updateInspectionRecords(inspectionRecords);
        return Result.ok(inspectionRecords);
    }

    /**
     * 解决巡检问题
     * @return 成功信息
     */
    @PostMapping("/solveRecords")
    @PreAuthorize("hasAuthority('gcxj_xcjl')")
    public Result solveRecords(@RequestBody InspectionRecords inspectionRecords) {
        InspectionRecords byId = inspectionRecordsService.getById(inspectionRecords.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新巡检记录不存在");
        }
        inspectionRecords.setSolve("已处理");
        inspectionRecordsService.updateInspectionRecords(inspectionRecords);
        return Result.ok(inspectionRecords);
    }

    /**
     * 查询所有工程名称
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/07/19 11:00
     */
    @GetMapping("/projects")
    @PreAuthorize("hasAuthority('gcxj_xcjl')")
    public Result getprojects() {
        List<String> projects = inspectionRecordsService.getAllProjects();
        return Result.ok(projects);
    }

    @GetMapping( "/sendSms")
    public Result sendSms(@RequestParam("phoneNumber") String phoneNumber) {
        // 每个字段12个字符以内
        String[] smsContent = new String[2];
        smsContent[0] = new SimpleDateFormat("MM月dd日").format(new Date()) + "统计";
        smsContent[1] = "已有1条";
        Boolean isSendSmsOk = smsService.sendUserNewDataInfo(phoneNumber, smsContent);
        if (isSendSmsOk) {
            return Result.ok("已发送！");
        } else {
            return Result.fail("发送失败，请查看日志！");
        }
    }

    /**
     * 从excel中批量导入巡检记录
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxj_xcjl')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) {
        List<InspectionRecords> inspectionRecords = EasyPoiUtil.importExcel(multipartFile, 0, 1, InspectionRecords.class);
        for (InspectionRecords i : inspectionRecords) {
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
            inspectionRecordsService.save(i);
        }
        return Result.ok();
    }

    /**
     * 导出全部巡检记录
     *
     * @return 全部巡检记录
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('gcxj_xcjl')")
    public Result exportExcel(@RequestParam(value = "name", required = false) String name) {
        name = (name != null) ? name : "";
        List<InspectionRecords> inspectionRecords = inspectionRecordsService.exportAll(name);
        return Result.ok(inspectionRecords);
    }

}
