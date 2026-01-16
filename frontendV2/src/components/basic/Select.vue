<template>
  <div class="w-full relative group" ref="containerRef">
    <label v-if="label" class="block text-sm font-medium text-gray-700 mb-1">
      {{ label }}
      <span v-if="required" class="text-red-500">*</span>
    </label>
    
    <div class="relative">
      <!-- 触发器 -->
      <button
        type="button"
        :class="triggerBoxClasses"
        :disabled="disabled"
        @click="toggleDropdown"
      >
        <span :class="['block truncate', !selectedLabel && placeholder ? 'text-gray-400' : 'text-gray-900']">
          {{ selectedLabel || placeholder || '&nbsp;' }}
        </span>
        <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
          <svg 
            xmlns="http://www.w3.org/2000/svg" 
            viewBox="0 0 20 20" 
            fill="currentColor" 
            aria-hidden="true" 
            class="h-5 w-5 text-gray-400 transition-transform duration-200"
            :class="{ 'rotate-180': isOpen }"
          >
            <path fill-rule="evenodd" d="M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z" clip-rule="evenodd" />
          </svg>
        </span>
      </button>

      <!-- 下拉面板 -->
      <div
        v-show="isOpen"
        class="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm"
      >
        <ul v-if="options.length > 0" role="listbox">
          <li
            v-for="option in options"
            :key="option.value"
            class="relative cursor-pointer select-none py-2 pl-3 pr-9 transition-colors duration-150"
            :class="[
              isActive(option.value) ? 'bg-primary-50 text-primary-600' : 'text-gray-900 hover:bg-gray-50'
            ]"
            @click="selectOption(option)"
          >
            <span :class="['block truncate', isActive(option.value) ? 'font-medium' : 'font-normal']">
              {{ option.label }}
            </span>
            <span
              v-if="isActive(option.value)"
              class="absolute inset-y-0 right-0 flex items-center pr-4 text-primary-600"
            >
              <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                <path fill-rule="evenodd" d="M16.704 4.153a.75.75 0 01.143 1.052l-8 10.5a.75.75 0 01-1.127.075l-4.5-4.5a.75.75 0 011.06-1.06l3.894 3.893 7.48-9.817a.75.75 0 011.05-.143z" clip-rule="evenodd" />
              </svg>
            </span>
          </li>
        </ul>
        <div v-else class="py-2 pl-3 pr-9 text-gray-500 italic">
          暂无数据
        </div>
      </div>
    </div>
    
    <p v-if="error" class="mt-1 text-sm text-red-600">{{ error }}</p>
  </div>
</template>

<script setup>
/**
 * Select 组件 (Custom Implementation)
 * 功能描述：自定义下拉选择组件，统一交互与样式
 * 遵循原则：KISS（简洁实现）、SOLID（单一职责）
 * 参考：Pagination 组件下拉实现
 */
import { computed, ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  modelValue: {
    type: [String, Number],
    default: ''
  },
  options: {
    type: Array,
    default: () => [],
    // Expects [{ label: 'Option 1', value: '1' }]
  },
  placeholder: {
    type: String,
    default: '请选择'
  },
  label: {
    type: String,
    default: ''
  },
  disabled: {
    type: Boolean,
    default: false
  },
  required: {
    type: Boolean,
    default: false
  },
  error: {
    type: String,
    default: ''
  },
  size: {
    type: String,
    default: 'md',
    validator: (value) => ['sm', 'md', 'lg'].includes(value)
  }
})

const emit = defineEmits(['update:modelValue', 'change', 'blur', 'focus'])

const isOpen = ref(false)
const containerRef = ref(null)

// 尺寸映射 (与 Button 保持一致)
const sizeClasses = {
  sm: 'py-1.5 pl-3 pr-10 text-sm',
  md: 'py-2 pl-3 pr-10 text-sm', // default button height
  lg: 'py-3 pl-3 pr-10 text-base'
}

// 触发器样式
const triggerBoxClasses = computed(() => {
  const baseStyle = 'relative w-full cursor-default rounded-md bg-white text-left border focus:outline-none focus:ring-1 sm:text-sm transition-all duration-200'
  
  let stateStyle = ''
  if (props.error) {
    stateStyle = 'border-red-300 focus:border-red-500 focus:ring-red-500'
  } else {
    stateStyle = isOpen.value 
      ? 'border-primary-500 ring-1 ring-primary-500' 
      : 'border-gray-300 focus:border-primary-500 focus:ring-primary-500'
  }

  const disabledStyle = props.disabled ? 'bg-gray-100 cursor-not-allowed opacity-75' : 'cursor-pointer hover:border-gray-400'

  return [
    baseStyle,
    sizeClasses[props.size] || sizeClasses.md,
    stateStyle,
    disabledStyle
  ]
})

// 获取当前选中项的 Label
const selectedLabel = computed(() => {
  const option = props.options.find(o => o.value === props.modelValue)
  return option ? option.label : ''
})

// 判断选项是否选中
const isActive = (value) => {
  return props.modelValue === value
}

// 切换下拉显示
const toggleDropdown = () => {
  if (props.disabled) return
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    emit('focus')
  } else {
    emit('blur')
  }
}

// 选择选项
const selectOption = (option) => {
  if (props.disabled) return
  
  isOpen.value = false
  if (option.value !== props.modelValue) {
    emit('update:modelValue', option.value)
    emit('change', option.value)
  }
}

// 点击外部关闭
const handleClickOutside = (event) => {
  if (containerRef.value && !containerRef.value.contains(event.target)) {
    if (isOpen.value) {
      isOpen.value = false
      emit('blur')
    }
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>
