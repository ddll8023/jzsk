/**
 * 设备监控 API 模块
 * 功能：封装设备监控相关的接口
 */
import request from '@/utils/request'

/**
 * 获取所有设备监控状态
 * @returns {Promise} 设备监控总览数据（含统计和设备列表）
 */
export function getDeviceMonitorStatus() {
  return request.get('/api/device-monitor/status')
}
