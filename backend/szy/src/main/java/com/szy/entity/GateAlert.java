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
 * 闸门报警记录表
 * </p>
 *
 * @author l
 * @since 2025-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GateAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 闸门ID
     */
    private Integer gateId;

    /**
     * 报警时间
     */
    private LocalDateTime alertTime;

    /**
     * 报警原因
     */
    private String alertReason;


}
