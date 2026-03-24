package com.jzsk.backendv2.service.report;

import com.jzsk.backendv2.pojo.dto.report.DutyLogCreateDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyLogPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.report.DutyLogUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.report.DutyLogVO;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

/**
 * 值班日志服务接口
 * 职责：提供值班日志的CRUD和列表查询功能
 * 遵循KISS原则：方法简洁，职责单一
 */
public interface DutyLogService {

    /**
     * 分页查询值班日志
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    @Operation(summary = "分页查询值班日志", description = "分页查询值班日志列表，支持按日期范围筛选")
    PageResultVO<DutyLogVO> page(DutyLogPageQueryDTO queryDTO);

    /**
     * 根据ID查询值班日志
     * @param id 值班日志ID
     * @return 值班日志VO
     */
    @Operation(summary = "根据ID查询值班日志", description = "查询指定ID的值班日志详情")
    DutyLogVO getById(Long id);

    /**
     * 创建值班日志
     * @param request 创建请求
     * @return 值班日志VO
     */
    @Operation(summary = "创建值班日志", description = "创建新的值班日志")
    DutyLogVO create(DutyLogCreateDTO request);

    /**
     * 更新值班日志
     * @param request 更新请求
     * @return 值班日志VO
     */
    @Operation(summary = "更新值班日志", description = "更新指定ID的值班日志")
    DutyLogVO update(DutyLogUpdateDTO request);

    /**
     * 删除值班日志
     * @param id 值班日志ID
     */
    @Operation(summary = "删除值班日志", description = "删除指定ID的值班日志")
    void delete(Long id);

    /**
     * 批量删除值班日志
     * @param ids ID列表
     */
    @Operation(summary = "批量删除值班日志", description = "根据ID列表批量删除值班日志")
    void deleteByIds(List<Long> ids);
}
