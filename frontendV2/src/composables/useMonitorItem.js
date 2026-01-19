/**
 * 测项信息管理 Composable
 * 功能：封装测项信息的CRUD逻辑
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive } from 'vue'
import {
  getMonitorItemList,
  getMonitorItemInfo,
  saveMonitorItem,
  updateMonitorItem,
  deleteMonitorItem,
  getMonitorItemNames,
  exportMonitorItemExcel
} from '@/api/engineering'

export function useMonitorItem() {
  // 数据状态
  const loading = ref(true)
  const itemList = ref([])
  const itemNames = ref([])
  const total = ref(0)

  // 查询参数
  const query = reactive({
    currentPage: 1,
    pageSize: 10,
    name: ''
  })

  // 表单数据
  const formData = reactive({
    id: null,
    number: '',
    name: '',
    unit: ''
  })

  /**
   * 加载测项列表
   */
  const loadItemList = async () => {
    loading.value = true
    try {
      const res = await getMonitorItemList(query)
      if (res.data.code === 200) {
        itemList.value = res.data.data.records || []
        total.value = res.data.data.total || 0
      } else {
        throw new Error(res.data.message || '获取数据失败')
      }
    } catch (error) {
      console.error('加载测项列表失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  /**
   * 加载测项名称列表（用于筛选下拉）
   */
  const loadItemNames = async () => {
    try {
      const res = await getMonitorItemNames()
      if (res.data.code === 200) {
        itemNames.value = (res.data.data || []).map(name => ({
          label: name,
          value: name
        }))
      }
    } catch (error) {
      console.error('加载测项名称失败:', error)
    }
  }

  /**
   * 加载测项详情
   */
  const loadItemInfo = async (id) => {
    loading.value = true
    try {
      const res = await getMonitorItemInfo(id)
      if (res.data.code === 200) {
        Object.assign(formData, res.data.data)
        return res.data.data
      } else {
        throw new Error(res.data.message || '获取详情失败')
      }
    } catch (error) {
      console.error('加载测项详情失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  /**
   * 保存测项（新增或更新）
   */
  const saveItem = async (data) => {
    loading.value = true
    try {
      const apiFunc = data.id ? updateMonitorItem : saveMonitorItem
      const res = await apiFunc(data)
      if (res.data.code === 200) {
        return { success: true, message: data.id ? '更新成功' : '新增成功' }
      } else {
        throw new Error(res.data.message || '保存失败')
      }
    } catch (error) {
      console.error('保存测项失败:', error)
      return { success: false, message: error.message || '保存失败' }
    } finally {
      loading.value = false
    }
  }

  /**
   * 删除测项
   */
  const deleteItem = async (id) => {
    loading.value = true
    try {
      const res = await deleteMonitorItem(id)
      if (res.data.code === 200) {
        return { success: true, message: '删除成功' }
      } else {
        throw new Error(res.data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除测项失败:', error)
      return { success: false, message: error.message || '删除失败' }
    } finally {
      loading.value = false
    }
  }

  /**
   * 搜索
   */
  const search = () => {
    query.currentPage = 1
    loadItemList()
  }

  /**
   * 重置筛选条件
   */
  const resetFilters = () => {
    query.name = ''
    search()
  }

  /**
   * 分页变化
   */
  const handlePageChange = (page) => {
    query.currentPage = page
    loadItemList()
  }

  /**
   * 每页条数变化
   */
  const handleSizeChange = (size) => {
    query.pageSize = size
    query.currentPage = 1
    loadItemList()
  }

  /**
   * 重置表单
   */
  const resetForm = () => {
    Object.assign(formData, {
      id: null,
      number: '',
      name: '',
      unit: ''
    })
  }

  /**
   * 导出CSV
   */
  const exportData = async () => {
    try {
      const res = await exportMonitorItemExcel()
      if (res.data.code === 200) {
        const data = res.data.data || []
        
        if (!data || data.length === 0) {
          return { success: false, message: '没有数据可导出' }
        }

        const headers = ['序号', '测项编号', '测项名称', '测项单位']
        const rows = data.map((item, index) => [
          index + 1,
          item.number || '',
          item.name || '',
          item.unit || ''
        ])

        let csvContent = '\ufeff' + headers.join(',') + '\n'
        rows.forEach(row => {
          csvContent += row.map(e => `"${e}"`).join(',') + '\n'
        })

        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = `测项信息数据_${new Date().getTime()}.csv`
        link.click()

        return { success: true, message: '导出成功' }
      } else {
        throw new Error(res.data.message || '导出失败')
      }
    } catch (error) {
      console.error('导出失败:', error)
      return { success: false, message: error.message || '导出失败' }
    }
  }

  return {
    // 状态
    loading,
    itemList,
    itemNames,
    total,
    query,
    formData,

    // 方法
    loadItemList,
    loadItemNames,
    loadItemInfo,
    saveItem,
    deleteItem,
    search,
    resetFilters,
    handlePageChange,
    handleSizeChange,
    resetForm,
    exportData
  }
}
