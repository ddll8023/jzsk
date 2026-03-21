package com.jzsk.backendv2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jzsk.v2.logging")
public class LoggingProperties {

    private boolean requestEnabled = true;

    private String traceHeader = "X-Trace-Id";
}
