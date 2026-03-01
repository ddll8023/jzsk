package com.szy.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色实体
 */
@Data
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 角色ID */
    private Long id;

    /** 角色名称 */
    private String name;

    /** 角色编码 */
    private String code;

    /** 备注 */
    private String note;

    /** 角色类型 */
    private String type;

    /** 状态（1启用 0禁用） */
    private String status;

    /** 菜单ID列表（非数据库字段） */
    private List<Long> menuIds = new ArrayList<>();
}