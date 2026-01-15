package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * 水位监测
 * @author admin
 * @date 2024/06/16 16:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WaterLevel implements Serializable {

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
    @Excel(name = "监测点" , orderNum = "2")
    @NotBlank(message = "监测地点不能为空")
    private String position;

    /**
     * 监测地点
     */
    @Excel(name = "站码" , orderNum = "3")
    @NotBlank(message = "站码不能为空")
    private String code;

    /**
     * 监测值
     * @author admin
     * @date 2024/06/16 16:51
     */
    @Excel(name = "监测值(m)" , orderNum = "4")
    @NotBlank(message = "监测值不能为空")
    private Double value;

    /**
     * 监测时间
     */
    @Excel(name = "监测时间" , orderNum = "5",importFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "监测时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date monitorTime;

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
                Optional.ofNullable(this.position).map(StringUtils::isBlank),
                Optional.ofNullable(this.code).map(StringUtils::isBlank),
                this.value == null,
                this.monitorTime == null
        ).contains(Boolean.TRUE);
    }
}
