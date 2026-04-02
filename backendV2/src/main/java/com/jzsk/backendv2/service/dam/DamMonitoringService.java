package com.jzsk.backendv2.service.dam;

import com.jzsk.backendv2.pojo.dto.dam.DamTimeQueryDTO;
import com.jzsk.backendv2.pojo.dto.dam.SeepageQueryDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.dam.*;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

/**
 * 大坝监测服务接口
 * 职责: 提供大坝监测数据的查询功能
 */
public interface DamMonitoringService {

    /**
     * 获取所有监测点列表
     *
     * @return 监测点列表
     */
    @Operation(summary = "获取所有监测点列表", description = "查询所有大坝监测点列表")
    List<DamPointVO> getPoints();

    /**
     * 分页查询渗压数据
     *
     * @param page 页码
     * @param size 每页大小
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询渗压数据", description = "按条件分页查询渗压监测数据")
    PageResultVO<SeepageVO> getSeepagePage(int page, int size, SeepageQueryDTO queryDTO);

    /**
     * 查询水位高程时序数据
     *
     * @param queryDTO 查询条件
     * @return 时序数据列表
     */
    @Operation(summary = "查询水位高程时序数据", description = "查询指定测点的水位高程时序数据")
    List<TimeSeriesVO> getTimeWaterElevation(DamTimeQueryDTO queryDTO);

    /**
     * 查询水位时序数据
     *
     * @param queryDTO 查询条件
     * @return 时序数据列表
     */
    @Operation(summary = "查询水位时序数据", description = "查询指定测点的水位时序数据")
    List<TimeSeriesVO> getTimeWaterLevel(DamTimeQueryDTO queryDTO);

    /**
     * 查询温度时序数据
     *
     * @param queryDTO 查询条件
     * @return 时序数据列表
     */
    @Operation(summary = "查询温度时序数据", description = "查询指定测点的温度时序数据")
    List<TimeSeriesVO> getTimeTemperature(DamTimeQueryDTO queryDTO);

    /**
     * 查询水压时序数据
     *
     * @param queryDTO 查询条件
     * @return 时序数据列表
     */
    @Operation(summary = "查询水压时序数据", description = "查询指定测点的水压时序数据")
    List<TimeSeriesVO> getTimeWaterPressure(DamTimeQueryDTO queryDTO);

    /**
     * 获取各测点最新水位高程
     *
     * @return 最新水位高程列表
     */
    @Operation(summary = "获取各测点最新水位高程", description = "获取所有测点的最新水位高程数据")
    List<LatestWaterElevationVO> getLatestWaterElevation();

    /**
     * 分页查询河道水位数据
     *
     * @param page 页码
     * @param size 每页大小
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询河道水位数据", description = "按条件分页查询河道水位监测数据")
    PageResultVO<RiverWaterLevelVO> getRiverWaterLevelPage(int page, int size, SeepageQueryDTO queryDTO);

    /**
     * 分页查询渗流量数据
     *
     * @param page 页码
     * @param size 每页大小
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询渗流量数据", description = "按条件分页查询渗流量监测数据")
    PageResultVO<SeepageFlowVO> getSeepageFlowPage(int page, int size, SeepageQueryDTO queryDTO);

    /**
     * 获取所有渗压测站最新数据（批量接口）
     * 用于一张图模块一次性获取所有测站数据，减少请求数量
     *
     * @return 所有测点最新渗压数据列表
     */
    @Operation(summary = "获取所有渗压测站最新数据", description = "一次性获取所有渗压测站的最新数据")
    List<SeepageVO> getSeepageLatestAll();
}
