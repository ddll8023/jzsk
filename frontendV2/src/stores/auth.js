/**
 * 认证状态管理
 * 功能：用户登录状态、Token 管理
 * Source: Pinia 官方文档
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCurrentUser as getCurrentUserApi } from '@/api/auth'


export const useAuthStore = defineStore('auth', () => {
  // 状态
  const token = ref(sessionStorage.getItem('token') || null)
  // 从 sessionStorage 恢复用户信息
  const storedUserInfo = sessionStorage.getItem('userInfo')
  const userInfo = ref(storedUserInfo ? JSON.parse(storedUserInfo) : null)

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const userRole = computed(() => userInfo.value?.type || 'guest')

  /**
   * 设置 Token
   * @param {string} newToken - 新 Token
   */
  const setToken = (newToken) => {
    token.value = newToken
    sessionStorage.setItem('token', newToken)
  }

  /**
   * 设置用户信息
   * @param {Object} info - 用户信息
   */
  const setUserInfo = (info) => {
    userInfo.value = info
    // 持久化到 sessionStorage
    if (info) {
      sessionStorage.setItem('userInfo', JSON.stringify(info))
    } else {
      sessionStorage.removeItem('userInfo')
    }
  }

  /**
   * 获取当前登录用户信息
   * 使用解构获取 response.data，与统一返回结构保持一致
   */
  const fetchCurrentUser = async () => {
    try {
      const { data: res } = await getCurrentUserApi()
      if (res.code === 200) {
        // 使用 setUserInfo 确保持久化
        setUserInfo(res.data)
        return res.data
      }
      console.error('获取用户信息失败:', res.message)
      return null
    } catch (error) {
      console.error('获取用户信息失败:', error)
      return null
    }
  }

  /**
   * 登出
   * 清除 Token 和用户信息
   */
  const logout = () => {
    token.value = null
    userInfo.value = null
    sessionStorage.removeItem('token')
    sessionStorage.removeItem('userInfo')
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    userRole,
    setToken,
    setUserInfo,
    fetchCurrentUser,
    logout
  }
})
