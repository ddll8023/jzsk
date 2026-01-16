/**
 * 静态菜单数据
 * 硬编码菜单树结构，无需后端接口
 */

let idCounter = 100

const generateId = () => idCounter++

export const staticMenuData = [
  {
    id: generateId(),
    name: '一张图',
    code: 'map',
    path: '',
    icon: 'fa fa-map',
    ordernum: 1,
    status: '启用',
    subsystemid: 0,
    children: []
  },
  {
    id: generateId(),
    name: '水雨情管理',
    code: 'water_rain',
    path: '',
    icon: 'fa fa-tint',
    ordernum: 2,
    status: '启用',
    subsystemid: 0,
    children: [
      { id: generateId(), name: '降雨查询', code: 'water_rain_rainfall', path: 'hydrology/rainfall', ordernum: 1, status: '启用', subsystemid: 101, children: [] },
      { id: generateId(), name: '水库水位查询', code: 'water_rain_reservoir', path: 'hydrology/waterstorage', ordernum: 2, status: '启用', subsystemid: 101, children: [] }
    ]
  },
  {
    id: generateId(),
    name: '视频监测',
    code: 'video',
    path: '',
    icon: 'fa fa-video-camera',
    ordernum: 3,
    status: '启用',
    subsystemid: 0,
    children: [
      { id: generateId(), name: '视频实时监测', code: 'video_monitor', path: '/home/video/monitor', ordernum: 1, status: '启用', subsystemid: 103, children: [] }
    ]
  },
  {
    id: generateId(),
    name: '大坝安全监测',
    code: 'dam_safety',
    path: '',
    icon: 'fa fa-shield',
    ordernum: 4,
    status: '启用',
    subsystemid: 0,
    children: [
      { id: generateId(), name: '渗流压力监测分析', code: 'dam_seepage_pressure', path: '/home/dam/seepage', ordernum: 1, status: '启用', subsystemid: 104, children: [] },
      { id: generateId(), name: '渗流量监测分析', code: 'dam_seepage_flow', path: '/home/dam/seepage-flow', ordernum: 2, status: '启用', subsystemid: 104, children: [] },
      { id: generateId(), name: '地表位移', code: 'dam_displacement', path: '/home/dam/surface-displacement', ordernum: 3, status: '启用', subsystemid: 104, children: [] }
    ]
  },
  {
    id: generateId(),
    name: '闸门监控管理',
    code: 'gate',
    path: '',
    icon: 'fa fa-columns',
    ordernum: 5,
    status: '启用',
    subsystemid: 0,
    children: [
      { id: generateId(), name: '闸门实时状态查询', code: 'gate_realtime', path: '/gate/realtime', ordernum: 1, status: '启用', subsystemid: 108, children: [] },
      { id: generateId(), name: '闸门报表', code: 'gate_report', path: '/gate/report', ordernum: 2, status: '启用', subsystemid: 108, children: [] }
    ]
  },
  {
    id: generateId(),
    name: '预警管理',
    code: 'warning',
    path: '',
    icon: 'fa fa-bell',
    ordernum: 6,
    status: '启用',
    subsystemid: 0,
    children: [
      { id: generateId(), name: '预警信息处理', code: 'warning_process', path: '/warning/process', ordernum: 1, status: '启用', subsystemid: 111, children: [] },
      { id: generateId(), name: '预警指标设定', code: 'warning_config', path: '/warning/config', ordernum: 2, status: '启用', subsystemid: 111, children: [] }
    ]
  },
  {
    id: generateId(),
    name: '综合报表',
    code: 'report',
    path: '',
    icon: 'fa fa-file-text',
    ordernum: 7,
    status: '启用',
    subsystemid: 0,
    children: [
      { id: generateId(), name: '值班安排', code: 'report_duty', path: '/report/duty', ordernum: 1, status: '启用', subsystemid: 114, children: [] },
      { id: generateId(), name: '值班日志', code: 'report_log', path: '/report/log', ordernum: 2, status: '启用', subsystemid: 114, children: [] }
    ]
  },
  {
    id: generateId(),
    name: '现场检查',
    code: 'inspection',
    path: '',
    icon: 'fa fa-clipboard-check',
    ordernum: 8,
    status: '启用',
    subsystemid: 0,
    children: [
      { id: generateId(), name: '巡检记录', code: 'inspection_patrol', path: '/inspection/patrol', ordernum: 1, status: '启用', subsystemid: 117, children: [] },
      { id: generateId(), name: '维护记录', code: 'inspection_maintain', path: '/inspection/maintain', ordernum: 2, status: '启用', subsystemid: 117, children: [] }
    ]
  },
  {
    id: generateId(),
    name: '基础信息查询',
    code: 'basic_info',
    path: '',
    icon: 'fa fa-database',
    ordernum: 9,
    status: '启用',
    subsystemid: 0,
    children: [
      { id: generateId(), name: '监测站点', code: 'basic_station', path: '/basic-info/station', ordernum: 1, status: '启用', subsystemid: 120, children: [] },
      { id: generateId(), name: '测项信息', code: 'basic_measure', path: '/basic-info/measure', ordernum: 2, status: '启用', subsystemid: 120, children: [] },
      { id: generateId(), name: '洪水防御预案', code: 'basic_flood_plan', path: '/basic-info/flood-plan', ordernum: 3, status: '启用', subsystemid: 120, children: [] },
      { id: generateId(), name: '预警设施', code: 'basic_warning_facility', path: '/basic-info/warning-facility', ordernum: 4, status: '启用', subsystemid: 120, children: [] },
      { id: generateId(), name: '库区基本情况', code: 'basic_reservoir', path: '/basic-info/reservoir', ordernum: 5, status: '启用', subsystemid: 120, children: [] }
    ]
  },
  {
    id: generateId(),
    name: '资源下载',
    code: 'download',
    path: '',
    icon: 'fa fa-download',
    ordernum: 10,
    status: '启用',
    subsystemid: 0,
    children: [
      { id: generateId(), name: '巡检app安装', code: 'download_app', path: '/download/app', ordernum: 1, status: '启用', subsystemid: 126, children: [] }
    ]
  },
  {
    id: generateId(),
    name: '系统管理',
    code: 'system',
    path: '',
    icon: 'fa fa-cog',
    ordernum: 11,
    status: '启用',
    subsystemid: 0,
    children: [
      { id: generateId(), name: '角色管理', code: 'system_role', path: '/home/role', ordernum: 1, status: '启用', subsystemid: 128, children: [] },
      { id: generateId(), name: '机构信息', code: 'system_org', path: '/home/org', ordernum: 2, status: '启用', subsystemid: 128, children: [] },
      { id: generateId(), name: '用户管理', code: 'system_user', path: '/home/user', ordernum: 3, status: '启用', subsystemid: 128, children: [] },
      { id: generateId(), name: '人员信息', code: 'system_person', path: '/home/person', ordernum: 4, status: '启用', subsystemid: 128, children: [] },
      { id: generateId(), name: '部门管理', code: 'system_dept', path: '/hoem/dept', ordernum: 5, status: '启用', subsystemid: 128, children: [] },
      { id: generateId(), name: '字典管理', code: 'system_dict', path: '/home/dictmanage', ordernum: 6, status: '启用', subsystemid: 128, children: [] },
      { id: generateId(), name: '菜单管理', code: 'system_menu', path: '/home/menumanage', ordernum: 7, status: '启用', subsystemid: 128, children: [] }
    ]
  }
]
