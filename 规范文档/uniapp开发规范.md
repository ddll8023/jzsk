# uni-app 开发规范

## 1. 技术栈基础

### 1.1 核心框架

本规范基于以下技术栈构建：

- **uni-app**：跨平台应用开发框架
  - Source: [uni-app 官方文档](https://uniapp.dcloud.net.cn/)
- **Vue 3**：渐进式 JavaScript 框架（推荐使用 Composition API）
  - 版本：^3.4.0
  - Source: [Vue 3 官方文档](https://cn.vuejs.org/)
- **Pinia**：Vue 3 官方推荐的状态管理库
  - 版本：^2.1.0
  - Source: [Pinia 官方文档](https://pinia.vuejs.org/zh/)
- **pinia-plugin-persistedstate**：Pinia 持久化插件
  - 版本：^3.2.0
  - Source: [pinia-plugin-persistedstate](https://github.com/prazdevs/pinia-plugin-persistedstate)
- **uni.request**：uni-app 原生网络请求 API

### 1.2 样式方案

- **Tailwind CSS**：原子化 CSS 框架（推荐）
  - 版本：^3.4.0
  - Source: [Tailwind CSS 官方文档](https://tailwindcss.com/)
- **weapp-tailwindcss**：小程序 Tailwind CSS 适配插件
  - 版本：^3.4.0
  - Source: [weapp-tailwindcss](https://tw.icebreaker.top/)
- **PostCSS**：CSS 转换工具
  - 版本：^8.4.0
- **Autoprefixer**：CSS 自动添加浏览器前缀
  - 版本：^10.4.0
- **uni-app 内置样式**：rpx 响应式单位
- **CSS 变量**：定义主题色、间距等全局常量
- **条件编译**：针对不同平台定制样式

### 1.3 项目结构

```
project-root/
├── services/                 # API 接口模块（避免与代理 /api 冲突）
├── components/               # 组件目录
├── composables/              # 组合式函数（Vue 3 Composition API）
├── config/                   # 配置文件
├── pages/                    # 页面目录
├── static/                   # 静态资源
├── stores/                   # Pinia 状态管理
│   ├── modules/              # 状态模块
│   └── index.js              # Store 入口
├── utils/                    # 工具函数
├── App.vue                   # 应用入口
├── main.js                   # 主入口文件
├── manifest.json             # 应用配置
├── pages.json                # 页面路由配置
├── uni.scss                  # 全局样式变量
├── postcss.config.js         # PostCSS 配置
├── tailwind.config.js        # Tailwind CSS 配置
└── package.json              # 项目依赖配置
```

**目录说明**：

- `services/`：API 接口模块，按功能模块划分（注意：使用 services 而非 api，避免与 manifest.json 中 /api 代理规则冲突）
- `components/`：组件目录，分为公共组件和业务组件
- `composables/`：可复用的组合式函数（Vue 3 特有）
- `config/`：全局配置文件（API 地址、常量等）
- `pages/`：页面目录，按功能分组
- `static/`：静态资源（图片、图标等）
- `stores/`：Pinia 状态管理，模块化组织
- `utils/`：工具函数（请求封装、本地存储、验证等）

### 1.4 核心依赖版本

**生产依赖**：

```json
{
  "dependencies": {
    "vue": "^3.4.0",
    "pinia": "^2.1.0",
    "pinia-plugin-persistedstate": "^3.2.0"
  }
}
```

**开发依赖**：

```json
{
  "devDependencies": {
    "tailwindcss": "^3.4.0",
    "postcss": "^8.4.0",
    "autoprefixer": "^10.4.0",
    "weapp-tailwindcss": "^3.4.0",
    "prettier": "^3.2.0",
    "eslint": "^8.56.0",
    "eslint-plugin-vue": "^9.20.0"
  }
}
```

**版本说明**：

- Vue 3.4.0+：支持最新的 Composition API 特性
- Pinia 2.1.0+：Vue 3 官方推荐的状态管理方案
- Tailwind CSS 3.4.0+：最新的原子化 CSS 框架
- PostCSS 8.4.0+：CSS 转换工具
- Prettier 3.2.0+：代码格式化工具
- ESLint 8.56.0+：代码质量检查工具

### 1.5 目标平台

本规范主要针对以下平台：

- **APP-PLUS**：Android App（主要目标平台）
- **H5**：Web 浏览器（可选，用于开发调试）

**平台特性**：

- 使用条件编译适配不同平台
- 优先保证 Android App 的功能完整性
- H5 平台仅用于快速开发和调试

## 2. 命名规范

### 2.1 文件命名

| 类型     | 命名规则   | 示例                              |
| -------- | ---------- | --------------------------------- |
| 页面文件 | kebab-case | `user-profile.vue`              |
| 组件文件 | PascalCase | `UserCard.vue`                  |
| API 模块 | camelCase  | `seepage.js`, `inspection.js` |
| 工具函数 | camelCase  | `request.js`, `validate.js`   |
| 配置文件 | camelCase  | `index.js`, `config.js`       |
| 静态资源 | kebab-case | `logo-icon.png`                 |

### 2.2 变量命名

| 类型     | 命名规则         | 示例                             |
| -------- | ---------------- | -------------------------------- |
| 变量     | camelCase        | `userName`, `dataList`       |
| 常量     | UPPER_SNAKE_CASE | `API_BASE_URL`, `PAGE_SIZE`  |
| 函数     | camelCase        | `handleSubmit`, `fetchData`  |
| 组件名称 | PascalCase       | `UserCard`, `DataTable`      |
| Props    | camelCase        | `pageTitle`, `dataList`      |
| 事件     | kebab-case       | `@update-data`, `@on-change` |

### 2.3 CSS 类名

**推荐：使用 Tailwind 原子化类名（强烈推荐）**：

```vue
<template>
  <!-- Tailwind 原子化类名，无需维护复杂CSS -->
  <view class="p-5 bg-white rounded-lg shadow-sm">
    <view class="flex items-center justify-between mb-5">
      <text class="text-lg font-bold text-gray-900">{{ title }}</text>
      <view class="w-8 h-8 bg-primary rounded-full"></view>
    </view>
    <text class="text-base text-gray-600">{{ content }}</text>
  </view>
</template>

<!-- 无需额外 <style> 标签，样式全部通过 Tailwind 类名实现 -->
```

**不推荐：使用 BEM 命名规范（已废弃）**：

```vue
<template>
  <!-- BEM 命名规范 - 已废弃，不推荐 -->
  <view class="user-card">
    <view class="user-card__header">
      <text class="user-card__title">{{ title }}</text>
    </view>
    <view class="user-card__body">
      <text class="user-card__content">{{ content }}</text>
    </view>
  </view>
</template>

<style scoped>
/* BEM 命名：块__元素--修饰符 - 已废弃 */
.user-card { }
.user-card__header { }
.user-card__title { }
.user-card__body { }
.user-card__content { }
.user-card--active { }
</style>
```

**为什么推荐 Tailwind**：
- ✅ **样式一致性**：统一使用 Tailwind 标准类名，无需维护变量
- ✅ **开发效率**：无需在 style 和 template 之间切换
- ✅ **代码可读性**：类名即样式，直观易懂
- ✅ **响应式支持**：原生支持 `sm:` `md:` `lg:` 前缀
- ✅ **样式去重**：Tailwind 自动去重，无样式膨胀
- ❌ **BEM 维护成本高**：需手动维护 CSS 层级，类名冗长，容易出错

## 3. pages.json 路由配置规范

### 3.1 路由配置结构

```json
{
  "pages": [
    {
      "path": "pages/login/login",
      "style": {
        "navigationBarTitleText": "登录",
        "navigationStyle": "custom"
      }
    },
    {
      "path": "pages/tabbar/index/index",
      "style": {
        "navigationBarTitleText": "首页",
        "navigationBarBackgroundColor": "#00b783",
        "navigationBarTextStyle": "white",
        "enablePullDownRefresh": false
      }
    }
  ],
  "globalStyle": {
    "navigationBarTextStyle": "white",
    "navigationBarTitleText": "智慧水利",
    "navigationBarBackgroundColor": "#00b783",
    "backgroundColor": "#f8f8f8"
  },
  "tabBar": {
    "color": "#999",
    "selectedColor": "#00b783",
    "borderStyle": "white",
    "backgroundColor": "#ffffff",
    "list": [
      {
        "pagePath": "pages/tabbar/index/index",
        "text": "首页",
        "iconPath": "static/icons/home.png",
        "selectedIconPath": "static/icons/home-active.png"
      }
    ]
  }
}
```

### 3.2 路由配置原则

**KISS 原则**：

- 路由路径简洁明了
- 避免过深的嵌套层级
- 页面标题清晰表达功能

**页面顺序**：

1. 登录页放在首位
2. tabBar 页面紧随其后
3. 业务模块页面按功能分组

### 3.3 导航栏配置

```json
{
  "style": {
    "navigationBarTitleText": "页面标题",
    "navigationBarBackgroundColor": "#00b783",
    "navigationBarTextStyle": "white",
    "enablePullDownRefresh": false,
    "backgroundTextStyle": "dark"
  }
}
```

### 3.4 tabBar 配置规范

```json
{
  "tabBar": {
    "color": "#999999",
    "selectedColor": "#00b783",
    "borderStyle": "white",
    "backgroundColor": "#ffffff",
    "list": [
      {
        "pagePath": "pages/tabbar/index/index",
        "text": "首页",
        "iconPath": "static/icons/home.png",
        "selectedIconPath": "static/icons/home-active.png"
      },
      {
        "pagePath": "pages/tabbar/function/function",
        "text": "功能",
        "iconPath": "static/icons/function.png",
        "selectedIconPath": "static/icons/function-active.png"
      },
      {
        "pagePath": "pages/tabbar/user/user",
        "text": "我的",
        "iconPath": "static/icons/user.png",
        "selectedIconPath": "static/icons/user-active.png"
      }
    ]
  }
}
```

**注意事项**：

- tabBar 最少 2 个，最多 5 个
- 图标尺寸建议 81px * 81px
- 图标格式支持 png、jpg、gif

## 4. API 接口规范

### 4.1 请求封装（utils/request.js）

```javascript
/**
 * uni.request 统一封装
 * 功能：网络请求拦截、错误处理、Token 管理
 * 遵循原则：KISS、YAGNI
 * Source: SK/api/seepage.js
 */

import config from '@/config/index.js'

/**
 * 统一请求方法
 * @param {Object} options - 请求配置
 * @param {string} options.url - 请求路径（相对路径）
 * @param {string} [options.method='GET'] - 请求方法
 * @param {Object} [options.data={}] - 请求参数
 * @param {Object} [options.header={}] - 请求头
 * @returns {Promise} 请求结果
 */
export function request(options) {
  return new Promise((resolve, reject) => {
    // 获取 Token
    const token = uni.getStorageSync('token')
  
    // 构建请求配置
    const requestConfig = {
      url: `${config.baseUrl}${options.url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : '',
        ...options.header
      },
      timeout: config.timeout || 15000
    }
  
    // 请求日志
    console.log('[API请求]', requestConfig.method, options.url)
    console.log('[请求参数]', requestConfig.data)
  
    // 发起请求
    uni.request({
      ...requestConfig,
      success: (res) => {
        console.log('[API响应]', res.statusCode, options.url)
      
        // 成功响应
        if (res.statusCode === 200) {
          // 业务逻辑判断
          if (res.data.code === 0 || res.data.code === 200) {
            resolve(res.data)
          } else {
            // 业务错误
            const errorMsg = res.data.message || '请求失败'
            console.error('[业务错误]', errorMsg)
            uni.showToast({
              title: errorMsg,
              icon: 'none',
              duration: 2000
            })
            reject(new Error(errorMsg))
          }
        } else if (res.statusCode === 401) {
          // 未授权，跳转登录
          console.warn('[未授权] 跳转登录页')
          uni.removeStorageSync('token')
          uni.reLaunch({
            url: '/pages/login/login'
          })
          reject(new Error('未授权，请重新登录'))
        } else {
          // HTTP 错误
          const errorMsg = `请求失败: ${res.statusCode}`
          console.error('[HTTP错误]', errorMsg)
          uni.showToast({
            title: errorMsg,
            icon: 'none'
          })
          reject(new Error(errorMsg))
        }
      },
      fail: (err) => {
        // 网络错误
        console.error('[网络错误]', err)
        uni.showToast({
          title: '网络连接失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

/**
 * GET 请求
 */
export function get(url, params = {}, header = {}) {
  return request({
    url,
    method: 'GET',
    data: params,
    header
  })
}

/**
 * POST 请求
 */
export function post(url, data = {}, header = {}) {
  return request({
    url,
    method: 'POST',
    data,
    header
  })
}

/**
 * PUT 请求
 */
export function put(url, data = {}, header = {}) {
  return request({
    url,
    method: 'PUT',
    data,
    header
  })
}

/**
 * DELETE 请求
 */
export function del(url, data = {}, header = {}) {
  return request({
    url,
    method: 'DELETE',
    data,
    header
  })
}

export default {
  request,
  get,
  post,
  put,
  del
}
```

### 4.2 API 模块定义（services/xxx.js）

```javascript
/**
 * 渗流监测 API 模块
 * 功能：封装渗流数据相关的所有 API 请求
 * 遵循原则：KISS、单一职责
 * Source: SK/api/seepage.js
 */
import { get, post } from '@/utils/request.js'

/**
 * 获取渗流数据列表
 * @param {Object} params - 查询参数
 * @param {string} [params.pointId] - 测点ID
 * @param {string} [params.startTime] - 开始时间
 * @param {string} [params.endTime] - 结束时间
 * @returns {Promise<Array>} 渗流数据列表
 */
export function getSeepageList(params = {}) {
  return get('/data-new/list', params)
}

/**
 * 获取渗流数据分页
 * @param {Object} params - 查询参数
 * @param {number} [params.current=1] - 当前页
 * @param {number} [params.size=10] - 每页条数
 * @returns {Promise<Object>} { total, records }
 */
export function getSeepagePage(params = {}) {
  return get('/data-new/page', {
    current: 1,
    size: 10,
    ...params
  })
}

/**
 * 获取所有测点列表
 * @returns {Promise<Array>} 测点列表
 */
export function getPoints() {
  return get('/data-new/points')
}

export default {
  getSeepageList,
  getSeepagePage,
  getPoints
}
```

### 4.3 配置文件（config/index.js）

```javascript
/**
 * API 配置模块
 * 功能：统一 API 基础地址配置
 * 遵循原则：KISS
 */

// 开发环境
const devBaseUrl = 'http://192.168.1.100:8081'

// 生产环境
const prodBaseUrl = 'http://111.4.68.108:8081'

// 根据环境自动切换
const baseUrl = process.env.NODE_ENV === 'development' ? devBaseUrl : prodBaseUrl

export default {
  // API 基础地址
  baseUrl: baseUrl,
  
  // 请求超时时间（毫秒）
  timeout: 15000,
  
  // 分页默认配置
  pageSize: 10,
  
  // 图片上传大小限制（MB）
  maxImageSize: 5
}
```

### 4.4 页面中调用 API

```vue
<script>
import { getSeepageList, getPoints } from '@/services/seepage.js'

export default {
  data() {
    return {
      dataList: [],
      points: [],
      loading: false
    }
  },
  
  onLoad() {
    this.loadData()
    this.loadPoints()
  },
  
  methods: {
    /**
     * 加载渗流数据
     */
    async loadData() {
      this.loading = true
      try {
        const res = await getSeepageList({
          startTime: '2024-01-01 00:00:00',
          endTime: '2024-12-31 23:59:59'
        })
        this.dataList = res.data || []
      } catch (error) {
        console.error('加载数据失败:', error)
      } finally {
        this.loading = false
      }
    },
  
    /**
     * 加载测点列表
     */
    async loadPoints() {
      try {
        const res = await getPoints()
        this.points = res.data || []
      } catch (error) {
        console.error('加载测点失败:', error)
      }
    }
  }
}
</script>
```

## 5. 组件开发规范

### 5.1 组件分类

| 分类     | 特征               | 目录                     | 复用范围     |
| -------- | ------------------ | ------------------------ | ------------ |
| 公共组件 | 高度复用、通用性强 | `components/common/`   | 全局复用     |
| 业务组件 | 业务逻辑绑定强     | `components/business/` | 特定业务场景 |

### 5.2 组件文件结构

**推荐写法（Vue 3 + Tailwind）**：

```vue
<template>
  <view class="bg-white rounded-lg p-5 shadow-sm">
    <view class="flex items-center justify-between mb-5">
      <text class="text-lg font-bold text-gray-900">{{ title }}</text>
      <slot name="header"></slot>
    </view>
    <view class="text-base text-gray-600">
      <slot></slot>
    </view>
  </view>
</template>

<script setup>
/**
 * 用户卡片组件
 * 功能：展示用户信息卡片
 * 遵循原则：KISS、单一职责 - 推荐使用 Tailwind CSS
 */
defineProps({
  // 标题
  title: {
    type: String,
    required: true
  },
  // 是否显示边框
  bordered: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['click'])

/**
 * 处理点击事件
 */
const handleClick = () => {
  emit('click')
}
</script>
```

**旧写法（Options API + BEM - 不推荐）**：

```vue
<script>
export default {
  name: 'UserCard',
  props: {
    title: { type: String, required: true },
    bordered: { type: Boolean, default: true }
  },
  methods: {
    handleClick() {
      this.$emit('click')
    }
  }
}
</script>

<style scoped lang="scss">
/* 使用 BEM 命名规范 - 已废弃，不推荐 */
.user-card {
  padding: 20rpx;
  background-color: #ffffff;
  border-radius: 10rpx;
}

.user-card__header {
  margin-bottom: 20rpx;
}

.user-card__title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333333;
}

.user-card__body {
  font-size: 28rpx;
  color: #666666;
}
</style>
```

**为什么推荐 Tailwind**：
- ✅ 无需定义 CSS 类名，样式即写即用
- ✅ 原子化类名，自动去重，无样式膨胀
- ✅ 响应式设计原生支持（`sm:` `md:` `lg:`）
- ❌ BEM 需要手动维护类名，增加维护成本

### 5.3 组件注册

**全局注册（main.js）**：

```javascript
import { createSSRApp } from 'vue'
import App from './App.vue'

// 导入公共组件（示例）
import SkCard from '@/components/common/SkCard.vue'
import SkButton from '@/components/common/SkButton.vue'

export function createApp() {
  const app = createSSRApp(App)

  // 全局注册组件（可选）
  app.component('SkCard', SkCard)
  app.component('SkButton', SkButton)

  return {
    app
  }
}
```

**局部注册（页面中）**：

```vue
<script setup>
import SkCard from '@/components/common/SkCard.vue'
import SkButton from '@/components/common/SkButton.vue'

// 注册组件
const components = {
  SkCard,
  SkButton
}
</script>
```

### 5.4 组件通信

**Props 传递数据**：

```vue
<template>
  <SkCard :title="userName" :bordered="true" @click="handleCardClick">
    <text>卡片内容</text>
  </SkCard>
</template>
```

**Events 触发事件**：

```vue
<template>
  <SkCard @click="handleCardClick" />
</template>

<script setup>
const handleCardClick = () => {
  console.log('卡片被点击')
}
</script>
```

**Provide / Inject（跨层级通信）**：

```vue
<!-- 父组件 -->
<script setup>
import { provide } from 'vue'

provide('theme', 'dark')
provide('userInfo', userInfo)
</script>

<!-- 子组件 -->
<script setup>
import { inject } from 'vue'

const theme = inject('theme')
console.log('主题：', theme) // 'dark'
</script>
```

### 5.5 组件设计原则

**KISS 原则**：
- 组件功能单一，职责明确
- 避免过度封装，使用 Tailwind 精简样式
- 接口简单易用，保持 API 一致性

**YAGNI 原则**：
- 只实现当前需要的功能
- 不预设未来可能用到的功能
- 按需扩展，使用 Tailwind 快速调整样式

**SOLID 原则**：
- **单一职责**：每个组件只做一件事
- **开放封闭**：通过 props 和 slots 扩展功能
- **接口隔离**：暴露最小必要接口
- **依赖倒置**：依赖抽象而非具体实现

**Tailwind 优势**：
- 样式与逻辑分离，组件更专注业务
- 不需要维护复杂的 CSS 层级关系
- 直接在模板中看到样式，快速理解组件外观

### 5.6 公共组件开发

**开发公共组件的注意事项**：

1. **优先使用 Tailwind 类名**，避免自定义 CSS
2. **保持 Props 接口简洁**，最多 5-7 个核心参数
3. **统一的视觉风格**，使用统一颜色和间距
4. **完善的文档注释**，说明每个 Prop 的作用

**示例：SkCard 组件**：

```vue
<template>
  <view
    class="bg-white rounded-xl overflow-hidden transition-all duration-300"
    :class="[
      shadow === 'always' ? 'shadow-sm' : 'shadow-none',
      border ? 'border border-gray-200' : ''
    ]"
  >
    <!-- 头部 -->
    <view v-if="title || $slots.header" class="px-6 py-4 border-b border-gray-50">
      <text class="text-lg font-semibold text-gray-900">{{ title }}</text>
      <slot name="header"></slot>
    </view>

    <!-- 内容 -->
    <view class="px-6 py-5">
      <slot></slot>
    </view>

    <!-- 底部 -->
    <view v-if="$slots.footer" class="px-6 py-4 border-t border-gray-50">
      <slot name="footer"></slot>
    </view>
  </view>
</template>
```

**为什么 SkCard 不用 BEM**：
- 原生 BEM：`.sk-card__header--large`（冗长）
- Tailwind：直接使用 `text-lg font-semibold`（直观）

### 5.7 样式隔离

**scoped 样式**：
- 默认使用 `scoped` 属性，确保样式只作用于当前组件
- 不需要使用 `/deep/` 或 `::v-deep` 深选择器

**Tailwind 原子类**：
- 所有样式都通过 Tailwind 类名实现，无需 scoped CSS
- 如果需要覆盖，使用 `!` 强制优先级：`!text-red-500`

```vue
<template>
  <!-- 使用 Tailwind 类名，无需额外样式 -->
  <view class="p-4 bg-white rounded-lg text-gray-900">
    <text class="text-base">内容</text>
  </view>
</template>
```

### 5.8 组件性能优化

**使用 computed 生成动态类名**：

```vue
<script setup>
import { computed } from 'vue'

const props = defineProps({
  size: { type: String, default: 'large' },
  type: { type: String, default: 'primary' }
})

// 动态生成 Tailwind 类名
const buttonClasses = computed(() => {
  return [
    'px-6 py-3 rounded-lg font-medium transition-colors',
    {
      'text-lg': props.size === 'large',
      'text-sm': props.size === 'small',
      'bg-primary text-white': props.type === 'primary',
      'bg-gray-200 text-gray-900': props.type === 'default',
      'opacity-60': disabled.value
    }
  ]
})
</script>
```

**避免过度使用深度选择器**：
- ❌ `.parent /deep/ .child { }`
- ✅ 直接使用 Tailwind 类修饰子元素
    }
  }
}
</script>

<!-- 子组件 -->
<script>
export default {
  inject: ['theme'],
  mounted() {
    console.log(this.theme) // 'dark'
  }
}
</script>
```

### 5.5 组件设计原则

**KISS 原则**：

- 组件功能单一，职责明确
- 避免过度封装
- 接口简单易用

**YAGNI 原则**：

- 只实现当前需要的功能
- 不预设未来可能用到的功能
- 按需扩展

**SOLID 原则**：

- **单一职责**：每个组件只做一件事
- **开放封闭**：通过 props 和 slots 扩展功能
- **接口隔离**：暴露最小必要接口

## 6. 页面开发规范

### 6.1 页面生命周期

```vue
<script>
export default {
  /**
   * 页面加载时触发（仅一次）
   * 用途：接收页面参数、初始化数据
   */
  onLoad(options) {
    console.log('页面加载', options)
    this.loadData()
  },
  
  /**
   * 页面显示时触发（每次都会触发）
   * 用途：刷新数据、恢复状态
   */
  onShow() {
    console.log('页面显示')
  },
  
  /**
   * 页面初次渲染完成时触发（仅一次）
   * 用途：DOM 操作、动画初始化
   */
  onReady() {
    console.log('页面渲染完成')
  },
  
  /**
   * 页面隐藏时触发
   * 用途：暂停定时器、保存状态
   */
  onHide() {
    console.log('页面隐藏')
  },
  
  /**
   * 页面卸载时触发
   * 用途：清理资源、取消请求
   */
  onUnload() {
    console.log('页面卸载')
  },
  
  /**
   * 下拉刷新时触发
   * 注意：需在 pages.json 中配置 enablePullDownRefresh: true
   */
  onPullDownRefresh() {
    console.log('下拉刷新')
    this.loadData().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  
  /**
   * 上拉加载更多时触发
   */
  onReachBottom() {
    console.log('上拉加载更多')
    this.loadMore()
  }
}
</script>
```

### 6.2 页面导航

**跳转到普通页面**：

```javascript
// 保留当前页面，跳转到应用内的某个页面
uni.navigateTo({
  url: '/pages/modules/seepage/seepage?id=123'
})

// 关闭当前页面，跳转到应用内的某个页面
uni.redirectTo({
  url: '/pages/modules/seepage/seepage'
})

// 关闭所有页面，打开到应用内的某个页面
uni.reLaunch({
  url: '/pages/login/login'
})

// 返回上一页面或多级页面
uni.navigateBack({
  delta: 1 // 返回的页面数，默认 1
})
```

**跳转到 tabBar 页面**：

```javascript
// 跳转到 tabBar 页面，并关闭其他所有非 tabBar 页面
uni.switchTab({
  url: '/pages/tabbar/index/index'
})
```

**页面间传参**：

```javascript
// 发送页面
uni.navigateTo({
  url: '/pages/detail/detail?id=123&name=test'
})

// 接收页面
export default {
  onLoad(options) {
    console.log(options.id)   // '123'
    console.log(options.name) // 'test'
  }
}
```

### 6.3 页面模板结构

**推荐写法（Vue 3 + Tailwind + Composition API）**：

```vue
<template>
  <view class="min-h-screen bg-gray-50">
    <!-- 自定义导航栏（可选） -->
    <view v-if="useCustomNavbar" class="h-22 flex items-center justify-center bg-primary">
      <text class="text-lg font-bold text-white">{{ pageTitle }}</text>
    </view>

    <!-- 页面主体内容 -->
    <view class="p-5">
      <!-- 加载状态 -->
      <view v-if="loading" class="flex flex-col items-center justify-center py-25">
        <uni-icons type="spinner-cycle" size="40" color="#00b783" />
        <text class="text-base text-gray-500 mt-5">加载中...</text>
      </view>

      <!-- 空数据状态 -->
      <view v-else-if="!loading && dataList.length === 0" class="flex flex-col items-center justify-center py-25">
        <image class="w-75 h-75" src="/static/images/empty.png" mode="aspectFit" />
        <text class="text-base text-gray-500 mt-5">暂无数据</text>
      </view>

      <!-- 数据列表 -->
      <view v-else class="space-y-3">
        <view
          v-for="item in dataList"
          :key="item.id"
          class="p-6 bg-white rounded-lg shadow-sm"
          @click="handleItemClick(item)"
        >
          <text class="text-base font-bold text-gray-900 block mb-2">{{ item.title }}</text>
          <text class="text-sm text-gray-600">{{ item.description }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * 页面名称
 * 功能：页面功能描述
 * 遵循原则：KISS、YAGNI、优先使用 Tailwind
 */
import { ref, onLoad } from 'vue'

// 响应式数据
const pageTitle = ref('页面标题')
const useCustomNavbar = ref(false)
const loading = ref(false)
const dataList = ref([])

onLoad((options) => {
  loadData()
})

/**
 * 加载数据
 */
const loadData = async () => {
  loading.value = true
  try {
    // API 调用
    const res = await uni.$api.getData()
    dataList.value = res.data || []
  } catch (error) {
    console.error('加载数据失败:', error)
    uni.showToast({
      title: '加载失败',
      icon: 'none'
    })
  } finally {
    loading.value = false
  }
}

/**
 * 处理列表项点击
 */
const handleItemClick = (item) => {
  uni.navigateTo({
    url: `/pages/detail/detail?id=${item.id}`
  })
}
</script>
```

**旧写法（Options API + 自定义CSS - 不推荐）**：

```vue
<template>
  <view class="page-container">
    <!-- 自定义导航栏 -->
    <view class="custom-navbar">
      <text class="navbar-title">{{ pageTitle }}</text>
    </view>

    <!-- 页面主体内容 -->
    <view class="page-content">
      <!-- 加载状态 -->
      <view v-if="loading" class="loading-container">
        <uni-icons type="spinner-cycle" size="40" color="#00b783" />
        <text class="loading-text">加载中...</text>
      </view>

      <!-- 数据列表 -->
      <view v-else class="data-list">
        <view class="data-item" @click="handleItemClick(item)">
          <text class="item-title">{{ item.title }}</text>
          <text class="item-desc">{{ item.description }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      pageTitle: '页面标题',
      loading: false,
      dataList: []
    }
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await this.$api.getData()
        this.dataList = res.data || []
      } finally {
        this.loading = false
      }
    },
    handleItemClick(item) {
      uni.navigateTo({
        url: `/pages/detail/detail?id=${item.id}`
      })
    }
  }
}
</script>

<style scoped lang="scss">
.page-container {
  min-height: 100vh;
  background-color: #f8f8f8;
}

.custom-navbar {
  height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #00b783;
}

.navbar-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #ffffff;
}

.page-content {
  padding: 20rpx;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.loading-text {
  margin-top: 20rpx;
  font-size: 28rpx;
  color: #999999;
}

.data-list {
  /* 列表样式 */
}

.data-item {
  padding: 30rpx;
  margin-bottom: 20rpx;
  background-color: #ffffff;
  border-radius: 10rpx;
}

.item-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333333;
}

.item-desc {
  margin-top: 10rpx;
  font-size: 28rpx;
  color: #666666;
}
</style>
```

**为什么推荐 Tailwind**：
- ✅ 类名更直接：`text-lg font-bold` vs `.navbar-title`
- ✅ 无需维护复杂的 CSS 层级关系
- ✅ 响应式支持：`sm:` `md:` `lg:` 前缀
- ❌ 自定义 CSS 需要维护多个层级，类名冗长

**样式方案优先级**：
1. **Tailwind CSS（强烈推荐）** - 原子化类名，一致性好
2. **公共组件样式** - 利用 `components/common/` 预制组件
3. **原生 CSS/SCSS** - 仅在 Tailwind 无法实现时使用

### 7.1 Tailwind CSS 配置（必选）

**安装依赖**：

```bash
npm i -D tailwindcss@3 postcss autoprefixer weapp-tailwindcss
npx tailwindcss init
```

**配置 tailwind.config.js**：

```javascript
/**
 * Tailwind CSS 配置
 * 功能：定制主题和扫描路径
 * 遵循原则：KISS
 */
const path = require('path')

const resolve = (p) => path.resolve(__dirname, p)

module.exports = {
  content: [
    './pages/**/*.{vue,js,ts}',
    './components/**/*.{vue,js,ts}',
    './App.vue'
  ].map(resolve),
  theme: {
    extend: {
      colors: {
        primary: '#00b783',     // 主色调（绿色）
        success: '#4cd964',     // 成功色
        warning: '#f0ad4e',     // 警告色
        error: '#dd524d'        // 错误色
      },
      spacing: {
        'safe-top': 'var(--status-bar-height)',
        'safe-bottom': 'var(--window-bottom)'
      }
    }
  },
  plugins: [],
  corePlugins: {
    preflight: false  // uni-app 要求关闭
  }
}
```

**在 App.vue 中引入**：

```vue
<style>
/* 引入 Tailwind CSS - 必须在前 */
@tailwind base;
@tailwind components;
@tailwind utilities;

/* 全局页面样式 */
page {
  background-color: #f8f8f8;  /* 页面背景 */
  font-size: 28rpx;            /* 基础字体 */
  color: #333333;              /* 基础文字色 */
}

/* 重置基础元素 */
view, text, button, input {
  box-sizing: border-box;
}
</style>
```

**安装 Tailwind CSS 智能提示插件**：
- VS Code: 搜索 "Tailwind CSS IntelliSense"
- HBuilderX: 已在项目配置中启用

### 7.2 Tailwind CSS 核心用法

**基础类名映射**：

| 样式属性 | Tailwind 类名 | 等价原生 CSS |
|---------|--------------|-------------|
| `display: flex` | `flex` | `display: flex` |
| `justify-content: space-between` | `justify-between` | `justify-content: space-between` |
| `padding: 20rpx` | `p-5` | `padding: 20rpx` |
| `margin-bottom: 20rpx` | `mb-5` | `margin-bottom: 20rpx` |
| `background-color: #ffffff` | `bg-white` | `background-color: #ffffff` |
| `color: #333333` | `text-gray-900` | `color: #333333` |
| `font-size: 28rpx` | `text-base` | `font-size: 28rpx` |
| `border-radius: 10rpx` | `rounded-lg` | `border-radius: 10rpx` |
| `border-width: 2rpx` | `border-2` | `border-width: 2rpx` |
| `width: 100%` | `w-full` | `width: 100%` |
| `height: 100%` | `h-full` | `height: 100%` |
| `min-height: 100vh` | `min-h-screen` | `min-height: 100vh` |

**常用布局示例**：

```vue
<template>
  <!-- 基础页面布局 -->
  <view class="min-h-screen bg-gray-50 p-5">

    <!-- 卡片容器 -->
    <view class="bg-white rounded-xl shadow-sm p-6 mb-5">
      <text class="text-lg font-bold text-gray-900">标题</text>
    </view>

    <!-- Flex 布局 -->
    <view class="flex items-center justify-between p-4 bg-white rounded-lg">
      <text class="text-base text-gray-700">左内容</text>
      <text class="text-sm text-gray-500">右内容</text>
    </view>

    <!-- Grid 布局 -->
    <view class="grid grid-cols-3 gap-4 mt-5">
      <view class="bg-white rounded-lg p-4 text-center">1</view>
      <view class="bg-white rounded-lg p-4 text-center">2</view>
      <view class="bg-white rounded-lg p-4 text-center">3</view>
    </view>

    <!-- 按钮样式 -->
    <view class="px-6 py-3 bg-primary text-white rounded-lg text-center mt-5">
      <text class="text-base font-medium">提交</text>
    </view>

  </view>
</template>
```

**响应式设计**：

```vue
<template>
  <view class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
    <!-- 在不同屏幕显示不同列数 -->
    <!-- 手机: 1列, 小屏: 2列, 中屏: 3列, 大屏: 4列 -->
  </view>
</template>
```

**伪类和状态**：

```vue
<template>
  <view class="class="text-gray-900 hover:text-primary transition-colors duration-300">
    <!-- 悬停时文字变绿，颜色平滑过渡 300ms -->
  </view>

  <view class="px-6 py-3 bg-gray-200 active:bg-gray-300 disabled:opacity-50">
    <!-- 点击时背景变深，禁用时透明度 50% -->
  </view>
</template>
```

### 7.3 与原生 CSS 对比

**原生写法（不推荐）**：

```vue
<style scoped lang="scss">
.container {
  padding: 20rpx;
  background-color: #ffffff;
  border-radius: 10rpx;

  .title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333333;
  }
}
</style>
```

**Tailwind 写法（推荐）**：

```vue
<template>
  <view class="p-5 bg-white rounded-lg">
    <text class="text-lg font-bold text-gray-900">标题</text>
  </view>
</template>
```

**优势对比**：
- ✅ 样式一致性好，不需重复定义变量
- ✅ 原子化类名，按需加载，无样式膨胀
- ✅ 智能提示，降低学习成本
- ✅ 响应式设计原生支持
- ❌ 需在 template 中写长类名（可用 prettier 自动格式化）

### 7.4 间距系统（Tailwind vs 原生）

**Tailwind 间距表**（单位：rem，自动转换为 rpx）：

| 类名 | px 值 | rpx 值 | 使用场景 |
|------|-------|--------|----------|
| `p-1` | 4px | 8rpx | 微小内边距 |
| `p-2` | 8px | 16rpx | 小内边距 |
| `p-3` | 12px | 24rpx | 小内边距 |
| `p-4` | 16px | 32rpx | **常用** |
| `p-5` | 20px | 40rpx | **常用** |
| `p-6` | 24px | 48rpx | 大内边距 |
| `p-8` | 32px | 64rpx | 超大内边距 |

**推荐间距使用规范**：
- 页面边距：`p-5`（20px）
- 卡片内边距：`p-4`（16px）
- 按钮内边距：`px-6 py-3`（24px x 12px）
- 列表项间距：`mb-3`（12px）
- 表单元素间距：`space-y-4`（每个子元素下边距 16px）

### 7.5 颜色系统

**文字颜色**：
- `text-gray-900` - 深色文字（#111827）
- `text-gray-700` - 中等文字（#374151）
- `text-gray-500` - 次要文字（#6B7280）
- `text-gray-400` - 占位文字（#9CA3AF）

**背景颜色**：
- `bg-white` - 白色背景
- `bg-gray-50` - 浅灰背景（#F9FAFB）
- `bg-gray-100` - 稍深灰背景（#F3F4F6）
- `bg-primary` - 主色调（#00B783）

**边框颜色**：
- `border-gray-200` - 淡灰色边框（#E5E7EB）
- `border-gray-300` - 中等灰色边框（#D1D5DB）
- `border-primary` - 主色调边框

### 7.6 自定义颜色（特殊情况）

当 Tailwind 默认颜色不满足需求时，可在组件内部定义：

```vue
<script setup>
// 如需使用自定义颜色，在组件末尾添加 scoped 样式
</script>

<style scoped>
/* 主色调 */
.text-primary {
  color: #00b783;
}
.bg-primary {
  background-color: #00b783;
}
.border-primary {
  border-color: #00b783;
}

/* 状态色（success / warning / error 已内置可直接使用） */
.text-success {
  color: #4cd964;
}
.text-warning {
  color: #f0ad4e;
}
.text-error {
  color: #dd524d;
}
</style>
```

### 7.7 组件开发样式规范

**使用公共组件（强烈推荐）**：

优先使用 `components/common/` 中的预制组件，避免重复开发样式：

```vue
<template>
  <!-- 使用 SkCard 卡片组件 -->
  <SkCard title="标题文字" shadow="always">
    <text>卡片内容</text>
    <template #footer>
      <SkButton text="提交" type="primary" />
    </template>
  </SkCard>

  <!-- 使用 SkInput 输入框组件 -->
  <SkInput
    v-model="form.name"
    label="姓名"
    placeholder="请输入姓名"
    clearable
  />

  <!-- 使用 SkSelect 选择器组件 -->
  <SkSelect
    v-model="form.dept"
    label="部门"
    :options="deptOptions"
    placeholder="请选择部门"
  />

  <!-- 使用 SkButton 按钮组件 -->
  <SkButton
    text="保存"
    type="primary"
    size="large"
    :loading="saving"
    @click="handleSave"
  />
</template>
```

**何时需要自定义样式**：
1. 页面布局复杂，需要组合多个 Tailwind 类
2. 特殊动画效果（`transition-xxx`）
3. 第三方组件库样式覆盖

### 7.8 原生 CSS/SCSS 使用场景

**仅在以下情况使用原生样式**：

1. **伪元素**：
```vue
<style scoped>
.custom-element::after {
  content: '';
  display: block;
  width: 100%;
  height: 2rpx;
  background-color: #e5e5e5;
}
</style>
```

2. **复杂动画**：
```vue
<style scoped>
@keyframes slideIn {
  from {
    transform: translateX(-100%);
  }
  to {
    transform: translateX(0);
  }
}
.slide-animation {
  animation: slideIn 0.3s ease-out;
}
</style>
```

3. **覆盖第三方库样式**：
```vue
<style scoped>
/deep/ .third-party-class {
  /* 深度选择器覆盖外部组件样式 */
}
</style>
```

**禁止使用 BEM 命名规范**：
- ❌ `.user-card__header--large`
- ✅ 直接使用 Tailwind 类：`text-lg font-bold text-gray-900`

### 7.9 样式检查清单

在提交代码前，检查以下项目：

- [ ] **优先使用 Tailwind 类名**，而非自定义 CSS
- [ ] **复用公共组件**，避免重复实现已有样式
- [ ] **遵守间距规范**，使用标准间距值
- [ ] **颜色系统一致**，统一使用 Tailwind 颜色
- [ ] **响应式设计**，在移动端测试显示效果
- [ ] **性能优化**，避免过深的类名嵌套
}
/* #endif */
</style>
```

## 8. 状态管理规范（Pinia）

### 8.1 Store 目录结构

```
stores/
├── modules/              # 模块化 store
│   ├── user.js           # 用户模块
│   ├── auth.js           # 认证模块
│   └── app.js            # 应用模块
└── index.js              # store 入口
```

### 8.2 Store 入口文件（stores/index.js）

```javascript
/**
 * Pinia Store 入口
 * 功能：状态管理中心
 * Source: Pinia 官方文档
 */
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

export default pinia
```

### 8.3 模块定义（stores/modules/user.js）

```javascript
/**
 * 用户模块
 * 功能：管理用户信息状态
 * 遵循原则：KISS、单一职责
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  // State（使用 ref）
  const userInfo = ref(null)
  const token = ref(uni.getStorageSync('token') || '')
  
  // Getters（使用 computed）
  const isLoggedIn = computed(() => !!token.value)
  const userName = computed(() => userInfo.value?.name || '未登录')
  const userRole = computed(() => userInfo.value?.role || 'guest')
  
  // Actions（普通函数）
  /**
   * 登录
   */
  async function login(credentials) {
    try {
      const res = await uni.$api.login(credentials)
      token.value = res.data.token
      userInfo.value = res.data.userInfo
      uni.setStorageSync('token', res.data.token)
      return res
    } catch (error) {
      console.error('登录失败:', error)
      throw error
    }
  }
  
  /**
   * 登出
   */
  function logout() {
    userInfo.value = null
    token.value = ''
    uni.removeStorageSync('token')
    uni.reLaunch({
      url: '/pages/login/login'
    })
  }
  
  /**
   * 获取用户信息
   */
  async function getUserInfo() {
    try {
      const res = await uni.$api.getUserInfo()
      userInfo.value = res.data
      return res
    } catch (error) {
      console.error('获取用户信息失败:', error)
      throw error
    }
  }
  
  return {
    // State
    userInfo,
    token,
    // Getters
    isLoggedIn,
    userName,
    userRole,
    // Actions
    login,
    logout,
    getUserInfo
  }
}, {
  // 持久化配置
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'user',
        storage: {
          getItem: (key) => uni.getStorageSync(key),
          setItem: (key, value) => uni.setStorageSync(key, value)
        }
      }
    ]
  }
})
```

### 8.4 在页面中使用 Store（Vue 3 Composition API）

```vue
<template>
  <view class="user-page">
    <text>用户名：{{ userName }}</text>
    <text>角色：{{ userRole }}</text>
    <button @click="handleLogout">退出登录</button>
  </view>
</template>

<script setup>
import { useUserStore } from '@/stores/modules/user'

// 使用 Store
const userStore = useUserStore()

// 直接访问 state 和 getters
const { userName, userRole, isLoggedIn } = userStore

// 调用 actions
const handleLogout = () => {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: (res) => {
      if (res.confirm) {
        userStore.logout()
      }
    }
  })
}

// 生命周期
import { onLoad } from '@dcloudio/uni-app'

onLoad(() => {
  if (isLoggedIn.value) {
    userStore.getUserInfo()
  }
})
</script>
```

### 8.5 直接访问 Store

```javascript
import { useUserStore } from '@/stores/modules/user'

// 在 setup 外部使用
const userStore = useUserStore()

// 获取 state
const token = userStore.token

// 获取 getters
const isLoggedIn = userStore.isLoggedIn

// 调用 actions
userStore.login({ username: 'admin', password: '123456' })

// 重置 store
userStore.$reset()
```

### 8.6 Pinia vs Vuex 对比

| 特性 | Pinia | Vuex |
|------|-------|------|
| Vue 版本 | Vue 3 | Vue 2/3 |
| TypeScript 支持 | 原生支持 | 需要额外配置 |
| 模块化 | 自动模块化 | 需要手动配置 |
| Mutations | 无需定义 | 必须定义 |
| DevTools | 完整支持 | 完整支持 |
| 代码量 | 更少 | 更多 |
| 学习曲线 | 更平缓 | 较陡峭 |

## 9. 本地存储规范

### 9.1 Storage 工具封装（utils/storage.js）

```javascript
/**
 * 本地存储工具
 * 功能：封装 uni.storage API，提供类型安全的存储方法
 * 遵循原则：KISS
 */

/**
 * 存储数据
 * @param {string} key - 存储键
 * @param {any} value - 存储值
 * @returns {boolean} 是否成功
 */
export function setStorage(key, value) {
  try {
    uni.setStorageSync(key, value)
    return true
  } catch (error) {
    console.error('存储失败:', key, error)
    return false
  }
}

/**
 * 获取数据
 * @param {string} key - 存储键
 * @param {any} defaultValue - 默认值
 * @returns {any} 存储值或默认值
 */
export function getStorage(key, defaultValue = null) {
  try {
    const value = uni.getStorageSync(key)
    return value !== '' ? value : defaultValue
  } catch (error) {
    console.error('读取失败:', key, error)
    return defaultValue
  }
}

/**
 * 移除数据
 * @param {string} key - 存储键
 * @returns {boolean} 是否成功
 */
export function removeStorage(key) {
  try {
    uni.removeStorageSync(key)
    return true
  } catch (error) {
    console.error('移除失败:', key, error)
    return false
  }
}

/**
 * 清空所有数据
 * @returns {boolean} 是否成功
 */
export function clearStorage() {
  try {
    uni.clearStorageSync()
    return true
  } catch (error) {
    console.error('清空失败:', error)
    return false
  }
}

/**
 * 存储对象（自动序列化）
 * @param {string} key - 存储键
 * @param {Object} obj - 对象
 * @returns {boolean} 是否成功
 */
export function setObject(key, obj) {
  try {
    const jsonStr = JSON.stringify(obj)
    uni.setStorageSync(key, jsonStr)
    return true
  } catch (error) {
    console.error('存储对象失败:', key, error)
    return false
  }
}

/**
 * 获取对象（自动反序列化）
 * @param {string} key - 存储键
 * @param {Object} defaultValue - 默认值
 * @returns {Object} 对象或默认值
 */
export function getObject(key, defaultValue = {}) {
  try {
    const jsonStr = uni.getStorageSync(key)
    if (!jsonStr) return defaultValue
    return JSON.parse(jsonStr)
  } catch (error) {
    console.error('读取对象失败:', key, error)
    return defaultValue
  }
}

export default {
  setStorage,
  getStorage,
  removeStorage,
  clearStorage,
  setObject,
  getObject
}
```

### 9.2 使用示例

```javascript
import { setStorage, getStorage, setObject, getObject } from '@/utils/storage.js'

// 存储字符串
setStorage('username', 'admin')

// 读取字符串
const username = getStorage('username', '游客')

// 存储对象
setObject('userInfo', {
  id: 1,
  name: 'admin',
  role: 'admin'
})

// 读取对象
const userInfo = getObject('userInfo', {})

// 移除数据
removeStorage('username')

// 清空所有数据
clearStorage()
```

### 9.3 存储键命名规范

```javascript
// 推荐：使用常量定义存储键
export const STORAGE_KEYS = {
  TOKEN: 'token',
  USER_INFO: 'userInfo',
  THEME: 'theme',
  LANGUAGE: 'language',
  CACHE_DATA: 'cacheData'
}

// 使用
import { STORAGE_KEYS } from '@/constants/storage.js'
setStorage(STORAGE_KEYS.TOKEN, 'xxx')
```

## 10. 条件编译规范

### 10.1 条件编译语法

uni-app 支持通过条件编译实现跨平台差异化开发。

**支持的平台标识**：

- `H5`：H5 平台
- `MP-WEIXIN`：微信小程序
- `MP-ALIPAY`：支付宝小程序
- `APP-PLUS`：App 平台
- `APP-PLUS-NVUE`：App nvue 页面

### 10.2 模板中的条件编译

```vue
<template>
  <view>
    <!-- #ifdef H5 -->
    <view class="h5-only">仅在 H5 显示</view>
    <!-- #endif -->
  
    <!-- #ifdef MP-WEIXIN -->
    <view class="weixin-only">仅在微信小程序显示</view>
    <!-- #endif -->
  
    <!-- #ifdef APP-PLUS -->
    <view class="app-only">仅在 App 显示</view>
    <!-- #endif -->
  
    <!-- #ifndef H5 -->
    <view class="not-h5">除 H5 外的平台显示</view>
    <!-- #endif -->
  
    <!-- #ifdef H5 || MP-WEIXIN -->
    <view class="h5-or-weixin">H5 或微信小程序显示</view>
    <!-- #endif -->
  </view>
</template>
```

### 10.3 脚本中的条件编译

```javascript
export default {
  methods: {
    handleShare() {
      // #ifdef MP-WEIXIN
      // 微信小程序分享
      uni.showShareMenu({
        withShareTicket: true
      })
      // #endif
    
      // #ifdef H5
      // H5 分享
      console.log('H5 分享')
      // #endif
    
      // #ifdef APP-PLUS
      // App 分享
      plus.share.sendWithSystem({
        type: 'text',
        content: '分享内容'
      })
      // #endif
    }
  }
}
```

### 10.4 样式中的条件编译

```vue
<style>
/* #ifdef H5 */
.h5-style {
  padding: 20px;
}
/* #endif */

/* #ifdef MP-WEIXIN */
.weixin-style {
  padding: 20rpx;
}
/* #endif */

/* #ifdef APP-PLUS */
.app-style {
  padding: 20rpx;
}
/* #endif */
</style>
```

### 10.5 pages.json 中的条件编译

```json
{
  "pages": [
    {
      "path": "pages/index/index",
      "style": {
        "navigationBarTitleText": "首页",
        // #ifdef MP-WEIXIN
        "enablePullDownRefresh": true,
        // #endif
        // #ifdef H5
        "enablePullDownRefresh": false
        // #endif
      }
    }
  ]
}
```

### 10.6 条件编译最佳实践

**KISS 原则**：

- 优先使用 uni-app 统一 API
- 仅在必要时使用条件编译
- 避免过度使用条件编译导致代码复杂

**示例**：

```javascript
// 推荐：使用统一 API
uni.showToast({
  title: '提示',
  icon: 'none'
})

// 避免：不必要的条件编译
// #ifdef H5
uni.showToast({ title: '提示' })
// #endif
// #ifdef MP-WEIXIN
uni.showToast({ title: '提示' })
// #endif
```

## 11. 常用 API 规范

### 11.1 提示框

```javascript
// 消息提示框
uni.showToast({
  title: '操作成功',
  icon: 'success',      // success / error / loading / none
  duration: 2000,       // 显示时长（毫秒）
  mask: false          // 是否显示透明蒙层
})

// 隐藏提示框
uni.hideToast()

// 加载提示框
uni.showLoading({
  title: '加载中...',
  mask: true
})

// 隐藏加载提示框
uni.hideLoading()

// 模态对话框
uni.showModal({
  title: '提示',
  content: '确定要删除吗？',
  showCancel: true,
  success: (res) => {
    if (res.confirm) {
      console.log('用户点击确定')
    } else if (res.cancel) {
      console.log('用户点击取消')
    }
  }
})

// 操作菜单
uni.showActionSheet({
  itemList: ['选项1', '选项2', '选项3'],
  success: (res) => {
    console.log('选中索引:', res.tapIndex)
  },
  fail: (err) => {
    console.log('取消选择')
  }
})
```

### 11.2 图片选择与上传

```javascript
/**
 * 选择图片
 */
chooseImage() {
  uni.chooseImage({
    count: 1,                    // 最多可选择的图片数量
    sizeType: ['compressed'],    // 压缩图
    sourceType: ['album', 'camera'], // 相册或相机
    success: (res) => {
      const tempFilePaths = res.tempFilePaths
      this.uploadImage(tempFilePaths[0])
    }
  })
},

/**
 * 上传图片
 */
uploadImage(filePath) {
  uni.showLoading({ title: '上传中...' })
  
  uni.uploadFile({
    url: 'https://api.example.com/upload',
    filePath: filePath,
    name: 'file',
    formData: {
      'user': 'test'
    },
    success: (res) => {
      uni.hideLoading()
      const data = JSON.parse(res.data)
      console.log('上传成功:', data)
      uni.showToast({
        title: '上传成功',
        icon: 'success'
      })
    },
    fail: (err) => {
      uni.hideLoading()
      console.error('上传失败:', err)
      uni.showToast({
        title: '上传失败',
        icon: 'none'
      })
    }
  })
}
```

### 11.3 位置信息

```javascript
/**
 * 获取当前位置
 */
getLocation() {
  uni.getLocation({
    type: 'gcj02',  // 坐标系类型
    success: (res) => {
      console.log('经度:', res.longitude)
      console.log('纬度:', res.latitude)
      console.log('速度:', res.speed)
      console.log('位置精度:', res.accuracy)
    },
    fail: (err) => {
      console.error('获取位置失败:', err)
      uni.showToast({
        title: '获取位置失败',
        icon: 'none'
      })
    }
  })
},

/**
 * 打开地图选择位置
 */
chooseLocation() {
  uni.chooseLocation({
    success: (res) => {
      console.log('位置名称:', res.name)
      console.log('详细地址:', res.address)
      console.log('经度:', res.longitude)
      console.log('纬度:', res.latitude)
    }
  })
}
```

### 11.4 扫码

```javascript
/**
 * 扫描二维码/条形码
 */
scanCode() {
  uni.scanCode({
    onlyFromCamera: false,  // 是否只能从相机扫码
    scanType: ['qrCode', 'barCode'],  // 扫码类型
    success: (res) => {
      console.log('扫码结果:', res.result)
      console.log('扫码类型:', res.scanType)
    
      // 处理扫码结果
      this.handleScanResult(res.result)
    },
    fail: (err) => {
      console.error('扫码失败:', err)
    }
  })
},

/**
 * 处理扫码结果
 */
handleScanResult(result) {
  // 业务逻辑处理
  uni.showToast({
    title: `扫码成功: ${result}`,
    icon: 'none'
  })
}
```

### 11.5 剪贴板

```javascript
/**
 * 设置剪贴板内容
 */
setClipboard(text) {
  uni.setClipboardData({
    data: text,
    success: () => {
      uni.showToast({
        title: '已复制',
        icon: 'success'
      })
    }
  })
},

/**
 * 获取剪贴板内容
 */
getClipboard() {
  uni.getClipboardData({
    success: (res) => {
      console.log('剪贴板内容:', res.data)
    }
  })
}
```

### 11.6 下拉刷新

```javascript
export default {
  onPullDownRefresh() {
    console.log('下拉刷新')
  
    // 加载数据
    this.loadData().then(() => {
      // 停止下拉刷新
      uni.stopPullDownRefresh()
    })
  },
  
  methods: {
    /**
     * 手动触发下拉刷新
     */
    startPullDownRefresh() {
      uni.startPullDownRefresh()
    }
  }
}
```

## 12. 性能优化规范

### 12.1 图片优化

```vue
<template>
  <!-- 使用 mode 属性优化图片显示 -->
  <image 
    :src="imageUrl" 
    mode="aspectFill"
    lazy-load
    @error="handleImageError"
  ></image>
</template>

<script>
export default {
  methods: {
    /**
     * 图片加载失败处理
     */
    handleImageError(e) {
      console.error('图片加载失败:', e)
      // 设置默认图片
      e.target.src = '/static/images/default.png'
    }
  }
}
</script>
```

**mode 属性值**：

- `aspectFit`：保持纵横比缩放，完整显示
- `aspectFill`：保持纵横比缩放，填充满容器
- `widthFix`：宽度不变，高度自动变化
- `heightFix`：高度不变，宽度自动变化

### 12.2 列表优化

```vue
<template>
  <scroll-view 
    scroll-y 
    class="scroll-container"
    @scrolltolower="loadMore"
  >
    <view 
      v-for="item in dataList" 
      :key="item.id"
      class="list-item"
    >
      {{ item.title }}
    </view>
  
    <!-- 加载更多提示 -->
    <view v-if="loading" class="loading-more">
      <text>加载中...</text>
    </view>
  
    <!-- 没有更多数据提示 -->
    <view v-if="!hasMore && dataList.length > 0" class="no-more">
      <text>没有更多数据了</text>
    </view>
  </scroll-view>
</template>

<script>
export default {
  data() {
    return {
      dataList: [],
      loading: false,
      hasMore: true,
      page: 1,
      pageSize: 20
    }
  },
  
  methods: {
    /**
     * 加载更多数据
     */
    async loadMore() {
      if (this.loading || !this.hasMore) return
    
      this.loading = true
      try {
        const res = await this.$api.getList({
          page: this.page,
          pageSize: this.pageSize
        })
      
        const newData = res.data || []
        this.dataList = [...this.dataList, ...newData]
        this.page++
      
        // 判断是否还有更多数据
        if (newData.length < this.pageSize) {
          this.hasMore = false
        }
      } catch (error) {
        console.error('加载失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>
```

### 12.3 避免频繁 setData

```javascript
// 避免：频繁更新数据
this.count = 1
this.count = 2
this.count = 3

// 推荐：批量更新
this.count = 3

// 避免：更新大数据
this.bigData = newBigData

// 推荐：只更新变化的部分
this.bigData[index] = newItem
```

### 12.4 使用防抖和节流

```javascript
/**
 * 防抖函数
 * @param {Function} func - 要执行的函数
 * @param {number} delay - 延迟时间（毫秒）
 */
export function debounce(func, delay = 300) {
  let timer = null
  return function(...args) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      func.apply(this, args)
    }, delay)
  }
}

/**
 * 节流函数
 * @param {Function} func - 要执行的函数
 * @param {number} delay - 间隔时间（毫秒）
 */
export function throttle(func, delay = 300) {
  let lastTime = 0
  return function(...args) {
    const now = Date.now()
    if (now - lastTime >= delay) {
      func.apply(this, args)
      lastTime = now
    }
  }
}

// 使用示例
export default {
  methods: {
    // 搜索输入防抖
    handleSearch: debounce(function(keyword) {
      console.log('搜索:', keyword)
      this.search(keyword)
    }, 500),
  
    // 滚动事件节流
    handleScroll: throttle(function(e) {
      console.log('滚动位置:', e.detail.scrollTop)
    }, 200)
  }
}
```

### 12.5 分包加载

**manifest.json 配置**：

```json
{
  "optimization": {
    "subPackages": true
  }
}
```

**pages.json 配置**：

```json
{
  "pages": [
    {
      "path": "pages/index/index"
    }
  ],
  "subPackages": [
    {
      "root": "modules/inspection",
      "pages": [
        {
          "path": "list/list",
          "style": {
            "navigationBarTitleText": "巡检列表"
          }
        }
      ]
    },
    {
      "root": "modules/maintenance",
      "pages": [
        {
          "path": "list/list",
          "style": {
            "navigationBarTitleText": "维护列表"
          }
        }
      ]
    }
  ]
}
```

## 13. 错误处理与调试

### 13.1 全局错误处理

```javascript
// main.js
import Vue from 'vue'

/**
 * 全局错误处理
 */
Vue.config.errorHandler = function(err, vm, info) {
  console.error('全局错误:', err)
  console.error('错误组件:', vm)
  console.error('错误信息:', info)
  
  // 上报错误到服务器
  reportError({
    error: err.message,
    stack: err.stack,
    component: vm.$options.name,
    info: info
  })
  
  // 显示友好提示
  uni.showToast({
    title: '系统异常，请稍后重试',
    icon: 'none'
  })
}

/**
 * Promise 未捕获错误处理
 */
uni.onUnhandledRejection((event) => {
  console.error('未捕获的 Promise 错误:', event.reason)
  
  // 上报错误
  reportError({
    error: event.reason,
    type: 'unhandledRejection'
  })
})
```

### 13.2 API 错误处理

```javascript
/**
 * 统一错误处理
 */
export function handleError(error, showToast = true) {
  let message = '操作失败，请稍后重试'
  
  if (error.response) {
    // HTTP 错误
    const { status, data } = error.response
    switch (status) {
      case 400:
        message = data.message || '请求参数错误'
        break
      case 401:
        message = '未授权，请重新登录'
        // 跳转登录页
        uni.reLaunch({ url: '/pages/login/login' })
        break
      case 403:
        message = '没有权限访问'
        break
      case 404:
        message = '请求的资源不存在'
        break
      case 500:
        message = '服务器错误'
        break
      default:
        message = data.message || '请求失败'
    }
  } else if (error.request) {
    // 网络错误
    message = '网络连接失败，请检查网络设置'
  } else {
    // 其他错误
    message = error.message || '未知错误'
  }
  
  console.error('错误详情:', error)
  
  if (showToast) {
    uni.showToast({
      title: message,
      icon: 'none',
      duration: 2000
    })
  }
  
  return message
}

// 使用示例
async loadData() {
  try {
    const res = await this.$api.getData()
    this.dataList = res.data
  } catch (error) {
    handleError(error)
  }
}
```

### 13.3 调试技巧

**console 日志**：

```javascript
// 普通日志
console.log('普通日志', data)

// 警告日志
console.warn('警告信息', warning)

// 错误日志
console.error('错误信息', error)

// 表格日志
console.table([
  { name: '张三', age: 20 },
  { name: '李四', age: 25 }
])

// 分组日志
console.group('用户信息')
console.log('姓名:', '张三')
console.log('年龄:', 20)
console.groupEnd()
```

**条件编译调试**：

```javascript
// #ifdef H5
console.log('H5 平台调试信息')
// #endif

// #ifdef MP-WEIXIN
console.log('微信小程序调试信息')
// #endif

// #ifdef APP-PLUS
console.log('App 平台调试信息')
// #endif
```

**性能监控**：

```javascript
/**
 * 性能监控
 */
export default {
  onLoad() {
    const startTime = Date.now()
  
    this.loadData().then(() => {
      const endTime = Date.now()
      console.log('数据加载耗时:', endTime - startTime, 'ms')
    })
  }
}
```

## 14. 代码质量规范

### 14.1 注释规范

**文件头注释**：

```javascript
/**
 * 文件名称
 * 功能描述：简明扼要说明主要功能
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: 参考的资料或文档来源（可选）
 */
```

**函数注释**：

```javascript
/**
 * 函数功能描述
 * @param {string} param1 - 参数1说明
 * @param {number} param2 - 参数2说明
 * @returns {Promise<Object>} 返回值说明
 */
async function fetchData(param1, param2) {
  // 函数实现
}
```

**复杂逻辑注释**：

```javascript
// 步骤1：验证用户输入
if (!this.validateForm()) {
  return
}

// 步骤2：构建提交数据
const submitData = this.buildSubmitData()

// 步骤3：调用 API 提交
await this.submitData(submitData)
```

### 14.2 代码格式化

**推荐使用 Prettier 配置**：

```json
{
  "semi": false,
  "singleQuote": true,
  "printWidth": 100,
  "tabWidth": 2,
  "trailingComma": "none",
  "arrowParens": "avoid"
}
```

**推荐使用 ESLint 配置**：

```json
{
  "extends": [
    "plugin:vue/essential",
    "eslint:recommended"
  ],
  "rules": {
    "no-console": "off",
    "no-debugger": "warn",
    "vue/multi-word-component-names": "off"
  }
}
```

### 14.3 代码审查清单

**功能性**：

- [ ] 功能是否符合需求
- [ ] 边界条件是否处理
- [ ] 错误处理是否完善

**性能**：

- [ ] 是否存在性能瓶颈
- [ ] 是否有不必要的计算
- [ ] 图片是否优化

**可维护性**：

- [ ] 代码是否易读
- [ ] 注释是否清晰
- [ ] 命名是否规范

**安全性**：

- [ ] 用户输入是否验证
- [ ] 敏感信息是否加密
- [ ] API 是否有权限控制

## 15. 常见问题与解决方案

### 15.1 跨域问题（H5）

**manifest.json 配置代理**：

```json
{
  "h5": {
    "devServer": {
      "proxy": {
        "/api": {
          "target": "http://111.4.68.108:8081",
          "changeOrigin": true,
          "pathRewrite": {
            "^/api": ""
          }
        }
      }
    }
  }
}
```

### 15.2 页面栈溢出

**问题**：频繁使用 `uni.navigateTo` 导致页面栈溢出。

**解决方案**：

- 使用 `uni.redirectTo` 替代 `uni.navigateTo`
- 使用 `uni.reLaunch` 清空页面栈
- 控制页面跳转层级

```javascript
// 避免：频繁 navigateTo
uni.navigateTo({ url: '/pages/a/a' })
uni.navigateTo({ url: '/pages/b/b' })
uni.navigateTo({ url: '/pages/c/c' })

// 推荐：使用 redirectTo
uni.redirectTo({ url: '/pages/a/a' })

// 推荐：使用 reLaunch 清空页面栈
uni.reLaunch({ url: '/pages/index/index' })
```

### 15.3 图片不显示

**问题**：图片路径错误或图片加载失败。

**解决方案**：

- 检查图片路径是否正确
- 使用绝对路径或 `@` 别名
- 添加图片加载失败处理

```vue
<template>
  <image 
    :src="imageUrl" 
    @error="handleImageError"
  ></image>
</template>

<script>
export default {
  data() {
    return {
      imageUrl: '/static/images/logo.png'
    }
  },
  methods: {
    handleImageError(e) {
      console.error('图片加载失败')
      // 设置默认图片
      this.imageUrl = '/static/images/default.png'
    }
  }
}
</script>
```

### 15.4 数据不更新

**问题**：修改数据后页面不更新。

**解决方案**：

- 使用 `this.$set` 更新对象属性
- 使用数组变异方法
- 使用 `this.$forceUpdate()` 强制更新

```javascript
// 避免：直接修改对象属性
this.userInfo.name = '新名称'  // 可能不会触发更新

// 推荐：使用 $set
this.$set(this.userInfo, 'name', '新名称')

// 避免：直接修改数组索引
this.list[0] = newItem  // 可能不会触发更新

// 推荐：使用数组方法
this.list.splice(0, 1, newItem)
// 或
this.$set(this.list, 0, newItem)
```

### 15.5 页面白屏

**问题**：页面加载后显示白屏。

**排查步骤**：

1. 检查控制台是否有错误
2. 检查 pages.json 路由配置
3. 检查页面文件是否存在
4. 检查网络请求是否正常

**解决方案**：

```javascript
// 添加错误边界
export default {
  onLoad() {
    try {
      this.init()
    } catch (error) {
      console.error('页面初始化失败:', error)
      uni.showToast({
        title: '页面加载失败',
        icon: 'none'
      })
    }
  }
}
```


**本规范遵循 KISS、YAGNI、SOLID 原则，旨在提供简洁、实用、可维护的 uni-app 开发指南。**
