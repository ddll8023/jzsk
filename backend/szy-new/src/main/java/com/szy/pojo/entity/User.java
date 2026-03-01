package com.szy.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

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
}