# 后端重构实施计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 创建新的后端项目，完全按照前端V2的API需求实现，使用原生MyBatis替代MyBatis Plus

**Architecture:** 分层架构：Controller → Service → Mapper → Entity，强制构造器注入，统一响应格式，多数据源支持

**Tech Stack:** Spring Boot 2.6.2, MyBatis, PageHelper, dynamic-datasource, Spring Security + JWT, Knife4j

---

## 阶段一：项目初始化

### Task 1: 创建项目目录结构

**Files:**
- Create: `backend/szy-new/` (项目根目录)
- Create: `backend/szy-new/pom.xml`
- Create: `backend/szy-new/src/main/java/com/szy/`
- Create: `backend/szy-new/src/main/resources/`
- Create: `backend/szy-new/src/test/java/com/szy/`

**Step 1: 创建Maven项目pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.2</version>
        <relativePath/>
    </parent>

    <groupId>com</groupId>
    <artifactId>szy-new</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>szy-new</name>
    <description>智慧水利后端重构版</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- MyBatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.2</version>
        </dependency>

        <!-- PageHelper -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.4.6</version>
        </dependency>

        <!-- 多数据源 -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
            <version>3.2.1</version>
        </dependency>

        <!-- 数据库驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.5.4</version>
        </dependency>
        <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
            <version>9.4.0.jre8</version>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Hutool -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.21</version>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.1</version>
        </dependency>

        <!-- Swagger/Knife4j -->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-spring-boot-starter</artifactId>
            <version>3.0.3</version>
        </dependency>

        <!-- 测试 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

**Step 2: 创建启动类**

文件: `backend/szy-new/src/main/java/com/szy/SzyApplication.java`

```java
package com.szy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 智慧水利后端应用
 */
@SpringBootApplication
public class SzyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SzyApplication.class, args);
    }
}
```

**Step 3: 验证项目可启动**

```bash
cd backend/szy-new && mvn spring-boot:run
```

Expected: 应用启动成功（可能会报缺少配置，这是正常的）

**Step 4: 提交**

```bash
git add backend/szy-new/pom.xml backend/szy-new/src/main/java/com/szy/SzyApplication.java
git commit -m "feat: 初始化后端项目结构"
```

---

### Task 2: 创建通用响应封装

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/common/result/ApiResult.java`
- Create: `backend/szy-new/src/main/java/com/szy/common/result/PageResultVO.java`

**Step 1: 创建ApiResult类**

```java
package com.szy.common.result;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一API响应封装
 */
@Schema(description = "统一API响应")
public class ApiResult<T> {

    @Schema(description = "状态码")
    private int code;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    private ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResult<T> success(T data, String message) {
        return new ApiResult<>(200, message, data);
    }

    public static <T> ApiResult<T> success(T data) {
        return success(data, "操作成功");
    }

    public static <T> ApiResult<T> success(String message) {
        return success(null, message);
    }

    public static <T> ApiResult<T> error(int code, String message) {
        return new ApiResult<>(code, message, null);
    }

    public static <T> ApiResult<T> error(String message) {
        return error(400, message);
    }

    // Getters
    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
```

**Step 2: 创建PageResultVO类**

```java
package com.szy.common.result;

import com.github.pagehelper.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页结果封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页结果")
public class PageResultVO<T> {

    @Schema(description = "数据列表")
    private List<T> list;

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "当前页码")
    private int page;

    @Schema(description = "每页大小")
    private int size;

    @Schema(description = "总页数")
    private int totalPages;

    /**
     * 从PageHelper的Page对象构建分页结果
     */
    public static <E, V> PageResultVO<V> of(Page<E> page, Function<E, V> converter) {
        List<V> list = page.stream().map(converter).collect(Collectors.toList());
        return new PageResultVO<>(
            list,
            page.getTotal(),
            page.getPageNum(),
            page.getPageSize(),
            page.getPages()
        );
    }

