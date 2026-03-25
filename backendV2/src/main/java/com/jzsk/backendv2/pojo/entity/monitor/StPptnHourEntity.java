package com.jzsk.backendv2.pojo.entity.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 逐小时降雨量实体
 * 对应数据库表：ST_PPTN_HOUR（SQL Server，数据源：dbo）
 * 用途：水雨情监测雨量数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StPptnHourEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 站码 */
    private String stcd;

    /** 时间 */
    private LocalDateTime tm;

    /** 降水量（mm） */
    private BigDecimal drp;

    /** 时段长 */
    private BigDecimal intv;

    /** 日雨量 */
    private BigDecimal pdr;

    /** 天雨量 */
    private BigDecimal dyp;

    /** 测站类型 */
    private String wth;
}
