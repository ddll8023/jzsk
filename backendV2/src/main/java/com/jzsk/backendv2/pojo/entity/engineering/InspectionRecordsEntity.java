package com.jzsk.backendv2.pojo.entity.engineering;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 巡检记录实体类
 * 对应数据库表：inspection_records
 * 用途：巡检记录的数据模型映射
 * <p>
 * 注意：数据库表包含 point 空间字段（GIS几何点），由 Mapper XML 根据经纬度自动生成，
 * Entity 不映射此字段，Java端无需直接操作。
 */
@Data
public class InspectionRecordsEntity {

    /** 巡检记录ID（主键，数据库自增） */
    private Long id;

    /** 巡检站点 */
    private String project;

    /** 经度 */
    private BigDecimal longitude;

    /** 纬度 */
    private BigDecimal latitude;

    /** 巡检类型 */
    private String type;

    /** 异常情况 */
    private String abnormal;

    /** 巡检情况 */
    private String situation;

    /** 处理状态 */
    private String solve;

    /** 图片路径 */
    private String image;

    /** 负责人 */
    private String person;

    /** 巡检日期 */
    private LocalDateTime date;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
