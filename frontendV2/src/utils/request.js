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
  console.log('🔧 Axios baseURL 已配置:', baseURL)
  
  // 验证配置是否生效
  if (service.defaults.baseURL !== baseURL) {
    console.error('❌ Axios baseURL 配置失败')
  }
}

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 构建完整URL用于日志
    const fullUrl = config.baseURL ? `${config.baseURL}${config.url}` : config.url
    console.log('📤 API请求:', (config.method || 'GET').toUpperCase(), fullUrl)
    console.log('   参数:', config.params || config.data || '无')
    
    // 验证baseURL是否已配置
    if (!config.baseURL && !config.url.startsWith('http')) {
      console.error('❌ baseURL未配置，请求可能失败:', config.url)
    }
    
    // 添加 Authorization 请求头
    const token = sessionStorage.getItem('token')
    if (token) {
      config.headers.Authorization = token
    }
    
    return config
  },
  (error) => {
    console.error('❌ 请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const fullUrl = response.config.baseURL ? `${response.config.baseURL}${response.config.url}` : response.config.url
    console.log('📥 API响应:', response.status, fullUrl)
    console.log('   数据:', response.data?.code, response.data?.message || '成功')
    return response
  },
  (error) => {
    const fullUrl = error.config?.baseURL ? `${error.config.baseURL}${error.config.url}` : error.config?.url
    console.error('❌ API响应错误:', fullUrl)
    console.error('   状态码:', error.response ? error.response.status : 'network error')
    console.error('   错误信息:', error.response?.data || error.message)
    
    // 401 未授权，跳转登录
    if (error.response && error.response.status === 401) {
      console.warn('⚠️ 未授权，跳转登录页')
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
