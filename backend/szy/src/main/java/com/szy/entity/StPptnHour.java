package com.szy.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 逐小时降雨量数据表
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ST_PPTN_HOUR")
public class StPptnHour implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 站码
     */
    @TableId("STCD")
    private String stcd;

    /**
     * 时间
     */
    @TableField("TM")
    private LocalDateTime tm;

    /**
     * 降水量
     */
    @TableField("DRP")
    private BigDecimal drp;

    /**
     * 时段长
     */
    @TableField("INTV")
    private BigDecimal intv;

    /**
     * 日雨量
     */
    @TableField("PDR")
    private BigDecimal pdr;

    /**
     * 天雨量
     */
    @TableField("DYP")
    private BigDecimal dyp;

    /**
     * 测站类型
     */
    @TableField("WTH")
    private String wth;


}
