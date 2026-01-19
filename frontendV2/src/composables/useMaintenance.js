/**
 * 维护记录 Composable
 * 功能：维护记录的业务逻辑封装
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive } from 'vue'
import {
  getMaintenanceList,
  getMaintenanceInfo,
  saveMaintenance,
  updateMaintenance,
  deleteMaintenance,
  exportMaintenanceExcel
} from '@/api/maintenance'

export function useMaintenance() {
  // 状态
  const loading = ref(false)
  const tableData = ref([])
  const total = ref(0)
  
  // 查询参数
  const query = reactive({
    current: 1,
    size: 10,
    name: '',
    startTime: '',
    overTime: ''
  })

  /**
   * 获取列表数据
   * @param {Boolean} showLoading - 是否显示loading状态（默认true）
   */
  const fetchData = async (showLoading = true) => {
    if (showLoading) {
      loading.value = true
    }
    try {
      const params = {
        pageSize: query.size,
        currentPage: query.current,
        name: query.name,
        startTime: query.startTime,
        overTime: query.overTime
      }
      const res = await getMaintenanceList(params)
      if (res.data.code === 200) {
        const rawRecords = res.data.data.records || []
        // 添加序号字段
        tableData.value = rawRecords.map((item, index) => ({
          ...item,
          index: index + 1 + (query.current - 1) * query.size
        }))
        total.value = res.data.data.total || 0
      }
    } catch (error) {
      console.error('获取维护记录列表失败:', error)
      throw error
    } finally {
      if (showLoading) {
        loading.value = false
      }
    }
  }

  /**
   * 获取详情
   * @param {Number} id - 记录ID
   */
  const fetchDetail = async (id) => {
    try {
      const res = await getMaintenanceInfo(id)
      if (res.data.code === 200) {
        return res.data.data
      }
    } catch (error) {
      console.error('获取维护记录详情失败:', error)
      throw error
    }
  }

  /**
   * 保存数据（新增或更新）
   * @param {Object} data - 表单数据
   */
  const saveData = async (data) => {
    try {
      const apiFunc = data.id ? updateMaintenance : saveMaintenance
      const res = await apiFunc(data)
      if (res.data.code === 200) {
        await fetchData()
        return res
      }
    } catch (error) {
      console.error('保存维护记录失败:', error)
      throw error
    }
  }

  /**
   * 删除数据
   * @param {Number} id - 记录ID
   */
  const deleteData = async (id) => {
    try {
      const res = await deleteMaintenance(id)
      if (res.data.code === 200) {
        await fetchData()
        return res
      }
    } catch (error) {
      console.error('删除维护记录失败:', error)
      throw error
    }
  }

  /**
   * 导出Excel
   */
  const exportData = async () => {
    try {
      const res = await exportMaintenanceExcel()
      if (res.data.code === 200) {
        return res.data.data
      }
    } catch (error) {
      console.error('导出维护记录失败:', error)
      throw error
    }
  }

  /**
   * 分页变化
   * @param {Number} page - 页码
   */
  const onPageChange = (page) => {
    query.current = page
    fetchData()
  }

  /**
   * 每页条数变化
   * @param {Number} size - 每页条数
   */
  const onSizeChange = (size) => {
    query.size = size
    query.current = 1
    fetchData()
  }

  /**
   * 搜索
   */
  const onSearch = (filters) => {
    Object.assign(query, filters)
    query.current = 1
    fetchData()
  }

  /**
   * 重置搜索
   */
  const resetSearch = () => {
    query.current = 1
    query.size = 10
    query.name = ''
    query.startTime = ''
    query.overTime = ''
    fetchData()
  }

  /**
   * 格式化日期时间
   * @param {String} dateStr - 日期字符串
   */
  const formatDateTime = (dateStr) => {
    if (!dateStr) return '-'
    return dateStr
  }

  return {
    loading,
    tableData,
    total,
    query,
    fetchData,
    fetchDetail,
    saveData,
    deleteData,
    exportData,
    onPageChange,
    onSizeChange,
    onSearch,
    resetSearch,
    formatDateTime
  }
}
