package com.szy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 雨量监测站数据表
 * </p>
 *
 * @author l
 * @since 2025-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RainfallStations implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测站名称
     */
    private String stationName;

    /**
     * 最近来报时间
     */
    private LocalDateTime reportTime;

    /**
     * 坝上水位(m)
     */
    private Float waterLevel;

    /**
     * 汛限水位(m)
     */
    private Float floodLevel;

    /**
     * 蓄水量(万m³)
     */
    private Float waterStorage;

    /**
     * 水势
     */
    private String waterSituation;

    /**
     * 入库流量(m³/s)
     */
    private Float inflow;

    /**
     * 出库流量(m³/s)
     */
    private Float outflow;


}
