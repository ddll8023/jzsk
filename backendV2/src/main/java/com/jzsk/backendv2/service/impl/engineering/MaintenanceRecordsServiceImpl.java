package com.jzsk.backendv2.service.impl.engineering;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.mapper.engineering.MaintenanceRecordsMapper;
import com.jzsk.backendv2.pojo.dto.engineering.MaintenanceRecordsCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MaintenanceRecordsPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.MaintenanceRecordsUpdateDTO;
import com.jzsk.backendv2.pojo.entity.engineering.MaintenanceRecordsEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.MaintenanceRecordsExcelVO;
import com.jzsk.backendv2.pojo.vo.engineering.MaintenanceRecordsVO;
import com.jzsk.backendv2.service.engineering.MaintenanceRecordsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 养护记录服务实现类
 * 职责：提供养护记录的CRUD和列表查询功能
 * 数据源：gcdd
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@DS("gcdd")
@RequiredArgsConstructor
public class MaintenanceRecordsServiceImpl implements MaintenanceRecordsService {

    private final MaintenanceRecordsMapper maintenanceRecordsMapper;

    @Override
    public PageResultVO<MaintenanceRecordsVO> page(MaintenanceRecordsPageQueryDTO queryDTO) {
        log.info("分页查询养护记录，请求参数：{}", queryDTO);

        long page = queryDTO.getPage();
        long size = queryDTO.getSize();
        long offset = (page - 1) * size;

        List<MaintenanceRecordsEntity> entities = maintenanceRecordsMapper.selectPage(queryDTO, offset, size);
        long total = maintenanceRecordsMapper.countPage(queryDTO);

        List<MaintenanceRecordsVO> voList = entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        log.info("分页查询养护记录成功，总记录数：{}，当前页记录数：{}", total, voList.size());
        return PageResultVO.of(voList, total, page, size);
    }

    @Override
    public MaintenanceRecordsVO getById(Long id) {
        log.info("查询养护记录详情，ID：{}", id);
        MaintenanceRecordsEntity entity = maintenanceRecordsMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(404, "养护记录不存在");
        }
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MaintenanceRecordsVO create(MaintenanceRecordsCreateDTO request) {
        log.info("创建养护记录，请求参数：{}", request);

        MaintenanceRecordsEntity entity = new MaintenanceRecordsEntity();
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setNote(request.getNote());
        entity.setResponsiblePerson(request.getResponsiblePerson());
        entity.setPhone(request.getPhone());
        entity.setStartTime(request.getStartTime());
        entity.setOverTime(request.getOverTime());

        maintenanceRecordsMapper.insert(entity);
        log.info("创建养护记录成功，ID：{}", entity.getId());
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MaintenanceRecordsVO update(MaintenanceRecordsUpdateDTO request) {
        log.info("更新养护记录，请求参数：{}", request);

        MaintenanceRecordsEntity existing = maintenanceRecordsMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(404, "养护记录不存在");
        }

        MaintenanceRecordsEntity entity = new MaintenanceRecordsEntity();
        entity.setId(request.getId());
        if (StringUtils.hasText(request.getName())) {
            entity.setName(request.getName());
        }
        if (StringUtils.hasText(request.getCode())) {
            entity.setCode(request.getCode());
        }
        if (request.getNote() != null) {
            entity.setNote(request.getNote());
        }
        if (StringUtils.hasText(request.getResponsiblePerson())) {
            entity.setResponsiblePerson(request.getResponsiblePerson());
        }
        if (StringUtils.hasText(request.getPhone())) {
            entity.setPhone(request.getPhone());
        }
        if (request.getStartTime() != null) {
            entity.setStartTime(request.getStartTime());
        }
        if (request.getOverTime() != null) {
            entity.setOverTime(request.getOverTime());
        }

        maintenanceRecordsMapper.update(entity);
        log.info("更新养护记录成功，ID：{}", request.getId());
        return getById(request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("删除养护记录，ID：{}", id);
        MaintenanceRecordsEntity existing = maintenanceRecordsMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "养护记录不存在");
        }
        maintenanceRecordsMapper.deleteById(id);
        log.info("删除养护记录成功，ID：{}", id);
    }

    @Override
    public List<MaintenanceRecordsExcelVO> listForExport() {
        log.info("查询养护记录导出数据");
        List<MaintenanceRecordsEntity> entities = maintenanceRecordsMapper.selectAllForExport();
        return entities.stream()
                .map(this::convertToExcelVO)
                .collect(Collectors.toList());
    }

    /**
     * 转换为VO
     */
    private MaintenanceRecordsVO convertToVO(MaintenanceRecordsEntity entity) {
        if (entity == null) {
            return null;
        }
        MaintenanceRecordsVO vo = new MaintenanceRecordsVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setCode(entity.getCode());
        vo.setNote(entity.getNote());
        vo.setResponsiblePerson(entity.getResponsiblePerson());
        vo.setPhone(entity.getPhone());
        vo.setStartTime(entity.getStartTime());
        vo.setOverTime(entity.getOverTime());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    /**
     * 转换为Excel导出VO
     */
    private MaintenanceRecordsExcelVO convertToExcelVO(MaintenanceRecordsEntity entity) {
        if (entity == null) {
            return null;
        }
        MaintenanceRecordsExcelVO vo = new MaintenanceRecordsExcelVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setCode(entity.getCode());
        vo.setNote(entity.getNote());
        vo.setResponsiblePerson(entity.getResponsiblePerson());
        vo.setPhone(entity.getPhone());
        vo.setStartTime(entity.getStartTime());
        vo.setOverTime(entity.getOverTime());
        return vo;
    }
}
