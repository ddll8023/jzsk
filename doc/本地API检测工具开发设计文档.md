# 本地 API 检测工具开发设计文档

## 一、文档目的

本文档用于说明本地 API 检测工具的开发设计。该工具作为项目根目录下的独立 Python 桌面软件存在，不嵌入现有 Java 后端或 Vue 前端工程。

工具目标是让使用者在需要时打开软件，选择要测试的接口，一键发起请求，查看本次请求结果，并将测试日志记录到本地 SQLite 数据库中，便于后续排查接口连通性、登录状态和设备监测接口状态。

## 二、需求理解

当前项目已有完整的后端接口与设备监测模块：

- API 健康检查：`GET /actuator/health`
- 登录接口：`POST /api/auth/login`
- GNSS 设备监测：`GET /api/device-monitor/gnss`
- 雨水情设备监测：`GET /api/device-monitor/rain`
- 渗流渗压设备监测：`GET /api/device-monitor/seepage`

本工具不负责实现设备监测业务逻辑，只调用现有设备监测接口。设备状态计算、采集时间判断、在线/离线/异常统计均复用后端已有逻辑。

## 三、设计目标

### 3.1 功能目标

1. 支持配置后端服务地址。
2. 支持输入登录账号和密码。
3. 支持勾选需要测试的 API。
4. 支持一键请求被勾选的 API。
5. 支持自动处理 JWT 登录态。
6. 支持展示每个接口的请求结果。
7. 支持展示设备监测接口的统计摘要。
8. 支持将每次测试结果写入 SQLite。
9. 支持查看历史测试记录。

### 3.2 非目标

第一版不做以下能力：

1. 不做定时任务。
2. 不做托盘常驻。
3. 不做开机自启动。
4. 不做后端接口改造。
5. 不做前端页面改造。
6. 不直连业务数据库。
7. 不写入或修改后端业务数据。
8. 不替代 Postman 的通用 API 调试能力。

## 四、技术选型

### 4.1 推荐技术栈

```text
Python 3.x
PySide6
requests
SQLAlchemy 2.0
SQLite
pydantic
pydantic-settings
json
PyInstaller
```

### 4.2 选型说明

- `PySide6`：用于构建 Windows 桌面软件窗口，避免使用浏览器访问本地 Web 页面。
- `requests`：用于请求后端 HTTP API。
- `SQLAlchemy 2.0 + SQLite`：用于本地保存测试日志，数据库访问风格遵循 Python 后端规范。
- `pydantic`：用于定义请求、响应、配置等结构化数据模型。
- `pydantic-settings`：用于统一管理配置入口，避免在业务代码中散落读取配置。
- `json`：用于维护接口配置和应用配置。
- `PyInstaller`：后续用于打包为 `.exe`，第一版开发阶段可暂不打包。

### 4.3 Python 规范适配说明

本工具是轻量桌面工具，不是 FastAPI 后端服务，因此不完整套用 `规范文档/python后端规范文档.md` 中的 HTTP API 层约束。

不适用内容：

1. 不创建本地 FastAPI 服务。
2. 不新增 `/api/v1/xxx` 本地路由。
3. 不要求桌面工具内部返回 `code/message/data` HTTP 响应壳。
4. 不设计分页 HTTP 接口。

适用内容：

1. 配置统一由 `settings.py` 管理，业务代码不直接读取环境变量。
2. 敏感配置不硬编码，密码由界面输入，不写入默认配置文件。
3. Schema 使用 Pydantic，命名采用 `*Request` / `*Response`。
4. 业务逻辑使用模块函数，不创建 `XxxService` 类。
5. Service 成功返回业务数据对象，失败抛出统一 `ServiceException`。
6. 错误码使用 `ErrorCode` 枚举，不在代码中硬编码裸数字。
7. SQLite 访问使用 SQLAlchemy 2.0 风格，写操作由 service 函数负责提交和回滚。
8. 数据库模型字段使用小写下划线命名，包含 `id`、`created_at`、`updated_at`。
9. 本工具内部错误码遵循 Python 规范；被测 Java 后端接口的成功码仍按现有项目 `code=200` 判断，不强行改成 Python 规范的 `code=0`。

