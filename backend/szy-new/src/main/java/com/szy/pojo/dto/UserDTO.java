package com.szy.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户请求参数
 */
@Data
public class UserDTO {

    /** 用户ID（更新时必填） */
    private Long id;

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 姓名 */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /** 用户类型 */
    private String type;

    /** 所属部门 */
    private String department;

    /** 手机号码 */
    private String phone;

    /** 电子邮箱 */
    private String email;

    /** 角色ID列表 */
    private List<Long> roleIds;
}