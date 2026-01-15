/**
 * 水雨情 Composable
 * 功能：水雨情模块公共逻辑（时间处理、数据导出、日期快捷选项）
 * 遵循原则：KISS - 简洁实现，SOLID - 单一职责
 */
import { ref, computed } from 'vue'

/**
 * 解析时间数组为 Date 对象
 * 后端返回格式：[year, month, day, hour, minute, second?]
 * @param {Array} timeArr - 时间数组
 * @returns {Date} Date 对象
 */
export function parseTimeArray(timeArr) {
  if (!Array.isArray(timeArr) || timeArr.length < 5) {
    return new Date(0)
  }
  const [year, month, day, hour, minute, second = 0] = timeArr
  return new Date(year, month - 1, day, hour, minute, second)
}

/**
 * 格式化时间数组为字符串
 * @param {Array} timeArr - 时间数组
 * @param {string} format - 格式类型：'datetime' | 'date' | 'time'
 * @returns {string} 格式化后的时间字符串
 */
export function formatTimeArray(timeArr, format = 'datetime') {
  if (!Array.isArray(timeArr) || timeArr.length < 3) {
    return ''
  }

  const [year, month, day, hour = 0, minute = 0] = timeArr
  const pad = (n) => String(n).padStart(2, '0')

  const dateStr = `${year}-${pad(month)}-${pad(day)}`
  const timeStr = `${pad(hour)}:${pad(minute)}`

  if (format === 'date') return dateStr
  if (format === 'time') return timeStr
  return `${dateStr} ${timeStr}`
}

/**
 * 格式化 Date 对象为字符串
 * @param {Date} date - Date 对象
 * @param {string} format - 格式类型：'datetime' | 'date' | 'time'
 * @returns {string} 格式化后的时间字符串
 */
export function formatDate(date, format = 'datetime') {
  if (!(date instanceof Date) || isNaN(date.getTime())) {
    return ''
  }

  const pad = (n) => String(n).padStart(2, '0')
  const year = date.getFullYear()
  const month = pad(date.getMonth() + 1)
  const day = pad(date.getDate())
  const hour = pad(date.getHours())
  const minute = pad(date.getMinutes())
  const second = pad(date.getSeconds())

  if (format === 'date') return `${year}-${month}-${day}`
  if (format === 'time') return `${hour}:${minute}:${second}`
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

/**
 * 生成日期快捷选项
 * @returns {Array} 快捷选项列表
 */
export function getDateShortcuts() {
  return [
    {
      label: '最近一天',
      value: () => {
        const end = new Date()
        const start = new Date()
        start.setTime(start.getTime() - 24 * 60 * 60 * 1000)
        return [start, end]
      }
    },
    {
      label: '最近一周',
      value: () => {
        const end = new Date()
        const start = new Date()
        start.setTime(start.getTime() - 7 * 24 * 60 * 60 * 1000)
        return [start, end]
      }
    },
    {
      label: '最近一个月',
      value: () => {
        const end = new Date()
        const start = new Date()
        start.setTime(start.getTime() - 30 * 24 * 60 * 60 * 1000)
        return [start, end]
      }
    }
  ]
}

/**
 * 获取默认日期范围（最近一天）
 * @returns {Array} [开始时间, 结束时间]
 */
export function getDefaultDateRange() {
  const end = new Date()
  const start = new Date()
  start.setTime(start.getTime() - 24 * 60 * 60 * 1000)
  return [start, end]
}

/**
 * 导出数据为 CSV 文件
 * @param {Object} options - 导出配置
 * @param {Array} options.data - 数据数组
 * @param {Array} options.headers - 表头数组 [{label: '名称', key: 'name'}]
 * @param {string} options.filename - 文件名（不含扩展名）
 */
export function exportToCSV({ data, headers, filename }) {
  if (!data || data.length === 0) {
    console.warn('没有数据可导出')
    return false
  }

  // 生成表头行
  const headerRow = headers.map((h) => h.label).join(',')

  // 生成数据行
  const dataRows = data.map((row) => {
    return headers
      .map((h) => {
        const value = row[h.key]
        // 处理包含逗号或引号的值
        if (typeof value === 'string' && (value.includes(',') || value.includes('"'))) {
          return `"${value.replace(/"/g, '""')}"`
        }
        return value ?? ''
      })
      .join(',')
  })

  // 组合 CSV 内容（添加 BOM 支持中文）
  const csvContent = '\ufeff' + headerRow + '\n' + dataRows.join('\n')

  // 创建下载链接
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)

  link.setAttribute('href', url)
  link.setAttribute('download', `${filename}.csv`)
  link.style.visibility = 'hidden'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)

  return true
}

