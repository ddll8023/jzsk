package com.jzsk.backendv2.service.warning;

import com.jzsk.backendv2.pojo.dto.warning.WarningPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningUpdateDTO;
import com.jzsk.backendv2.pojo.entity.warning.WarningInformationEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningVO;

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
    PageResultVO<WarningVO> page(WarningPageQueryDTO queryDTO);

    /**
     * 更新预警信息（主要用于解除预警操作）
     *
     * @param dto 更新请求
     * @return 更新后的预警信息
     */
    WarningVO update(WarningUpdateDTO dto);

    /**
     * 删除预警信息（物理删除）
     *
     * @param id 预警信息ID
     */
    void delete(Long id);

    /**
     * 创建预警信息（用于自动预警任务落库）
     *
     * @param entity 预警信息实体
     * @return 生成ID后的预警信息实体
     */
    WarningInformationEntity create(WarningInformationEntity entity);
}
