package com.jzsk.backendv2.service.impl.monitor;

import com.jzsk.backendv2.mapper.monitor.DeviceFaultEventLogMapper;
import com.jzsk.backendv2.mapper.monitor.DeviceFaultRecordMapper;
import com.jzsk.backendv2.pojo.dto.monitor.DeviceFaultPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.monitor.DeviceFaultEventLogEntity;
import com.jzsk.backendv2.pojo.entity.monitor.DeviceFaultRecordEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceFaultEventLogVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceFaultRecordVO;
import com.jzsk.backendv2.pojo.vo.monitor.DeviceStatusVO;
import com.jzsk.backendv2.service.monitor.DeviceFaultRecordService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备到报情况记录服务实现类
 * 职责：处理设备到报状态变化，维护到报情况记录的创建、更新、恢复，以及分页查询
 * 遵循KISS原则：一次异常到报情况一条主记录，状态变化更新不新增
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceFaultRecordServiceImpl implements DeviceFaultRecordService {

    private static final long MAX_PAGE_SIZE = 100L;

    private final DeviceFaultRecordMapper deviceFaultRecordMapper;
    private final DeviceFaultEventLogMapper deviceFaultEventLogMapper;

    @Override
    public void processDeviceStatus(DeviceStatusVO device) {
        String deviceType = device.getType();
        String deviceCode = device.getCode();
        String status = device.getStatus();

        if ("online".equals(status)) {
            resolveActiveFault(deviceType, deviceCode);
        } else if ("offline".equals(status) || "abnormal".equals(status)) {
            upsertFaultRecord(device);
        }
    }

    /**
     * 设备恢复到报，将活跃异常记录标记为已恢复
     */
    private void resolveActiveFault(String deviceType, String deviceCode) {
        String activeKey = deviceType + ":" + deviceCode;
        DeviceFaultRecordEntity activeFault = deviceFaultRecordMapper.selectByActiveKey(activeKey);
        if (activeFault == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        activeFault.setProcessStatus("resolved");
        activeFault.setEndTime(now);
        long minutes = ChronoUnit.MINUTES.between(activeFault.getStartTime(), now);
        activeFault.setDurationMinutes((int) minutes);
        activeFault.setUpdateTime(now);
        deviceFaultRecordMapper.updateResolved(activeFault);
        insertEventLog(activeFault.getId(), deviceType, deviceCode, "online", "fault_recover", null, null, now);
        log.info("[DeviceFault] 到报恢复: {}/{}, 持续{}分钟", deviceType, deviceCode, minutes);
    }

    /**
     * 设备未到报或采集异常，新增或更新到报情况记录
     */
    private void upsertFaultRecord(DeviceStatusVO device) {
        String activeKey = device.getType() + ":" + device.getCode();
        DeviceFaultRecordEntity activeFault = deviceFaultRecordMapper.selectByActiveKey(activeKey);

        if (activeFault != null) {
            String prevStatus = activeFault.getCurrentFaultStatus();
            activeFault.setCurrentFaultStatus(device.getStatus());
            activeFault.setFaultDetail(device.getDetail());
            activeFault.setLastCollectTime(device.getLastCollectTime());
            activeFault.setUpdateTime(LocalDateTime.now());
            deviceFaultRecordMapper.updateActive(activeFault);
            if (!prevStatus.equals(device.getStatus())) {
                insertEventLog(activeFault.getId(), device.getType(), device.getCode(),
                        device.getStatus(), "status_change", device.getDetail(),
                        device.getLastCollectTime(), LocalDateTime.now());
            }
        } else {
            DeviceFaultRecordEntity record = new DeviceFaultRecordEntity();
            record.setDeviceType(device.getType());
            record.setDeviceCode(device.getCode());
            record.setDeviceName(device.getName());
            record.setActiveKey(activeKey);
            record.setFirstFaultStatus(device.getStatus());
            record.setCurrentFaultStatus(device.getStatus());
            record.setFaultType(determineFaultType(device));
            record.setFaultDetail(device.getDetail());
            record.setLastCollectTime(device.getLastCollectTime());
            LocalDateTime now = LocalDateTime.now();
            // 未到报优先使用最后采集时间作为异常起点；无采集时间时使用检测时刻。
            LocalDateTime startTime = device.getLastCollectTime() != null ? device.getLastCollectTime() : now;
            record.setStartTime(startTime);
            record.setProcessStatus("active");
            record.setCreateTime(now);
            record.setUpdateTime(now);
            deviceFaultRecordMapper.insert(record);
            insertEventLog(record.getId(), device.getType(), device.getCode(),
                    device.getStatus(), "fault_start", device.getDetail(),
                    device.getLastCollectTime(), startTime);
            log.info("[DeviceFault] 到报异常发生: {}/{}, 状态: {}", device.getType(), device.getCode(), device.getStatus());
        }
    }

    /**
     * 推断到报异常类型
     */
    private String determineFaultType(DeviceStatusVO device) {
        if ("abnormal".equals(device.getStatus()) || device.getLastCollectTime() == null) {
            return "no_data";
        }
        return "collect_timeout";
    }

    /**
     * 分页查询到报情况记录
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    @Override
    public PageResultVO<DeviceFaultRecordVO> page(DeviceFaultPageQueryDTO queryDTO) {
        DeviceFaultPageQueryDTO normalized = normalizePageQuery(queryDTO);
        long total = deviceFaultRecordMapper.countPage(normalized);
        if (total <= 0L) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        long offset = (normalized.getPage() - 1L) * normalized.getSize();
        List<DeviceFaultRecordEntity> entities = deviceFaultRecordMapper.selectPage(normalized, offset, normalized.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        List<DeviceFaultRecordVO> voList = entities.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageUtils.buildPage(voList, total, normalized.getPage(), normalized.getSize());
    }

    /**
     * 归一化分页参数
     */
    private DeviceFaultPageQueryDTO normalizePageQuery(DeviceFaultPageQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new DeviceFaultPageQueryDTO();
        }
        if (queryDTO.getPage() < 1L) {
            queryDTO.setPage(1L);
        }
        if (queryDTO.getSize() < 1L) {
            queryDTO.setSize(10L);
        }
        if (queryDTO.getSize() > MAX_PAGE_SIZE) {
            queryDTO.setSize(MAX_PAGE_SIZE);
        }
        return queryDTO;
    }

    /**
     * Entity转VO
     */
    private DeviceFaultRecordVO toVO(DeviceFaultRecordEntity entity) {
        DeviceFaultRecordVO vo = new DeviceFaultRecordVO();
        vo.setId(entity.getId());
        vo.setDeviceType(entity.getDeviceType());
        vo.setDeviceCode(entity.getDeviceCode());
        vo.setDeviceName(entity.getDeviceName());
        vo.setFirstFaultStatus(entity.getFirstFaultStatus());
        vo.setCurrentFaultStatus(entity.getCurrentFaultStatus());
        vo.setFaultType(entity.getFaultType());
        vo.setFaultDetail(entity.getFaultDetail());
        vo.setLastCollectTime(entity.getLastCollectTime());
        vo.setStartTime(entity.getStartTime());
        vo.setEndTime(entity.getEndTime());
        vo.setDurationMinutes(entity.getDurationMinutes());
        vo.setProcessStatus(entity.getProcessStatus());
        return vo;
    }

    /**
     * 写入故障事件明细
     */
    private void insertEventLog(Long faultRecordId, String deviceType, String deviceCode,
                                String eventStatus, String eventType, String eventDetail,
                                LocalDateTime lastCollectTime, LocalDateTime eventTime) {
        try {
            DeviceFaultEventLogEntity eventLog = new DeviceFaultEventLogEntity();
            eventLog.setFaultRecordId(faultRecordId);
            eventLog.setDeviceType(deviceType);
            eventLog.setDeviceCode(deviceCode);
            eventLog.setEventStatus(eventStatus);
            eventLog.setEventType(eventType);
            eventLog.setEventDetail(eventDetail);
            eventLog.setLastCollectTime(lastCollectTime);
            eventLog.setEventTime(eventTime);
            eventLog.setCreateTime(LocalDateTime.now());
            deviceFaultEventLogMapper.insert(eventLog);
        } catch (Exception e) {
            log.error("[DeviceFault] 事件明细写入失败: faultRecordId={}, eventType={}", faultRecordId, eventType, e);
        }
    }

    /**
     * 查询故障事件明细列表
     * @param faultRecordId 故障主记录ID
     * @return 事件明细列表
     */
    @Override
    public List<DeviceFaultEventLogVO> getEvents(Long faultRecordId) {
        if (faultRecordId == null) {
            return Collections.emptyList();
        }
        List<DeviceFaultEventLogEntity> entities = deviceFaultEventLogMapper.selectByFaultRecordId(faultRecordId);
        return entities.stream()
                .map(this::eventLogToVO)
                .collect(Collectors.toList());
    }

    /**
     * 事件Entity转VO
     */
    private DeviceFaultEventLogVO eventLogToVO(DeviceFaultEventLogEntity entity) {
        DeviceFaultEventLogVO vo = new DeviceFaultEventLogVO();
        vo.setId(entity.getId());
        vo.setEventStatus(entity.getEventStatus());
        vo.setEventType(entity.getEventType());
        vo.setEventDetail(entity.getEventDetail());
        vo.setLastCollectTime(entity.getLastCollectTime());
        vo.setEventTime(entity.getEventTime());
        return vo;
    }

    /**
     * 删除到报情况记录（级联删除事件明细）
     */
    @Override
    public void delete(Long id) {
        deviceFaultEventLogMapper.deleteByFaultRecordId(id);
        deviceFaultRecordMapper.deleteById(id);
        log.info("[DeviceFault] 到报情况记录已删除: id={}", id);
    }

    /**
     * 清理超过保留天数的到报情况记录和事件明细
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanExpiredRecords(int retentionDays) {
        if (retentionDays < 1) {
            throw new IllegalArgumentException("数据保留天数必须大于0");
        }
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(retentionDays);
        int eventCount = deviceFaultEventLogMapper.deleteBeforeTime(cutoffTime);
        int recordCount = deviceFaultRecordMapper.deleteBeforeTime(cutoffTime);
        log.info("[DeviceFault] 过期到报情况记录清理完成，cutoffTime={}, recordCount={}, eventCount={}",
                cutoffTime, recordCount, eventCount);
    }
}
