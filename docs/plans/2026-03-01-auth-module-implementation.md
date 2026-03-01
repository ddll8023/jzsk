# 认证模块实施计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 实现用户登录、获取用户信息、修改密码三个核心API

**Architecture:** Controller → Service → Mapper → Entity，使用原生MyBatis查询，Redis缓存权限，JWT认证

**Tech Stack:** Spring Boot 2.6.2, MyBatis, Spring Security, JWT, Redis

---

## Task 1: 创建用户实体类

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/pojo/entity/User.java`

**Step 1: 创建User实体类**

```java
package com.szy.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 姓名 */
    private String name;

    /** 用户类型 */
    private String type;

    /** 所属部门 */
    private String department;

    /** 手机号码 */
    private String phone;

    /** 电子邮箱 */
    private String email;
}
```

**Step 2: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/pojo/entity/User.java
git commit -m "feat(auth): 添加用户实体类"
```

---

## Task 2: 创建DTO和VO类

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/pojo/dto/LoginDTO.java`
- Create: `backend/szy-new/src/main/java/com/szy/pojo/dto/UpdatePasswordDTO.java`
- Create: `backend/szy-new/src/main/java/com/szy/pojo/vo/UserVO.java`

**Step 1: 创建LoginDTO**

```java
package com.szy.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 */
@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
```

**Step 2: 创建UpdatePasswordDTO**

```java
package com.szy.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码请求DTO
 */
@Data
public class UpdatePasswordDTO {

    @NotBlank(message = "原密码不能为空")
    private String currentPassword;

