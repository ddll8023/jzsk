package com.szy.entity;

import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.*;

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
 * 工程站点-蓄水池
 * </p>
 *
 * @author l
 * @since 2022-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Impoundment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 蓄水池id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属公司
     */
    @Excel(name = "所属公司" , orderNum = "1")
    @NotBlank(message = "所属公司不能为空")
    private String company;

    /**
     * 蓄水池站点名称
     */
    @Excel(name = "蓄水池站点名称" , orderNum = "2")
    @NotBlank(message = "蓄水池站点名称不能为空")
    private String name;

    /**
     * 地址
     */
    @Excel(name = "地址" , orderNum = "3")
    @NotBlank(message = "地址不能为空")
    private String address;

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
     * 建站年月
     */
    @Excel(name = "建站年月" , orderNum = "6", importFormat = "yyyy-MM", exportFormat = "yyyy-MM")
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM",timezone = "GMT+8")
    @NotNull(message = "建站年月不能为空")
    private Date date;

    /**
     * 管理单位
     */
    @Excel(name = "管理单位" , orderNum = "7")
    @NotBlank(message = "管理单位不能为空")
    private String manageUnit;

    /**
     * 蓄水池容积（m³）
     */
    @Excel(name = "蓄水池容积(m³)" , orderNum = "8")
    @NotNull(message = "蓄水池容积（m³）不能为空")
    private Double volume;

    /**
     * 蓄水池控制水位（m）
     */
    @Excel(name = "蓄水池控制水位(m)" , orderNum = "9")
    @NotNull(message = "蓄水池控制水位（m）不能为空")
    private Double controlWaterLevel;

    /**
     * 负责人
     */
    @Excel(name = "负责人" , orderNum = "10")
    @NotBlank(message = "负责人不能为空")
    private String responsiblePerson;

    /**
     * 负责人电话
     */
    @Excel(name = "负责人电话" , orderNum = "11")
    @NotBlank(message = "负责人电话不能为空")
    private String phone;

    /**
     * 蓄水池RTU编码
     */
    @Excel(name = "蓄水池RTU编码" , orderNum = "12")
    @NotBlank(message = "蓄水池RTU编码不能为空")
    @TableField("RTU_code")
    private String rtuCode;

    /**
     * 备注
     */
    @Excel(name = "备注" , orderNum = "13")
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
     * 图层位置
     */
    @ExcelIgnore
    private String point;

    //检查字段是否不为空或者空白,有空或者空白返回true
    //全部字段都有值则返回false
    public boolean checkForEmptyFields() {
        return Arrays.asList(
                Optional.ofNullable(this.company).map(StringUtils::isBlank),
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                Optional.ofNullable(this.address).map(StringUtils::isBlank),
                this.latitude == null,
                this.longitude == null,
                this.date == null,
                Optional.ofNullable(this.manageUnit).map(StringUtils::isBlank),
                this.volume == null,
                this.controlWaterLevel == null,
                Optional.ofNullable(this.responsiblePerson).map(StringUtils::isBlank),
                Optional.ofNullable(this.phone).map(StringUtils::isBlank),
                Optional.ofNullable(this.rtuCode).map(StringUtils::isBlank),
                Optional.ofNullable(this.note).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }
}
