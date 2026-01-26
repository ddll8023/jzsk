# Spring Boot 3 升级与包结构重构开发文档 (Vertical Slice Edition)

## 0. 项目进度仪表盘 (Project Dashboard)

**当前状态**: 🚀 **Phase 3.5 已完成，准备进入 Phase 4**
**整体进度**: 95%
**最后更新**: 2025-02-12
**新项目路径**: `D:\demo\jzsk\backendV2`

|       阶段       | 任务描述                                                                   |   状态   | 预计完成时间 |
| :---------------: | :------------------------------------------------------------------------- | :-------: | :----------: |
| **Phase 0** | **项目初始化**                                                       | 🟢 已完成 |  2025-01-24  |
|        0.1        | 创建新项目目录结构 (backendV2)                                             |    ✅    |      -      |
|        0.2        | pom.xml 创建 (SB3.2.5, JDK17, 纯MyBatis)                                   |    ✅    |      -      |
|        0.3        | SzyApplication 启动类 & yml配置                                            |    ✅    |      -      |
| **Phase 1** | **基础设施层 (Infrastructure)**                                      | 🟢 已完成 |  2025-01-24  |
|        1.1        | 迁移 common/lang (Result, ResponseCode)                                    |    ✅    |      -      |
|        1.2        | 迁移 common/exception (全局异常处理)                                       |    ✅    |      -      |
|        1.3        | 搭建 MyBatis & PageHelper 基础配置                                         |    ✅    |      -      |
| **Phase 2** | **系统管理模块 (System Kernel)**                                     | 🟢 已完成 |  2025-01-24  |
|        2.1        | **用户/认证**: User/Role/Dept (Entity->Mapper->Service->Controller)  |    ✅    |  2025-01-24  |
|        2.2        | **字典/配置**: Dict/DictDetail (Entity->Mapper->Service->Controller) |    ✅    |  2025-01-24  |
|        2.3        | **安全接入**: Spring Security 基础集成 (基于DB用户)                  |    ✅    |  2025-01-24  |
| **Phase 3** | **业务功能模块 (Vertical Slices)**                                   | 🟡 进行中 |     TBD     |
|        3.1        | **基础信息**: 测站、测项 (MeasuringStation/MeasuringItem)            |    ✅    |  2025-01-24  |
|        3.2        | **水雨情**: 小时雨量(StPptnHour)、河道水情(StRiversR)                |    ✅    |  2025-01-24  |
|        3.3        | **实时数据**: 传感器监测数据 (DataNew/SensorPoint)                   |    ✅    |  2025-01-24  |
|        3.4        | **预警与报表**: 大坝安全、闸门工控                                   |    ✅    |  2025-01-24  |
|        3.5        | **运维与预警**: 预警逻辑、值班排班                                   |    ✅    |  2025-02-12  |
| **Phase 4** | **收尾与交付**                                                       | 🔴 未开始 |     TBD     |
|        4.1        | 全局定时任务 (Task) 迁移                                                   |    ✅    |  2025-02-12  |
|        4.2        | 完整性回归测试                                                             |    ⬜    |      -      |

---

## 1. 核心策略：垂直切片 (Vertical Slicing)

**变更说明**:
放弃原计划的"按层迁移" (Entities -> Services -> Controllers)，改为**"按业务模块迁移"**。
每个模块开发时，闭环完成该模块的 `Entity` -> `DTO/VO` -> `Mapper(XML)` -> `Service` -> `Controller`。

**优势**:

1. **即时验证**: 完成一个模块即可运行测试，无需等待所有代码迁移完毕。
2. **降低复杂度**: 聚焦单一业务域，减少上下文切换。
3. **符合规范**: 严格对照《后端模块设计说明.md》落地。

---

## 2. 详细执行路线

### Phase 1: 基础设施 (Infrastructure) ✅

*目标: 建立地基，确保工具类和全局配置可用。*

* [X] **1.1 通用响应与异常**: 迁移 `Result`, `GlobalExceptionHandler`, `BusinessException`。
* [X] **1.2 配置类**: 迁移 `MyBatisConfig` (Mapper扫描+事务管理)。
* [X] **1.3 持久层基座**: 确认 `MyBatis` 扫描路径，配置 `PageHelper`，创建 `mapper/` 目录。

