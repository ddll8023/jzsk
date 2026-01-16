/**
 * 路由配置
 * 功能：Vue Router 4.x 路由定义、导航守卫
 * 参考：frontend/src/router/index.js
 */
import { createRouter, createWebHistory } from 'vue-router'

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    redirect: '/home/onemap'
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    redirect: '/home/onemap',
    children: [
      {
        path: 'onemap',
        name: 'OneMap',
        component: () => import('@/views/OneMap.vue'),
        meta: { title: '一张图' }
      },
      // 系统管理
      {
        path: 'dictmanage',
        name: 'DictManage',
        component: () => import('@/views/system/DictManage.vue'),
        meta: { title: '字典管理' }
      },
      {
        path: 'menumanage',
        name: 'MenuManage',
        component: () => import('@/views/system/MenuManage.vue'),
        meta: { title: '菜单管理' }
      },
      // 水雨情管理
      {
        path: 'hydrology/rainfall',
        name: 'RainfallQuery',
        component: () => import('@/views/hydrology/RainfallQuery.vue'),
        meta: { title: '降雨查询' }
      },
      {
        path: 'hydrology/waterstorage',
        name: 'WaterStorage',
        component: () => import('@/views/hydrology/WaterStorage.vue'),
        meta: { title: '水库水位查询' }
      },
      // 视频监控
      {
        path: 'video/monitor',
        name: 'VideoMonitor',
        component: () => import('@/views/video/VideoMonitor.vue'),
        meta: { title: '视频实时监测' }
      },
      // 大坝安全监测
      {
        path: 'dam/seepage',
        name: 'DamSeepage',
        component: () => import('@/views/dam/Seepage.vue'),
        meta: { title: '渗流压力监测' }
      },
      {
        path: 'dam/seepage-flow',
        name: 'SeepageFlowMonitor',
        component: () => import('@/views/dam/SeepageFlowMonitor.vue'),
        meta: { title: '渗流量监测分析' }
      },
      {
        path: 'dam/surface-displacement',
        name: 'SurfaceDisplacement',
        component: () => import('@/views/dam/SurfaceDisplacement.vue'),
        meta: { title: '地表位移监测' }
      }
      // 其他子路由按需添加
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '404' }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 智慧水利`
  }

  // 白名单路由直接放行（登录页无需验证）
  if (to.path === '/login') {
    return next()
  }

  // 检查 token - 未登录则重定向到登录页
  const token = sessionStorage.getItem('token')
  if (!token) {
    return next('/login')
  }

  next()
})

export default router
