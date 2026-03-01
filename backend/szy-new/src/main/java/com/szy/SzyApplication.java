package com.szy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 智慧水利应用启动类
 * 遵循KISS原则：简洁的启动配置
 */
@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class SzyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SzyApplication.class, args);
    }

}