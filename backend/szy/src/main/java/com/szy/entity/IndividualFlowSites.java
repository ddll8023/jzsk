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
 * 单独流量测站
 * </p>
 *
 * @author l
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class IndividualFlowSites implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单独流量站点
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流量测站名称
     */
    @Excel(name = "流量测站名称" , orderNum = "1")
    @NotBlank(message = "流量测站名称不能为空")
    private String name;

    /**
     * 测点编号
     */
    @Excel(name = "测点编号" , orderNum = "2")
    @NotBlank(message = "测点编号不能为空")
    private String stationNumber;

    /**
     * RTU编号
     */
    @Excel(name = "RTU编号" , orderNum = "3")
    @TableField("RTU_code")
    @NotBlank(message = "RTU编号不能为空")
    private String rtuCode;

    /**
     * 测站要素
     */
    @Excel(name = "测站要素" , orderNum = "4")
    @NotBlank(message = "测站要素不能为空")
    private String measuringStationsElements;

    /**
     * 拆分站点编码
     */
    @Excel(name = "拆分站点编码" , orderNum = "5")
    @NotBlank(message = "拆分站点编码不能为空")
    private String splitSiteCode;

    /**
     * 所在地址
     */
    @Excel(name = "所在地址" , orderNum = "6")
    @NotBlank(message = "所在地址不能为空")
    private String address;

    /**
     * 站点经度
     */
    @Excel(name = "站点经度" , orderNum = "7")
    @NotNull(message = "站点经度不能为空")
    private BigDecimal longitude;

    /**
     * 站点纬度
     */
    @Excel(name = "站点纬度" , orderNum = "8")
    @NotNull(message = "站点纬度不能为空")
    private BigDecimal latitude;

    /**
     * 预警上上限
     */
    @Excel(name = "上上限", groupName = "预警指标范围", orderNum = "9")
    @NotNull(message = "预警上上限不能为空")
    private Double upUpLimit;

    /**
     * 预警上限
     */
    @Excel(name = "上限", orderNum = "10")
    @NotNull(message = "预警上限不能为空")
    private Double upLimit;

    /**
     * 预警下限
     */
    @Excel(name = "下限" ,orderNum = "11")
    @NotNull(message = "预警下限不能为空")
    private Double lowLimit;

    /**
     * 预警下下限
     */
    @Excel(name = "下下限" , orderNum = "12")
    @NotNull(message = "预警下下限不能为空")
    private Double lowerLimit;

    /**
     * 备注
     */
    @Excel(name = "备注" , orderNum = "13")
    private String note;

    /**
     * 地图位置
     */
    @ExcelIgnore
    private String point;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //检查字段是否不为空或者空白,有空或者空白返回true
    //全部字段都有值则返回false
    public boolean checkForEmptyFields() {
        return Arrays.asList(
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                Optional.ofNullable(this.stationNumber).map(StringUtils::isBlank),
                Optional.ofNullable(this.rtuCode).map(StringUtils::isBlank),
                Optional.ofNullable(this.measuringStationsElements).map(StringUtils::isBlank),
                Optional.ofNullable(this.splitSiteCode).map(StringUtils::isBlank),
                Optional.ofNullable(this.address).map(StringUtils::isBlank),
                this.latitude == null,
                this.longitude == null,
                this.upUpLimit == null,
                this.upLimit == null,
                this.lowLimit == null,
                this.lowerLimit == null,
                Optional.ofNullable(this.note).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }


}
