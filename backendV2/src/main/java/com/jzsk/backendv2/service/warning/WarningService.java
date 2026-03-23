package com.jzsk.backendv2.service.warning;

import com.jzsk.backendv2.pojo.dto.warning.WarningPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningVO;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 预警信息服务接口
 * 职责：提供预警信息CRUD的业务逻辑
 */
public interface WarningService {

    /**
     * 分页查询预警信息列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询预警信息", description = "支持按地点、状态、等级、类型、时间范围筛选，支持分页")
    PageResultVO<WarningVO> page(WarningPageQueryDTO queryDTO);

    /**
     * 更新预警信息（主要用于解除预警操作）
     *
     * @param dto 更新请求
     * @return 更新后的预警信息
     */
    @Operation(summary = "更新预警信息", description = "更新预警信息，用于解除预警等操作，若传入overTime则自动计算stayTime")
    WarningVO update(WarningUpdateDTO dto);

    /**
     * 删除预警信息（物理删除）
     *
     * @param id 预警信息ID
     */
    @Operation(summary = "删除预警信息", description = "根据ID物理删除预警信息，删除后不可恢复")
    void delete(Long id);
}