## 五、项目位置与目录结构

建议在项目根目录下创建独立 Python 项目：

```text
jzsk/
├── backend/
├── backendV2/
├── frontend/
├── frontendV2/
├── doc/
└── local-api-tester/
    ├── pyproject.toml
    ├── README.md
    ├── .env.example
    ├── config/
    │   └── apis.json
    ├── data/
    │   └── api_test_logs.db
    ├── src/
    │   └── local_api_tester/
    │       ├── main.py
    │       ├── settings.py
    │       ├── exceptions.py
    │       ├── schemas.py
    │       ├── models.py
    │       ├── db.py
    │       ├── api_client.py
    │       ├── services.py
    │       └── ui/
    │           └── main_window.py
    └── logs/
```

## 六、模块职责

### 6.1 `main.py`

应用入口，负责创建 Qt 应用实例、加载配置、初始化数据库、初始化主窗口并启动桌面程序。

文件首行应包含文件级 docstring，说明该文件是桌面工具入口。

### 6.2 `settings.py`

统一配置入口，负责读取应用配置和环境变量。

设计约束：

1. 使用 `pydantic-settings` 的 `BaseSettings`。
2. 所有字段必须有类型注解。
3. 全局只暴露一个 `settings` 实例。
4. 业务代码统一 `from local_api_tester.settings import settings`。
5. 不在业务代码中直接调用 `os.getenv()`。
6. 密码不写入配置文件，由界面输入。

### 6.3 `exceptions.py`

定义统一错误码和服务异常。

包含：

```text
ErrorCode
ServiceException
```

设计约束：

1. 错误码使用枚举，不使用裸数字。
2. service 函数失败时抛出 `ServiceException`。
3. UI 层捕获异常后展示简短错误信息。
4. 详细异常仅写入本地日志或 SQLite 错误字段。

### 6.4 `schemas.py`

定义 Pydantic 数据结构，供 UI、service、数据库写入共用。

命名约束：

1. 请求结构统一使用 `*Request`。
2. 响应或结果结构统一使用 `*Response`。
3. 不使用 DTO、VO 命名。
4. 响应类启用 `from_attributes=True`。

建议包含：

```text
ApiDefinitionResponse
LoginRequest
ApiTestRequest
ApiTestResultResponse
TestRunSummaryResponse
```

### 6.5 `models.py`

定义 SQLAlchemy 数据库模型。

设计约束：

1. 主键字段统一为 `id`。
2. 主键类型使用 `BigInteger`。
3. 字段名使用小写下划线。
4. 所有字段写明 `comment`。
5. 包含 `created_at` 和 `updated_at`。
6. 不在 model 中编写业务逻辑。

### 6.6 `db.py`

负责 SQLite 连接、Session 创建和数据库初始化。

设计约束：

1. 使用 SQLAlchemy 2.0 风格。
2. 提供 `SessionLocal`。
3. 提供 `init_db()` 初始化表结构。
4. 提供 `commit_or_rollback(session)` 统一提交辅助函数。
5. 不在 UI 层直接提交事务。

### 6.7 `api_client.py`

负责 HTTP 请求封装：

- 拼接 `base_url + path`。
- 设置请求超时时间。
- 添加 `Authorization: Bearer <token>` 请求头。
- 统一捕获网络异常并转换为 `ServiceException`。
- 记录请求耗时。

该模块只负责 HTTP 协议细节，不负责测试流程编排和 SQLite 写入。

### 6.8 `services.py`

负责业务流程编排，采用模块函数形式，不创建 `ApiTestService` 类。

公共函数放在文件上部，辅助函数放在 `"""辅助函数"""` 分隔注释之后。

建议公共函数：

```text
load_api_definitions
create_test_run
execute_selected_apis
query_test_run_list
query_test_log_list
export_test_results
```

职责：

1. 接收用户勾选的 API 列表。
2. 判断是否需要登录。
3. 调用登录接口获取 token。
4. 按顺序请求被勾选接口。
5. 将响应转换为统一结果模型。
6. 写入 SQLite 测试日志。
7. 写操作负责调用 `commit_or_rollback()`。

