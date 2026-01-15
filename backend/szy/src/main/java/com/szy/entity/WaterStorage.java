package com.szy.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 水库水量表
 * </p>
 *
 * @author l
 * @since 2025-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WaterStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测站名称
     */
    @TableField("station_name")
    private String stationName;

    /**
     * 记录时间
     */
    @TableField("record_time")
    private java.sql.Timestamp recordTime;

    /**
     * 农历日期
     */
    @TableField("lunar_date")
    private String lunarDate;

    /**
     * 坝上水位(m)
     */
    @TableField("water_level")
    private BigDecimal waterLevel;

    /**
     * 出库流量(m³/s)
     */
    @TableField("outflow_volume")
    private BigDecimal outflowVolume;

    /**
     * 蓄水量(万m³)
     */
    @TableField("storage_volume")
    private BigDecimal storageVolume;


}
