package com.jzsk.backendv2.service.impl.engineering;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.mapper.engineering.MeasuringItemMapper;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringItemCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringItemPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MeasuringItemUpdateDTO;
import com.jzsk.backendv2.pojo.entity.engineering.MeasuringItemEntity;
import com.jzsk.backendv2.pojo.vo.ExcelExportData;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringItemExcelVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringItemOptionVO;
import com.jzsk.backendv2.pojo.vo.engineering.MeasuringItemVO;
import com.jzsk.backendv2.service.engineering.MeasuringItemService;
import com.jzsk.backendv2.utils.ExcelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 测项服务实现类
 * 职责：提供测项的CRUD、列表查询和导出功能
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MeasuringItemServiceImpl implements MeasuringItemService {

    private final MeasuringItemMapper measuringItemMapper;

    @Override
    public PageResultVO<MeasuringItemVO> page(MeasuringItemPageQueryDTO queryDTO) {
        log.info("分页查询测项，请求参数：{}", queryDTO);

        long page = queryDTO.getPage();
        long size = queryDTO.getSize();
        long offset = (page - 1) * size;

        List<MeasuringItemEntity> entities = measuringItemMapper.selectPage(queryDTO, offset, size);
        long total = measuringItemMapper.countPage(queryDTO);

        List<MeasuringItemVO> voList = entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        log.info("分页查询测项成功，总记录数：{}，当前页记录数：{}", total, voList.size());
        return PageResultVO.of(voList, total, page, size);
    }

    @Override
    public MeasuringItemVO getById(Long id) {
        log.info("查询测项详情，ID：{}", id);

        if (id == null) {
            throw new IllegalArgumentException("测项ID不能为空");
        }

        MeasuringItemEntity entity = measuringItemMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(404, "测项不存在");
        }

        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MeasuringItemVO create(MeasuringItemCreateDTO createDTO) {
        log.info("创建测项，请求参数：{}", createDTO);

        // 校验编号唯一性
        validateNumberUnique(createDTO.getNumber(), null);

        MeasuringItemEntity entity = new MeasuringItemEntity();
        entity.setNumber(createDTO.getNumber());
        entity.setName(createDTO.getName());
        entity.setUnit(createDTO.getUnit());

        int rows = measuringItemMapper.insert(entity);
        if (rows <= 0) {
            throw new BusinessException(500, "创建测项失败");
        }

        log.info("创建测项成功，ID：{}", entity.getId());
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MeasuringItemVO update(MeasuringItemUpdateDTO updateDTO) {
        log.info("更新测项，请求参数：{}", updateDTO);

        // 校验ID
        if (updateDTO.getId() == null) {
            throw new IllegalArgumentException("测项ID不能为空");
        }

        // 校验编号唯一性
        validateNumberUnique(updateDTO.getNumber(), updateDTO.getId());

        // 校验测项存在
        MeasuringItemEntity existingEntity = measuringItemMapper.selectById(updateDTO.getId());
        if (existingEntity == null) {
            throw new BusinessException(404, "测项不存在");
        }

        MeasuringItemEntity entity = new MeasuringItemEntity();
        entity.setId(updateDTO.getId());
        entity.setNumber(updateDTO.getNumber());
        entity.setName(updateDTO.getName());
        entity.setUnit(updateDTO.getUnit());

        int rows = measuringItemMapper.update(entity);
        if (rows <= 0) {
            throw new BusinessException(500, "更新测项失败");
        }

        log.info("更新测项成功，ID：{}", updateDTO.getId());
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("删除测项，ID：{}", id);

        if (id == null) {
            throw new IllegalArgumentException("测项ID不能为空");
        }

        // 校验测项存在
        MeasuringItemEntity existingEntity = measuringItemMapper.selectById(id);
        if (existingEntity == null) {
            throw new BusinessException(404, "测项不存在");
        }

        int rows = measuringItemMapper.deleteById(id);
        if (rows <= 0) {
            throw new BusinessException(500, "删除测项失败");
        }

        log.info("删除测项成功，ID：{}", id);
    }

    @Override
    public List<MeasuringItemOptionVO> listNames() {
        log.info("查询所有测项名称");
        return measuringItemMapper.selectNames().stream()
                .map(entity -> {
                    MeasuringItemOptionVO vo = new MeasuringItemOptionVO();
                    vo.setId(entity.getId());
                    vo.setName(entity.getName());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ExcelExportData generateExportData() {
        log.info("生成测项列表导出数据");

        List<MeasuringItemEntity> entities = measuringItemMapper.selectAll();
        List<MeasuringItemExcelVO> excelVOList = entities.stream()
                .map(entity -> {
                    MeasuringItemExcelVO vo = new MeasuringItemExcelVO();
                    vo.setId(entity.getId());
                    vo.setNumber(entity.getNumber());
                    vo.setName(entity.getName());
                    vo.setUnit(entity.getUnit());
                    return vo;
                })
                .collect(Collectors.toList());

        ExcelExportData exportData = ExcelUtils.generateExcelData(excelVOList, MeasuringItemExcelVO.class, "测项列表");
        exportData.setFileName("测项信息");

        log.info("生成测项列表导出数据成功，记录数：{}", excelVOList.size());
        return exportData;
    }

    /**
     * 校验测项编号唯一性
     */
    private void validateNumberUnique(String number, Long excludeId) {
        if (!StringUtils.hasText(number)) {
            return;
        }
        int count = measuringItemMapper.countByNumber(number, excludeId);
        if (count > 0) {
            throw new BusinessException(400, "测项编号已存在");
        }
    }

    /**
     * 实体转换为VO
     */
    private MeasuringItemVO convertToVO(MeasuringItemEntity entity) {
        if (entity == null) {
            return null;
        }

        MeasuringItemVO vo = new MeasuringItemVO();
        vo.setId(entity.getId());
        vo.setNumber(entity.getNumber());
        vo.setName(entity.getName());
        vo.setUnit(entity.getUnit());
        return vo;
    }
}
