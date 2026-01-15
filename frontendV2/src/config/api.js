/**
 * API 配置文件
 * 功能：智能 API 地址选择、连通性测试
 * 参考：frontend/src/config/api.js
 */

/**
 * 获取 API 基础地址
 * 根据当前访问环境返回对应的 API 地址
 * @returns {string} API 基础地址
 */
export const getApiBaseURL = () => {
  const hostname = window.location.hostname
  
  // 检查是否有手动设置的 API 地址
  const manualApiURL = sessionStorage.getItem('manual_api_url')
  if (manualApiURL) {
    console.log('使用手动设置的API地址:', manualApiURL)
    return manualApiURL
  }
  
  // 本地开发环境
  if (hostname === 'localhost' || hostname === '127.0.0.1') {
    return 'http://localhost:8081'
  }
  
  // 内网环境（192.168.20.x）
  if (hostname.indexOf('192.168.20.') === 0) {
    return 'http://192.168.20.3:8081'
  }
  
  // 公网环境
  if (hostname === '111.4.68.108') {
    return 'http://111.4.68.108:8081'
  }
  
  // 默认使用公网 API
  return 'http://111.4.68.108:8081'
}

/**
 * 测试 API 地址连通性
 * @param {string} apiUrl - 待测试的 API 地址
 * @returns {Promise<boolean>} 是否连通
 */
export const testApiConnection = async (apiUrl) => {
  try {
    const controller = new AbortController()
    const timeoutId = setTimeout(() => controller.abort(), 2000)
    
    console.log(`🔍 测试API地址: ${apiUrl}`)
    
    const response = await fetch(`${apiUrl}/actuator/health`, {
      method: 'GET',
      signal: controller.signal,
      mode: 'cors',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    clearTimeout(timeoutId)
    
    if (response.ok) {
      console.log(`✅ API地址 ${apiUrl} 连接成功`)
      return true
    } else {
      console.log(`❌ API地址 ${apiUrl} 响应异常: ${response.status}`)
      return false
    }
  } catch (error) {
    console.log(`❌ API地址 ${apiUrl} 连接失败:`, error.message)
    return false
  }
}

/**
 * 智能选择最佳 API 地址
 * 根据访问环境和连通性测试选择最佳 API 地址
 * @returns {Promise<string>} 最佳 API 地址
 */
export const getBestApiURL = async () => {
  const hostname = window.location.hostname
  
  // 检查是否有手动设置的 API 地址
  const manualApiURL = sessionStorage.getItem('manual_api_url')
  if (manualApiURL) {
    console.log('使用手动设置的API地址:', manualApiURL)
    return manualApiURL
  }
  
  // 本地开发环境
  if (hostname === 'localhost' || hostname === '127.0.0.1') {
    return 'http://localhost:8081'
  }
  
  // 内网环境
  if (hostname.indexOf('192.168.20.') === 0) {
    return 'http://192.168.20.3:8081'
  }
  
  // 公网环境
  if (hostname === '111.4.68.108') {
    console.log('检测到公网访问，使用公网API...')
    return 'http://111.4.68.108:8081'
  }
  
  // 其他情况，智能测试选择
  console.log('未知访问方式，智能测试API地址...')
  const apiUrls = [
    'http://192.168.20.3:8081',
    'http://111.4.68.108:8081',
    'http://localhost:8081'
  ]
  
  for (const apiUrl of apiUrls) {
    console.log(`测试API地址: ${apiUrl}`)
    const isAvailable = await testApiConnection(apiUrl)
    if (isAvailable) {
      console.log(`API地址 ${apiUrl} 可用，将使用此地址`)
      return apiUrl
    }
  }
  
  // 都不可用，使用内网 API 作为默认
  console.log('所有API地址都不可用，使用默认内网地址')
  return 'http://192.168.20.3:8081'
}

/**
 * 手动设置 API 地址
 * @param {string} url - API 地址
 */
export const setManualApiURL = (url) => {
  sessionStorage.setItem('manual_api_url', url)
  console.log('手动设置API地址:', url)
  window.location.reload()
}

/**
 * 清除手动设置的 API 地址
 */
export const clearManualApiURL = () => {
  sessionStorage.removeItem('manual_api_url')
  console.log('清除手动设置的API地址')
  window.location.reload()
}

/**
 * 获取视频监控 API 配置
 * @returns {Object} 视频服务配置
 */
export const getVideoApiConfig = () => {
  const hostname = window.location.hostname
  
  // 本地或内网环境
  if (hostname === 'localhost' || hostname === '127.0.0.1' || hostname.indexOf('192.168.20.') === 0) {
    return {
      loginIp: '192.168.20.250',
      port: '80'
    }
  }
  
  // 公网环境
  return {
    loginIp: '111.4.68.108',
    port: '80'
  }
}
