<template>
  <div class="border-b border-gray-200">
    <nav class="flex -mb-px space-x-8" aria-label="Tabs">
      <button
        v-for="tab in items"
        :key="tab.value"
        @click="handleClick(tab.value)"
        :class="[
          modelValue === tab.value
            ? 'border-primary-500 text-primary-600'
            : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300',
          'whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm transition-colors duration-200'
        ]"
        :aria-current="modelValue === tab.value ? 'page' : undefined"
      >
        {{ tab.label }}
      </button>
    </nav>
  </div>
</template>

<script setup>
/**
 * Tabs 组件
 * 功能：简单的标签页切换
 */
defineProps({
  modelValue: {
    type: [String, Number],
    required: true
  },
  items: {
    type: Array,
    required: true,
    // [{ label: 'Tab 1', value: 'tab1' }]
  }
})

const emit = defineEmits(['update:modelValue', 'change'])

const handleClick = (value) => {
  emit('update:modelValue', value)
  emit('change', value)
}
</script>
