package com.szy.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * rain std criterion
 * </p>
 *
 * @author l
 * @since 2025-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StPextremumB implements Serializable {


    /**
     * 监测站代码
     */
    @TableId("STCD")
    private String stcd;

    /**
     * 1小时最大雨量
     */
    @TableField("maxDrp1h")
    private BigDecimal maxdrp1h;

    /**
     * 3小时最大雨量
     */
    @TableField("maxDrp3h")
    private BigDecimal maxdrp3h;

    /**
     * 6小时最大雨量
     */
    @TableField("maxDrp6h")
    private BigDecimal maxdrp6h;

    /**
     * 12小时最大雨量
     */
    @TableField("maxDrp12h")
    private BigDecimal maxdrp12h;

    /**
     * 24小时最大雨量
     */
    @TableField("maxDrp24h")
    private BigDecimal maxdrp24h;

    /**
     * 1小时最大雨量发生时间
     */
    @TableField("Tm1h")
    private LocalDateTime tm1h;

    /**
     * 3小时最大雨量发生时间
     */
    @TableField("Tm3h")
    private LocalDateTime tm3h;

    /**
     * 6小时最大雨量发生时间
     */
    @TableField("Tm6h")
    private LocalDateTime tm6h;

    /**
     * 12小时最大雨量发生时间
     */
    @TableField("Tm12h")
    private LocalDateTime tm12h;

    /**
     * 24小时最大雨量发生时间
     */
    @TableField("Tm24h")
    private LocalDateTime tm24h;

    /**
     * 备注
     */
    @TableField("Remark")
    private String remark;


}
