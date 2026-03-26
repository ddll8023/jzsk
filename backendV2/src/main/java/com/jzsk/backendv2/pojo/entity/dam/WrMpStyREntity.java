package com.jzsk.backendv2.pojo.entity.dam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 渗压监测数据实体
 * 对应数据库表：wr_mp_sty_r（SQL Server，数据源：yjxx）
 * 用途：存储渗压监测原始数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WrMpStyREntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 测点编码 */
    private String mpCd;

    /** 监测时间 */
    private LocalDateTime tm;

    /** 渗压值 */
    private BigDecimal mpSty;

    /** 渗透压标识 */
    private Integer speRegData;
}
