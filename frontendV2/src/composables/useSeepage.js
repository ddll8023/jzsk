/**
 * 渗流压力监测 Composable
 * 功能：管理渗流数据查询、图表、表格的状态和逻辑
 */
import { ref, computed, reactive } from 'vue'
import { formatMinute as formatMinuteUtil } from '@/utils/time'
import {
  getPoints,
  getSeepagePage,
  getTimeWaterElevation,
  getTimeWaterLevel,
  getTimeTemperature,
  getTimeWaterPressure,
  getLatestWaterElevation
} from '@/api/dam'

/**
 * 测点名称映射表（ID -> 显示名称）
 */
const pointNameIdMap = {
  '0+100': [
    { name: 'UPb1-1', id: 'P0108248' },
    { name: 'UPb1-2', id: 'P0108234' },
    { name: 'UPb1-3', id: 'P0108376' },
    { name: 'UPb1-4', id: 'P0108173' },
    { name: 'UPb1-5', id: 'P0108236' }
  ],
  '0+150': [
    { name: 'UPa1-1', id: 'P0108190', pipeIndex: 0 },
    { name: 'UPa1-4', id: 'P0108345', pipeIndex: 0 },
    { name: 'UPa1-5', id: 'P0108154', pipeIndex: 0 },
    { name: 'UPb2-1', id: 'P0108310', pipeIndex: 0 },
    { name: 'UPb2-4', id: 'P0108066', pipeIndex: 0 },
    { name: 'UPb2-2', id: 'P0108046', pipeIndex: 1 },
    { name: 'UPa1-2', id: 'P0108050', pipeIndex: 2 },
    { name: 'UPb2-3', id: 'P0108235', pipeIndex: 3 },
    { name: 'UPa1-3', id: 'P0108242', pipeIndex: 4 }
  ],
  '0+250': [
    { name: 'UPb3-1', id: 'P0108267' },
    { name: 'UPb3-2', id: 'P0108282' },
    { name: 'UPb3-3', id: 'P0108033' },
    { name: 'UPb3-4', id: 'P0108100' },
    { name: 'UPb3-5', id: 'P0108377' }
  ],
  '0+350': [
    { name: 'UPb4-1', id: 'P0108174' },
    { name: 'UPb4-2', id: 'P0108273' },
    { name: 'UPb4-3', id: 'P0108198' },
    { name: 'UPb4-4', id: 'P0108181' },
    { name: 'UPb4-5', id: 'P0108056' }
  ],
  '0+000': [{ name: 'UPr1-1', id: 'P0108118' }],
  '0+015': [{ name: 'UPr1-2', id: 'P0108148' }],
  '0+025': [{ name: 'UPr2-1', id: 'P0108206' }],
  '0+035': [{ name: 'UPr2-2', id: 'P0108311' }]
}

/**
 * 根据ID查找测点名称
 */
export function formatUpNameById(id) {
  if (!id) return ''
  for (const k in pointNameIdMap) {
    const arr = pointNameIdMap[k] || []
    const found = arr.find(x => x.id === id || String(x.id) === String(id))
    if (found) return found.name
  }
  return ''
}

/**
 * 根据名称查找ID
 */
export function getNameToId(name) {
  if (!name) return ''
  for (const k in pointNameIdMap) {
    const arr = pointNameIdMap[k] || []
    const found = arr.find(x => x.name === name)
    if (found) return found.id
  }
  return name
}

/**
 * 格式化日期为 yyyy-MM-dd HH:mm:ss
 */
export function formatDateForPicker(date) {
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  const h = String(date.getHours()).padStart(2, '0')
  const min = String(date.getMinutes()).padStart(2, '0')
  const s = String(date.getSeconds()).padStart(2, '0')
  return `${y}-${m}-${d} ${h}:${min}:${s}`
}

/**
 * 格式化时间显示（精确到分钟，分钟个位数四舍五入）
 * @deprecated 请从 '@/utils/time' 导入 formatMinute
 */
export function formatMinute(val) {
  return formatMinuteUtil(val)
}

/**
 * 解析后端时间
 */
export function parseBackendTime(t) {
  if (t == null) return NaN
  if (typeof t === 'number') {
    return t < 1e12 ? t * 1000 : t
  }
  const d = new Date(String(t).replace(/-/g, '/'))
  return d.getTime()
}

/**
 * 解析JSON数据字段
 */
export function parseJsonField(data, key) {
  try {
    const parsed = typeof data === 'string' ? JSON.parse(data) : data
    const value = parsed[key]
    if (value !== undefined) {
      return typeof value === 'number' ? value.toFixed(2) : value
    }
    return ''
  } catch {
    return ''
  }
}

/**
 * 渗流数据查询 Composable
 */
