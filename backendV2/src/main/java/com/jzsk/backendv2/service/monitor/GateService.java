package com.jzsk.backendv2.service.monitor;

import com.jzsk.backendv2.pojo.dto.monitor.GateQueryDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.monitor.GateDataVO;

import java.util.List;

/**
 * 闸门数据服务接口
 * 职责：提供闸门数据查询功能
 */
public interface GateService {

    /**
     * 根据闸门编码分页查询数据
     * @param gateCode 闸门编码（dgq/dzdf/qst/xgq/yhd）
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    PageResultVO<GateDataVO> getGateDataByPage(String gateCode, long page, long size);

    /**
     * 根据闸门编码和时间范围分页查询数据
     * @param queryDTO 查询参数（包含闸门编码、时间范围、分页参数）
     * @return 分页结果
     */
    PageResultVO<GateDataVO> getGateDataByTimeRange(GateQueryDTO queryDTO);

    /**
     * 校验闸门编码是否有效
     * @param gateCode 闸门编码
     * @return 是否有效
     */
    boolean isValidGateCode(String gateCode);
}
