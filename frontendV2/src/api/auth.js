/**
 * 认证接口模块
 * 功能：封装登录相关API
 * 遵循原则：KISS、YAGNI
 * Source: frontend/src/main.js axios配置
 */
import request from '@/utils/request'

/**
 * 用户登录
 * @param {Object} credentials - 登录凭证
 * @param {string} credentials.username - 用户名
 * @param {string} credentials.password - 密码
 * @returns {Promise} 登录结果
 */
export function login(credentials) {
    return request.post('/api/auth/login', credentials, {
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

/**
 * 获取当前登录用户信息
 * @returns {Promise} 当前登录用户信息
 */
export function getCurrentUser() {
    return request.get('/api/auth/current-user')
}
