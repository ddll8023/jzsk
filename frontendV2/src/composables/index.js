/**
 * Composables 统一导出
 * 功能：集中管理所有组合式函数
 */
export { useAuth } from './useAuth'
export { useForm } from './useForm'
export { useTable } from './useTable'
export { useDict } from './useDict'
export {
  useWaterData,
  parseTimeArray,
  formatTimeArray,
  formatDate,
  getDateShortcuts,
  getDefaultDateRange,
  exportToCSV,
  generateTimeAxis,
  mapDataToTimeAxis
} from './useWater'
