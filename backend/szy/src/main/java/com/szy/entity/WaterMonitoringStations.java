package com.szy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WaterMonitoringStations implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "station_id", type = IdType.AUTO)
    private Integer stationId;

    private String stationCode;

    private String stationName;

    private String location;

    private String stationType;


}
