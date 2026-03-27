/**
 * 渗流量监测分析 Composable
 * 功能：管理渗流量数据查询、图表、表格的状态和逻辑
 */
import { ref, computed, reactive } from 'vue'
import { getSeepageFlowPage } from '@/api/dam'

/**
 * 测站列表配置（对应数据库 seepage_data 表的 station_id 字段）
 */
const STATION_LIST = [
  { id: '2', name: '主坝0+400坝脚量水堰' },
  { id: '3', name: '主坝0+200坝脚量水堰' }
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
 * 解析时间数组或字符串为Date对象
 * @param {Array|string} timeArr - 时间数组 [年, 月, 日, 时, 分, 秒] 或字符串 "yyyy-MM-dd HH:mm:ss"
 */
export function parseTimeArrayToDate(timeArr) {
  // 支持字符串格式 "yyyy-MM-dd HH:mm:ss"
  if (typeof timeArr === 'string') {
    const parts = timeArr.match(/\d+/g)
    if (parts && parts.length >= 5) {
      return new Date(parts[0], parts[1] - 1, parts[2], parts[3], parts[4], parts[5] || 0)
    }
    return new Date(0)
  }
  // 支持数组格式 [年, 月, 日, 时, 分, 秒]
  if (Array.isArray(timeArr) && timeArr.length >= 5) {
    return new Date(timeArr[0], timeArr[1] - 1, timeArr[2], timeArr[3], timeArr[4], timeArr.length > 5 ? timeArr[5] : 0)
  }
  return new Date(0)
}

/**
 * 格式化时间数组或字符串为字符串（精确到分钟）
 * @param {Array|string} timeArr - 时间数组或字符串
 */
export function formatTimeArray(timeArr) {
  // 支持字符串格式 "yyyy-MM-dd HH:mm:ss"
  if (typeof timeArr === 'string') {
    const parts = timeArr.match(/\d+/g)
    if (parts && parts.length >= 5) {
      const [y, m, d, h, min] = parts
      return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}`
    }
    return ''
  }
  // 支持数组格式 [年, 月, 日, 时, 分]
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
    stationId: '3', // 默认选择0+200测站（station_id=3）
    dateRangeType: '', // 空字符串表示未选择时间范围，默认显示全部数据
    dateRange: [],
    current: 1,
    size: 10
  })

  // 计算属性：筛选后的数据
  const filteredData = computed(() => {
    console.log('[useSeepageFlow] filteredData计算中...')
    console.log('[useSeepageFlow] allTableData.value 长度:', allTableData.value.length)
    console.log('[useSeepageFlow] query.stationId:', query.stationId)
    console.log('[useSeepageFlow] query.dateRange:', query.dateRange)

    // dateRange 无有效值时显示所有数据
    const hasValidDateRange = query.dateRange && query.dateRange.length === 2 &&
                              query.dateRange[0] && query.dateRange[1]

    const withinRange = (item) => {
      if (!hasValidDateRange) return true
      const [startDateStr, endDateStr] = query.dateRange
      const startDate = new Date(startDateStr.replace(/-/g, '/'))
      const endDate = new Date(endDateStr.replace(/-/g, '/'))
      const itemTime = parseTimeArrayToDate(item.tm)
      return itemTime >= startDate && itemTime <= endDate
    }

    const matchStation = (item) => {
      if (!query.stationId) return false
      // 优先取 stcd（测站编码），其次是 id
      const itemId = String(item.stcd || item.stationId || item.id || '')
      const matched = itemId === String(query.stationId)
      console.log('[useSeepageFlow] 测站匹配:', itemId, '===', query.stationId, '=', matched)
      return matched
    }

    const filtered = allTableData.value.filter(item => {
      if (!withinRange(item)) return false
      if (!matchStation(item)) return false
      const q1Val = Number(item.q1)
      const itemId = String(item.stcd || item.stationId || item.id || '')
      // 0+400测站显示q1>=0，其他测站只显示q1>0
      if (itemId === '2') {
        return !isNaN(q1Val) && q1Val >= 0
      } else {
        return !isNaN(q1Val) && q1Val > 0
      }
    })
    console.log('[useSeepageFlow] filteredData 结果长度:', filtered.length)
    return filtered
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

      // 构建API请求参数
      const hasDateRange = query.dateRange && query.dateRange.length === 2 &&
                           query.dateRange[0] && query.dateRange[1]

      // 数据过滤函数（在已有时间范围的基础上再过滤一次）
      const withinRange = (item) => {
        if (!hasDateRange) return true
        const [startDateStr, endDateStr] = query.dateRange
        const startDate = new Date(startDateStr.replace(/-/g, '/'))
        const endDate = new Date(endDateStr.replace(/-/g, '/'))
        const itemTime = parseTimeArrayToDate(item.tm)
        return itemTime >= startDate && itemTime <= endDate
      }

      const pushWithFilter = (list) => {
        console.log('[useSeepageFlow] pushWithFilter 收到记录数:', list.length)
        if (list.length > 0) {
          console.log('[useSeepageFlow] 第一条记录字段:', Object.keys(list[0]))
          console.log('[useSeepageFlow] 第一条记录内容:', JSON.stringify(list[0]))
        }
        list.forEach(raw => {
          if (!withinRange(raw)) return
          const q1Val = Number(raw.q1)
          // 优先取 stcd（测站编码），其次是 id
          const stationId = String(raw.stcd || raw.stationId || raw.id || '')
          console.log('[useSeepageFlow] 处理记录 stationId:', stationId, 'q1:', raw.q1, 'q1Val:', q1Val)

          // 根据测站过滤q1值（station_id=2 对应 0+400测站，q1>=0）
          if (stationId === '2') {
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
        // 构建API请求参数
        const apiParams = {
          page: currentFetchPage,
          size: fetchSize
        }
        // 传递测站ID（后端使用 pointId 接收）
        if (query.stationId) {
          apiParams.pointId = query.stationId
        }
        // 传递时间范围
        if (hasDateRange) {
          apiParams.startTime = query.dateRange[0]
          apiParams.endTime = query.dateRange[1]
        }

        const res = await getSeepageFlowPage(apiParams)
        console.log('[useSeepageFlow] 第', currentFetchPage, '页请求参数:', apiParams)
        console.log('[useSeepageFlow] 第', currentFetchPage, '页响应:', JSON.stringify(res))
        // API 响应是三层嵌套：res.data.data = { list, total, ... }
        const pageData = res && res.data && res.data.data ? res.data.data : {}
        // 支持两种响应格式：records（MyBatis-Plus）和 list（统一响应）
        const records = Array.isArray(pageData.records) ? pageData.records :
                        Array.isArray(pageData.list) ? pageData.list : []
        total = Number(pageData.total || 0)
        console.log('[useSeepageFlow] records长度:', records.length, 'total:', total, 'pageData keys:', Object.keys(pageData))
        if (records.length === 0) break

        pushWithFilter(records)

        // 获取足够数据后提前结束
        if (accumulatedAll.length >= 100) break

        currentFetchPage += 1
      }

      console.log('[useSeepageFlow] 累计数据条数:', accumulatedAll.length)
      console.log('[useSeepageFlow] 前3条原始数据:', JSON.stringify(accumulatedAll.slice(0, 3)))
      allTableData.value = accumulatedAll
      console.log('[useSeepageFlow] allTableData.value 设置后的值:', JSON.stringify(allTableData.value.slice(0, 3)))
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
    const hasDateRange = query.dateRange && query.dateRange.length === 2 &&
                         query.dateRange[0] && query.dateRange[1]

    if (hasDateRange) {
      startTime = new Date(query.dateRange[0].replace(/-/g, '/'))
      endTime = new Date(query.dateRange[1].replace(/-/g, '/'))
    } else if (chartData.value.length > 0) {
      // 无日期范围时，从实际数据中推导时间范围
      const times = chartData.value.map(item => parseTimeArrayToDate(item.tm)).filter(t => t.getTime() > 0)
      if (times.length > 0) {
        startTime = new Date(Math.min(...times))
        endTime = new Date(Math.max(...times))
      } else {
        endTime = new Date()
        startTime = new Date(endTime.getTime() - 24 * 60 * 60 * 1000)
      }
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
   * 将时间向下取整到最近的整点或半点
   * @param {string} formattedTime - 格式化时间字符串 "yyyy-MM-dd HH:mm"
   */
  function floorToHalfHour(formattedTime) {
    if (!formattedTime) return ''
    const match = formattedTime.match(/^(\d{4}-\d{2}-\d{2}) (\d{2}):(\d{2})$/)
    if (!match) return formattedTime
    const [, date, hour, min] = match
    const minutes = parseInt(min, 10)
    // 向下取整到最近的半点：0-29 -> 00, 30-59 -> 30
    const flooredMin = minutes < 30 ? '00' : '30'
    return `${date} ${hour}:${flooredMin}`
  }

  /**
   * 生成图表流量数据
   */
  function generateFlowData(timeAxis) {
    if (!chartData.value.length || !timeAxis.length) return []

    // 创建数据映射（将时间向下取整到最近的整点或半点）
    const dataMap = {}
    chartData.value.forEach(item => {
      // 将 formattedTime 向下取整到最近的半点
      const timeKey = floorToHalfHour(item.formattedTime)
      const q1 = Number(item.q1)
      const stationId = String(item.stationId || item.id || item.stcd || '')
      if (!isNaN(q1)) {
        if (stationId === '2') {
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
