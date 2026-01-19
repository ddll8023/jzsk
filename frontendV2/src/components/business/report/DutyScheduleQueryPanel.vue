<template>
  <div class="flex flex-col lg:flex-row lg:items-end gap-6 justify-between">
    <!-- 左侧：筛选条件 -->
    <div class="flex-1">
      <div class="flex items-center gap-3 mb-4">
        <!-- 视觉锚点装饰 -->
        <div class="h-6 w-1 rounded-full bg-primary-500"></div>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
        </svg>
        <h2 class="text-base font-bold text-gray-800">筛选条件</h2>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <!-- 开始日期 -->
        <div class="relative group">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-gray-400 group-focus-within:text-primary-500 transition-colors z-10" style="top: 28px;">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <label class="block text-sm font-medium text-gray-700 mb-2">开始日期</label>
          <input
            type="date"
            :value="startDate"
            @input="$emit('update:startDate', $event.target.value)"
            class="w-full pl-10 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all"
          />
        </div>

        <!-- 结束日期 -->
        <div class="relative group">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-gray-400 group-focus-within:text-primary-500 transition-colors z-10" style="top: 28px;">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <label class="block text-sm font-medium text-gray-700 mb-2">结束日期</label>
          <input
            type="date"
            :value="endDate"
            @input="$emit('update:endDate', $event.target.value)"
            class="w-full pl-10 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all"
          />
        </div>
      </div>
    </div>

    <!-- 右侧：操作按钮 -->
    <div class="flex items-end gap-3 pt-4 lg:pt-0 border-t lg:border-t-0 border-gray-100">
      <Button type="primary" icon="search" class="w-full lg:w-auto shadow-md shadow-primary-500/20" @click="$emit('search')">
        搜索
      </Button>
      <Button icon="refresh" class="w-full lg:w-auto" @click="$emit('reset')">
        重置
      </Button>
      <Button type="primary" icon="plus" class="w-full lg:w-auto shadow-md shadow-primary-500/20" @click="$emit('add')">
        新增
      </Button>
      <Button
        type="danger"
        icon="trash"
        class="w-full lg:w-auto"
        :disabled="!hasSelection"
        @click="$emit('batch-delete')"
      >
        批量删除
      </Button>
    </div>
  </div>
</template>

<script setup>
/**
 * 值班安排查询面板
 * 功能：日期范围搜索、新增、批量删除
 * 遵循原则：KISS, YAGNI, SOLID
 */
import Button from '@/components/basic/Button.vue'

defineProps({
  startDate: {
    type: String,
    default: ''
  },
  endDate: {
    type: String,
    default: ''
  },
  hasSelection: {
    type: Boolean,
    default: false
  }
})

defineEmits(['update:startDate', 'update:endDate', 'search', 'reset', 'add', 'batch-delete'])
</script>
