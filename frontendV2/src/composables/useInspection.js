/**
 * 巡检记录 Composable
 * 功能：巡检记录的业务逻辑封装
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive } from 'vue'
import {
  getInspectionList,
  getInspectionInfo,
  saveInspection,
  updateInspection,
  deleteInspection,
  exportInspectionExcel
} from '@/api/inspection'

export function useInspection() {
  // 状态
  const loading = ref(false)
  const tableData = ref([])
  const total = ref(0)
  
  // 查询参数
  const query = reactive({
    current: 1,
    size: 10,
    project: '',
    abnormal: '',
    person: '',
    solve: '',
    startTime: '',
    endTime: ''
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
        project: query.project,
        abnormal: query.abnormal,
        person: query.person,
        solve: query.solve,
        startTime: query.startTime,
        endTime: query.endTime
      }
      const res = await getInspectionList(params)
      console.log('🔍 [useInspection] API响应数据:', res)
      
      // 修复：响应拦截器返回完整response对象，需要访问res.data.data
      if (res.data.code === 200) {
        const rawRecords = res.data.data.records || []
        console.log('🔍 [useInspection] 原始记录数据:', rawRecords)
        console.log('🔍 [useInspection] 第一条记录字段:', rawRecords[0])
        
        // 字段映射：后端字段名与表格列key一致，直接添加序号
        tableData.value = rawRecords.map((item, index) => ({
          ...item, // 保留所有原始字段
          index: index + 1 + (query.current - 1) * query.size // 添加序号
        }))
        console.log('🔍 [useInspection] 映射后数据:', tableData.value)
        total.value = res.data.data.total || 0
      }
    } catch (error) {
      console.error('获取巡检记录列表失败:', error)
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
      const res = await getInspectionInfo(id)
      if (res.data.code === 200) {
        return res.data.data
      }
    } catch (error) {
      console.error('获取巡检记录详情失败:', error)
      throw error
    }
  }

  /**
   * 保存数据（新增或更新）
   * @param {Object} data - 表单数据
   */
  const saveData = async (data) => {
    try {
      const apiFunc = data.id ? updateInspection : saveInspection
      const res = await apiFunc(data)
      if (res.data.code === 200) {
        await fetchData()
        return res
      }
    } catch (error) {
      console.error('保存巡检记录失败:', error)
      throw error
    }
  }

  /**
   * 删除数据
   * @param {Number} id - 记录ID
   */
  const deleteData = async (id) => {
    try {
      const res = await deleteInspection(id)
      if (res.data.code === 200) {
        await fetchData()
        return res
      }
    } catch (error) {
      console.error('删除巡检记录失败:', error)
      throw error
    }
  }

  /**
   * 导出Excel
   */
  const exportData = async () => {
    try {
      const res = await exportInspectionExcel()
      if (res.data.code === 200) {
        return res.data.data
      }
    } catch (error) {
      console.error('导出巡检记录失败:', error)
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
    query.project = ''
    query.abnormal = ''
    query.person = ''
    query.solve = ''
    query.startTime = ''
    query.endTime = ''
    fetchData()
  }

  /**
   * 格式化日期
   * @param {String} dateStr - 日期字符串
   */
  const formatDate = (dateStr) => {
    if (!dateStr) return '-'
    return dateStr.split(' ')[0]
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
    formatDate,
    formatDateTime
  }
}
