package com.szy.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色权限关联实体（复用jcxx.role_authority表）
 */
@Data
public class RoleAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 角色ID */
    private Long roleId;

    /** 权限/菜单ID */
    private Long authorityId;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;
}
