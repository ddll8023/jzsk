# uni-app 开发规范

> 本文档为 uni-app 端开发规范，与 `前端规范文档.md` 互为补充。通用规范（Props/Emits 定义、命名规则、注释规范、代码质量等）以前端规范为准，本文档只描述 uni-app 特有内容。

## 1. 技术栈

| 技术                        | 版本   | 说明                              |
| --------------------------- | ------ | --------------------------------- |
| uni-app                     | -      | 跨平台应用开发框架                |
| Vue 3                       | ^3.4.0 | Composition API +`<script setup>` |
| Pinia                       | ^2.1.0 | 状态管理                          |
| pinia-plugin-persistedstate | ^3.2.0 | 状态持久化                        |
| Tailwind CSS                | ^3.4.0 | 原子化 CSS                        |
| weapp-tailwindcss           | ^3.4.0 | 小程序 Tailwind 适配              |

- 目标平台：**APP-PLUS**（Android，主要）、**H5**（开发调试）
- 网络请求：使用 `uni.request`，封装在 `utils/request.js`
- 样式：统一使用 Tailwind 原子类名，禁止 BEM 命名，禁止 Element Plus 等组件库

## 2. 项目结构

```
project-root/
├── services/                 # API 接口模块（用 services 非 api，避免与代理冲突）
├── components/
│   ├── common/               # 公共组件
│   └── business/             # 业务组件
├── composables/              # 组合式函数
├── config/                   # 配置文件（API 地址、常量）
├── pages/                    # 页面目录，按功能分组
├── static/                   # 静态资源
├── stores/
│   ├── modules/              # 状态模块
│   └── index.js              # Store 入口
├── utils/                    # 工具函数（request、storage、validate）
├── App.vue                   # 应用入口
├── main.js                   # 主入口
├── manifest.json             # 应用配置
├── pages.json                # 页面路由配置
├── uni.scss                  # 全局样式变量
└── tailwind.config.js        # Tailwind 配置
```

## 3. 命名规范（uni-app 特有）

| 类型     | 规则       | 示例               |
| -------- | ---------- | ------------------ |
| 页面文件 | kebab-case | `user-profile.vue` |
| 组件文件 | PascalCase | `UserCard.vue`     |
| API 模块 | camelCase  | `seepage.js`       |
| 静态资源 | kebab-case | `logo-icon.png`    |

其他命名（变量、常量、函数、事件等）参见前端规范。

## 4. pages.json 路由配置

### 4.1 配置结构

```json
{
  "pages": [
    {
      "path": "pages/login/login",
      "style": { "navigationBarTitleText": "登录", "navigationStyle": "custom" }
    },
    {
      "path": "pages/tabbar/index/index",
      "style": {
        "navigationBarTitleText": "首页",
        "navigationBarBackgroundColor": "#00b783",
        "navigationBarTextStyle": "white"
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
    "list": []
  }
}
```

### 4.2 配置原则

- 页面顺序：登录页 → tabBar 页 → 业务模块页
- 路径简洁，避免过深嵌套
- tabBar：最少 2 个、最多 5 个，图标建议 81×81px

## 5. API 接口规范

### 5.1 请求封装核心结构（utils/request.js）

```javascript
import config from "@/config/index.js";

export function request(options) {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync("token");
    uni.request({
      url: `${config.baseUrl}${options.url}`,
      method: options.method || "GET",
      data: options.data || {},
      header: {
        "Content-Type": "application/json",
        Authorization: token ? `Bearer ${token}` : "",
        ...options.header,
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 0 || res.data.code === 200) {
            resolve(res.data);
          } else {
            uni.showToast({
              title: res.data.message || "请求失败",
              icon: "none",
            });
            reject(new Error(res.data.message));
          }
        } else if (res.statusCode === 401) {
          uni.removeStorageSync("token");
          uni.reLaunch({ url: "/pages/login/login" });
          reject(new Error("未授权"));
        } else {
          reject(new Error(`请求失败: ${res.statusCode}`));
        }
      },
      fail: (err) => reject(err),
    });
  });
}

export const get = (url, params = {}, header = {}) =>
  request({ url, method: "GET", data: params, header });
export const post = (url, data = {}, header = {}) =>
  request({ url, method: "POST", data, header });
export const put = (url, data = {}, header = {}) =>
  request({ url, method: "PUT", data, header });
export const del = (url, data = {}, header = {}) =>
  request({ url, method: "DELETE", data, header });
```

