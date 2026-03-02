# 前后端联调自动化测试实施计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 使用 Playwright MCP 完成认证、角色、用户管理三大模块的前后端联调测试并生成测试报告

**Architecture:** 模块顺序测试方案 - 先启动后端服务，使用 Playwright MCP 自动化测试前端页面，逐个验证20个API端点的正常流程和异常场景

**Tech Stack:** Playwright MCP, Spring Boot 2.6.2, JWT认证, MyBatis

---

## Task 1: 环境准备 - 创建日志目录

**Files:**
- Create: `backend/szy-new/logs/`

**Step 1: 创建日志目录**

```bash
mkdir -p backend/szy-new/logs
```

**Step 2: 验证目录创建成功**

```bash
ls -la backend/szy-new/logs
```

Expected: 目录存在且可写

**Step 3: Commit**

```bash
git add .gitignore
git commit -m "chore: 创建测试日志目录" || echo "No changes to commit"
```

---

## Task 2: 环境准备 - 启动后端服务

**Files:**
- Monitor: `backend/szy-new/logs/app.log`

**Step 1: 后台启动 Spring Boot**

```bash
cd backend/szy-new && mvn spring-boot:run -s D:/demo/java/jzsk/backend/szy/maven-settings.xml > logs/app.log 2>&1 &
```

**Step 2: 等待启动完成（30秒）**

```bash
sleep 30 && tail -n 20 backend/szy-new/logs/app.log
```

Expected: 日志显示 "Started SzyApplication" 或端口8081已监听

**Step 3: 验证服务端口**

```bash
netstat -ano | findstr :8081 || curl -s http://localhost:8081/actuator/health || echo "Service not ready"
```

Expected: 端口8081处于LISTENING状态或健康检查返回200

---

## Task 3: 环境准备 - 验证前端服务

**Step 1: 使用 Playwright 导航到前端**

使用 `mcp__playwright__browser_navigate` 工具访问:
```
http://localhost:8084
```

**Step 2: 捕获页面快照**

使用 `mcp__playwright__browser_snapshot` 工具验证登录页面是否正常显示

Expected: 页面包含登录表单，有用户名和密码输入框

**Step 3: 记录环境状态**

在测试报告中记录:
- 前端URL: http://localhost:8084
- 后端URL: http://localhost:8081
- 环境状态: ✅ 就绪

---

## Task 4: 认证模块测试 - 登录（正常流程）

**API:** POST /login

**Step 1: 填写登录表单**

使用 `mcp__playwright__browser_fill_form` 工具:
```json
{
  "fields": [
    {
      "name": "用户名",
      "type": "textbox",
      "ref": "[找到用户名输入框的ref]",
      "value": "admin01"
    },
    {
      "name": "密码",
      "type": "textbox",
      "ref": "[找到密码输入框的ref]",
      "value": "Jzsk@123456"
    }
  ]
}
```

**Step 2: 点击登录按钮**

使用 `mcp__playwright__browser_click` 工具点击登录按钮

**Step 3: 等待响应并捕获快照**

使用 `mcp__playwright__browser_wait_for` 等待页面跳转或提示

**Step 4: 验证登录成功**

使用 `mcp__playwright__browser_snapshot` 检查是否跳转到主页或显示用户信息

**Step 5: 检查后端日志**

```bash
tail -n 50 backend/szy-new/logs/app.log | grep -A 5 -B 5 "login\|认证\|token"
```

**Step 6: 记录测试结果**

在测试报告中记录:
- 测试场景: ✅ 正常登录
- 响应状态: [记录HTTP状态]
- JWT Token: [记录Token是否存在]
- 后端日志: [关键日志片段]

---

## Task 5: 认证模块测试 - 登录（异常：错误密码）

**API:** POST /login

**Step 1: 登出当前用户（如需要）**

如果已登录，先点击登出按钮或清除浏览器状态

**Step 2: 填写错误密码**

