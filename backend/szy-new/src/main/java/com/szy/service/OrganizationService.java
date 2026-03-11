package com.szy.service;

import com.github.pagehelper.PageInfo;
import com.szy.pojo.dto.OrganizationDTO;
import com.szy.pojo.dto.OrganizationQueryDTO;
import com.szy.pojo.vo.OrganizationVO;

/**
 * 组织机构服务接口
 */
public interface OrganizationService {

    /**
     * 分页查询组织机构列表
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    PageInfo<OrganizationVO> list(OrganizationQueryDTO queryDTO);

    /**
     * 根据ID查询组织机构详情
     *
     * @param id 组织机构ID
     * @return 组织机构详情
     */
    OrganizationVO getInfo(Long id);

    /**
     * 新增组织机构
     *
     * @param dto 组织机构信息
     */
    void save(OrganizationDTO dto);

    /**
     * 更新组织机构
     *
     * @param dto 组织机构信息
     */
    void update(OrganizationDTO dto);

    /**
     * 删除组织机构
     *
     * @param id 组织机构ID
     */
    void delete(Long id);
}