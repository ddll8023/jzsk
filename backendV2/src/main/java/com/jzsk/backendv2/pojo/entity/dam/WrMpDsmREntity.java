package com.jzsk.backendv2.pojo.entity.dam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 多要素监测数据实体
 * 对应数据库表：wr_mp_dsm_r（SQL Server，数据源：yjxx）
 * 用途：大坝多要素监测数据（渗压、渗流量、位移、水位等）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WrMpDsmREntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 测点编码 */
    private String mpCd;

    /** 监测时间 */
    private LocalDateTime tm;

    /** 温度 */
    private BigDecimal tmp;

    /** 渗透压 */
    private BigDecimal p;

    /** 渗流量 */
    private BigDecimal q;

    /** 水位 */
    private BigDecimal z;

    /** 应变 */
    private BigDecimal e;

    /** 空高 */
    private BigDecimal h;

    /** 位移 */
    private BigDecimal d;

    /** 水平位移 */
    private BigDecimal dx;

    /** 垂直位移 */
    private BigDecimal dy;
}
