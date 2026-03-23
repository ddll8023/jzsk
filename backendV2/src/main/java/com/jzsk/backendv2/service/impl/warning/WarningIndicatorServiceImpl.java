package com.jzsk.backendv2.service.impl.warning;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.warning.WarningIndicatorMapper;
import com.jzsk.backendv2.pojo.dto.warning.WarningIndicatorCreateDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningIndicatorPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningIndicatorUpdateDTO;
import com.jzsk.backendv2.pojo.entity.warning.WarningIndicatorEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningIndicatorBindingVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningIndicatorOptionsVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningIndicatorVO;
import com.jzsk.backendv2.service.warning.WarningIndicatorService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 预警指标服务实现类
 * 职责：提供预警指标CRUD业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DS("yjxx")
public class WarningIndicatorServiceImpl implements WarningIndicatorService {

    private final WarningIndicatorMapper warningIndicatorMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResultVO<WarningIndicatorVO> page(WarningIndicatorPageQueryDTO queryDTO) {
        log.info("分页查询预警指标，请求参数：page={}, size={}, type={}, position={}",
                queryDTO.getPage(), queryDTO.getSize(), queryDTO.getType(), queryDTO.getPosition());
        long total = warningIndicatorMapper.countPage(queryDTO);
        if (total <= 0L) {
            return PageResultVO.empty(queryDTO.getPage(), queryDTO.getSize());
        }

        long offset = (queryDTO.getPage() - 1L) * queryDTO.getSize();
        List<WarningIndicatorEntity> entities = warningIndicatorMapper.selectPage(queryDTO, offset, queryDTO.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(queryDTO.getPage(), queryDTO.getSize());
        }

        List<WarningIndicatorVO> voList = entities.stream()
                .map(this::toWarningIndicatorVO)
                .collect(Collectors.toList());

        return PageUtils.buildPage(voList, total, queryDTO.getPage(), queryDTO.getSize());
    }

