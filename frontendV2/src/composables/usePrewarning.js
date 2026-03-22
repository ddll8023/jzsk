/**
 * 预警信息 Composable
 * 功能：预警信息列表加载、筛选、解除预警
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive, computed } from 'vue'
import { getWarningList, updateWarning } from '../api/warning'
import { useDict } from './useDict'

/**
 * 预警信息管理
 * @returns {Object} 预警状态和方法
 */
export function usePrewarning() {
  const { getDictOptions } = useDict()

  // 数据列表
  const warningList = ref([])
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
    position: '',
    status: '',
    level: '',
    type: '',
    dateRange: ['', '']
  })

  // 字典数据
  const dictData = reactive({
    positions: [],
    statuses: [],
    levels: [],
    types: []
  })

  /**
   * 加载预警列表
   */
  const loadWarningList = async () => {
    loading.value = true
    error.value = null

    try {
      console.log('🔍 开始加载预警列表...')
      const params = {
        currentPage: pagination.currentPage,
        pageSize: pagination.pageSize,
        position: filters.position || '',
        status: filters.status || '',
        level: filters.level || '',
        type: filters.type || '',
        startTime: filters.dateRange[0] || '',
        endTime: filters.dateRange[1] || ''
      }
      console.log('   查询参数:', params)

      const { data: res } = await getWarningList(params)
      console.log('   API响应:', res)

      // 修复：响应拦截器返回完整response，实际数据在res中
      if (res.code === 200) {
        const rawRecords = res.data.records || []
        // 添加序号字段
        warningList.value = rawRecords.map((item, index) => ({
          ...item,
          index: index + 1 + (pagination.currentPage - 1) * pagination.pageSize
        }))
        pagination.total = res.data.total || 0
        console.log(`✅ 加载成功: ${warningList.value.length} 条记录`)
      } else {
        error.value = res.message || '加载失败'
        console.error('❌ 加载失败:', error.value)
      }
    } catch (e) {
      error.value = e.message || '网络请求失败'
      console.error('❌ 加载预警列表异常:', e)
      console.error('   错误详情:', {
        message: e.message,
        response: e.response?.data,
        status: e.response?.status
      })
    } finally {
      loading.value = false
    }
  }

  /**
   * 解除预警
   * @param {Object} warning - 预警数据
   */
  const resolveWarning = async (warning) => {
    loading.value = true
    error.value = null

    try {
      const data = {
        id: warning.id,
        position: warning.position,
        project: warning.project,
        content: warning.content,
        type: warning.type,
        level: warning.level,
        status: '已解除',
        longitude: warning.longitude,
        latitude: warning.latitude,
        startTime: warning.startTime,
        overTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
      }

      const { data: res } = await updateWarning(data)

      // 修复：响应拦截器返回完整response，实际数据在res中
      if (res.code === 200) {
        await loadWarningList()
        return { success: true, message: '预警已解除' }
      } else {
        error.value = res.message || '解除失败'
        return { success: false, message: error.value }
      }
    } catch (e) {
      error.value = e.message || '解除失败'
      console.error('解除预警失败:', e)
      return { success: false, message: error.value }
    } finally {
      loading.value = false
    }
  }

  /**
   * 加载字典数据
   */
  const loadDictData = async () => {
    try {
      console.log('🔍 开始加载字典数据...')

      dictData.statuses = await getDictOptions('预警状态')
      console.log('   预警状态:', dictData.statuses.length, '项')

      dictData.levels = await getDictOptions('预警等级')
      console.log('   预警等级:', dictData.levels.length, '项')

      dictData.types = await getDictOptions('预警类型')
      console.log('   预警类型:', dictData.types.length, '项')

      // 预警地点（写死）
      dictData.positions = [
        { value: 'LJ1-1', label: 'LJ1-1' },
        { value: 'LJ1-2', label: 'LJ1-2' },
        { value: 'LJ1-3', label: 'LJ1-3' },
        { value: 'LJ1-4', label: 'LJ1-4' },
        { value: 'LT2-1', label: 'LT2-1' },
        { value: 'LT2-2', label: 'LT2-2' },
        { value: 'LT2-3', label: 'LT2-3' },
        { value: 'LT2-4', label: 'LT2-4' },
        { value: '坝前雨量水位站（新站）', label: '坝前雨量水位站（新站）' },
        { value: 'mcu测站', label: 'mcu测站' }
      ]
      console.log('   预警地点:', dictData.positions.length, '项')
      console.log('✅ 字典数据加载完成')
    } catch (e) {
      console.error('❌ 加载字典数据失败:', e)
      console.error('   错误详情:', {
        message: e.message,
        response: e.response?.data
      })
    }
  }

  /**
   * 搜索
   */
  const search = () => {
    pagination.currentPage = 1
    loadWarningList()
  }

  /**
   * 重置筛选
   */
  const resetFilters = () => {
    filters.position = ''
    filters.status = ''
    filters.level = ''
    filters.type = ''
    filters.dateRange = ['', '']
    pagination.currentPage = 1
    loadWarningList()
  }

  /**
   * 分页变化
   */
  const handlePageChange = (page) => {
    pagination.currentPage = page
    loadWarningList()
  }

  /**
   * 每页条数变化
   */
  const handleSizeChange = (size) => {
    pagination.pageSize = size
    pagination.currentPage = 1
    loadWarningList()
  }

  // 是否有数据
  const isEmpty = computed(() => warningList.value.length === 0)

  /**
   * 获取预警统计数据（用于地图面板展示）
   * 修复：支持懒加载模式，仅在需要时请求数据
   * Source: 新增功能
   */
  const warningStats = ref({
    total: 0,           // 总预警数
    unresolved: 0,      // 未解除数
    serious: 0,         // 严重预警数
    latest: null        // 最新预警
  })

  const loadWarningStats = async () => {
    try {
      console.log('🔍 加载预警统计数据...')
      
      // 请求最新20条预警（不分页）
      const params = {
        currentPage: 1,
        pageSize: 20,
        position: '',
        status: '',
        level: '',
        type: '',
        startTime: '',
        endTime: ''
      }
      
      const { data: res } = await getWarningList(params)
      
      if (res.code === 200) {
        const records = res.data.records || []
        
        // 统计数据
        warningStats.value = {
          total: res.data.total || 0,
          unresolved: records.filter(w => w.status === '未解除').length,
          serious: records.filter(w => ['严重', '特别严重'].includes(w.level)).length,
          latest: records.length > 0 ? records[0] : null
        }
        
        console.log('✅ 预警统计加载成功:', warningStats.value)
      }
    } catch (e) {
      console.error('❌ 加载预警统计失败:', e)
    }
  }

  return {
    warningList,
    loading,
    error,
    pagination,
    filters,
    dictData,
    isEmpty,
    warningStats,
    loadWarningList,
    resolveWarning,
    loadDictData,
    loadWarningStats,
    search,
    resetFilters,
    handlePageChange,
    handleSizeChange
  }
}
