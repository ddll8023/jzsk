package com.szy.config;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 多数据源配置类
 * 遵循规范：KISS - 保持简单
 * 数据源配置在application.yml中完成，此处仅启用配置属性
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DataSourceConfig {

    /**
     * DynamicDataSourceProperties 已自动配置
     * 所有数据源在 application.yml 中配置
     * 使用 @DS 注解在 Mapper 层切换数据源
     */
}
