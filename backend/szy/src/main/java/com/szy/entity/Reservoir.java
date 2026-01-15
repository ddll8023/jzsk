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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 工程站点-水库
 * </p>
 *
 * @author l
 * @since 2022-01-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Reservoir implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水库id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属供水工程
     */
    @Excel(name = "所属供水工程" , orderNum = "1")
    @NotBlank(message = "所属供水工程不能为空")
    private String waterSupply;

    /**
     * 水库编码
     */
    @Excel(name = "水库编码" , orderNum = "2")
    @NotBlank(message = "水库编码不能为空")
    private String code;

    /**
     * 水库名称
     */
    @Excel(name = "水库名称" , orderNum = "3")
    @NotBlank(message = "水库名称不能为空")
    private String name;

    /**
     * 经度
     */
    @Excel(name = "经度" , orderNum = "4")
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度" , orderNum = "5")
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    /**
     * 水库所在位置
     */
    @Excel(name = "水库所在位置" , orderNum = "6")
    @NotBlank(message = "水库所在位置不能为空")
    private String locate;

    /**
     * 水库注册登记号
     */
    @Excel(name = "水库注册登记号" , orderNum = "7")
    @NotBlank(message = "水库注册登记号不能为空")
    private String registrationNumber;

    /**
     * 水库注册所在行政区划
     */
    @Excel(name = "水库注册所在行政区划" , orderNum = "8")
    @NotBlank(message = "水库注册所在行政区划不能为空")
    private String administrationDivision;

    /**
     * 水库工程等别
     */
    @Excel(name = "水库工程等级" , orderNum = "9")
    @NotBlank(message = "水库工程等级不能为空")
    private String level;

    /**
     * 水库工程规模
     */
    @Excel(name = "水库工程规模" , orderNum = "10")
    @NotBlank(message = "水库工程规模不能为空")
    private String scale;

    /**
     * 总库容（万m3）
     */
    @Excel(name = "总库容(万m3)" , orderNum = "11")
    @NotNull(message = "总库容（万m3）不能为空")
    private Double totalStorageCapacity;

    /**
     * 调节库容（万m3）
     */
    @Excel(name = "调节库容(万m3)" , orderNum = "12")
    @NotNull(message = "调节库容（万m3）不能为空")
    private Double regulatingStorageCapacity;

    /**
     * 死库容（万m3）
     */
    @Excel(name = "死库容(万m3)" , orderNum = "13")
    @NotNull(message = "死库容（万m3）不能为空")
    private Double deadStorage;

    /**
     * 设计洪水位（m）
     */
    @Excel(name = "设计洪水位(m)" , orderNum = "14")
    @NotNull(message = "设计洪水位（m）不能为空")
    private Double designFloodLevel;

    /**
     * 正常蓄水位(m)
     */
    @Excel(name = "正常蓄水位(m)" , orderNum = "15")
    @NotNull(message = "正常蓄水位(m)不能为空")
    private Double normalStorageLevel;

    /**
     * 死水位（m）
     */
    @Excel(name = "死水位(m)" , orderNum = "16")
    @NotNull(message = "死水位（m）不能为空")
    private Double deadWaterLevel;

    /**
     * 建站年月
     */
    @Excel(name = "建站年月" , orderNum = "17", importFormat = "yyyy-MM", exportFormat = "yyyy-MM")
    @NotNull(message = "建站年月不能为空")
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM",timezone = "GMT+8")
    private Date date;

    /**
     * 管理单位
     */
    @Excel(name = "管理单位" , orderNum = "18")
    @NotBlank(message = "管理单位不能为空")
    private String manageUnit;

    /**
     * 供水范围
     */
    @Excel(name = "供水范围" , orderNum = "19")
    @NotBlank(message = "供水范围不能为空")
    private String waterSupplyArea;

    /**
     * 地图位置
     */
    @ExcelIgnore
    private String point;

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

    //检查字段是否不为空或者空白,有空或者空白返回true
    //全部字段都有值则返回false
    public boolean checkForEmptyFields() {
        return Arrays.asList(
                Optional.ofNullable(this.waterSupply).map(StringUtils::isBlank),
                Optional.ofNullable(this.code).map(StringUtils::isBlank),
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                this.latitude == null,
                this.longitude == null,
                Optional.ofNullable(this.locate).map(StringUtils::isBlank),
                Optional.ofNullable(this.registrationNumber).map(StringUtils::isBlank),
                Optional.ofNullable(this.administrationDivision).map(StringUtils::isBlank),
                Optional.ofNullable(this.level).map(StringUtils::isBlank),
                Optional.ofNullable(this.scale).map(StringUtils::isBlank),
                this.totalStorageCapacity == null,
                this.regulatingStorageCapacity == null,
                this.deadStorage == null,
                this.designFloodLevel == null,
                this.normalStorageLevel == null,
                this.deadWaterLevel == null,
                this.date == null,
                Optional.ofNullable(this.manageUnit).map(StringUtils::isBlank),
                Optional.ofNullable(this.waterSupplyArea).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }

}