### Phase 2: 系统核心 (System Kernel) ✅ 已完成

*目标: 跑通登录、权限与基础字典，作为业务模块的依赖。*

* **依赖顺序**: `Dict` -> `Dept` -> `Authority` -> `Role` -> `User` -> `Security`

* [X] **2.1 字典模块**: Dict/DictDetail 全套 (Entity/DTO/VO/Mapper/XML/Service/Controller)
* [X] **2.2 部门模块**: Department 全套
* [X] **2.3 权限模块**: Authority 全套（含树形结构构建）
* [X] **2.4 角色模块**: Role + RoleAuthority 关联表
* [X] **2.5 用户模块**: User + UserRole 关联表（含BCrypt密码加密）
* [X] **2.6 安全接入**: SecurityConfig(SB3 SecurityFilterChain), JwtFilter, 认证Handler

### Phase 3: 业务模块垂直切片 (Business Vertical Slices)

*核心策略：依赖倒置，先基础后应用，先写入后读取。*

#### 3.1 基础档案子阶段 (Foundation Slice)

*构建系统的"名词"层，确立所有数据的归属对象。*

* [X] **Step 1: 测站核心体系**: `MeasuringStation`, `MeasuringItem` (V2前端已有页面)

* ~~[ ] **Step 2: 工程对象档案**: `Reservoir`, `PumpStation`, `Waterworks`, `SurfaceWaterSources`~~ (V2前端无对应页面，暂不迁移)

#### 3.2 核心监测子阶段 (Core Monitoring Slice) ✅

*构建系统的"动词"层，处理高频时序数据。*

* [X] **Step 3: 水雨情数据**: `StPptnHour` (小时雨量), `StRiversR` (河道水情)

* ~~雨量监测站/逐日雨量/水位/蓄水池~~ (V2前端未调用，YAGNI原则暂不迁移)

#### 3.3 实时与可视化子阶段 (Real-time Slice) ✅

*强依赖前两个阶段的数据聚合。*

* [X] **Step 4: 传感器监测数据**: `DataNew`, `SensorPoint` (7个API接口)

* ~~`IconController`~~ (V2前端未调用，YAGNI原则暂不迁移)

#### 3.4 工程安全与工控子阶段 (Safety & Control Slice)

*专用设备对接与安全监测。*

* [X] **Step 5: 大坝安全监测**: `SeepageData`, `Displacement`。

* ~~**Step 5 (Video): 视频监控**~~: `VideoConfiguration`。
  > **分析结果**: 前端 V2 版本使用海康威视 Web SDK 直连设备，连接参数（IP/账号）硬编码在前端代码中，运行过程不调用后端接口。遵循 YAGNI 原则，本次重构**跳过**后端视频配置模块开发。
  >

* [X] **Step 6: 闸门监控**: `GateStatus`, `GateControl`, `GateInfo`, `GateOperation`。

#### 3.5 运维与预警子阶段 (Operations Slice)

*业务闭环。*

* [X] **Step 7: 预警与运维**: `WarningIndicatorSetting`, `WarningInformation`, `DutySchedule`, `InspectionRecords`。

### Phase 4: 全局收尾

* [X] 2025-02-12: **Phase 4.1 全局定时任务迁移完成**

  - 迁移 `WarningAutoCheckTask` 至 `infrastructure/task/`。
  - **重构**: 适配 SB3 + LocalDateTime + BigDecimal。
  - **优化**: GNSS 检查改为直接调用 Service，移除低效的 HTTP 回环调用。
  - **配置**: 启动类添加 `@EnableScheduling`。
  - **YAGNI**: 暂不迁移 `DataSyncTask` (依赖模块未就绪)。
* [ ] **最终清理**: 移除旧项目残留代码，统一代码格式。

---

## 3. 技术栈规范 (New)

