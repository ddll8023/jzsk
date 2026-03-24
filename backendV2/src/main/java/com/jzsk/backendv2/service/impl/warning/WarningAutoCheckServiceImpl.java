package com.jzsk.backendv2.service.impl.warning;

import com.jzsk.backendv2.constant.WarningConstants;
import com.jzsk.backendv2.mapper.warning.WarningMapper;
import com.jzsk.backendv2.pojo.entity.warning.WarningInformationEntity;
import com.jzsk.backendv2.service.warning.WarningAutoCheckService;
import com.jzsk.backendv2.service.warning.WarningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 自动预警检查服务实现类
 * 职责：封装阈值判断和预警生成通用逻辑，供WaterLevelRainfallCheckTask、
 *      GnssCheckTask、McuCheckTask三个任务类调用
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarningAutoCheckServiceImpl implements WarningAutoCheckService {

    private final WarningService warningService;
    private final WarningMapper warningMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkAndInsertWarning(String position, String type, BigDecimal value,
                                     LocalDateTime time, String level, String content,
                                     BigDecimal longitude, BigDecimal latitude) {
        if (position == null || type == null || value == null || level == null) {
            log.warn("预警检查参数不完整，跳过: position={}, type={}, value={}, level={}",
                    position, type, value, level);
            return;
        }

        // 去重：查询是否存在同位置、同类型且状态为未解除的预警
        WarningInformationEntity existing = warningMapper.selectUnresolvedByPositionAndType(
                position, type, WarningConstants.STATUS_UNRESOLVED);
        if (existing != null) {
            log.info("自动预警跳过（已存在未解除预警）: position={}, type={}", position, type);
            return;
        }

        WarningInformationEntity entity = new WarningInformationEntity();
        entity.setPosition(position);
        entity.setProject(WarningConstants.PROJECT_NAME);
        entity.setType(type);
        entity.setLevel(level);
        entity.setContent(content);
        entity.setStatus(WarningConstants.STATUS_UNRESOLVED);
        entity.setLongitude(longitude);
        entity.setLatitude(latitude);
        entity.setStartTime(toDate(time));
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());

        warningService.create(entity);
        log.info("自动预警生成成功: position={}, type={}, level={}, content={}, startTime={}",
                position, type, level, content, entity.getStartTime());
    }

    private Date toDate(LocalDateTime ldt) {
        if (ldt == null) {
            return new Date();
        }
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
