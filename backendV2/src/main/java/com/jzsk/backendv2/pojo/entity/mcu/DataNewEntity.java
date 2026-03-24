package com.jzsk.backendv2.pojo.entity.mcu;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * MCU传感器数据实体
 * 对应数据库表：data_new（PostgreSQL，数据源：pgsql）
 * 用途：MCU传感器最新数据，用于自动预警判断
 */
@Data
public class DataNewEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 采集时间 */
    private OffsetDateTime time;

    /** 测点编号 */
    private String pointId;

    /** 原始数据 */
    private String originalData;

    /** 数据类型 */
    private String type;

    /** 索引 */
    private Long indexes;

    /** 项目ID */
    private Long projectId;

    /** 报警标识 */
    private Integer alarmSign;

    /** 结果数据（JSON格式，含模数等字段） */
    private String resultData;

    /** 参数数据 */
    private String paramData;

    /** 状态 */
    private Integer state;

    /** 报警详情 */
    private String alarmDetail;

    /** 确认状态 */
    private String confirm;

    /** 创建时间 */
    private OffsetDateTime createTime;
}
