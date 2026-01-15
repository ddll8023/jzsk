/**
 * 字典 Store
 * 功能：全局字典数据缓存与状态管理
 * 遵循原则：KISS - 简洁的缓存机制，SOLID - 单一职责
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getDictKinds, getDictLVs } from '@/api/dict'

export const useDictStore = defineStore('dict', () => {
  // 字典缓存 Map<字典名称, 选项数组>
  const dictCache = ref(new Map())
  
  // 加载状态 Map<字典名称, boolean>
  const loadingMap = ref(new Map())

  /**
   * 获取字典选项（优先从缓存读取）
   * @param {string} name - 字典名称
   * @param {boolean} forceRefresh - 是否强制刷新
   * @param {string} type - 获取类型：'kinds'(树形) | 'lvs'(扁平)
   * @returns {Promise<Array>} 字典选项列表
   */
  const fetchDict = async (name, forceRefresh = false, type = 'lvs') => {
    // 缓存key包含类型，避免同名字典不同类型冲突
    const cacheKey = `${name}_${type}`
    
    // 有缓存且不强制刷新，直接返回
    if (!forceRefresh && dictCache.value.has(cacheKey)) {
      return dictCache.value.get(cacheKey)
    }

    // 正在加载中，等待
    if (loadingMap.value.get(cacheKey)) {
      return new Promise((resolve) => {
        const checkCache = setInterval(() => {
          if (!loadingMap.value.get(cacheKey)) {
            clearInterval(checkCache)
            resolve(dictCache.value.get(cacheKey) || [])
          }
        }, 50)
      })
    }

    // 开始加载
    loadingMap.value.set(cacheKey, true)
    
    try {
      const fetchFn = type === 'kinds' ? getDictKinds : getDictLVs
      const res = await fetchFn(name)
      
      if (res.data?.code === 200) {
        const options = res.data.data || []
        dictCache.value.set(cacheKey, options)
        return options
      }
      return []
    } catch (error) {
      console.error(`[Dict Store] 获取字典失败: ${name}`, error)
      return []
    } finally {
      loadingMap.value.set(cacheKey, false)
    }
  }

  /**
   * 从缓存获取字典选项（同步方法）
   * @param {string} name - 字典名称
   * @param {string} type - 获取类型
   * @returns {Array} 字典选项列表
   */
  const getOptions = (name, type = 'lvs') => {
    const cacheKey = `${name}_${type}`
    return dictCache.value.get(cacheKey) || []
  }

  /**
   * 检查是否正在加载
   * @param {string} name - 字典名称
   * @param {string} type - 获取类型
   * @returns {boolean} 是否加载中
   */
  const isLoading = (name, type = 'lvs') => {
    const cacheKey = `${name}_${type}`
    return loadingMap.value.get(cacheKey) || false
  }

  /**
   * 清除缓存
   * @param {string} name - 字典名称（不传则清除全部）
   */
  const clearCache = (name) => {
    if (name) {
      // 清除指定字典的所有类型缓存
      dictCache.value.delete(`${name}_kinds`)
      dictCache.value.delete(`${name}_lvs`)
    } else {
      dictCache.value.clear()
    }
  }

  return {
    dictCache,
    loadingMap,
    fetchDict,
    getOptions,
    isLoading,
    clearCache
  }
})
