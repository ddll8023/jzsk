/**
 * 测站到报状态检测 Composable
 * 功能：根据数据采集时间判断设备是否到报（超时未到报）
 * 超时阈值（含1分钟缓冲，应对四舍五入显示）：
 * - GNSS测站：61分钟（每小时采集一次）
 * - 雨量水位站：6分钟
 * - 渗流量测站：11分钟
 */
import { ref, computed, onUnmounted } from 'vue'

/**
 * 测站状态枚举
 */
export const STATION_STATUS = {
  HIDDEN: 'hidden',  // 兼容旧状态，不再用于初始化
  ONLINE: 'online',   // 已到报
  OFFLINE: 'offline' // 未到报
}

/**
 * 超时阈值配置（毫秒）
 * 说明：阈值已预留1分钟缓冲，用于应对界面显示四舍五入导致的误判
 */
const TIMEOUT_THRESHOLDS = {
  gnss: 61 * 60 * 1000,       // GNSS：61分钟（原60分钟+1分钟缓冲）
  rain: 6 * 60 * 1000,         // 雨量水位站：6分钟（原5分钟+1分钟缓冲）
  seepage: 11 * 60 * 1000       // 渗流量测站：11分钟（原10分钟+1分钟缓冲）
}

/**
 * GNSS测站ID列表（用于初始化加载状态）
 */
const GNSS_STATION_IDS = [
  33210, 33214, 33216, 33212, 33215, 33211, 33217, 33213
]

/**
 * 渗压测站ID列表（用于初始化加载状态）
 */
const SEEPAGE_PIEZOMETER_IDS = [
  'P0108118', 'P0108248', 'P0108310', 'P0108190', 'P0108267',
  'P0108174', 'P0108181', 'P0108273', 'P0108198', 'P0108056',
  'P0108282', 'P0108100', 'P0108033', 'P0108046', 'P0108050',
  'P0108235', 'P0108242', 'P0108066', 'P0108345', 'P0108043',
  'P0108154', 'P0108236', 'P0108173', 'P0108376', 'P0108234',
  'P0108148', 'P0108206', 'P0108311', 'P0108377'
]

/**
 * 测站到报状态管理
 * @returns {Object} 到报状态和方法
 */
