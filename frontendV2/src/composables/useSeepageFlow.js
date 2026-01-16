/**
 * 渗流量监测分析 Composable
 * 功能：管理渗流量数据查询、图表、表格的状态和逻辑
 */
import { ref, computed, reactive } from 'vue'
import { getSeepageFlowPage } from '@/api/dam'

/**
 * 测站列表配置
 */
const STATION_LIST = [
  { id: '4211822043', name: '主坝0+400坝脚量水堰' },
  { id: '4211823043', name: '主坝0+200坝脚量水堰' }
]

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
 * 格式化时间显示（精确到分钟）
 */
export function formatMinute(val) {
  if (!val) return ''
  let d
  if (typeof val === 'number') {
    d = new Date(val < 1e12 ? val * 1000 : val)
  } else {
    d = new Date(String(val).replace(/-/g, '/'))
  }
  if (isNaN(d.getTime())) return ''
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  const h = String(d.getHours()).padStart(2, '0')
  const min = String(d.getMinutes()).padStart(2, '0')
  return `${y}-${m}-${dd} ${h}:${min}`
}

/**
 * 解析时间数组为Date对象
 * @param {Array} timeArr - [年, 月, 日, 时, 分, 秒]
 */
export function parseTimeArrayToDate(timeArr) {
  if (Array.isArray(timeArr) && timeArr.length >= 5) {
    return new Date(timeArr[0], timeArr[1] - 1, timeArr[2], timeArr[3], timeArr[4], timeArr.length > 5 ? timeArr[5] : 0)
  }
  return new Date(0)
}

/**
 * 格式化时间数组为字符串
 */
