/**
 * 字典 Composable
 * 功能：提供组件级别的字典数据访问
 * 遵循原则：KISS - 简洁易用的API
 */
import { ref, computed, onMounted } from 'vue'
import { useDictStore } from '@/stores/dict'

/**
 * 字典组合式函数
 * @returns {Object} 字典相关方法
 */
export function useDict() {
  const dictStore = useDictStore()

  /**
   * 获取字典选项（响应式）
   * @param {string} name - 字典名称
   * @param {string} type - 类型：'lvs'(扁平) | 'kinds'(树形)
   * @returns {Object} { options, loading, refresh }
   */
  const getDictOptions = (name, type = 'lvs') => {
    const options = ref([])
    const loading = ref(false)

    // 加载字典数据
    const load = async (forceRefresh = false) => {
      loading.value = true
      try {
        options.value = await dictStore.fetchDict(name, forceRefresh, type)
      } finally {
        loading.value = false
      }
    }

    // 刷新方法
    const refresh = () => load(true)

    // 初始加载
    load()

    return {
      options,
      loading,
      refresh
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
