package com.szy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Line;
import com.szy.entity.icon;
import com.szy.mapper.IconMapper;
import com.szy.service.IconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.szy.common.lang.Const.PROJECT_VIDEO_URL;

@RestController
@RequestMapping("/icon")
public class IconController extends BaseController{
    @Autowired
    private IconMapper iconMapper;

    @PostMapping("/upload/video")
    public Result uploadVideo(@RequestParam("video") MultipartFile file) {
        // 确保保存路径存在
//        File folder = new File(PROJECT_VIDEO_URL);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
        // 保存文件到服务器
        String filePath = PROJECT_VIDEO_URL + file.getOriginalFilename();
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 发生错误时返回null
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 更新icon表中的name为“工程简介”的记录的img字段
        int updateCount = iconMapper.updateImgByName(fileName);
        if (updateCount <= 0) {
            // 更新失败处理逻辑
            return null;
        }
        // 返回新视频的文件名
        return Result.ok(fileName);
    }

    @GetMapping("/getByName")
    public Result getByName() {
        LambdaQueryWrapper<icon> queryWrapper = new LambdaQueryWrapper<icon>();
        queryWrapper.eq(icon::getName, "工程简介");
        List<icon> iconList = iconMapper.selectList(queryWrapper);
        return Result.ok(iconList.get(0));
    }

    @PostMapping("/updateByName")
    public Result updateByName(@RequestParam("content") String content) {
        // 创建更新条件
        UpdateWrapper<icon> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", "工程简介").set("text", content);

        // 执行更新操作
        int updateCount = iconMapper.update(null, updateWrapper);

        // 检查更新是否成功
        if (updateCount > 0) {
            return Result.ok("更新成功");
        } else {
            return Result.fail("更新失败");
        }
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        icon icon1 = iconService.getById(id);
        if (icon1 == null){
            return Result.fail(ResponseCode.NOT_EXIST, "工程简介文字不存在");
        }
        return Result.ok(icon1);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result update(@RequestBody icon icon2) {
        icon byId = iconService.getById(icon2.getId());
        if (byId == null){
            return Result.fail(ResponseCode.NOT_EXIST, "待更新工程简介文字不存在");
        }
        iconService.updateById(icon2);
        return Result.ok(icon2);
    }
}
