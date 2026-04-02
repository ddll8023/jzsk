/**
 * 大坝安全监测 API
 * 功能：渗流压力、浸润线等监测数据接口
 */
import request from '@/utils/request'
import { normalizePageParams } from './_helpers'

/**
 * 获取监测点列表
 */
export function getPoints() {
  return request.get('/api/dam-monitoring/points')
}

/**
 * 获取渗流数据分页
 * @param {Object} params - { pointId, pointIds, startTime, endTime, current, size }
 */
export function getSeepagePage(params) {
  return request.get('/api/dam-monitoring/seepage/page', { params: normalizePageParams(params) })
}

/**
 * 获取水位高程时序数据
 * @param {Object} params - { pointId, startTime, endTime }
 */
export function getTimeWaterElevation(params) {
  return request.get('/api/dam-monitoring/time-water-elevation', { params })
}

/**
 * 获取水位时序数据
 */
export function getTimeWaterLevel(params) {
  return request.get('/api/dam-monitoring/time-water-level', { params })
}

/**
 * 获取温度时序数据
 */
export function getTimeTemperature(params) {
  return request.get('/api/dam-monitoring/time-temperature', { params })
}

/**
 * 获取水压时序数据
 */
export function getTimeWaterPressure(params) {
  return request.get('/api/dam-monitoring/time-water-pressure', { params })
}

/**
 * 获取最新水位高程
 */
export function getLatestWaterElevation() {
  return request.get('/api/dam-monitoring/latest-water-elevation')
}

/**
 * 获取水库水位分页数据
 * @param {Object} params - { page, size }
 */
export function getRiverWaterLevel(params) {
  return request.get('/api/dam-monitoring/river-water-level/page', { params: normalizePageParams(params) })
}

/**
 * 获取渗流量分页数据
 * @param {Object} params - { page, size }
 */
export function getSeepageFlowPage(params) {
  return request.get('/api/dam-monitoring/seepage-flow/page', { params: normalizePageParams(params) })
}

/**
 * 获取地表位移历史数据
 * @param {Object} params - { startTime, endTime, sensor, stationIds, projectId, page, size }
 */
export function getDisplacementHistory(params) {
  return request.get('/api/displacement-history/page', { params: normalizePageParams(params) })
}

/**
 * 获取所有测站最新位移数据（一张图专用）
 * @param {Object} params - { sensor, stationIds, projectId }
 */
export function getDisplacementLatest(params) {
  return request.get('/api/displacement-history/latest', { params: normalizePageParams(params) })
}
