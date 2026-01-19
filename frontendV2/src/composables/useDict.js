/**
 * 字典 Composable
 * 功能：提供组件级别的字典数据访问
 * 遵循原则：KISS - 简洁易用的API，直接返回数据
 * 修改记录：简化getDictOptions返回值，从对象改为直接返回数组（符合YAGNI原则）
 */
import { useDictStore } from '@/stores/dict'

/**
 * 字典组合式函数
 * @returns {Object} 字典相关方法
 */
export function useDict() {
  const dictStore = useDictStore()

  /**
   * 获取字典选项（简化版 - 直接返回数组）
   * @param {string} name - 字典名称
   * @param {string} type - 类型：'lvs'(扁平) | 'kinds'(树形)
   * @returns {Promise<Array>} 字典选项数组 [{ label, value }]
   * 
   * 设计原则：KISS - 简化API，直接返回数据而非包装对象
   * 理由：当前业务场景未使用loading/refresh，遵循YAGNI原则
   */
  const getDictOptions = async (name, type = 'lvs') => {
    try {
      return await dictStore.fetchDict(name, false, type)
    } catch (error) {
      console.error(`获取字典[${name}]失败:`, error)
      return []
    }
  }

  /**
   * 根据值获取标签
   * @param {string} name - 字典名称
   * @param {string|number} value - 字典值
   * @param {string} type - 类型
   * @returns {string} 标签文本
   */
  const getDictLabel = (name, value, type = 'lvs') => {
    const options = dictStore.getOptions(name, type)
    const item = options.find(opt => opt.value === value || opt.value === String(value))
    return item?.label || value
  }

  /**
   * 批量预加载字典
   * @param {Array<string>} names - 字典名称数组
   * @param {string} type - 类型
   */
  const preloadDicts = async (names, type = 'lvs') => {
    await Promise.all(names.map(name => dictStore.fetchDict(name, false, type)))
  }

  /**
   * 刷新指定字典
   * @param {string} name - 字典名称
   * @param {string} type - 类型
   */
  const refreshDict = async (name, type = 'lvs') => {
    await dictStore.fetchDict(name, true, type)
  }

  /**
   * 清除字典缓存
   * @param {string} name - 字典名称（不传则清除全部）
   */
  const clearDictCache = (name) => {
    dictStore.clearCache(name)
  }

  return {
    getDictOptions,
    getDictLabel,
    preloadDicts,
    refreshDict,
    clearDictCache
  }
}
