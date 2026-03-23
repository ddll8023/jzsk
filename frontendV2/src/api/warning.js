/**
 * 预警管理 API
 * 功能：预警信息、预警指标相关接口
 * 遵循原则：KISS - 简洁实现
 */
import request from '@/utils/request'
import { buildIdPayload, normalizePageParams } from './_helpers'

// ==================== 预警信息接口 ====================

/**
 * 获取预警信息列表
 * @param {Object} params - 查询参数
 * @param {number} params.currentPage - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.position - 预警地点
 * @param {string} params.status - 预警状态
 * @param {string} params.level - 预警等级
 * @param {string} params.type - 预警类型
 * @param {string} params.startTime - 开始时间
 * @param {string} params.endTime - 结束时间
 * @returns {Promise}
 */
export function getWarningList(params) {
  return request.get('/api/warnings/page', { params: normalizePageParams(params) })
}
/**
 * 更新预警信息（解除预警）
 * @param {Object} data - 预警数据
 * @returns {Promise}
 */
export function updateWarning(data) {
  return request.post('/api/warnings/update', data)
}

/**
 * 删除预警信息
 * @param {number|string} id - 预警ID
 * @returns {Promise}
 */
export function deleteWarning(id) {
  return request.post('/api/warnings/delete', buildIdPayload(id))
}
// ==================== 预警指标接口 ====================

/**
 * 获取预警指标列表
 * @param {Object} params - 查询参数
 * @param {number} params.currentPage - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.type - 监测项类型
 * @returns {Promise}
 */
export function getIndicatorList(params) {
  return request.get('/api/warning-indicators/page', { params: normalizePageParams(params) })
}

/**
 * 根据测点名称搜索指标
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function searchIndicatorByPosition(params) {
  return request.get('/api/warning-indicators/page', { params: normalizePageParams(params) })
}

/**
 * 获取监测项类型列表
 * @returns {Promise}
 */
export function getIndicatorTypes() {
  return request.get('/api/warning-indicators/types')
}

/**
 * 获取指标详情
 * @param {number|string} id - 指标ID
 * @returns {Promise}
 */
export function getIndicatorInfo(id) {
  return request.get(`/api/warning-indicators/${id}`)
}

/**
 * 新增预警指标
 * @param {Object} data - 指标数据
 * @returns {Promise}
 */
export function saveIndicator(data) {
  return request.post('/api/warning-indicators/create', data)
}

/**
 * 更新预警指标
 * @param {Object} data - 指标数据
 * @returns {Promise}
 */
export function updateIndicator(data) {
  return request.post('/api/warning-indicators/update', data)
}

/**
 * 删除预警指标
 * @param {number|string} id - 指标ID
 * @returns {Promise}
 */
export function deleteIndicator(id) {
  return request.post('/api/warning-indicators/delete', buildIdPayload(id))
}

