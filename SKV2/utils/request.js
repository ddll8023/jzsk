/**
 * 网络请求封装
 * 功能：统一处理 baseUrl、token、响应解析、401 拦截
 */
import config from '@/config/index.js'
import { getStorage, removeStorage } from '@/utils/storage.js'

export function request(options) {
  return new Promise((resolve, reject) => {
    const token = getStorage('token')
    uni.request({
      url: `${config.baseUrl}${options.url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        Authorization: token ? `Bearer ${token}` : '',
        ...options.header,
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 0 || res.data.code === 200) {
            resolve(res.data)
          } else {
            uni.showToast({
              title: res.data.message || '请求失败',
              icon: 'none',
            })
            reject(new Error(res.data.message))
          }
        } else if (res.statusCode === 401) {
          removeStorage('token')
          uni.reLaunch({ url: '/pages/login/login' })
          reject(new Error('未授权'))
        } else {
          uni.showToast({
            title: `请求失败: ${res.statusCode}`,
            icon: 'none',
          })
          reject(new Error(`请求失败: ${res.statusCode}`))
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '网络连接失败',
          icon: 'none',
        })
        reject(err)
      },
    })
  })
}

export const get = (url, params = {}, header = {}) =>
  request({ url, method: 'GET', data: params, header })

export const post = (url, data = {}, header = {}) =>
  request({ url, method: 'POST', data, header })

export const put = (url, data = {}, header = {}) =>
  request({ url, method: 'PUT', data, header })

export const del = (url, data = {}, header = {}) =>
  request({ url, method: 'DELETE', data, header })
