/**
 * 综合报表模块API接口
 * 包含值班安排、值班日志相关接口
 */
import request from '@/utils/request'
import { buildIdPayload, buildIdsPayload, normalizePageParams } from './_helpers'

// ==================== 值班安排 ====================

/**
 * 获取值班安排分页列表
 * @param {Object} params - 查询参数 { current, size, startDate, endDate }
 * @returns {Promise}
 */
export const getDutySchedulePage = (params) => {
  return request.get('/api/duty-schedules/page', { params: normalizePageParams(params) })
}

/**
 * 新增值班安排
 * @param {Object} data - 值班安排数据
 * @returns {Promise}
 */
export const saveDutySchedule = (data) => {
  return request.post('/api/duty-schedules/create', data)
}

/**
 * 更新值班安排
 * @param {Number|String} id - 值班安排ID
 * @param {Object} data - 值班安排数据
 * @returns {Promise}
 */
export const updateDutySchedule = (id, data) => {
  return request.post('/api/duty-schedules/update', { ...data, id })
}

/**
 * 删除值班安排
 * @param {Number|String} id - 值班安排ID
 * @returns {Promise}
 */
export const deleteDutySchedule = (id) => {
  return request.post('/api/duty-schedules/delete', buildIdPayload(id))
}

/**
 * 批量删除值班安排
 * @param {Array} ids - 值班安排ID数组
 * @returns {Promise}
 */
export const batchDeleteDutySchedule = (ids) => {
  return request.post('/api/duty-schedules/batch-delete', buildIdsPayload(ids))
}

// ==================== 值班日志 ====================

/**
 * 获取值班日志分页列表
 * @param {Object} params - 查询参数 { current, size, startDate, endDate }
 * @returns {Promise}
 */
export const getDutyLogPage = (params) => {
  return request.get('/api/duty-logs/page', { params: normalizePageParams(params) })
}

/**
 * 新增值班日志
 * @param {Object} data - 值班日志数据
 * @returns {Promise}
 */
export const saveDutyLog = (data) => {
  return request.post('/api/duty-logs/create', data)
}

/**
 * 更新值班日志
 * @param {Number|String} id - 值班日志ID
 * @param {Object} data - 值班日志数据
 * @returns {Promise}
 */
export const updateDutyLog = (id, data) => {
  return request.post('/api/duty-logs/update', { ...data, id })
}

/**
 * 删除值班日志
 * @param {Number|String} id - 值班日志ID
 * @returns {Promise}
 */
export const deleteDutyLog = (id) => {
  return request.post('/api/duty-logs/delete', buildIdPayload(id))
}

/**
 * 批量删除值班日志
 * @param {Array} ids - 值班日志ID数组
 * @returns {Promise}
 */
export const batchDeleteDutyLog = (ids) => {
  return request.post('/api/duty-logs/batch-delete', buildIdsPayload(ids))
}
