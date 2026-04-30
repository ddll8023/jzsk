/**
 * 设备监控 Composable
 * 功能：获取设备监控数据、计算统计信息、筛选逻辑
 * 遵循原则：KISS、YAGNI
 */
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { getDeviceMonitorStatus } from '@/api/deviceMonitor'

/**
 * 设备状态枚举
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
  rain: { label: '雨水情', icon: 'fa-cloud-rain', color: 'cyan' },
  seepage: { label: '渗流渗压', icon: 'fa-water', color: 'teal' }
}

/**
 * 状态显示配置
 */
export const STATUS_CONFIG = {
  [DEVICE_STATUS.ONLINE]: { label: '在线', color: 'emerald', dotClass: 'bg-emerald-500' },
  [DEVICE_STATUS.OFFLINE]: { label: '离线', color: 'red', dotClass: 'bg-red-500' },
  [DEVICE_STATUS.ABNORMAL]: { label: '采集异常', color: 'amber', dotClass: 'bg-amber-500' }
}

export function useDeviceMonitor() {
  const loading = ref(false)
  const error = ref(null)
  const overview = ref(null)
  const devices = ref([])
  const activeType = ref(null)
  const activeStatus = ref(null)

  let refreshTimer = null

  const filteredDevices = computed(() => {
    return devices.value.filter(d => {
      if (activeType.value && d.type !== activeType.value) return false
      if (activeStatus.value && d.status !== activeStatus.value) return false
      return true
    })
  })

  const totalStats = computed(() => {
    if (!overview.value) return { total: 0, online: 0, offline: 0, abnormal: 0 }
    const types = ['gnss', 'rain', 'seepage']
    return types.reduce((acc, type) => {
      const s = overview.value[type]
      if (s) {
        acc.total += s.total
        acc.online += s.online
        acc.offline += s.offline
        acc.abnormal += s.abnormal
      }
      return acc
    }, { total: 0, online: 0, offline: 0, abnormal: 0 })
  })

  const fetchData = async () => {
    loading.value = true
    error.value = null
    try {
      const res = await getDeviceMonitorStatus()
      overview.value = res.data.data.overview
      devices.value = res.data.data.devices
    } catch (e) {
      error.value = e.message || '获取设备监控数据失败'
      console.error('[DeviceMonitor] 数据加载失败:', e)
    } finally {
      loading.value = false
    }
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
    devices,
    filteredDevices,
    totalStats,
    activeType,
    activeStatus,
    setTypeFilter,
    setStatusFilter,
    fetchData
  }
}