* **ORM**: 纯 MyBatis (XML管理SQL)，禁止使用 MyBatis-Plus。
* **Entity**: 纯 POJO，无 `@TableName` 等注解。
* **Controller**: 统一返回 `Result<T>`。
* **Documentation**: 每个 Controller 方法必须带 `@Operation` (SpringDoc)。
* **时间类型**: 使用 `LocalDateTime` 替代 `Date`。
* **密码加密**: 使用 `BCryptPasswordEncoder`。

---

## 4. 已完成工作记录

* [X] 2025-01-24: 项目初始化 (脚手架/POM/YML)。
* [X] 2025-01-24: 策略调整为垂直切片模式。
* [X] 2025-01-24: **Phase 1 基础设施层完成**

  - 创建 `BusinessException.java` 业务异常类（支持多种构造方式）
  - 创建 `GlobalExceptionHandler.java` 全局异常处理器（5类异常分级处理）
  - 创建 `MyBatisConfig.java` 配置类（Mapper扫描 + 事务管理）
  - 创建 `src/main/resources/mapper/` 目录（XML映射文件存放位置）
* [X] 2025-01-24: **Phase 2 系统核心模块开发（Step 1-5）**

  ### Step 1: Dict字典模块 (14个文件)

  | 类型        | 文件                                                     |
  | :---------- | :------------------------------------------------------- |
  | Entity      | `Dict.java`, `DictDetail.java`                       |
  | DTO         | `DictDTO.java`, `DictDetailDTO.java`                 |
  | VO          | `DictVO.java`, `DictDetailVO.java`                   |
  | Mapper      | `DictMapper.java`, `DictDetailMapper.java`           |
  | XML         | `DictMapper.xml`, `DictDetailMapper.xml`             |
  | Service     | `DictService.java`, `DictDetailService.java`         |
  | ServiceImpl | `DictServiceImpl.java`, `DictDetailServiceImpl.java` |
  | Controller  | `DictController.java`, `DictDetailController.java`   |

  ### Step 2: Department部门模块 (8个文件)


  - Entity/DTO/VO/Mapper/XML/Service/ServiceImpl/Controller 全套

  ### Step 3: Authority权限模块 (8个文件)

  - 含树形结构构建逻辑 (`buildTree()`)
  - 处理DB列名映射：`subsystemID` → `parentId`, `orderNum` → `orderNum`
  - 支持按角色/用户查询权限

  ### Step 4: Role角色模块 (11个文件)

  - 含 `RoleAuthority` 关联表实体
  - 支持批量权限分配 (`batchInsert`)

  ### Step 5: User用户模块 (13个文件)

  - 含 `UserRole` 关联表实体
  - 含 `LoginDTO`/`LoginVO` 登录相关对象
  - 处理DB列名映射：`personID` → `personId`, `ID_number` → `idNumber`
  - XML `collection` 嵌套查询用户角色
  - BCrypt密码加密集成

  ### 优化落地记录

  | 优化项              | 实现方式                         |
  | :------------------ | :------------------------------- |
  | Date→LocalDateTime | 所有Entity使用 `LocalDateTime` |
  | MP注解移除          | 纯POJO，XML显式resultMap映射     |
  | 一对多关联          | XML `collection`嵌套查询       |
  | 多对多关联          | 关联表实体 + 批量插入            |
  | 树形结构            | Service层递归构建，非DB递归      |
  | 密码安全            | BCryptPasswordEncoder            |
