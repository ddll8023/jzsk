/**
 * 值班日志Composable
 * 功能：管理值班日志的状态和业务逻辑
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive } from 'vue'
import {
  getDutyLogPage,
  saveDutyLog,
  updateDutyLog,
  deleteDutyLog,
  batchDeleteDutyLog
} from '@/api/report'

export function useDutyLog() {
  // 状态
  const loading = ref(false)
  const tableData = ref([])
  const total = ref(0)
  const selectedRows = ref([])

  // 查询参数
  const query = reactive({
    current: 1,
    size: 10,
    startDate: '',
    endDate: ''
  })

  /**
   * 获取列表数据
   */
  const fetchData = async () => {
    loading.value = true
    try {
      const params = {
        current: query.current,
        size: query.size
      }
      
      // 添加日期范围参数
      if (query.startDate && query.endDate) {
        params.startDate = query.startDate
        params.endDate = query.endDate
      }

      const res = await getDutyLogPage(params)
      const rawRecords = res.data.data.list || []
      // 添加序号字段
      tableData.value = rawRecords.map((item, index) => ({
        ...item,
        index: index + 1 + (query.current - 1) * query.size
      }))
      total.value = res.data.data.total || 0
    } catch (error) {
      console.error('获取值班日志列表失败:', error)
      tableData.value = []
      total.value = 0
    } finally {
      loading.value = false
    }
  }

  /**
   * 保存数据（新增或编辑）
   * @param {Object} form - 表单数据
   * @returns {Promise<boolean>}
   */
  const saveData = async (form) => {
    try {
      if (form.dutyLogId) {
        await updateDutyLog(form.dutyLogId, form)
      } else {
        await saveDutyLog(form)
      }
      await fetchData()
      return true
    } catch (error) {
      console.error('保存值班日志失败:', error)
      throw error
    }
  }

  /**
   * 删除单条数据
   * @param {Number|String} id - 值班日志ID
   * @returns {Promise<boolean>}
   */
  const deleteData = async (id) => {
    try {
      await deleteDutyLog(id)
      await fetchData()
      return true
    } catch (error) {
      console.error('删除值班日志失败:', error)
      throw error
    }
  }

  /**
   * 批量删除
   * @param {Array} ids - ID数组
   * @returns {Promise<boolean>}
   */
  const batchDelete = async (ids) => {
    try {
      await batchDeleteDutyLog(ids)
      await fetchData()
      return true
    } catch (error) {
      console.error('批量删除值班日志失败:', error)
      throw error
    }
  }

  /**
   * 处理分页变化
   * @param {Number} page - 页码
   */
  const onPageChange = (page) => {
    query.current = page
    fetchData()
  }

  /**
   * 处理每页条数变化
   * @param {Number} size - 每页条数
   */
  const onSizeChange = (size) => {
    query.size = size
    query.current = 1
    fetchData()
  }

  /**
   * 处理搜索
   * @param {String} startDate - 开始日期
   * @param {String} endDate - 结束日期
   */
  const onSearch = (startDate, endDate) => {
    query.startDate = startDate
    query.endDate = endDate
    query.current = 1
    fetchData()
  }

  /**
   * 重置搜索条件
   */
  const resetSearch = () => {
    query.startDate = ''
    query.endDate = ''
    query.current = 1
    fetchData()
  }

  /**
   * 格式化日期
   * 处理后端返回的数组格式 [year, month, day]
   * @param {*} value - 日期值
   * @returns {String}
   */
  const formatDate = (value) => {
    if (!value) return ''
    
    let d
    if (Array.isArray(value)) {
      const [y, M, D] = value
      d = new Date(y, M - 1, D)
    } else if (typeof value === 'object' && value.year) {
      const { year, monthValue, dayOfMonth } = value
      d = new Date(year, monthValue - 1, dayOfMonth)
    } else if (typeof value === 'string') {
      d = new Date(value.replace('T', ' '))
    } else if (typeof value === 'number') {
      d = new Date(value)
    } else {
      return String(value)
    }

    if (isNaN(d.getTime())) return String(value)

    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')

    return `${year}-${month}-${day}`
  }

  /**
   * 格式化日期时间
   * @param {*} value - 日期时间值
   * @returns {String}
   */
  const formatDateTime = (value) => {
    if (!value) return ''
    
    let d
    if (Array.isArray(value)) {
      const [y, M, D, h = 0, m = 0, s = 0] = value
      d = new Date(y, M - 1, D, h, m, s)
    } else if (typeof value === 'object' && value.year) {
      const { year, monthValue, dayOfMonth, hour = 0, minute = 0, second = 0 } = value
      d = new Date(year, monthValue - 1, dayOfMonth, hour, minute, second)
    } else if (typeof value === 'string') {
      d = new Date(value.replace('T', ' '))
    } else if (typeof value === 'number') {
      d = new Date(value)
    } else {
      return String(value)
    }

    if (isNaN(d.getTime())) return String(value)

    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    const hours = String(d.getHours()).padStart(2, '0')
    const minutes = String(d.getMinutes()).padStart(2, '0')
    const seconds = String(d.getSeconds()).padStart(2, '0')

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
  }

  return {
    loading,
    tableData,
    total,
    selectedRows,
    query,
    fetchData,
    saveData,
    deleteData,
    batchDelete,
    onPageChange,
    onSizeChange,
    onSearch,
    resetSearch,
    formatDate,
    formatDateTime
  }
}