    /**
     * 从PageHelper的Page对象构建分页结果（无需转换）
     */
    public static <E> PageResultVO<E> of(Page<E> page) {
        return of(page, e -> e);
    }
}
```

**Step 3: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/common/result/
git commit -m "feat: 添加统一响应封装ApiResult和PageResultVO"
```

---

### Task 3: 创建异常处理

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/common/exception/BusinessException.java`
- Create: `backend/szy-new/src/main/java/com/szy/common/exception/GlobalExceptionHandler.java`

**Step 1: 创建BusinessException**

```java
package com.szy.common.exception;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
```

**Step 2: 创建GlobalExceptionHandler**

```java
package com.szy.common.exception;

import com.szy.common.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResult<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResponseEntity.badRequest().body(ApiResult.error(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败: {}", message);
        return ResponseEntity.badRequest().body(ApiResult.error(400, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleException(Exception e) {
        log.error("系统异常: ", e);
        return ResponseEntity.internalServerError().body(ApiResult.error(500, "系统错误，请稍后重试"));
    }
}
```

**Step 3: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/common/exception/
git commit -m "feat: 添加业务异常和全局异常处理器"
```

---

### Task 4: 创建配置文件

**Files:**
- Create: `backend/szy-new/src/main/resources/application.yml`
- Create: `backend/szy-new/src/main/resources/application-dev.yml`

**Step 1: 创建主配置文件**

```yaml
# application.yml
spring:
  profiles:
    active: dev
  application:
    name: szy-new

server:
  port: 8081

# MyBatis配置
mybatis:
  mapper-locations: classpath:mapper/**/*.xml
  type-aliases-package: com.szy.pojo.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

# PageHelper配置
pagehelper:
  helper-dialect: mysql
  reasonable: true
  support-methods-arguments: true

# Knife4j配置
knife4j:
  enable: true
  setting:
    language: zh_cn
```

**Step 2: 创建开发环境配置**

```yaml
# application-dev.yml
spring:
  datasource:
    dynamic:
      primary: jcxx
      strict: false
      datasource:
        jcxx:
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://localhost:3306/jcxx?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
          username: root
          password: root
  redis:
    host: localhost
    port: 6379
    database: 0
    timeout: 3000

logging:
  level:
    com.szy: debug
    com.szy.mapper: debug
```

**Step 3: 提交**

```bash
git add backend/szy-new/src/main/resources/
git commit -m "feat: 添加应用配置文件"
```

---

### Task 5: 创建Security配置

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/config/SecurityConfig.java`
- Create: `backend/szy-new/src/main/java/com/szy/config/CorsConfig.java`

**Step 1: 创建SecurityConfig**

```java
package com.szy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers("/login", "/logout", "/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}
```

**Step 2: 创建CorsConfig**

```java
package com.szy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

**Step 3: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/config/
git commit -m "feat: 添加Security和CORS配置"
```

---

### Task 6: 创建Swagger配置

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/config/SwaggerConfig.java`

**Step 1: 创建SwaggerConfig**

```java
package com.szy.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger/Knife4j配置
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.szy.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("智慧水利API文档")
                .description("智慧水利后端重构版API接口文档")
                .contact(new Contact("开发团队", "", ""))
                .version("1.0.0")
                .build();
    }
}
```

**Step 2: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/config/SwaggerConfig.java
git commit -m "feat: 添加Swagger/Knife4j配置"
```

---

## 阶段二：API清单提取

### Task 7: 提取前端API清单

**Files:**
- Create: `docs/API清单.md`

**Step 1: 分析前端API文件**

阅读 `frontendV2/src/api/*.js` 文件，提取所有API端点。

**Step 2: 创建API清单文档**

详细内容见下一页...

---

## 执行选项

计划已保存到 `docs/plans/2026-03-01-backend-refactor-implementation.md`。

**两种执行方式：**

**1. Subagent-Driven (当前会话)** - 我为每个任务派发新的子代理，任务间进行代码审查，快速迭代

**2. Parallel Session (单独会话)** - 打开新会话使用 executing-plans，批量执行带检查点

**您选择哪种方式？**