### 5.2 API 模块定义（services/xxx.js）

```javascript
import { get } from "@/utils/request.js";

/** 获取渗流数据列表 */
export function getSeepageList(params = {}) {
  return get("/data-new/list", params);
}

/** 获取渗流数据分页 */
export function getSeepagePage(params = {}) {
  return get("/data-new/page", { current: 1, size: 10, ...params });
}
```

### 5.3 配置文件（config/index.js）

```javascript
const devBaseUrl = "http://192.168.1.100:8081";
const prodBaseUrl = "http://111.4.68.108:8081";

export default {
  baseUrl: process.env.NODE_ENV === "development" ? devBaseUrl : prodBaseUrl,
  timeout: 15000,
  pageSize: 10,
  maxImageSize: 5,
};
```

### 5.4 页面中调用

```vue
<script setup>
import { ref } from "vue";
import { onLoad } from "@dcloudio/uni-app";
import { getSeepageList } from "@/services/seepage.js";

const dataList = ref([]);
const loading = ref(false);

onLoad(() => loadData());

const loadData = async () => {
  loading.value = true;
  try {
    const res = await getSeepageList({
      startTime: "2024-01-01 00:00:00",
      endTime: "2024-12-31 23:59:59",
    });
    dataList.value = res.data || [];
  } finally {
    loading.value = false;
  }
};
</script>
```

## 6. 组件开发规范

### 6.1 组件分类

| 分类     | 目录                   | 说明               |
| -------- | ---------------------- | ------------------ |
| 公共组件 | `components/common/`   | 高复用、无业务逻辑 |
| 业务组件 | `components/business/` | 绑定特定业务场景   |

### 6.2 组件模板（使用 view/text 替代 div/span）

```vue
<template>
  <view class="bg-white rounded-lg p-5 shadow-sm">
    <view class="flex items-center justify-between mb-5">
      <text class="text-lg font-bold text-gray-900">{{ title }}</text>
    </view>
    <slot></slot>
  </view>
</template>

<script setup>
/**
 * 组件名称
 * 功能：组件功能描述
 */
defineProps({
  title: { type: String, required: true },
  bordered: { type: Boolean, default: true },
});
const emit = defineEmits(["click"]);
</script>
```

> **关键差异**：uni-app 模板使用 `<view>`、`<text>`、`<image>` 替代 HTML 标签。

### 6.3 导入顺序

```vue
<script setup>
// 1. Vue 官方 API + uni-app 生命周期
import { ref, computed } from "vue";
import { onLoad, onShow } from "@dcloudio/uni-app";

// 2. Pinia Store
import { useUserStore } from "@/stores/modules/user";

// 3. 工具函数 / 常量
import { formatDate } from "@/utils/format.js";

// 4. API 接口
import { getDataList } from "@/services/data.js";

// 5. 子组件
import SkCard from "@/components/common/SkCard.vue";
</script>
```

## 7. 页面开发规范

### 7.1 常用页面生命周期

```vue
<script setup>
import {
  onLoad,
  onShow,
  onPullDownRefresh,
  onReachBottom,
} from "@dcloudio/uni-app";

// 页面加载（仅一次）：接收参数、初始化数据
onLoad((options) => {
  console.log("页面参数", options);
});

// 页面显示（每次触发）：刷新数据
onShow(() => {
  console.log("页面显示");
});

// 下拉刷新（需 pages.json 配置 enablePullDownRefresh: true）
onPullDownRefresh(() => {
  loadData().then(() => uni.stopPullDownRefresh());
});

// 上拉加载更多
onReachBottom(() => loadMore());
</script>
```

### 7.2 页面导航

```javascript
uni.navigateTo({ url: "/pages/detail/detail?id=123" }); // 保留当前页
uni.redirectTo({ url: "/pages/detail/detail" }); // 关闭当前页
uni.reLaunch({ url: "/pages/login/login" }); // 关闭所有页
uni.navigateBack({ delta: 1 }); // 返回
uni.switchTab({ url: "/pages/tabbar/index/index" }); // 跳转 tabBar
```

### 7.3 页面模板结构

