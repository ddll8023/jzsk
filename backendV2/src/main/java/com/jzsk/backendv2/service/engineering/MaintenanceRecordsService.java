package com.jzsk.backendv2.service.engineering;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.pojo.dto.engineering.MaintenanceRecordsCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MaintenanceRecordsPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MaintenanceRecordsUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.MaintenanceRecordsExcelVO;
import com.jzsk.backendv2.pojo.vo.engineering.MaintenanceRecordsVO;

import java.util.List;

/**
 * 养护记录服务接口
 * 职责：定义养护记录的业务能力边界
 */
public interface MaintenanceRecordsService {

    /**
     * 分页查询养护记录
     * @param queryDTO 查询条件（工程名称、时间范围）
     * @return 分页结果
     */
    PageResultVO<MaintenanceRecordsVO> page(MaintenanceRecordsPageQueryDTO queryDTO);

    /**
     * 根据ID查询养护记录详情
     * @param id 养护记录ID
     * @return 养护记录详情
     * @throws BusinessException 养护记录不存在时抛出
     */
    MaintenanceRecordsVO getById(Long id);

    /**
     * 创建养护记录
     * @param request 创建请求参数
     * @return 创建后的养护记录详情
     */
    MaintenanceRecordsVO create(MaintenanceRecordsCreateDTO request);

    /**
     * 更新养护记录
     * @param request 更新请求参数
     * @return 更新后的养护记录详情
     * @throws BusinessException 养护记录不存在时抛出
     */
    MaintenanceRecordsVO update(MaintenanceRecordsUpdateDTO request);

    /**
     * 删除养护记录
     * @param id 养护记录ID
     * @throws BusinessException 养护记录不存在时抛出
     */
    void delete(Long id);

    /**
     * 查询全部养护记录用于导出
     * @return 全部养护记录列表
     */
    List<MaintenanceRecordsExcelVO> listForExport();
}
