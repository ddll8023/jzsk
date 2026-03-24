/**
 * 工程信息管理 API
 * 功能：监测站点、测项信息等基础工程信息接口
 * 注意：使用旧项目实际接口路径 /measuring-station/*
 */
import request from '@/utils/request'
import { buildIdPayload, normalizePageParams } from './_helpers'

/**
 * 获取监测站点列表
 * @param {Object} params - 查询参数
 * @param {Number} params.currentPage - 当前页码
 * @param {Number} params.pageSize - 每页条数
 * @param {String} params.name - 站点名称（可选）
 * @returns {Promise}
 */
export function getMonitorSiteList(params) {
  return request.get('/api/measuring-stations/page', { params: normalizePageParams(params) })
}

/**
 * 获取监测站点详情
 * @param {Number|String} id - 站点ID
 * @returns {Promise}
 */
export function getMonitorSiteInfo(id) {
  return request.get(`/api/measuring-stations/${id}`)
}

/**
 * 新增监测站点
 * @param {Object} data - 站点数据
 * @returns {Promise}
 */
export function saveMonitorSite(data) {
  return request.post('/api/measuring-stations/create', data)
}

/**
 * 更新监测站点
 * @param {Object} data - 站点数据
 * @returns {Promise}
 */
export function updateMonitorSite(data) {
  return request.post('/api/measuring-stations/update', data)
}

/**
 * 删除监测站点
 * @param {Number|String} id - 站点ID
 * @returns {Promise}
 */
export function deleteMonitorSite(id) {
  return request.post('/api/measuring-stations/delete', buildIdPayload(id))
}

/**
 * 获取所有监测站点名称（用于下拉选择）
 * @returns {Promise}
 */
export function getMonitorSiteNames() {
  return request.get('/api/measuring-stations/options')
}

/**
 * 获取测项信息列表
 * @param {Object} params - 查询参数
 * @param {Number} params.currentPage - 当前页码
 * @param {Number} params.pageSize - 每页条数
 * @param {String} params.name - 测项名称（可选）
 * @returns {Promise}
 */
export function getMonitorItemList(params) {
  return request.get('/api/measuring-items/page', { params: normalizePageParams(params) })
}

/**
 * 获取测项信息详情
 * @param {Number|String} id - 测项ID
 * @returns {Promise}
 */
export function getMonitorItemInfo(id) {
  return request.get(`/api/measuring-items/${id}`)
}

/**
 * 新增测项信息
 * @param {Object} data - 测项数据
 * @returns {Promise}
 */
export function saveMonitorItem(data) {
  return request.post('/api/measuring-items/create', data)
}

/**
 * 更新测项信息
 * @param {Object} data - 测项数据
 * @returns {Promise}
 */
export function updateMonitorItem(data) {
  return request.post('/api/measuring-items/update', data)
}

/**
 * 删除测项信息
 * @param {Number|String} id - 测项ID
 * @returns {Promise}
 */
export function deleteMonitorItem(id) {
  return request.post('/api/measuring-items/delete', buildIdPayload(id))
}

/**
 * 获取所有测项名称（用于下拉选择）
 * @returns {Promise}
 */
export function getMonitorItemNames() {
  return request.get('/api/measuring-items/options')
}

/**
 * 导出测项信息Excel
 * @returns {Promise}
 */
export function exportMonitorItemExcel() {
  return request.get('/api/measuring-items/export')
}

// ==================== 预警设施接口 ====================

/**
 * 获取预警设施列表
 * @param {Object} params - 查询参数
 * @param {Number} params.current - 当前页码
 * @param {Number} params.pageSize - 每页条数
 * @returns {Promise}
 */
export function getWarningFacilityList(params) {
  return request.get('/api/warning-facilities/page', { params: normalizePageParams(params) })
}

/**
 * 获取预警设施详情
 * @param {Number|String} id - 设施ID
 * @returns {Promise}
 */
export function getWarningFacilityInfo(id) {
  return request.get(`/api/warning-facilities/${id}`)
}

/**
 * 新增预警设施
 * @param {Object} data - 设施数据
 * @returns {Promise}
 */
export function saveWarningFacility(data) {
  return request.post('/api/warning-facilities/create', data)
}

/**
 * 更新预警设施
 * @param {Object} data - 设施数据
 * @returns {Promise}
 */
export function updateWarningFacility(data) {
  return request.post('/api/warning-facilities/update', data)
}

/**
 * 删除预警设施
 * @param {Number|String} id - 设施ID
 * @returns {Promise}
 */
export function deleteWarningFacility(id) {
  return request.post('/api/warning-facilities/delete', buildIdPayload(id))
}
