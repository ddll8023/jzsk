package com.szy.controller;

import com.szy.entity.StRiversR;
import com.szy.service.IStRiversRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 河道水情表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@RestController
@RequestMapping("/st-rivers-r")
public class StRiversRController {

    @Autowired
    private IStRiversRService stRiversRService;

    /**
     * 查询所有河道水情数据
     * @return 河道水情数据列表
     */
    @GetMapping("/list")
    public List<StRiversR> list() {
        return stRiversRService.list();
    }
    
    /**
     * 分页查询河道水情数据
     * @param page 页码，从1开始
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/page")
    public Page<StRiversR> listByPage(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return stRiversRService.listByPage(page, size);
    }
}