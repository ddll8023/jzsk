package com.szy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名
     */
    @NotBlank(message = "角色名称不能为空")
    private String name;

    /**
     * 角色代码
     */
    @NotBlank(message = "角色编码不能为空")
    private String code;

    /**
     * 角色备注
     */
    private String note;

    /**
     * 角色类型
     */
    private String type;

    /**
     * 角色状态
     */
    @NotBlank(message = "状态不为空")
    private String status;

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
     * 菜单id列表
     */
    @TableField(exist = false)
    private List<Long> menusIds = new ArrayList<>();

}
