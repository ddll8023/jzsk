package com.jzsk.backendv2.pojo.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户-角色关联实体类
 * 对应数据库表：sys_user_role
 * 用途：用户与角色的多对多关联映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 角色ID */
    private Long roleId;
}