### 6.9 `ui/main_window.py`

负责桌面界面展示与用户交互：

- 后端地址输入。
- 用户名和密码输入。
- API 勾选列表。
- 一键测试按钮。
- 本次测试结果表格。
- 响应详情查看区。
- 历史记录查看入口。

UI 层只负责界面事件和展示，不直接拼接业务结果，不直接操作 SQLAlchemy model，不直接提交数据库事务。

## 七、API 配置设计

接口列表建议放在 `local-api-tester/config/apis.json`。

```json
[
  {
    "key": "health",
    "name": "API 健康检查",
    "method": "GET",
    "path": "/actuator/health",
    "auth_required": false,
    "category": "基础检查"
  },
  {
    "key": "login",
    "name": "登录检查",
    "method": "POST",
    "path": "/api/auth/login",
    "auth_required": false,
    "category": "基础检查"
  },
  {
    "key": "device_gnss",
    "name": "GNSS 地表位移",
    "method": "GET",
    "path": "/api/device-monitor/gnss",
    "auth_required": true,
    "category": "设备监测"
  },
  {
    "key": "device_rain",
    "name": "雨水情",
    "method": "GET",
    "path": "/api/device-monitor/rain",
    "auth_required": true,
    "category": "设备监测"
  },
  {
    "key": "device_seepage",
    "name": "渗流渗压",
    "method": "GET",
    "path": "/api/device-monitor/seepage",
    "auth_required": true,
    "category": "设备监测"
  }
]
```

## 八、界面设计

### 8.1 主界面布局

```text
┌────────────────────────────────────────────────────────────┐
│ 后端地址 [http://localhost:8081              ]             │
│ 用户名   [admin                             ] 密码 [****]  │
├──────────────────────┬─────────────────────────────────────┤
│ API 选择             │ 本次测试结果                        │
│ [ ] API 健康检查     │ 接口名称 | 状态 | HTTP | 耗时 | 摘要 │
│ [ ] 登录检查         │                                     │
│ [ ] GNSS 地表位移    │                                     │
│ [ ] 雨水情           │                                     │
│ [ ] 渗流渗压         │                                     │
│                      │                                     │
│ [全选] [反选]        │                                     │
├──────────────────────┴─────────────────────────────────────┤
│ [一键测试] [清空结果] [查看历史] [导出本次结果]             │
├────────────────────────────────────────────────────────────┤
│ 响应详情 / 错误信息 / 原始 JSON                             │
└────────────────────────────────────────────────────────────┘
```

### 8.2 结果表格字段

结果表格展示字段：

- 接口名称
- 请求方法
- 状态
- HTTP 状态码
- 后端消息
- 请求耗时
- 设备摘要
- 错误原因

设备摘要格式：

```text
总数: 8, 在线: 8, 离线: 0, 异常: 0
```

### 8.3 响应详情区

用户点击结果表格某一行时，详情区展示：

- 请求 URL
- 请求头摘要
- 请求体
- 响应 JSON
- 异常信息

## 九、请求流程

### 9.1 一键测试流程

```text
用户点击一键测试
    ↓
读取后端地址、账号、密码、勾选接口
    ↓
创建 test_runs 测试批次
    ↓
判断是否需要登录
    ↓
如需要登录，先请求 POST /api/auth/login
    ↓
登录成功后保存本轮 token
    ↓
逐个请求被勾选接口
    ↓
每个接口完成后立即写入 api_test_logs
    ↓
实时刷新界面表格
    ↓
所有接口完成后更新 test_runs 汇总
```

### 9.2 登录规则

如果用户勾选了任意 `auth_required=true` 的接口，则必须先执行登录。

登录请求体：

```json
{
  "username": "用户输入的用户名",
  "password": "用户输入的密码"
}
```

登录成功后从响应中读取：

```text
data.token
```

请求认证接口时添加请求头：

```text
Authorization: Bearer <token>
```

### 9.3 成功判断规则

普通后端接口成功条件：

```text
HTTP 状态码为 200
响应 JSON 中 code 为 200
```

