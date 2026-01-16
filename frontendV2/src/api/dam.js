/**
 * 大坝安全监测 API
 * 功能：渗流压力、浸润线等监测数据接口
 */
import request from '@/utils/request'

/**
 * 获取监测点列表
 */
export function getPoints() {
  return request.get('/data-new/points')
}

/**
 * 获取渗流数据分页
 * @param {Object} params - { pointId, pointIds, startTime, endTime, current, size }
 */
export function getSeepagePage(params) {
  return request.get('/data-new/page', { params })
}

/**
 * 获取水位高程时序数据
 * @param {Object} params - { pointId, startTime, endTime }
 */
export function getTimeWaterElevation(params) {
  return request.get('/data-new/time-water-elevation', { params })
}

/**
 * 获取水位时序数据
 */
export function getTimeWaterLevel(params) {
  return request.get('/data-new/time-water-level', { params })
}

/**
 * 获取温度时序数据
 */
export function getTimeTemperature(params) {
  return request.get('/data-new/time-temperature', { params })
}

/**
 * 获取水压时序数据
 */
export function getTimeWaterPressure(params) {
  return request.get('/data-new/time-water-pressure', { params })
}

/**
 * 获取最新水位高程
 */
export function getLatestWaterElevation() {
  return request.get('/data-new/latest-water-elevation')
}

/**
 * 获取水库水位分页数据
 * @param {Object} params - { page, size }
 */
export function getRiverWaterLevel(params) {
  return request.get('/st-rivers-r/page', { params })
}

/**
 * 获取渗流量分页数据
 * @param {Object} params - { page, size }
 */
export function getSeepageFlowPage(params) {
  return request.get('/st-rivers-r/page', { params })
}

/**
 * 获取地表位移历史数据
 * @param {Object} params - { startTime, endTime, sensor, stationIds, projectId, page, size }
 */
export function getDisplacementHistory(params) {
  return request.get('/external-data/displacement-history', { params })
}
