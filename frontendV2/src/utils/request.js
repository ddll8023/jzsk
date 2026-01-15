/**
 * Axios 请求封装
 * 功能：HTTP 请求实例、拦截器配置
 * 参考：frontend/src/main.js 中的 axios 配置
 */
import axios from 'axios'
import qs from 'qs'

// 创建 Axios 实例
const service = axios.create({
  timeout: 30000,
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  }
})

/**
 * 配置 Axios 实例
 * @param {string} baseURL - API 基础地址
 */
export const setupAxios = (baseURL) => {
  service.defaults.baseURL = baseURL
}

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    console.log('API请求:', (config.method || 'GET').toUpperCase(), config.url)
    
    // 添加 Authorization 请求头
    const token = sessionStorage.getItem('token')
    if (token) {
      config.headers.Authorization = token
    }
    
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    console.log('API响应:', response.status, response.config.url)
    return response
  },
  (error) => {
    console.error('API响应错误:', 
      error.response ? error.response.status : 'unknown',
      error.response ? error.response.data : 'unknown'
    )
    console.error('错误详情:', error.message)
    
    // 401 未授权，跳转登录
    if (error.response && error.response.status === 401) {
      sessionStorage.removeItem('token')
      window.location.href = '/login'
    }
    
    return Promise.reject(error)
  }
)

// 导出 qs 工具
export { qs }

// 导出请求实例
export default service
