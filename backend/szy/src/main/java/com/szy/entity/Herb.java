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
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * 消毒药材
 * @author admin
 * @date 2024/08/13 16:15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Herb implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 药材ID
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 药材名称
     */
    @Excel(name = "名称" , orderNum = "2")
    @NotBlank(message = "不能为空")
    private String name;

    /**
     * 存储条件
     */
    @Excel(name = "存储条件" , orderNum = "3")
    @NotBlank(message = "存储条件不能为空")
    private String storageConditions;

    /**
     * 生产日期
     */
    @Excel(name = "生产日期", orderNum = "4", importFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "生产日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date productionDate;

    /**
     * 有效期
     * @author admin
     * @date 2024/06/16 16:51
     */
    @Excel(name = "有效期" , orderNum = "5")
    @NotBlank(message = "有效期不能为空")
    private String expiryDate;

    /**
     * 备注
     * @author admin
     * @date 2024/06/16 16:51
     */
    @Excel(name = "备注" , orderNum = "6")
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
                Optional.ofNullable(this.storageConditions).map(StringUtils::isBlank),
                this.productionDate == null,
                Optional.ofNullable(this.expiryDate).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }
}
