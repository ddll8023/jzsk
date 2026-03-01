package com.szy.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 用户详情响应
 */
@Data
public class UserDetailVO {

    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 姓名 */
    private String name;

    /** 用户类型 */
    private String type;

    /** 所属部门 */
    private String department;

    /** 手机号码 */
    private String phone;

    /** 电子邮箱 */
    private String email;

    /** 角色列表 */
    private List<RoleVO> roles;
}