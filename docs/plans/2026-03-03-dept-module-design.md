# 部门管理模块设计文档

> 创建日期：2026-03-03

## 1. 模块概述

**模块名称：** 部门管理（dept）
**功能：** 部门信息的增删改查
**数据源：** jcxx 数据库

## 2. API清单

| 方法 | 路径 | 说明 |
|-----|------|------|
| GET | /department/list | 获取部门列表（分页，支持名称搜索） |
| GET | /department/{id} | 获取部门详情 |
| POST | /department/save | 新增部门 |
| PUT | /department/update | 更新部门信息 |
| DELETE | /department/{id} | 删除部门 |

## 3. 数据模型

### 3.1 数据库表

表名：`department`

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | bigint | 主键，自增 |
| department_name | varchar(100) | 部门名称 |
| department_responsibility | varchar(500) | 部门职责 |
| level | varchar(50) | 部门级别 |
| company | varchar(100) | 所属公司 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 修改时间 |

### 3.2 实体类

```java
public class Department {
    private Long id;
    private String departmentName;
    private String departmentResponsibility;
    private String level;
    private String company;
    private Date createTime;
    private Date updateTime;
}
```

## 4. DTO设计

### 4.1 DeptDTO（新增/更新）

```java
public class DeptDTO {
    @Schema(description = "部门名称", required = true, example = "技术部")
    @NotBlank(message = "部门名称不能为空")
    private String departmentName;

    @Schema(description = "部门职责", required = true, example = "负责系统开发")
    @NotBlank(message = "部门职责不能为空")
    private String departmentResponsibility;

    @Schema(description = "部门级别", example = "一级部门")
    private String level;

    @Schema(description = "所属公司", example = "水利局")
    private String company;
}
```

### 4.2 DeptQueryDTO（列表查询）

```java
public class DeptQueryDTO {
    @Schema(description = "当前页码", required = true, example = "1")
    @NotBlank(message = "当前页码不能为空")
    private Integer currentPage;

    @Schema(description = "每页条数", required = true, example = "10")
    @NotBlank(message = "每页条数不能为空")
    private Integer pageSize;

    @Schema(description = "部门名称（模糊搜索）", example = "技术")
    private String departmentName;
}
```

### 4.3 DeptVO（响应）

```java
public class DeptVO {
    private Long id;
    private String departmentName;
    private String departmentResponsibility;
    private String level;
    private String company;
    private Date createTime;
    private Date updateTime;
}
```

## 5. 组件设计

### 5.1 Mapper

- `DeptMapper.java` - 接口
- `DeptMapper.xml` - SQL映射

### 5.2 Service

- `DeptService.java` - 接口
- `DeptServiceImpl.java` - 实现

### 5.3 Controller

- `DeptController.java` - 控制器

## 6. 异常处理

| 场景 | 异常 | 状态码 |
|-----|------|-------|
| 部门不存在 | BusinessException | 404 |
| 部门名称为空 | ValidationException | 400 |
| 部门职责为空 | ValidationException | 400 |

## 7. 安全配置

需要配置Spring Security放行以下路径：
- GET /department/list
- GET /department/{id}

其他接口需要认证。
