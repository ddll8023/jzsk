<template>
  <div class="w-full relative group">
    <label v-if="label" :for="inputId" class="block text-sm font-medium text-gray-700 mb-1">
      {{ label }}
      <span v-if="required" class="text-red-500">*</span>
    </label>
    <div class="relative">
      <i 
        v-if="prefixIcon" 
        :class="[
          `fa fa-${prefixIcon}`, 
          'absolute left-3 top-1/2 -translate-y-1/2 transition-colors duration-200 z-10',
          isFocused ? 'text-primary-600' : (dark ? 'text-[#C1C5CD]' : 'text-gray-400')
        ]" 
        aria-hidden="true"
      ></i>
      <input
        :id="inputId"
        :type="type"
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="disabled"
        :readonly="readonly"
        :class="[inputClasses, inputClass]"
        @input="handleInput"
        @blur="handleBlur"
        @focus="handleFocus"
      />
      <i 
        v-if="suffixIcon" 
        :class="[
          `fa fa-${suffixIcon}`, 
          'absolute right-3 top-1/2 -translate-y-1/2 transition-colors duration-200 z-10',
          isFocused ? 'text-primary-600' : (dark ? 'text-[#C1C5CD]' : 'text-gray-400')
        ]" 
        aria-hidden="true"
      ></i>
    </div>
    <p v-if="error" class="mt-1 text-sm text-red-600">{{ error }}</p>
  </div>
</template>

<script setup>
/**
 * Input 组件
 * 功能：统一输入框样式与交互
 * 遵循 KISS 原则：简洁实现
 */
import { computed, ref } from 'vue'

const props = defineProps({
  modelValue: {
    type: [String, Number],
    default: ''
  },
  type: {
    type: String,
    default: 'text'
  },
  placeholder: {
    type: String,
    default: ''
  },
  label: {
    type: String,
    default: ''
  },
  disabled: {
    type: Boolean,
    default: false
  },
  readonly: {
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
  prefixIcon: {
    type: String,
    default: ''
  },
  suffixIcon: {
    type: String,
    default: ''
  },
  size: {
    type: String,
    default: 'md'
  },
  // 样式变体: 'outlined' (default) | 'underline'
  variant: {
    type: String,
    default: 'outlined',
    validator: (value) => ['outlined', 'underline'].includes(value)
  },
  // 自定义 input 类名
  inputClass: {
    type: String,
    default: ''
  },
  // 深色模式：适用于深色背景
  dark: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'blur', 'focus'])

// 内部焦点状态
const isFocused = ref(false)

// 生成唯一 ID
const inputId = computed(() => `input-${Math.random().toString(36).substr(2, 9)}`)

// 尺寸映射
const sizeClasses = {
  sm: 'px-3 py-1.5 text-sm',
  md: 'px-4 py-2 text-sm',
  lg: 'px-4 py-3 text-base'
}

// 计算输入框样式
// 计算输入框样式
const inputClasses = computed(() => {
  // 基础样式
  const baseStyle = 'w-full transition-all duration-200 focus:outline-none'
  
  // 变体样式
  let variantStyle = ''
  if (props.variant === 'outlined') {
    variantStyle = 'border rounded-lg focus:ring-2'
    if (props.dark) {
      variantStyle += ' bg-white/10 border-white/30 text-white placeholder-white/60 focus:ring-white/50 focus:border-white/50'
    } else {
      variantStyle += props.error
        ? ' border-red-300 focus:ring-red-500 focus:border-red-500 bg-white'
        : ' border-gray-300 focus:ring-primary-500 focus:border-primary-500 bg-white'
    }
  } else if (props.variant === 'underline') {
    variantStyle = 'border-0 border-b rounded-none bg-transparent px-0'
    if (props.dark) {
      variantStyle += ' border-white/30 text-white placeholder-white/60 focus:border-white'
    } else {
      variantStyle += props.error
        ? ' border-red-300 focus:border-red-500'
        : ' border-slate-300 focus:border-primary-600' // 使用更通用的 slate-300 和 primary-600
    }
  }

  // 禁用状态
  const disabledStyle = (!props.dark && props.disabled) ? 'bg-gray-100 cursor-not-allowed' : ''

  // 图标 Padding
  // 如果是 underline 模式，图标应该影响 padding-left
  // 但 underline 模式下通常希望 input 文字紧贴左边（如果没有图标）
  // 或者如果有图标，padding 应该适当
  // 这里我们假设 icon 定位逻辑不变，通过 padding 让出空间
  const paddingStyle = [
    props.prefixIcon ? 'pl-10' : (props.variant === 'underline' ? 'pl-0' : ''),
    props.suffixIcon ? 'pr-10' : (props.variant === 'underline' ? 'pr-0' : '')
  ]

  return [
    baseStyle,
    sizeClasses[props.size] || sizeClasses.md,
    variantStyle,
    disabledStyle,
    ...paddingStyle
  ]
})

/**
 * 处理输入事件
 */
const handleInput = (event) => {
  emit('update:modelValue', event.target.value)
}

const handleFocus = (event) => {
  isFocused.value = true
  emit('focus', event)
}

const handleBlur = (event) => {
  isFocused.value = false
  emit('blur', event)
}
</script>
