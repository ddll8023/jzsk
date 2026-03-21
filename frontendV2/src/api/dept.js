/**
 * 部门管理 API
 * 功能：部门信息的增删改查接口
 * 遵循原则：KISS - 简洁实现，接口定义清晰
 */
import request from '@/utils/request'
import { buildIdPayload, normalizePageParams } from './_helpers'

/**
 * 获取部门列表（分页）
 * @param {Object} params - 查询参数
 * @param {number} params.currentPage - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.departmentName - 部门名称（可选，用于搜索）
 * @returns {Promise} 部门列表数据
 */
export function getDeptList(params) {
  return request.get('/api/departments/page', { params: normalizePageParams(params) })
}

/**
 * 获取部门详情
 * @param {number|string} id - 部门ID
 * @returns {Promise} 部门详细信息
 */
export function getDeptInfo(id) {
  return request.get(`/api/departments/${id}`)
}

/**
 * 新增部门
 * @param {Object} data - 部门信息
 * @param {string} data.departmentName - 部门名称
 * @param {string} data.departmentResponsibility - 部门职责
 * @param {string} data.level - 部门级别
 * @param {string} data.company - 所属公司
 * @returns {Promise} 新增结果
 */
export function saveDept(data) {
  return request.post('/api/departments/create', data)
}

/**
 * 更新部门信息
 * @param {Object} data - 部门信息（包含id）
 * @returns {Promise} 更新结果
 */
export function updateDept(data) {
  return request.post('/api/departments/update', data)
}

/**
 * 删除部门
 * @param {number|string} id - 部门ID
 * @returns {Promise} 删除结果
 */
export function deleteDept(id) {
  return request.post('/api/departments/delete', buildIdPayload(id))
}