* [X] 2025-01-24: **Phase 2 Step 6: Security安全接入 (9个文件)**
* [X] 2025-01-24: **Phase 3.1 基础档案模块完成 (16个文件)**

  ### MeasuringStation 监测站点模块 (8个文件)

  | 类型        | 文件                                              |
  | :---------- | :------------------------------------------------ |
  | Entity      | `pojo/entity/MeasuringStation.java`             |
  | DTO         | `pojo/dto/MeasuringStationDTO.java`             |
  | VO          | `pojo/vo/MeasuringStationVO.java`               |
  | Mapper      | `mapper/MeasuringStationMapper.java`            |
  | XML         | `resources/mapper/MeasuringStationMapper.xml`   |
  | Service     | `service/MeasuringStationService.java`          |
  | ServiceImpl | `service/impl/MeasuringStationServiceImpl.java` |
  | Controller  | `controller/MeasuringStationController.java`    |

  ### MeasuringItem 测项信息模块 (8个文件)

  | 类型        | 文件                                           |
  | :---------- | :--------------------------------------------- |
  | Entity      | `pojo/entity/MeasuringItem.java`             |
  | DTO         | `pojo/dto/MeasuringItemDTO.java`             |
  | VO          | `pojo/vo/MeasuringItemVO.java`               |
  | Mapper      | `mapper/MeasuringItemMapper.java`            |
  | XML         | `resources/mapper/MeasuringItemMapper.xml`   |
  | Service     | `service/MeasuringItemService.java`          |
  | ServiceImpl | `service/impl/MeasuringItemServiceImpl.java` |
  | Controller  | `controller/MeasuringItemController.java`    |

  ### API 接口清单

  | 模块     | 接口     | 方法 | 路径                               |
  | :------- | :------- | :--- | :--------------------------------- |
  | 监测站点 | 分页列表 | GET  | `/measuring-station/list`        |
  | 监测站点 | 详情     | GET  | `/measuring-station/info/{id}`   |
  | 监测站点 | 新增     | POST | `/measuring-station/save`        |
  | 监测站点 | 更新     | POST | `/measuring-station/update`      |
  | 监测站点 | 删除     | POST | `/measuring-station/delete/{id}` |
  | 测项信息 | 分页列表 | GET  | `/measuring-item/list`           |
  | 测项信息 | 详情     | GET  | `/measuring-item/info/{id}`      |
  | 测项信息 | 新增     | POST | `/measuring-item/save`           |
  | 测项信息 | 更新     | POST | `/measuring-item/update`         |
  | 测项信息 | 删除     | POST | `/measuring-item/delete/{id}`    |

  ### 技术规范落地

  | 规范项                | 实现情况                            |
  | :-------------------- | :---------------------------------- |
  | 纯 MyBatis（无 MP）   | ✅ XML 显式 resultMap               |
  | Date → LocalDateTime | ✅ 全部使用 LocalDateTime/LocalDate |
  | JSR-303 校验          | ✅ DTO 含 @NotBlank/@DecimalMin 等  |
  | 事务管理              | ✅ @Transactional(rollbackFor)      |
  | 日志记录              | ✅ SLF4J Logger                     |
  | 统一响应              | ✅ Result\<T\>                      |
  | OpenAPI 文档          | ✅ @Tag/@Operation/@Schema          |

  ### 备注


  - `Reservoir` 页面为静态硬编码，无需后端接口
  - `PumpStation`/`Waterworks`/`SurfaceWaterSources` V2前端暂无页面，暂不迁移

  ### 创建文件清单

  | 类型      | 文件路径                                      | 说明                              |
  | :-------- | :-------------------------------------------- | :-------------------------------- |
  | JWT工具   | `infrastructure/util/JwtUtils.java`         | 升级至 jjwt 0.12.x API            |
  | 用户实体  | `security/AccountUser.java`                 | 自定义 UserDetails，含 userId     |
  | 入口点    | `security/JwtAuthenticationEntryPoint.java` | 401 未认证响应                    |
  | 拒绝处理  | `security/JwtAccessDeniedHandler.java`      | 403 无权限响应                    |
  | 用户加载  | `security/UserDetailsServiceImpl.java`      | 对接新 UserMapper                 |
  | 成功处理  | `security/LoginSuccessHandler.java`         | 登录成功生成 JWT                  |
  | 失败处理  | `security/LoginFailureHandler.java`         | 登录失败响应                      |
  | JWT过滤器 | `security/JwtAuthenticationFilter.java`     | 基于 OncePerRequestFilter         |
  | 安全配置  | `config/SecurityConfig.java`                | SB3 SecurityFilterChain Bean 模式 |

  ### SB3 技术变更

  | 变更项       | 旧实现 (SB2)                     | 新实现 (SB3)                   |
  | :----------- | :------------------------------- | :----------------------------- |
  | Security配置 | `WebSecurityConfigurerAdapter` | `SecurityFilterChain` Bean   |
  | JWT过滤器    | `BasicAuthenticationFilter`    | `OncePerRequestFilter`       |
  | jjwt API     | 0.9.x `signWith(algo, secret)` | 0.12.x `signWith(key, algo)` |
  | Servlet API  | `javax.servlet.*`              | `jakarta.servlet.*`          |
  | 方法级权限   | `@EnableGlobalMethodSecurity`  | `@EnableMethodSecurity`      |
  | URL匹配      | `antMatchers()`                | `requestMatchers()`          |

  ### 白名单优化

  从旧项目 50+ URL 精简为核心路径：

  - 登录/登出：`/login`, `/logout`
  - API文档：`/doc.html`, `/swagger-ui/**`, `/v3/api-docs/**`
  - 静态资源：`/photo/**`, `/pic/**`, `/shipin/**`, `/icon/**`
  - 公开接口：`/data-new/**`, `/external-data/**`, `/app/**`
