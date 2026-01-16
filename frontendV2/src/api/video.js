/**
 * 视频监控 API
 * 功能：视频配置、设备管理相关接口
 * 遵循原则：KISS
 */
import request from '@/utils/request'

/**
 * 获取视频配置列表
 * @param {Object} params - 查询参数
 * @param {number} params.currentPage - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.type - 设备类型（可选）
 * @param {string} params.name - 设备名称（可选）
 */
export function getVideoConfigList(params) {
  return request.get('/video-configuration/list', { params })
}

/**
 * 获取视频配置详情
 * @param {number|string} id - 配置ID
 */
export function getVideoConfigInfo(id) {
  return request.get(`/video-configuration/info/${id}`)
}

/**
 * 新增视频配置
 * @param {Object} data - 配置数据
 */
export function saveVideoConfig(data) {
  return request.post('/video-configuration/save', data)
}

/**
 * 更新视频配置
 * @param {Object} data - 配置数据
 */
export function updateVideoConfig(data) {
  return request.post('/video-configuration/update', data)
}

/**
 * 删除视频配置
 * @param {number|string} id - 配置ID
 */
export function deleteVideoConfig(id) {
  return request.post(`/video-configuration/delete/${id}`)
}

/**
 * 获取设备树形结构
 */
export function getVideoTree() {
  return request.get('/video-configuration/tree')
}

/**
 * 获取设备名称列表
 */
export function getVideoNames() {
  return request.get('/video-configuration/getNames')
}

/**
 * 按类型和名称搜索
 * @param {Object} params - 查询参数
 */
export function searchVideoConfig(params) {
  return request.get('/video-configuration/type-name-list', { params })
}

/**
 * 获取监测图片
 * @param {Object} params - 查询参数
 * @param {string} params.code - 监测点编码
 * @param {string} params.queryDate - 查询日期
 */
export function getVideoPhotos(params) {
  return request.get('/video-configuration/photos', { params })
}
