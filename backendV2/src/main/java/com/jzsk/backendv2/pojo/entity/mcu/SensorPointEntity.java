package com.jzsk.backendv2.pojo.entity.mcu;

import lombok.Data;

import java.io.Serializable;

/**
 * 传感器测点实体
 * 对应数据库表：sensor_point（PostgreSQL，数据源：pgsql）
 * 用途：根据测点ID查询测点名称
 */
@Data
public class SensorPointEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 测点名称 */
    private String name;
}
