/**
 * 设备监控 API 模块
 * 功能：封装设备监控相关的接口（按设备类型拆分为独立接口）
 */
import request from '@/utils/request'

/**
 * 获取GNSS设备监控状态
 * @returns {Promise} GNSS设备统计和设备列表
 */
export function getGnssStatus() {
  return request.get('/api/device-monitor/gnss')
}

/**
 * 获取雨水情设备监控状态
 * @returns {Promise} 雨水情设备统计和设备列表
 */
export function getRainStatus() {
  return request.get('/api/device-monitor/rain')
}

/**
 * 获取渗流渗压设备监控状态
 * @returns {Promise} 渗流渗压设备统计和设备列表
 */
export function getSeepageStatus() {
  return request.get('/api/device-monitor/seepage')
}

/**
 * 分页查询历史故障记录
 * @param {Object} data - 查询参数（page, size, deviceType, faultStatus, processStatus, keyword, startTime, endTime）
 * @returns {Promise} 分页故障记录列表
 */
export function getDeviceFaultRecordPage(data) {
  return request.post('/api/device-monitor/fault-record/page', data)
}

/**
 * 查询故障事件明细
 * @param {number} id - 故障主记录ID
 * @returns {Promise} 事件明细列表
 */
export function getDeviceFaultEvents(id) {
  return request.get(`/api/device-monitor/fault-record/${id}/events`)
}

/**
 * 删除故障记录
 * @param {number} id - 故障记录ID
 * @returns {Promise}
 */
export function deleteDeviceFaultRecord(id) {
  return request.post('/api/device-monitor/fault-record/delete', { id })
}
