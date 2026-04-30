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
      {
        path: 'usermanage',
        name: 'UserManage',
        component: () => import('@/views/system/UserManage.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'personmanage',
        name: 'PersonManage',
        component: () => import('@/views/system/PersonManage.vue'),
        meta: { title: '人员管理' }
      },
      {
        path: 'deptmanage',
        name: 'DeptManage',
        component: () => import('@/views/system/DeptManage.vue'),
        meta: { title: '部门管理' }
      },
      {
        path: 'orgmanage',
        name: 'OrgManage',
        component: () => import('@/views/system/OrgManage.vue'),
        meta: { title: '机构信息' }
      },
      {
        path: 'rolemanage',
        name: 'RoleManage',
        component: () => import('@/views/system/RoleManage.vue'),
        meta: { title: '角色管理' }
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
      // 设备监控
      {
        path: 'monitor/device',
        name: 'DeviceMonitor',
        component: () => import('@/views/monitor/DeviceMonitor.vue'),
        meta: { title: '设备监控' }
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
      },
      // 闸门监控管理
      {
        path: 'gate/status',
        name: 'GateStatus',
        component: () => import('@/views/gate/GateStatus.vue'),
        meta: { title: '闸门实时状态' }
      },
      {
        path: 'gate/report',
        name: 'GateReport',
        component: () => import('@/views/gate/GateReport.vue'),
        meta: { title: '闸门报表' }
      },
      // 预警管理
      {
        path: 'warning/information',
        name: 'PrewarningInformation',
        component: () => import('@/views/warning/PrewarningInformation.vue'),
        meta: { title: '预警信息处理' }
      },
      {
        path: 'warning/indicator',
        name: 'PrewarningIndicatorSetting',
        component: () => import('@/views/warning/PrewarningIndicatorSetting.vue'),
        meta: { title: '预警指标设定' }
      },
      // 综合报表
      {
        path: 'report/duty-schedule',
        name: 'DutySchedule',
        component: () => import('@/views/report/DutySchedule.vue'),
        meta: { title: '值班安排' }
      },
      {
        path: 'report/duty-log',
        name: 'DutyLog',
        component: () => import('@/views/report/DutyLog.vue'),
        meta: { title: '值班日志' }
      },
      // 工程巡检
      {
        path: 'inspection/polling',
        name: 'PollingRecord',
        component: () => import('@/views/inspection/PollingRecord.vue'),
        meta: { title: '巡检记录' }
      },
      {
        path: 'inspection/maintenance',
        name: 'MaintenanceRecord',
        component: () => import('@/views/inspection/MaintenanceRecord.vue'),
        meta: { title: '维护记录' }
      },
      // 基础工情查询
      {
        path: 'engineering/monitor-site',
        name: 'MonitorSite',
        component: () => import('@/views/engineering/MonitorSite.vue'),
        meta: { title: '监测站点管理' }
      },
      {
        path: 'engineering/monitor-item',
        name: 'MonitorItem',
        component: () => import('@/views/engineering/MonitorItem.vue'),
        meta: { title: '测项信息管理' }
      },
      {
        path: 'basic-info/flood-plan',
        name: 'FloodPlan',
        component: () => import('@/views/basic-info/FloodPlan.vue'),
        meta: { title: '洪水防御预案' }
      },
      {
        path: 'basic-info/warning-facility',
        name: 'WarningFacility',
        component: () => import('@/views/basic-info/WarningFacility.vue'),
        meta: { title: '预警设施' }
      },
      {
        path: 'basic-info/reservoir',
        name: 'Reservoir',
        component: () => import('@/views/basic-info/Reservoir.vue'),
        meta: { title: '库区基本情况' }
      },
      // 巡检APP下载
      {
        path: 'app-download',
        name: 'AppDownload',
        component: () => import('@/views/AppDownload.vue'),
        meta: { title: '巡检APP下载' }
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
