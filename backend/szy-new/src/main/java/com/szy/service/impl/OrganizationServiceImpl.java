package com.szy.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.szy.common.exception.BusinessException;
import com.szy.mapper.OrganizationMapper;
import com.szy.pojo.dto.OrganizationDTO;
import com.szy.pojo.dto.OrganizationQueryDTO;
import com.szy.pojo.entity.Organization;
import com.szy.pojo.vo.OrganizationVO;
import com.szy.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组织机构服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationMapper organizationMapper;

    @Override
    public PageInfo<OrganizationVO> list(OrganizationQueryDTO queryDTO) {
        log.info("分页查询组织机构列表，参数：{}", queryDTO);

        // 设置分页默认值
        int currentPage = queryDTO.getCurrentPage() == null ? 1 : queryDTO.getCurrentPage();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();

        PageHelper.startPage(currentPage, pageSize);
        List<OrganizationVO> list = organizationMapper.selectList(queryDTO.getName());
        return new PageInfo<>(list);
    }

    @Override
    public OrganizationVO getInfo(Long id) {
        log.info("查询组织机构详情，ID：{}", id);

        OrganizationVO organization = organizationMapper.selectById(id);
        if (organization == null) {
            throw new BusinessException("组织机构不存在");
        }

        return organization;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(OrganizationDTO dto) {
        log.info("新增组织机构，参数：{}", dto);

        Organization organization = new Organization();
        organization.setOrganizationName(dto.getOrganizationName());
        organization.setOrganizationCode(dto.getOrganizationCode());
        organization.setAdministrativeName(dto.getAdministrativeName());
        organization.setOrganizationAbbr(dto.getOrganizationAbbr());
        organization.setLegalRepresentative(dto.getLegalRepresentative());
        organization.setAgencySpecifications(dto.getAgencySpecifications());
        organization.setSubordinateRelations(dto.getSubordinateRelations());
        organization.setInstitutionalType(dto.getInstitutionalType());
        organization.setMainFunction(dto.getMainFunction());
        organization.setApproveContent(dto.getApproveContent());
        organization.setWebsite(dto.getWebsite());
        organization.setEmail(dto.getEmail());
        organization.setAddress(dto.getAddress());
        organization.setPostalCode(dto.getPostalCode());
        organization.setOfficeTelephone(dto.getOfficeTelephone());
        organization.setFax(dto.getFax());
        organization.setStaffSize(dto.getStaffSize());
        organization.setWhetherReform(dto.getWhetherReform());

        int result = organizationMapper.insert(organization);
        if (result <= 0) {
            throw new BusinessException("新增组织机构失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrganizationDTO dto) {
        log.info("更新组织机构，参数：{}", dto);

        if (dto.getId() == null) {
            throw new BusinessException("组织机构ID不能为空");
        }

        Organization organization = new Organization();
        organization.setId(dto.getId());
        organization.setOrganizationName(dto.getOrganizationName());
        organization.setOrganizationCode(dto.getOrganizationCode());
        organization.setAdministrativeName(dto.getAdministrativeName());
        organization.setOrganizationAbbr(dto.getOrganizationAbbr());
        organization.setLegalRepresentative(dto.getLegalRepresentative());
        organization.setAgencySpecifications(dto.getAgencySpecifications());
        organization.setSubordinateRelations(dto.getSubordinateRelations());
        organization.setInstitutionalType(dto.getInstitutionalType());
        organization.setMainFunction(dto.getMainFunction());
        organization.setApproveContent(dto.getApproveContent());
        organization.setWebsite(dto.getWebsite());
        organization.setEmail(dto.getEmail());
        organization.setAddress(dto.getAddress());
        organization.setPostalCode(dto.getPostalCode());
        organization.setOfficeTelephone(dto.getOfficeTelephone());
        organization.setFax(dto.getFax());
        organization.setStaffSize(dto.getStaffSize());
        organization.setWhetherReform(dto.getWhetherReform());

        int result = organizationMapper.update(organization);
        if (result <= 0) {
            throw new BusinessException("更新组织机构失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("删除组织机构，ID：{}", id);

        int result = organizationMapper.deleteById(id);
        if (result <= 0) {
            throw new BusinessException("删除组织机构失败");
        }
    }
}