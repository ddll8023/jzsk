/**
 * 洪水防御预案 Composable
 * 功能：封装洪水防御预案的CRUD逻辑
 * 遵循原则：KISS, YAGNI, SOLID
 * 支持本地存储降级方案
 */
import { ref, reactive } from 'vue'
import {
  getFloodPlanList,
  getFloodPlanInfo,
  saveFloodPlan,
  updateFloodPlan,
  deleteFloodPlan
} from '@/api/engineering'

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
  const useLocalStorage = ref(false) // 是否使用本地存储

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
      const res = await getFloodPlanList()
      if (res.data.code === 200) {
        planList.value = res.data.data || []
        useLocalStorage.value = false
      } else {
        throw new Error(res.data.message || '获取数据失败')
      }
    } catch (error) {
      console.warn('后端接口调用失败，降级到本地存储:', error)
      useLocalStorage.value = true
      loadFromLocalStorage()
    } finally {
      loading.value = false
    }
  }

  /**
   * 加载预案详情
   */
  const loadPlanInfo = async (id) => {
    if (useLocalStorage.value) {
      const item = planList.value.find(p => p.id === id)
      if (item) {
        Object.assign(formData, item)
        return item
      }
      throw new Error('未找到该预案步骤')
    }

    loading.value = true
    try {
      const res = await getFloodPlanInfo(id)
      if (res.data.code === 200) {
        Object.assign(formData, res.data.data)
        return res.data.data
      } else {
        throw new Error(res.data.message || '获取详情失败')
      }
    } catch (error) {
      console.error('加载预案详情失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  /**
   * 保存预案（新增或更新）
   */
  const savePlanItem = async (data) => {
    if (useLocalStorage.value) {
      // 本地存储模式
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

    // 后端接口模式
    loading.value = true
    try {
      const apiFunc = data.id ? updateFloodPlan : saveFloodPlan
      const res = await apiFunc(data)
      if (res.data.code === 200) {
        return { success: true, message: data.id ? '更新成功' : '新增成功' }
      } else {
        throw new Error(res.data.message || '保存失败')
      }
    } catch (error) {
      console.error('保存预案失败:', error)
      return { success: false, message: error.message || '保存失败' }
    } finally {
      loading.value = false
    }
  }

  /**
   * 删除预案步骤
   */
  const deletePlanItem = async (id) => {
    if (useLocalStorage.value) {
      // 本地存储模式
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

    // 后端接口模式
    loading.value = true
    try {
      const res = await deleteFloodPlan(id)
      if (res.data.code === 200) {
        return { success: true, message: '删除成功' }
      } else {
        throw new Error(res.data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除预案失败:', error)
      return { success: false, message: error.message || '删除失败' }
    } finally {
      loading.value = false
    }
  }

  /**
   * 重置为默认预案
   */
  const resetToDefault = () => {
    planList.value = JSON.parse(JSON.stringify(DEFAULT_PLAN))
    if (useLocalStorage.value) {
      saveToLocalStorage()
    }
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
    useLocalStorage,
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
