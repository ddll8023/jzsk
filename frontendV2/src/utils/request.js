/**
 * Axios 请求封装
 * 功能：HTTP 请求实例、拦截器配置
 * 参考：frontend/src/main.js 中的 axios 配置
 */
import axios from 'axios'

// 创建 Axios 实例
const service = axios.create({
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

/**
 * 配置 Axios 实例
 * @param {string} baseURL - API 基础地址
 */
export const setupAxios = (baseURL) => {
  service.defaults.baseURL = baseURL
}

function normalizePageData(pageData) {
  if (!pageData || typeof pageData !== 'object' || Array.isArray(pageData)) {
    return
  }

  if (Array.isArray(pageData.list) && !Array.isArray(pageData.records)) {
    pageData.records = pageData.list
  }

  if (Array.isArray(pageData.records) && !Array.isArray(pageData.list)) {
    pageData.list = pageData.records
  }

  if (typeof pageData.page === 'number' && typeof pageData.current !== 'number') {
    pageData.current = pageData.page
  }

  if (typeof pageData.current === 'number' && typeof pageData.page !== 'number') {
    pageData.page = pageData.current
  }

  if (typeof pageData.totalPages !== 'number' && typeof pageData.total === 'number' && typeof pageData.size === 'number' && pageData.size > 0) {
    pageData.totalPages = Math.ceil(pageData.total / pageData.size)
  }
}

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 构建完整URL用于日志
    const fullUrl = config.baseURL ? `${config.baseURL}${config.url}` : config.url
    // console.log('📤 API请求:', (config.method || 'GET').toUpperCase(), fullUrl)
    // console.log('   参数:', config.params || config.data || '无')
    
    // 验证baseURL是否已配置
    if (!config.baseURL && !config.url.startsWith('http')) {
      console.error('❌ baseURL未配置，请求可能失败:', config.url)
    }
    
    // 添加 Authorization 请求头（Bearer Token 格式）
    const token = sessionStorage.getItem('token')
    if (token) {
      // 添加 Bearer 前缀，符合 RFC 6750 规范
      config.headers.Authorization = token.startsWith('Bearer ') ? token : `Bearer ${token}`
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
    // console.log('📥 API响应:', response.status, fullUrl)
    // console.log('   数据:', response.data?.code, response.data?.message || '成功')

    if (response.data && typeof response.data === 'object') {
      normalizePageData(response.data)
      normalizePageData(response.data.data)
    }

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

// 导出请求实例
export default service
