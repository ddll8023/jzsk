package com.jzsk.backendv2.pojo.entity.warning;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预警指标实体类
 * 对应数据库表：warning_indicator_setting
 * 用途：预警指标的数据模型映射
 */
@Data
public class WarningIndicatorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID（数据库自增） */
    private Long id;

    /** 监测点 */
    private String position;

    /** 监测类型 */
    private String type;

    /** 上上限 */
    private Double upUpLimit;

    /** 上限 */
    private Double upLimit;

    /** 下限 */
    private Double lowLimit;

    /** 下下限 */
    private Double lowerLimit;

    /** 单位 */
    private String unit;

    /** 经度 */
    private BigDecimal longitude;

    /** 纬度 */
    private BigDecimal latitude;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;
}
