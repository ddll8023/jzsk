package com.jzsk.backendv2.service.warning;

import com.jzsk.backendv2.pojo.dto.warning.WarningIndicatorCreateDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningIndicatorPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningIndicatorUpdateDTO;
import com.jzsk.backendv2.pojo.entity.warning.WarningIndicatorEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningIndicatorOptionsVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningIndicatorVO;

import java.util.List;

/**
 * 预警指标服务接口
 * 职责：提供预警指标CRUD的业务逻辑
 */
public interface WarningIndicatorService {

    /**
     * 分页查询预警指标列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResultVO<WarningIndicatorVO> page(WarningIndicatorPageQueryDTO queryDTO);

    /**
     * 根据ID查询预警指标
     *
     * @param id 预警指标ID
     * @return 预警指标详情
     */
    WarningIndicatorVO getById(Long id);

    /**
     * 查询预警指标页面选项
     *
     * @return 页面选项
     */
    WarningIndicatorOptionsVO getOptions();

    /**
     * 获取所有监测类型列表
     *
     * @return 类型列表
     */
    List<String> listAllTypes();

    /**
     * 创建预警指标
     *
     * @param dto 创建请求
     * @return 创建后的预警指标
     */
    WarningIndicatorVO create(WarningIndicatorCreateDTO dto);

    /**
     * 更新预警指标
     *
     * @param dto 更新请求
     * @return 更新后的预警指标
     */
    WarningIndicatorVO update(WarningIndicatorUpdateDTO dto);

    /**
     * 删除预警指标（物理删除）
     *
     * @param id 预警指标ID
     */
    void delete(Long id);

    /**
     * 查询所有预警指标（用于自动预警任务）
     *
     * @return 预警指标列表
     */
    List<WarningIndicatorEntity> listAll();

    /**
     * 根据测点和监测类型查询预警指标
     *
     * @param position 测点名称
     * @param type     监测类型
     * @return 预警指标，若不存在则返回null
     */
    WarningIndicatorEntity getByPositionAndType(String position, String type);
}
