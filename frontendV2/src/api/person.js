/**
 * 人员管理接口模块
 * 功能：封装管理人员信息增删改查相关API
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: backend/szy/src/main/java/com/szy/controller/PersonController.java
 */
import request from '@/utils/request'
import { buildIdPayload, normalizePageParams } from './_helpers'

/**
 * 获取人员列表（分页）
 * @param {Object} params - 查询参数
 * @param {number} params.currentPage - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} [params.name] - 姓名关键词（模糊搜索）
 * @returns {Promise} 人员列表数据
 */
export function getPersonList(params) {
  return request.get('/api/persons/page', { params: normalizePageParams(params) })
}

/**
 * 获取人员详情
 * @param {number|string} id - 人员ID
 * @returns {Promise} 人员详细信息
 */
export function getPersonInfo(id) {
  return request.get(`/api/persons/${id}`)
}

/**
 * 新增人员
 * @param {Object} data - 人员信息
 * @returns {Promise} 操作结果
 */
export function savePerson(data) {
  return request.post('/api/persons/create', data)
}

/**
 * 更新人员信息
 * @param {Object} data - 人员信息（包含id）
 * @returns {Promise} 操作结果
 */
export function updatePerson(data) {
  return request.post('/api/persons/update', data)
}

/**
 * 删除人员
 * @param {number|string} id - 人员ID
 * @returns {Promise} 操作结果
 */
export function deletePerson(id) {
  return request.post('/api/persons/delete', buildIdPayload(id))
}
