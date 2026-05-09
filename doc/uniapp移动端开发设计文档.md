# uni-app 移动端开发设计文档

## 1. 文档说明

本文档用于指导智慧水利 uni-app 移动端开发。移动端功能只参考 `SK` 已存在的功能设计与相关配置，不参考 `SK` 的代码实现。

开发时必须遵循：

- `规范文档/uniapp开发规范.md`
- `规范文档/前端规范文档.md`
- 涉及后端接口时，以 `backendV2` 现有 Controller、DTO、VO 为准

本文档只描述移动端开发设计，不涉及后端改造，不设计数据库迁移，不扩展 `SK` 未实际落地的功能。

## 2. 设计依据

### 2.1 参考范围

`SK` 仅作为以下内容的参考来源：

- 移动端 tabBar 功能结构
- 已有页面功能范围
- 水库地图中心点、水库基础信息等配置
- 静态图片和图标资源的业务含义

`SK` 不作为以下内容的参考来源：

- 页面代码实现
- 组件实现方式
- service 接口路径
- 请求封装方式
- 状态管理实现
- 样式写法
- 旧接口适配逻辑

### 2.2 规范约束

移动端采用 uni-app 规范目录：

```text
project-root/
├── services/
├── components/
│   ├── common/
│   └── business/
├── composables/
├── config/
├── pages/
├── static/
├── stores/
│   ├── modules/
│   └── index.js
├── utils/
├── App.vue
├── main.js
├── manifest.json
├── pages.json
├── uni.scss
└── tailwind.config.js
```

核心技术约束：

- 使用 Vue 3、Composition API、`<script setup>`
- 使用 Pinia 管理状态
- 使用 `uni.request`，统一封装在 `utils/request.js`
- API 模块放在 `services/` 目录
- uni-app 模板使用 `view`、`text`、`image` 等组件，不使用 `div`、`span`
- 样式优先使用 Tailwind 原子类
- 禁止使用 Element Plus 等 PC 组件库
- 目标平台以 APP-PLUS Android 为主，H5 仅用于开发调试

## 3. 功能范围

### 3.1 本期功能

只开发 `SK` 已有且有实际页面支撑的功能：

| 模块       | 页面                                       | 功能说明                                     |
| ---------- | ------------------------------------------ | -------------------------------------------- |
| 登录       | `pages/login/login`                        | 账号密码登录、Token 持久化、登录后跳转       |
| 首页       | `pages/tabbar/index/index`                 | 水库地图、水库图片、水库基础信息             |
| 功能中心   | `pages/tabbar/function/function`           | 监测模块、巡检模块入口                       |
| 我的       | `pages/tabbar/user/user`                   | 当前用户信息展示                             |
| 水位监测   | `pages/modules/water-level/water-level`    | 当前水位、更新时间、趋势图、历史记录         |
| 雨量监测   | `pages/modules/rainfall/rainfall`          | 当前雨量、累计雨量、趋势图、历史记录         |
| 渗压监测   | `pages/modules/seepage/seepage`            | 测点选择、水压、温度、模数、趋势图、历史记录 |
| 渗流量监测 | `pages/modules/seepage/seepage-flow`       | 测站选择、渗流量趋势、历史记录               |
| 巡检记录   | `pages/modules/inspection/inspection`      | 巡检列表、筛选、刷新、加载更多、编辑、删除   |
| 巡检表单   | `pages/modules/inspection/inspection-form` | 新增巡检、编辑巡检、定位、图片上传、表单校验 |

### 3.2 不开发功能

以下功能不纳入本设计：

- 变形监测
- 独立巡检上报页面
- 预警管理
- 设备监控
- 闸门监控
- 视频监控
- 值班管理
- 系统管理
- 资源下载
- PC 端复杂报表和导出

说明：`SK` 功能页中出现但无实际页面支撑的入口，应在新移动端中删除或保留为“功能开发中”，不得扩展实现。

## 4. 总体架构设计

### 4.1 页面结构

移动端采用 3 个 tabBar：

| tab  | 页面路径                         | 说明               |
| ---- | -------------------------------- | ------------------ |
| 首页 | `pages/tabbar/index/index`       | 地图和水库信息总览 |
| 功能 | `pages/tabbar/function/function` | 业务功能入口       |
| 我的 | `pages/tabbar/user/user`         | 当前用户信息       |

业务页面通过 `uni.navigateTo` 进入：

