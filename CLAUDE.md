# 智慧水利后端项目 - Claude Code 上下文

## 项目概述

**项目名称：** 智慧水利后端重构（szy-new）
**目标：** 按前端V2需求重构后端，使用原生MyBatis替代MyBatis Plus

## 技术栈

| 组件 | 版本 | 说明 |
|-----|------|------|
| Spring Boot | 2.6.2 | 保持现有版本 |
| MyBatis | 2.2.2 | 原生MyBatis，非Plus |
| PageHelper | 1.4.6 | 分页插件 |
| dynamic-datasource | 3.2.1 | 多数据源 |
| Knife4j | 3.0.3 | API文档 |
| Hutool | 5.8.21 | 工具库 |

## 架构设计

```
com.szy
├── controller          # 控制器层（只做请求转发，禁止业务逻辑）
├── service/impl        # 服务层（业务逻辑、事务管理）
├── mapper              # 数据访问层（MyBatis）
├── pojo
│   ├── entity          # 实体类（数据库表映射）
│   ├── dto             # 数据传输对象（请求参数）
│   └── vo              # 视图对象（响应数据）
├── config              # 配置类
├── security            # JWT安全模块
└── common              # 通用模块（异常、响应封装）
```

## 代码规范（强制）

- **依赖注入：** 使用 `@RequiredArgsConstructor` + `private final` 构造器注入
- **禁止：** `@Autowired` 字段注入
- **响应格式：** 统一使用 `Result<T>` 或 `ApiResult<T>`
- **分页：** 使用 PageHelper + `PageResultVO<T>`
- **HTTP方法：** 只用 GET（查询）和 POST（变更操作）
- **规范文档：** 详见 `规范文档/后端规范文档.md`

## 关键文档路径

| 文档 | 路径 | 用途 |
|-----|------|------|
| 后端规范文档 | `规范文档/后端规范文档.md` | 代码规范详情 |
| 设计文档 | `docs/plans/2026-03-01-backend-refactor-design.md` | 架构设计 |
| 实施计划 | `docs/plans/2026-03-01-backend-refactor-implementation.md` | 任务分解 |
| API清单 | `docs/API清单.md` | 129个API端点（15个模块） |
| 开发状态 | `docs/开发状态.md` | 当前进度追踪 |

## 新项目位置

**新后端项目：** `backend/szy-new/`
**旧后端项目：** `backend/szy/`（仅供参考，不在其上修改）

## 当前状态

> 详见 `docs/开发状态.md`

**阶段：** 项目初始化完成
**下一步：** 实现认证模块（auth）

## 新对话快速启动

在新对话中，可以直接说：

> "请先阅读CLAUDE.md和docs/开发状态.md，继续后端重构的下一阶段开发"