```vue
<template>
  <view class="min-h-screen bg-gray-50">
    <view class="p-5">
      <!-- 加载状态 -->
      <view
        v-if="loading"
        class="flex flex-col items-center justify-center py-25"
      >
        <view
          class="w-10 h-10 border-4 border-primary border-t-transparent rounded-full animate-spin"
        ></view>
        <text class="text-base text-gray-500 mt-5">加载中...</text>
      </view>

      <!-- 空数据 -->
      <view
        v-else-if="dataList.length === 0"
        class="flex flex-col items-center justify-center py-25"
      >
        <text class="text-base text-gray-500">暂无数据</text>
      </view>

      <!-- 数据列表 -->
      <view v-else class="space-y-3">
        <view
          v-for="item in dataList"
          :key="item.id"
          class="p-6 bg-white rounded-lg shadow-sm"
          @click="handleItemClick(item)"
        >
          <text class="text-base font-bold text-gray-900 block mb-2">{{
            item.title
          }}</text>
        </view>
      </view>
    </view>
  </view>
</template>
```

## 8. Tailwind CSS 配置

### 8.1 安装与配置

```bash
npm i -D tailwindcss@3 postcss autoprefixer weapp-tailwindcss
npx tailwindcss init
```

tailwind.config.js：

```javascript
const path = require("path");
const resolve = (p) => path.resolve(__dirname, p);

module.exports = {
  content: [
    "./pages/**/*.{vue,js}",
    "./components/**/*.{vue,js}",
    "./App.vue",
  ].map(resolve),
  theme: {
    extend: {
      colors: {
        primary: "#00b783",
        success: "#4cd964",
        warning: "#f0ad4e",
        error: "#dd524d",
      },
      spacing: {
        "safe-top": "var(--status-bar-height)",
        "safe-bottom": "var(--window-bottom)",
      },
    },
  },
  corePlugins: { preflight: false }, // uni-app 要求关闭
};
```

App.vue 引入：

```vue
<style>
@tailwind base;
@tailwind components;
@tailwind utilities;

page {
  background-color: #f8f8f8;
  font-size: 28rpx;
  color: #333333;
}
</style>
```

### 8.2 常用间距

| 类名        | px   | 使用场景     |
| ----------- | ---- | ------------ |
| `p-4`       | 16px | 卡片内边距   |
| `p-5`       | 20px | 页面边距     |
| `p-6`       | 24px | 大内边距     |
| `mb-3`      | 12px | 列表项间距   |
| `space-y-4` | 16px | 表单元素间距 |

### 8.3 颜色系统

- 文字：`text-gray-900`（深）、`text-gray-500`（次）、`text-gray-400`（占位）
- 背景：`bg-white`、`bg-gray-50`、`bg-primary`
- 边框：`border-gray-200`、`border-primary`

### 8.4 仅在以下场景使用原生 CSS

```vue
<!-- 伪元素 -->
<style scoped>
.custom-element::after {
  content: "";
  display: block;
  height: 2rpx;
  background: #e5e5e5;
}
</style>

<!-- 复杂动画 -->
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

## 9. 状态管理（Pinia）

### 9.1 Store 入口（stores/index.js）

```javascript
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
export default pinia;
```

### 9.2 模块定义（Setup 语法 + uni-app 持久化）

```javascript
import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { post, get } from "@/utils/request.js";

export const useUserStore = defineStore(
  "user",
  () => {
    const userInfo = ref(null);
    const token = ref(uni.getStorageSync("token") || "");

    const isLoggedIn = computed(() => !!token.value);

    async function login(credentials) {
      const res = await post("/auth/login", credentials);
      token.value = res.data.token;
      userInfo.value = res.data.userInfo;
      uni.setStorageSync("token", res.data.token);
      return res;
    }

    function logout() {
      userInfo.value = null;
      token.value = "";
      uni.removeStorageSync("token");
      uni.reLaunch({ url: "/pages/login/login" });
    }

    return { userInfo, token, isLoggedIn, login, logout };
  },
  {
    persist: {
      key: "user",
      storage: {
        getItem: (key) => uni.getStorageSync(key),
        setItem: (key, value) => uni.setStorageSync(key, value),
      },
    },
  },
);
```

### 9.3 页面中使用

```vue
<script setup>
import { useUserStore } from "@/stores/modules/user";
import { onLoad } from "@dcloudio/uni-app";

