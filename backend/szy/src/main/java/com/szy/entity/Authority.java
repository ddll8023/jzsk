package com.szy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 权限信息
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限id（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 权限代码
     */
    @NotBlank(message = "资源编码不能为空")
    private String code;

    /**
     * 父资源
     */
    @NotNull(message = "父资源不能为空")
    @TableField("subsystemID")
    private Long subsystemid;

    /**
     * 资源地址
     */
    private String path;

    /**
     * 资源类型
     */
    private String type;

    /**
     * 资源顺序
     */
    @NotNull(message = "资源顺序不能为空")
    @TableField("orderNum")
    private Integer ordernum;

    /**
     * 资源状态
     */
    @NotBlank(message = "资源状态不能为空")
    private String status;

    /**
     * 资源备注
     */
    private String note;

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

    /**
     * 子系统
     */
    @TableField(exist = false)
    private List<Authority> children = new ArrayList<>();

}