- `pages/modules/water-level/water-level`
- `pages/modules/rainfall/rainfall`
- `pages/modules/seepage/seepage`
- `pages/modules/seepage/seepage-flow`
- `pages/modules/inspection/inspection`
- `pages/modules/inspection/inspection-form`

### 4.2 数据流

页面数据流：

```text
pages -> composables -> services -> utils/request.js -> backendV2
```

状态数据流：

```text
pages -> stores/modules/user.js -> uni storage
```

设计原则：

- 页面负责展示和交互
- composables 负责页面业务状态和动作编排
- services 只封装后端接口
- request 统一处理 baseUrl、token、错误和 401
- store 只管理跨页面共享状态

## 5. 接口设计

### 5.1 统一响应结构

后端统一返回 `ApiResult<T>`：

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {},
  "timestamp": "2026-05-09T12:00:00",
  "traceId": "trace-id"
}
```

移动端请求封装只在 `code === 200` 时 resolve，否则 reject 并提示 `message`。

### 5.2 分页响应结构

后端统一分页为 `PageResultVO<T>`：

```json
{
  "list": [],
  "total": 0,
  "page": 1,
  "size": 10,
  "totalPages": 0
}
```

移动端分页列表统一读取：

- 列表：`res.data.list`
- 总数：`res.data.total`
- 当前页：`res.data.page`
- 每页数量：`res.data.size`
- 总页数：`res.data.totalPages`

不得沿用旧代码中的 `records` 字段。

### 5.3 认证接口

后端来源：`AuthController`

| 功能     | 方法 | 接口                     | 参数                   | 返回            |
| -------- | ---- | ------------------------ | ---------------------- | --------------- |
| 登录     | POST | `/api/auth/login`        | `username`、`password` | `token`、`user` |
| 当前用户 | GET  | `/api/auth/current-user` | 无                     | `CurrentUserVO` |

登录请求：

```json
{
  "username": "admin",
  "password": "******"
}
```

登录响应核心字段：

```json
{
  "token": "jwt-token",
  "user": {
    "userId": 1,
    "username": "admin",
    "displayName": "管理员",
    "name": "张三",
    "department": "技术部",
    "position": "工程师",
    "type": "只读用户",
    "phoneNumber": "13800138000",
    "email": "admin@example.com",
    "roles": [],
    "authorities": []
  }
}
```

移动端处理：

- 登录成功后存储 token
- 存储当前用户信息
- 请求头携带 `Authorization: Bearer ${token}`
- 401 时清理 token 并跳转登录页

### 5.4 水位监测接口

后端来源：`WaterLevelController`

| 功能     | 方法 | 接口                     | 参数                                           |
| -------- | ---- | ------------------------ | ---------------------------------------------- |
| 水位分页 | GET  | `/api/water-levels/page` | `page`、`size`、`stcd`、`startDate`、`endDate` |
| 水位列表 | GET  | `/api/water-levels/list` | `stcd`、`startDate`、`endDate`                 |

返回字段：

| 字段   | 说明            |
| ------ | --------------- |
| `stcd` | 测站编码        |
| `tm`   | 时间            |
| `z1`   | 水位，单位 m    |
| `q1`   | 流量，单位 m3/s |

页面展示：

- 当前水位取最新一条有效数据
- 趋势图使用最近数据采样展示
- 历史记录按时间倒序展示

### 5.5 雨量监测接口

后端来源：`HourlyRainfallController`

| 功能     | 方法 | 接口                         | 参数                                   |
| -------- | ---- | ---------------------------- | -------------------------------------- |
| 雨量列表 | GET  | `/api/hourly-rainfalls/list` | `startDate`、`endDate`                 |
| 雨量分页 | GET  | `/api/hourly-rainfalls/page` | `page`、`size`、`startDate`、`endDate` |

返回字段：

| 字段   | 说明            |
| ------ | --------------- |
| `stcd` | 站码            |
| `tm`   | 时间            |
| `drp`  | 降水量，单位 mm |
| `intv` | 时段长          |
| `pdr`  | 日雨量          |
| `dyp`  | 天雨量          |
| `wth`  | 测站类型        |

页面展示：

- 当前雨量取最新一条 `drp`
- 累计雨量在前端按列表数据聚合
- 趋势图展示最近 24 小时数据
- 历史记录分页或列表展示

### 5.6 大坝监测接口

后端来源：`DamMonitoringController`

| 功能             | 方法 | 接口                                         | 参数                                                      |
| ---------------- | ---- | -------------------------------------------- | --------------------------------------------------------- |
| 测点列表         | GET  | `/api/dam-monitoring/points`                 | 无                                                        |
| 渗压分页         | GET  | `/api/dam-monitoring/seepage/page`           | `page`、`size`、`pointId`、`startTime`、`endTime`、`stcd` |
| 水压时序         | GET  | `/api/dam-monitoring/time-water-pressure`    | `pointId`、`startTime`、`endTime`                         |
| 温度时序         | GET  | `/api/dam-monitoring/time-temperature`       | `pointId`、`startTime`、`endTime`                         |
| 水位时序         | GET  | `/api/dam-monitoring/time-water-level`       | `pointId`、`startTime`、`endTime`                         |
| 水位高程时序     | GET  | `/api/dam-monitoring/time-water-elevation`   | `pointId`、`startTime`、`endTime`                         |
| 最新水位高程     | GET  | `/api/dam-monitoring/latest-water-elevation` | 无                                                        |
| 渗流量分页       | GET  | `/api/dam-monitoring/seepage-flow/page`      | `page`、`size`、`pointId`、`startTime`、`endTime`、`stcd` |
| 所有渗压最新数据 | GET  | `/api/dam-monitoring/seepage/latest-all`     | 无                                                        |

渗压分页返回字段：

| 字段           | 说明                                      |
| -------------- | ----------------------------------------- |
| `pointId`      | 测点编号                                  |
| `time`         | 采集时间                                  |
| `originalData` | 原始数据 JSON，包含模数、温度等           |
| `resultData`   | 结果数据 JSON，包含水位高程、水位、水压等 |
| `pointName`    | 测点名称                                  |

渗流量返回字段：

| 字段      | 说明     |
| --------- | -------- |
| `id`      | 记录 ID  |
| `stcd`    | 测站编码 |
| `tm`      | 监测时间 |
| `q1`      | 渗流量   |
| `remarks` | 备注     |

移动端处理：

- 渗压页面通过测点列表选择 `pointId`
- 水压、温度、模数等展示字段由 `originalData` 和 `resultData` 解析得到
- 解析 JSON 的逻辑放在 composable 或工具函数中，不写在 template 中
- 趋势图优先使用时序接口
- 渗流量页面通过 `stcd` 查询指定测站数据

### 5.7 巡检记录接口

后端来源：`InspectionRecordsController`

| 功能     | 方法 | 接口                             | 参数                                                                             |
| -------- | ---- | -------------------------------- | -------------------------------------------------------------------------------- |
| 巡检分页 | GET  | `/api/inspection-records/page`   | `page`、`size`、`project`、`abnormal`、`person`、`solve`、`startTime`、`endTime` |
| 巡检详情 | GET  | `/api/inspection-records/{id}`   | `id`                                                                             |
| 新增巡检 | POST | `/api/inspection-records/create` | `InspectionRecordsCreateDTO`                                                     |
| 更新巡检 | POST | `/api/inspection-records/update` | `InspectionRecordsUpdateDTO`                                                     |
| 删除巡检 | POST | `/api/inspection-records/delete` | `{ id }`                                                                         |
| 处理巡检 | POST | `/api/inspection-records/solve`  | `{ id }`                                                                         |
| 上传图片 | POST | `/api/inspection-records/upload` | multipart 字段 `image`                                                           |

巡检创建字段：

| 字段        | 必填 | 说明     |
| ----------- | ---- | -------- |
| `project`   | 是   | 巡检站点 |
| `longitude` | 是   | 经度     |
| `latitude`  | 是   | 纬度     |
| `type`      | 是   | 巡检类型 |
| `abnormal`  | 是   | 异常情况 |
| `situation` | 否   | 巡检情况 |
| `image`     | 否   | 图片路径 |
| `person`    | 是   | 负责人   |
| `date`      | 是   | 巡检日期 |

巡检返回字段：

| 字段         | 说明        |
| ------------ | ----------- |
| `id`         | 巡检记录 ID |
| `project`    | 巡检站点    |
| `longitude`  | 经度        |
| `latitude`   | 纬度        |
| `type`       | 巡检类型    |
| `abnormal`   | 异常情况    |
| `situation`  | 巡检情况    |
| `solve`      | 处理状态    |
| `image`      | 图片路径    |
| `person`     | 负责人      |
| `date`       | 巡检日期    |
| `createTime` | 创建时间    |
| `updateTime` | 更新时间    |

移动端处理：

- 新增和编辑使用同一个表单页
- 新增时不传 `id`
- 编辑时必须传 `id`
- 删除传 `{ id }`
- 图片上传使用 `uni.uploadFile`
- 上传字段名必须为 `image`
- 图片路径由后端返回后拼接到巡检表单 `image` 字段

## 6. 页面设计

### 6.1 登录页

职责：

- 输入用户名和密码
- 表单校验
- 调用 `/api/auth/login`
- 保存 token 和当前用户
- 登录成功后跳转首页

状态：

- `form.username`
- `form.password`
- `loading`
- `errors`

### 6.2 首页

职责：

- 展示水库地图
- 展示水库图片
- 展示水库基础信息

配置：

- 水库名称：武穴市荆竹水库
- 中心坐标：参考 `SK/config/map.js`
- 图片资源：参考 `SK/static/img`

说明：

- H5 可使用天地图配置
- APP-PLUS 优先使用 uni-app 原生 `map`
- 不在首页扩展额外 PC 端统计模块

### 6.3 功能中心

功能分组：

```text
监测模块
- 水位监测
- 雨量监测
- 渗压监测
- 渗流量监测

