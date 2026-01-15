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
 * 地下水源水
 * </p>
 *
 * @author l
 * @since 2022-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GroundSourceWater implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地下水源水id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 水源地名称
     */
    @Excel(name = "水源地名称", orderNum = "1")
    @NotBlank(message = "水源地名称不能为空")
    private String name;

    /**
     * 水源地面积
     */
    @Excel(name = "水源地面积", orderNum = "2")
    @NotNull(message = "水源地面积不能为空")
    private Double area;

    /**
     * 经度
     */
    @Excel(name = "经度", orderNum = "3")
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度", orderNum = "4")
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    /**
     * 水质目标
     */
    @Excel(name = "水质目标", orderNum = "5")
    @NotBlank(message = "水质目标不能为空")
    private String waterQualityGoals;

    /**
     * 运行状况
     */
    @Excel(name = "运行状况", orderNum = "6")
    @NotBlank(message = "运行状况不能为空")
    private String status;

    /**
     * 供水对象
     */
    @Excel(name = "供水对象", orderNum = "7")
    @NotBlank(message = "供水对象不能为空")
    private String supplyObject;

    /**
     * 多年平均可开采量
     */
    @Excel(name = "多年平均可开采量", orderNum = "8")
    @NotNull(message = "多年平均可开采量不能为空")
    private Double averageMineableVolume;

    /**
     * 年许可取水量
     */
    @Excel(name = "年许可取水量", orderNum = "9")
    @NotNull(message = "年许可取水量不能为空")
    private Double annualPermittedWithdrawal;

    /**
     * 管理单位
     */
    @Excel(name = "管理单位", orderNum = "10")
    @NotBlank(message = "管理单位不能为空")
    private String management;

    /**
     * 是否为应急水源地
     */
    @Excel(name = "是否为应急水源地", orderNum = "11")
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
     * 图层位置
     */
    @ExcelIgnore
    private String point;

    //检查字段是否不为空或者空白,有空或者空白返回true
    //全部字段都有值则返回false
    public boolean checkForEmptyFields() {
        return Arrays.asList(
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                this.area == null,
                this.latitude == null,
                this.longitude == null,
                Optional.ofNullable(this.waterQualityGoals).map(StringUtils::isBlank),
                Optional.ofNullable(this.status).map(StringUtils::isBlank),
                Optional.ofNullable(this.supplyObject).map(StringUtils::isBlank),
                this.averageMineableVolume == null,
                this.annualPermittedWithdrawal == null,
                Optional.ofNullable(this.management).map(StringUtils::isBlank),
                Optional.ofNullable(this.whetherEmergencySource).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }
}
