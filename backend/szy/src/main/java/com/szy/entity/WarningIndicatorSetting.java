package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 预警指标设定
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WarningIndicatorSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 监测点
     */
    @NotBlank(message = "监测点不能为空")
    private String position;

    /**
     * 监测类型
     */
    @NotBlank(message = "监测类型不能为空")
    private String type;

    /**
     * 上上限
     */
    @NotNull(message = "上上限不能为空")
    private Double upUpLimit;

    /**
     * 上限
     */
    @NotNull(message = "上限不能为空")
    private Double upLimit;

    /**
     * 下限
     */
    @NotNull(message = "下限不能为空")
    private Double lowLimit;

    /**
     * 下下限
     */
    @NotNull(message = "下下限不能为空")
    private Double lowerLimit;

    /**
     * 单位
     */
    @NotBlank(message = "单位不能为空")
    private String unit;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private java.math.BigDecimal longitude;
    private java.math.BigDecimal latitude;

    public java.math.BigDecimal getLongitude() {
        return longitude;
    }
    public void setLongitude(java.math.BigDecimal longitude) {
        this.longitude = longitude;
    }
    public java.math.BigDecimal getLatitude() {
        return latitude;
    }
    public void setLatitude(java.math.BigDecimal latitude) {
        this.latitude = latitude;
    }

}