`/actuator/health` 成功条件：

```text
HTTP 状态码为 200
响应 JSON 中 status 为 UP
```

设备监测接口成功条件：

```text
HTTP 状态码为 200
响应 JSON 中 code 为 200
data.stats 存在
data.devices 存在
```

设备接口即使存在离线或采集异常设备，也不代表接口请求失败。此类情况应在设备摘要中展示，由使用者判断业务状态。

## 十、SQLite 设计

数据库文件建议放在：

```text
local-api-tester/data/api_test_logs.db
```

SQLite 只作为本工具的本地日志库使用，不连接现有业务数据库。数据库访问不使用裸 SQL 拼接作为主要实现方式，推荐通过 SQLAlchemy model 和 Session 完成读写。

数据库写入规则：

1. UI 层不直接写数据库。
2. `services.py` 负责创建测试批次、写入接口日志、更新批次汇总。
3. 写操作统一通过 `commit_or_rollback(session)` 提交。
4. 提交失败时回滚并抛出 `ServiceException(ErrorCode.INTERNAL_ERROR, "操作失败")`。
5. 查询历史记录时由 service 函数返回 Pydantic Response 对象，不向 UI 返回 ORM 实体。

### 10.1 测试批次表 `test_runs`

```sql
CREATE TABLE IF NOT EXISTS test_runs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    started_at TEXT NOT NULL,
    finished_at TEXT,
    base_url TEXT NOT NULL,
    selected_count INTEGER NOT NULL DEFAULT 0,
    success_count INTEGER NOT NULL DEFAULT 0,
    fail_count INTEGER NOT NULL DEFAULT 0,
    total_cost_ms INTEGER NOT NULL DEFAULT 0,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);
```

### 10.2 接口测试日志表 `api_test_logs`

```sql
CREATE TABLE IF NOT EXISTS api_test_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_id INTEGER NOT NULL,
    api_key TEXT NOT NULL,
    api_name TEXT NOT NULL,
    method TEXT NOT NULL,
    url TEXT NOT NULL,
    request_headers TEXT,
    request_body TEXT,
    http_status INTEGER,
    success INTEGER NOT NULL DEFAULT 0,
    cost_ms INTEGER NOT NULL DEFAULT 0,
    response_code INTEGER,
    response_message TEXT,
    response_body TEXT,
    error_message TEXT,
    summary_total INTEGER,
    summary_online INTEGER,
    summary_offline INTEGER,
    summary_abnormal INTEGER,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    FOREIGN KEY (run_id) REFERENCES test_runs(id)
);
```

### 10.3 SQLAlchemy 模型设计约束

实现时建议在 `models.py` 中定义两个模型：

```text
TestRun
ApiTestLog
```

模型约束：

1. 表名使用单数或明确业务名，本工具保留 `test_runs`、`api_test_logs` 两个日志表名。
2. 类名使用 PascalCase。
3. 字段名使用小写下划线。
4. 主键字段固定为 `id`。
5. 主键类型按规范使用 `BigInteger`，SQLite 下仍可映射为整数主键。
6. 状态字段如 `success` 使用 `SmallInteger`，含义为 `0=失败，1=成功`。
7. 所有字段添加 `comment`。
8. 时间字段包含 `created_at`、`updated_at`。
9. 不使用软删除，日志数据后续如需清理，提供明确的清理函数或删除历史功能。

## 十一、设备监测结果摘要

设备监测接口返回结构：

```text
data.stats:
- total
- online
- offline
- abnormal

data.devices:
- name
- type
- status
- lastCollectTime
- detail
```

测试工具需要从 `data.stats` 中提取摘要字段并写入 SQLite：

```text
summary_total
summary_online
summary_offline
summary_abnormal
```

完整响应仍保存到 `response_body`，用于排查具体设备状态。

## 十二、错误处理设计

### 12.1 网络错误

包括连接失败、连接超时、读取超时等。处理方式：

- 本条接口标记为失败。
- `http_status` 为空。
- `error_message` 记录异常信息。
- 界面状态显示为“请求失败”。

### 12.2 登录失败

如果登录失败：

