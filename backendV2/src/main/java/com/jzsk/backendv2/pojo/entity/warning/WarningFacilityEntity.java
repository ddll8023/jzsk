package com.jzsk.backendv2.pojo.entity.warning;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预警设施实体类
 * 对应数据库表：warning_facilities
 * 用途：预警设施的数据模型映射
 */
@Data
public class WarningFacilityEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID（数据库自增） */
    private Long id;

    /** 设施名称 */
    private String facilityName;

    /** 类型（水位监测站、雨量站等） */
    private String type;

    /** 位置 */
    private String location;

    /** 状态（正常、异常、停用等） */
    private String status;

    /** 负责人 */
    private String manager;

    /** 最后维护时间 */
    private LocalDateTime lastUpdate;

    /** 建档时间 */
    private LocalDateTime recordTime;
}
