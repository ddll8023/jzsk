package com.szy.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 角色响应对象
 */
@Data
public class RoleVO {

    /** 角色ID */
    private Long id;

    /** 角色名称 */
    private String name;

    /** 角色编码 */
    private String code;

    /** 备注 */
    private String note;

    /** 状态 */
    private String status;

    /** 菜单ID列表 */
    private List<Long> menuIds;
}