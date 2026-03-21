package com.jzsk.backendv2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jzsk.v2")
public class V2Properties {

    private String projectName = "JZSK Backend V2";

    private String apiPrefix = "/api";
}
