/**
 * 用户管理接口模块
 * 功能：封装用户增删改查、角色分配、密码重置相关API
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: frontend/src/components/menu/SystemServe/UserManage.vue
 */
import request from '@/utils/request'

/**
 * 获取用户列表（分页）
 * @param {Object} params - 查询参数
 * @param {number} params.currentPage - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} [params.blurry] - 模糊搜索关键词
 * @returns {Promise} 用户列表数据
 */
export function getUserList(params) {
  return request.get('/user/list', { params })
}

/**
 * 搜索用户（按姓名）
 * @param {Object} params - 查询参数
 * @param {string} params.name - 姓名关键词
 * @param {number} params.currentPage - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @returns {Promise} 用户列表数据
 */
export function searchUser(params) {
  return request.get('/user/search-list', { params })
}

/**
 * 获取用户详情
 * @param {number|string} id - 用户ID
 * @returns {Promise} 用户详细信息
 */
export function getUserInfo(id) {
  return request.get(`/user/info/${id}`)
}

/**
 * 新增用户
 * @param {Object} data - 用户信息
 * @returns {Promise} 操作结果
 */
export function saveUser(data) {
  return request.post('/user/save', data)
}

/**
 * 更新用户信息
 * @param {Object} data - 用户信息（包含id）
 * @returns {Promise} 操作结果
 */
export function updateUser(data) {
  return request.post('/user/update', data)
}

/**
 * 删除用户
 * @param {number|string} id - 用户ID
 * @returns {Promise} 操作结果
 */
export function deleteUser(id) {
  return request.post(`/user/delete/${id}`)
}

/**
 * 分配角色
 * @param {number|string} userId - 用户ID
 * @param {Array<number>} roleIds - 角色ID数组
 * @returns {Promise} 操作结果
 */
export function allocateRole(userId, roleIds) {
  return request.post(`/user/role/${userId}`, roleIds)
}

/**
 * 初始化密码（重置为123456）
 * @param {number|string} id - 用户ID
 * @returns {Promise} 操作结果
 */
export function resetPassword(id) {
  return request.post('/user/repass', null, { params: { id } })
}

/**
 * 获取角色列表
 * @param {Object} params - 查询参数
 * @returns {Promise} 角色列表
 */
export function getRoleList(params) {
  return request.get('/role/list', { params })
}
