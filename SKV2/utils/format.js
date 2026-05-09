/**
 * 格式化工具函数（统一 re-export）
 * 功能：从 time.js 和 number.js 重新导出，保持向后兼容
 */
export { formatDate, getDefaultDateRange } from './time.js'
export { formatNum } from './number.js'
