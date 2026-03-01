package com.szy.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 角色请求参数
 */
@Data
public class RoleDTO {

    /** 角色ID（更新时必填） */
    private Long id;

    /** 角色名称 */
    @NotBlank(message = "角色名称不能为空")
    private String name;

    /** 角色编码 */
    @NotBlank(message = "角色编码不能为空")
    private String code;

    /** 备注 */
    private String note;

    /** 状态 */
    private String status;
}