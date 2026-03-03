# 智慧水利后端项目 - Claude Code 上下文

## 项目概述

**项目名称：** 智慧水利后端重构（szy-new）
**目标：** 按前端V2需求重构后端，使用原生MyBatis替代MyBatis Plus

## 技术栈

| 组件 | 版本 | 说明 |
|-----|------|------|
| JDK | 1.8 | Amazon Corretto 1.8.0_452 |
| Maven | 3.x | 使用项目自定义 settings |
| Spring Boot | 2.6.2 | 保持现有版本 |
| MyBatis | 2.2.2 | 原生MyBatis，非Plus |
| PageHelper | 1.4.6 | 分页插件 |
| dynamic-datasource | 3.2.1 | 多数据源 |
| Knife4j | 3.0.3 | API文档 |
| Hutool | 5.8.21 | 工具库 |

## Maven 编译命令

**项目使用 IDEA 内置 Maven，编译命令：**

**方式一：在 backend/szy-new 目录下使用命令行**
```bash
# Windows 环境下使用 IDEA Maven（推荐）
# 注意：路径中使用正斜杠 / 而不是反斜杠 \
"C:/Program Files/JetBrains/IntelliJ IDEA 2024.3.1.1/plugins/maven/lib/maven3/bin/mvn" compile -s D:/demo/java/jzsk/backend/szy/maven-settings.xml -f D:/demo/java/jzsk/backend/szy-new/pom.xml

# 或完整打包
"C:/Program Files/JetBrains/IntelliJ IDEA 2024.3.1.1/plugins/maven/lib/maven3/bin/mvn" clean package -s D:/demo/java/jzsk/backend/szy/maven-settings.xml -f D:/demo/java/jzsk/backend/szy-new/pom.xml -DskipTests
```

**方式二：在 IDEA 中操作（推荐）**
- 打开 IDEA 右侧 Maven 面板
- 展开 szy-new → Lifecycle
- 双击 compile 或 package

**IDEA 中的 Maven 配置：**
- Settings 文件路径：`D:\demo\java\jzsk\backend\szy\maven-settings.xml`
- JDK 版本：Java 8 (corretto-1.8.0_452)
- Maven Home：`C:/Program Files/JetBrains/IntelliJ IDEA 2024.3.1.1/plugins/maven/lib/maven3`

---

## 常见命令错误与正确写法

### 1. Maven 编译失败（jar文件被占用）

**错误原因：** 后端服务正在运行，占用了 target 目录下的 jar 文件

**解决方案：**
```bash
# 方案一：先停止后端服务，再执行 clean package
# （见下方"停止Java服务"命令）

# 方案二：只编译不清理（快速编译）
"C:/Program Files/JetBrains/IntelliJ IDEA 2024.3.1.1/plugins/maven/lib/maven3/bin/mvn" compile -s D:/demo/java/jzsk/backend/szy/maven-settings.xml -f D:/demo/java/jzsk/backend/szy-new/pom.xml
```

### 2. 启动后端服务失败

**错误命令：**
```bash
# 错误：start 命令在 bash 中语法不正确
start "szy-new-backend" java -jar backend/szy-new/target/szy-new-0.0.1-SNAPSHOT.jar
```

**正确命令：**
```bash
# 方式一：直接运行（前台）
java -jar D:/demo/java/jzsk/backend/szy-new/target/szy-new-0.0.1-SNAPSHOT.jar

# 方式二：后台运行
java -jar D:/demo/java/jzsk/backend/szy-new/target/szy-new-0.0.1-SNAPSHOT.jar &

# 方式三：使用 run_in_background 参数（Claude Code 环境）
# 在 Bash 工具中设置 run_in_background: true
```

### 3. 停止Java服务失败

**错误命令：**
```bash
# 错误：Windows 参数语法在 bash 中需要转义
taskkill /F /PID 21076
```

**正确命令：**
```bash
# 方式一：使用双斜杠（Windows命令）
taskkill //F //PID <PID号>

# 方式二：使用 Windows 的 wmic 命令
wmic process where "name='java.exe'" delete

# 方式三：直接在 IDEA 中停止运行
```

### 4. 路径分隔符问题

**错误写法：**
```bash
# 反斜杠 \ 在 bash 中需要转义
mvn compile -s D:\demo\java\jzsk\backend\szy\maven-settings.xml
```

**正确写法：**
```bash
# 使用正斜杠 /
mvn compile -s D:/demo/java/jzsk/backend/szy/maven-settings.xml

# 或使用双引号包裹路径
mvn compile -s "D:/demo/java/jzsk/backend/szy/maven-settings.xml"
```

### 5. 查看正在运行的Java进程

```bash
# Windows 查看进程
tasklist | findstr java

# 或使用 jps 命令
jps -l
```

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

---

## 开发流程规范

**完成任务后必须更新文档：**

1. **更新 `docs/开发状态.md`：**
   - 将任务标记为 ✅ 完成
   - 添加Git提交记录
   - 更新"下一步任务"

2. **提交时包含文档更新：**
   ```bash
   git add docs/开发状态.md
   git commit -m "feat: 完成xxx功能，更新开发状态"
   ```

3. **遇到问题时：** 在开发状态.md的"待解决问题"表格中记录