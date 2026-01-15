package com.szy.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 逐日降雨查询表
 * </p>
 *
 * @author l
 * @since 2025-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("daily_rainfall")
public class DailyRainfall implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测站名称
     */
    @TableField("station_name")
    private String stationName;

    /**
     * 8-11时降雨量(mm)
     */
    @TableField("period_8_11")
    private BigDecimal period811;

    /**
     * 11-14时降雨量(mm)
     */
    @TableField("period_11_14")
    private BigDecimal period1114;

    /**
     * 14-17时降雨量(mm)
     */
    @TableField("period_14_17")
    private BigDecimal period1417;

    /**
     * 17-20时降雨量(mm)
     */
    @TableField("period_17_20")
    private BigDecimal period1720;

    /**
     * 20-23时降雨量(mm)
     */
    @TableField("period_20_23")
    private BigDecimal period2023;

    /**
     * 23-2时降雨量(mm)
     */
    @TableField("period_23_2")
    private BigDecimal period232;

    /**
     * 2-5时降雨量(mm)
     */
    @TableField("period_2_5")
    private BigDecimal period25;

    /**
     * 5-8时降雨量(mm)
     */
    @TableField("period_5_8")
    private BigDecimal period58;

    /**
     * 降雨日期
     */
    @TableField("rainfall_date")
    private java.sql.Date rainfallDate;
}
