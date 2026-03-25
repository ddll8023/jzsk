package com.jzsk.backendv2.service.water;

import com.jzsk.backendv2.pojo.dto.water.WaterLevelQueryDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.water.WaterLevelVO;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

/**
 * 水位服务接口
 * 职责: 提供水位数据的查询功能
 */
public interface WaterLevelService {

    /**
     * 分页查询水位数据
     * @param page 页码
     * @param size 每页大小
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询水位数据", description = "按条件分页查询水位数据")
    PageResultVO<WaterLevelVO> getWaterLevelPage(int page, int size, WaterLevelQueryDTO queryDTO);

    /**
     * 查询水位数据列表
     * @param queryDTO 查询条件
     * @return 水位数据列表
     */
    @Operation(summary = "查询水位数据列表", description = "根据测站编码查询水位数据列表")
    List<WaterLevelVO> getWaterLevelList(WaterLevelQueryDTO queryDTO);
}