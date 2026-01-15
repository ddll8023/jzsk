package com.szy.entity;

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
 * 供水工程
 * </p>
 *
 * @author l
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WaterSupplyProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供水工程id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 供水工程名
     */
    @Excel(name = "供水工程名" , orderNum = "1")
    @NotBlank(message = "供水工程名不能为空")
    private String projectName;

    /**
     * 供水工程代码
     */
    @Excel(name = "供水工程代码" , orderNum = "2")
    @NotBlank(message = "供水工程代码不能为空")
    private String code;

    /**
     * 工程类型
     */
    @Excel(name = "工程类型" , orderNum = "3")
    @NotBlank(message = "工程类型不能为空")
    private String type;

    /**
     * 所属分区
     */
    @Excel(name = "所属分区" , orderNum = "4")
    @NotBlank(message = "所属分区不能为空")
    private String zone;

    /**
     * 设计日供水规模
     */
    @Excel(name = "设计日供水规模(m³)" , orderNum = "5")
    @NotNull(message = "设计日供水规模不能为空")
    private Double designSupply;

    /**
     * 实际日供水规模
     */
    @Excel(name = "实际日供水规模(m³)" , orderNum = "6")
    @NotNull(message = "实际日供水规模不能为空")
    private Double actualSupple;

    /**
     * 设计供水人口
     */
    @Excel(name = "设计供水人口(万人)" , orderNum = "7")
    @NotNull(message = "设计供水人口不能为空")
    private Double designPopulation;

    /**
     * 收益行政村数量
     */
    @Excel(name = "受益行政村数量" , orderNum = "8")
    @NotNull(message = "收益行政村数量不能为空")
    private Integer village;

    /**
     * 收益人口
     */
    @Excel(name = "受益人口(万人)" , orderNum = "9")
    @NotNull(message = "收益人口不能为空")
    private Double population;

    /**
     * 供水户数
     */
    @Excel(name = "供水户数(万人)" , orderNum = "10")
    @NotNull(message = "供水户数不能为空")
    private Double supplyNumber;

    /**
     * 工程主管部门
     */
    @Excel(name = "工程主管部门" , orderNum = "11")
    @NotBlank(message = "工程主管部门不能为空")
    private String manageDepartment;

    /**
     * 工程管理单位
     */
    @Excel(name = "工程管理单位" , orderNum = "12")
    @NotBlank(message = "工程管理单位不能为空")
    private String manageUnit;

    /**
     * 供水范围
     */
    @Excel(name = "供水范围" , orderNum = "13")
    @NotBlank(message = "供水范围不能为空")
    private String area;

    /**
     * 备注
     */
    @Excel(name = "备注" , orderNum = "14")
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

    //检查字段是否不为空或者空白,有空或者空白返回true
    //全部字段都有值则返回false
    public boolean checkForEmptyFields() {
        return Arrays.asList(
                Optional.ofNullable(this.projectName).map(StringUtils::isBlank),
                Optional.ofNullable(this.code).map(StringUtils::isBlank),
                Optional.ofNullable(this.type).map(StringUtils::isBlank),
                Optional.ofNullable(this.zone).map(StringUtils::isBlank),
                this.designSupply == null,
                this.actualSupple == null,
                this.designPopulation == null,
                this.village == null,
                this.population == null,
                this.supplyNumber == null,
                Optional.ofNullable(this.manageDepartment).map(StringUtils::isBlank),
                Optional.ofNullable(this.manageUnit).map(StringUtils::isBlank),
                Optional.ofNullable(this.area).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }


}