- 登录接口本身写入日志。
- 依赖登录态的接口不再继续请求。
- 被跳过的接口标记为失败，错误原因记录为“登录失败，未执行请求”。

### 12.3 响应格式异常

如果 HTTP 成功但响应不是合法 JSON：

- 本条接口标记为失败。
- `response_body` 保存原始文本。
- `error_message` 记录“响应不是合法 JSON”。

### 12.4 后端业务失败

如果 HTTP 状态码为 200，但响应 `code` 不是 200：

- 本条接口标记为失败。
- `response_code` 保存后端 code。
- `response_message` 保存后端 message。

## 十三、配置设计

配置遵循统一入口原则。应用配置由 `settings.py` 读取，业务代码不直接读取环境变量或配置文件。

建议提供 `.env.example`：

```text
LOCAL_API_TESTER_BASE_URL=http://localhost:8081
LOCAL_API_TESTER_TIMEOUT_SECONDS=10
LOCAL_API_TESTER_DEFAULT_USERNAME=admin
LOCAL_API_TESTER_DATABASE_URL=sqlite:///data/api_test_logs.db
```

正式使用时可复制为：

```text
local-api-tester/.env
```

配置规则：

1. `settings.py` 中定义 `Settings(BaseSettings)`。
2. 所有配置字段必须有类型注解。
3. 全局只暴露一个 `settings` 实例。
4. 业务代码统一从 `settings` 读取配置。
5. 后端地址、超时时间、数据库地址可提供默认值。
6. 密码不写入配置文件，第一版由用户在界面输入。
7. API 列表仍放在 `config/apis.json`，由 `services.py` 通过统一函数读取。
8. 不在业务代码中硬编码后端地址、数据库路径、超时时间。

## 十四、后续扩展

第一版完成后，可按需扩展：

1. 打包为 Windows `.exe`。
2. 支持导出本次测试结果为 JSON。
3. 支持导出历史记录为 CSV。
4. 支持保存多个后端环境配置。
5. 支持接口分组折叠展示。
6. 支持查看设备明细列表。
7. 支持请求参数可视化编辑。
8. 支持清理历史日志。

## 十五、分阶段实施计划

本工具建议拆分为多个阶段分次完成。每个阶段都应尽量形成可检查、可演示的阶段性成果，避免一次性完成所有功能导致问题难以定位。

### 15.1 第一阶段：项目骨架与本地日志库（已完成 2026-05-06）

目标：创建独立 Python 项目基础结构，完成配置读取和 SQLite 日志库初始化。

交付内容：

1. 创建 `local-api-tester/` 独立项目目录。
2. 创建 `pyproject.toml`。
3. 创建基础包目录 `src/local_api_tester/`。
4. 创建 `.env.example`。
5. 创建 `config/apis.json`。
6. 实现 `settings.py` 统一配置入口。
7. 实现 SQLAlchemy SQLite 数据库初始化模块。
8. 创建 `test_runs` 和 `api_test_logs` 两张表。

涉及模块：

```text
local-api-tester/pyproject.toml
local-api-tester/.env.example
local-api-tester/config/apis.json
local-api-tester/src/local_api_tester/settings.py
local-api-tester/src/local_api_tester/exceptions.py
local-api-tester/src/local_api_tester/schemas.py
local-api-tester/src/local_api_tester/models.py
local-api-tester/src/local_api_tester/db.py
```

验收标准：

1. 能读取应用配置和 API 配置。
2. 能在 `data/` 目录下创建 SQLite 数据库文件。
3. 能创建测试批次表和接口测试日志表。
4. 数据库模型字段命名、时间字段、状态字段符合 Python 规范。
5. 暂不要求发起真实 API 请求。
6. 暂不要求实现桌面界面。

实际交付说明：

