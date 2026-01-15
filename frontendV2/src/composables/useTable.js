/**
 * 表格 Composable
 * 功能：表格数据加载、分页、搜索公共逻辑
 */
import { ref, reactive, computed } from 'vue'

/**
 * 创建表格的公共状态和方法
 * @param {Object} options - 配置选项
 * @param {Function} options.fetchFn - 数据获取函数
 * @param {number} options.pageSize - 每页条数，默认 10
 * @returns {Object} 表格状态和方法
 */
export function useTable(options = {}) {
  const { fetchFn = null, pageSize = 10 } = options

  // 表格数据
  const tableData = ref([])
  const loading = ref(false)
  const error = ref(null)

  // 分页
  const pagination = reactive({
    current: 1,
    pageSize: pageSize,
    total: 0
  })

  // 搜索条件
  const searchParams = reactive({})

  // 是否有数据
  const isEmpty = computed(() => tableData.value.length === 0)

  /**
   * 加载数据
   * @param {Object} params - 额外参数
   */
  const loadData = async (params = {}) => {
    if (!fetchFn) {
      console.warn('未配置 fetchFn')
      return
    }

    loading.value = true
    error.value = null

    try {
      const res = await fetchFn({
        page: pagination.current,
        pageSize: pagination.pageSize,
        ...searchParams,
        ...params
      })

      if (res.data && res.data.code === 200) {
        tableData.value = res.data.data.list || res.data.data || []
        pagination.total = res.data.data.total || tableData.value.length
      } else {
        error.value = res.data?.message || '加载失败'
      }
    } catch (e) {
      error.value = e.message || '加载失败'
      console.error('表格数据加载失败:', e)
    } finally {
      loading.value = false
    }
  }

  /**
   * 刷新数据
   */
  const refresh = () => {
    loadData()
  }

  /**
   * 搜索
   * @param {Object} params - 搜索参数
   */
  const search = (params = {}) => {
    Object.assign(searchParams, params)
    pagination.current = 1
    loadData()
  }

  /**
   * 重置搜索
   */
  const resetSearch = () => {
    Object.keys(searchParams).forEach(key => {
      delete searchParams[key]
    })
    pagination.current = 1
    loadData()
  }

  /**
   * 分页变化
   * @param {number} page - 页码
   */
  const handlePageChange = (page) => {
    pagination.current = page
    loadData()
  }

  /**
   * 每页条数变化
   * @param {number} size - 每页条数
   */
  const handleSizeChange = (size) => {
    pagination.pageSize = size
    pagination.current = 1
    loadData()
  }

  return {
    tableData,
    loading,
    error,
    pagination,
    searchParams,
    isEmpty,
    loadData,
    refresh,
    search,
    resetSearch,
    handlePageChange,
    handleSizeChange
  }
}
