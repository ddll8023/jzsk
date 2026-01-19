/**
 * 预警设施 Composable
 * 功能：封装预警设施的CRUD逻辑
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive } from 'vue'
import {
  getWarningFacilityList,
  getWarningFacilityInfo,
  saveWarningFacility,
  updateWarningFacility,
  deleteWarningFacility
} from '@/api/engineering'

export function useWarningFacility() {
  // 数据状态
  const loading = ref(false)
  const facilityList = ref([])

  // 分页状态
  const pagination = reactive({
    currentPage: 1,
    pageSize: 10,
    total: 0
  })

  // 表单数据
  const formData = reactive({
    id: null,
    facilityName: '',
    type: '',
    location: '',
    status: '',
    manager: '',
    lastUpdate: ''
  })

  /**
   * 格式化日期时间显示
   * @param {string} dateStr - 日期字符串
   * @returns {string} 格式化后的日期时间
   */
  const formatDateTime = (dateStr) => {
    if (!dateStr) return '-'
    
    try {
      let date
      
      // 处理数组格式 [2024, 1, 18, 10, 30, 0]
      if (Array.isArray(dateStr)) {
        const [y, M, D, h = 0, m = 0, s = 0] = dateStr
        date = new Date(y, M - 1, D, h, m, s)
      }
      // 处理对象格式 { year, monthValue, dayOfMonth, hour, minute, second }
      else if (typeof dateStr === 'object' && dateStr.year) {
        const { year, monthValue, dayOfMonth, hour = 0, minute = 0, second = 0 } = dateStr
        date = new Date(year, monthValue - 1, dayOfMonth, hour, minute, second)
      }
      // 处理字符串格式
      else if (typeof dateStr === 'string') {
        date = new Date(dateStr.replace('T', ' '))
      }
      // 处理时间戳
      else if (typeof dateStr === 'number') {
        date = new Date(dateStr)
      }
      else {
        return String(dateStr)
      }

      if (isNaN(date.getTime())) return String(dateStr)

      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')

      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    } catch (error) {
      console.error('日期格式化失败:', error)
      return String(dateStr)
    }
  }

  /**
   * 转换为 datetime-local 输入格式
   * @param {string} dateStr - 日期字符串
   * @returns {string} YYYY-MM-DDTHH:mm 格式
   */
  const toDatetimeLocal = (dateStr) => {
    if (!dateStr) return ''
    try {
      const formatted = formatDateTime(dateStr)
      if (formatted === '-') return ''
      // 转换为 datetime-local 格式：YYYY-MM-DDTHH:mm
      return formatted.replace(' ', 'T').slice(0, 16)
    } catch (error) {
      console.error('日期转换失败:', error)
      return ''
    }
  }

  /**
   * 加载设施列表
   * 兼容多种后端响应格式
   */
  const loadFacilityList = async () => {
    loading.value = true
    try {
      const res = await getWarningFacilityList({
        current: pagination.currentPage,
        pageSize: pagination.pageSize
      })

      // 兼容多种响应格式
      let records = []
      let total = 0

      // 格式1: 直接返回数组 res.data = [...]
      if (Array.isArray(res.data)) {
        records = res.data
        total = res.data.length
      }
      // 格式2: 分页对象 res.data = { records: [...], total: 0 }
      else if (res.data && res.data.records) {
        records = res.data.records
        total = res.data.total || 0
      }
      // 格式3: 标准响应 res.data = { code: 200, data: { records: [...], total: 0 } }
      else if (res.data && res.data.code === 200 && res.data.data) {
        const data = res.data.data
        if (Array.isArray(data)) {
          records = data
          total = data.length
        } else {
          records = data.records || []
          total = data.total || 0
        }
      }
      // 格式4: 嵌套data res.data.data = [...]
      else if (res.data && Array.isArray(res.data.data)) {
        records = res.data.data
        total = res.data.data.length
      }
      else {
        console.warn('未识别的响应格式:', res.data)
      }

      facilityList.value = records
      pagination.total = total
    } catch (error) {
      console.error('加载设施列表失败:', error)
      facilityList.value = []
      pagination.total = 0
    } finally {
      loading.value = false
    }
  }

  /**
   * 加载设施详情
   * @param {number|string} id - 设施ID
   */
  const loadFacilityInfo = async (id) => {
    loading.value = true
    try {
      const res = await getWarningFacilityInfo(id)
      
      // 兼容多种响应格式
      let data = null
      if (res.data && res.data.code === 200) {
        data = res.data.data
      } else if (res.data && !res.data.code) {
        data = res.data
      }

      if (data) {
        Object.assign(formData, {
          ...data,
          lastUpdate: toDatetimeLocal(data.lastUpdate)
        })
        return data
      } else {
        throw new Error('获取详情失败')
      }
    } catch (error) {
      console.error('加载设施详情失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  /**
   * 保存设施（新增或更新）
   * @param {Object} data - 设施数据
   */
  const saveFacility = async (data) => {
    loading.value = true
    try {
      // 转换时间格式为后端需要的格式
      const submitData = {
        ...data,
        lastUpdate: data.lastUpdate ? data.lastUpdate.replace('T', ' ') + ':00' : ''
      }

      const apiFunc = data.id ? updateWarningFacility : saveWarningFacility
      const res = await apiFunc(submitData)

      // 兼容多种响应格式
      if (res.data === true || res.data.code === 200 || res.data.success === true) {
        return { success: true, message: data.id ? '更新成功' : '新增成功' }
      } else {
        const errorMsg = res.data.message || res.data.msg || '保存失败'
        throw new Error(errorMsg)
      }
    } catch (error) {
      console.error('保存设施失败:', error)
      return { success: false, message: error.message || '保存失败' }
    } finally {
      loading.value = false
    }
  }

  /**
   * 删除设施
   * @param {number|string} id - 设施ID
   */
  const deleteFacility = async (id) => {
    loading.value = true
    try {
      const res = await deleteWarningFacility(id)
      
      // 兼容多种响应格式
      if (res.data === true || res.data.code === 200 || res.data.success === true) {
        return { success: true, message: '删除成功' }
      } else {
        const errorMsg = res.data.message || res.data.msg || '删除失败'
        throw new Error(errorMsg)
      }
    } catch (error) {
      console.error('删除设施失败:', error)
      return { success: false, message: error.message || '删除失败' }
    } finally {
      loading.value = false
    }
  }

  /**
   * 重置表单
   */
  const resetForm = () => {
    Object.assign(formData, {
      id: null,
      facilityName: '',
      type: '',
      location: '',
      status: '',
      manager: '',
      lastUpdate: ''
    })
  }

  /**
   * 处理分页变化
   */
  const handlePageChange = (page) => {
    pagination.currentPage = page
    loadFacilityList()
  }

  /**
   * 处理每页条数变化
   */
  const handleSizeChange = (size) => {
    pagination.pageSize = size
    pagination.currentPage = 1
    loadFacilityList()
  }

  return {
    // 状态
    loading,
    facilityList,
    pagination,
    formData,

    // 方法
    loadFacilityList,
    loadFacilityInfo,
    saveFacility,
    deleteFacility,
    resetForm,
    handlePageChange,
    handleSizeChange,
    formatDateTime,
    toDatetimeLocal
  }
}
