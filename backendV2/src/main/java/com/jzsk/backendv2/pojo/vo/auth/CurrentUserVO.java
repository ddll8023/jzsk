package com.jzsk.backendv2.pojo.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "当前用户", description = "当前登录用户上下文")
public class CurrentUserVO {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "显示名称", example = "系统管理员")
    private String displayName;

    @Schema(description = "权限列表")
    private List<String> authorities;
}