* [X] 2025-01-24: **Phase 3.3 实时与可视化模块完成 (14个文件)**

  ### DataNew 传感器监测数据模块 (10个文件)

  | 类型        | 文件                                                                                                                                   |
  | :---------- | :------------------------------------------------------------------------------------------------------------------------------------- |
  | Entity      | `pojo/entity/DataNew.java`, `pojo/entity/SensorPoint.java`                                                                         |
  | VO          | `pojo/vo/DataNewVO.java`, `pojo/vo/SensorPointVO.java`, `pojo/vo/TimeSeriesDataVO.java`, `pojo/vo/LatestWaterElevationVO.java` |
  | Mapper      | `mapper/DataNewMapper.java`, `mapper/SensorPointMapper.java`                                                                       |
  | XML         | `resources/mapper/DataNewMapper.xml`, `resources/mapper/SensorPointMapper.xml`                                                     |
  | Service     | `service/DataNewService.java`, `service/SensorPointService.java`                                                                   |
  | ServiceImpl | `service/impl/DataNewServiceImpl.java`, `service/impl/SensorPointServiceImpl.java`                                                 |
  | Controller  | `controller/DataNewController.java`, `controller/SensorPointController.java`                                                       |

  ### API 接口清单 (7个接口)

  | 接口         | 方法 | 路径                                 |
  | :----------- | :--- | :----------------------------------- |
  | 测点列表     | GET  | `/data-new/points`                 |
  | 分页查询     | GET  | `/data-new/page`                   |
  | 水位高程时序 | GET  | `/data-new/time-water-elevation`   |
  | 水位时序     | GET  | `/data-new/time-water-level`       |
  | 温度时序     | GET  | `/data-new/time-temperature`       |
  | 水压时序     | GET  | `/data-new/time-water-pressure`    |
  | 最新水位高程 | GET  | `/data-new/latest-water-elevation` |

  ### 技术重构

  | 重构项          | 说明                                     |
  | :-------------- | :--------------------------------------- |
  | Controller 瘦身 | 业务逻辑迁移至 Service 层                |
  | JSON 解析封装   | `extractValueFromJson()` 方法          |
  | Redis 缓存      | 时序数据缓存10分钟，可选降级             |
  | ID-名称转换     | `SensorPointService.getNameMapByIds()` |

  ### YAGNI 原则落地


  - ~~`/data-new/list`~~ (V2前端未调用)
  - ~~`/data-new/time-modulus`~~ (V2前端未调用)
  - ~~`IconController`~~ (V2前端未调用)
