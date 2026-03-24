package com.jzsk.backendv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Web配置类
 * 职责：配置RestTemplate等Web客户端Bean
 */
@Configuration
public class WebConfig {

    /**
     * RestTemplate Bean
     * 用于外部API调用（如GNSS位移数据获取）
     * 配置连接超时5秒、读取超时10秒，防止外部接口阻塞任务线程
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        return new RestTemplate(factory);
    }
}
