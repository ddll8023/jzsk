package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户请求参数
 */
@Schema(name = "用户DTO", description = "用户创建/更新请求参数")
@Data
public class UserDTO {

    @Schema(description = "用户ID", example = "1", required = false)
    private Long id;

    @Schema(description = "用户名", example = "admin01", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过50个字符")
    private String username;

    @Schema(description = "姓名", example = "张三", required = true)
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    @Schema(description = "用户类型", example = "系统管理员", required = false)
    private String type;

    @Schema(description = "所属部门", example = "管理部门", required = false)
    private String department;

    @Schema(description = "手机号码", example = "13800138000", required = false)
    @Size(max = 20, message = "手机号码长度不能超过20个字符")
    private String phoneNumber;

    @Schema(description = "电子邮箱", example = "test@example.com", required = false)
    @Size(max = 100, message = "电子邮箱长度不能超过100个字符")
    private String email;

    @Schema(description = "角色ID列表", example = "[1,2,3]", required = false)
    private List<Long> roleIds;
}
