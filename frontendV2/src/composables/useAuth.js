/**
 * 认证 Composable
 * 功能：登录、登出、用户信息获取
 * 遵循原则：KISS、YAGNI、SOLID
 */
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { login as loginApi, getCurrentUser as getCurrentUserApi } from '@/api/auth'

/**
 * 认证相关逻辑
 * @returns {Object} 认证方法和状态
 */
export function useAuth() {
  const router = useRouter()
  const authStore = useAuthStore()

  /**
   * 用户登录
   * @param {Object} credentials - 登录凭证
   * @param {string} credentials.username - 用户名
   * @param {string} credentials.password - 密码
   * @returns {Promise<Object>} 登录结果
   */
  const login = async (credentials) => {
    try {
      const res = await loginApi(credentials)
      if (res.data && res.data.code === 200) {
        const { token, user } = res.data.data
        authStore.setToken(token)
        authStore.setUserInfo(user)
        return { success: true, data: res.data.data }
      }
      return { success: false, message: res.data?.message || '登录失败' }
    } catch (error) {
      return { success: false, message: error.message || '登录失败' }
    }
  }

  /**
   * 用户登出
   */
  const logout = () => {
    authStore.logout()
    router.push('/login')
  }

  /**
   * 获取当前登录用户信息
   * @returns {Promise<Object>} 当前登录用户信息
   */
  const getCurrentUser = async () => {
    try {
      const res = await getCurrentUserApi()
      if (res.data && res.data.code === 200) {
        authStore.setUserInfo(res.data.data)
        return { success: true, data: res.data.data }
      }
      return { success: false, message: res.data?.message || '获取用户信息失败' }
    } catch (error) {
      return { success: false, message: error.message || '获取用户信息失败' }
    }
  }

  return {
    login,
    logout,
    getCurrentUser,
    isLoggedIn: authStore.isLoggedIn,
    userInfo: authStore.userInfo,
    token: authStore.token
  }
}