- `settings.py` 使用 `pydantic-settings BaseSettings`，环境变量前缀 `LOCAL_API_TESTER_`，全局暴露 `settings` 单例。
- `exceptions.py` 定义 `ErrorCode(IntEnum)` 和 `ServiceException`，其中原规范 `AI_SERVICE_ERROR(4001)` 适配为 `NETWORK_ERROR(4001)`（本工具无 AI 服务）。
- `schemas.py` 按规范分三个区域：辅助类 `DeviceStats`、请求类 `LoginRequest/ApiTestRequest`、响应类 `ApiDefinitionResponse/ApiTestResultResponse/TestRunSummaryResponse`，均启用 `from_attributes=True`。
- `models.py` 定义 `TestRun` 和 `ApiTestLog` 两个 SQLAlchemy 模型，主键 `BigInteger`，字段小写下划线，全部含 `comment`，包含 `created_at/updated_at`，SQLite 时间默认值使用 `func.strftime`。
- `db.py` 提供 `init_db()`、`get_session()`、`commit_or_rollback()`，会话异常时自动回滚。
- `config/apis.json` 包含 5 个 API 定义（健康检查、登录、GNSS、雨水情、渗流渗压）。
- 项目依赖在 `pyproject.toml` 中声明，使用 `src` 布局。

### 15.2 第二阶段：基础 API 请求能力（已完成 2026-05-06）

目标：完成 API 健康检查和登录检查，验证后端服务地址、账号密码、JWT 获取流程可用。

交付内容：

1. 实现 HTTP 请求客户端。
2. 实现统一请求结果模型。
3. 实现 `/actuator/health` 请求。
4. 实现 `/api/auth/login` 请求。
5. 登录成功后提取 `data.token`。
6. 将健康检查和登录检查结果写入 SQLite。

涉及模块：

```text
local-api-tester/src/local_api_tester/api_client.py
local-api-tester/src/local_api_tester/services.py
local-api-tester/src/local_api_tester/schemas.py
local-api-tester/src/local_api_tester/models.py
local-api-tester/src/local_api_tester/db.py
```

验收标准：

1. 能请求 `GET /actuator/health`。
2. 能请求 `POST /api/auth/login`。
3. 能判断健康检查是否 `UP`。
4. 能判断登录接口是否成功。
5. 能把请求耗时、HTTP 状态码、响应内容和错误信息写入 SQLite。
6. 暂不接入三类设备接口。
7. 暂不要求完整桌面界面，可先用最小入口触发一次请求。

实际交付说明：

- `api_client.py` 提供 `send_request()` 模块函数，封装 `requests.request()`，自动拼接 URL、添加 JWT 头、设置超时、捕获三类网络异常（连接失败、超时、其他异常）统一转为 `ServiceException(NETWORK_ERROR, ...)`，返回 `{http_status, response_body, cost_ms}`。
- `services.py` 采用模块函数设计，不创建 Service 类。公共函数包括 `load_api_definitions()`、`create_test_run()`、`execute_selected_apis()`，辅助函数以 `_` 前缀放在分隔注释之后。
- `execute_selected_apis()` 为核心编排入口，流程为：筛选选中 API → 创建 test_runs 批次 → 判断是否需要登录 → 执行登录提取 token → 逐个请求接口 → 每条完成后写入 api_test_logs → 更新批次汇总。
- 健康检查通过 `_parse_health_response()` 判断 `status == "UP"`，登录通过 `_do_login()` 判断 `code == 200` 并提取 `data.token`。
- 登录失败时依赖登录态的接口标记为失败，错误原因记录为"登录失败，未执行请求"，不继续请求。
- 网络异常不中断整个流程，单条失败后继续执行下一条。
- 所有数据库写操作统一通过 `commit_or_rollback()` 提交。
- 未修改第一阶段任何已有文件（settings/exceptions/schemas/models/db）。
- 预留设备监测接口扩展位置（`DeviceStats` 字段已对接，设备接口解析逻辑留到第三阶段实现）。

### 15.3 第三阶段：设备监测接口接入（已完成 2026-05-06）

目标：复用现有后端设备监测模块，完成三类设备接口请求与摘要解析。

交付内容：

1. 接入 `GET /api/device-monitor/gnss`。
2. 接入 `GET /api/device-monitor/rain`。
3. 接入 `GET /api/device-monitor/seepage`。
4. 请求设备接口前自动执行登录。
5. 请求设备接口时自动添加 `Authorization` 请求头。
6. 从 `data.stats` 中提取设备摘要。
7. 将 `total/online/offline/abnormal` 写入 SQLite。
8. 保存完整响应 JSON。

