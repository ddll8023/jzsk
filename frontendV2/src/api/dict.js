/**
 * 字典管理 API 模块
 * 功能：封装字典相关的所有接口
 * 遵循原则：KISS - 简洁的接口封装
 */
import request from '@/utils/request'
import { buildIdPayload, normalizePageParams } from './_helpers'

// ==================== 字典主表接口 ====================

/**
 * 分页查询字典列表
 * @param {Object} params - 查询参数
 * @param {string} params.blurry - 模糊搜索关键词
 * @param {number} params.currentPage - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @returns {Promise} 字典列表
 */
export function getDictList(params) {
  return request.get('/api/dicts/page', { params: normalizePageParams(params) })
}

/**
 * 获取字典详情
 * @param {number|string} id - 字典ID
 * @returns {Promise} 字典信息
 */
export function getDictInfo(id) {
  return request.get(`/api/dicts/${id}`)
}

/**
 * 新增字典
 * @param {Object} data - 字典数据
 * @param {string} data.name - 字典名称
 * @param {string} data.description - 描述
 * @returns {Promise} 操作结果
 */
export function saveDict(data) {
  return request.post('/api/dicts/create', data)
}

/**
 * 更新字典
 * @param {Object} data - 字典数据
 * @param {number} data.id - 字典ID
 * @param {string} data.name - 字典名称
 * @param {string} data.description - 描述
 * @returns {Promise} 操作结果
 */
export function updateDict(data) {
  return request.post('/api/dicts/update', data)
}

/**
 * 删除字典
 * @param {number|string} id - 字典ID
 * @returns {Promise} 操作结果
 */
export function deleteDict(id) {
  return request.post('/api/dicts/delete', buildIdPayload(id))
}

/**
 * 获取字典选项（树形结构）
 * @param {string} name - 字典名称
 * @returns {Promise} 字典选项列表
 */
export function getDictKinds(name) {
  return request.get('/api/dicts/options/tree', { params: { name } })
}

/**
 * 获取字典选项（扁平结构）
 * @param {string} name - 字典名称
 * @returns {Promise} 字典选项列表
 */
export function getDictLVs(name) {
  return request.get('/api/dicts/options/list', { params: { name } })
}

// ==================== 字典详情接口 ====================

/**
 * 获取字典详情项
 * @param {number|string} id - 详情ID
 * @returns {Promise} 详情信息
 */
export function getDictDetailInfo(id) {
  return request.get(`/api/dict-details/${id}`)
}

/**
 * 新增字典详情
 * @param {Object} data - 详情数据
 * @param {number} data.dictId - 所属字典ID
 * @param {string} data.label - 标签
 * @param {string} data.value - 值
 * @param {number} data.dictSort - 排序
 * @returns {Promise} 操作结果
 */
export function saveDictDetail(data) {
  return request.post('/api/dict-details/create', data)
}

/**
 * 更新字典详情
 * @param {Object} data - 详情数据
 * @param {number} data.id - 详情ID
 * @param {string} data.label - 标签
 * @param {string} data.value - 值
 * @param {number} data.dictSort - 排序
 * @returns {Promise} 操作结果
 */
export function updateDictDetail(data) {
  return request.post('/api/dict-details/update', data)
}

/**
 * 删除字典详情
 * @param {number|string} id - 详情ID
 * @returns {Promise} 操作结果
 */
export function deleteDictDetail(id) {
  return request.post('/api/dict-details/delete', buildIdPayload(id))
}