使用 `mcp__playwright__browser_fill_form` 工具:
```json
{
  "fields": [
    {
      "name": "用户名",
      "type": "textbox",
      "ref": "[用户名输入框ref]",
      "value": "admin01"
    },
    {
      "name": "密码",
      "type": "textbox",
      "ref": "[密码输入框ref]",
      "value": "wrongpassword"
    }
  ]
}
```

**Step 3: 点击登录**

**Step 4: 验证错误提示**

捕获页面快照，检查是否显示错误提示（如"用户名或密码错误"）

**Step 5: 检查后端日志**

```bash
tail -n 50 backend/szy-new/logs/app.log | grep -i "error\|fail\|exception"
```

**Step 6: 记录测试结果**

- 测试场景: ❌ 错误密码
- 预期行为: 拒绝登录，显示错误提示
- 实际行为: [记录实际结果]
- 结论: [✅ 符合预期 / ❌ 不符合预期]

---

## Task 6: 认证模块测试 - 登录（异常：空用户名）

**API:** POST /login

**Step 1: 清空表单**

刷新页面或清空输入框

**Step 2: 只填写密码**

```json
{
  "fields": [
    {
      "name": "密码",
      "type": "textbox",
      "ref": "[密码输入框ref]",
      "value": "Jzsk@123456"
    }
  ]
}
```

**Step 3: 尝试提交登录**

**Step 4: 验证前端验证或后端错误**

检查是否阻止提交或显示"用户名不能为空"提示

**Step 5: 记录测试结果**

---

## Task 7: 认证模块测试 - 获取用户信息（正常流程）

**API:** GET /user/info

**前置条件:** 已使用 admin01 登录成功

**Step 1: 导航到用户信息页面**

如果页面有用户信息入口，点击进入；否则直接调用API

**Step 2: 使用浏览器开发者工具监控网络**

使用 `mcp__playwright__browser_network_requests` 工具查看API请求

**Step 3: 捕获用户信息显示**

使用 `mcp__playwright__browser_snapshot` 查看页面是否显示用户信息（姓名、角色等）

**Step 4: 验证响应数据**