export function useSeepage() {
  // 活跃状态标志，onMounted重置/onUnmounted清除，防止路由返回后静默失效
  let isActive = true

  // 状态
  const pointList = ref([])
  const loading = ref(false)
  const tableData = ref([])
  const total = ref(0)
  const chartData = ref([])

  // 查询参数
  const query = reactive({
    pointId: '',
    dateRangeType: '24h',
    dateRange: [],
    current: 1,
    size: 10
  })

  // 图表Tab
  const activeTab = ref('waterElevation')

  // 图表配置
  const chartConfig = {
    waterElevation: { title: '水位高程变化趋势', yAxisName: '水位高程', minSpan: 0.1 },
    waterLevel: { title: '水位变化趋势', yAxisName: '水位(mm)', minSpan: 1 },
    temperature: { title: '温度变化趋势', yAxisName: '温度(°C)', minSpan: 0.1 },
    waterPressure: { title: '水压变化趋势', yAxisName: '水压', minSpan: 0.1 }
  }

  // 计算属性：显示的日期范围
  const displayDateRange = computed(() => {
    if (query.dateRange?.length === 2) {
      return `${formatMinute(query.dateRange[0])} 至 ${formatMinute(query.dateRange[1])}`
    }
    return ''
  })

  /**
   * 设置快捷时间范围
   */
  function setQuickDateRange(type) {
    query.dateRangeType = type
    const now = new Date()
    let startDate, endDate

    switch (type) {
      case '24h':
        startDate = new Date(now.getTime() - 24 * 60 * 60 * 1000)
        endDate = now
        break
      case 'week':
        startDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
        endDate = now
        break
      case 'month':
        startDate = new Date(now.getFullYear(), now.getMonth() - 1, now.getDate())
        endDate = now
        break
      case 'threeMonth':
        startDate = new Date(now.getFullYear(), now.getMonth() - 3, now.getDate())
        endDate = now
        break
      case 'custom':
        if (!query.dateRange?.length) {
          query.dateRange = [
            formatDateForPicker(new Date(now.getTime() - 24 * 60 * 60 * 1000)),
            formatDateForPicker(now)
          ]
        }
        return
      default:
        startDate = new Date(now.getTime() - 24 * 60 * 60 * 1000)
        endDate = now
    }

    query.dateRange = [formatDateForPicker(startDate), formatDateForPicker(endDate)]
  }

  /**
   * 获取测点列表
   */
  async function fetchPoints() {
    try {
      const res = await getPoints()
      console.log('[fetchPoints] API响应:', res)
      if (!isActive) return

      const raw = Array.isArray(res.data) ? res.data : (res.data?.data || res.data?.records || [])
      console.log('[fetchPoints] 原始数据条数:', raw.length)

      pointList.value = raw.map(r => {
        // 后端返回的name字段可能是ID格式（如'P0108248'）
        const backendName = r.name || ''
        const backendId = r.id || ''
        const actualId = backendName || String(backendId)

        // 尝试从映射表查找显示名称
        const displayName = formatUpNameById(actualId)

        console.log('[fetchPoints] 处理测点:', { backendName, backendId, actualId, displayName })

        return {
          id: actualId,  // 用于API查询的ID
          name: displayName || actualId  // 显示名称，如果没有映射则显示ID
        }
      }).filter(p => p.id || p.name)

      pointList.value.sort((a, b) => (a.name || '').localeCompare(b.name || '', 'zh-CN'))
      console.log('[fetchPoints] 处理后测点数:', pointList.value.length)
    } catch (err) {
      console.error('[fetchPoints] 获取测点列表失败:', err)
      pointList.value = []
    }
  }

  /**
   * 获取表格数据
   */
  async function fetchTableData() {
    loading.value = true
    try {
      // query.pointId 现在直接是ID格式
      const params = {
        current: query.current,
        size: query.size,
        pointId: query.pointId || undefined
      }

      if (query.dateRange?.length === 2) {
        params.startTime = query.dateRange[0]
        params.endTime = query.dateRange[1]
      }

      const res = await getSeepagePage(params)
      if (!isActive) return
      tableData.value = res.data?.data?.list || res.data?.data?.records || []
      total.value = res.data?.data?.total || 0
    } catch {
      if (!isActive) return
      tableData.value = []
      total.value = 0
    } finally {
      if (!isActive) return
      loading.value = false
    }
  }

  // 图表状态
  const chartLoading = ref(false)

  // ... (existing code)

  /**
   * 获取图表数据
   */
  async function fetchChartData() {
    if (!query.pointId) {
      chartData.value = []
      return
    }

    chartLoading.value = true
    const apiMap = {
      waterElevation: getTimeWaterElevation,
      waterLevel: getTimeWaterLevel,
      temperature: getTimeTemperature,
      waterPressure: getTimeWaterPressure
    }

    const apiFn = apiMap[activeTab.value] || getTimeWaterElevation

    try {
      const params = { pointId: query.pointId }
      if (query.dateRange?.length === 2) {
        params.startTime = query.dateRange[0]
        params.endTime = query.dateRange[1]
      }

      const res = await apiFn(params)
      if (!isActive) return
      chartData.value = res.data?.data || []
    } catch {
      if (!isActive) return
      chartData.value = []
    } finally {
      if (!isActive) return
      chartLoading.value = false
    }
  }

  /**
   * 组件卸载时调用，防止异步更新已销毁组件
   */
  function cleanup() {
    isActive = false
  }

  /**
   * 重置活跃状态（组件挂载时调用）
   */
  function resetActive() {
    isActive = true
  }

  /**
   * 执行查询
   */
  function onSearch() {
    query.current = 1
    fetchTableData()
    fetchChartData()
  }

  /**
   * 分页变化
   */
  function onPageChange(page) {
    query.current = page
    fetchTableData()
  }

  return {
    // 状态
    pointList,
    loading,
    chartLoading,
    tableData,
    total,
    chartData,
    query,
    activeTab,
    chartConfig,
    displayDateRange,
    // 方法
    setQuickDateRange,
    fetchPoints,
    fetchTableData,
    fetchChartData,
    onSearch,
    onPageChange,
    cleanup,
    resetActive,
    // 工具函数
    formatMinute,
    parseBackendTime,
    parseJsonField,
    formatUpNameById,
    getNameToId,
    pointNameIdMap
  }
}
