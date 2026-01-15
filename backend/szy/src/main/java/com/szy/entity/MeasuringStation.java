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

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 监测站点
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MeasuringStation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 监测站点id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属供水工程
     */
    @Excel(name = "站码" , orderNum = "1")
    private String code;

    /**
     * 关联工程类型
     */
    @Excel(name = "站名" , orderNum = "2")
    private String name;

    /**
     * 工程站点编码
     */
    @Excel(name = "水系名称" , orderNum = "3")
    private String waterName;

    /**
     * 所属工程站点
     */
    @Excel(name = "河流名称" , orderNum = "4")
    private String riverName;

    /**
     * RTU站点
     */
    @Excel(name = "施测项目码" , orderNum = "5")
//    @TableField("RTU_site")
    private String monitorCode;

    /**
     * 工程监测站点编码
     */
    @Excel(name = "行政区划码" , orderNum = "6")
    private String addressCode;


    /**
     * 一句话描述该方法的作用
     * @author admin
     * @date 2024/06/29 19:28
     */
    @Excel(name = "设站年月" , orderNum = "7", importFormat = "yyyy-MM", exportFormat = "yyyy-MM")
    @NotNull(message = "设站年月不能为空")
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM",timezone = "GMT+8")
    private Date establishDate;


    /**
     * 经度
     */
    @Excel(name = "经度" , orderNum = "8")
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度" , orderNum = "9")
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    /**
     * 工程备注
     */
    @Excel(name = "备注" , orderNum = "10")
    private String note;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

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
                Optional.ofNullable(this.code).map(StringUtils::isBlank),
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                Optional.ofNullable(this.waterName).map(StringUtils::isBlank),
                Optional.ofNullable(this.riverName).map(StringUtils::isBlank),
                Optional.ofNullable(this.monitorCode).map(StringUtils::isBlank),
                Optional.ofNullable(this.addressCode).map(StringUtils::isBlank),
                this.establishDate == null,
                this.longitude == null,
                this.latitude == null
        ).contains(Boolean.TRUE);
    }

}