检查响应JSON是否包含:
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "username": "admin01",
    "name": "管理员"
  }
}
```

**Step 5: 记录测试结果**

---

## Task 8: 认证模块测试 - 获取用户信息（异常：无Token）

**API:** GET /user/info

**Step 1: 清除JWT Token**

使用 `mcp__playwright__browser_evaluate` 工具执行:
```javascript
() => {
  localStorage.removeItem('token');
  sessionStorage.removeItem('token');
}
```

**Step 2: 刷新页面或重新请求用户信息**

**Step 3: 验证重定向到登录页**

捕获快照，检查是否跳转到登录页或显示"未登录"提示

**Step 4: 检查后端日志**

```bash
tail -n 30 backend/szy-new/logs/app.log | grep -i "unauthorized\|401\|认证失败"
```

**Step 5: 记录测试结果**

---

## Task 9: 认证模块测试 - 修改密码（正常流程）

**API:** POST /user/updatePassword

**前置条件:** 重新登录为 admin01

**Step 1: 导航到修改密码页面**

找到修改密码入口并点击

**Step 2: 填写修改密码表单**

```json
{
  "fields": [
    {
      "name": "旧密码",
      "type": "textbox",
      "ref": "[旧密码输入框ref]",
      "value": "Jzsk@123456"
    },
    {
      "name": "新密码",
      "type": "textbox",
      "ref": "[新密码输入框ref]",
      "value": "Jzsk@123456"
    },
    {
      "name": "确认密码",
      "type": "textbox",
      "ref": "[确认密码输入框ref]",
      "value": "Jzsk@123456"
    }
  ]
}
```

**Step 3: 提交修改**

**Step 4: 验证成功提示**

检查是否显示"密码修改成功"提示

**Step 5: 记录测试结果**

---

## Task 10: 认证模块测试 - 登出

**API:** POST /logout

**Step 1: 点击登出按钮**

在页面找到登出/退出登录按钮并点击

**Step 2: 验证登出成功**

捕获快照，确认跳转到登录页

**Step 3: 验证Token已清除**

使用 `mcp__playwright__browser_evaluate` 检查:
```javascript
() => {
  return !localStorage.getItem('token') && !sessionStorage.getItem('token');
}
```

**Step 4: 记录测试结果**

---

## Task 11: 角色管理测试 - 获取角色列表

**API:** GET /role/list

**前置条件:** 以 admin01 登录

**Step 1: 导航到角色管理页面**

找到角色管理菜单并点击

**Step 2: 捕获角色列表**

使用 `mcp__playwright__browser_snapshot` 查看角色列表表格

**Step 3: 检查网络请求**

使用 `mcp__playwright__browser_network_requests` 确认调用了 `/role/list` API

**Step 4: 验证响应数据格式**

检查是否包含分页信息和角色列表

**Step 5: 记录测试结果**

---

## Task 12: 角色管理测试 - 新增角色

**API:** POST /role/save

**Step 1: 点击新增角色按钮**

在角色管理页面找到"新增"按钮

**Step 2: 填写角色表单**

```json
{
  "fields": [
    {
      "name": "角色名称",
      "type": "textbox",
      "ref": "[角色名称输入框ref]",
      "value": "测试角色20260302"
    },
    {
      "name": "角色编码",
      "type": "textbox",
      "ref": "[角色编码输入框ref]",
      "value": "TEST_20260302"
    },
    {
      "name": "备注",
      "type": "textbox",
      "ref": "[备注输入框ref]",
      "value": "自动化测试创建的角色"
    }
  ]
}
```

**Step 3: 提交表单**

**Step 4: 验证创建成功**

检查是否显示成功提示，并在列表中出现"测试角色20260302"

**Step 5: 查询验证数据**

刷新列表或在列表中搜索"测试角色20260302"

**Step 6: 检查后端日志**

```bash
tail -n 30 backend/szy-new/logs/app.log | grep -A 3 "INSERT INTO.*role"
```

**Step 7: 记录测试结果和角色ID**

记录创建的角色ID，后续测试需要使用

---

## Task 13: 角色管理测试 - 获取角色详情

**API:** GET /role/{id}

**Step 1: 点击刚创建的角色查看详情**

在角色列表中找到"测试角色20260302"，点击查看或编辑按钮

**Step 2: 捕获详情页快照**

**Step 3: 验证详情数据**

确认显示的数据与创建时填写的一致:
- 角色名称: 测试角色20260302
- 角色编码: TEST_20260302

**Step 4: 记录测试结果**

---

## Task 14: 角色管理测试 - 更新角色

**API:** PUT /role/update

**Step 1: 进入编辑模式**

点击编辑按钮

**Step 2: 修改角色名称**

```json
{
  "fields": [
    {
      "name": "角色名称",
      "type": "textbox",
      "ref": "[角色名称输入框ref]",
      "value": "测试角色20260302-已修改"
    }
  ]
}
```

**Step 3: 提交更新**

**Step 4: 验证更新成功**

刷新列表，确认角色名称已变为"测试角色20260302-已修改"

**Step 5: 检查后端日志**

```bash
tail -n 30 backend/szy-new/logs/app.log | grep -A 3 "UPDATE.*role"
```

**Step 6: 记录测试结果**

---

## Task 15: 角色管理测试 - 分配权限

**API:** POST /role/assignMenus

**Step 1: 选择角色并进入权限分配**

找到"测试角色20260302-已修改"，点击"分配权限"按钮

**Step 2: 选择菜单权限**

在权限树中勾选几个菜单项

**Step 3: 提交权限分配**

**Step 4: 验证分配成功**

显示成功提示

**Step 5: 验证权限已保存**

使用 API GET /role/menuIds/{id} 查询权限列表

**Step 6: 记录测试结果**

---

## Task 16: 角色管理测试 - 删除角色

**API:** DELETE /role/{id}

**Step 1: 删除测试角色**

找到"测试角色20260302-已修改"，点击删除按钮

**Step 2: 确认删除**

在确认对话框中点击"确定"

**Step 3: 验证删除成功**

检查角色列表中是否已不存在该角色

**Step 4: 检查后端日志**

```bash
tail -n 30 backend/szy-new/logs/app.log | grep -A 3 "DELETE FROM.*role"
```

**Step 5: 记录测试结果**

---

## Task 17: 用户管理测试 - 获取用户列表

**API:** GET /user/list

**Step 1: 导航到用户管理页面**

找到用户管理菜单并点击

**Step 2: 捕获用户列表快照**

**Step 3: 验证列表数据**

检查是否显示用户表格，包含用户名、姓名、状态等字段

**Step 4: 记录测试结果**

---

## Task 18: 用户管理测试 - 新增用户

**API:** POST /user/save

**Step 1: 点击新增用户按钮**

**Step 2: 填写用户表单**

```json
{
  "fields": [
    {
      "name": "用户名",
      "type": "textbox",
      "ref": "[用户名输入框ref]",
      "value": "testuser20260302"
    },
    {
      "name": "姓名",
      "type": "textbox",
      "ref": "[姓名输入框ref]",
      "value": "测试用户20260302"
    },
    {
      "name": "手机号",
      "type": "textbox",
      "ref": "[手机号输入框ref]",
      "value": "13800138000"
    },
    {
      "name": "邮箱",
      "type": "textbox",
      "ref": "[邮箱输入框ref]",
      "value": "test@example.com"
    },
    {
      "name": "密码",
      "type": "textbox",
      "ref": "[密码输入框ref]",
      "value": "Test@123456"
    }
  ]
}
```

**Step 3: 提交创建用户**

**Step 4: 验证创建成功**

在用户列表中查找"testuser20260302"

**Step 5: 检查后端日志**

```bash
tail -n 30 backend/szy-new/logs/app.log | grep -A 5 "INSERT INTO.*user"
```

**Step 6: 记录用户ID**

记录创建的用户ID，后续测试使用

---

## Task 19: 用户管理测试 - 更新用户

**API:** PUT /user/update

**Step 1: 编辑测试用户**

找到"testuser20260302"，点击编辑

**Step 2: 修改姓名**

```json
{
  "fields": [
    {
      "name": "姓名",
      "type": "textbox",
      "ref": "[姓名输入框ref]",
      "value": "测试用户20260302-已修改"
    }
  ]
}
```

**Step 3: 提交更新**

**Step 4: 验证更新成功**

刷新列表，确认姓名已修改

**Step 5: 记录测试结果**

---

## Task 20: 用户管理测试 - 分配角色

**API:** POST /user/assignRoles

**Step 1: 选择用户并分配角色**

找到"testuser20260302-已修改"，点击"分配角色"

**Step 2: 选择角色**

在角色列表中勾选一个或多个角色

**Step 3: 提交分配**

**Step 4: 验证分配成功**

使用 API GET /user/roleIds/{id} 查询用户角色

**Step 5: 记录测试结果**

---

## Task 21: 用户管理测试 - 重置密码

**API:** POST /user/resetPassword

**Step 1: 点击重置密码**

找到"testuser20260302-已修改"，点击"重置密码"

**Step 2: 确认重置**

在确认对话框中点击"确定"

**Step 3: 验证重置成功**

显示成功提示，记录默认密码

**Step 4: 检查后端日志**

```bash
tail -n 30 backend/szy-new/logs/app.log | grep -i "password\|密码"
```

**Step 5: 记录测试结果**

---

## Task 22: 用户管理测试 - 禁用用户

**API:** POST /user/status

**Step 1: 点击禁用按钮**

找到"testuser20260302-已修改"，点击"禁用"或状态切换按钮

**Step 2: 确认禁用**

**Step 3: 验证状态变更**

用户状态变为"已禁用"

**Step 4: 记录测试结果**

---

## Task 23: 用户管理测试 - 删除用户

**API:** DELETE /user/{id}

**Step 1: 删除测试用户**

找到"testuser20260302-已修改"，点击删除

**Step 2: 确认删除**

**Step 3: 验证删除成功**

用户列表中不再显示该用户

**Step 4: 检查后端日志**

```bash
tail -n 30 backend/szy-new/logs/app.log | grep -A 3 "DELETE FROM.*user"
```

**Step 5: 记录测试结果**

---

## Task 24: 异常场景测试 - 权限验证

**Step 1: 创建普通用户并登录**

如果需要测试权限，创建一个无管理员权限的用户

**Step 2: 尝试访问管理接口**

访问角色管理或用户管理页面

**Step 3: 验证权限拒绝**

检查是否显示"无权限"提示或隐藏管理菜单

**Step 4: 检查后端日志**

```bash
tail -n 30 backend/szy-new/logs/app.log | grep -i "forbidden\|403\|权限"
```

**Step 5: 记录测试结果**

---

## Task 25: 异常场景测试 - 数据验证

**Step 1: 测试新增角色名称重复**

尝试创建已存在的角色名称，验证是否提示"角色名称已存在"

**Step 2: 测试新增用户名重复**

尝试创建已存在的用户名，验证是否提示"用户名已存在"

**Step 3: 测试邮箱格式错误**

输入错误的邮箱格式（如"abc"），验证前端或后端是否拦截

**Step 4: 记录所有异常测试结果**

---

## Task 26: 生成测试报告

**Files:**
- Create: `docs/test-report-2026-03-02.md`

**Step 1: 汇总测试统计**

统计:
- 认证模块: X成功 / Y失败
- 角色管理: X成功 / Y失败
- 用户管理: X成功 / Y失败
- 总计: X成功 / Y失败 / Z通过率

**Step 2: 编写测试报告**

创建完整的测试报告文档，包含:
- 测试环境
- 测试总览表格
- 详细测试结果
- 发现的问题表格
- 改进建议
- 后端日志片段

**Step 3: 提交测试报告**

```bash
git add docs/test-report-2026-03-02.md
git commit -m "test: 添加前后端联调测试报告"
```

---

## Task 27: 更新开发状态文档

**Files:**
- Modify: `docs/开发状态.md`

**Step 1: 添加测试验证章节**

在开发状态文档中添加:
```markdown
## 测试验证

