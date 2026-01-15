/**
 * 表单 Composable
 * 功能：表单验证、提交公共逻辑
 * Source: Vue 3 官方文档 - Composition API
 */
import { ref, reactive } from 'vue'

/**
 * 创建表单的公共状态和方法
 * @param {Object} options - 配置选项
 * @param {Function} options.validateFn - 自定义验证函数
 * @param {Function} options.submitFn - 提交函数
 * @returns {Object} 表单状态和方法
 */
export function useForm(options = {}) {
  const { validateFn = null, submitFn = null } = options

  const formRef = ref(null)
  const loading = ref(false)
  const errors = ref({})

  const form = reactive({})

  /**
   * 重置表单到初始状态
   */
  const resetForm = () => {
    errors.value = {}
    Object.keys(form).forEach(key => {
      form[key] = ''
    })
  }

  /**
   * 表单验证
   * @returns {Promise<boolean>}
   */
  const validate = async () => {
    if (validateFn) {
      return await validateFn(form)
    }
    return true
  }

  /**
   * 提交表单
   * @param {Object} data - 表单数据
   * @returns {Promise<boolean>}
   */
  const handleSubmit = async (data = form) => {
    loading.value = true
    errors.value = {}
    
    try {
      const valid = await validate()
      if (!valid) {
        loading.value = false
        return false
      }
    
      if (submitFn) {
        await submitFn(data)
      }
      return true
    } catch (error) {
      console.error('表单提交失败:', error)
      errors.value.submit = error.message || '提交失败'
      throw error
    } finally {
      loading.value = false
    }
  }

  return {
    formRef,
    form,
    errors,
    loading,
    resetForm,
    validate,
    handleSubmit
  }
}
