# API清单

本文档由前端V2代码提取，作为后端重构的参考。

**提取日期**: 2026-03-01
**前端路径**: `frontendV2/src/api/`

---

## 认证模块 (auth)

| 方法 | 路径 | 用途 |
|-----|------|------|
| POST | /login | 用户登录 |
| GET | /user/userInfo | 获取当前用户信息 |
| POST | /user/updatePass | 修改密码 |

---

## 用户管理模块 (user)

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /user/list | 获取用户列表（分页） |
| GET | /user/search-list | 搜索用户（按姓名） |
| GET | /user/info/{id} | 获取用户详情 |
| POST | /user/save | 新增用户 |
| POST | /user/update | 更新用户信息 |
| POST | /user/delete/{id} | 删除用户 |
| POST | /user/role/{userId} | 分配角色 |
| POST | /user/repass | 初始化密码（重置为123456） |
| PUT | /user/updatePassword | 修改当前用户密码 |
| GET | /role/list | 获取角色列表 |

---

## 角色管理模块 (role)

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /role/list | 获取角色列表（分页） |
| GET | /role/info/{id} | 获取角色详情 |
| POST | /role/save | 新增角色 |
| POST | /role/update | 更新角色信息 |
| POST | /role/delete/{id} | 删除角色 |
| POST | /role/menu/{roleId} | 分配菜单权限 |
| GET | /role/menus/{roleId} | 获取角色已分配的菜单ID列表 |

---

## 菜单管理模块 (menu)

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | (静态数据) | 获取菜单树列表（用于管理页面） |
| POST | (静态数据) | 新增菜单（静态数据，空操作） |
| PUT | (静态数据) | 更新菜单（静态数据，空操作） |
| DELETE | (静态数据) | 删除菜单（静态数据，空操作） |

> **注意**: 菜单模块目前使用静态数据，无实际API调用。

---

## 部门管理模块 (dept)

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /department/list | 获取部门列表（分页） |
| GET | /department/{id} | 获取部门详情 |
| POST | /department/save | 新增部门 |
| PUT | /department/update | 更新部门信息 |
| DELETE | /department/{id} | 删除部门 |

---

## 字典管理模块 (dict)

### 字典主表接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /dict/list | 分页查询字典列表 |
| GET | /dict/info/{id} | 获取字典详情 |
| POST | /dict/save | 新增字典 |
| POST | /dict/update | 更新字典 |
| POST | /dict/delete/{id} | 删除字典 |
| GET | /dict/kinds | 获取字典选项（树形结构） |
| GET | /dict/LVs | 获取字典选项（扁平结构） |

### 字典详情接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /dict-detail/info/{id} | 获取字典详情项 |
| POST | /dict-detail/save | 新增字典详情 |
| POST | /dict-detail/update | 更新字典详情 |
| POST | /dict-detail/delete/{id} | 删除字典详情 |

---

## 人员管理模块 (person)

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /person/list | 获取人员列表（分页） |
| GET | /person/info/{id} | 获取人员详情 |
| POST | /person/save | 新增人员 |
| POST | /person/update | 更新人员信息 |
| POST | /person/delete/{id} | 删除人员 |

---

## 组织机构模块 (organization)

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /organization/list | 获取机构列表（分页） |
| GET | /organization/info/{id} | 获取机构详情 |
| POST | /organization/save | 新增机构 |
| POST | /organization/update | 更新机构信息 |
| POST | /organization/delete/{id} | 删除机构 |

---

## 大坝安全监测模块 (dam)

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /data-new/points | 获取监测点列表 |
| GET | /data-new/page | 获取渗流数据分页 |
| GET | /data-new/time-water-elevation | 获取水位高程时序数据 |
| GET | /data-new/time-water-level | 获取��位时序数据 |
| GET | /data-new/time-temperature | 获取温度时序数据 |
| GET | /data-new/time-water-pressure | 获取水压时序数据 |
| GET | /data-new/latest-water-elevation | 获取最新水位高程 |
| GET | /st-rivers-r/page | 获取水库水位分页数据 |
| GET | /st-rivers-r/page | 获取渗流量分页数据 |
| GET | /external-data/displacement-history | 获取地表位移历史数据 |

---

## 水雨情模块 (water)

### 降雨数据接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /st-pptn-hour/list | 获取小时雨量列表 |

### 水位数据接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /st-rivers-r/page | 分页查询水位数据 |
| GET | /st-rivers-r/list | 获取水位数据列表 |

### 逐日雨量接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /daily-rainfall | 获取逐日雨量列表 |
| POST | /daily-rainfall | 新增逐日雨量 |
| PUT | /daily-rainfall/{id} | 更新逐日雨量 |

### 河道站接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /river-station | 获取河道站数据列表 |

### 历年水情接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /annual-water-situation | 获取历年水情列表 |
| POST | /annual-water-situation | 新增历年水情 |
| PUT | /annual-water-situation/{id} | 更新历年水情 |
| DELETE | /annual-water-situation/{id} | 删除历年水情 |

### 测站极值接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /st-pextremum-b | 获取测站极值列表 |
| POST | /st-pextremum-b | 新增测站极值 |
| PUT | /st-pextremum-b/{stcd} | 更新测站极值 |

---

## 预警管理模块 (warning)

