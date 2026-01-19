/**
 * 监测站点管理 Composable
 * 功能：封装监测站点的CRUD逻辑
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { ref, reactive } from 'vue'
import {
  getMonitorSiteList,
  getMonitorSiteInfo,
  saveMonitorSite,
  updateMonitorSite,
  deleteMonitorSite,
  getMonitorSiteNames
} from '@/api/engineering'

export function useMonitorSite() {
  // 数据状态
  const loading = ref(true) // 初始加载状态为 true，显示加载动画
  const siteList = ref([])
  const siteNames = ref([])
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
    code: '',
    name: '',
    waterName: '',
    riverName: '',
    monitorCode: '',
    addressCode: '',
    establishDate: '',
    longitude: '',
    latitude: '',
    note: ''
  })

  // 表单验证规则
  const formRules = {
    code: [
      { required: true, message: '请输入站码', trigger: 'blur' }
    ],
    name: [
      { required: true, message: '请输入站名', trigger: 'blur' }
    ],
    longitude: [
      { required: true, message: '请输入经度', trigger: 'blur' },
      {
        pattern: /^-?((1[0-7]\d)|(\d{1,2}))(\.\d+)?$/,
        message: '经度范围：-180到180',
        trigger: 'blur'
      }
    ],
    latitude: [
      { required: true, message: '请输入纬度', trigger: 'blur' },
      {
        pattern: /^-?([1-8]?\d(\.\d+)?|90(\.0+)?)$/,
        message: '纬度范围：-90到90',
        trigger: 'blur'
      }
    ]
  }

  /**
   * 加载站点列表
   */
  const loadSiteList = async () => {
    loading.value = true
    try {
      const res = await getMonitorSiteList(query)
      if (res.data.code === 200) {
        siteList.value = res.data.data.records || []
        total.value = res.data.data.total || 0
      } else {
        throw new Error(res.data.message || '获取数据失败')
      }
    } catch (error) {
      console.error('加载站点列表失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  /**
   * 加载站点名称列表（用于筛选下拉）
   */
  const loadSiteNames = async () => {
    try {
      const res = await getMonitorSiteNames()
      if (res.data.code === 200) {
        siteNames.value = (res.data.data || []).map(name => ({
          label: name,
          value: name
        }))
      }
    } catch (error) {
      console.error('加载站点名称失败:', error)
    }
  }

  /**
   * 加载站点详情
   */
  const loadSiteInfo = async (id) => {
    loading.value = true
    try {
      const res = await getMonitorSiteInfo(id)
      if (res.data.code === 200) {
        Object.assign(formData, res.data.data)
        return res.data.data
      } else {
        throw new Error(res.data.message || '获取详情失败')
      }
    } catch (error) {
      console.error('加载站点详情失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  /**
   * 保存站点（新增或更新）
   */
  const saveSite = async (data) => {
    loading.value = true
    try {
      const apiFunc = data.id ? updateMonitorSite : saveMonitorSite
      const res = await apiFunc(data)
      if (res.data.code === 200) {
        return { success: true, message: data.id ? '更新成功' : '新增成功' }
      } else {
        throw new Error(res.data.message || '保存失败')
      }
    } catch (error) {
      console.error('保存站点失败:', error)
      return { success: false, message: error.message || '保存失败' }
    } finally {
      loading.value = false
    }
  }

  /**
   * 删除站点
   */
  const deleteSite = async (id) => {
    loading.value = true
    try {
      const res = await deleteMonitorSite(id)
      if (res.data.code === 200) {
        return { success: true, message: '删除成功' }
      } else {
        throw new Error(res.data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除站点失败:', error)
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
    loadSiteList()
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
    loadSiteList()
  }

  /**
   * 每页条数变化
   */
  const handleSizeChange = (size) => {
    query.pageSize = size
    query.currentPage = 1
    loadSiteList()
  }

  /**
   * 重置表单
   */
  const resetForm = () => {
    Object.assign(formData, {
      id: null,
      code: '',
      name: '',
      waterName: '',
      riverName: '',
      monitorCode: '',
      addressCode: '',
      establishDate: '',
      longitude: '',
      latitude: '',
      note: ''
    })
  }

  /**
   * 导出CSV
   */
  const exportData = () => {
    if (!siteList.value || siteList.value.length === 0) {
      return { success: false, message: '没有数据可导出' }
    }

    const headers = [
      '序号', '站码', '站名', '水系名称', '河流名称',
      '施测项目码', '行政区划码', '设站年月', '经度', '纬度', '备注'
    ]

    const rows = siteList.value.map((item, index) => [
      index + 1,
      item.code || '',
      item.name || '',
      item.waterName || '',
      item.riverName || '',
      item.monitorCode || '',
      item.addressCode || '',
      item.establishDate || '',
      item.longitude || '',
      item.latitude || '',
      item.note || ''
    ])

    let csvContent = '\ufeff' + headers.join(',') + '\n'
    rows.forEach(row => {
      csvContent += row.map(e => `"${e}"`).join(',') + '\n'
    })

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = `监测站点数据_${new Date().getTime()}.csv`
    link.click()

    return { success: true, message: '导出成功' }
  }

  return {
    // 状态
    loading,
    siteList,
    siteNames,
    total,
    query,
    formData,
    formRules,

    // 方法
    loadSiteList,
    loadSiteNames,
    loadSiteInfo,
    saveSite,
    deleteSite,
    search,
    resetFilters,
    handlePageChange,
    handleSizeChange,
    resetForm,
    exportData
  }
}
