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
 * 巡检记录
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InspectionRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 巡检记录id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 位置
     */
    @Excel(name = "巡检站点" , orderNum = "1")
    @NotBlank(message = "巡检站点不能为空")
    private String project;

//    /**
//     * 位置
//     */
//    @Excel(name = "位置" , orderNum = "2")
//    @NotBlank(message = "位置不能为空")
//    private String position;

    /**
     * 经度
     */
    @Excel(name = "经度" , orderNum = "2")
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度" , orderNum = "3")
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;


    /**
     * 巡检类型
     */
    @Excel(name = "巡检类型" , orderNum = "4")
    @NotBlank(message = "巡检类型不能为空")
    private String type;

    /**
     * 异常情况
     * @author admin
     * @date 2024/07/19 10:52
     */
    @Excel(name = "异常情况" , orderNum = "5")
    @NotBlank(message = "异常情况不能为空")
    private String abnormal;

    /**
     * 巡检情况
     */
    @Excel(name = "巡检情况" , orderNum = "6")
    private String situation;

    @Excel(name = "处理状态" , orderNum = "7")
    private String solve;

    /**
     * 图片路径
     * @author admin
     * @date 2024/07/19 10:51
     */
    @Excel(name = "图片" , orderNum = "8")
    @TableField("image")
    private String image;

    /**
     * 巡检情况
     */
    @Excel(name = "负责人" , orderNum = "9")
    @NotBlank(message = "负责人不能为空")
    private String person;

    /**
     * 日期
     */
    @Excel(name = "日期" , orderNum = "10",format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "日期不能为空")
    private Date date;

    /**
     * 地图位置
     */
    @ExcelIgnore
    private String point;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    //检查字段是否不为空或者空白,有空或者空白返回true
    //全部字段都有值则返回false
    public boolean checkForEmptyFields() {
        return Arrays.asList(
                Optional.ofNullable(this.project).map(StringUtils::isBlank),
                this.latitude == null,
                this.longitude == null,
                this.date == null,
                Optional.ofNullable(this.type).map(StringUtils::isBlank),
                Optional.ofNullable(this.abnormal).map(StringUtils::isBlank),
                Optional.ofNullable(this.person).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }


}
