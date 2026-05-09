/**
 * 巡检记录业务逻辑
 * 功能：封装巡检记录的状态管理和业务方法
 */
import { ref, reactive } from 'vue'
import {
  getInspectionPage,
  getInspectionById,
  createInspection,
  updateInspection,
  deleteInspection,
  solveInspection,
} from '@/services/inspection.js'

export function useInspection() {
  const loading = ref(false)
  const tableData = ref([])
  const total = ref(0)

  const query = reactive({
    page: 1,
    size: 10,
    project: '',
    abnormal: '',
    person: '',
    solve: '',
    startTime: '',
    endTime: '',
  })

  const options = {
    projectList: [
      { label: '大坝主体', value: '大坝主体' },
      { label: '溢洪道', value: '溢洪道' },
      { label: '输水隧洞', value: '输水隧洞' },
      { label: '放水设施', value: '放水设施' },
      { label: '观测设施', value: '观测设施' },
      { label: '管理房', value: '管理房' },
    ],
    typeList: [
      { label: '日常巡检', value: '日常巡检' },
      { label: '专项巡检', value: '专项巡检' },
      { label: '节假日巡检', value: '节假日巡检' },
      { label: '汛期巡检', value: '汛期巡检' },
    ],
    abnormalList: [
      { label: '正常', value: '正常' },
      { label: '异常', value: '异常' },
    ],
    solveList: [
      { label: '未处理', value: '未处理' },
      { label: '处理中', value: '处理中' },
      { label: '已处理', value: '已处理' },
    ],
    personList: [
      { label: '张三', value: '张三' },
      { label: '李四', value: '李四' },
      { label: '王五', value: '王五' },
    ],
  }

  const buildParams = () => {
    const params = { page: query.page, size: query.size }
    if (query.project) params.project = query.project
    if (query.abnormal) params.abnormal = query.abnormal
    if (query.person) params.person = query.person
    if (query.solve) params.solve = query.solve
    if (query.startTime) params.startTime = query.startTime
    if (query.endTime) params.endTime = query.endTime
    return params
  }

  const fetchData = async () => {
    loading.value = true
    try {
      const params = buildParams()
      const res = await getInspectionPage(params)
      tableData.value = res.data?.list || []
      total.value = res.data?.total || 0
    } catch {
      tableData.value = []
      total.value = 0
    } finally {
      loading.value = false
    }
  }

  const loadMore = async () => {
    if (loading.value) return
    if (tableData.value.length >= total.value) return

    query.page++
    loading.value = true
    try {
      const res = await getInspectionPage(buildParams())
      const records = res.data?.list || []
      tableData.value = [...tableData.value, ...records]
    } catch {
      query.page--
    } finally {
      loading.value = false
    }
  }

  const refresh = () => {
    query.page = 1
    return fetchData()
  }

  const getDetail = async (id) => {
    try {
      const res = await getInspectionById(id)
      return res.data || null
    } catch {
      return null
    }
  }

  const saveData = async (formData) => {
    try {
      if (formData.id) {
        await updateInspection(formData)
      } else {
        await createInspection(formData)
      }
      uni.showToast({ title: '保存成功', icon: 'success' })
      return true
    } catch {
      // request.js 已统一提示错误 toast
      return false
    }
  }

  const removeData = async (id) => {
    try {
      await deleteInspection(id)
      uni.showToast({ title: '删除成功', icon: 'success' })
      await refresh()
      return true
    } catch {
      // request.js 已统一提示错误 toast
      return false
    }
  }

  const solveData = async (id) => {
    try {
      await solveInspection(id)
      uni.showToast({ title: '处理成功', icon: 'success' })
      await refresh()
      return true
    } catch {
      // request.js 已统一提示错误 toast
      return false
    }
  }

  const resetQuery = () => {
    query.page = 1
    query.project = ''
    query.abnormal = ''
    query.person = ''
    query.solve = ''
    query.startTime = ''
    query.endTime = ''
  }

  return {
    loading,
    tableData,
    total,
    query,
    options,
    fetchData,
    loadMore,
    refresh,
    getDetail,
    saveData,
    removeData,
    solveData,
    resetQuery,
  }
}