    @Override
    @Transactional(readOnly = true)
    public WarningIndicatorVO getById(Long id) {
        log.info("根据ID查询预警指标，id={}", id);
        WarningIndicatorEntity entity = warningIndicatorMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "预警指标不存在");
        }
        return toWarningIndicatorVO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public WarningIndicatorOptionsVO getOptions() {
        LinkedHashMap<String, PositionBindingAccumulator> bindingMap = buildPositionBindingMap();

        WarningIndicatorOptionsVO optionsVO = new WarningIndicatorOptionsVO();
        if (bindingMap.isEmpty()) {
            return optionsVO;
        }

        LinkedHashSet<String> allTypes = new LinkedHashSet<>();
        List<OptionVO> positionOptions = new ArrayList<>();
        List<WarningIndicatorBindingVO> bindings = new ArrayList<>();

        for (PositionBindingAccumulator accumulator : bindingMap.values()) {
            positionOptions.add(new OptionVO(accumulator.getLabel(), accumulator.getValue()));
            allTypes.addAll(accumulator.getTypes());

            WarningIndicatorBindingVO bindingVO = new WarningIndicatorBindingVO();
            bindingVO.setPosition(accumulator.getValue());
            bindingVO.setTypeOptions(accumulator.getTypes().stream()
                    .map(this::toOption)
                    .collect(Collectors.toList()));
            bindings.add(bindingVO);
        }

        optionsVO.setPositionOptions(positionOptions);
        optionsVO.setTypeOptions(allTypes.stream()
                .map(this::toOption)
                .collect(Collectors.toList()));
        optionsVO.setBindings(bindings);
        return optionsVO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listAllTypes() {
        log.info("查询所有监测类型");
        return warningIndicatorMapper.selectAllTypes();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarningIndicatorVO create(WarningIndicatorCreateDTO dto) {
        String position = normalizeText(dto.getPosition());
        String type = normalizeText(dto.getType());
        validateBinding(position, type);
        validateDuplicate(position, type, null);

        LocalDateTime now = LocalDateTime.now();

        WarningIndicatorEntity entity = new WarningIndicatorEntity();
        entity.setPosition(position);
        entity.setType(type);
        entity.setUpUpLimit(dto.getUpUpLimit());
        entity.setUpLimit(dto.getUpLimit());
        entity.setLowLimit(dto.getLowLimit());
        entity.setLowerLimit(dto.getLowerLimit());
        entity.setUnit(dto.getUnit());
        entity.setLongitude(dto.getLongitude());
        entity.setLatitude(dto.getLatitude());
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        warningIndicatorMapper.insert(entity);
        log.info("预警指标创建成功，id={}", entity.getId());
        return toWarningIndicatorVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarningIndicatorVO update(WarningIndicatorUpdateDTO dto) {
        WarningIndicatorEntity existing = warningIndicatorMapper.selectById(dto.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "预警指标不存在");
        }

        String position = normalizeText(dto.getPosition());
        String type = normalizeText(dto.getType());
        validateBinding(position, type);
        validateDuplicate(position, type, dto.getId());

        existing.setPosition(position);
        existing.setType(type);
        existing.setUpUpLimit(dto.getUpUpLimit());
        existing.setUpLimit(dto.getUpLimit());
        existing.setLowLimit(dto.getLowLimit());
        existing.setLowerLimit(dto.getLowerLimit());
        existing.setUnit(dto.getUnit());
        existing.setLongitude(dto.getLongitude());
        existing.setLatitude(dto.getLatitude());
        existing.setUpdateTime(LocalDateTime.now());

        warningIndicatorMapper.update(existing);
        log.info("预警指标更新成功，id={}", dto.getId());
        return toWarningIndicatorVO(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        WarningIndicatorEntity existing = warningIndicatorMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "预警指标不存在");
        }
        warningIndicatorMapper.deleteById(id);
        log.info("预警指标删除成功，id={}", id);
    }

    private void validateBinding(String position, String type) {
        LinkedHashMap<String, PositionBindingAccumulator> bindingMap = buildPositionBindingMap();
        PositionBindingAccumulator accumulator = bindingMap.get(position);
        if (accumulator == null || !accumulator.getTypes().contains(type)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "测点名称与监测项不匹配");
        }
    }

    private void validateDuplicate(String position, String type, Long excludeId) {
        long count = warningIndicatorMapper.countByPositionAndType(position, type, excludeId);
        if (count > 0L) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该测点与监测项的预警指标已存在");
        }
    }

    private LinkedHashMap<String, PositionBindingAccumulator> buildPositionBindingMap() {
        LinkedHashMap<String, PositionBindingAccumulator> bindingMap = new LinkedHashMap<>();

        // 兼容旧预警语义中尚未落库的绑定关系，前端不再维护该映射。
        appendPresetBindings(bindingMap);

        List<WarningIndicatorEntity> entities = warningIndicatorMapper.selectAllOptionRelations();
        for (WarningIndicatorEntity entity : entities) {
            String position = normalizeText(entity.getPosition());
            String type = normalizeText(entity.getType());
            if (!StringUtils.hasText(position) || !StringUtils.hasText(type)) {
                continue;
            }
            appendBinding(bindingMap, position, position, Arrays.asList(type));
        }
        return bindingMap;
    }

    private void appendPresetBindings(Map<String, PositionBindingAccumulator> bindingMap) {
        List<String> gnssTypes = Arrays.asList("x位移", "y位移", "z位移", "合位移", "水平位移");
        appendBinding(bindingMap, "LJ1-1", "LJ1-1", gnssTypes);
        appendBinding(bindingMap, "LJ1-2", "LJ1-2", gnssTypes);
        appendBinding(bindingMap, "LJ1-3", "LJ1-3", gnssTypes);
        appendBinding(bindingMap, "LJ1-4", "LJ1-4", gnssTypes);
        appendBinding(bindingMap, "LT2-1", "LT2-1", gnssTypes);
        appendBinding(bindingMap, "LT2-2", "LT2-2", gnssTypes);
        appendBinding(bindingMap, "LT2-3", "LT2-3", gnssTypes);
        appendBinding(bindingMap, "LT2-4", "LT2-4", gnssTypes);
        appendBinding(bindingMap, "坝前雨量水位站", "坝前雨量水位站（新站）", Arrays.asList("雨量", "水位"));
        appendBinding(bindingMap, "mcu测试站", "mcu测站", Arrays.asList("模数", "温度", "水位", "水压", "水位高程"));
    }

    private void appendBinding(Map<String, PositionBindingAccumulator> bindingMap,
                               String value,
                               String label,
                               List<String> types) {
        PositionBindingAccumulator accumulator = bindingMap.computeIfAbsent(value,
                key -> new PositionBindingAccumulator(label, value));
        if (StringUtils.hasText(label)) {
            accumulator.setLabel(label);
        }
        for (String type : types) {
            String normalizedType = normalizeText(type);
            if (StringUtils.hasText(normalizedType)) {
                accumulator.getTypes().add(normalizedType);
            }
        }
    }

    private OptionVO toOption(String value) {
        return new OptionVO(value, value);
    }

    private String normalizeText(String value) {
        return value == null ? null : value.trim();
    }

    /**
     * 实体转VO
     */
    private WarningIndicatorVO toWarningIndicatorVO(WarningIndicatorEntity entity) {
        if (entity == null) {
            return null;
        }
        WarningIndicatorVO vo = new WarningIndicatorVO();
        vo.setId(entity.getId());
        vo.setPosition(entity.getPosition());
        vo.setType(entity.getType());
        vo.setUpUpLimit(entity.getUpUpLimit());
        vo.setUpLimit(entity.getUpLimit());
        vo.setLowLimit(entity.getLowLimit());
        vo.setLowerLimit(entity.getLowerLimit());
        vo.setUnit(entity.getUnit());
        vo.setLongitude(entity.getLongitude());
        vo.setLatitude(entity.getLatitude());
        return vo;
    }

    private static final class PositionBindingAccumulator {
        private String label;
        private final String value;
        private final Set<String> types = new LinkedHashSet<>();

        private PositionBindingAccumulator(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public Set<String> getTypes() {
            return types;
        }
    }
}