const userStore = useUserStore();
const { userName, isLoggedIn } = userStore;

onLoad(() => {
  if (isLoggedIn.value) userStore.getUserInfo();
});
</script>
```

## 10. 本地存储

### 10.1 工具封装（utils/storage.js）接口

```javascript
export function setStorage(key, value)          // 存储数据
export function getStorage(key, defaultValue)    // 获取数据
export function removeStorage(key)               // 移除数据
export function clearStorage()                   // 清空数据
export function setObject(key, obj)              // 存储对象（自动序列化）
export function getObject(key, defaultValue)     // 获取对象（自动反序列化）
```

### 10.2 存储键常量

```javascript
// constants/storage.js
export const STORAGE_KEYS = {
  TOKEN: "token",
  USER_INFO: "userInfo",
  THEME: "theme",
};

// 使用
import { STORAGE_KEYS } from "@/constants/storage.js";
setStorage(STORAGE_KEYS.TOKEN, "xxx");
```

## 11. 条件编译

> 仅针对 **APP-PLUS** 和 **H5** 两个平台。

```vue
<!-- 模板 -->
<!-- #ifdef H5 -->
<view>仅在 H5 显示</view>
<!-- #endif -->

<!-- #ifdef APP-PLUS -->
<view>仅在 App 显示</view>
<!-- #endif -->
```

```javascript
// 脚本
// #ifdef APP-PLUS
plus.share.sendWithSystem({ type: "text", content: "分享内容" });
// #endif
```

```css
/* 样式 */
/* #ifdef APP-PLUS */
.app-style {
  padding: 20rpx;
}
/* #endif */
```

优先使用 uni-app 统一 API，避免过度条件编译。

## 12. 性能优化

### 12.1 列表分页加载

```vue
<template>
  <scroll-view scroll-y class="h-full" @scrolltolower="loadMore">
    <view
      v-for="item in dataList"
      :key="item.id"
      class="p-4 bg-white rounded-lg mb-3"
    >
      <text class="text-base text-gray-900">{{ item.title }}</text>
    </view>
    <view v-if="!hasMore && dataList.length > 0" class="text-center py-4">
      <text class="text-sm text-gray-400">没有更多数据了</text>
    </view>
  </scroll-view>
</template>

<script setup>
const dataList = ref([]);
const hasMore = ref(true);
const page = ref(1);

const loadMore = async () => {
  if (loading.value || !hasMore.value) return;
  loading.value = true;
  try {
    const res = await getDataPage({ current: page.value, size: 20 });
    dataList.value = [...dataList.value, ...(res.data?.records || [])];
    page.value++;
    if (res.data.records.length < 20) hasMore.value = false;
  } finally {
    loading.value = false;
  }
};
</script>
```

### 12.2 分包加载

pages.json 配置：

```json
{
  "subPackages": [
    {
      "root": "modules/inspection",
      "pages": [
        {
          "path": "list/list",
          "style": { "navigationBarTitleText": "巡检列表" }
        }
      ]
    },
    {
      "root": "modules/maintenance",
      "pages": [
        {
          "path": "list/list",
          "style": { "navigationBarTitleText": "维护列表" }
        }
      ]
    }
  ]
}
```

### 12.3 其他优化要点

- 图片使用 `lazy-load`，设置 `@error` 兜底
- 大数组使用 `shallowRef`
- 搜索输入使用防抖，滚动事件使用节流

## 13. 错误处理

### 13.1 全局错误处理

```javascript
// main.js
export function createApp() {
  const app = createSSRApp(App);
  app.config.errorHandler = (err, instance, info) => {
    console.error("全局错误:", err, info);
    uni.showToast({ title: "系统异常，请稍后重试", icon: "none" });
  };
  return { app };
}

uni.onUnhandledRejection((event) => {
  console.error("未捕获的 Promise 错误:", event.reason);
});
```

### 13.2 页面级错误处理

```javascript
const loadData = async () => {
  loading.value = true;
  try {
    const res = await getDataList();
    dataList.value = res.data || [];
  } catch (error) {
    uni.showToast({ title: error.message || "加载失败", icon: "none" });
  } finally {
    loading.value = false;
  }
};
```

---

**本规范遵循 KISS、YAGNI 原则，旨在提供简洁、实用的 uni-app 开发指南。通用规范参见 `前端规范文档.md`。**
