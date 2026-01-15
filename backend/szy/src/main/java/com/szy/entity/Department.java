package com.szy.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 部门
 * </p>
 *
 * @author l
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    private String departmentName;

    /**
     * 部门职责
     */
    @NotBlank(message = "部门职责不能为空")
    private String departmentResponsibility;

    /**
     * 部门级别
     */
    private String level;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
