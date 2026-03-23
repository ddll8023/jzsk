package com.jzsk.backendv2.service.warning;

import com.jzsk.backendv2.pojo.dto.warning.WarningFacilityCreateDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningFacilityPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningFacilityUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningFacilityVO;

/**
 * 预警设施服务接口
 * 职责：提供预警设施的CRUD和列表查询功能
 * 遵循KISS原则：方法简洁，职责单一
 */
public interface WarningFacilityService {

    /**
     * 分页查询预警设施列表
     * @param queryDTO 分页查询条件
     * @return 分页结果
     */
    PageResultVO<WarningFacilityVO> page(WarningFacilityPageQueryDTO queryDTO);

    /**
     * 根据ID查询预警设施详情
     * @param id 预警设施ID
     * @return 预警设施VO
     */
    WarningFacilityVO getById(Long id);

    /**
     * 创建预警设施
     * @param request 创建请求
     * @return 预警设施VO
     */
    WarningFacilityVO create(WarningFacilityCreateDTO request);

    /**
     * 更新预警设施
     * @param request 更新请求
     * @return 预警设施VO
     */
    WarningFacilityVO update(WarningFacilityUpdateDTO request);

    /**
     * 删除预警设施（物理删除）
     * @param id 预警设施ID
     */
    void delete(Long id);
}
