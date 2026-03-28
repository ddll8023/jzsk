/**
 * 测站数据管理 Composable
 * 功能：统一管理GNSS、雨量水位、渗流量等测站数据
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: 参照旧项目 frontend/src/components/menu/OneMaps.vue
 * 修复：使用统一的request实例，确保baseURL配置生效
 * 修复：使用V2接口路径（/api前缀）
 */

import { ref, computed } from 'vue'
import request from '@/utils/request'

/**
 * GNSS测站配置
 * Source: frontend/src/components/menu/OneMaps.vue
 */
const GNSS_STATIONS = [
  { stationId: 33210, name: 'LJ1-1' },
  { stationId: 33214, name: 'LJ1-2' },
  { stationId: 33216, name: 'LJ1-3' },
  { stationId: 33212, name: 'LJ1-4' },
  { stationId: 33215, name: 'LT2-1' },
  { stationId: 33211, name: 'LT2-2' },
  { stationId: 33217, name: 'LT2-3' },
  { stationId: 33213, name: 'LT2-4' }
]

/**
 * 测站数据管理
 * @returns {Object} 测站数据状态和操作方法
 */
export function useStationData() {
  // 加载状态
  const loading = ref(false)
  const error = ref('')

  // GNSS位移数据
  const gnssData = ref([])
  const latestGnss = computed(() => {
    if (!gnssData.value.length) return null
    // 返回最新的合位移数据
    const sorted = [...gnssData.value].sort((a, b) =>
      new Date(b.collectTime) - new Date(a.collectTime)
    )
    return sorted[0]
  })

  // 雨量水位数据
  const waterLevelData = ref(null)
  const rainfallData = ref(null)

  // 渗流量数据
  const seepageData = ref([])
  const latestSeepage = computed(() => {
    if (!seepageData.value.length) return null
    return seepageData.value[0]
  })

  /**
   * 获取GNSS位移数据
   * API: /api/displacement-history/page
   * 修复：使用V2接口路径
   */
  const fetchGnssData = async () => {
    try {
      const end = new Date()
      const start = new Date(end.getTime() - 7 * 24 * 60 * 60 * 1000) // 最近7天

      const params = {
        startTime: formatDateTime(start),
        endTime: formatDateTime(end),
        sensor: 'L1_GP', // GNSS传感器类型（固定值）
        stationIds: GNSS_STATIONS.map(s => s.stationId).join(','), // 测站ID列表（逗号分隔）
        projectId: 1681, // 项目ID（固定值）
        page: 1, // 分页参数
        size: 2000
      }

      const res = await request.get('/api/displacement-history/page', { params })
      // 修复：API返回的是 res.data.data.list（嵌套了两层data）
      const records = (res && res.data && res.data.data && res.data.data.list) ? res.data.data.list : []

      // 转换数据结构：从 keyValues 数组提取位移值
      const transformedRecords = records.map(record => {
        const keyValuesMap = {}
        if (record.keyValues && Array.isArray(record.keyValues)) {
          record.keyValues.forEach(kv => {
            keyValuesMap[kv.key] = kv.value
          })
        }

        return {
          stationId: record.stationId,
          stationName: record.stationName,
          collectTime: record.collectTime,
          displacement3d: keyValuesMap['displacement3d'] || null, // 合位移
          gpsTotalZ: keyValuesMap['gpsTotalZ'] || null, // Z位移
          gpsTotalX: keyValuesMap['gpsTotalX'] || null, // X位移
          gpsTotalY: keyValuesMap['gpsTotalY'] || null // Y位移
        }
      })

      // 聚合：每个stationId取最新的一条
      const stationMap = {}
      transformedRecords.forEach(r => {
        const sid = r.stationId
        if (!stationMap[sid] || new Date(r.collectTime) > new Date(stationMap[sid].collectTime)) {
          stationMap[sid] = r
        }
      })

      gnssData.value = Object.values(stationMap)
      return gnssData.value
    } catch (e) {
      console.error('获取GNSS数据失败:', e)
      error.value = '获取GNSS数据失败'
      return []
    }
  }

  /**
   * 获取水位数据
   * API: /api/water-levels/list
   * 修复：使用V2接口路径
   */
  const fetchWaterLevelData = async () => {
    try {
      const res = await request.get('/api/water-levels/list')
      const arr = Array.isArray(res.data?.data) ? res.data.data : Array.isArray(res.data) ? res.data : []
      const valid = arr.filter(it => Number(it.z1) > 0)

      if (valid.length > 0) {
        // 取最新的一条
        const sorted = valid.sort((a, b) => new Date(b.tm) - new Date(a.tm))
        waterLevelData.value = {
          waterLevel: Number(sorted[0].z1),
          time: sorted[0].tm
        }
      }

      return waterLevelData.value
    } catch (e) {
      console.error('获取水位数据失败:', e)
      error.value = '获取水位数据失败'
      return null
    }
  }

  /**
   * 获取雨量数据
   * API: /api/hourly-rainfalls/list
   * 修复：使用V2接口路径
   * 响应格式：{code, message, data: []}
   */
  const fetchRainfallData = async () => {
    try {
      const res = await request.get('/api/hourly-rainfalls/list')
      const lst = Array.isArray(res.data?.data) ? res.data.data : Array.isArray(res.data) ? res.data : []

      if (lst.length > 0) {
        // 取最新的一条
        const sorted = lst.sort((a, b) => new Date(b.tm) - new Date(a.tm))
        rainfallData.value = {
          rainfall: Number(sorted[0].drp || 0),
          time: sorted[0].tm
        }
      }

      return rainfallData.value
    } catch (e) {
      console.error('获取雨量数据失败:', e)
      error.value = '获取雨量数据失败'
      return null
    }
  }

  /**
   * 获取单个渗压测站数据
   * API: /api/dam-monitoring/seepage/page
   * 修复：使用V2接口路径
   * 修复：从 res.data.records 提取数据（不是 res.data）
   * 修复：补充温度数据解析（从 originalData 提取）
   * 修复：参数名调整 pointIds -> pointId, current -> page
   * Source: 参照旧项目 OneMaps.vue loadUpbLatest 方法
   */
  const fetchSingleSeepageData = async (piezometerId) => {
    try {
      const end = new Date()
      const start = new Date(end.getTime() - 7 * 24 * 60 * 60 * 1000)

      const params = {
        pointId: piezometerId, // V2参数名：pointId（单数）
        startTime: formatDateTime(start),
        endTime: formatDateTime(end),
        size: 100,
        page: 1 // V2参数名：page（不是current）
      }

      const res = await request.get('/api/dam-monitoring/seepage/page', { params })
      // 尝试多种可能的路径获取数据
      const rawData = res.data?.data?.list || res.data?.list || []
      const records = Array.isArray(rawData) ? rawData : []

      // 取最新的一条
      if (records.length > 0) {
        const latest = records.sort((a, b) => new Date(b.time) - new Date(a.time))[0]

        // 解析 resultData（水位高程、水位、水压）
        let resultData = {}
        try {
          resultData = JSON.parse(latest.resultData || '{}')
        } catch (e) {
          console.warn(`解析 ${piezometerId} resultData失败:`, e)
        }

        // 解析 originalData（温度）
        let originalData = {}
        try {
          originalData = JSON.parse(latest.originalData || '{}')
        } catch (e) {
          console.warn(`解析 ${piezometerId} originalData失败:`, e)
        }

        return {
          piezometerId,
          waterLevelElevation: resultData['水位高程'],
          waterLevel: resultData['水位'],
          pressure: resultData['水压'],
          temperature: originalData['温度'],
          time: latest.time
        }
      }

      return null
    } catch (e) {
      console.error(`获取渗压测站 ${piezometerId} 数据失败:`, e)
      return null
    }
  }

  /**
   * 批量获取渗流量数据
   * 修复：批量加载所有渗压测站数据（对齐旧项目 fetchLatestUpbStationData）
   * Source: 参照旧项目 OneMaps.vue 第980行
   */
  const fetchSeepageData = async (seepageStations = []) => {
    try {
      const promises = seepageStations.map(station =>
        fetchSingleSeepageData(station.piezometerId)
      )
      const results = await Promise.all(promises)
      seepageData.value = results.filter(data => data !== null)
      return seepageData.value
    } catch (e) {
      console.error('批量获取渗流量数据失败:', e)
      error.value = '批量获取渗流量数据失败'
      return []
    }
  }

  /**
   * 获取所有测站数据
   * 修复：接受渗压测站配置作为参数
   */
  const fetchAllStationData = async (seepageStations = []) => {
    loading.value = true
    error.value = ''

    try {
      await Promise.all([
        fetchGnssData(),
        fetchWaterLevelData(),
        fetchRainfallData(),
        fetchSeepageData(seepageStations)
      ])
    } catch (e) {
      console.error('获取测站数据失败:', e)
      error.value = '获取测站数据失败'
    } finally {
      loading.value = false
    }
  }

  /**
   * 格式化日期时间
   */
  const formatDateTime = (date) => {
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    const seconds = String(date.getSeconds()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
  }

  return {
    loading,
    error,
    gnssData,
    latestGnss,
    waterLevelData,
    rainfallData,
    seepageData,
    latestSeepage,
    fetchGnssData,
    fetchWaterLevelData,
    fetchRainfallData,
    fetchSeepageData,
    fetchAllStationData
  }
}
