# 认证模块设计文档

> **日期：** 2026-03-01
> **状态：** 已批准

## 概述

实现智慧水利后端认证模块，包含登录、获取用户信息、修改密码三个核心API。

## 需求

| API | 方法 | 说明 |
|-----|------|------|
| `/login` | POST | 用户登录，返回JWT token |
| `/user/userInfo` | GET | 获取当前登录用户信息 |
| `/user/updatePass` | POST | 修改当前用户密码 |

## 设计决策

### 1. 用户实体精简

只保留认证必需字段：

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | Long | 主键 |
| username | String | 用户名（登录凭证） |
| password | String | 密码（BCrypt加密） |
| name | String | 姓名 |
| type | String | 用户类型 |
| department | String | 所属部门 |
| phone | String | 手机号码 |
| email | String | 电子邮箱 |

### 2. 权限缓存机制

保留Redis缓存用户权限：
- 缓存Key：`GrantedAuthority:{username}`
- 过期时间：1小时
- 触发清除：角色变更、权限变更时

### 3. 响应格式

沿用现有 `Result` 类，保持前端兼容：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": { ... }
}
```

## 架构

```
com.szy
├── controller
│   ├── AuthController.java        # 登录接口
│   └── UserController.java        # 用户信息、修改密码
├── service
│   ├── UserService.java
│   └── impl/UserServiceImpl.java
├── mapper
│   └── UserMapper.java
├── pojo
│   ├── entity/User.java           # 用户实体
│   ├── dto/LoginDTO.java          # 登录请求
│   └── vo/UserVO.java             # 用户信息响应
└── security/
    ├── JwtTokenUtil.java          # 已存在
    ├── JwtAuthenticationFilter.java # 已存在
    └── UserDetailsServiceImpl.java  # 需改造
```

## 数据流

### 登录流程

```
前端 → POST /login (form-urlencoded)
     → AuthController.login()
     → AuthenticationManager.authenticate()
     → UserDetailsServiceImpl.loadUserByUsername()
     → JWT生成 → Result.success(token)
     → 前端存储token
```

### 获取用户信息流程

```
前端 → GET /user/userInfo (Bearer token)
     → JwtAuthenticationFilter 验证token
     → SecurityContextHolder 设置认证信息
     → UserController.userInfo()
     → UserService.getByUsername()
     → Result.success(UserVO)
```

## 数据库

使用 `jcxx` 数据源的 `user` 表，关键字段：
- id, username, password, name, type, department, phone, email

关联表：
- `user_role`：用户-角色关联
- `role`：角色表
- `role_authority`：角色-权限关联
- `authority`：权限表

## 安全考虑

1. 密码使用BCrypt加密存储
2. JWT token过期时间默认24小时
3. 权限验证使用 `@PreAuthorize` 注解
4. 修改密码需验证旧密码