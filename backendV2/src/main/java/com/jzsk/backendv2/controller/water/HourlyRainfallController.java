package com.jzsk.backendv2.controller.water;

import com.jzsk.backendv2.pojo.dto.water.HourlyRainfallPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.water.HourlyRainfallQueryDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.water.HourlyRainfallVO;
import com.jzsk.backendv2.service.water.HourlyRainfallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 小时雨量控制器
 * 用途: 提供小时雨量数据的查询接口
 * 遵循KISS原则: 简单清晰的接口设计
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/hourly-rainfalls")
@Tag(name = "小时雨量管理", description = "提供小时雨量数据的查询功能")
@RequiredArgsConstructor
public class HourlyRainfallController {

    private final HourlyRainfallService hourlyRainfallService;

    /**
     * 查询小时雨量列表
     * 权限: 需要登录
     */
    @Operation(summary = "查询小时雨量列表", description = "根据时间范围查询小时雨量数据列表")
    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<HourlyRainfallVO>>> getHourlyRainfallList(
        @Parameter(description = "查询条件")
        @Valid HourlyRainfallQueryDTO queryDTO) {

        List<HourlyRainfallVO> list = hourlyRainfallService.getHourlyRainfallList(queryDTO);
        return ResponseEntity.ok(ApiResult.success(list, "查询成功"));
    }

    /**
     * 分页查询小时雨量数据
     * 权限: 需要登录
     */
    @Operation(summary = "分页查询小时雨量数据", description = "按条件分页查询小时雨量数据")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<HourlyRainfallVO>>> getHourlyRainfallPage(
        @Parameter(description = "分页查询条件")
        @Valid HourlyRainfallPageQueryDTO queryDTO) {

        PageResultVO<HourlyRainfallVO> result = hourlyRainfallService.getHourlyRainfallPage(queryDTO);
        return ResponseEntity.ok(ApiResult.success(result, "查询成功"));
    }
}