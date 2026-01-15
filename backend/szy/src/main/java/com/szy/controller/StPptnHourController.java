package com.szy.controller;

import com.szy.entity.StPptnHour;
import com.szy.service.IStPptnHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 逐小时降雨量数据表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@RestController
@RequestMapping("/st-pptn-hour")
public class StPptnHourController {

    @Autowired
    private IStPptnHourService stPptnHourService;

    /**
     * 查询所有逐小时降雨量数据
     * @return 降雨量数据列表
     */
    @GetMapping("/list")
    public List<StPptnHour> list() {
        return stPptnHourService.list();
    }
}
