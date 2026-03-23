/**
 * 预警指标 Composable
 * 功能：预警指标列表加载、筛选、CRUD操作
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive, computed } from 'vue'
import {
  getIndicatorList,
  getIndicatorOptions,
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

  const pointOptions = ref([])
  const positionBindings = ref({})

  const normalizeOption = (option) => {
    const value = option?.value ?? ''
    const label = option?.label ?? value

    return {
      label,
      value
    }
  }

  const ensureOption = (options, value, label = value) => {
    if (!value) return
    if (!options.some(item => item.value === value)) {
      options.push({ label, value })
    }
  }

  const buildBindingMap = (bindings = []) => {
    return bindings.reduce((accumulator, binding) => {
      if (!binding?.position) {
        return accumulator
      }

      accumulator[binding.position] = (binding.typeOptions || []).map(normalizeOption)
      return accumulator
    }, {})
  }

  const ensureBinding = (position, type) => {
    if (!position) return

    ensureOption(pointOptions.value, position)

    if (!positionBindings.value[position]) {
      positionBindings.value = {
        ...positionBindings.value,
        [position]: []
      }
    }

    if (type) {
      ensureOption(positionBindings.value[position], type)
      ensureOption(dictData.types, type)
    }
  }

  /**
   * 根据测点获取监测项列表
   * @param {string} point - 测点名称
   * @returns {Array}
   */
  const getMonitorItems = (point) => {
    if (!point) return []
    return positionBindings.value[point] || []
  }

  /**
   * 加载页面选项
   */
  const loadIndicatorOptions = async () => {
    try {
      const { data: res } = await getIndicatorOptions()

      if (res.code === 200 && res.data) {
        pointOptions.value = (res.data.positionOptions || []).map(normalizeOption)
        dictData.types = (res.data.typeOptions || []).map(normalizeOption)
        positionBindings.value = buildBindingMap(res.data.bindings)
      }
    } catch (e) {
      console.error('加载预警指标选项失败:', e)
    }
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
        type: filters.type || '',
        position: filters.position?.trim() || ''
      }

      const { data: res } = await getIndicatorList(params)

      if (res.code === 200) {
        const rawRecords = res.data.list || []
        indicatorList.value = rawRecords.map((item, index) => ({
          ...item,
          index: index + 1 + (pagination.currentPage - 1) * pagination.pageSize
        }))
        pagination.total = res.data.total || 0
      } else {
        error.value = res.message || '加载失败'
      }
    } catch (e) {
      error.value = e.message || '加载失败'
      console.error('加载指标列表失败:', e)
    } finally {
      loading.value = false
    }
  }

  /**
   * 获取指标详情
   */
  const getDetail = async (id) => {
    try {
      const { data: res } = await getIndicatorInfo(id)
      if (res.code === 200) {
        ensureBinding(res.data?.position, res.data?.type)
        return res.data
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

      if (res.code === 200) {
        await loadIndicatorOptions()
        await loadIndicatorList()
        return { success: true, message: '添加成功' }
      } else {
        error.value = res.message || '添加失败'
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

      if (res.code === 200) {
        await loadIndicatorOptions()
        await loadIndicatorList()
        return { success: true, message: '更新成功' }
      } else {
        error.value = res.message || '更新失败'
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

      if (res.code === 200) {
        await loadIndicatorOptions()
        await loadIndicatorList()
        return { success: true, message: '删除成功' }
      } else {
        error.value = res.message || '删除失败'
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
    loadIndicatorList()
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
    loadIndicatorList()
  }

  /**
   * 每页条数变化
   */
  const handleSizeChange = (size) => {
    pagination.pageSize = size
    pagination.currentPage = 1
    loadIndicatorList()
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
    loadIndicatorOptions,
    loadIndicatorList,
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
