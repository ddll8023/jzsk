package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = false)
public class FloatingBoat implements Serializable {

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
    @Excel(name = "名称" , orderNum = "2")
    @NotBlank(message = "浮船名称不能为空")
    private String name;

    /**
     * 监测地点
     */
    @Excel(name = "经度" , orderNum = "3")
    @NotBlank(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 监测值
     * @author admin
     * @date 2024/06/16 16:51
     */
    @Excel(name = "纬度" , orderNum = "4")
    @NotBlank(message = "纬度不能为空")
    private BigDecimal latitude;

    /**
     * 监测时间
     */
    @Excel(name = "容量(m³/h)" , orderNum = "5")
    @NotBlank(message = "容量不能为空")
    private Double capacity;


    /**
     * 监测时间
     */
    @Excel(name = "能耗(kw/h)" , orderNum = "6")
    @NotBlank(message = "能耗不能为空")
    private Double powerConsumption;

    /**
     * 监测时间
     */
    @Excel(name = "抽水状态" , orderNum = "7")
    @NotBlank(message = "抽水状态不能为空")
    private String status;

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
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                Optional.ofNullable(this.status).map(StringUtils::isBlank),
                this.longitude == null,
                this.latitude == null,
                this.capacity == null,
                this.powerConsumption == null
        ).contains(Boolean.TRUE);
    }
}
