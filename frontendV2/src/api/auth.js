/**
 * 认证接口模块
 * 功能：封装登录、登出、用户信息相关API
 * 遵循原则：KISS、YAGNI
 * Source: frontend/src/main.js axios配置
 */
import request, { qs } from '@/utils/request'

/**
 * 用户登录
 * @param {Object} credentials - 登录凭证
 * @param {string} credentials.username - 用户名
 * @param {string} credentials.password - 密码
 * @returns {Promise} 登录结果
 */
export function login(credentials) {
    return request.post('/login', qs.stringify(credentials))
}

/**
 * 获取当前用户信息
 * @returns {Promise} 用户信息
 */
export function getUserInfo() {
    return request.get('/user/userInfo')
}

/**
 * 修改密码
 * @param {Object} data - 密码数据
 * @param {string} data.currentPassword - 原密码
 * @param {string} data.password - 新密码
 * @param {string} data.confirmPassword - 确认密码
 * @returns {Promise} 修改结果
 */
export function updatePassword(data) {
    return request.post('/user/updatePass', data)
}
