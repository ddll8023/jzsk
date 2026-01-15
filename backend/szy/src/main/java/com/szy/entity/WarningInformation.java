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
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 预警信息
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WarningInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警信息id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 监测地点
     */
    @Excel(name = "预警地点" , orderNum = "2")
    @NotBlank(message = "预警地点不能为空")
    private String position;

    /**
     * 经度
     */
    @Excel(name = "经度" , orderNum = "3")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度" , orderNum = "4")
    private BigDecimal latitude;

    /**
     * 所属工程
     */
    @Excel(name = "所属工程" , orderNum = "5")
    private String project;

    /**
     * 预警内容
     */
    @Excel(name = "预警内容" , orderNum = "6")
    @NotBlank(message = "预警内容不能为空")
    private String content;

    /**
     * 预警类型
     */
    @Excel(name = "预警类型" , orderNum = "7")
    @NotBlank(message = "预警类型不能为空")
    private String type;

    /**
     * 预警等级
     */
    @Excel(name = "预警等级" , orderNum = "8")
    @NotBlank(message = "预警等级不能为空")
    private String level;

    /**
     * 预警状态
     */
    @Excel(name = "预警状态" , orderNum = "9")
    @NotBlank(message = "预警状态不能为空")
    private String status;

    /**
     * 预警时间
     */
    @Excel(name = "发生时间" , orderNum = "10",importFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "发生时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "解除时间" , orderNum = "11",importFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date overTime;

    /**
     * 持续时长
     */
    @ExcelIgnore
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String stayTime;

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
    //只校验position、content、type、level、status、startTime
    public boolean checkForEmptyFields() {
        return Arrays.asList(
                Optional.ofNullable(this.position).map(StringUtils::isBlank),
                Optional.ofNullable(this.content).map(StringUtils::isBlank),
                Optional.ofNullable(this.type).map(StringUtils::isBlank),
                Optional.ofNullable(this.level).map(StringUtils::isBlank),
                Optional.ofNullable(this.status).map(StringUtils::isBlank),
                this.startTime == null
        ).contains(Boolean.TRUE);
    }
}
