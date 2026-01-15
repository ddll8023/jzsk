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
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员信息ID
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @Excel(name = "姓名" , orderNum = "1")
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 年龄
     */
    @Excel(name = "年龄" , orderNum = "2")
    @NotNull(message = "年龄不能为空")
    private Integer age;

    /**
     * 性别
     */
    @Excel(name = "性别" , orderNum = "3")
    @NotBlank(message = "性别不能为空")
    private String gender;

    /**
     * 电话
     */
    @Excel(name = "电话" , orderNum = "4")
    @NotBlank(message = "电话不能为空")
    private String phone;

    /**
     * 所属机构
     */
    @Excel(name = "所属机构" , orderNum = "5")
    @NotBlank(message = "所属机构不能为空")
    private String organization;

    /**
     * 职位
     */
    @Excel(name = "职位" , orderNum = "6")
    @NotBlank(message = "职位不能为空")
    private String position;

    /**
     * 职责
     */
    @Excel(name = "职责" , orderNum = "7")
    @NotBlank(message = "职责不能为空")
    private String duty;

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
                this.age == null,
                Optional.ofNullable(this.gender).map(StringUtils::isBlank),
                Optional.ofNullable(this.phone).map(StringUtils::isBlank),
                Optional.ofNullable(this.organization).map(StringUtils::isBlank),
                Optional.ofNullable(this.position).map(StringUtils::isBlank),
                Optional.ofNullable(this.duty).map(StringUtils::isBlank)
        ).contains(Boolean.TRUE);
    }

}
