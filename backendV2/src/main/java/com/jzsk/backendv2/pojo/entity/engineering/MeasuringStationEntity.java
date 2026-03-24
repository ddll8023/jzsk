package com.jzsk.backendv2.pojo.entity.engineering;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 监测站点实体类
 * 对应数据库表：measuring_station
 * 用途：监测站点的数据模型映射
 * <p>
 * 注意：数据库表包含 point 空间字段（GIS几何点），由 Mapper XML 根据经纬度自动生成，
 * Entity 不映射此字段，Java端无需直接操作。
 */
@Data
public class MeasuringStationEntity {

    /** 监测站点ID（主键，数据库自增） */
    private Long id;

    /** 站码 */
    private String code;

    /** 站名 */
    private String name;

    /** 水系名称 */
    private String waterName;

    /** 河流名称 */
    private String riverName;

    /** 施测项目码 */
    private String monitorCode;

    /** 行政区划码 */
    private String addressCode;

    /** 设站年月 */
    private LocalDate establishDate;

    /** 经度 */
    private BigDecimal longitude;

    /** 纬度 */
    private BigDecimal latitude;

    /** 备注 */
    private String note;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