巡检模块
- 巡检记录
```

不展示无实际页面的入口，避免出现无法开发或不在范围内的功能。

### 6.4 我的

职责：

- 展示当前登录用户信息
- 展示姓名、账号、部门、岗位、角色、手机号、邮箱等
- 提供退出登录

接口：

- `/api/auth/current-user`

说明：

- 后端当前没有移动端个人资料更新接口，本期不做资料编辑提交。
- 如保留编辑入口，只做 UI 占位会造成误解，建议不展示编辑按钮。

### 6.5 水位监测

页面结构：

- 实时监测卡片
- 趋势图卡片
- 历史记录卡片

接口：

- `/api/water-levels/page`
- `/api/water-levels/list`

交互：

- 进入页面加载数据
- 点击刷新重新加载
- 历史记录支持分页加载

### 6.6 雨量监测

页面结构：

- 实时监测卡片
- 24 小时趋势图
- 历史记录卡片

接口：

- `/api/hourly-rainfalls/list`
- `/api/hourly-rainfalls/page`

交互：

- 下拉刷新
- 手动刷新
- 历史记录加载更多

### 6.7 渗压监测

页面结构：

- 测点选择
- 实时数据卡片
- 指标切换：水压、温度、模数
- 趋势图
- 历史记录

接口：

- `/api/dam-monitoring/points`
- `/api/dam-monitoring/seepage/page`
- `/api/dam-monitoring/time-water-pressure`
- `/api/dam-monitoring/time-temperature`

处理规则：

- 测点列表使用后端 `pointId` 作为查询值
- `originalData` 和 `resultData` 做 JSON 解析
- 数值不存在时展示 `--`

### 6.8 渗流量监测

页面结构：

- 测站选择
- 当前渗流量
- 趋势图
- 历史记录

接口：

- `/api/dam-monitoring/seepage-flow/page`

处理规则：

- 根据 `stcd` 查询指定测站
- `q1` 作为渗流量核心值
- 时间字段使用 `tm`

### 6.9 巡检记录

页面结构：

- 筛选区
- 巡检记录列表
- 新增按钮

筛选条件：

- 巡检站点 `project`
- 异常情况 `abnormal`
- 负责人 `person`
- 处理状态 `solve`
- 开始时间 `startTime`
- 结束时间 `endTime`

列表操作：

- 查看详情
- 编辑
- 删除
- 标记处理
- 图片预览

### 6.10 巡检表单

页面结构：

- 基本信息
- 位置信息
- 巡检情况
- 现场图片
- 底部操作按钮

表单字段：

- `project`
- `type`
- `person`
- `date`
- `longitude`
- `latitude`
- `abnormal`
- `solve`
- `situation`
- `image`

交互：

- 获取当前位置：`uni.getLocation`
- 上传图片：`uni.uploadFile`
- 保存：新增调用 `/create`，编辑调用 `/update`
- 取消：返回上一页

## 7. 组件设计

### 7.1 公共组件

| 组件            | 职责                                     |
| --------------- | ---------------------------------------- |
| `SkButton`      | 按钮，支持 loading、disabled、type、size |
| `SkInput`       | 输入框，支持文本、数字、日期             |
| `SkSelect`      | 选择器，基于 picker 实现                 |
| `SkCard`        | 内容容器                                 |
| `SkImageUpload` | 图片选择、上传、预览、删除               |
| `SkEmpty`       | 空状态                                   |
| `SkLoading`     | 加载状态                                 |

### 7.2 业务组件

| 组件                  | 职责         |
| --------------------- | ------------ |
| `MapView`             | 地图展示     |
| `ReservoirInfoCard`   | 水库基础信息 |
| `MonitorStatCard`     | 监测指标卡片 |
| `TrendChart`          | 趋势图封装   |
| `InspectionFilter`    | 巡检筛选     |
| `InspectionList`      | 巡检列表     |
| `InspectionImageGrid` | 巡检图片展示 |

## 8. services 设计

### 8.1 `services/auth.js`

```text
login(credentials)
getCurrentUser()
```

### 8.2 `services/water.js`

```text
getWaterLevelPage(params)
getWaterLevelList(params)
getHourlyRainfallPage(params)
getHourlyRainfallList(params)
```

### 8.3 `services/dam.js`

```text
getDamPoints()
getSeepagePage(params)
getTimeWaterPressure(params)
getTimeTemperature(params)
getTimeWaterLevel(params)
getTimeWaterElevation(params)
getLatestWaterElevation()
getSeepageFlowPage(params)
getSeepageLatestAll()
```

### 8.4 `services/inspection.js`

```text
getInspectionPage(params)
getInspectionDetail(id)
createInspection(data)
updateInspection(data)
deleteInspection(id)
solveInspection(id)
uploadInspectionImage(filePath)
```

## 9. stores 设计

### 9.1 `stores/modules/user.js`

状态：

- `token`
- `userInfo`
- `redirectPath`

计算属性：

- `isLoggedIn`
- `displayName`
- `roleNames`

动作：

- `login`
- `fetchCurrentUser`
- `setToken`
- `logout`
- `setRedirectPath`
- `clearRedirectPath`

持久化：

- token 写入 `uni.setStorageSync`
- userInfo 可持久化，进入 APP 时再调用当前用户接口刷新

## 10. 工具设计

### 10.1 `utils/request.js`

职责：

- 拼接 `baseUrl`
- 注入 token
- 统一处理响应
- 统一处理 401
- 统一处理网络错误

请求头：

```text
Content-Type: application/json
Authorization: Bearer ${token}
```

### 10.2 `utils/time.js`

职责：

- 格式化后端返回的日期时间字符串
- 格式化图表 X 轴时间
- 生成查询开始/结束时间

### 10.3 `utils/number.js`

职责：

- 数值空值处理
- 小数位格式化
- 单位展示辅助

### 10.4 `utils/storage.js`

职责：

- 封装 `uni.getStorageSync`
- 封装 `uni.setStorageSync`
- 封装对象序列化和反序列化

## 11. 配置设计

### 11.1 `config/index.js`

配置内容：

- `baseUrl`
- `timeout`
- `pageSize`
- `maxImageSize`

### 11.2 `config/map.js`

配置内容：

- 天地图 token
- 水库中心点
- 水库 marker
- 水库基础信息
- 首页图片列表

说明：配置值可参考 `SK/config/map.js`，但文件结构和命名按新规范重新整理。

## 12. 开发阶段拆分

### 12.1 第一阶段：项目基础与登录

目标：完成应用基础设施，保证能登录并访问受保护页面。

开发内容：

- 创建 uni-app 项目目录
- 配置 `pages.json`
- 配置 tabBar
- 配置 Tailwind
- 实现 `utils/request.js`
- 实现 Pinia 入口
- 实现 `stores/modules/user.js`
- 实现 `services/auth.js`
- 实现登录页
- 实现权限拦截

完成标准：

- 登录页可输入账号密码
- 登录成功后保存 token
- 可获取当前用户
- 未登录访问业务页时跳转登录

### 12.2 第二阶段：首页、功能中心、我的

目标：完成移动端主框架。

开发内容：

- 首页地图
- 水库图片展示
- 水库基础信息展示
- 功能中心入口
- 我的页面用户信息展示
- 退出登录

完成标准：

- tabBar 三个入口可正常切换
- 首页只展示水库相关内容
- 功能中心只展示本期范围内功能
- 我的页面展示当前用户信息

### 12.3 第三阶段：水雨情监测

目标：完成水位和雨量监测。

开发内容：

- `services/water.js`
- 水位监测页面
- 雨量监测页面
- 趋势图组件封装
- 历史记录列表
- 刷新和分页加载

完成标准：

- 水位页面能展示最新水位、趋势图、历史记录
- 雨量页面能展示最新雨量、累计雨量、趋势图、历史记录
- 数据字段对齐后端 `WaterLevelVO` 和 `HourlyRainfallVO`

### 12.4 第四阶段：大坝监测

目标：完成渗压和渗流量监测。

开发内容：

- `services/dam.js`
- 渗压监测页面
- 渗流量监测页面
- 测点选择
- 测站选择
- JSON 数据解析工具
- 指标趋势切换

完成标准：

- 渗压页面能选择测点并展示水压、温度、模数
- 渗流量页面能按测站展示渗流量趋势和历史记录
- 接口全部使用 `/api/dam-monitoring/*`

### 12.5 第五阶段：巡检记录

目标：完成移动端巡检闭环。

开发内容：

- `services/inspection.js`
- `composables/useInspection.js`
- 巡检列表
- 巡检筛选
- 巡检详情
- 巡检新增
- 巡检编辑
- 巡检删除
- 图片上传
- 定位获取

完成标准：

- 巡检记录可分页查询
- 可按筛选条件查询
- 可新增巡检
- 可编辑巡检
- 可删除巡检
- 可上传现场图片
- 经纬度字段满足后端 DTO 必填要求

### 12.6 第六阶段：联调修正与文档收尾

目标：统一体验并补齐开发说明。

开发内容：

- 统一加载态
- 统一空状态
- 统一错误提示
- 统一时间格式
- 统一分页加载逻辑
- 清理未开发入口
- 更新开发进度表
- 记录接口差异和处理方式

完成标准：

- 页面无明显字段错位
- 后端接口字段全部按现有 DTO/VO 对齐
- 文档进度更新完整

## 13. 开发进度说明

### 13.1 总进度

| 阶段     | 内容                 | 状态   | 说明                         |
| -------- | -------------------- | ------ | ---------------------------- |
| 第一阶段 | 项目基础与登录       | 已完成 | 搭建基础结构、请求封装、登录 |
| 第二阶段 | 首页、功能中心、我的 | 已完成 | 完成主框架和基础展示         |
| 第三阶段 | 水雨情监测           | 已完成 | 水位、雨量                   |
| 第四阶段 | 大坝监测             | 已完成 | 渗压、渗流量                 |
| 第五阶段 | 巡检记录             | 已完成 | 巡检列表、表单、上传、定位   |
| 第六阶段 | 联调修正与文档收尾   | 已完成 | 统一体验和补充说明           |

### 13.2 阶段记录

#### 第一阶段：项目基础与登录

- 完成时间：2026-05-09
- 完成内容：创建 uni-app 项目目录、配置 pages.json/tabBar/Tailwind、实现 request.js/Pinia/user store/auth service、登录页、权限拦截
- 涉及文件：manifest.json、pages.json、main.js、App.vue、uni.scss、tailwind.config.js、config/index.js、config/map.js、utils/request.js、utils/storage.js、stores/index.js、stores/modules/user.js、services/auth.js、pages/login/login.vue、pages/tabbar 三个占位页、static/icons 和 static/img
- 遗留问题：无
- 下一阶段计划：第二阶段首页、功能中心、我的

#### 第二阶段：首页、功能中心、我的

- 完成时间：2026-05-09
- 完成内容：
  - 首页：自定义导航栏 + MapView 地图组件（H5 天地图 + APP 原生 map）+ InfoCard 水库信息卡片（图片轮播 + 基础信息网格）
  - 功能中心：监测模块 4 入口（水位/雨量/渗压/渗流量）+ 巡检模块 1 入口（巡检记录），SkCard 分组网格布局
  - 我的：顶部用户概要 + 基础信息卡片 + 详细信息卡片（7 字段）+ 退出登录，onShow 调用 fetchCurrentUser 刷新
  - 公共组件：SkCard 通用卡片
  - 工具函数：format.js 日期格式化
  - 静态资源：monitor 和 inspection 目录下 5 个 SVG 图标
- 涉及文件：
  - 新建：components/common/SkCard.vue、components/business/MapView.vue、components/business/InfoCard.vue、composables/useMap.js、utils/format.js、static/icons/monitor/*.svg、static/icons/inspection/record.svg
  - 修改：pages/tabbar/index/index.vue、pages/tabbar/function/function.vue、pages/tabbar/user/user.vue
- 遗留问题：
  - 天地图 token 有效性需在 H5 端实测验证
  - APP 端原生 map 在 Android 上默认使用高德底图，若需天地图瓦片可能需额外配置
  - 首页自定义导航栏状态栏高度适配需在真机验证
- 下一阶段计划：第三阶段水雨情监测

#### 第三阶段：水雨情监测

- 完成时间：2026-05-09
- 完成内容：
  - services/water.js：水位分页/列表、雨量分页/列表 4 个接口，参数对齐后端 WaterLevelPageQueryDTO/HourlyRainfallPageQueryDTO
  - 水位监测页：实时水位/流量 MonitorStatCard + 刷新按钮 + 更新时间 → TrendChart 水位折线趋势图 → 历史记录分页加载
  - 雨量监测页：当前雨量/日雨量/累计雨量 MonitorStatCard + 刷新按钮 + 更新时间 → TrendChart 24小时柱状图 → 历史记录分页加载
  - TrendChart 组件：canvas 2d 绘制，支持折线图(line)和柱状图(bar)，自动 Y 轴刻度、X 轴时间标签、空数据提示
  - MonitorStatCard 组件：监测指标卡片，展示数值+单位+标签，空值显示 `--`，支持 normal/warning/danger 状态色
  - 刷新图标：static/icons/monitor/refresh.svg
  - pages.json：水位监测页增加 enablePullDownRefresh
- 涉及文件：
  - 新建：services/water.js、components/business/TrendChart.vue、components/business/MonitorStatCard.vue、pages/modules/water-level/water-level.vue、pages/modules/rainfall/rainfall.vue、static/icons/monitor/refresh.svg
  - 修改：pages.json（水位页增加下拉刷新）
- 遗留问题：
  - TrendChart 使用 uni-app 旧版 canvas API（createCanvasContext），APP-PLUS 端 canvas 2d 新 API 兼容性需真机验证
  - 趋势图在数据量大时（>100 条）绘制性能需真机验证
- 下一阶段计划：第四阶段大坝监测（渗压、渗流量）

#### 第四阶段：大坝监测

- 完成时间：2026-05-09
- 完成内容：
  - services/dam.js：9 个大坝监测接口（测点列表、渗压分页、水压/温度/水位/水位高程时序、最新水位高程、渗流量分页、渗压最新全量），路径全部对齐 `/api/dam-monitoring/*`，参数对齐 SeepageQueryDTO/DamTimeQueryDTO
  - 渗压监测页：picker 测点选择（从 /points 接口动态获取）+ 实时水压/温度/模数 MonitorStatCard（从 originalData/resultData JSON 解析）+ 指标切换趋势图（水压/温度 tab，调用 time-water-pressure/time-temperature 时序接口）+ 历史记录分页加载
  - 渗流量监测页：picker 测站选择（hardcode 两个测站 4211823043/4211822043）+ 当前渗流量 MonitorStatCard（q1×1000 转为 L/s）+ 趋势折线图 + 历史记录分页加载
  - pages.json：渗压/渗流量页面增加 enablePullDownRefresh
  - 复用已有组件：TrendChart、MonitorStatCard、SkCard
- 涉及文件：
  - 新建：services/dam.js、pages/modules/seepage/seepage.vue、pages/modules/seepage/seepage-flow.vue
  - 修改：pages.json（渗压/渗流量页增加下拉刷新）
- 遗留问题：
  - 渗压 originalData/resultData JSON 字段的 key 名称依赖后端数据实际内容，若后端数据格式变更需同步调整解析逻辑
  - 渗流量测站列表当前为 hardcode，若后续测站增加需手动维护
  - TrendChart canvas API 兼容性问题与第三阶段相同，需真机验证
- 下一阶段计划：第五阶段巡检记录（巡检列表、表单、上传、定位）

#### 第五阶段：巡检记录

- 完成时间：2026-05-09
- 完成内容：
  - services/inspection.js：7 个巡检接口（分页查询、详情、创建、更新、删除、处理、图片上传），路径全部对齐 `/api/inspection-records/*`，分页参数 page/size，分页返回 list/total，删除 POST `{ id }` 请求体，上传字段名 `image`
  - composables/useInspection.js：巡检业务逻辑（分页查询、加载更多、刷新、增删改、静态字典选项：站点6个/类型4个/异常2个/处理状态3个/负责人3个）
  - 公共组件：SkButton（多类型/尺寸/plain/loading）、SkInput（文本/数字/日期/密码/clearable）、SkSelect（下拉选项/遮罩关闭/选中高亮）、SkImageUpload（选择/上传/预览/删除）
  - 业务组件：InspectionFilter（站点/负责人/时间范围/异常/处理状态多条件筛选）、InspectionList（卡片布局/状态标签颜色/图片缩略图/编辑删除）
  - 巡检列表页：InspectionFilter + InspectionList 双 SkCard 布局 + 悬浮新增按钮，onShow 刷新 + 下拉刷新
  - 巡检表单页：基本信息（站点/类型/负责人/日期）+ 位置信息（经纬度/获取定位）+ 巡检情况（异常/处理状态/描述）+ 现场图片（SkImageUpload 最多6张）+ 固定底部操作栏
  - pages.json：巡检列表页增加 enablePullDownRefresh
  - 复用已有组件：SkCard
- 涉及文件：
  - 新建：components/common/SkButton.vue、components/common/SkInput.vue、components/common/SkSelect.vue、components/common/SkImageUpload.vue、services/inspection.js、composables/useInspection.js、components/business/inspection/InspectionFilter.vue、components/business/inspection/InspectionList.vue、pages/modules/inspection/inspection.vue、pages/modules/inspection/inspection-form.vue
  - 修改：pages.json（巡检列表页增加下拉刷新）
- 遗留问题：
  - 巡检站点和负责人列表当前为静态数据（hardcode），若后续后端提供字典接口需替换
  - 图片上传依赖后端 `/api/inspection-records/upload` 接口可用性
  - uni.getLocation 在 APP 端需用户授权定位权限，未授权时的提示引导需真机验证
- 下一阶段计划：第六阶段联调修正与文档收尾

#### 第六阶段：联调修正与文档收尾

- 完成时间：2026-05-09
- 完成内容：
  - 统一加载态：user.vue spinner 颜色/字号/间距与监测页面统一（`border-primary border-t-transparent`、`text-base`、`mt-5`），InspectionList.vue 间距 `mt-4` → `mt-5`
  - 统一空状态：6 处空状态颜色统一为 `text-gray-500`，4 个监测页面历史记录空状态添加 `flex flex-col items-center justify-center` 居中布局
  - 统一错误提示：useInspection.js 的 saveData/removeData 删除 catch 中的双重 toast，仅依赖 request.js 统一错误提示
  - 统一时间格式：InspectionList.vue 的 `item.date` 使用 `formatDate` 格式化，inspection-form.vue 的手写 `getCurrentDate` 改为 `formatDate(new Date(), 'YYYY-MM-DD')`
  - 统一分页加载逻辑：确认所有分页页面统一使用 `page/size` 参数 + `list/total` 响应字段，与 backendV2 `PageResultVO` 一致
  - 抽取公共函数：`formatNum` 从 3 个页面抽取到 `utils/format.js`，`getDefaultDateRange` 从 4 个页面抽取到 `utils/format.js`（支持 `startKey/endKey` 参数名配置）
  - 清理未开发入口：确认功能中心仅展示监测模块 4 入口 + 巡检模块 1 入口，无超范围功能
- 涉及文件：
  - 修改：utils/format.js（追加 formatNum + getDefaultDateRange）、pages/tabbar/user/user.vue、components/business/inspection/InspectionList.vue、components/business/TrendChart.vue、pages/modules/water-level/water-level.vue、pages/modules/rainfall/rainfall.vue、pages/modules/seepage/seepage.vue、pages/modules/seepage/seepage-flow.vue、pages/modules/inspection/inspection-form.vue、composables/useInspection.js
- 遗留问题：
  - 天地图 token 有效性需在 H5 端实测验证（第二阶段遗留）
  - TrendChart canvas API 兼容性需真机验证（第三阶段遗留）
  - 渗压 originalData/resultData JSON key 依赖后端数据格式（第四阶段遗留）
  - 渗流量测站列表 hardcode（第四阶段遗留）
  - 巡检站点/负责人列表 hardcode（第五阶段遗留）
- 下一阶段计划：无，全部阶段已完成

## 14. 风险与约束

### 14.1 接口字段差异

旧移动端可能使用 `records` 作为分页列表字段，但 `backendV2` 当前分页结构为 `list`。新移动端必须以 `list` 为准。

### 14.2 图片上传字段

巡检图片上传后端字段名为 `image`，移动端 `uni.uploadFile` 必须使用：

```text
name: "image"
```

### 14.3 用户资料编辑

后端当前认证模块提供登录和当前用户查询，不提供移动端个人资料更新接口。本期“我的”页面只做展示和退出登录。

### 14.4 未落地入口处理

`SK` 中没有实际页面支撑的入口不纳入开发范围。功能中心应避免展示不可进入页面。

### 14.5 不主动执行事项

开发过程中不主动执行：

- 编译
- 运行
- 测试
- 安装依赖
- 启动服务

仅在明确要求时执行。
