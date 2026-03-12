package com.szy.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * 遵循规范：KISS - 保持简单
 * 核心配置在 application.yml 中完成
 */
@Configuration
@MapperScan("com.szy.mapper")
public class MybatisConfig {

    /**
     * Mapper接口扫描
     * 使用 @MapperScan 替代每个Mapper接口上的 @Mapper 注解
     * SQL语句在 XML 文件中定义（规范要求）
     */

}
