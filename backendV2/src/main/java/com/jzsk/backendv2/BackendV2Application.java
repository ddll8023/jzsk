package com.jzsk.backendv2;

import com.jzsk.backendv2.config.AliOSSProperties;
import com.jzsk.backendv2.config.CorsProperties;
import com.jzsk.backendv2.config.LoggingProperties;
import com.jzsk.backendv2.config.SecurityProperties;
import com.jzsk.backendv2.config.TaskProperties;
import com.jzsk.backendv2.config.V2Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        V2Properties.class,
        TaskProperties.class,
        LoggingProperties.class,
        SecurityProperties.class,
        CorsProperties.class,
        AliOSSProperties.class
})
public class BackendV2Application {

    public static void main(String[] args) {
        SpringApplication.run(BackendV2Application.class, args);
    }
}
