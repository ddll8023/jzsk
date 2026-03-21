package com.jzsk.backendv2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "jzsk.v2.task")
public class TaskProperties {

    private boolean warningEnabled = false;

    private boolean syncEnabled = false;

    private Map<String, Boolean> taskSwitches = new LinkedHashMap<>();

    private String zone = "Asia/Shanghai";

    private int poolSize = 4;

    private String threadNamePrefix = "jzsk-task-";

    private int shutdownAwaitSeconds = 30;
}
