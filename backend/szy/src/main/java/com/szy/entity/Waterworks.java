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
 * 工程站点-水厂
 * </p>
 *
 * @author l
 * @since 2022-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Waterworks implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水厂id
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
     * 水厂编码
     */
    @Excel(name = "水厂编码" , orderNum = "2")
    @NotBlank(message = "水厂编码不能为空")
    private String code;

    /**
     * 水厂名称
     */
    @Excel(name = "水厂名称" , orderNum = "3")
    @NotBlank(message = "水厂名称不能为空")
    private String name;

    /**
     * 地址
     */
    @Excel(name = "地址" , orderNum = "4")
    @NotBlank(message = "地址不能为空")
    private String address;

    /**
     * 管理单位
     */
    @Excel(name = "管理单位" , orderNum = "5")
    @NotBlank(message = "管理单位不能为空")
    private String manageUnit;

    /**
     * 经度
     */
    @Excel(name = "经度" , orderNum = "6")
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度" , orderNum = "7")
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    /**
     * 设计规模（m³/天）
     */
    @Excel(name = "设计规模(m³/天)" , orderNum = "8")
    @NotNull(message = "设计规模（m³/天）不能为空")
    private Double designScale;

    /**
     * 供水范围（村镇）
     */
    @Excel(name = "供水范围(村镇)" , orderNum = "9")
    @NotBlank(message = "供水范围（村镇）不能为空")
    private String waterSupplyRange;

    /**
     * 供水负荷率（%）
     */
    @Excel(name = "供水负荷率(%)" , orderNum = "10")
    @NotNull(message = "供水负荷率（%）不能为空")
    private Double waterSupplyLoadRate;

    /**
     * 供水人口（万人）
     */
    @Excel(name = "供水人口(万人)" , orderNum = "11")
    @NotNull(message = "供水人口（万人）不能为空")
    private Double population;

    /**
     * 负责人
     */
    @Excel(name = "负责人" , orderNum = "12")
    @NotBlank(message = "负责人不能为空")
    private String responsiblePerson;

    /**
     * 负责人电话
     */
    @Excel(name = "负责人电话" , orderNum = "13")
    @NotBlank(message = "负责人电话不能为空")
    private String phone;

    /**
     * 建站年月
     */
    @Excel(name = "建站年月" , orderNum = "14", importFormat = "yyyy-MM", exportFormat = "yyyy-MM")
    @NotNull(message = "建站年月不能为空")
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM",timezone = "GMT+8")
    private Date date;

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
                Optional.ofNullable(this.waterSupply).map(StringUtils::isBlank),
                Optional.ofNullable(this.code).map(StringUtils::isBlank),
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                Optional.ofNullable(this.address).map(StringUtils::isBlank),
                Optional.ofNullable(this.manageUnit).map(StringUtils::isBlank),
                this.latitude == null,
                this.longitude == null,
                this.designScale == null,
                Optional.ofNullable(this.waterSupplyRange).map(StringUtils::isBlank),
                this.waterSupplyLoadRate == null,
                this.population == null,
                Optional.ofNullable(this.responsiblePerson).map(StringUtils::isBlank),
                Optional.ofNullable(this.phone).map(StringUtils::isBlank),
                this.date == null
        ).contains(Boolean.TRUE);
    }
}
