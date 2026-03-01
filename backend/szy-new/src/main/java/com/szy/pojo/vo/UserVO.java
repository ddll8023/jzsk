package com.szy.pojo.vo;

import lombok.Data;

/**
 * 用户信息VO
 */
@Data
public class UserVO {

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
}