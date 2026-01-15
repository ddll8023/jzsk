package com.szy.entity;

import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 地表水源水
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SurfaceWaterSources implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地表水源水id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 水源地名称
     */
    @Excel(name = "水源地名称" , orderNum = "1")
    @NotBlank(message = "水源地名称不能为空")
    private String name;

    /**
     * 经度
     */
    @Excel(name = "经度" , orderNum = "2")
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度" , orderNum = "3")
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    /**
     * 站点类型
     */
    @Excel(name = "站点类型" , orderNum = "4")
    @NotBlank(message = "站点类型不能为空")
    private String type;

    /**
     * 水面面积
     */
    @Excel(name = "水面面积" , orderNum = "5")
    @NotNull(message = "水面面积不能为空")
    private Double area;

    /**
     * 水质目标
     */
    @Excel(name = "水质目标" , orderNum = "6")
    @NotBlank(message = "水质目标不能为空")
    private String waterQualityObjectives;

    /**
     * 水资源供水持续状况
     */
    @Excel(name = "水资源供水持续状况", orderNum = "7")
    @NotBlank(message = "水资源供水持续状况不能为空")
    private String waterSupplyContinuity;

    /**
     * 供水对象
     */
    @Excel(name = "供水对象" , orderNum = "8")
    @NotBlank(message = "供水对象不能为空")
    private String object;

    /**
     * 管理单位
     */
    @Excel(name = "管理单位" , orderNum = "9")
    @NotBlank(message = "管理单位不能为空")
    private String manageUnit;

    /**
     * 所属供水工程
     */
    @Excel(name = "所属供水工程" , orderNum = "10")
    @NotBlank(message = "所属供水工程不能为空")
    private String waterSupplyProject;

    /**
     * 是否为应急水源地
     */
    @Excel(name = "是否为应急水源地" , orderNum = "11")
    @NotBlank(message = "是否为应急水源地不能为空")
    private String whetherEmergencySource;

    /**
     * 创建时间
     */
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 地图位置
     */
    @ExcelIgnore
    private String point;

    //检查字段是否不为空或者空白,有空或者空白返回true
    //全部字段都有值则返回false
    public boolean checkForEmptyFields() {
        return Arrays.asList(
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                this.latitude == null,
                this.longitude == null,
                Optional.ofNullable(this.type).map(StringUtils::isBlank),
                this.area == null,
                Optional.ofNullable(this.waterQualityObjectives).map(StringUtils::isBlank),
                Optional.ofNullable(this.waterSupplyContinuity).map(StringUtils::isBlank),
                Optional.ofNullable(this.object).map(StringUtils::isBlank),
                Optional.ofNullable(this.manageUnit).map(StringUtils::isBlank),
                Optional.ofNullable(this.waterSupplyProject).map(StringUtils::isBlank),
                Optional.ofNullable(this.whetherEmergencySource).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }
}
