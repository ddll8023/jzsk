# 本地 API 检测工具

本地桌面工具，用于一键测试后端接口连通性、登录状态和设备监测接口状态。测试结果自动记录到本地 SQLite 数据库，便于后续排查。

## 功能概述

- 配置后端服务地址
- 输入登录账号和密码
- 勾选需要测试的 API（支持全选/反选）
- 一键测试所有勾选接口，实时展示结果
- 自动处理 JWT 登录态
- 展示设备监测接口的统计摘要（总数/在线/离线/异常）
- 查看历史测试批次和接口明细
- 导出本次测试结果为 JSON

## 环境要求

- Python 3.10+
- Windows 操作系统

## 安装步骤

### 方式一：使用 uv（推荐）

```bash
cd local-api-tester
uv sync
```

### 方式二：使用 pip

```bash
cd local-api-tester
pip install -e .
```

## 配置说明

### 环境配置

复制 `.env.example` 为 `.env`，按需修改：

```bash
cp .env.example .env
```

配置项说明：

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `LOCAL_API_TESTER_BASE_URL` | `http://localhost:8081` | 后端服务地址 |
| `LOCAL_API_TESTER_TIMEOUT_SECONDS` | `10` | 请求超时时间（秒） |
| `LOCAL_API_TESTER_DEFAULT_USERNAME` | `admin` | 默认登录用户名 |
| `LOCAL_API_TESTER_DATABASE_URL` | `sqlite:///data/api_test_logs.db` | SQLite 数据库路径 |

密码由界面输入，不写入配置文件。

### API 配置

接口列表定义在 `config/apis.json`，默认包含以下接口：

| key | 名称 | 方法 | 路径 | 需要登录 |
|-----|------|------|------|----------|
| `health` | API 健康检查 | GET | `/actuator/health` | 否 |
| `login` | 登录检查 | POST | `/api/auth/login` | 否 |
| `device_gnss` | GNSS 地表位移 | GET | `/api/device-monitor/gnss` | 是 |
| `device_rain` | 雨水情 | GET | `/api/device-monitor/rain` | 是 |
| `device_seepage` | 渗流渗压 | GET | `/api/device-monitor/seepage` | 是 |

如需新增或修改测试接口，直接编辑 `config/apis.json` 即可。

## 启动方式

### 方式一：命令行启动

```bash
cd local-api-tester
uv run local-api-tester
```

或安装后直接运行：

```bash
local-api-tester
```

### 方式二：Python 模块启动

```bash
cd local-api-tester
uv run python -m local_api_tester.main
```

## 使用流程

1. 启动后出现桌面窗口
2. 填写后端地址（默认 `http://localhost:8081`）
3. 填写用户名和密码（勾选了需要登录的接口时必须填写）
4. 在左侧勾选要测试的接口
5. 点击「一键测试」
6. 右侧表格实时展示每个接口的测试结果
7. 点击表格某一行，底部详情区展示完整的响应 JSON、设备摘要、错误信息
8. 点击「查看历史」可查看历史测试批次和接口明细
9. 点击「导出本次结果」可将当前测试结果保存为 JSON 文件

## SQLite 日志

测试日志保存在 `data/api_test_logs.db`，包含两张表：

- `test_runs`：测试批次记录
- `api_test_logs`：接口测试明细

查看日志：

```bash
# 使用 sqlite3 命令行工具
sqlite3 data/api_test_logs.db

# 查询最近 10 次测试批次
SELECT id, started_at, base_url, success_count, fail_count FROM test_runs ORDER BY started_at DESC LIMIT 10;

# 查询某批次的接口测试明细（替换 {run_id} 为实际批次 ID）
SELECT api_name, success, http_status, cost_ms, error_message FROM api_test_logs WHERE run_id = {run_id};
```

也可使用 [DB Browser for SQLite](https://sqlitebrowser.org/) 等图形化工具打开查看。

## 项目结构

```text
local-api-tester/
├── pyproject.toml          # 项目配置与依赖
├── .env.example            # 环境配置示例
├── config/
│   └── apis.json           # API 接口定义
├── data/
│   └── api_test_logs.db    # SQLite 日志库（运行后生成）
├── src/local_api_tester/
│   ├── main.py             # 应用入口
│   ├── settings.py         # 统一配置
│   ├── exceptions.py       # 错误码与异常
│   ├── schemas.py          # Pydantic 数据模型
│   ├── models.py           # SQLAlchemy 数据库模型
│   ├── db.py               # 数据库连接与会话
│   ├── api_client.py       # HTTP 请求客户端
│   ├── services.py         # 业务流程编排
│   └── ui/
│       └── main_window.py  # PySide6 主窗口
└── logs/                   # 日志目录（预留）
```

## 打包为 exe

安装打包依赖：

```bash
uv sync --extra packaging
```

打包命令：

```bash
pyinstaller --name "本地API检测工具" --windowed --onedir src/local_api_tester/main.py
```

打包时需要将 `config/apis.json` 一同打包或放在 exe 同级目录下。打包产物在 `dist/` 目录中。
