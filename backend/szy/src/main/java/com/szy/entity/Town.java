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
import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = false)
public class Town implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 村庄id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 村庄名称
     */
    @Excel(name = "名称" , orderNum = "1")
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 经度
     */
    @Excel(name = "经度" , orderNum = "2")
    @NotBlank(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度" , orderNum = "3")
    @NotBlank(message = "纬度不能为空")
    private BigDecimal latitude;

    /**
     * 现状人口
     */
    @Excel(name = "现状人口" , orderNum = "4")
    private Integer population;

    /**
     * 备注
     */
    @Excel(name = "备注" , orderNum = "5")
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
                Optional.ofNullable(this.name).map(StringUtils::isBlank),
                this.latitude == null,
                this.longitude == null
        ).contains(Boolean.TRUE);
    }


}