### 预警信息接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /warning-information/list | 获取预警信息列表 |
| GET | /warning-information/info/{id} | 获取预警信息详情 |
| POST | /warning-information/update | 更新预警信息（解除预警） |
| POST | /warning-information/delete/{id} | 删除预警信息 |
| GET | /warning-information/position | 根据地点搜索预警信息 |

### 预警指标接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /warning-indicator-setting/search-list | 获取预警指标列表 |
| GET | /warning-indicator-setting/search-position | 根据测点名称搜索指标 |
| GET | /warning-indicator-setting/types | 获取监测项类型列表 |
| GET | /warning-indicator-setting/info/{id} | 获取指标详情 |
| POST | /warning-indicator-setting/save | 新增预警指标 |
| POST | /warning-indicator-setting/update | 更新预警指标 |
| POST | /warning-indicator-setting/delete/{id} | 删除预警指标 |

---

## 综合报表模块 (report)

### 值班安排接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /duty-schedule/page | 获取值班安排分页列表 |
| POST | /duty-schedule | 新增值班安排 |
| PUT | /duty-schedule/{id} | 更新值班安排 |
| DELETE | /duty-schedule/{id} | 删除值班安排 |
| DELETE | /duty-schedule/batch | 批量删除值班安排 |

### 值班日志接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /duty-log/page | 获取值班日志分页列表 |
| POST | /duty-log | 新增值班日志 |
| PUT | /duty-log/{id} | 更新值班日志 |
| DELETE | /duty-log/{id} | 删除值班日志 |
| DELETE | /duty-log/batch | 批量删除值班日志 |

---

## 巡检记录模块 (inspection)

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /inspection-records/list | 获取巡检记录列表 |
| GET | /inspection-records/info/{id} | 获取巡检记录详情 |
| POST | /inspection-records/save | 新增巡检记录 |
| POST | /inspection-records/update | 更新巡检记录 |
| POST | /inspection-records/delete/{id} | 删除巡检记录 |
| POST | /inspection-records/solveRecords | 处理巡检记录 |
| GET | /inspection-records/export-excel | 导出Excel |
| POST | /inspection-records/upload2 | 上传图片 |

---

## 维护记录模块 (maintenance)

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /maintence-records/list | 获取维护记录列表 |
| GET | /maintence-records/info/{id} | 获取维护记录详情 |
| POST | /maintence-records/save | 新增维护记录 |
| POST | /maintence-records/update | 更新维护记录 |
| POST | /maintence-records/delete/{id} | 删除维护记录 |
| GET | /maintence-records/export-excel | 导出Excel |

---

## 工程信息模块 (engineering)

### 监测站点接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /measuring-station/list | 获取监测站点列表 |
| GET | /measuring-station/info/{id} | 获取监测站点详情 |
| POST | /measuring-station/save | 新增监测站点 |
| POST | /measuring-station/update | 更新监测站点 |
| POST | /measuring-station/delete/{id} | 删除监测站点 |
| GET | /dict/kinds?name=监测站名称 | 获取所有监测站点名称 |

### 测项信息接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /measuring-item/list | 获取测项信息列表 |
| GET | /measuring-item/info/{id} | 获取测项信息详情 |
| POST | /measuring-item/save | 新增测项信息 |
| POST | /measuring-item/update | 更新测项信息 |
| POST | /measuring-item/delete/{id} | 删除测项信息 |
| GET | /measuring-item/export-excel | 导出测项信息Excel |

### 洪水防御预案接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /flood-plan/list | 获取洪水防御预案列表 |
| GET | /flood-plan/info/{id} | 获取洪水防御预案详情 |
| POST | /flood-plan/save | 新增洪水防御预案步骤 |
| POST | /flood-plan/update | 更新洪水防御预案步骤 |
| POST | /flood-plan/delete/{id} | 删除洪水防御预案步骤 |

### 预警设施接口

| 方法 | 路径 | 用途 |
|-----|------|------|
| GET | /warning-facilities/list | 获取预警设施列表 |
| GET | /warning-facilities/info/{id} | 获取预警设施详情 |
| POST | /warning-facilities/add | 新增预警设施 |
| PUT | /warning-facilities/update | 更新预警设施 |
| DELETE | /warning-facilities/delete/{id} | 删除预警设施 |

---

## 统计汇总

| 模块 | API数量 |
|-----|---------|
| 认证模块 | 3 |
| 用户管理模块 | 10 |
| 角色管理模块 | 7 |
| 菜单管理模块 | 0（静态数据） |
| 部门管理模块 | 5 |
| 字典管理模块 | 11 |
| 人员管理模块 | 5 |
| 组织机构模块 | 5 |
| 大坝安全监测模块 | 10 |
| 水雨情模块 | 15 |
| 预警管理模块 | 13 |
| 综合报表模块 | 10 |
| 巡检记录模块 | 8 |
| 维护记录模块 | 6 |
| 工程信息模块 | 21 |
| **总计** | **129** |

---

## 备注

1. 菜单管理模块目前使用静态数据（`staticMenuData`），未调用实际后端API
2. 部分接口存在复用情况，如 `/dict/kinds` 被多个模块使用
3. HTTP方法使用说明：
   - `GET` - 查询操作
   - `POST` - 新增或特殊操作
   - `PUT` - 更新操作
   - `DELETE` - 删除操作
4. 路径参数使用 `{参数名}` 格式表示