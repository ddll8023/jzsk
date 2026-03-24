/**
 * 巡检记录 API
 * 功能：巡检记录的增删改查接口
 * Source: 参考 frontend/src/components/menu/EngineeringPolling/PollingRecordRead.vue
 */
import request from '@/utils/request'
import { buildIdPayload, normalizePageParams } from './_helpers'

/**
 * 获取巡检记录列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getInspectionList(params) {
  return request.get('/api/inspection-records/page', { params: normalizePageParams(params) })
}

/**
 * 获取巡检记录详情
 * @param {Number} id - 记录ID
 * @returns {Promise}
 */
export function getInspectionInfo(id) {
  return request.get(`/api/inspection-records/${id}`)
}

/**
 * 新增巡检记录
 * @param {Object} data - 表单数据
 * @returns {Promise}
 */
export function saveInspection(data) {
  return request.post('/api/inspection-records/create', data)
}

/**
 * 更新巡检记录
 * @param {Object} data - 表单数据
 * @returns {Promise}
 */
export function updateInspection(data) {
  return request.post('/api/inspection-records/update', data)
}

/**
 * 删除巡检记录
 * @param {Number} id - 记录ID
 * @returns {Promise}
 */
export function deleteInspection(id) {
  return request.post('/api/inspection-records/delete', buildIdPayload(id))
}
/**
 * 导出Excel
 * @returns {Promise}
 */
export function exportInspectionExcel() {
  return request.get('/api/inspection-records/export')
}

/**
 * 上传图片
 * @param {FormData} formData - 图片文件
 * @returns {Promise}
 */
export function uploadInspectionImage(formData) {
  return request.post('/api/inspection-records/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
