package com.jzsk.backendv2.service.impl.system;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.system.OrganizationMapper;
import com.jzsk.backendv2.pojo.dto.system.organization.OrganizationCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.organization.OrganizationPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.organization.OrganizationUpdateDTO;
import com.jzsk.backendv2.pojo.entity.system.OrganizationEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.organization.OrganizationVO;
import com.jzsk.backendv2.service.system.OrganizationService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织机构服务实现类
 * 职责：提供组织机构CRUD等业务逻辑
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DS("jcxx")
public class OrganizationServiceImpl implements OrganizationService {

    private static final long MAX_PAGE_SIZE = 500L;

    private final OrganizationMapper organizationMapper;

    @Override
    public PageResultVO<OrganizationVO> page(OrganizationPageQueryDTO queryDTO) {
        OrganizationPageQueryDTO normalized = normalizePageQuery(queryDTO);
        long total = organizationMapper.countPage(normalized);
        if (total <= 0L) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        long offset = (normalized.getPage() - 1L) * normalized.getSize();
        List<OrganizationEntity> entities = organizationMapper.selectPage(normalized, offset, normalized.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        List<OrganizationVO> voList = entities.stream()
                .map(this::toOrganizationVO)
                .collect(Collectors.toList());

        return PageUtils.buildPage(voList, total, normalized.getPage(), normalized.getSize());
    }

    @Override
    public OrganizationVO getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID不能为空");
        }
        OrganizationEntity entity = organizationMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "组织机构不存在");
        }
        return toOrganizationVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrganizationVO create(OrganizationCreateDTO request) {
        log.info("创建组织机构开始，organizationName={}", request.getOrganizationName());
        // 校验组织机构名称唯一性
        ensureOrganizationNameUnique(request.getOrganizationName(), null);
        // 校验组织机构代码唯一性
        ensureOrganizationCodeUnique(request.getOrganizationCode(), null);

        OrganizationEntity entity = new OrganizationEntity();
        entity.setOrganizationName(request.getOrganizationName());
        entity.setOrganizationCode(request.getOrganizationCode());
        entity.setAdministrativeName(request.getAdministrativeName());
        entity.setOrganizationAbbr(request.getOrganizationAbbr());
        entity.setLegalRepresentative(request.getLegalRepresentative());
        entity.setAgencySpecifications(request.getAgencySpecifications());
        entity.setSubordinateRelations(request.getSubordinateRelations());
        entity.setInstitutionalType(request.getInstitutionalType());
        entity.setMainFunction(request.getMainFunction());
        entity.setApproveContent(request.getApproveContent());
        entity.setWebsite(request.getWebsite());
        entity.setEmail(request.getEmail());
        entity.setAddress(request.getAddress());
        entity.setPostalCode(request.getPostalCode());
        entity.setOfficeTelephone(request.getOfficeTelephone());
        entity.setFax(request.getFax());
        entity.setStaffSize(request.getStaffSize());
        entity.setWhetherReform(request.getWhetherReform());

        organizationMapper.insert(entity);
        log.info("创建组织机构成功，organizationId={}, name={}", entity.getId(), entity.getOrganizationName());

        return getById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrganizationVO update(OrganizationUpdateDTO request) {
        log.info("更新组织机构开始，id={}", request.getId());
        OrganizationEntity existing = organizationMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "组织机构不存在");
        }

        // 校验组织机构名称唯一性
        if (StringUtils.hasText(request.getOrganizationName())
                && !request.getOrganizationName().equals(existing.getOrganizationName())) {
            ensureOrganizationNameUnique(request.getOrganizationName(), request.getId());
            existing.setOrganizationName(request.getOrganizationName());
        }

        // 校验组织机构代码唯一性
        if (StringUtils.hasText(request.getOrganizationCode())
                && !request.getOrganizationCode().equals(existing.getOrganizationCode())) {
            ensureOrganizationCodeUnique(request.getOrganizationCode(), request.getId());
            existing.setOrganizationCode(request.getOrganizationCode());
        }

        // 更新字段
        if (request.getAdministrativeName() != null) {
            existing.setAdministrativeName(request.getAdministrativeName());
        }
        if (request.getOrganizationAbbr() != null) {
            existing.setOrganizationAbbr(request.getOrganizationAbbr());
        }
        if (request.getLegalRepresentative() != null) {
            existing.setLegalRepresentative(request.getLegalRepresentative());
        }
        if (request.getAgencySpecifications() != null) {
            existing.setAgencySpecifications(request.getAgencySpecifications());
        }
        if (request.getSubordinateRelations() != null) {
            existing.setSubordinateRelations(request.getSubordinateRelations());
        }
        if (request.getInstitutionalType() != null) {
            existing.setInstitutionalType(request.getInstitutionalType());
        }
        if (request.getMainFunction() != null) {
            existing.setMainFunction(request.getMainFunction());
        }
        if (request.getApproveContent() != null) {
            existing.setApproveContent(request.getApproveContent());
        }
        if (request.getWebsite() != null) {
            existing.setWebsite(request.getWebsite());
        }
        if (request.getEmail() != null) {
            existing.setEmail(request.getEmail());
        }
        if (request.getAddress() != null) {
            existing.setAddress(request.getAddress());
        }
        if (request.getPostalCode() != null) {
            existing.setPostalCode(request.getPostalCode());
        }
        if (request.getOfficeTelephone() != null) {
            existing.setOfficeTelephone(request.getOfficeTelephone());
        }
        if (request.getFax() != null) {
            existing.setFax(request.getFax());
        }
        if (request.getStaffSize() != null) {
            existing.setStaffSize(request.getStaffSize());
        }
        if (request.getWhetherReform() != null) {
            existing.setWhetherReform(request.getWhetherReform());
        }

        organizationMapper.update(existing);
        log.info("更新组织机构成功，organizationId={}, name={}", existing.getId(), existing.getOrganizationName());

        return getById(existing.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("删除组织机构开始，id={}", id);
        OrganizationEntity existing = organizationMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "组织机构不存在");
        }

        organizationMapper.deleteById(id);
        log.info("删除组织机构成功，organizationId={}, name={}", id, existing.getOrganizationName());
    }

    /**
     * 校验组织机构名称唯一性
     */
    private void ensureOrganizationNameUnique(String organizationName, Long excludeId) {
        if (organizationMapper.countByOrganizationName(organizationName, excludeId) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "组织机构名称已存在");
        }
    }

    /**
     * 校验组织机构代码唯一性
     */
    private void ensureOrganizationCodeUnique(String organizationCode, Long excludeId) {
        if (organizationMapper.countByOrganizationCode(organizationCode, excludeId) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "组织机构代码已存在");
        }
    }

    /**
     * 标准化分页查询参数
     */
    private OrganizationPageQueryDTO normalizePageQuery(OrganizationPageQueryDTO queryDTO) {
        OrganizationPageQueryDTO normalized = new OrganizationPageQueryDTO();
        long page = queryDTO == null ? 1L : PageUtils.normalizePage(queryDTO.getPage());
        long size = (queryDTO == null || queryDTO.getSize() < 1L)
                ? 10L : Math.min(queryDTO.getSize(), MAX_PAGE_SIZE);

        normalized.setPage(page);
        normalized.setSize(size);
        if (queryDTO != null) {
            normalized.setName(queryDTO.getName());
            normalized.setAdministrativeName(queryDTO.getAdministrativeName());
        }
        return normalized;
    }

    /**
     * 转换为OrganizationVO
     */
    private OrganizationVO toOrganizationVO(OrganizationEntity entity) {
        OrganizationVO vo = new OrganizationVO();
        vo.setId(entity.getId());
        vo.setOrganizationName(entity.getOrganizationName());
        vo.setOrganizationCode(entity.getOrganizationCode());
        vo.setAdministrativeName(entity.getAdministrativeName());
        vo.setOrganizationAbbr(entity.getOrganizationAbbr());
        vo.setLegalRepresentative(entity.getLegalRepresentative());
        vo.setAgencySpecifications(entity.getAgencySpecifications());
        vo.setSubordinateRelations(entity.getSubordinateRelations());
        vo.setInstitutionalType(entity.getInstitutionalType());
        vo.setMainFunction(entity.getMainFunction());
        vo.setApproveContent(entity.getApproveContent());
        vo.setWebsite(entity.getWebsite());
        vo.setEmail(entity.getEmail());
        vo.setAddress(entity.getAddress());
        vo.setPostalCode(entity.getPostalCode());
        vo.setOfficeTelephone(entity.getOfficeTelephone());
        vo.setFax(entity.getFax());
        vo.setStaffSize(entity.getStaffSize());
        vo.setWhetherReform(entity.getWhetherReform());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }
}
