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
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 工程站点-泵站
 * </p>
 *
 * @author l
 * @since 2022-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PumpStation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 泵站id(主键)
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属供水工程
     */
    @Excel(name = "所属供水工程" , orderNum = "4")
    @NotBlank(message = "所属供水工程不能为空")
    private String waterSupplyProject;

    /**
     * 泵站编码
     */
    @Excel(name = "泵站编码" , orderNum = "2")
    @NotBlank(message = "泵站编码不能为空")
    private String code;

    /**
     * 泵站名称
     */
    @Excel(name = "泵站名称" , orderNum = "1")
    @NotBlank(message = "泵站名称不能为空")
    private String name;

    /**
     * 泵站类型
     * @author admin
     * @date 2024/06/07 17:50
     */
    @Excel(name = "泵站类型" , orderNum = "3")
    @NotBlank(message = "泵站编码不能为空")
    private String type;

    /**
     * 所属管理公司
     */
    @Excel(name = "泵站状态" , orderNum = "5")
    @NotBlank(message = "泵站状态不能为空")
    private String company;

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
     * 地址
     */
    @Excel(name = "地址" , orderNum = "8")
    @NotBlank(message = "地址不能为空")
    private String address;

    /**
     * 运行方式（几备几用）
     */
    @Excel(name = "运行方式(几备几用)" , orderNum = "9")
    @NotBlank(message = "运行方式（几备几用）不能为空")
    private String operationMode;

    /**
     * 机组数量（台）
     */
    @Excel(name = "机组数量(台)" , orderNum = "10")
    @NotNull(message = "机组数量（台）不能为空")
    private Integer number;

    /**
     * 设计规模（m³/天）
     */
    @Excel(name = "设计规模(m³/天)" , orderNum = "11")
    @NotNull(message = "设计规模（m³/天）不能为空")
    private Double designScale;

    /**
     * 装机容量（kw）
     */
    @Excel(name = "装机容量(kw)" , orderNum = "12")
    @NotNull(message = "装机容量（kw）不能为空")
    private Double installedCapacity;

    /**
     * 扬程
     */
    @Excel(name = "扬程" , orderNum = "13")
    @NotNull(message = "扬程不能为空")
    private Double lift;

    /**
     * 建站年月
     */
    @NotNull(message = "建站年月不能为空")
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM",timezone = "GMT+8")
    @Excel(name = "建站年月", orderNum = "14", importFormat = "yyyy-MM")
    private Date date;

//    /**
//     * 地图位置
//     */
//    @ExcelIgnore
//    private String point;

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
                Optional.ofNullable(this.waterSupplyProject).map(StringUtils::isBlank),
                Optional.ofNullable(this.code).map(StringUtils::isBlank),
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                Optional.ofNullable(this.company).map(StringUtils::isBlank),
                this.latitude == null,
                this.longitude == null,
                Optional.ofNullable(this.address).map(StringUtils::isBlank),
                Optional.ofNullable(this.operationMode).map(StringUtils::isBlank),
                this.number == null,
                this.designScale == null,
                this.installedCapacity == null,
                this.lift == null,
                this.date == null
        ).contains(Boolean.TRUE);
    }
}
