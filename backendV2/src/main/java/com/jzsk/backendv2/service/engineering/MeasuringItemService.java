package com.jzsk.backendv2.service.engineering;

import com.jzsk.backendv2.pojo.dto.engineering.MeasuringItemCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringItemPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringItemUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ExcelExportData;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringItemOptionVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringItemVO;

import java.util.List;

/**
 * 测项服务接口
 * 职责：定义测项的业务能力接口
 */
public interface MeasuringItemService {

    /**
     * 分页查询测项
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    PageResultVO<MeasuringItemVO> page(MeasuringItemPageQueryDTO queryDTO);

    /**
     * 根据ID查询测项详情
     * @param id 测项ID
     * @return 测项视图对象
     */
    MeasuringItemVO getById(Long id);

    /**
     * 创建测项
     * @param createDTO 创建请求参数
     * @return 测项视图对象
     */
    MeasuringItemVO create(MeasuringItemCreateDTO createDTO);

    /**
     * 更新测项
     * @param updateDTO 更新请求参数
     * @return 测项视图对象
     */
    MeasuringItemVO update(MeasuringItemUpdateDTO updateDTO);

    /**
     * 删除测项
     * @param id 测项ID
     */
    void delete(Long id);

    /**
     * 查询所有测项名称
     * @return 测项下拉选项列表
     */
    List<MeasuringItemOptionVO> listNames();

    /**
     * 生成测项列表导出数据
     * @return Excel导出数据
     */
    ExcelExportData generateExportData();
}
