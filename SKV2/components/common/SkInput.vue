<template>
  <view class="w-full">
    <view
      class="flex items-center w-full transition-all duration-200"
      :class="[containerClasses, { 'opacity-60': disabled }]"
    >
      <text
        v-if="label"
        class="text-base font-medium text-gray-900 mr-3 flex-shrink-0"
        :style="{ width: labelWidth }"
      >
        {{ label }}
      </text>

      <view class="flex-1 flex items-center relative min-h-[40px]">
        <!-- 日期选择器 -->
        <picker
          v-if="type === 'datetime'"
          mode="date"
          :value="modelValue"
          @change="handleDateChange"
          class="flex-1"
        >
          <view class="flex-1 text-base flex items-center min-h-[40px]">
            <text :class="modelValue ? 'text-gray-900' : 'text-gray-400'">
              {{ modelValue || placeholder }}
            </text>
          </view>
        </picker>

        <!-- 普通输入框 -->
        <input
          v-else
          class="flex-1 text-base text-gray-900 min-h-[40px]"
          :type="type"
          :value="modelValue"
          :placeholder="placeholder"
          :disabled="disabled"
          :password="type === 'password'"
          placeholder-class="text-gray-400"
          @input="handleInput"
          @focus="focused = true"
          @blur="focused = false"
        />

        <!-- 清除按钮 -->
        <view
          v-if="clearable && modelValue && !disabled && type !== 'datetime'"
          class="flex items-center justify-center w-5 h-5 rounded-full bg-gray-200 ml-2"
          @click.stop="handleClear"
        >
          <text class="text-xs text-gray-500 leading-none">×</text>
        </view>
      </view>
    </view>

    <text v-if="error" class="text-xs text-red-500 mt-1 ml-1">{{ error }}</text>
  </view>
</template>

<script setup>
/**
 * SkInput 输入框组件
 * 功能：基础表单输入，支持文本、数字、日期、密码
 */
import { ref, computed } from 'vue'

const props = defineProps({
  modelValue: { type: [String, Number], default: '' },
  label: { type: String, default: '' },
  labelWidth: { type: String, default: '160rpx' },
  placeholder: { type: String, default: '请输入' },
  type: { type: String, default: 'text' },
  disabled: { type: Boolean, default: false },
  clearable: { type: Boolean, default: false },
  error: { type: String, default: '' },
})

const emit = defineEmits(['update:modelValue', 'clear'])

const focused = ref(false)

const containerClasses = computed(() => {
  if (props.error) return 'border-b-2 border-red-500'
  if (focused.value) return 'border-b-2 border-primary'
  return 'border-b border-gray-200'
})

const handleInput = (e) => {
  emit('update:modelValue', e.detail.value)
}

const handleDateChange = (e) => {
  emit('update:modelValue', e.detail.value)
}

const handleClear = () => {
  emit('update:modelValue', '')
  emit('clear')
}
</script>
