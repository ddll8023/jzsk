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
 * 闸门控制记录表
 * </p>
 *
 * @author l
 * @since 2025-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GateControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 闸门ID
     */
    private Integer gateId;

    /**
     * 目标开度
     */
    private BigDecimal targetPosition;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 操作状态
     */
    private String status;


}
