package com.jzsk.backendv2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "JWT secret must not be blank")
    private String secret;

    private List<String> permitAll = new ArrayList<>(Arrays.asList(
            "/actuator/health",
            "/error",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/photo/**",
            "/pic/**",
            "/shipin/**",
            "/app/**"
    ));
}
