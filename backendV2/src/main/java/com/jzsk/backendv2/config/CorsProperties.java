package com.jzsk.backendv2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "jzsk.v2.cors")
public class CorsProperties {

    private List<String> allowedOrigins = new ArrayList<>(Arrays.asList(
            "http://localhost:8084",
            "http://111.4.68.108:8084"
    ));

    private List<String> allowedMethods = new ArrayList<>(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
    ));

    private List<String> allowedHeaders = new ArrayList<>(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With"
    ));

    private boolean allowCredentials = true;

    private long maxAge = 3600L;
}