export function useStationStatus() {
  // 测站到报状态 Map：stationId/key -> { isOnline, lastCollectTime, status }
  const stationStatus = ref(new Map())

  // 定时刷新引用
  let refreshInterval = null

  /**
   * 判断单个测站是否已到报
   * @param {string} stationType - 测站类型
   * @param {string|Date} collectTime - 数据采集时间
   * @returns {boolean} 是否已到报
   */
  const isStationOnline = (stationType, collectTime) => {
    if (!collectTime) return false

    const threshold = TIMEOUT_THRESHOLDS[stationType] || TIMEOUT_THRESHOLDS.gnss
    const collectDate = new Date(collectTime)
    const now = new Date()
    const diff = now - collectDate

    return diff <= threshold
  }

  /**
   * 计算距超时剩余时间
   * @param {string} stationType - 测站类型
   * @param {string|Date} collectTime - 数据采集时间
   * @returns {number} 剩余时间（毫秒），负数表示已超时
   */
  const getRemainingTime = (stationType, collectTime) => {
    if (!collectTime) return 0

    const threshold = TIMEOUT_THRESHOLDS[stationType] || TIMEOUT_THRESHOLDS.gnss
    const collectDate = new Date(collectTime)
    const now = new Date()
    const diff = now - collectDate

    return threshold - diff
  }

  /**
   * 格式化剩余时间
   * @param {number} ms - 毫秒
   * @returns {string} 格式化字符串
   */
  const formatRemainingTime = (ms) => {
    if (ms <= 0) return '未到报'

    const minutes = Math.floor(ms / 60000)
    const seconds = Math.floor((ms % 60000) / 1000)

    if (minutes >= 60) {
      const hours = Math.floor(minutes / 60)
      const mins = minutes % 60
      return `${hours}小时${mins}分钟`
    }
    if (minutes >= 1) {
      return `${minutes}分${seconds}秒`
    }
    return `${seconds}秒`
  }

  /**
   * 初始化所有测站为未到报状态（无数据时也显示）
   * 用于数据开始加载时调用
   */
  const initLoadingStatus = () => {
    // GNSS测站 - 8个
    GNSS_STATION_IDS.forEach(stationId => {
      const key = `gnss_${stationId}`
      stationStatus.value.set(key, {
        isOnline: null,
        lastCollectTime: null,
        status: STATION_STATUS.OFFLINE
      })
    })

    // 雨量站
    stationStatus.value.set('rain', {
      isOnline: null,
      lastCollectTime: null,
      status: STATION_STATUS.OFFLINE
    })

    // 渗压测站
    SEEPAGE_PIEZOMETER_IDS.forEach(piezometerId => {
      const key = `seepage_${piezometerId}`
      stationStatus.value.set(key, {
        isOnline: null,
        lastCollectTime: null,
        status: STATION_STATUS.OFFLINE
      })
    })
  }

  /**
   * 更新GNSS测站状态
   * @param {Array} gnssData - GNSS数据列表
   */
  const updateGnssStatus = (gnssData) => {
    if (!Array.isArray(gnssData)) return

    gnssData.forEach(item => {
      const key = `gnss_${item.stationId}`
      const isOnline = isStationOnline('gnss', item.collectTime)
      stationStatus.value.set(key, {
        isOnline,
        lastCollectTime: item.collectTime,
        status: isOnline ? STATION_STATUS.ONLINE : STATION_STATUS.OFFLINE
      })
    })
  }

  /**
   * 更新雨量水位站状态
   * @param {Object} waterLevelData - 水位数据
   * @param {Object} rainfallData - 雨量数据
   */
  const updateRainStatus = (waterLevelData, rainfallData) => {
    const waterTime = waterLevelData?.time
    const rainTime = rainfallData?.time
    const lastTime = waterTime || rainTime

    const isOnline = isStationOnline('rain', lastTime)
    stationStatus.value.set('rain', {
      isOnline,
      lastCollectTime: lastTime,
      status: isOnline ? STATION_STATUS.ONLINE : STATION_STATUS.OFFLINE
    })
  }

  /**
   * 更新渗压测站状态
   * @param {Array} seepageData - 渗压数据列表
   */
  const updateSeepageStatus = (seepageData) => {
    if (!Array.isArray(seepageData)) return

    seepageData.forEach(item => {
      const key = `seepage_${item.piezometerId}`
      const isOnline = isStationOnline('seepage', item.time)
      stationStatus.value.set(key, {
        isOnline,
        lastCollectTime: item.time,
        status: isOnline ? STATION_STATUS.ONLINE : STATION_STATUS.OFFLINE
      })
    })
  }

  /**
   * 获取测站状态
   * @param {string} stationType - 测站类型
   * @param {string|number} stationId - 测站ID
   * @returns {Object} 状态对象
   */
  const getStationStatus = (stationType, stationId) => {
    // 雨量站只有单个固定站点，key 直接用 'rain'
    if (stationType === 'rain') {
      return stationStatus.value.get('rain') || {
        isOnline: null,
        lastCollectTime: null,
        status: STATION_STATUS.OFFLINE
      }
    }
    // upb 类型映射到 seepage_ 前缀（useStationMarkers 中渗压测站的类型是 'upb'）
    const key = (stationType === 'seepage' || stationType === 'upb') ? `seepage_${stationId}` : `${stationType}_${stationId}`
    return stationStatus.value.get(key) || {
      isOnline: null,
      lastCollectTime: null,
      status: STATION_STATUS.OFFLINE
    }
  }

  /**
   * 获取所有未到报测站数量统计
   */
  const offlineStats = computed(() => {
    let gnss = 0, rain = 0, seepage = 0

    stationStatus.value.forEach((status, key) => {
      if (status.status === STATION_STATUS.OFFLINE) {
        if (key.startsWith('gnss_')) gnss++
        else if (key === 'rain') rain++
        else if (key.startsWith('seepage_')) seepage++
      }
    })

    return { gnss, rain, seepage }
  })

  /**
   * 批量更新所有测站状态
   * @param {Object} allData - 所有测站数据
   */
  const updateAllStatus = (allData) => {
    if (allData.gnssData) updateGnssStatus(allData.gnssData)
    if (allData.waterLevelData || allData.rainfallData) {
      updateRainStatus(allData.waterLevelData, allData.rainfallData)
    }
    if (allData.seepageData) updateSeepageStatus(allData.seepageData)
  }

  /**
   * 启动定时刷新
   * @param {Function} callback - 刷新回调
   * @param {number} interval - 刷新间隔（毫秒）
   */
  const startAutoRefresh = (callback, interval = 60000) => {
    stopAutoRefresh()
    refreshInterval = setInterval(callback, interval)
  }

  /**
   * 停止定时刷新
   */
  const stopAutoRefresh = () => {
    if (refreshInterval) {
      clearInterval(refreshInterval)
      refreshInterval = null
    }
  }

  // 组件卸载时清理
  onUnmounted(() => {
    stopAutoRefresh()
  })

  return {
    stationStatus,
    offlineStats,
    isStationOnline,
    getRemainingTime,
    formatRemainingTime,
    updateGnssStatus,
    updateRainStatus,
    updateSeepageStatus,
    updateAllStatus,
    getStationStatus,
    initLoadingStatus,
    startAutoRefresh,
    stopAutoRefresh,
    TIMEOUT_THRESHOLDS,
    STATION_STATUS
  }
}
