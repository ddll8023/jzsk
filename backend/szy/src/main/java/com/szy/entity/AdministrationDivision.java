package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.*;

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
 * 行政区划信息
 * </p>
 *
 * @author l
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AdministrationDivision implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行政区划id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 供水管理单位
     */
    @Excel(name = "供水管理单位" , orderNum = "1")
    @NotBlank(message = "供水管理单位不能为空")
    private String waterSupply;

    /**
     * 所属乡镇
     */
    @Excel(name = "所属乡镇" , orderNum = "2")
    @NotBlank(message = "所属乡镇不能为空")
    private String townBelong;

    /**
     * 乡镇行政区划代码
     */
    @Excel(name = "乡镇行政区划代码" , orderNum = "3")
    @NotBlank(message = "乡镇行政区划代码不能为空")
    private String townCode;

    /**
     * 所属村
     */
    @Excel(name = "所属村" , orderNum = "4")
    @NotBlank(message = "所属村不能为空")
    private String villageBelong;

    /**
     * 村行政区划代码
     */
    @Excel(name = "村行政区划代码" , orderNum = "5")
    @NotBlank(message = "村行政区划代码不能为空")
    private String villageCode;

    /**
     * 户数
     */
    @Excel(name = "户数" , orderNum = "6")
    @NotNull(message = "户数不能为空")
    private Integer households;

    /**
     * 人口
     */
    @Excel(name = "人口" , orderNum = "7")
    @NotNull(message = "人口不能为空")
    private Integer population;

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
                Optional.ofNullable(this.townBelong).map(StringUtils::isBlank),
                Optional.ofNullable(this.townCode).map(StringUtils::isBlank),
                Optional.ofNullable(this.villageBelong).map(StringUtils::isBlank),
                Optional.ofNullable(this.villageCode).map(StringUtils::isBlank),
                this.households == null,
                this.population == null
        ).contains(Boolean.TRUE);

    }
}