    @NotBlank(message = "新密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
```

**Step 3: 创建UserVO**

```java
package com.szy.pojo.vo;

import lombok.Data;

/**
 * 用户信息VO
 */
@Data
public class UserVO {

    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 姓名 */
    private String name;

    /** 用户类型 */
    private String type;

    /** 所属部门 */
    private String department;

    /** 手机号码 */
    private String phone;

    /** 电子邮箱 */
    private String email;
}
```

**Step 4: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/pojo/dto/
git add backend/szy-new/src/main/java/com/szy/pojo/vo/
git commit -m "feat(auth): 添加登录DTO、修改密码DTO和用户VO"
```

---

## Task 3: 创建UserMapper

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/mapper/UserMapper.java`
- Create: `backend/szy-new/src/main/resources/mapper/UserMapper.xml`

**Step 1: 创建UserMapper接口**

```java
package com.szy.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
@DS("jcxx")
public interface UserMapper {

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询用户
     */
    User selectById(@Param("id") Long id);

    /**
     * 更新用户密码
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 根据用户ID查询权限编码
     */
    List<String> selectAuthorityCodesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询角色编码
     */
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);
}
```

**Step 2: 创建UserMapper.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.szy.mapper.UserMapper">

    <resultMap id="BaseResultMap" type="com.szy.pojo.entity.User">
        <id column="id" property="id"/>
        <result column="username" property="username"/>
        <result column="password" property="password"/>
        <result column="name" property="name"/>
        <result column="type" property="type"/>
        <result column="department" property="department"/>
        <result column="phone" property="phone"/>
        <result column="email" property="email"/>
    </resultMap>

    <sql id="Base_Column_List">
        id, username, password, name, type, department, phone, email
    </sql>

    <select id="selectByUsername" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM user
        WHERE username = #{username}
    </select>

    <select id="selectById" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM user
        WHERE id = #{id}
    </select>

    <update id="updatePassword">
        UPDATE user
        SET password = #{password}
        WHERE id = #{id}
    </update>

    <select id="selectRoleCodesByUserId" resultType="java.lang.String">
        SELECT DISTINCT r.code
        FROM user_role ur
        LEFT JOIN role r ON ur.role_id = r.id
        WHERE ur.user_id = #{userId}
    </select>

    <select id="selectAuthorityCodesByUserId" resultType="java.lang.String">
        SELECT DISTINCT a.code
        FROM user_role ur
        LEFT JOIN role_authority ra ON ur.role_id = ra.role_id
        LEFT JOIN authority a ON ra.authority_id = a.id
        WHERE ur.user_id = #{userId}
    </select>

</mapper>
```

**Step 3: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/mapper/UserMapper.java
git add backend/szy-new/src/main/resources/mapper/UserMapper.xml
git commit -m "feat(auth): 添加UserMapper接口和XML映射文件"
```

---

## Task 4: 创建UserService

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/service/UserService.java`
- Create: `backend/szy-new/src/main/java/com/szy/service/impl/UserServiceImpl.java`

**Step 1: 创建UserService接口**

```java
package com.szy.service;

import com.szy.pojo.entity.User;
import com.szy.pojo.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);

    /**
     * 根据用户ID查询用户
     */
    User getById(Long id);

    /**
     * 获取用户权限信息（角色+权��）
     */
    String getUserAuthorityInfo(Long userId);

    /**
     * 清除用户权限缓存
     */
    void clearUserAuthorityCache(String username);

    /**
     * 修改用户密码
     */
    void updatePassword(Long userId, String newPassword);

    /**
     * 转换为UserVO
     */
    UserVO toUserVO(User user);
}
```

**Step 2: 创建UserServiceImpl实现类**

```java
package com.szy.service.impl;

import com.szy.mapper.UserMapper;
import com.szy.pojo.entity.User;
import com.szy.pojo.vo.UserVO;
import com.szy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate stringRedisTemplate;

    /** 权限缓存Key前缀 */
    private static final String AUTHORITY_CACHE_PREFIX = "GrantedAuthority:";

    /** 权限缓存过期时间（小时） */
    private static final long AUTHORITY_CACHE_EXPIRE_HOURS = 1;

    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        // 先从缓存获取
        User user = userMapper.selectById(userId);
        if (user == null) {
            return "";
        }

        String cacheKey = AUTHORITY_CACHE_PREFIX + user.getUsername();
        String cachedAuthority = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cachedAuthority != null) {
            return cachedAuthority;
        }

        // 查询角色
        List<String> roleCodes = userMapper.selectRoleCodesByUserId(userId);
        String roles = roleCodes.stream()
                .map(code -> "ROLE_" + code)
                .collect(Collectors.joining(","));

        // 查询权限
        List<String> authorityCodes = userMapper.selectAuthorityCodesByUserId(userId);
        String authorities = String.join(",", authorityCodes);

        // 拼接角色和权限
        String authorityInfo = roles;
        if (!authorities.isEmpty()) {
            if (!authorityInfo.isEmpty()) {
                authorityInfo += ",";
            }
            authorityInfo += authorities;
        }

        // 缓存结果
        stringRedisTemplate.opsForValue().set(
                cacheKey,
                authorityInfo,
                AUTHORITY_CACHE_EXPIRE_HOURS,
                TimeUnit.HOURS
        );

        return authorityInfo;
    }

    @Override
    public void clearUserAuthorityCache(String username) {
        String cacheKey = AUTHORITY_CACHE_PREFIX + username;
        stringRedisTemplate.delete(cacheKey);
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        userMapper.updatePassword(userId, newPassword);
    }

    @Override
    public UserVO toUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setName(user.getName());
        vo.setType(user.getType());
        vo.setDepartment(user.getDepartment());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        return vo;
    }
}
```

**Step 3: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/service/UserService.java
git add backend/szy-new/src/main/java/com/szy/service/impl/UserServiceImpl.java
git commit -m "feat(auth): 添加用户服务接口和实现类"
```

---

## Task 5: 创建AccountUser类

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/security/AccountUser.java`

**Step 1: 创建AccountUser类**

```java
package com.szy.security;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 扩展的UserDetails实现，携带用户ID
 */
@Getter
public class AccountUser extends User {

    /** 用户ID */
    private final Long userId;

    public AccountUser(Long userId, String username, String password,
                       Collection<? extends org.springframework.security.core.GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }
}
```

**Step 2: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/security/AccountUser.java
git commit -m "feat(auth): 添加AccountUser扩展UserDetails"
```

---

## Task 6: 改造UserDetailsServiceImpl

**Files:**
- Modify: `backend/szy-new/src/main/java/com/szy/security/UserDetailsServiceImpl.java`

**Step 1: 替换UserDetailsServiceImpl内容**

```java
package com.szy.security;

import com.szy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户详情服务实现
 * 用途：加载用户信息用于Spring Security认证
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.szy.pojo.entity.User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return new AccountUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                getUserAuthority(user.getId())
        );
    }

    /**
     * 获取用户权限信息
     */
    private List<GrantedAuthority> getUserAuthority(Long userId) {
        String authority = userService.getUserAuthorityInfo(userId);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
```

**Step 2: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/security/UserDetailsServiceImpl.java
git commit -m "feat(auth): 改造UserDetailsServiceImpl使用数据库查询"
```

---

## Task 7: 创建AuthController

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/controller/AuthController.java`

**Step 1: 创建AuthController**

```java
package com.szy.controller;

import com.szy.common.lang.Result;
import com.szy.pojo.dto.LoginDTO;
import com.szy.security.AccountUser;
import com.szy.security.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@Api(tags = "认证管理")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@Validated LoginDTO loginDTO, HttpServletRequest request) {
        // 表单登录方式
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        // 执行认证
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT
        AccountUser accountUser = (AccountUser) authentication.getPrincipal();
        String jwt = jwtTokenUtil.generateToken(accountUser);

        // 返回token
        Map<String, Object> data = new HashMap<>();
        data.put("token", jwt);
        return Result.success(data);
    }

    /**
     * 用户登出
     */
    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result logout() {
        SecurityContextHolder.clearContext();
        return Result.success("退出成功");
    }
}
```

**Step 2: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/controller/AuthController.java
git commit -m "feat(auth): 添加认证控制器（登录/登出）"
```

