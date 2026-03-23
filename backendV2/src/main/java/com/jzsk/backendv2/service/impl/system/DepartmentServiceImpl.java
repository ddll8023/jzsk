package com.jzsk.backendv2.service.impl.system;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.system.DepartmentMapper;
import com.jzsk.backendv2.pojo.dto.system.department.DepartmentCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.department.DepartmentPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.department.DepartmentUpdateDTO;
import com.jzsk.backendv2.pojo.entity.system.DepartmentEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.department.DepartmentVO;
import com.jzsk.backendv2.service.system.DepartmentService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 * 职责：提供部门CRUD等业务逻辑
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DS("jcxx")
public class DepartmentServiceImpl implements DepartmentService {

    private static final long MAX_PAGE_SIZE = 500L;

    private final DepartmentMapper departmentMapper;

    @Override
    public PageResultVO<DepartmentVO> page(DepartmentPageQueryDTO queryDTO) {
        DepartmentPageQueryDTO normalized = normalizePageQuery(queryDTO);
        long total = departmentMapper.countPage(normalized);
        if (total <= 0L) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        long offset = (normalized.getPage() - 1L) * normalized.getSize();
        List<DepartmentEntity> entities = departmentMapper.selectPage(normalized, offset, normalized.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        List<DepartmentVO> voList = entities.stream()
                .map(this::toDepartmentVO)
                .collect(Collectors.toList());

        return PageUtils.buildPage(voList, total, normalized.getPage(), normalized.getSize());
    }

    @Override
    public DepartmentVO getById(Long id) {
        DepartmentEntity entity = departmentMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "部门不存在");
        }
        return toDepartmentVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DepartmentVO create(DepartmentCreateDTO request) {
        // 校验部门名称唯一性
        ensureDepartmentNameUnique(request.getDepartmentName(), null);

        DepartmentEntity entity = new DepartmentEntity();
        entity.setDepartmentName(request.getDepartmentName());
        entity.setDepartmentResponsibility(request.getDepartmentResponsibility());
        entity.setLevel(request.getLevel());
        entity.setCompany(request.getCompany());

        departmentMapper.insert(entity);
        log.info("创建部门成功，departmentId={}, name={}", entity.getId(), entity.getDepartmentName());

        return getById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DepartmentVO update(DepartmentUpdateDTO request) {
        DepartmentEntity existing = departmentMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "部门不存在");
        }

        // 校验部门名称唯一性
        if (StringUtils.hasText(request.getDepartmentName()) && !request.getDepartmentName().equals(existing.getDepartmentName())) {
            ensureDepartmentNameUnique(request.getDepartmentName(), request.getId());
            existing.setDepartmentName(request.getDepartmentName());
        }

        // 更新字段
        if (request.getDepartmentResponsibility() != null) {
            existing.setDepartmentResponsibility(request.getDepartmentResponsibility());
        }
        if (request.getLevel() != null) {
            existing.setLevel(request.getLevel());
        }
        if (request.getCompany() != null) {
            existing.setCompany(request.getCompany());
        }

        departmentMapper.update(existing);
        log.info("更新部门成功，departmentId={}, name={}", existing.getId(), existing.getDepartmentName());

        return getById(existing.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DepartmentEntity existing = departmentMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "部门不存在");
        }

        departmentMapper.deleteById(id);
        log.info("逻辑删除部门成功，departmentId={}, name={}", id, existing.getDepartmentName());
    }

    /**
     * 校验部门名称唯一性
     */
    private void ensureDepartmentNameUnique(String departmentName, Long excludeId) {
        if (departmentMapper.countByDepartmentName(departmentName, excludeId) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "部门名称已存在");
        }
    }

    /**
     * 标准化分页查询参数
     */
    private DepartmentPageQueryDTO normalizePageQuery(DepartmentPageQueryDTO queryDTO) {
        DepartmentPageQueryDTO normalized = new DepartmentPageQueryDTO();
        long page = queryDTO == null ? 1L : PageUtils.normalizePage(queryDTO.getPage());
        long size = (queryDTO == null || queryDTO.getSize() < 1L)
                ? 10L : Math.min(queryDTO.getSize(), MAX_PAGE_SIZE);

        normalized.setPage(page);
        normalized.setSize(size);
        if (queryDTO != null) {
            normalized.setName(queryDTO.getName());
            normalized.setLevel(queryDTO.getLevel());
            normalized.setCompany(queryDTO.getCompany());
        }
        return normalized;
    }

    /**
     * 转换为DepartmentVO
     */
    private DepartmentVO toDepartmentVO(DepartmentEntity entity) {
        DepartmentVO vo = new DepartmentVO();
        vo.setId(entity.getId());
        vo.setDepartmentName(entity.getDepartmentName());
        vo.setDepartmentResponsibility(entity.getDepartmentResponsibility());
        vo.setLevel(entity.getLevel());
        vo.setCompany(entity.getCompany());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }
}
