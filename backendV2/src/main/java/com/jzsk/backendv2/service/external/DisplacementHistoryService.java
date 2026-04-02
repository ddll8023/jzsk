package com.jzsk.backendv2.service.external;

import com.jzsk.backendv2.pojo.dto.dam.DisplacementQueryDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.dam.DisplacementHistoryVO;

import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDateTime;

/**
 * 外部位移历史服务接口
 * 职责: 提供外部GNSS位移历史数据的查询功能
 */
public interface DisplacementHistoryService {

    /**
     * 分页查询位移历史数据
     *
     * @param page 页码
     * @param size 每页大小
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询位移历史数据", description = "查询外部GNSS位移历史监测数据")
    PageResultVO<DisplacementHistoryVO> getDisplacementHistoryPage(int page, int size, DisplacementQueryDTO queryDTO);

    /**
     * 分页查询位移历史数据（原始参数版本）
     * Controller 透传参数，由 Service 完成解析和业务处理
     *
     * @param page 页码
     * @param size 每页大小
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param sensor 传感器类型
     * @param stationIds 站点ID列表（逗号分隔）
     * @param projectId 项目ID
     * @param statsFreq 统计频率
     * @return 分页结果
     */
    @Operation(summary = "分页查询位移历史数据", description = "查询外部GNSS位移历史监测数据")
    PageResultVO<DisplacementHistoryVO> getDisplacementHistoryPageRaw(int page, int size,
            LocalDateTime startTime, LocalDateTime endTime,
            String sensor, String stationIds, Integer projectId, Integer statsFreq);

    /**
     * 获取所有测站最新位移数据（一张图专用接口）
     * 优化：并行调用 + size=1，大幅减少响应时间
     *
     * @param stationIds 站点ID列表（逗号分隔）
     * @param sensor 传感器类型
     * @param projectId 项目ID
     * @return 各测站最新数据列表
     */
    @Operation(summary = "获取所有测站最新位移数据", description = "一张图模块专用，获取所有GNSS测站的最新数据")
    java.util.List<DisplacementHistoryVO> getDisplacementLatest(String stationIds, String sensor, Integer projectId);
}
