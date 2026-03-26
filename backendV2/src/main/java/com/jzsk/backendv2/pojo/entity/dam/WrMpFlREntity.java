package com.jzsk.backendv2.pojo.entity.dam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 渗流量监测数据实体
 * 对应数据库表：wr_mp_fl_r（SQL Server，数据源：yjxx）
 * 用途：存储渗流量监测数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WrMpFlREntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 测点编码 */
    private String mpCd;

    /** 监测时间 */
    private LocalDateTime tm;

    /** 渗流量 */
    private BigDecimal mpFl;

    /** 渗流状态 */
    private String flCond;
}
