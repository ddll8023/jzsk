/**
 * 应用配置模块
 * 功能：统一 API 基础地址、超时、分页等配置
 */

const devBaseUrl = 'http://192.168.1.100:8081'
const prodBaseUrl = 'http://111.4.68.108:8081'

const baseUrl = process.env.NODE_ENV === 'development' ? devBaseUrl : prodBaseUrl

export default {
  baseUrl,
  timeout: 15000,
  pageSize: 10,
  maxImageSize: 5,
}
