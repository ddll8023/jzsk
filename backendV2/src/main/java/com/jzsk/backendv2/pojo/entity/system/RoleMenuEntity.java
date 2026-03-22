package com.jzsk.backendv2.pojo.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色-菜单关联实体类
 * 对应数据库表：sys_role_authority（authority字段实际存菜单ID）
 * 用途：角色与菜单的多对多关联映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 角色ID */
    private Long roleId;

    /** 菜单ID（authority字段） */
    private Long authorityId;
}
