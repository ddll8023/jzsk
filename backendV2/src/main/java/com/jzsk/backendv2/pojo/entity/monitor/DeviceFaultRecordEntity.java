package com.jzsk.backendv2.pojo.entity.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备故障记录实体类
 * 对应数据库表：device_fault_record
 * 用途：记录设备故障生命周期的数据模型映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceFaultRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 故障记录ID（主键，数据库自增） */
    private Long id;

    /** 设备类型：gnss/rain/seepage */
    private String deviceType;

    /** 设备唯一编码 */
    private String deviceCode;

    /** 设备名称 */
    private String deviceName;

    /** 活跃故障唯一键，active时为device_type:device_code，resolved后置空 */
    private String activeKey;

    /** 首次故障状态：offline/abnormal */
    private String firstFaultStatus;

    /** 当前故障状态：offline/abnormal */
    private String currentFaultStatus;

    /** 故障类型：interface_error/no_data/collect_timeout/db_error */
    private String faultType;

    /** 故障详情或采集值快照 */
    private String faultDetail;

    /** 最后采集时间 */
    private LocalDateTime lastCollectTime;

    /** 故障开始时间 */
    private LocalDateTime startTime;

    /** 故障解除时间 */
    private LocalDateTime endTime;

    /** 故障持续分钟数 */
    private Integer durationMinutes;

    /** 处理状态：active/resolved */
    private String processStatus;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;
}
