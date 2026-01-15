package com.szy.entity;

import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 工程站点-分水口
 * </p>
 *
 * @author l
 * @since 2022-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WaterDistributor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分水口编号
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分水口名称
     */
    @Excel(name = "分水口名称" , orderNum = "1")
    @NotBlank(message = "分水口名称不能为空")
    private String name;

    /**
     * 所属区县
     */
    @Excel(name = "所属区县" , orderNum = "2")
    @NotBlank(message = "所属区县不能为空")
    private String county;

    /**
     * 所属乡镇
     */
    @Excel(name = "所属乡镇" , orderNum = "3")
    @NotBlank(message = "所属乡镇不能为空")
    private String town;

    /**
     * 所属村庄
     */
    @Excel(name = "所属村庄" , orderNum = "4")
    @NotBlank(message = "所属村庄不能为空")
    private String village;

    /**
     * 分水口经度
     */
    @Excel(name = "分水口经度" , orderNum = "5")
    @NotNull(message = "分水口经度不能为空")
    private BigDecimal longitude;

    /**
     * 分水口纬度
     */
    @Excel(name = "分水口纬度" , orderNum = "6")
    @NotNull(message = "分水口纬度不能为空")
    private BigDecimal latitude;

    /**
     * 所属供水工程
     */
    @Excel(name = "所属供水工程" , orderNum = "7")
    @NotBlank(message = "所属供水工程不能为空")
    private String waterSupply;

    /**
     * 建站年月
     */
    @Excel(name = "建站年月" , orderNum = "8", importFormat = "yyyy-MM", exportFormat = "yyyy-MM")
    @NotNull(message = "建站年月不能为空")
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM",timezone = "GMT+8")
    private Date date;

    /**
     * 管理单位
     */
    @Excel(name = "管理单位" , orderNum = "9")
    @NotBlank(message = "管理单位不能为空")
    private String manageUnit;

    /**
     * 地址
     */
    @Excel(name = "地址" , orderNum = "10")
    @NotBlank(message = "地址不能为空")
    private String address;

    /**
     * 分水口RTU编码
     */
    @Excel(name = "分水口RTU编码" , orderNum = "11")
    @TableField("RTU_code")
    @NotBlank(message = "分水口RTU编码不能为空")
    private String rtuCode;

    /**
     * 备注
     */
    @Excel(name = "备注" , orderNum = "12")
    private String note;

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
                Optional.ofNullable(this.county).map(StringUtils::isBlank),
                Optional.ofNullable(this.town).map(StringUtils::isBlank),
                Optional.ofNullable(this.village).map(StringUtils::isBlank),
                this.latitude == null,
                this.longitude == null,
                Optional.ofNullable(this.waterSupply).map(StringUtils::isBlank),
                this.date == null,
                Optional.ofNullable(this.manageUnit).map(StringUtils::isBlank),
                Optional.ofNullable(this.address).map(StringUtils::isBlank),
                Optional.ofNullable(this.rtuCode).map(StringUtils::isBlank),
                Optional.ofNullable(this.note).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }

}
