package com.szy.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色菜单关联实体
 */
@Data
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 角色ID */
    private Long roleId;

    /** 菜单ID */
    private Long menuId;
}