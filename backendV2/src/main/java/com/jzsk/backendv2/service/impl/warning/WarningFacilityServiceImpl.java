package com.jzsk.backendv2.service.impl.warning;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.warning.WarningFacilityMapper;
import com.jzsk.backendv2.pojo.dto.warning.WarningFacilityCreateDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningFacilityPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningFacilityUpdateDTO;
import com.jzsk.backendv2.pojo.entity.warning.WarningFacilityEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningFacilityVO;
import com.jzsk.backendv2.service.warning.WarningFacilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预警设施服务实现类
 * 职责：提供预警设施的CRUD和列表查询功能
 * 遵循KISS原则：方法简洁，职责单一
 */
@DS("gcdd")
@Service
@RequiredArgsConstructor
@Slf4j
public class WarningFacilityServiceImpl implements WarningFacilityService {

    private final WarningFacilityMapper warningFacilityMapper;

    /**
     * 分页查询预警设施列表
     * @param queryDTO 分页查询条件
     * @return 分页结果
     */
    @Override
    public PageResultVO<WarningFacilityVO> page(WarningFacilityPageQueryDTO queryDTO) {
        log.info("分页查询预警设施，查询条件：{}", queryDTO);
        long offset = (queryDTO.getPage() - 1L) * queryDTO.getSize();
        List<WarningFacilityEntity> list = warningFacilityMapper.selectPage(queryDTO, offset, queryDTO.getSize());
        long total = warningFacilityMapper.selectCount(queryDTO);
        List<WarningFacilityVO> voList = list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return PageResultVO.of(voList, total, queryDTO.getPage(), queryDTO.getSize());
    }

    /**
     * 根据ID查询预警设施详情
     * @param id 预警设施ID
     * @return 预警设施VO
     * @throws BusinessException 预警设施不存在时抛出
     */
    @Override
    public WarningFacilityVO getById(Long id) {
        log.info("查询预警设施详情，ID：{}", id);
        WarningFacilityEntity entity = warningFacilityMapper.selectById(id);
        if (entity == null) {
            log.warn("查询预警设施不存在，ID：{}", id);
            throw new BusinessException(ErrorCode.NOT_FOUND, "预警设施不存在");
        }
        return convertToVO(entity);
    }

    /**
     * 创建预警设施
     * @param request 创建请求
     * @return 预警设施VO
     * @throws BusinessException 创建失败时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarningFacilityVO create(WarningFacilityCreateDTO request) {
        log.info("创建预警设施，请求参数：{}", request);
        WarningFacilityEntity entity = new WarningFacilityEntity();
        entity.setFacilityName(request.getFacilityName());
        entity.setType(request.getType());
        entity.setLocation(request.getLocation());
        entity.setStatus(request.getStatus());
        entity.setManager(request.getManager());
        entity.setLastUpdate(request.getLastUpdate() != null ? request.getLastUpdate() : LocalDateTime.now());
        entity.setRecordTime(request.getRecordTime() != null ? request.getRecordTime() : LocalDateTime.now());
        warningFacilityMapper.insert(entity);
        log.info("创建预警设施成功，ID：{}", entity.getId());
        return convertToVO(entity);
    }

    /**
     * 更新预警设施
     * @param request 更新请求
     * @return 预警设施VO
     * @throws BusinessException 预警设施不存在时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarningFacilityVO update(WarningFacilityUpdateDTO request) {
        log.info("更新预警设施，请求参数：{}", request);
        WarningFacilityEntity existing = warningFacilityMapper.selectById(request.getId());
        if (existing == null) {
            log.warn("更新预警设施不存在，ID：{}", request.getId());
            throw new BusinessException(ErrorCode.NOT_FOUND, "预警设施不存在");
        }
        WarningFacilityEntity entity = new WarningFacilityEntity();
        entity.setId(request.getId());
        entity.setFacilityName(request.getFacilityName());
        entity.setType(request.getType());
        entity.setLocation(request.getLocation());
        entity.setStatus(request.getStatus());
        entity.setManager(request.getManager());
        if (request.getLastUpdate() != null) {
            entity.setLastUpdate(request.getLastUpdate());
        } else {
            entity.setLastUpdate(existing.getLastUpdate());
        }
        if (request.getRecordTime() != null) {
            entity.setRecordTime(request.getRecordTime());
        } else {
            entity.setRecordTime(existing.getRecordTime());
        }
        warningFacilityMapper.update(entity);
        log.info("更新预警设施成功，ID：{}", request.getId());
        return convertToVO(entity);
    }

    /**
     * 删除预警设施（物理删除）
     * @param id 预警设施ID
     * @throws BusinessException 预警设施不存在时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("删除预警设施，ID：{}", id);
        WarningFacilityEntity existing = warningFacilityMapper.selectById(id);
        if (existing == null) {
            log.warn("删除预警设施不存在，ID：{}", id);
            throw new BusinessException(ErrorCode.NOT_FOUND, "预警设施不存在");
        }
        warningFacilityMapper.deleteById(id);
        log.info("删除预警设施成功，ID：{}", id);
    }

    /**
     * 实体转VO
     * @param entity 预警设施实体
     * @return 预警设施VO
     */
    private WarningFacilityVO convertToVO(WarningFacilityEntity entity) {
        WarningFacilityVO vo = new WarningFacilityVO();
        vo.setId(entity.getId());
        vo.setFacilityName(entity.getFacilityName());
        vo.setType(entity.getType());
        vo.setLocation(entity.getLocation());
        vo.setStatus(entity.getStatus());
        vo.setManager(entity.getManager());
        vo.setLastUpdate(entity.getLastUpdate());
        vo.setRecordTime(entity.getRecordTime());
        return vo;
    }
}
