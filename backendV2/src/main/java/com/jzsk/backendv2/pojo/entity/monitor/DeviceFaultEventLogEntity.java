package com.jzsk.backendv2.pojo.entity.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备故障事件明细实体类
 * 对应数据库表：device_fault_event_log
 * 用途：记录一次故障期间的关键状态变化
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceFaultEventLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 故障事件ID（主键，数据库自增） */
    private Long id;

    /** 故障主记录ID */
    private Long faultRecordId;

    /** 设备类型：gnss/rain/seepage */
    private String deviceType;

    /** 设备唯一编码 */
    private String deviceCode;

    /** 事件状态：abnormal/offline/online */
    private String eventStatus;

    /** 事件类型：fault_start/status_change/fault_recover */
    private String eventType;

    /** 事件详情 */
    private String eventDetail;

    /** 最后采集时间 */
    private LocalDateTime lastCollectTime;

    /** 事件发生时间 */
    private LocalDateTime eventTime;

    /** 创建时间 */
    private LocalDateTime createTime;
}
