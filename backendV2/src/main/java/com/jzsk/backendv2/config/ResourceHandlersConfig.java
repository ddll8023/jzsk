package com.jzsk.backendv2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源映射配置
 * 用途：映射本地文件目录到URL路径，实现图片等静态资源的访问
 * 参考旧后端 WebMvcConfiguration.java 的实现
 */
@Configuration
public class ResourceHandlersConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源映射
     * 映射关系：
     * - /photo/** -> D:/tencent/szy_project/photo/ (巡检图片)
     * - /pic/** -> D:/pic/ (视频截图)
     * - /shipin/** -> D:/tencent/szy_project/shipin/ (项目视频)
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 巡检图片映射
        registry.addResourceHandler("/photo/**")
                .addResourceLocations("file:D:/tencent/szy_project/photo/");

        // 视频截图映射
        registry.addResourceHandler("/pic/**")
                .addResourceLocations("file:D:/pic/");

        // 项目视频映射
        registry.addResourceHandler("/shipin/**")
                .addResourceLocations("file:D:/tencent/szy_project/shipin/");

        // APP安装包映射
        registry.addResourceHandler("/app/**")
                .addResourceLocations("file:D:/app/");
    }
}
