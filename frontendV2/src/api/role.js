/**
 * 角色管理接口模块
 * 功能：封装角色增删改查、菜单权限分配相关API
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: 参考 user.js、dept.js 的接口封装模式
 */
import request from '@/utils/request'

/**
 * 获取角色列表（分页）
 * @param {Object} params - 查询参数
 * @param {number} params.currentPage - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} [params.name] - 角色名称（模糊搜索）
 * @returns {Promise} 角色列表数据
 */
export function getRoleList(params) {
  return request.get('/role/list', { params })
}

/**
 * 获取角色详情
 * @param {number|string} id - 角色ID
 * @returns {Promise} 角色详细信息
 */
export function getRoleInfo(id) {
  return request.get(`/role/info/${id}`)
}

/**
 * 新增角色
 * @param {Object} data - 角色信息
 * @param {string} data.name - 角色名称
 * @param {string} data.code - 角色编码
 * @param {string} [data.description] - 角色描述
 * @param {number} [data.status] - 状态（1启用 0禁用）
 * @param {number} [data.sort] - 排序号
 * @returns {Promise} 操作结果
 */
export function saveRole(data) {
  return request.post('/role/save', data)
}

/**
 * 更新角色信息
 * @param {Object} data - 角色信息（包含id）
 * @returns {Promise} 操作结果
 */
export function updateRole(data) {
  return request.post('/role/update', data)
}

/**
 * 删除角色
 * @param {number|string} id - 角色ID
 * @returns {Promise} 操作结果
 */
export function deleteRole(id) {
  return request.post(`/role/delete/${id}`)
}

/**
 * 分配菜单权限
 * @param {number|string} roleId - 角色ID
 * @param {Array<number>} menuIds - 菜单ID数组
 * @returns {Promise} 操作结果
 */
export function allocateMenu(roleId, menuIds) {
  return request.post(`/role/menu/${roleId}`, menuIds, { headers: { 'Content-Type': 'application/json' } })
}

/**
 * 获取角色已分配的菜单ID列表
 * @param {number|string} roleId - 角色ID
 * @returns {Promise} 菜单ID数组
 */
export function getRoleMenus(roleId) {
  return request.get(`/role/menus/${roleId}`)
}
