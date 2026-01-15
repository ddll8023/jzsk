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
 * 
 * </p>
 *
 * @author l
 * @since 2025-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AnnualWaterSituation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    private Integer stationId;

    private LocalDateTime year;

    private Float waterLevel;

    private Float flowRate;

    private Float maxWaterLevel;

    private Float minWaterLevel;

    private Float maxFlowRate;

    private Float minFlowRate;

    private String remarks;


}
