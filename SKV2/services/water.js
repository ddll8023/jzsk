/**
 * 水位/雨量接口模块
 * 功能：水位监测、雨量监测数据查询
 */
import { get } from '@/utils/request.js'

/** 水位分页查询 */
export function getWaterLevelPage(params = {}) {
  return get('/api/water-levels/page', { page: 1, size: 10, ...params })
}

/** 水位列表查询 */
export function getWaterLevelList(params = {}) {
  return get('/api/water-levels/list', params)
}

/** 雨量分页查询 */
export function getHourlyRainfallPage(params = {}) {
  return get('/api/hourly-rainfalls/page', { page: 1, size: 10, ...params })
}

/** 雨量列表查询 */
export function getHourlyRainfallList(params = {}) {
  return get('/api/hourly-rainfalls/list', params)
}
