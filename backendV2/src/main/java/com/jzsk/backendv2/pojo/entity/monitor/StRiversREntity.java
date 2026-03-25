package com.jzsk.backendv2.pojo.entity.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 河道水情实体
 * 对应数据库表：ST_RIVER_R（SQL Server，数据源：dbo）
 * 用途：水雨情监测水位/流量数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StRiversREntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 测站编码 */
    private String stcd;

    /** 时间 */
    private LocalDateTime tm;

    /** 水位 */
    private BigDecimal z1;

    /** 流量 */
    private BigDecimal q1;
}
