# 后端重构设计文档

## 1. 概述

### 1.1 背景

现有后端项目存在大量冗余API和代码，需创建新项目进行重构。重构目标是完全按照前端V2的实际需求实现API，去除未使用的接口。

### 1.2 目标

- 以前端V2为基准，只实现前端实际调用的API
- 采用原生MyBatis替代MyBatis Plus
- 遵循后端规范文档的代码标准
- 保持多数据源配置

### 1.3 核心原则

- **KISS**：保持简单，避免过度设计
- **YAGNI**：不预实现未来可能需要的功能
- **SOLID**：单一职责、开闭原则、里氏替换、接口隔离、依赖倒置

---

## 2. 技术栈

### 2.1 核心框架

| 组件 | 版本 | 说明 |
|-----|------|------|
| Spring Boot | 2.6.2 | 保持现有版本 |
| MyBatis | 原生 | 替换MyBatis Plus |
| PageHelper | 1.4.6 | 分页插件 |
| dynamic-datasource | 3.2.1 | 多数据源 |

### 2.2 依赖变更

**移除**：
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
</dependency>
```

**新增**：
```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.2</version>
</dependency>
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.4.6</version>
</dependency>
```

### 2.3 保持不变

- Spring Security + JWT
- Knife4j (Swagger)
- Lombok, Hutool
- Redis
- 多数据源（MySQL + PostgreSQL + SQL Server）

---

## 3. 项目架构

### 3.1 分层结构

```
com.szy
├── controller          # 控制器层
├── service             # 服务层接口
│   └── impl            # 服务实现类
├── mapper              # 数据访问层
├── pojo                # 实体类层
│   ├── entity          # 实体类
│   ├── dto             # 数据传输对象
│   └── vo              # 视图对象
├── config              # 配置类
├── security            # 安全模块
├── common              # 通用模块
│   ├── exception       # 异常定义
│   └── result          # 统一响应封装
└── utils               # 工具类
```

### 3.2 各层职责

| 层级 | 职责 | 禁止事项 |
|-----|------|---------|
| Controller | 接收请求、调用Service、返回响应 | 业务逻辑、数据转换 |
| Service | 业务逻辑处理、事务管理 | 直接操作HTTP请求/响应 |
| Mapper | 数据库操作、SQL执行 | 业务逻辑、数据处理 |
| Entity | 数据模型映射 | 业务逻辑、校验规则 |
| DTO | 接口参数封装 | 业务逻辑 |
| VO | 返回数据封装 | 业务逻辑 |

---

## 4. 代码规范

### 4.1 依赖注入

**强制**使用构造器注入：
```java
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
```

**禁止**使用字段注入：
```java
// ❌ 禁止
@Autowired
private UserService userService;
```

### 4.2 HTTP方法规范

只使用 GET 和 POST：
- **GET**：查询操作
- **POST**：创建、更新、删除操作

### 4.3 响应格式

统一使用 `ApiResult<T>` 封装：
```java
// 成功
return ResponseEntity.ok(ApiResult.success(data, "操作成功"));

// 失败
return ResponseEntity.status(400).body(ApiResult.error(400, "错误信息"));
```

### 4.4 分页

使用 PageHelper：
```java
PageHelper.startPage(page, size);
List<User> list = userMapper.selectList(userInfo);
return PageResultVO.restPage(list, this::convertToVO);
```

---

## 5. 重构策略

### 5.1 方案：前端API逆向分析法

从前端代码出发，提取所有API调用，生成API清单，按清单重构后端。

### 5.2 前端API模块

| 模块文件 | 业务领域 |
|---------|---------|
| auth.js | 认证登录 |
| user.js | 用户管理 |
| menu.js | 菜单管理 |
| role.js | 角色管理 |
| dept.js | 部门管理 |
| dict.js | 字典管理 |
| person.js | 人员管理 |
| organization.js | 组织机构 |
| dam.js | 大坝监测 |
| water.js | 水雨情 |
| warning.js | 预警管理 |
| report.js | 综合报表 |
| inspection.js | 巡检记录 |
| maintenance.js | 维护记录 |
| engineering.js | 工程信息 |

### 5.3 新旧项目对照

| 新项目API | 旧项目Controller | 迁移内容 |
|-----------|-----------------|---------|
| POST /login | AuthController.login | 登录验证逻辑、JWT生成 |
| GET /user/list | UserController.list | 分页查询、角色关联 |
| POST /user/save | UserController.save | 用户创建、密码加密 |

---

## 6. 执行流程

### 阶段一：准备工作
1. 创建新项目骨架
2. 配置依赖（MyBatis、PageHelper）
3. 配置多数据源、Redis、Security
4. 编写通用模块

### 阶段二：API清单提取
1. 解析前端 `api/*.js` 文件
2. 提取所有API端点
3. 生成 `API清单.md` 文档
4. 人工审核确认

### 阶段三：核心模块开发
1. 认证模块（auth）
2. 用户权限模块（user、role、menu）
3. 业务模块（按API清单）
4. 工具模块（文件上传、导出）

### 阶段四：测试验证
1. 接口测试
2. 前端联调
3. 性能测试

---

## 7. 交付物

### 7.1 代码
- 完整的后端项目
- 所有API接口实现
- 单元测试

### 7.2 文档
- `API清单.md`
- `数据库设计.md`
- `部署说明.md`

### 7.3 配置
- `application.yml`
- `application-dev.yml`
- `application-prod.yml`

---

## 8. 风险与应对

| 风险 | 应对措施 |
|-----|---------|
| API遗漏 | 多轮审核、测试覆盖 |
| 业务逻辑丢失 | 对照旧代码、保留注释 |
| 数据源配置复杂 | 保持现有配置、逐步迁移 |

---

## 9. 参考资料

- 后端规范文档：`规范文档/后端规范文档.md`
- 前端API模块：`frontendV2/src/api/*.js`
- 旧后端项目：`backend/szy/`