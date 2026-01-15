package com.szy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.*;
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
 * 工程维护记录实体
 * @author admin
 * @date 2024/06/10 20:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MaintenceRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属供水工程
     */
    @Excel(name = "工程名称" , orderNum = "1")
    @NotBlank(message = "工程名称不能为空")
    private String name;

    /**
     * 水厂编码
     */
    @Excel(name = "工程编码" , orderNum = "2")
    @NotBlank(message = "工程编码不能为空")
    private String code;

    /**
     * 水厂名称
     */
    @Excel(name = "备注" , orderNum = "3")
    @NotBlank(message = "备注")
    private String note;

    /**
     * 负责人
     */
    @Excel(name = "负责人" , orderNum = "4")
    @NotBlank(message = "负责人不能为空")
    private String responsiblePerson;

    /**
     * 负责人电话
     */
    @Excel(name = "负责人电话" , orderNum = "5")
    @NotBlank(message = "负责人电话不能为空")
    private String phone;

    /**
     * 发生时间
     */
    @Excel(name = "开始维护时间" , orderNum = "6",importFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "开始维护时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束维护时间" , orderNum = "7",importFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date overTime;
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
                Optional.ofNullable(this.code).map(StringUtils::isBlank),
                Optional.ofNullable(this.note).map(StringUtils::isBlank),
                Optional.ofNullable(this.responsiblePerson).map(StringUtils::isBlank),
                Optional.ofNullable(this.phone).map(StringUtils::isBlank),
                this.startTime == null
        ).contains(Boolean.TRUE);
    }
}
