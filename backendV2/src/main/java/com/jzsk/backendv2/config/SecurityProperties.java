package com.jzsk.backendv2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "jzsk.v2.security")
public class SecurityProperties {

    private String header = "Authorization";

    private String tokenPrefix = "Bearer ";

    private long expireSeconds = 604800L;

    @NotBlank(message = "JWT密钥不能为空")
    private String secret;

    private List<String> permitAll = new ArrayList<>(Arrays.asList(
            "/actuator/health",
            "/error"
    ));

    @Valid
    private BootstrapUser bootstrapUser = new BootstrapUser();

    @Getter
    @Setter
    public static class BootstrapUser {

        private Long userId = 1L;

        @NotBlank(message = "Bootstrap用户名不能为空")
        private String username;

        @NotBlank(message = "Bootstrap密码不能为空")
        private String password;

        @NotBlank(message = "Bootstrap显示名称不能为空")
        private String displayName;

        @NotEmpty(message = "Bootstrap权限列表不能为空")
        private List<String> authorities = new ArrayList<>(Arrays.asList("ROLE_ADMIN", "system:manage"));
    }
}
