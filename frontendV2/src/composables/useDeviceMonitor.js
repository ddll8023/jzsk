/**
 * 设备监控 Composable
 * 功能：分类型获取设备到报数据、管理独立的 loading/error 状态、筛选逻辑
 * 遵循原则：KISS、YAGNI
 */
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { getGnssStatus, getRainStatus, getSeepageStatus } from '@/api/deviceMonitor'

/**
 * 设备到报状态枚举
 */
export const DEVICE_STATUS = {
  ONLINE: 'online',
  OFFLINE: 'offline',
  ABNORMAL: 'abnormal'
}

/**
 * 设备类型枚举
 */
export const DEVICE_TYPES = {
  gnss: { label: 'GNSS 地表位移', icon: 'fa-satellite', color: 'blue' },
  rain: { label: '水雨情', icon: 'fa-cloud-rain', color: 'cyan' },
  seepage: { label: '渗流渗压', icon: 'fa-water', color: 'teal' }
}

/**
 * 状态显示配置
 */
export const STATUS_CONFIG = {
  [DEVICE_STATUS.ONLINE]: { label: '已到报', color: 'emerald', dotClass: 'bg-emerald-500' },
  [DEVICE_STATUS.OFFLINE]: { label: '未到报', color: 'amber', dotClass: 'bg-amber-500' },
  [DEVICE_STATUS.ABNORMAL]: { label: '采集异常', color: 'red', dotClass: 'bg-red-500' }
}

/**
 * 渗流渗压设备 pointId → 可读名称映射
 * Source: useStationMarkers.js SEEPAGE_STATIONS
 */
const SEEPAGE_NAME_MAP = {
  '1130221274157547520': 'UPR1-1',
  '1130221285905793024': 'UPB1-1',
  '1130221296655794176': 'UPB2-1',
  '1130221308043329536': 'UPA1-1',
  '1130221319053377536': 'UPB3-1',
  '1130221331892142080': 'UPB4-1',
  '1130221343288066048': 'UPB4-4',
  '1130221354100981760': 'UPB4-2',
  '1130221364981006336': 'UPB4-3',
  '1130221376058163200': 'UPB4-5',
  '1130221386883661824': 'UPB3-2',
  '1130221397532999680': 'UPB3-4',
  '1130221408509493248': 'UPB3-3',
  '1130221419490181120': 'UPB2-2',
  '1130221430265348096': 'UPA1-2',
  '1130221441057292288': 'UPB2-3',
  '1130221451794710528': 'UPA1-3',
  '1130221462834118656': 'UPB2-4',
  '1130221474066464768': 'UPA1-4',
  '1130221485206536192': 'UPB2-5',
  '1130221496413716480': 'UPA1-5',
  '1130221507100803072': 'UPB1-5',
  '1130221518182154240': 'UPB1-4',
  '1130221528902795264': 'UPB1-3',
  '1130221539753459712': 'UPB1-2',
  '1130221562159431680': 'UPR1-2',
  'P0108206': 'UPR2-1',
  'P0108311': 'UPR2-2',
  '1130221574088032256': 'UPB3-5'
}

/**
 * 创建单类型的响应式状态
 */
function createTypeState() {
  return {
    loading: ref(false),
    error: ref(null),
    stats: ref(null),
    devices: ref([])
  }
}

export function useDeviceMonitor() {
  const gnss = createTypeState()
  const rain = createTypeState()
  const seepage = createTypeState()

  const activeType = ref(null)
  const activeStatus = ref(null)

  let refreshTimer = null

  /**
   * 整体 loading：任意一路正在首次加载
   */
  const loading = computed(() =>
    gnss.loading.value || rain.loading.value || seepage.loading.value
  )

  /**
   * 整体 error：仅在所有数据都为空时显示
   */
  const error = computed(() => {
    const hasData = gnss.stats.value || rain.stats.value || seepage.stats.value
    if (hasData) return null
    return gnss.error.value || rain.error.value || seepage.error.value
  })

  /**
   * overview：三路统计汇总
   */
  const overview = computed(() => {
    if (!gnss.stats.value && !rain.stats.value && !seepage.stats.value) return null
    return {
      gnss: gnss.stats.value || { total: 0, online: 0, offline: 0, abnormal: 0 },
      rain: rain.stats.value || { total: 0, online: 0, offline: 0, abnormal: 0 },
      seepage: seepage.stats.value || { total: 0, online: 0, offline: 0, abnormal: 0 }
    }
  })

  /**
   * 所有设备合并列表
   */
  const allDevices = computed(() => [
    ...gnss.devices.value,
    ...rain.devices.value,
    ...seepage.devices.value
  ])

  /**
   * 按类型和状态筛选后的设备列表，按最后采集时间倒序
   */
  const filteredDevices = computed(() => {
    const filtered = allDevices.value.filter(d => {
      if (activeType.value && d.type !== activeType.value) return false
      if (activeStatus.value && d.status !== activeStatus.value) return false
      return true
    })
    return [...filtered].sort((a, b) => {
      const timeA = a.lastCollectTime ? new Date(a.lastCollectTime).getTime() : 0
      const timeB = b.lastCollectTime ? new Date(b.lastCollectTime).getTime() : 0
      return timeB - timeA
    })
  })

  /**
   * 设备列表是否正在首次加载（无任何数据时）
   */
  const tableLoading = computed(() => {
    return loading.value && !overview.value
  })

  /**
   * 获取单类型数据
   * typeKey 用于渗压设备的名称转换
   */
  async function fetchType(typeState, apiFn, typeKey) {
    typeState.loading.value = true
    typeState.error.value = null
    try {
      const res = await apiFn()
      const data = res.data.data
      typeState.stats.value = data.stats
      // 渗压设备名称转换
      if (typeKey === 'seepage') {
        typeState.devices.value = data.devices.map(d => ({
          ...d,
          name: SEEPAGE_NAME_MAP[d.name] || d.name
        }))
      } else {
        typeState.devices.value = data.devices
      }
    } catch (e) {
      typeState.error.value = e.message || '获取数据失败'
      console.error(`[DeviceMonitor] 数据加载失败:`, e)
    } finally {
      typeState.loading.value = false
    }
  }

  /**
   * 并发获取所有类型数据
   */
  const fetchData = () => {
    return Promise.all([
      fetchType(gnss, getGnssStatus, 'gnss'),
      fetchType(rain, getRainStatus, 'rain'),
      fetchType(seepage, getSeepageStatus, 'seepage')
    ])
  }

  const startAutoRefresh = (interval = 60000) => {
    stopAutoRefresh()
    refreshTimer = setInterval(fetchData, interval)
  }

  const stopAutoRefresh = () => {
    if (refreshTimer) {
      clearInterval(refreshTimer)
      refreshTimer = null
    }
  }

  const setTypeFilter = (type) => {
    activeType.value = activeType.value === type ? null : type
  }

  const setStatusFilter = (status) => {
    activeStatus.value = activeStatus.value === status ? null : status
  }

  onMounted(() => {
    fetchData()
    startAutoRefresh()
  })

  onUnmounted(() => {
    stopAutoRefresh()
  })

  return {
    loading,
    error,
    overview,
    filteredDevices,
    tableLoading,
    activeType,
    activeStatus,
    gnss,
    rain,
    seepage,
    setTypeFilter,
    setStatusFilter,
    fetchData
  }
}
