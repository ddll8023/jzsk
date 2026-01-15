package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

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
 *
 * </p>
 *
 * @author l
 * @since 2022-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MeasuringItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测项id
     * @author admin
     * @date 2024/06/30 18:51
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 测项编号
     * @author admin
     * @date 2024/06/30 18:51
     */
    @Excel(name = "测项编号" , orderNum = "1")
    @NotBlank(message = "测项编号不能为空")
    private String number;

    /**
     * 测项名称
     * @author admin
     * @date 2024/06/30 18:52
     */
    @Excel(name = "测项名称" , orderNum = "2")
    @NotNull(message = "测项名称不能为空")
    private String name ;

    /**
     * 测项单位
     */
    @Excel(name = "测项单位" , orderNum = "3")
    private String unit;


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
                this.number == null
        ).contains(Boolean.TRUE);
    }

}
