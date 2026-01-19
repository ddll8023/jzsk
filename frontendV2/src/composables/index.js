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
export { usePrewarning } from './usePrewarning'
export { useIndicator } from './useIndicator'
export { useDutySchedule } from './useDutySchedule'
export { useDutyLog } from './useDutyLog'

// 工程巡检相关Composables
export { useInspection } from './useInspection'
export { useMaintenance } from './useMaintenance'
