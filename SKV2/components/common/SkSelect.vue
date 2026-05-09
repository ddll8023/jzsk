<template>
  <view class="w-full">
    <text
      v-if="label"
      class="text-base font-medium text-gray-900 mr-3 flex-shrink-0"
      :style="{ width: labelWidth }"
    >
      {{ label }}
    </text>

    <picker
      mode="selector"
      :range="options"
      range-key="label"
      :value="selectedIndex"
      :disabled="disabled"
      @change="handleChange"
    >
      <view
        class="flex-1 flex items-center justify-between min-h-[40px]"
        :class="{ 'opacity-60': disabled }"
      >
        <text class="text-base" :class="selectedLabel ? 'text-gray-900' : 'text-gray-400'">
          {{ selectedLabel || placeholder }}
        </text>
        <text class="text-xs text-gray-400 ml-2">▼</text>
      </view>
    </picker>
  </view>
</template>

<script setup>
/**
 * SkSelect 下拉选择组件
 * 功能：基于 picker 实现的下拉选择器
 */
import { computed } from 'vue'

const props = defineProps({
  modelValue: { type: [String, Number], default: '' },
  options: { type: Array, default: () => [] },
  label: { type: String, default: '' },
  labelWidth: { type: String, default: '160rpx' },
  placeholder: { type: String, default: '请选择' },
  disabled: { type: Boolean, default: false },
})

const emit = defineEmits(['update:modelValue', 'change'])

const selectedIndex = computed(() => {
  return props.options.findIndex((opt) => opt.value === props.modelValue)
})

const selectedLabel = computed(() => {
  const target = props.options.find((opt) => opt.value === props.modelValue)
  return target ? target.label : ''
})

const handleChange = (e) => {
  const index = e.detail.value
  if (index < 0 || index >= props.options.length) return
  const selected = props.options[index]
  emit('update:modelValue', selected.value)
  emit('change', selected.value)
}
</script>
