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
