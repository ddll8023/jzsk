package com.jzsk.backendv2.service.system;

import com.jzsk.backendv2.pojo.dto.system.department.DepartmentCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.department.DepartmentPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.department.DepartmentUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.department.DepartmentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 部门服务接口
 * 职责：提供部门管理的业务逻辑
 */
public interface DepartmentService {

    /**
     * 分页查询部门列表
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询部门列表", description = "支持按名称、级别、公司条件筛选分页查询部门列表")
    PageResultVO<DepartmentVO> page(DepartmentPageQueryDTO queryDTO);

    /**
     * 根据ID查询部门详情
     * @param id 部门ID
     * @return 部门详情
     */
    @Operation(summary = "查询部门详情", description = "根据ID查询单个部门详情信息")
    DepartmentVO getById(Long id);

    /**
     * 创建部门
     * @param request 创建请求
     * @return 创建的部门
     */
    @Operation(summary = "创建部门", description = "创建新部门，包含名称、职责等信息")
    DepartmentVO create(DepartmentCreateDTO request);

    /**
     * 更新部门
     * @param request 更新请求
     * @return 更新后的部门
     */
    @Operation(summary = "更新部门", description = "更新指定部门信息，支持部分更新")
    DepartmentVO update(DepartmentUpdateDTO request);

    /**
     * 删除部门
     * @param id 部门ID
     */
    @Operation(summary = "删除部门", description = "逻辑删除指定部门")
    void delete(Long id);
}
