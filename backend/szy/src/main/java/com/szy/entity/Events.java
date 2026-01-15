package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * 事件中心
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Events implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件中心id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 事件级别
     */
    @Excel(name = "事件级别" , orderNum = "1")
    @NotBlank(message = "事件级别不能为空")
    private String level;

    /**
     * 事件对象
     */
    @Excel(name = "事件对象" , orderNum = "2")
    @NotBlank(message = "事件对象不能为空")
    private String object;

    /**
     * 异常情况
     */
    @Excel(name = "异常情况" , orderNum = "3")
    @NotBlank(message = "异常情况不能为空")
    private String situation;

    /**
     * 上报时间
     */
    @Excel(name = "上报时间" , orderNum = "4",importFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "上报时间不能为空")
    private Date time;

    /**
     * 事件状态
     */
    @Excel(name = "事件状态" , orderNum = "5")
    @NotBlank(message = "事件状态不能为空")
    private String status;

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
                Optional.ofNullable(this.level).map(StringUtils::isBlank),
                Optional.ofNullable(this.object).map(StringUtils::isBlank),
                Optional.ofNullable(this.situation).map(StringUtils::isBlank),
                this.time == null,
                Optional.ofNullable(this.status).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }


}
