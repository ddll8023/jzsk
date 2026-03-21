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
 * @returns {Promise} 水位数据分页结果
 */
export function getWaterLevelPage(params) {
  return request.get('/api/water-levels/page', { params: normalizePageParams(params) })
}

/**
 * 获取水位数据列表
 * @returns {Promise} 水位数据列表
 */
export function getWaterLevelList() {
  return request.get('/api/water-levels/list')
}

// ==================== 逐日雨量接口 ====================

/**
 * 获取逐日雨量列表
 * @returns {Promise} 逐日雨量数据列表
 */
export function getDailyRainfallList() {
  return request.get('/api/daily-rainfalls/list')
}

/**
 * 新增逐日雨量
 * @param {Object} data - 雨量数据
 * @param {string} data.stationName - 测站名称
 * @param {string} data.rainfallDate - 日期
 * @param {number} data.period811 - 8-11时雨量
 * @param {number} data.period1114 - 11-14时雨量
 * @param {number} data.period1417 - 14-17时雨量
 * @param {number} data.period1720 - 17-20时雨量
 * @param {number} data.period2023 - 20-23时雨量
 * @param {number} data.period232 - 23-2时雨量
 * @param {number} data.period25 - 2-5时雨量
 * @param {number} data.period58 - 5-8时雨量
 * @param {string} data.remark - 备注
 * @returns {Promise} 操作结果
 */
export function saveDailyRainfall(data) {
  return request.post('/api/daily-rainfalls/create', data)
}

/**
 * 更新逐日雨量
 * @param {number|string} id - 记录ID
 * @param {Object} data - 雨量数据
 * @returns {Promise} 操作结果
 */
export function updateDailyRainfall(id, data) {
  return request.post('/api/daily-rainfalls/update', { ...data, id })
}

// ==================== 河道站接口 ====================

/**
 * 获取河道站数据列表
 * @returns {Promise} 河道站数据列表
 */
export function getRiverStationList() {
  return request.get('/api/river-stations/list')
}

// ==================== 历年水情接口 ====================

/**
 * 获取历年水情列表
 * @returns {Promise} 历年水情数据列表
 */
export function getAnnualWaterList() {
  return request.get('/api/annual-water-situations/list')
}

/**
 * 新增历年水情
 * @param {Object} data - 水情数据
 * @param {number} data.stationId - 测站ID
 * @param {string} data.year - 年份
 * @param {number} data.waterLevel - 水位
 * @param {number} data.flowRate - 流量
 * @param {number} data.maxWaterLevel - 最大水位
 * @param {number} data.minWaterLevel - 最小水位
 * @param {number} data.maxFlowRate - 最大流量
 * @param {number} data.minFlowRate - 最小流量
 * @param {string} data.remarks - 备注
 * @returns {Promise} 操作结果
 */
export function saveAnnualWater(data) {
  return request.post('/api/annual-water-situations/create', data)
}

/**
 * 更新历年水情
 * @param {number|string} id - 记录ID
 * @param {Object} data - 水情数据
 * @returns {Promise} 操作结果
 */
export function updateAnnualWater(id, data) {
  return request.post('/api/annual-water-situations/update', { ...data, id })
}

/**
 * 删除历年水情
 * @param {number|string} id - 记录ID
 * @returns {Promise} 操作结果
 */
export function deleteAnnualWater(id) {
  return request.post('/api/annual-water-situations/delete', buildIdPayload(id))
}

// ==================== 测站极值接口 ====================

/**
 * 获取测站极值列表
 * @param {string} stcd - 监测站代码（可选）
 * @returns {Promise} 测站极值数据列表
 */
export function getStationExtremumList(stcd) {
  const params = stcd ? { stcd } : {}
  return request.get('/api/station-extremums/list', { params })
}

/**
 * 新增测站极值
 * @param {Object} data - 极值数据
 * @param {string} data.stcd - 监测站代码
 * @param {number} data.maxdrp1h - 1小时最大雨量
 * @param {string} data.tm1h - 1小时最大雨量发生时间
 * @param {number} data.maxdrp3h - 3小时最大雨量
 * @param {string} data.tm3h - 3小时最大雨量发生时间
 * @param {number} data.maxdrp6h - 6小时最大雨量
 * @param {string} data.tm6h - 6小时最大雨量发生时间
 * @param {number} data.maxdrp12h - 12小时最大雨量
 * @param {string} data.tm12h - 12小时最大雨量发生时间
 * @param {number} data.maxdrp24h - 24小时最大雨量
 * @param {string} data.tm24h - 24小时最大雨量发生时间
 * @param {string} data.remark - 备注
 * @returns {Promise} 操作结果
 */
export function saveStationExtremum(data) {
  return request.post('/api/station-extremums/create', data)
}

/**
 * 更新测站极值
 * @param {string} stcd - 监测站代码
 * @param {Object} data - 极值数据
 * @returns {Promise} 操作结果
 */
export function updateStationExtremum(stcd, data) {
  return request.post('/api/station-extremums/update', { ...data, stcd })
}