/**
 * 水雨情数据处理 Composable
 * @param {Object} options - 配置选项
 * @param {Function} options.fetchFn - 数据获取函数
 * @returns {Object} 状态和方法
 */
export function useWaterData(options = {}) {
  const { fetchFn = null } = options

  // 状态
  const data = ref([])
  const loading = ref(false)
  const error = ref(null)

  // 日期范围
  const dateRange = ref(getDefaultDateRange())

  // 过滤后的数据
  const filteredData = computed(() => {
    if (!dateRange.value || dateRange.value.length !== 2) {
      return data.value
    }

    const [start, end] = dateRange.value
    const startTime = start instanceof Date ? start : new Date(start)
    const endTime = end instanceof Date ? end : new Date(end)

    return data.value.filter((item) => {
      // 支持时间数组格式
      const itemTime = Array.isArray(item.tm) ? parseTimeArray(item.tm) : new Date(item.tm)
      return itemTime >= startTime && itemTime <= endTime
    })
  })

  /**
   * 加载数据
   */
  const loadData = async () => {
    if (!fetchFn) {
      console.warn('未配置 fetchFn')
      return
    }

    loading.value = true
    error.value = null

    try {
      const res = await fetchFn()
      // 兼容不同的响应格式
      data.value = res.data?.data || res.data || []
    } catch (e) {
      error.value = e.message || '数据加载失败'
      console.error('水雨情数据加载失败:', e)
    } finally {
      loading.value = false
    }
  }

  /**
   * 设置日期范围
   * @param {Array} range - [开始时间, 结束时间]
   */
  const setDateRange = (range) => {
    dateRange.value = range
  }

  /**
   * 重置日期范围为默认值
   */
  const resetDateRange = () => {
    dateRange.value = getDefaultDateRange()
  }

  return {
    data,
    loading,
    error,
    dateRange,
    filteredData,
    loadData,
    setDateRange,
    resetDateRange
  }
}

/**
 * 生成等间隔时间轴（用于图表）
 * @param {Date} start - 开始时间
 * @param {Date} end - 结束时间
 * @param {number} intervalMinutes - 间隔分钟数，默认 30
 * @returns {Array} 时间轴数组
 */
export function generateTimeAxis(start, end, intervalMinutes = 30) {
  const timeAxis = []
  const current = new Date(start)

  // 对齐到整点或半点
  const minutes = current.getMinutes()
  if (minutes < 30) {
    current.setMinutes(0, 0, 0)
  } else {
    current.setMinutes(30, 0, 0)
  }

  while (current <= end) {
    timeAxis.push(formatDate(new Date(current), 'datetime'))
    current.setMinutes(current.getMinutes() + intervalMinutes)
  }

  return timeAxis
}

/**
 * 将数据映射到时间轴
 * @param {Array} data - 原始数据
 * @param {Array} timeAxis - 时间轴
 * @param {string} timeKey - 时间字段名
 * @param {string} valueKey - 值字段名
 * @returns {Array} 映射后的值数组
 */
export function mapDataToTimeAxis(data, timeAxis, timeKey = 'tm', valueKey = 'value') {
  // 创建时间到值的映射
  const dataMap = new Map()
  data.forEach((item) => {
    const time = Array.isArray(item[timeKey])
      ? formatTimeArray(item[timeKey])
      : item[timeKey]
    dataMap.set(time, item[valueKey])
  })

  // 生成映射后的数据，缺失值用前一个值填充
  const result = []
  let lastValue = null

  timeAxis.forEach((time) => {
    if (dataMap.has(time)) {
      lastValue = dataMap.get(time)
    }
    result.push(lastValue)
  })

  // 回填开头的空值
  if (result[0] === null) {
    const firstNonNull = result.find((v) => v !== null)
    if (firstNonNull !== undefined) {
      for (let i = 0; i < result.length && result[i] === null; i++) {
        result[i] = firstNonNull
      }
    }
  }

  return result
}
