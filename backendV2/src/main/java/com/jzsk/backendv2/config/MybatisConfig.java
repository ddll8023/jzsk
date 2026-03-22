package com.jzsk.backendv2.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jzsk.backendv2.mapper")
public class MybatisConfig {
}
