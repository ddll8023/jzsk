package com.jzsk.backendv2.pojo.dto.system.user;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "用户分页查询请求", description = "分页查询用户列表的请求参数")
public class UserPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "用户名（模糊搜索）", example = "admin")
    private String username;

    @Schema(description = "姓名（模糊搜索）", example = "张三")
    private String name;

    @Schema(description = "部门", example = "技术部")
    private String department;

    @Schema(description = "用户类型", example = "只读用户")
    private String type;
}
