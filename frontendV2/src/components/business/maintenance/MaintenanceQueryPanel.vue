<template>
  <div class="flex flex-col gap-4">
    <!-- 第一行：筛选条件 -->
    <div class="flex flex-wrap items-center gap-3">
      <!-- 工程名称搜索 -->
      <div class="flex items-center gap-2">
        <label class="text-sm font-medium text-gray-700 whitespace-nowrap">工程名称:</label>
        <input
          :value="name"
          type="text"
          placeholder="请输入工程名称"
          class="px-3 py-2 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent w-48"
          @input="$emit('update:name', $event.target.value)"
        />
      </div>

      <!-- 时间范围 -->
      <div class="flex items-center gap-2">
        <label class="text-sm font-medium text-gray-700 whitespace-nowrap">发生时间:</label>
        <input
          type="datetime-local"
          :value="startDate"
          class="px-3 py-2 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          @input="$emit('update:start-date', $event.target.value)"
        />
        <span class="text-gray-500">-</span>
        <input
          type="datetime-local"
          :value="endDate"
          class="px-3 py-2 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          @input="$emit('update:end-date', $event.target.value)"
        />
      </div>
    </div>

    <!-- 第二行：操作按钮 -->
    <div class="flex items-center gap-3">
      <Button type="primary" icon="search" :disabled="loading" @click="$emit('search')">
        搜索
      </Button>
      <Button type="default" icon="refresh" :disabled="loading" @click="$emit('reset')">
        重置
      </Button>
      <div class="ml-auto flex items-center gap-3">
        <Button type="success" icon="download" :disabled="loading" @click="$emit('export')">
          导出
        </Button>
        <Button type="primary" icon="plus" :disabled="loading" @click="$emit('add')">
          新增
        </Button>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 维护记录查询面板
 * 功能：工程名称搜索、时间筛选、导出、新增
 * 遵循原则：KISS, YAGNI, SOLID
 */
import Button from '@/components/basic/Button.vue'

defineProps({
  name: { type: String, default: '' },
  startDate: { type: String, default: '' },
  endDate: { type: String, default: '' },
  loading: { type: Boolean, default: false }
})

defineEmits([
  'update:name',
  'update:start-date',
  'update:end-date',
  'search',
  'reset',
  'export',
  'add'
])
</script>
