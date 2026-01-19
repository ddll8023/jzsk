/**
 * 巡检记录 API
 * 功能：巡检记录的增删改查接口
 * Source: 参考 frontend/src/components/menu/EngineeringPolling/PollingRecordRead.vue
 */
import request from '@/utils/request'

/**
 * 获取巡检记录列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getInspectionList(params) {
  return request.get('/inspection-records/list', { params })
}

/**
 * 获取巡检记录详情
 * @param {Number} id - 记录ID
 * @returns {Promise}
 */
export function getInspectionInfo(id) {
  return request.get(`/inspection-records/info/${id}`)
}

/**
 * 新增巡检记录
 * @param {Object} data - 表单数据
 * @returns {Promise}
 */
export function saveInspection(data) {
  return request.post('/inspection-records/save', data)
}

/**
 * 更新巡检记录
 * @param {Object} data - 表单数据
 * @returns {Promise}
 */
export function updateInspection(data) {
  return request.post('/inspection-records/update', data)
}

/**
 * 删除巡检记录
 * @param {Number} id - 记录ID
 * @returns {Promise}
 */
export function deleteInspection(id) {
  return request.post(`/inspection-records/delete/${id}`)
}

/**
 * 处理巡检记录
 * @param {Object} data - 处理数据
 * @returns {Promise}
 */
export function solveInspection(data) {
  return request.post('/inspection-records/solveRecords', data)
}

/**
 * 导出Excel
 * @returns {Promise}
 */
export function exportInspectionExcel() {
  return request.get('/inspection-records/export-excel')
}

/**
 * 上传图片
 * @param {FormData} formData - 图片文件
 * @returns {Promise}
 */
export function uploadInspectionImage(formData) {
  return request.post('/inspection-records/upload2', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