---

## Task 8: 创建UserController

**Files:**
- Create: `backend/szy-new/src/main/java/com/szy/controller/UserController.java`

**Step 1: 创建UserController**

```java
package com.szy.controller;

import com.szy.common.exception.BusinessException;
import com.szy.common.lang.Result;
import com.szy.pojo.dto.UpdatePasswordDTO;
import com.szy.pojo.entity.User;
import com.szy.pojo.vo.UserVO;
import com.szy.security.AccountUser;
import com.szy.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 获取当前用户信息
     */
    @ApiOperation("获取当前用户信息")
    @GetMapping("/userInfo")
    public Result userInfo(@AuthenticationPrincipal AccountUser accountUser) {
        User user = userService.getById(accountUser.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UserVO userVO = userService.toUserVO(user);
        return Result.success(userVO);
    }

    /**
     * 修改密码
     */
    @ApiOperation("修改密码")
    @PostMapping("/updatePass")
    public Result updatePassword(@Validated @RequestBody UpdatePasswordDTO dto,
                                  @AuthenticationPrincipal AccountUser accountUser) {
        // 验证新密码和确认密码是否一致
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("新密码与确认密码不一致");
        }

        // 获取当前用户
        User user = userService.getById(accountUser.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证原密码
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }

        // 更新密码
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        userService.updatePassword(user.getId(), encodedPassword);

        // 清除权限缓存
        userService.clearUserAuthorityCache(user.getUsername());

        return Result.success("密码修改成功");
    }
}
```

**Step 2: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/controller/UserController.java
git commit -m "feat(auth): 添加用户控制器（获取信息/修改密码）"
```

---

## Task 9: 更新SecurityConfig

**Files:**
- Modify: `backend/szy-new/src/main/java/com/szy/config/SecurityConfig.java`

**Step 1: 更新SecurityConfig，添加AuthenticationManager Bean并修正放行路径**

将SecurityConfig.java的 `securityFilterChain` 方法中的放行路径从 `/auth/login` 改为 `/login`，并添加 `AuthenticationManager` Bean：

```java
package com.szy.config;

import com.szy.security.JwtAuthenticationEntryPoint;
import com.szy.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 * 用途：配置安全认证和授权
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全过滤链配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 关闭CSRF
            .csrf().disable()
            // 禁用Session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 异常处理
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            // 请求授权配置
            .authorizeRequests()
            // 放行路径
            .antMatchers(
                "/login",
                "/logout",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/favicon.ico"
            ).permitAll()
            // 其他请求需要认证
            .anyRequest().authenticated();

        // 添加JWT过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
```

**Step 2: 提交**

```bash
git add backend/szy-new/src/main/java/com/szy/config/SecurityConfig.java
git commit -m "feat(auth): 更新SecurityConfig添加AuthenticationManager并修正放行路径"
```

---

## Task 10: 验证项目编译

**Step 1: 编译项目**

```bash
cd backend/szy-new && mvn compile -DskipTests
```

Expected: BUILD SUCCESS

**Step 2: 提交最终状态**

如果编译成功，更新开发状态文档。

---

## 执行选项

**计划已保存到 `docs/plans/2026-03-01-auth-module-implementation.md`。**

**两种执行方式：**

**1. Subagent-Driven (当前会话)** - 我为每个任务派发新的子代理，任务间进行代码审查，快速迭代

**2. Parallel Session (单独会话)** - 打开新会话使用 executing-plans，批量执行带检查点

**您选择哪种方式？**