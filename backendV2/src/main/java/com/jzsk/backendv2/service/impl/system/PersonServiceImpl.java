package com.jzsk.backendv2.service.impl.system;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.system.PersonMapper;
import com.jzsk.backendv2.pojo.dto.system.person.PersonCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.person.PersonPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.person.PersonUpdateDTO;
import com.jzsk.backendv2.pojo.entity.system.PersonEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.person.PersonVO;
import com.jzsk.backendv2.service.system.PersonService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 人员信息服务实现类
 * 职责：提供人员信息CRUD等业务逻辑
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DS("jcxx")
public class PersonServiceImpl implements PersonService {

    private static final long MAX_PAGE_SIZE = 500L;

    private final PersonMapper personMapper;

    @Override
    public PageResultVO<PersonVO> page(PersonPageQueryDTO queryDTO) {
        PersonPageQueryDTO normalized = normalizePageQuery(queryDTO);
        long total = personMapper.countPage(normalized);
        if (total <= 0L) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        long offset = (normalized.getPage() - 1L) * normalized.getSize();
        List<PersonEntity> entities = personMapper.selectPage(normalized, offset, normalized.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        List<PersonVO> voList = entities.stream()
                .map(this::toPersonVO)
                .collect(Collectors.toList());

        return PageUtils.buildPage(voList, total, normalized.getPage(), normalized.getSize());
    }

    @Override
    public PersonVO getById(Long id) {
        PersonEntity entity = personMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "人员不存在");
        }
        return toPersonVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonVO create(PersonCreateDTO request) {
        PersonEntity entity = new PersonEntity();
        entity.setName(request.getName());
        entity.setAge(request.getAge());
        entity.setGender(request.getGender());
        entity.setPhone(request.getPhone());
        entity.setOrganization(request.getOrganization());
        entity.setPosition(request.getPosition());
        entity.setDuty(request.getDuty());

        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        personMapper.insert(entity);
        log.info("创建人员成功，personId={}, name={}", entity.getId(), entity.getName());

        return getById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonVO update(PersonUpdateDTO request) {
        PersonEntity existing = personMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "人员不存在");
        }

        existing.setName(request.getName());
        existing.setAge(request.getAge());
        existing.setGender(request.getGender());
        existing.setPhone(request.getPhone());
        existing.setOrganization(request.getOrganization());
        existing.setPosition(request.getPosition());
        existing.setDuty(request.getDuty());
        existing.setUpdateTime(LocalDateTime.now());

        personMapper.update(existing);
        log.info("更新人员成功，personId={}, name={}", existing.getId(), existing.getName());

        return getById(existing.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PersonEntity existing = personMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "人员不存在");
        }

        personMapper.deleteById(id);
        log.info("删除人员成功，personId={}, name={}", id, existing.getName());
    }

    /**
     * 标准化分页查询参数
     */
    private PersonPageQueryDTO normalizePageQuery(PersonPageQueryDTO queryDTO) {
        PersonPageQueryDTO normalized = new PersonPageQueryDTO();
        long page = queryDTO == null ? 1L : PageUtils.normalizePage(queryDTO.getPage());
        long size = (queryDTO == null || queryDTO.getSize() < 1L)
                ? 10L : Math.min(queryDTO.getSize(), MAX_PAGE_SIZE);

        normalized.setPage(page);
        normalized.setSize(size);
        if (queryDTO != null) {
            normalized.setName(queryDTO.getName());
        }
        return normalized;
    }

    /**
     * 转换为PersonVO
     */
    private PersonVO toPersonVO(PersonEntity entity) {
        PersonVO vo = new PersonVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setAge(entity.getAge());
        vo.setGender(entity.getGender());
        vo.setPhone(entity.getPhone());
        vo.setOrganization(entity.getOrganization());
        vo.setPosition(entity.getPosition());
        vo.setDuty(entity.getDuty());
        return vo;
    }
}
