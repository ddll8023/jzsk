<template>
  <div class="flex flex-col gap-4">
    <!-- 第一行：筛选条件 -->
    <div class="flex flex-wrap items-center gap-3">
      <!-- 巡检站点 -->
      <div class="flex items-center gap-2">
        <label class="text-sm font-medium text-gray-700 whitespace-nowrap">巡检站点:</label>
        <Select
          :model-value="project"
          :options="projectOptions"
          placeholder="请选择"
          size="md"
          class="w-40"
          @update:model-value="$emit('update:project', $event)"
        />
      </div>

      <!-- 时间范围 -->
      <div class="flex items-center gap-2">
        <label class="text-sm font-medium text-gray-700 whitespace-nowrap">巡检时间:</label>
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

      <!-- 异常情况 -->
      <div class="flex items-center gap-2">
        <label class="text-sm font-medium text-gray-700 whitespace-nowrap">异常情况:</label>
        <Select
          :model-value="abnormal"
          :options="abnormalOptions"
          placeholder="请选择"
          size="md"
          class="w-40"
          @update:model-value="$emit('update:abnormal', $event)"
        />
      </div>

      <!-- 处理状态 -->
      <div class="flex items-center gap-2">
        <label class="text-sm font-medium text-gray-700 whitespace-nowrap">处理状态:</label>
        <Select
          :model-value="solve"
          :options="solveOptions"
          placeholder="请选择"
          size="md"
          class="w-40"
          @update:model-value="$emit('update:solve', $event)"
        />
      </div>

      <!-- 负责人 -->
      <div class="flex items-center gap-2">
        <label class="text-sm font-medium text-gray-700 whitespace-nowrap">负责人:</label>
        <Select
          :model-value="person"
          :options="personOptions"
          placeholder="请选择"
          size="md"
          class="w-40"
          @update:model-value="$emit('update:person', $event)"
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
 * 巡检记录查询面板
 * 功能：多条件筛选、导出、新增
 * 遵循原则：KISS, YAGNI, SOLID
 */
import Button from '@/components/basic/Button.vue'
import Select from '@/components/basic/Select.vue'

defineProps({
  project: { type: String, default: '' },
  startDate: { type: String, default: '' },
  endDate: { type: String, default: '' },
  abnormal: { type: String, default: '' },
  solve: { type: String, default: '' },
  person: { type: String, default: '' },
  projectOptions: { type: Array, default: () => [] },
  abnormalOptions: { type: Array, default: () => [] },
  solveOptions: { type: Array, default: () => [] },
  personOptions: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false }
})

defineEmits([
  'update:project',
  'update:start-date',
  'update:end-date',
  'update:abnormal',
  'update:solve',
  'update:person',
  'search',
  'reset',
  'export',
  'add'
])
</script>
