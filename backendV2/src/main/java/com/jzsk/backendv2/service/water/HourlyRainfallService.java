package com.jzsk.backendv2.service.water;

import com.jzsk.backendv2.pojo.dto.water.HourlyRainfallPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.water.HourlyRainfallQueryDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.water.HourlyRainfallVO;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

/**
 * 小时雨量服务接口
 * 职责: 提供小时雨量数据的查询功能
 */
public interface HourlyRainfallService {

    /**
     * 查询小时雨量列表
     * @param queryDTO 查询条件
     * @return 小时雨量数据列表
     */
    @Operation(summary = "查询小时雨量列表", description = "根据时间范围查询小时雨量数据")
    List<HourlyRainfallVO> getHourlyRainfallList(HourlyRainfallQueryDTO queryDTO);

    /**
     * 分页查询小时雨量数据
     * @param queryDTO 分页查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询小时雨量数据", description = "按条件分页查询小时雨量数据")
    PageResultVO<HourlyRainfallVO> getHourlyRainfallPage(HourlyRainfallPageQueryDTO queryDTO);
}