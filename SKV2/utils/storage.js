/**
 * 本地存储封装
 * 功能：统一 uni.getStorageSync/setStorageSync 操作
 */

export function setStorage(key, value) {
  uni.setStorageSync(key, value)
}

export function getStorage(key, defaultValue = null) {
  const value = uni.getStorageSync(key)
  return value !== '' && value !== undefined ? value : defaultValue
}

export function removeStorage(key) {
  uni.removeStorageSync(key)
}

export function clearStorage() {
  uni.clearStorageSync()
}

export function setObject(key, obj) {
  uni.setStorageSync(key, JSON.stringify(obj))
}

export function getObject(key, defaultValue = null) {
  const value = uni.getStorageSync(key)
  if (value === '' || value === undefined) return defaultValue
  try {
    return JSON.parse(value)
  } catch {
    return defaultValue
  }
}
