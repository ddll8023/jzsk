/**
 * 预警指标 Composable
 * 功能：预警指标列表加载、筛选、CRUD操作
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive, computed } from 'vue'
import {
  getIndicatorList,
  searchIndicatorByPosition,
  getIndicatorTypes,
  getIndicatorInfo,
  saveIndicator,
  updateIndicator,
  deleteIndicator
} from '../api/warning'

/**
 * 预警指标管理
 * @returns {Object} 指标状态和方法
 */
export function useIndicator() {
  // 数据列表
  const indicatorList = ref([])
  const loading = ref(true) // 初始加载状态为 true，显示加载动画
  const error = ref(null)

  // 分页
  const pagination = reactive({
    currentPage: 1,
    pageSize: 10,
    total: 0
  })

  // 筛选条件
  const filters = reactive({
    type: '',
    position: ''
  })

  // 字典数据
  const dictData = reactive({
    types: []
  })

  // 测点→监测项映射
  const pointOptions = [
    { value: 'LJ1-1', label: 'LJ1-1', type: 'gnss' },
    { value: 'LJ1-2', label: 'LJ1-2', type: 'gnss' },
    { value: 'LJ1-3', label: 'LJ1-3', type: 'gnss' },
    { value: 'LJ1-4', label: 'LJ1-4', type: 'gnss' },
    { value: 'LT2-1', label: 'LT2-1', type: 'gnss' },
    { value: 'LT2-2', label: 'LT2-2', type: 'gnss' },
    { value: 'LT2-3', label: 'LT2-3', type: 'gnss' },
    { value: 'LT2-4', label: 'LT2-4', type: 'gnss' },
    { value: '坝前雨量水位站', label: '坝前雨量水位站', type: 'rain' },
    { value: 'mcu测站', label: 'mcu测站', type: 'mcu' }
  ]

  const monitorItemMap = {
    gnss: [
      { value: 'x位移', label: 'x位移' },
      { value: 'y位移', label: 'y位移' },
      { value: 'z位移', label: 'z位移' },
      { value: '合位移', label: '合位移' },
      { value: '水平位移', label: '水平位移' }
    ],
    rain: [
      { value: '雨量', label: '雨量' },
      { value: '水位', label: '水位' }
    ],
    mcu: [
      { value: '模数', label: '模数' },
      { value: '温度', label: '温度' },
      { value: '水位', label: '水位' },
      { value: '水压', label: '水压' },
      { value: '水位高程', label: '水位高程' }
    ]
  }

  /**
   * 根据测点获取监测项列表
   * @param {string} point - 测点名称
   * @returns {Array}
   */
  const getMonitorItems = (point) => {
    const found = pointOptions.find(item => item.value === point)
    if (!found) return []
    return monitorItemMap[found.type] || []
  }

  /**
   * 加载指标列表
   */
  const loadIndicatorList = async () => {
    loading.value = true
    error.value = null

    try {
      const params = {
        currentPage: pagination.currentPage,
        pageSize: pagination.pageSize,
        type: filters.type || ''
      }

      const { data: res } = await getIndicatorList(params)

      // 修复：响应拦截器返回完整response，需访问res.data
      if (res.data.code === 200) {
        const rawRecords = res.data.data.records || []
        // 添加序号字段
        indicatorList.value = rawRecords.map((item, index) => ({
          ...item,
          index: index + 1 + (pagination.currentPage - 1) * pagination.pageSize
        }))
        pagination.total = res.data.data.total || 0
      } else {
        error.value = res.data.message || '加载失败'
      }
    } catch (e) {
      error.value = e.message || '加载失败'
      console.error('加载指标列表失败:', e)
    } finally {
      loading.value = false
    }
  }

  /**
   * 根据测点搜索
   */
  const searchByPosition = async () => {
    if (!filters.position) {
      loadIndicatorList()
      return
    }

    loading.value = true
    error.value = null

    try {
      const params = {
        currentPage: pagination.currentPage,
        pageSize: pagination.pageSize,
        position: filters.position
      }

      const { data: res } = await searchIndicatorByPosition(params)

      // 修复：响应拦截器返回完整response，需访问res.data
      if (res.data.code === 200) {
        const rawRecords = res.data.data.records || []
        // 添加序号字段（搜索结果）
        indicatorList.value = rawRecords.map((item, index) => ({
          ...item,
          index: index + 1 + (pagination.currentPage - 1) * pagination.pageSize
        }))
        pagination.total = res.data.data.total || 0
      } else {
        error.value = res.data.message || '搜索失败'
      }
    } catch (e) {
      error.value = e.message || '搜索失败'
      console.error('搜索指标失败:', e)
    } finally {
      loading.value = false
    }
  }

  /**
   * 加载监测项类型
   */
  const loadTypes = async () => {
    try {
      const { data: res } = await getIndicatorTypes()
      if (res.code === 200 && res.data) {
        dictData.types = res.data.map(type => ({
          value: type,
          label: type
        }))
      }
    } catch (e) {
      console.error('加载监测项类型失败:', e)
    }
  }

  /**
   * 获取指标详情
   * @param {number|string} id - 指标ID
   */
  const getDetail = async (id) => {
    try {
      const { data: res } = await getIndicatorInfo(id)
      if (res.data.code === 200) {
        return res.data.data
      }
      return null
    } catch (e) {
      console.error('获取指标详情失败:', e)
      return null
    }
  }

  /**
   * 保存指标
   * @param {Object} data - 指标数据
   */
  const save = async (data) => {
    loading.value = true
    error.value = null

    try {
      const { data: res } = await saveIndicator(data)

      // 修复：响应拦截器返回完整response，需访问res.data
      if (res.data.code === 200) {
        await loadIndicatorList()
        return { success: true, message: '添加成功' }
      } else {
        error.value = res.data.message || '添加失败'
        return { success: false, message: error.value }
      }
    } catch (e) {
      error.value = e.message || '添加失败'
      console.error('添加指标失败:', e)
      return { success: false, message: error.value }
    } finally {
      loading.value = false
    }
  }

  /**
   * 更新指标
   * @param {Object} data - 指标数据
   */
  const update = async (data) => {
    loading.value = true
    error.value = null

    try {
      const { data: res } = await updateIndicator(data)

      // 修复：响应拦截器返回完整response，需访问res.data
      if (res.data.code === 200) {
        await loadIndicatorList()
        return { success: true, message: '更新成功' }
      } else {
        error.value = res.data.message || '更新失败'
        return { success: false, message: error.value }
      }
    } catch (e) {
      error.value = e.message || '更新失败'
      console.error('更新指标失败:', e)
      return { success: false, message: error.value }
    } finally {
      loading.value = false
    }
  }

  /**
   * 删除指标
   * @param {number|string} id - 指标ID
   */
  const remove = async (id) => {
    loading.value = true
    error.value = null

    try {
      const { data: res } = await deleteIndicator(id)

      // 修复：响应拦截器返回完整response，需访问res.data
      if (res.data.code === 200) {
        await loadIndicatorList()
        return { success: true, message: '删除成功' }
      } else {
        error.value = res.data.message || '删除失败'
        return { success: false, message: error.value }
      }
    } catch (e) {
      error.value = e.message || '删除失败'
      console.error('删除指标失败:', e)
      return { success: false, message: error.value }
    } finally {
      loading.value = false
    }
  }

  /**
   * 搜索
   */
  const search = () => {
    pagination.currentPage = 1
    if (filters.position) {
      searchByPosition()
    } else {
      loadIndicatorList()
    }
  }

  /**
   * 重置筛选
   */
  const resetFilters = () => {
    filters.type = ''
    filters.position = ''
    pagination.currentPage = 1
    loadIndicatorList()
  }

  /**
   * 分页变化
   */
  const handlePageChange = (page) => {
    pagination.currentPage = page
    if (filters.position) {
      searchByPosition()
    } else {
      loadIndicatorList()
    }
  }

  /**
   * 每页条数变化
   */
  const handleSizeChange = (size) => {
    pagination.pageSize = size
    pagination.currentPage = 1
    if (filters.position) {
      searchByPosition()
    } else {
      loadIndicatorList()
    }
  }

  // 是否有数据
  const isEmpty = computed(() => indicatorList.value.length === 0)

  return {
    indicatorList,
    loading,
    error,
    pagination,
    filters,
    dictData,
    pointOptions,
    isEmpty,
    getMonitorItems,
    loadIndicatorList,
    searchByPosition,
    loadTypes,
    getDetail,
    save,
    update,
    remove,
    search,
    resetFilters,
    handlePageChange,
    handleSizeChange
  }
}
