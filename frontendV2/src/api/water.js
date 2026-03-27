/**
 * 水雨情管理 API 模块
 * 功能：封装水雨情相关的所有接口
 * 遵循原则：KISS - 简洁的接口封装
 */
import request from '@/utils/request'
import { buildIdPayload, normalizePageParams } from './_helpers'

// ==================== 降雨数据接口 ====================

/**
 * 获取小时雨量列表
 * @param {Object} params - 查询参数
 * @param {string} params.startDate - 开始时间 (可选)
 * @param {string} params.endDate - 结束时间 (可选)
 * @returns {Promise} 小时雨量数据列表
 */
export function getHourlyRainfall(params) {
  return request.get('/api/hourly-rainfalls/list', { params })
}

// ==================== 水位数据接口 ====================

/**
 * 分页查询水位数据
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页条数
 * @param {string} params.startDate - 开始时间 (可选)
 * @param {string} params.endDate - 结束时间 (可选)
 * @returns {Promise} 水位数据分页结果
 */
export function getWaterLevelPage(params) {
  return request.get('/api/water-levels/page', { params: normalizePageParams(params) })
}

/**
 * 查询水位数据列表
 * @param {Object} params - 查询参数
 * @param {string} params.startDate - 开始时间 (可选)
 * @param {string} params.endDate - 结束时间 (可选)
 * @param {string} params.stcd - 测站编码 (可选)
 * @returns {Promise} 水位数据列表
 */
export function getWaterLevelList(params) {
  return request.get('/api/water-levels/list', { params })
}
