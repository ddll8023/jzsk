/**
 * 大坝监测接口模块
 * 功能：渗压监测、渗流量监测数据查询
 */
import { get } from '@/utils/request.js'

/** 获取监测点列表 */
export function getDamPoints() {
  return get('/api/dam-monitoring/points')
}

/** 渗压分页查询 */
export function getSeepagePage(params = {}) {
  return get('/api/dam-monitoring/seepage/page', { page: 1, size: 10, ...params })
}

/** 水压时序数据 */
export function getTimeWaterPressure(params = {}) {
  return get('/api/dam-monitoring/time-water-pressure', params)
}

/** 温度时序数据 */
export function getTimeTemperature(params = {}) {
  return get('/api/dam-monitoring/time-temperature', params)
}

/** 水位时序数据 */
export function getTimeWaterLevel(params = {}) {
  return get('/api/dam-monitoring/time-water-level', params)
}

/** 水位高程时序数据 */
export function getTimeWaterElevation(params = {}) {
  return get('/api/dam-monitoring/time-water-elevation', params)
}

/** 各测点最新水位高程 */
export function getLatestWaterElevation() {
  return get('/api/dam-monitoring/latest-water-elevation')
}

/** 渗流量分页查询 */
export function getSeepageFlowPage(params = {}) {
  return get('/api/dam-monitoring/seepage-flow/page', { page: 1, size: 10, ...params })
}

/** 所有渗压最新数据 */
export function getSeepageLatestAll() {
  return get('/api/dam-monitoring/seepage/latest-all')
}
