# CLAUDE.md 编写规范

> 本文档整理自 Anthropic 官方文档，为 Claude Code 项目提供 CLAUDE.md 编写指导。

---

## 一、官方文档来源

| 文档 | URL |
|------|-----|
| Claude Code 官方概览 | https://docs.anthropic.com/en/docs/claude-code |
| Claude Code 最佳实践 | https://docs.anthropic.com/en/docs/claude-code/best-practices |
| Claude Code 官方 GitHub | https://github.com/anthropics/claude-code |
| Claude Code 官方概述 | https://code.claude.com/docs/en/overview |

---

## 二、CLAUDE.md 文件作用

CLAUDE.md 是一个**特殊文件**，Claude Code 在每次对话开始时都会自动读取。

**用途：**
- 提供 Claude 无法从代码本身推断的持久上下文
- 定义 Bash 命令（Claude 无法猜测的特殊命令）
- 设置代码风格规则（与默认设置不同）
- 声明工作流规则

---

## 三、快速启动

### 使用 /init 命令

官方推荐使用 `/init` 命令生成初始模板：

```bash
/init
```

该命令会分析代码库结构，自动检测：
- 构建系统（Gradle、Maven、npm 等）
- 测试框架（JUnit、pytest、Jest 等）
- 代码模式

---

## 四、应该包含的内容

| 内容类型 | 示例 |
|---------|------|
| **Claude 无法猜测的 Bash 命令** | 特定的构建/部署/启动命令 |
| **代码风格规则** | 强制使用 ES modules 而非 CommonJS |
| **测试说明** | "运行 jest 而非整个测试套件" |
| **仓库约定** | 分支命名、PR 规范 |
| **架构决策** | 非显而易见的项目特定决策 |
| **开发环境怪癖** | 必需的 env 变量 |
| **常见陷阱** | 非显而易见的警告 |

---

## 五、应该排除的内容

| 内容类型 | 说明 |
|---------|------|
| Claude 可推断的内容 | 如代码位置、结构 |
| 标准语言约定 | Claude 已默认知道 |
| 详细 API 文档 | 链接到外部文档即可 |
| 频繁变更的信息 | 如当前 sprint 的任务 |
| 长篇解释或教程 | 保持简洁 |
| 文件逐个描述 | 不要做代码库地图 |
| 不言自明的做法 | 如 "write clean code" |

---

## 六、文件放置位置

| 位置 | 作用范围 |
|------|---------|
| `~/.claude/CLAUDE.md` | 全局 - 所有 Claude 会话 |
| `./CLAUDE.md` (项目根目录) | 项目级 - 纳入 git 与团队共享 |
| 父目录 | Monorepo 场景 - 自动同时加载 |
| `./src/CLAUDE.md` | 目录级 - 仅在该目录工作时加载 |

---

## 七、导入语法

CLAUDE.md 支持 `@path/to/file` 语法导入其他文件：

```markdown
See @README.md for project overview
See @package.json for available npm commands
See @规范文档/后端规范文档.md for coding standards
```

---

## 八、示例结构

### 简洁示例（官方推荐）

```markdown
# Code style
- Use ES modules (import/export) syntax, not CommonJS
- Destructure imports when possible

# Workflow
- Be sure to typecheck when you're done making changes
- Prefer running single tests for performance
```

### 本项目示例

```markdown
# 项目概述
HR Assistant 闸门监控系统

# 代码规范
- 遵循 规范文档/后端规范文档.md
- DTO 必须添加 @Schema 注解

# Bash 命令
- 启动: ./mvnw spring-boot:run
- 测试: ./mvnw test

# 工作流
- 每次提交前执行 verification-before-completion
- 代码审查使用 requesting-code-review
```

---

## 九、最佳实践

### 1. 保持简短精炼

**官方建议：** 没有强制格式要求，但必须保持简短和人类可读。

**自检问题：** 对每个条款自问："删除这条会导致 Claude 犯错吗？"
- 如果不会 → 删除它

### 2. 使用强调标记

添加 **IMPORTANT** 或 **YOU MUST** 等标记可提高指令遵循度：

```markdown
**YOU MUST** 按照以下流程执行所有任务。
**IMPORTANT** 只有回复"嘻嘻"才执行代码修改。
```

### 3. 纳入版本控制

**强烈建议** 将 CLAUDE.md 纳入 git，团队成员可以共同改进。

### 4. 定期审查和修剪

| 症状 | 问题原因 |
|------|---------|
| Claude 持续做你不希望的事 | 文件太长，规则被淹没 |
| Claude 询问已有答案的问题 | 措辞不明确 |

---

## 十、CLAUDE.md vs Skills

| 功能 | 加载时机 | 适用场景 |
|------|---------|---------|
| **CLAUDE.md** | 每次会话都加载 | 广泛适用的规则 |
| **Skills** (`.claude/skills/`) | 按需加载 | 偶尔相关的领域知识 |

---

## 十一、相关官方功能

| 功能 | 配置文件 | 用途 |
|------|---------|------|
| **Hooks** | `.claude/settings.json` | 每次会话必须执行的操作 |
| **Subagents** | `.claude/agents/` | 专业化的独立助手 |
| **MCP Servers** | - | 外部工具集成 |
| **Plugins** | - | 社区和官方打包的技能 |

---

## 十二、参考资料

### 官方文档
- [Claude Code Best Practices](https://docs.anthropic.com/en/docs/claude-code/best-practices)
- [Claude Code Overview](https://code.claude.com/docs/en/overview)
- [Anthropic Documentation](https://docs.anthropic.com/en/docs)

### GitHub
- [Claude Code GitHub Repository](https://github.com/anthropics/claude-code)

### Claude Code CLI
- `/init` - 初始化项目
- `/help` - 获取帮助
- `/skills` - 查看可用技能

---

## 十三、本项目 CLAUDE.md 编写规范

### 思考与执行流程

```
用户输入 → 思考链分析 → 调用 Skill → 展示方案 → 等待"嘻嘻"
```

### 思考链（必须逐层执行）

```
第零层：读取 doc/ 和 规范文档/ 目录
  ↓
第一层：理解用户意图
  ↓
第二层：识别任务性质
  ↓
第三层：识别任务阶段
  ↓
第四层：识别具体问题类型
  ↓
第五层：确定最终 Skill
  ↓
第六层：确认方案（Skill 调用后才能展示）
```

### 核心 Skill 索引

| 分类 | Skill | 用途 |
|------|-------|------|
| 核心开发 | `test-driven-development` | 新功能开发前 |
| 核心开发 | `systematic-debugging` | Bug 修复 |
| 核心开发 | `verification-before-completion` | 完成前验证 |
| 核心开发 | `requesting-code-review` | 代码审查 |
| 代码质量 | `simplify` | 精简冗余 |
| 代码质量 | `harden` | 边界处理 |
| 前端 UI | `arrange` | 布局优化 |
| 前端 UI | `colorize` | 颜色优化 |

---

*文档版本：2026-03-28*
*来源：Anthropic 官方 Claude Code 文档*