涉及模块：

```text
local-api-tester/src/local_api_tester/services.py
```

验收标准：

1. 能分别请求 GNSS、雨水情、渗流渗压三个接口。
2. 能正确携带 JWT。
3. 能区分”接口请求失败”和”接口成功但设备存在异常”。
4. 设备接口成功时能展示和记录设备摘要。
5. 登录失败时，依赖登录态的设备接口不继续请求，并记录失败原因。

实际交付说明：

- 仅修改 `services.py`，未新增或修改其他文件。
- 在 `_parse_response()` 中增加 `device_` 前缀判断，将设备监测接口路由到新增的 `_parse_device_response()` 函数。
- `_parse_device_response()` 实现设备接口专用解析逻辑：
  - 成功条件：HTTP 200 + 后端 code 200 + `data.stats` 存在 + `data.devices` 存在。
  - 成功时从 `data.stats` 提取 `total/online/offline/abnormal` 构造 `DeviceStats`。
  - 设备存在离线或异常不影响接口成功判定，仅在设备摘要中展示。
  - `data.stats` 或 `data.devices` 缺失时标记为失败，错误信息记录具体原因。
- `config/apis.json` 已在第一阶段包含三类设备接口定义，无需修改。
- `schemas.py` 的 `DeviceStats`、`models.py` 的 `summary_*` 字段、`_write_test_log()` 的摘要写入逻辑均在第一、二阶段预留到位，本阶段直接复用。
- 登录/token 携带/登录失败跳过等逻辑由第二阶段的 `execute_selected_apis()` 统一处理，设备接口无需额外处理。

### 15.4 第四阶段：桌面主界面（已完成 2026-05-06）

目标：实现 PySide6 桌面窗口，让用户可以勾选 API 并一键测试。

交付内容：

1. 创建桌面应用入口。
2. 实现主窗口布局。
3. 实现后端地址、用户名、密码输入。
4. 实现 API 勾选列表。
5. 实现全选和反选。
6. 实现一键测试按钮。
7. 实现本次测试结果表格。
8. 实现响应详情展示区。
9. 请求执行期间禁用重复点击。

涉及模块：

```text
local-api-tester/src/local_api_tester/main.py
local-api-tester/src/local_api_tester/ui/main_window.py
local-api-tester/src/local_api_tester/services.py
```

验收标准：

1. 双击或命令启动后出现独立桌面窗口。
2. 用户能选择一个或多个 API。
3. 点击”一键测试”后能看到本次测试结果。
4. 点击结果表格中的某一行能查看响应详情。
5. 测试结果同时写入 SQLite。
6. 软件关闭后不保留后台定时任务。

实际交付说明：

- `main.py` 为桌面应用入口，负责创建 `QApplication` 实例、调用 `init_db()` 初始化数据库、创建并显示 `MainWindow`、启动 Qt 事件循环。
- `ui/main_window.py` 实现完整的主窗口，包含：
  - 顶部配置区：后端地址输入框（预填 `settings.base_url`）、用户名输入框（预填 `settings.default_username`）、密码输入框（`Password` 模式）。
  - 左侧 API 勾选列表：按 `category` 分组展示接口，每个接口显示名称和方法路径，支持全选和反选。
  - 右侧测试结果表格：8 列（接口名称、请求方法、状态、HTTP、后端消息、耗时、设备摘要、错误原因），成功/失败以绿色/红色文字区分。
  - 按钮行：一键测试、清空结果、查看历史（第五阶段启用）、导出本次结果（第五阶段启用）。
  - 底部响应详情区：点击结果表格某行时展示请求 URL、HTTP 状态码、响应 JSON（格式化）、设备摘要、错误信息。
