package com.jzsk.backendv2.service.engineering;

import com.jzsk.backendv2.pojo.dto.engineering.MeasuringStationCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringStationPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringStationUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringStationOptionVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringStationVO;

import java.util.List;

/**
 * 监测站点服务接口
 * 职责：定义监测站点业务能力接口
 */
public interface MeasuringStationService {

    /**
     * 分页查询监测站点
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    PageResultVO<MeasuringStationVO> page(MeasuringStationPageQueryDTO queryDTO);

    /**
     * 根据ID查询监测站点详情
     * @param id 监测站点ID
     * @return 监测站点详情
     */
    MeasuringStationVO getById(Long id);

    /**
     * 创建监测站点
     * @param createDTO 创建请求参数
     * @return 创建后的监测站点详情
     */
    MeasuringStationVO create(MeasuringStationCreateDTO createDTO);

    /**
     * 更新监测站点
     * @param updateDTO 更新请求参数
     * @return 更新后的监测站点详情
     */
    MeasuringStationVO update(MeasuringStationUpdateDTO updateDTO);

    /**
     * 删除监测站点
     * @param id 监测站点ID
     */
    void delete(Long id);

    /**
     * 查询所有站点名称（用于下拉选择）
     * @return 站点名称选项列表
     */
    List<MeasuringStationOptionVO> listNames();
}