export function formatTimeArray(timeArr) {
  if (Array.isArray(timeArr) && timeArr.length >= 5) {
    const [y, m, d, h, min] = timeArr
    return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}`
  }
  return ''
}

/**
 * 渗流量数据查询 Composable
 */
export function useSeepageFlow() {
  // 状态
  const stationList = ref(STATION_LIST)
  const loading = ref(false)
  const chartLoading = ref(false)
  const allTableData = ref([])
  const chartData = ref([])

  // 查询参数
  const query = reactive({
    stationId: '4211823043', // 默认选择0+200测站
    dateRangeType: '24h',
    dateRange: [],
    current: 1,
    size: 10
  })

  // 计算属性：筛选后的数据
  const filteredData = computed(() => {
    const withinRange = (item) => {
      if (!query.dateRange || query.dateRange.length === 0) return true
      const [startDateStr, endDateStr] = query.dateRange
      const startDate = new Date(startDateStr.replace(/-/g, '/'))
      const endDate = new Date(endDateStr.replace(/-/g, '/'))
      const itemTime = parseTimeArrayToDate(item.tm)
      return itemTime >= startDate && itemTime <= endDate
    }

    const matchStation = (item) => {
      if (!query.stationId) return false
      const itemId = String(item.id || item.stcd || item.stationId || '')
      return itemId === String(query.stationId)
    }

    return allTableData.value.filter(item => {
      if (!withinRange(item)) return false
      if (!matchStation(item)) return false
      const q1Val = Number(item.q1)
      const itemId = String(item.id || item.stcd || item.stationId || '')
      // 0+400测站显示q1>=0，其他测站只显示q1>0
      if (itemId === '4211822043') {
        return !isNaN(q1Val) && q1Val >= 0
      } else {
        return !isNaN(q1Val) && q1Val > 0
      }
    })
  })

  // 计算属性：分页数据
  const pagedData = computed(() => {
    const sortedData = [...filteredData.value].sort((a, b) => 
      parseTimeArrayToDate(b.tm) - parseTimeArrayToDate(a.tm)
    )
    const start = (query.current - 1) * query.size
    return sortedData.slice(start, start + query.size)
  })

  // 计算属性：总条数
  const total = computed(() => filteredData.value.length)

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
   * 获取测站名称
   */
  function getStationName(stationId) {
    const station = stationList.value.find(s => String(s.id) === String(stationId))
    return station ? station.name : `测站${stationId}`
  }

  /**
   * 获取数据
   */
  async function fetchData() {
    loading.value = true
    chartLoading.value = true
    try {
      const fetchSize = 200
      let total = Infinity
      let accumulatedAll = []
      let currentFetchPage = 1

      const withinRange = (item) => {
        if (!query.dateRange || query.dateRange.length === 0) return true
        const [startDateStr, endDateStr] = query.dateRange
        const startDate = new Date(startDateStr.replace(/-/g, '/'))
        const endDate = new Date(endDateStr.replace(/-/g, '/'))
        const itemTime = parseTimeArrayToDate(item.tm)
        return itemTime >= startDate && itemTime <= endDate
      }

      const pushWithFilter = (list) => {
        list.forEach(raw => {
          if (!withinRange(raw)) return
          const q1Val = Number(raw.q1)
          const stationId = String(raw.id || raw.stcd || raw.stationId || '')

          // 根据测站过滤q1值
          if (stationId === '4211822043') {
            if (isNaN(q1Val) || q1Val < 0) return
          } else {
            if (isNaN(q1Val) || q1Val <= 0) return
          }

          accumulatedAll.push({
            ...raw,
            formattedTime: formatTimeArray(raw.tm),
            stationName: getStationName(stationId),
            stationId: stationId
          })
        })
      }

      // 循环抓取数据
      while ((currentFetchPage - 1) * fetchSize < total) {
        const res = await getSeepageFlowPage({ page: currentFetchPage, size: fetchSize })
        const pageData = res && res.data ? res.data : {}
        const records = Array.isArray(pageData.records) ? pageData.records : []
        total = Number(pageData.total || 0)

        if (records.length === 0) break

        pushWithFilter(records)

        // 获取足够数据后提前结束
        if (accumulatedAll.length >= 100) break

        currentFetchPage += 1
      }

      allTableData.value = accumulatedAll
      updateChartData()
    } catch (error) {
      console.error('[useSeepageFlow] 数据加载失败:', error)
      allTableData.value = []
      chartData.value = []
    } finally {
      loading.value = false
      chartLoading.value = false
    }
  }

  /**
   * 更新图表数据
   */
  function updateChartData() {
    const sortedData = [...filteredData.value].sort((a, b) => 
      parseTimeArrayToDate(a.tm) - parseTimeArrayToDate(b.tm)
    )
    chartData.value = sortedData
  }

  /**
   * 生成等间隔时间轴（每半小时一个点）
   */
  function generateTimeAxis() {
    let endTime, startTime
    if (query.dateRange && query.dateRange.length === 2) {
      startTime = new Date(query.dateRange[0].replace(/-/g, '/'))
      endTime = new Date(query.dateRange[1].replace(/-/g, '/'))
    } else {
      endTime = new Date()
      startTime = new Date(endTime.getTime() - 24 * 60 * 60 * 1000)
    }

    // 起点对齐
    startTime.setSeconds(0, 0)
    startTime.setMinutes(startTime.getMinutes() < 30 ? 0 : 30)

    // 终点对齐
    const endM = endTime.getMinutes()
    endTime.setSeconds(0, 0)
    if (endM < 30) {
      endTime.setMinutes(30)
    } else {
      endTime.setMinutes(0)
      endTime.setHours(endTime.getHours() + 1)
    }

    const timeAxis = []
    const current = new Date(startTime)

    while (current <= endTime) {
      timeAxis.push(formatTimeArray([
        current.getFullYear(),
        current.getMonth() + 1,
        current.getDate(),
        current.getHours(),
        current.getMinutes()
      ]))
      current.setMinutes(current.getMinutes() + 30)
    }

    return timeAxis
  }

  /**
   * 生成图表流量数据
   */
  function generateFlowData(timeAxis) {
    if (!chartData.value.length || !timeAxis.length) return []

    // 创建数据映射
    const dataMap = {}
    chartData.value.forEach(item => {
      const timeKey = item.formattedTime
      const q1 = Number(item.q1)
      const stationId = String(item.stationId || item.id || item.stcd || '')
      if (!isNaN(q1)) {
        if (stationId === '4211822043') {
          if (q1 >= 0) dataMap[timeKey] = q1 * 1000
        } else {
          if (q1 > 0) dataMap[timeKey] = q1 * 1000
        }
      }
    })

    // 生成等间隔流量数据
    const flowData = []
    let lastValue = null

    timeAxis.forEach(timeKey => {
      if (dataMap[timeKey] !== undefined) {
        lastValue = dataMap[timeKey]
        flowData.push(lastValue)
      } else {
        flowData.push(lastValue)
      }
    })

    // 回填前段null值
    let firstNonNull = null
    for (let i = 0; i < flowData.length; i++) {
      if (flowData[i] != null) { firstNonNull = flowData[i]; break }
    }
    if (firstNonNull != null) {
      for (let i = 0; i < flowData.length && flowData[i] == null; i++) {
        flowData[i] = firstNonNull
      }
    }

    return flowData
  }

  /**
   * 执行查询
   */
  function onSearch() {
    query.current = 1
    fetchData()
  }

  /**
   * 测站变化
   */
  function onStationChange() {
    query.current = 1
    updateChartData()
  }

  /**
   * 分页变化
   */
  function onPageChange(page) {
    query.current = page
  }

  /**
   * 每页条数变化
   */
  function onSizeChange(size) {
    query.size = size
    query.current = 1
  }

  return {
    // 状态
    stationList,
    loading,
    chartLoading,
    allTableData,
    filteredData,
    pagedData,
    total,
    chartData,
    query,
    displayDateRange,
    // 方法
    setQuickDateRange,
    getStationName,
    fetchData,
    updateChartData,
    generateTimeAxis,
    generateFlowData,
    onSearch,
    onStationChange,
    onPageChange,
    onSizeChange,
    // 工具函数
    formatMinute,
    formatTimeArray,
    parseTimeArrayToDate
  }
}