### 前后端联调测试（2026-03-02）

**测试范围:** 认证、角色、用户管理模块

**测试结果:**
- 总计: 20个API
- 成功: X个
- 失败: Y个
- 通过率: Z%

**发现的问题:**
[列出主要问题]

**测试报告:** `docs/test-report-2026-03-02.md`
```

**Step 2: 提交更新**

```bash
git add docs/开发状态.md
git commit -m "docs: 更新开发状态，添加联调测试结果"
```

---

## Task 28: 清理测试环境

**Step 1: 停止后端服务**

```bash
# 查找Spring Boot进程
jps -l | grep SzyApplication

# 或者使用端口查找
netstat -ano | findstr :8081

# 终止进程（替换<PID>为实际进程ID）
taskkill /F /PID <PID>
```

**Step 2: 归档日志文件**

```bash
# 如果需要，压缩日志文件
tar -czf backend/szy-new/logs/app-2026-03-02.tar.gz backend/szy-new/logs/app.log
```

**Step 3: 记录清理完成**

在测试报告中添加"环境已清理"说明

---

## 执行说明

**前置条件:**
- 前端已启动在 http://localhost:8084
- 数据库已配置并包含初始数据（admin01用户）
- Maven已配置正确的settings文件

**测试凭证:**
- 管理员: admin01 / Jzsk@123456

**预计测试时间:** 30-45分钟

**风险提示:**
- 某个API失败可能影响后续测试（模块独立性设计降低了此风险）
- 测试数据可能与现有数据冲突（使用唯一标识避免）

**成功标准:**
- 所有20个API正常流程测试通过
- 异常场景被正确拦截并返回友好提示
- 测试报告完整记录所有测试结果