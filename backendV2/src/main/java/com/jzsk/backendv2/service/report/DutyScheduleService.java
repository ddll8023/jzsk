package com.jzsk.backendv2.service.report;

import com.jzsk.backendv2.pojo.dto.report.DutyScheduleCreateDTO;
import com.jzsk.backendv2.pojo.dto.report.DutySchedulePageQueryDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyScheduleUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.report.DutyScheduleVO;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 值班安排服务接口
 * 职责：提供值班安排的CRUD和列表查询功能
 * 遵循KISS原则：方法简洁，职责单一
 */
public interface DutyScheduleService {

    /**
     * 分页查询值班安排
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    @Operation(summary = "分页查询值班安排", description = "分页查询值班安排列表，支持按日期范围筛选")
    PageResultVO<DutyScheduleVO> page(DutySchedulePageQueryDTO queryDTO);

    /**
     * 根据ID查询值班安排
     * @param id 值班安排ID
     * @return 值班安排VO
     */
    @Operation(summary = "根据ID查询值班安排", description = "查询指定ID的值班安排详情")
    DutyScheduleVO getById(Long id);

    /**
     * 创建值班安排
     * @param request 创建请求
     * @return 值班安排VO
     */
    @Operation(summary = "创建值班安排", description = "创建新的值班安排")
    DutyScheduleVO create(DutyScheduleCreateDTO request);

    /**
     * 更新值班安排
     * @param request 更新请求
     * @return 值班安排VO
     */
    @Operation(summary = "更新值班安排", description = "更新指定ID的值班安排")
    DutyScheduleVO update(DutyScheduleUpdateDTO request);

    /**
     * 删除值班安排
     * @param id 值班安排ID
     */
    @Operation(summary = "删除值班安排", description = "删除指定ID的值班安排")
    void delete(Long id);

    /**
     * 批量删除值班安排
     * @param ids ID列表
     */
    @Operation(summary = "批量删除值班安排", description = "根据ID列表批量删除值班安排")
    void deleteByIds(java.util.List<Long> ids);
}
