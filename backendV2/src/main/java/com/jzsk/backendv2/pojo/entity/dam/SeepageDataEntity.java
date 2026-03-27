package com.jzsk.backendv2.pojo.entity.dam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 渗流量数据实体
 * 对应数据库表：seepage_data（gcdd数据库，MySQL）
 * 用途：存储渗流量监测数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeepageDataEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Integer recordId;

    /** 测站ID */
    private Integer stationId;

    /** 记录时间 */
    private LocalDateTime recordTime;

    /** 渗流量 */
    private Float seepageFlow;

    /** 备注 */
    private String remarks;
}
