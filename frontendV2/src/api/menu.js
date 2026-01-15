/**
 * 菜单接口模块
 * 功能：菜单管理页面的 CRUD 操作
 * 遵循原则：KISS、YAGNI
 */
import { staticMenuData } from '@/data/menuData'

// 模拟异步响应格式
const mockResponse = (data) => Promise.resolve({ data: { code: 200, data, message: 'success' } })

/**
 * 获取菜单树列表（用于管理页面）
 * @returns {Promise} 菜单树数据
 */
export function getMenuList() {
    return mockResponse(staticMenuData)
}

/**
 * 新增菜单（静态数据，空操作）
 * @returns {Promise} 操作结果
 */
export function saveMenu() {
    return mockResponse(null)
}

/**
 * 更新菜单（静态数据，空操作）
 * @returns {Promise} 操作结果
 */
export function updateMenu() {
    return mockResponse(null)
}

/**
 * 删除菜单（静态数据，空操作）
 * @returns {Promise} 操作结果
 */
export function deleteMenu() {
    return mockResponse(null)
}
