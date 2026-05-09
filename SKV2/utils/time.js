/**
 * 日期时间工具函数
 * 功能：日期格式化、图表 X 轴时间、查询时间范围生成
 */

/**
 * 格式化日期
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @param {string} format - 格式模板，如 'YYYY-MM-DD'、'YYYY-MM-DD HH:mm'
 * @returns {string} 格式化后的日期字符串
 */
export function formatDate(date, format = 'YYYY-MM-DD') {
  if (!date) return '--'
  const d = date instanceof Date ? date : new Date(date)
  if (isNaN(d.getTime())) return '--'

  const pad = (n) => String(n).padStart(2, '0')

  const tokens = {
    YYYY: d.getFullYear(),
    MM: pad(d.getMonth() + 1),
    DD: pad(d.getDate()),
    HH: pad(d.getHours()),
    mm: pad(d.getMinutes()),
    ss: d.getSeconds(),
  }

  let result = format
  for (const [token, value] of Object.entries(tokens)) {
    result = result.replace(token, value)
  }
  return result
}

/**
 * 获取默认日期范围（最近 N 天）
 * @param {Object} options - 配置项
 * @param {number} options.days - 回溯天数，默认 7
 * @param {string} options.startKey - 开始日期的参数名，默认 'startDate'
 * @param {string} options.endKey - 结束日期的参数名，默认 'endDate'
 * @returns {Object} 如 { startDate: '2026-05-02 00:00:00', endDate: '2026-05-09 00:00:00' }
 */
export function getDefaultDateRange(options = {}) {
  const {
    days = 7,
    startKey = 'startDate',
    endKey = 'endDate',
  } = options
  const now = new Date()
  const ago = new Date(now.getTime() - days * 24 * 60 * 60 * 1000)
  return {
    [startKey]: formatDate(ago, 'YYYY-MM-DD HH:mm:ss'),
    [endKey]: formatDate(now, 'YYYY-MM-DD HH:mm:ss'),
  }
}
