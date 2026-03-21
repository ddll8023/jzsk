package com.jzsk.backendv2.pojo.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "登录响应", description = "登录成功后的返回信息")
public class LoginResponseVO {

    @Schema(description = "JWT令牌")
    private String token;

    @Schema(description = "当前登录用户")
    private CurrentUserVO user;
}
