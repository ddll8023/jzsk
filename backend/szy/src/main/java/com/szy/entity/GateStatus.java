package com.szy.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 闸门状态表
 * </p>
 *
 * @author l
 * @since 2025-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GateStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 闸门ID
     */
    private Integer gateId;

    /**
     * 闸门开度
     */
    private BigDecimal position;

    /**
     * 闸前水位
     */
    private BigDecimal upstreamLevel;

    /**
     * 闸后水位
     */
    private BigDecimal downstreamLevel;

    /**
     * 流量
     */
    private BigDecimal flowRate;

    /**
     * 启闭机状态
     */
    private String machineStatus;

    /**
     * 记录创建时间
     */
    private LocalDateTime createdAt;


}
