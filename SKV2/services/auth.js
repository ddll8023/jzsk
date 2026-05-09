/**
 * 认证接口模块
 * 功能：登录、获取当前用户
 */
import { post, get } from '@/utils/request.js'

/** 账号密码登录 */
export function login(credentials) {
  return post('/api/auth/login', credentials)
}

/** 获取当前登录用户信息 */
export function getCurrentUser() {
  return get('/api/auth/current-user')
}
