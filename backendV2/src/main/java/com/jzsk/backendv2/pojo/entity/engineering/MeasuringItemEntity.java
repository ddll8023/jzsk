package com.jzsk.backendv2.pojo.entity.engineering;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 测项实体类
 * 对应数据库表：measuring_item
 * 用途：测项的数据模型映射
 */
@Data
public class MeasuringItemEntity {

    /** 测项主键ID（数据库自增） */
    private Long id;

    /** 测项编号 */
    private String number;

    /** 测项名称 */
    private String name;

    /** 测项单位 */
    private String unit;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
