/**
 * 维护记录 API
 * 功能：维护记录的增删改查接口
 * Source: 参考 frontend/src/components/menu/EngineeringPolling/MaintenceRecordRead.vue
 */
import request from '@/utils/request'

/**
 * 获取维护记录列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getMaintenanceList(params) {
  return request.get('/maintence-records/list', { params })
}

/**
 * 获取维护记录详情
 * @param {Number} id - 记录ID
 * @returns {Promise}
 */
export function getMaintenanceInfo(id) {
  return request.get(`/maintence-records/info/${id}`)
}

/**
 * 新增维护记录
 * @param {Object} data - 表单数据
 * @returns {Promise}
 */
export function saveMaintenance(data) {
  return request.post('/maintence-records/save', data)
}

/**
 * 更新维护记录
 * @param {Object} data - 表单数据
 * @returns {Promise}
 */
export function updateMaintenance(data) {
  return request.post('/maintence-records/update', data)
}

/**
 * 删除维护记录
 * @param {Number} id - 记录ID
 * @returns {Promise}
 */
export function deleteMaintenance(id) {
  return request.post(`/maintence-records/delete/${id}`)
}

/**
 * 导出Excel
 * @returns {Promise}
 */
export function exportMaintenanceExcel() {
  return request.get('/maintence-records/export-excel')
}
