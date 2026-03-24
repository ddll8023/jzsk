/**
 * 洪水防御预案 Composable
 * 功能：封装洪水防御预案的CRUD逻辑
 * 遵循原则：KISS, YAGNI, SOLID
 * 数据存储：使用 localStorage 持久化
 */
import { ref, reactive } from 'vue'

// 默认预案数据
const DEFAULT_PLAN = [
  { id: 1, time: '汛前', content: '检查大坝、闸门、通讯等设施，完善应急物资储备', ordernum: 1 },
  { id: 2, time: '汛期', content: '加强监测，及时发布预警，科学调度水库', ordernum: 2 },
  { id: 3, time: '洪水发生时', content: '启动应急响应，组织人员转移，保障群众安全', ordernum: 3 },
  { id: 4, time: '洪水后', content: '开展灾后评估和恢复重建', ordernum: 4 }
]

const STORAGE_KEY = 'flood_plan_data'

export function useFloodPlan() {
  // 数据状态
  const loading = ref(false)
  const planList = ref([])

  // 表单数据
  const formData = reactive({
    id: null,
    time: '',
    content: '',
    ordernum: 0
  })

  /**
   * 从本地存储加载数据
   */
  const loadFromLocalStorage = () => {
    try {
      const stored = localStorage.getItem(STORAGE_KEY)
      if (stored) {
        planList.value = JSON.parse(stored)
      } else {
        planList.value = JSON.parse(JSON.stringify(DEFAULT_PLAN))
        saveToLocalStorage()
      }
    } catch (error) {
      console.error('加载本地数据失败:', error)
      planList.value = JSON.parse(JSON.stringify(DEFAULT_PLAN))
    }
  }

  /**
   * 保存到本地存储
   */
  const saveToLocalStorage = () => {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(planList.value))
    } catch (error) {
      console.error('保存到本地存储失败:', error)
    }
  }

  /**
   * 加载预案列表
   */
  const loadPlanList = async () => {
    loading.value = true
    try {
      loadFromLocalStorage()
    } finally {
      loading.value = false
    }
  }

  /**
   * 加载预案详情
   */
  const loadPlanInfo = (id) => {
    const item = planList.value.find(p => p.id === id)
    if (item) {
      Object.assign(formData, item)
      return item
    }
    throw new Error('未找到该预案步骤')
  }

  /**
   * 保存预案（新增或更新）
   */
  const savePlanItem = (data) => {
    if (data.id) {
      // 更新
      const index = planList.value.findIndex(p => p.id === data.id)
      if (index !== -1) {
        planList.value[index] = { ...data }
      }
    } else {
      // 新增
      const maxId = Math.max(0, ...planList.value.map(p => p.id))
      planList.value.push({
        ...data,
        id: maxId + 1,
        ordernum: planList.value.length + 1
      })
    }
    saveToLocalStorage()
    return { success: true, message: data.id ? '更新成功' : '新增成功' }
  }

  /**
   * 删除预案步骤
   */
  const deletePlanItem = (id) => {
    const index = planList.value.findIndex(p => p.id === id)
    if (index !== -1) {
      planList.value.splice(index, 1)
      // 重新排序
      planList.value.forEach((item, idx) => {
        item.ordernum = idx + 1
      })
      saveToLocalStorage()
      return { success: true, message: '删除成功' }
    }
    return { success: false, message: '未找到该预案步骤' }
  }

  /**
   * 重置为默认预案
   */
  const resetToDefault = () => {
    planList.value = JSON.parse(JSON.stringify(DEFAULT_PLAN))
    saveToLocalStorage()
    return { success: true, message: '已重置为默认预案' }
  }

  /**
   * 重置表单
   */
  const resetForm = () => {
    Object.assign(formData, {
      id: null,
      time: '',
      content: '',
      ordernum: 0
    })
  }

  return {
    // 状态
    loading,
    planList,
    formData,

    // 方法
    loadPlanList,
    loadPlanInfo,
    savePlanItem,
    deletePlanItem,
    resetToDefault,
    resetForm
  }
}
