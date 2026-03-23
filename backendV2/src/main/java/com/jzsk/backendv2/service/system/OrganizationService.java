package com.jzsk.backendv2.service.system;

import com.jzsk.backendv2.pojo.dto.system.organization.OrganizationCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.organization.OrganizationPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.organization.OrganizationUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.organization.OrganizationVO;

/**
 * 组织机构服务接口
 * 职责：提供组织机构管理的业务逻辑
 */
public interface OrganizationService {

    /**
     * 分页查询组织机构列表
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResultVO<OrganizationVO> page(OrganizationPageQueryDTO queryDTO);

    /**
     * 根据ID查询组织机构详情
     * @param id 组织机构ID
     * @return 组织机构详情
     */
    OrganizationVO getById(Long id);

    /**
     * 创建组织机构
     * @param request 创建请求
     * @return 创建的组织机构
     */
    OrganizationVO create(OrganizationCreateDTO request);

    /**
     * 更新组织机构
     * @param request 更新请求
     * @return 更新后的组织机构
     */
    OrganizationVO update(OrganizationUpdateDTO request);

    /**
     * 删除组织机构
     * @param id 组织机构ID
     */
    void delete(Long id);
}
