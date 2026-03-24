package com.jzsk.backendv2.service.impl.warning;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.warning.WarningMapper;
import com.jzsk.backendv2.pojo.dto.warning.WarningPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.warning.WarningUpdateDTO;
import com.jzsk.backendv2.pojo.entity.warning.WarningInformationEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.warning.WarningVO;
import com.jzsk.backendv2.service.warning.WarningService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预警信息服务实现类
 * 职责：提供预警信息CRUD、解除预警等业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DS("yjxx")
public class WarningServiceImpl implements WarningService {

    private final WarningMapper warningMapper;

    @Override
    public PageResultVO<WarningVO> page(WarningPageQueryDTO queryDTO) {
        // 时间范围校验
        if (queryDTO.getStartTime() != null && queryDTO.getEndTime() != null
                && queryDTO.getStartTime().after(queryDTO.getEndTime())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "开始时间不能大于结束时间");
        }

        long total = warningMapper.countPage(queryDTO);
        if (total <= 0L) {
            return PageResultVO.empty(queryDTO.getPage(), queryDTO.getSize());
        }

        long offset = (queryDTO.getPage() - 1L) * queryDTO.getSize();
        List<WarningInformationEntity> entities = warningMapper.selectPage(queryDTO, offset, queryDTO.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(queryDTO.getPage(), queryDTO.getSize());
        }

        List<WarningVO> voList = entities.stream()
                .map(this::toWarningVO)
                .collect(Collectors.toList());

        return PageUtils.buildPage(voList, total, queryDTO.getPage(), queryDTO.getSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarningVO update(WarningUpdateDTO dto) {
        WarningInformationEntity existing = warningMapper.selectById(dto.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "预警信息不存在");
        }

        // 复制可更新字段
        copyFields(dto, existing);
        existing.setUpdateTime(new Date());

        // 如果传入了 overTime（解除预警），自动计算持续时长
        if (dto.getOverTime() != null) {
            existing.setOverTime(dto.getOverTime());
            String stayTime = calStayTime(existing.getStartTime(), existing.getOverTime());
            existing.setStayTime(stayTime);
        }

        // 更新基本信息（包括经纬度转换为几何点位）
        warningMapper.update(existing);

        log.info("预警信息更新成功，id={}", dto.getId());
        return toWarningVO(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        WarningInformationEntity existing = warningMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "预警信息不存在");
        }
        warningMapper.deleteById(id);
        log.info("预警信息删除成功，id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarningInformationEntity create(WarningInformationEntity entity) {
        if (entity == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "预警信息不能为空");
        }
        warningMapper.insert(entity);
        log.info("预警信息创建成功，id={}, position={}, type={}, level={}",
                entity.getId(), entity.getPosition(), entity.getType(), entity.getLevel());
        return entity;
    }

    /**
     * 计算持续时长
     * 格式：N天N小时N分钟
     */
    private String calStayTime(Date startTime, Date overTime) {
        if (startTime == null || overTime == null) {
            return null;
        }
        LocalDateTime start = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(overTime.toInstant(), ZoneId.systemDefault());
        Duration duration = Duration.between(start, end);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(days).append("天")
          .append(hours).append("小时")
          .append(minutes).append("分钟");
        return sb.toString();
    }

    /**
     * 将 UpdateDTO 字段复制到 Entity（非空字段才覆盖）
     */
    private void copyFields(WarningUpdateDTO dto, WarningInformationEntity entity) {
        if (dto.getPosition() != null) entity.setPosition(dto.getPosition());
        if (dto.getProject() != null) entity.setProject(dto.getProject());
        if (dto.getContent() != null) entity.setContent(dto.getContent());
        if (dto.getType() != null) entity.setType(dto.getType());
        if (dto.getLevel() != null) entity.setLevel(dto.getLevel());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getLongitude() != null) entity.setLongitude(dto.getLongitude());
        if (dto.getLatitude() != null) entity.setLatitude(dto.getLatitude());
        if (dto.getStartTime() != null) entity.setStartTime(dto.getStartTime());
        if (dto.getOverTime() != null) entity.setOverTime(dto.getOverTime());
    }

    /**
     * 实体转VO
     */
    private WarningVO toWarningVO(WarningInformationEntity entity) {
        if (entity == null) {
            return null;
        }
        WarningVO vo = new WarningVO();
        vo.setId(entity.getId());
        vo.setPosition(entity.getPosition());
        vo.setProject(entity.getProject());
        vo.setContent(entity.getContent());
        vo.setType(entity.getType());
        vo.setLevel(entity.getLevel());
        vo.setStatus(entity.getStatus());
        vo.setLongitude(entity.getLongitude());
        vo.setLatitude(entity.getLatitude());
        vo.setStartTime(entity.getStartTime());
        vo.setOverTime(entity.getOverTime());
        vo.setStayTime(entity.getStayTime());
        return vo;
    }
}
