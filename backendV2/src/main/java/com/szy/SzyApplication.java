package com.szy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 水闸信息管理系统 - 启动类
 * 遵循规范：KISS - 简洁启动入口
 */
@SpringBootApplication
@MapperScan("com.szy.mapper")
public class SzyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SzyApplication.class, args);
    }

}
