package com.szy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 河道水情表
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ST_RIVER_R")
public class StRiversR implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测站编码
     */
    @TableId("STCD") // 假设 STCD 为主键，如果不是，请告知
    private String stcd;

    /**
     * 时间
     */
    @TableField("TM")
    private LocalDateTime tm;

    /**
     * 水位
     */
    @TableField("Z1")
    private BigDecimal z1;

    /**
     * 流量
     */
    @TableField("Q1")
    private BigDecimal q1;

} 