- `TestWorker(QThread)` 后台线程执行 `execute_selected_apis()`，通过 `Signal` 将结果传回主线程，避免 UI 卡顿。
- 执行测试期间禁用”一键测试”按钮并显示”测试中...”，完成后恢复。
- 前端校验：未勾选接口、未填后端地址、需要登录但未填用户名/密码时弹出提示。
- “查看历史”和”导出本次结果”按钮已预留位置但设为禁用，等待第五阶段实现。
- `pyproject.toml` 新增 `[project.scripts]` 入口点 `local-api-tester = “local_api_tester.main:main”`。
- 未修改前三阶段任何已有文件（settings/exceptions/schemas/models/db/api_client/services）。

### 15.5 第五阶段：历史记录与导出（已完成 2026-05-06）

目标：让使用者可以查看历史测试批次和接口明细，并导出本次测试结果。

交付内容：

1. 实现历史测试批次查询。
2. 实现历史接口明细查询。
3. 在界面中增加”查看历史”入口。
4. 支持按测试批次查看明细。
5. 支持导出本次测试结果为 JSON。

涉及模块：

```text
local-api-tester/src/local_api_tester/ui/main_window.py
local-api-tester/src/local_api_tester/services.py
```

验收标准：

1. 能查看最近测试批次。
2. 能查看某一批次下的接口请求明细。
3. 能导出本次测试结果。
4. 历史记录展示不影响一键测试主流程。

实际交付说明：

- `services.py` 新增三个公共函数：
  - `query_test_run_list(limit=50)`：查询 `test_runs` 表，按 `started_at` 降序排列，返回 `list[TestRunSummaryResponse]`。
  - `query_test_log_list(run_id)`：根据 `run_id` 查询 `api_test_logs` 表，将 `summary_*` 字段组装为 `DeviceStats`（非 None 时），返回 `list[ApiTestResultResponse]`。
  - `export_test_results(results, file_path)`：将 `list[ApiTestResultResponse]` 序列化为 JSON 写入文件。
- 三个函数均遵循规范：查询函数返回 Pydantic Response 而非 ORM 实体，异常统一抛出 `ServiceException`，查询函数使用 `session.close()` 而非 `commit_or_rollback()`。
- `ui/main_window.py` 新增 `HistoryDialog(QDialog)` 历史对话框：
  - 上半部分为批次列表表格（7 列：批次ID、开始时间、后端地址、选中数、成功、失败、耗时），打开时自动加载最近 50 条批次。
  - 下半部分为接口明细表格（8 列，与主界面结果表格一致），选中批次后加载该批次的接口测试明细。
  - 底部为响应详情区，点击接口明细某行时展示请求 URL、HTTP 状态码、响应 JSON、设备摘要、错误信息。
  - 底部提供关闭按钮。
- 主界面”查看历史”按钮已启用，点击打开 `HistoryDialog` 对话框。
- 主界面”导出本次结果”按钮已启用，弹出文件保存对话框（默认文件名 `api_test_results.json`），调用 `export_test_results()` 保存 JSON。
- 未修改其他文件（settings/exceptions/schemas/models/db/api_client）。

### 15.6 第六阶段：打包与交付整理

目标：整理使用说明，并按需打包为 Windows 可执行文件。

交付内容：

1. 补充 `README.md`。
2. 说明配置文件位置和字段含义。
3. 说明如何启动软件。
4. 说明如何查看 SQLite 日志。
5. 整理默认 API 配置。
6. 按需使用 PyInstaller 打包为 `.exe`。

涉及模块：

```text
local-api-tester/README.md
local-api-tester/pyproject.toml
local-api-tester/.env.example
local-api-tester/config/apis.json
```

验收标准：

1. 新使用者能根据 README 启动工具。
2. 默认 API 配置包含健康检查、登录、三类设备监测接口。
3. 打包产物可在目标 Windows 环境启动。
4. 打包不是第一版开发阻塞项，可在前五个阶段稳定后再做。

## 十六、实施顺序建议

1. 创建独立 Python 项目结构。
2. 定义 API 配置文件。
3. 实现 SQLite 初始化和日志写入。
4. 实现 HTTP 客户端。
5. 实现登录和 token 处理。
6. 实现 API 执行器。
7. 实现 PySide6 主界面。
8. 实现结果表格和详情区。
9. 实现历史记录查看。
10. 补充 README 使用说明。