* [X] 2025-01-24: **Phase 3.4 工程安全与工控模块完成 (7个文件)**

  ### ExternalData 位移监测模块 (GNSS对接)

  | 类型        | 文件                                                                                                           |
  | :---------- | :------------------------------------------------------------------------------------------------------------- |
  | Service     | `service/DisplacementHistoryService.java` (接口)                                                             |
  | ServiceImpl | `service/impl/DisplacementHistoryServiceImpl.java` (RestTemplate调用)                                        |
  | Controller  | `controller/ExternalDataController.java`                                                                     |
  | VO          | `pojo/vo/DisplacementHistoryVO.java`, `pojo/vo/DisplacementKeyValueVO.java`, `pojo/vo/PageResultVO.java` |
  | DTO         | `infrastructure/external/DisplacementHistoryResponse.java` (内部解析用)                                      |

  ### Zkxt 闸门工控模块 (SQL Server直连)

  | 类型        | 文件                                                 |
  | :---------- | :--------------------------------------------------- |
  | Service     | `service/ZkxtService.java`                         |
  | ServiceImpl | `service/impl/ZkxtServiceImpl.java` (JDBC动态查询) |
  | Controller  | `controller/ZkxtController.java`                   |

  ### 核心重构亮点


  1. **5合1控制器**: 将 `Dgq`, `Dzdf`, `Qst`, `Xgq`, `Yhd` 5个Controller合并为 `ZkxtController`，通过 `gateCode` 动态路由。
  2. **JDBC直查**: 针对SQL Server `float` 类型映射问题，放弃MyBatis，使用原生JDBC `ResultSetMetaData` 动态解析列名，确保数据精度。
  3. **多数据源**: 配置 `spring.datasource.dynamic` 支持MySQL(主库) + SQL Server(工控库)。
  4. **外部接口对接**: 规范化外部GNSS API调用，封装统一的 `PageResultVO`。

---

## 5. 待执行任务

### Phase 3: 业务模块开发 (按顺序执行)

#### 3.1 基础档案子阶段 (Foundation Slice) ✅ 已完成

- [X] **Step 1**: 测站核心 (`MeasuringStation`, `MeasuringItem`) - 2025-01-24

- ~~[ ] **Step 2**: 工程档案~~ (V2前端无对应页面，暂不迁移)

#### 3.2 核心监测子阶段 (Core Monitoring Slice) ✅ 已完成

- [X] **Step 3.1**: 小时雨量 (`StPptnHour`) - 2025-01-24
- [X] **Step 3.2**: 河道水情 (`StRiversR`) - 2025-01-24

- ~~[ ] 雨量监测站/逐日雨量/水位/蓄水池~~ (V2前端未调用，YAGNI原则暂不迁移)

#### 3.3 实时与可视化子阶段 (Real-time Slice) ✅ 已完成

- [X] **Step 4**: 传感器监测数据 (`DataNew`, `SensorPoint`) - 2025-01-24

- ~~[ ] `IconController`~~ (V2前端未调用，YAGNI原则暂不迁移)

#### 3.4 工程安全与工控子阶段 (Safety & Control Slice) ✅ 已完成

- [X] **Step 5**: 安全监测 (`Displacement`) - 2025-01-24
  - **YAGNI**: `SeepageData` 跳过

- ~~**Step 5 (Video)**: 视频监控 (`VideoConfiguration`)~~ (前端硬编码直连，后端跳过)

- [X] **Step 6**: 闸门监控 (`ZkxtController`) - 2025-01-24
  - 整合5大闸门为统一接口，支持 SQL Server 直连

#### 3.5 运维与预警子阶段 (Operations Slice) ✅ 已完成

*业务闭环。*

- [X] **Step 7**: 运维预警 (`WarningIndicatorSetting`, `WarningInformation`, `DutySchedule`, `InspectionRecords`)

  ### 模块完成清单 (16个文件)

  | 模块               | 说明                                                                                 |
  | :----------------- | :----------------------------------------------------------------------------------- |
  | **预警指标** | `WarningIndicatorSetting` 全套。阈值升级为 `BigDecimal`，支持测点/类型筛选。     |
  | **预警信息** | `WarningInformation` 全套。实现了预警发布、查询与解除（自动回填结束时间）。        |
  | **值班排班** | `DutySchedule` 全套。**技术亮点**：Mapper XML 映射解决了数据库中文列名问题。 |
  | **巡检记录** | `InspectionRecords` 全套。移除了Entity中冗余校验，支持图片上传与状态流转。         |

  ### API 接口清单

  | 模块     | 接口                                       | 方法 | 说明              |
  | :------- | :----------------------------------------- | :--- | :---------------- |
  | 预警指标 | `/warning-indicator-setting/search-list` | GET  | 配置查询          |
  | 预警信息 | `/warning-information/list`              | GET  | 列表查询          |
  | 预警信息 | `/warning-information/update`            | POST | 解除预警/更新状态 |
  | 值班排班 | `/duty-schedule/page`                    | GET  | 分页查询          |
  | 巡检记录 | `/inspection-records/solveRecords`       | POST | 异常处理闭环      |
  | 巡检记录 | `/inspection-records/upload2`            | POST | 图片上传          |

  ### 技术重构落地


  - **中文列名隔离**: 通过 MyBatis `<resultMap>` 屏蔽了数据库层面的中文命名（如 `值班人员`），Java层保持纯英文规范。
  - **精度保障**: 涉及阈值判定字段全面使用 `BigDecimal`。
  - **单一职责**: 剥离了 POJO 中的业务校验逻辑，统一收口至 Service/Controller。

