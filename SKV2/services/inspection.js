/**
 * 巡检记录接口模块
 * 功能：巡检记录增删改查、图片上传
 */
import { get, post } from '@/utils/request.js'
import { getStorage } from '@/utils/storage.js'
import config from '@/config/index.js'

/** 巡检记录分页查询 */
export function getInspectionPage(params = {}) {
  return get('/api/inspection-records/page', { page: 1, size: 10, ...params })
}

/** 巡检记录详情 */
export function getInspectionById(id) {
  return get(`/api/inspection-records/${id}`)
}

/** 新增巡检记录 */
export function createInspection(data) {
  return post('/api/inspection-records/create', data)
}

/** 更新巡检记录 */
export function updateInspection(data) {
  return post('/api/inspection-records/update', data)
}

/** 删除巡检记录 */
export function deleteInspection(id) {
  return post('/api/inspection-records/delete', { id })
}

/** 处理巡检记录 */
export function solveInspection(id) {
  return post('/api/inspection-records/solve', { id })
}

/** 上传巡检图片 */
export function uploadInspectionImage(filePath) {
  return new Promise((resolve, reject) => {
    const token = getStorage('token')

    uni.uploadFile({
      url: `${config.baseUrl}/api/inspection-records/upload`,
      filePath,
      name: 'image',
      header: {
        Authorization: token ? `Bearer ${token}` : '',
      },
      success: (res) => {
        if (res.statusCode === 200) {
          const data = JSON.parse(res.data)
          if (data.code === 200 || data.code === 0) {
            resolve(data)
          } else {
            reject(new Error(data.message || '上传失败'))
          }
        } else {
          reject(new Error(`上传失败: ${res.statusCode}`))
        }
      },
      fail: (err) => {
        reject(err)
      },
    })
  })
}
