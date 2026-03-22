package com.jzsk.backendv2.service.impl.system;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.system.DictDetailMapper;
import com.jzsk.backendv2.mapper.system.DictMapper;
import com.jzsk.backendv2.pojo.dto.system.dict.DictCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictOptionQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictUpdateDTO;
import com.jzsk.backendv2.pojo.entity.system.DictDetailEntity;
import com.jzsk.backendv2.pojo.entity.system.DictEntity;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.TreeOptionVO;
import com.jzsk.backendv2.pojo.vo.system.dict.DictDetailVO;
import com.jzsk.backendv2.pojo.vo.system.dict.DictVO;
import com.jzsk.backendv2.service.system.DictService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private static final long MAX_PAGE_SIZE = 10000L;

    private final DictMapper dictMapper;
    private final DictDetailMapper dictDetailMapper;

    @Override
    public PageResultVO<DictVO> page(DictPageQueryDTO queryDTO) {
        DictPageQueryDTO normalized = normalizePageQuery(queryDTO);
        long total = dictMapper.countPage(normalized);
        if (total <= 0L) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        long offset = (normalized.getPage() - 1L) * normalized.getSize();
        List<DictEntity> entities = dictMapper.selectPage(normalized, offset, normalized.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        // 批量查询详情数量
        List<Long> dictIds = entities.stream().map(DictEntity::getId).collect(Collectors.toList());
        List<Map<String, Object>> detailCounts = dictMapper.selectDetailCounts(dictIds);
        Map<Long, Integer> countMap = detailCounts.stream()
                .collect(Collectors.toMap(
                        m -> ((Number) m.get("dictId")).longValue(),
                        m -> ((Number) m.get("detailCount")).intValue()
                ));

        List<DictVO> voList = entities.stream()
                .map(entity -> {
                    DictVO vo = toDictVO(entity);
                    vo.setDetailCount(countMap.getOrDefault(entity.getId(), 0));
                    return vo;
                })
                .collect(Collectors.toList());

        return PageUtils.buildPage(voList, total, normalized.getPage(), normalized.getSize());
    }

    @Override
    public DictVO getById(Long id) {
        DictEntity entity = dictMapper.selectBaseById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "字典不存在");
        }
        DictVO vo = toDictVO(entity);
        vo.setDetailCount(dictDetailMapper.countByDictId(id));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictVO create(DictCreateDTO request) {
        String name = normalizeRequiredText(request.getName(), "字典名称不能为空");
        ensureDictNameUnique(name, null);

        DictEntity entity = new DictEntity();
        entity.setName(name);
        entity.setDescription(normalizeOptionalText(request.getDescription()));
        dictMapper.insert(entity);
        log.info("创建字典成功，dictId={}, name={}", entity.getId(), entity.getName());
        return getById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictVO update(DictUpdateDTO request) {
        DictEntity existing = dictMapper.selectBaseById(request.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "字典不存在");
        }

        String name = normalizeRequiredText(request.getName(), "字典名称不能为空");
        ensureDictNameUnique(name, request.getId());

        existing.setName(name);
        existing.setDescription(normalizeOptionalText(request.getDescription()));
        dictMapper.update(existing);
        log.info("更新字典成功，dictId={}, name={}", existing.getId(), existing.getName());
        return getById(existing.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DictEntity existing = dictMapper.selectBaseById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "字典不存在");
        }

        dictDetailMapper.deleteByDictId(id);
        dictMapper.deleteById(id);
        log.info("删除字典成功，dictId={}, name={}", existing.getId(), existing.getName());
    }

    @Override
    public List<OptionVO> listOptions(DictOptionQueryDTO queryDTO) {
        String name = queryDTO == null ? null : queryDTO.getName();
        List<OptionVO> options = dictMapper.selectOptionsByName(normalizeRequiredText(name, "字典名称不能为空"));
        return options == null ? Collections.emptyList() : options;
    }

    @Override
    public List<TreeOptionVO> treeOptions(DictOptionQueryDTO queryDTO) {
        return listOptions(queryDTO).stream()
                .map(option -> new TreeOptionVO(option.getLabel(), option.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DictDetailVO> getDetailsById(Long id) {
        DictEntity entity = dictMapper.selectBaseById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "字典不存在");
        }
        return toDictDetailVOList(dictDetailMapper.selectByDictId(id));
    }

    private DictPageQueryDTO normalizePageQuery(DictPageQueryDTO queryDTO) {
        DictPageQueryDTO normalized = new DictPageQueryDTO();
        long page = queryDTO == null ? 1L : PageUtils.normalizePage(queryDTO.getPage());
        Long requestSize = queryDTO == null ? null : queryDTO.getSize();
        long size = requestSize == null || requestSize < 1L ? 10L : Math.min(requestSize, MAX_PAGE_SIZE);

        normalized.setPage(page);
        normalized.setSize(size);
        if (queryDTO != null) {
            normalized.setBlurry(normalizeOptionalText(queryDTO.getBlurry()));
        }
        return normalized;
    }

    private void ensureDictNameUnique(String name, Long excludeId) {
        if (dictMapper.countByName(name, excludeId) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "字典名称已存在");
        }
    }

    private DictVO toDictVO(DictEntity entity) {
        return DictVO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    private List<DictVO> toDictVOList(List<DictEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDictVO)
                .collect(Collectors.toList());
    }

    private List<DictDetailVO> toDictDetailVOList(List<DictDetailEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(entity -> DictDetailVO.builder()
                        .id(entity.getId())
                        .dictId(entity.getDictId())
                        .label(entity.getLabel())
                        .value(entity.getValue())
                        .dictSort(entity.getDictSort())
                        .build())
                .collect(Collectors.toList());
    }

    private String normalizeRequiredText(String value, String message) {
        String normalized = normalizeOptionalText(value);
        if (!StringUtils.hasText(normalized)) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    private String normalizeOptionalText(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