---

## 6. 目录结构预览 (当前)

```
com.szy
├── common/                         # ✅ Phase 1
│   ├── lang/
│   │   ├── Result.java
│   │   ├── ResponseCode.java
│   │   ├── IResponseCode.java
│   │   └── Const.java
│   └── exception/
│       ├── BusinessException.java
│       └── GlobalExceptionHandler.java
├── config/
│   ├── MyBatisConfig.java          # ✅ Phase 1
│   └── SecurityConfig.java         # ✅ Phase 2 Step 6
├── infrastructure/                 # ✅ Phase 2 Step 6
│   └── util/
│       └── JwtUtils.java
├── security/                       # ✅ Phase 2 Step 6
│   ├── AccountUser.java
│   ├── UserDetailsServiceImpl.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtAuthenticationEntryPoint.java
│   ├── JwtAccessDeniedHandler.java
│   ├── LoginSuccessHandler.java
│   └── LoginFailureHandler.java
├── pojo/
│   ├── entity/
│   │   ├── Dict.java               # ✅ Phase 2
│   │   ├── DictDetail.java
│   │   ├── Department.java
│   │   ├── Authority.java
│   │   ├── Role.java
│   │   ├── RoleAuthority.java
│   │   ├── User.java
│   │   ├── UserRole.java
│   │   ├── MeasuringStation.java   # ✅ Phase 3.1
│   │   ├── MeasuringItem.java      # ✅ Phase 3.1
│   │   ├── StPptnHour.java         # ✅ Phase 3.2
│   │   ├── StRiversR.java          # ✅ Phase 3.2
│   │   ├── WarningIndicatorSetting.java # ✅ Phase 3.5
│   │   ├── WarningInformation.java      # ✅ Phase 3.5
│   │   ├── DutySchedule.java            # ✅ Phase 3.5
│   │   └── InspectionRecords.java       # ✅ Phase 3.5
│   ├── dto/
│   │   ├── DictDTO.java            # ✅ Phase 2
│   │   ├── DictDetailDTO.java
│   │   ├── DepartmentDTO.java
│   │   ├── AuthorityDTO.java
│   │   ├── RoleDTO.java
│   │   ├── UserDTO.java
│   │   ├── LoginDTO.java
│   │   ├── MeasuringStationDTO.java  # ✅ Phase 3.1
│   │   └── MeasuringItemDTO.java     # ✅ Phase 3.1
│   └── vo/
│       ├── DictVO.java             # ✅ Phase 2
│       ├── DictDetailVO.java
│       ├── DepartmentVO.java
│       ├── AuthorityVO.java
│       ├── RoleVO.java
│       ├── UserVO.java
│       ├── LoginVO.java
│       ├── MeasuringStationVO.java   # ✅ Phase 3.1
│       ├── MeasuringItemVO.java      # ✅ Phase 3.1
│       ├── StPptnHourVO.java         # ✅ Phase 3.2
│       └── StRiversRVO.java          # ✅ Phase 3.2
├── mapper/
│   ├── DictMapper.java             # ✅ Phase 2
│   ├── DictDetailMapper.java
│   ├── DepartmentMapper.java
│   ├── AuthorityMapper.java
│   ├── RoleMapper.java
│   ├── RoleAuthorityMapper.java
│   ├── UserMapper.java
│   ├── UserRoleMapper.java
│   ├── MeasuringStationMapper.java   # ✅ Phase 3.1
│   ├── MeasuringItemMapper.java      # ✅ Phase 3.1
│   ├── StPptnHourMapper.java         # ✅ Phase 3.2
│   ├── StRiversRMapper.java          # ✅ Phase 3.2
│   ├── WarningIndicatorSettingMapper.java # ✅ Phase 3.5
│   ├── WarningInformationMapper.java    # ✅ Phase 3.5
│   ├── DutyScheduleMapper.java          # ✅ Phase 3.5
│   └── InspectionRecordsMapper.java     # ✅ Phase 3.5
├── service/
│   ├── DictService.java            # ✅ Phase 2
│   ├── DictDetailService.java
│   ├── DepartmentService.java
│   ├── AuthorityService.java
│   ├── RoleService.java
│   ├── UserService.java
│   ├── MeasuringStationService.java  # ✅ Phase 3.1
│   ├── MeasuringItemService.java     # ✅ Phase 3.1
│   ├── StPptnHourService.java        # ✅ Phase 3.2
│   ├── StRiversRService.java         # ✅ Phase 3.2
│   ├── DisplacementHistoryService.java # ✅ Phase 3.4
│   ├── ZkxtService.java              # ✅ Phase 3.4
│   ├── WarningIndicatorSettingService.java # ✅ Phase 3.5
│   ├── WarningInformationService.java   # ✅ Phase 3.5
│   ├── DutyScheduleService.java         # ✅ Phase 3.5
│   ├── InspectionRecordsService.java    # ✅ Phase 3.5
│   └── impl/
│       ├── DictServiceImpl.java
│       ├── DictDetailServiceImpl.java
│       ├── DepartmentServiceImpl.java
│       ├── AuthorityServiceImpl.java
│       ├── RoleServiceImpl.java
│       ├── UserServiceImpl.java
│       ├── MeasuringStationServiceImpl.java  # ✅ Phase 3.1
│       ├── MeasuringItemServiceImpl.java     # ✅ Phase 3.1
│       ├── StPptnHourServiceImpl.java        # ✅ Phase 3.2
│       ├── StRiversRServiceImpl.java         # ✅ Phase 3.2
│       ├── DisplacementHistoryServiceImpl.java # ✅ Phase 3.4
│       └── ZkxtServiceImpl.java              # ✅ Phase 3.4
└── controller/
    ├── DictController.java         # ✅ Phase 2
    ├── DictDetailController.java
    ├── DepartmentController.java
    ├── AuthorityController.java
    ├── RoleController.java
    ├── UserController.java
    ├── MeasuringStationController.java  # ✅ Phase 3.1
    ├── MeasuringItemController.java     # ✅ Phase 3.1
    ├── StPptnHourController.java        # ✅ Phase 3.2
    ├── StRiversRController.java         # ✅ Phase 3.2
    ├── ExternalDataController.java      # ✅ Phase 3.4
    ├── ZkxtController.java              # ✅ Phase 3.4
    ├── WarningIndicatorSettingController.java # ✅ Phase 3.5
    ├── WarningInformationController.java    # ✅ Phase 3.5
    ├── DutyScheduleController.java          # ✅ Phase 3.5
    └── InspectionRecordsController.java     # ✅ Phase 3.5

resources/
└── mapper/
    ├── DictMapper.xml              # ✅ Phase 2
    ├── DictDetailMapper.xml
    ├── DepartmentMapper.xml
    ├── AuthorityMapper.xml
    ├── RoleMapper.xml
    ├── RoleAuthorityMapper.xml
    ├── UserMapper.xml
    ├── UserRoleMapper.xml
    ├── MeasuringStationMapper.xml    # ✅ Phase 3.1
    ├── MeasuringItemMapper.xml       # ✅ Phase 3.1
    ├── StPptnHourMapper.xml          # ✅ Phase 3.2
    ├── StRiversRMapper.xml           # ✅ Phase 3.2
    ├── WarningIndicatorSettingMapper.xml    # ✅ Phase 3.5
    ├── WarningInformationMapper.xml         # ✅ Phase 3.5
    ├── DutyScheduleMapper.xml               # ✅ Phase 3.5
    └── InspectionRecordsMapper.xml          # ✅ Phase 3.5
```
