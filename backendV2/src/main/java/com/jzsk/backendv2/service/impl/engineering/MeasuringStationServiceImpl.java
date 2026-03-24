package com.jzsk.backendv2.service.impl.engineering;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.mapper.engineering.MeasuringStationMapper;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringStationCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringStationPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringStationUpdateDTO;
import com.jzsk.backendv2.pojo.entity.engineering.MeasuringStationEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringStationOptionVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringStationVO;
import com.jzsk.backendv2.service.engineering.MeasuringStationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 监测站点服务实现类
 * 职责：提供监测站点的CRUD和列表查询功能
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MeasuringStationServiceImpl implements MeasuringStationService {

    private final MeasuringStationMapper measuringStationMapper;

    private static final DateTimeFormatter ESTABLISH_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public PageResultVO<MeasuringStationVO> page(MeasuringStationPageQueryDTO queryDTO) {
        log.info("分页查询监测站点，请求参数：{}", queryDTO);

        long page = queryDTO.getPage();
        long size = queryDTO.getSize();
        long offset = (page - 1) * size;

        List<MeasuringStationEntity> entities = measuringStationMapper.selectPage(queryDTO, offset, size);
        long total = measuringStationMapper.countPage(queryDTO);

        List<MeasuringStationVO> voList = entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        log.info("分页查询监测站点成功，总记录数：{}，当前页记录数：{}", total, voList.size());
        return PageResultVO.of(voList, total, page, size);
    }

    @Override
    public MeasuringStationVO getById(Long id) {
        log.info("查询监测站点详情，ID：{}", id);

        if (id == null) {
            throw new IllegalArgumentException("监测站点ID不能为空");
        }

        MeasuringStationEntity entity = measuringStationMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(404, "监测站点不存在");
        }

        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MeasuringStationVO create(MeasuringStationCreateDTO createDTO) {
        log.info("创建监测站点，请求参数：{}", createDTO);

        // 校验站码唯一性
        validateCodeUnique(createDTO.getCode(), null);

        MeasuringStationEntity entity = new MeasuringStationEntity();
        entity.setCode(createDTO.getCode());
        entity.setName(createDTO.getName());
        entity.setWaterName(createDTO.getWaterName());
        entity.setRiverName(createDTO.getRiverName());
        entity.setMonitorCode(createDTO.getMonitorCode());
        entity.setAddressCode(createDTO.getAddressCode());
        entity.setLongitude(createDTO.getLongitude());
        entity.setLatitude(createDTO.getLatitude());
        entity.setNote(createDTO.getNote());

        // 处理设站年月
        if (StringUtils.hasText(createDTO.getEstablishDate())) {
            entity.setEstablishDate(parseEstablishDate(createDTO.getEstablishDate()));
        }

        int rows = measuringStationMapper.insert(entity);
        if (rows <= 0) {
            throw new BusinessException(500, "创建监测站点失败");
        }

        log.info("创建监测站点成功，ID：{}", entity.getId());
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MeasuringStationVO update(MeasuringStationUpdateDTO updateDTO) {
        log.info("更新监测站点，请求参数：{}", updateDTO);

        // 校验ID
        if (updateDTO.getId() == null) {
            throw new IllegalArgumentException("监测站点ID不能为空");
        }

        // 校验站码唯一性
        validateCodeUnique(updateDTO.getCode(), updateDTO.getId());

        // 校验站点存在
        MeasuringStationEntity existingEntity = measuringStationMapper.selectById(updateDTO.getId());
        if (existingEntity == null) {
            throw new BusinessException(404, "监测站点不存在");
        }

        MeasuringStationEntity entity = new MeasuringStationEntity();
        entity.setId(updateDTO.getId());
        entity.setCode(updateDTO.getCode());
        entity.setName(updateDTO.getName());
        entity.setWaterName(updateDTO.getWaterName());
        entity.setRiverName(updateDTO.getRiverName());
        entity.setMonitorCode(updateDTO.getMonitorCode());
        entity.setAddressCode(updateDTO.getAddressCode());
        entity.setLongitude(updateDTO.getLongitude());
        entity.setLatitude(updateDTO.getLatitude());
        entity.setNote(updateDTO.getNote());

        // 处理设站年月
        if (StringUtils.hasText(updateDTO.getEstablishDate())) {
            entity.setEstablishDate(parseEstablishDate(updateDTO.getEstablishDate()));
        }

        int rows = measuringStationMapper.update(entity);
        if (rows <= 0) {
            throw new BusinessException(500, "更新监测站点失败");
        }

        log.info("更新监测站点成功，ID：{}", updateDTO.getId());
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("删除监测站点，ID：{}", id);

        if (id == null) {
            throw new IllegalArgumentException("监测站点ID不能为空");
        }

        // 校验站点存在
        MeasuringStationEntity existingEntity = measuringStationMapper.selectById(id);
        if (existingEntity == null) {
            throw new BusinessException(404, "监测站点不存在");
        }

        int rows = measuringStationMapper.deleteById(id);
        if (rows <= 0) {
            throw new BusinessException(500, "删除监测站点失败");
        }

        log.info("删除监测站点成功，ID：{}", id);
    }

    @Override
    public List<MeasuringStationOptionVO> listNames() {
        log.info("查询所有监测站点名称");
        return measuringStationMapper.selectNames().stream()
                .map(entity -> {
                    MeasuringStationOptionVO vo = new MeasuringStationOptionVO();
                    vo.setId(entity.getId());
                    vo.setName(entity.getName());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 校验站码唯一性
     */
    private void validateCodeUnique(String code, Long excludeId) {
        if (!StringUtils.hasText(code)) {
            return;
        }
        int count = measuringStationMapper.countByCode(code, excludeId);
        if (count > 0) {
            throw new BusinessException(400, "站码已存在");
        }
    }

    /**
     * 解析设站年月
     */
    private LocalDate parseEstablishDate(String establishDate) {
        try {
            LocalDate date = LocalDate.parse(establishDate + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return date;
        } catch (DateTimeParseException e) {
            log.warn("设站年月格式解析失败：{}", establishDate);
            return null;
        }
    }

    /**
     * 实体转换为VO
     */
    private MeasuringStationVO convertToVO(MeasuringStationEntity entity) {
        if (entity == null) {
            return null;
        }

        MeasuringStationVO vo = new MeasuringStationVO();
        vo.setId(entity.getId());
        vo.setCode(entity.getCode());
        vo.setName(entity.getName());
        vo.setWaterName(entity.getWaterName());
        vo.setRiverName(entity.getRiverName());
        vo.setMonitorCode(entity.getMonitorCode());
        vo.setAddressCode(entity.getAddressCode());
        vo.setLongitude(entity.getLongitude());
        vo.setLatitude(entity.getLatitude());
        vo.setNote(entity.getNote());

        // 处理设站年月格式
        if (entity.getEstablishDate() != null) {
            vo.setEstablishDate(entity.getEstablishDate().format(ESTABLISH_DATE_FORMATTER));
        }

        return vo;
    }
}
