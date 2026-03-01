package com.szy.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色关联实体
 */
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 角色ID */
    private Long roleId;
}