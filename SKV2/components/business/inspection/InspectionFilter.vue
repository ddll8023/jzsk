<template>
  <view class="bg-white">
    <view class="flex items-center justify-between py-2">
      <text class="text-base font-medium text-gray-900">筛选条件</text>
      <view class="flex items-center gap-2">
        <SkButton text="搜索" type="primary" size="small" @click="handleSearch" />
        <SkButton text="重置" type="default" size="small" @click="handleReset" />
      </view>
    </view>

    <view class="space-y-3 mt-2">
      <!-- 基本信息 -->
      <view class="flex items-center gap-3">
        <SkSelect
          v-model="filters.project"
          label="巡检站点"
          :options="projectOptions"
          placeholder="全部"
          class="flex-1"
        />
        <SkSelect
          v-model="filters.person"
          label="负责人"
          :options="personOptions"
          placeholder="全部"
          class="flex-1"
        />
      </view>

      <!-- 时间范围 -->
      <view class="grid grid-cols-2 gap-3">
        <SkInput
          v-model="filters.startTime"
          label="开始"
          type="datetime"
          placeholder="选择开始时间"
        />
        <SkInput
          v-model="filters.endTime"
          label="结束"
          type="datetime"
          placeholder="选择结束时间"
        />
      </view>

      <!-- 状态信息 -->
      <view class="flex items-center gap-3">
        <SkSelect
          v-model="filters.abnormal"
          label="异常情况"
          :options="abnormalOptions"
          placeholder="全部"
          class="flex-1"
        />
        <SkSelect
          v-model="filters.solve"
          label="处理状态"
          :options="solveOptions"
          placeholder="全部"
          class="flex-1"
        />
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * 巡检记录筛选组件
 * 功能：多条件筛选
 */
import { reactive } from 'vue'
import SkSelect from '@/components/common/SkSelect.vue'
import SkInput from '@/components/common/SkInput.vue'
import SkButton from '@/components/common/SkButton.vue'

defineProps({
  projectOptions: { type: Array, default: () => [] },
  abnormalOptions: { type: Array, default: () => [] },
  solveOptions: { type: Array, default: () => [] },
  personOptions: { type: Array, default: () => [] },
})

const emit = defineEmits(['search', 'reset'])

const filters = reactive({
  project: '',
  startTime: '',
  endTime: '',
  abnormal: '',
  solve: '',
  person: '',
})

const handleSearch = () => {
  emit('search', { ...filters })
}

const handleReset = () => {
  filters.project = ''
  filters.startTime = ''
  filters.endTime = ''
  filters.abnormal = ''
  filters.solve = ''
  filters.person = ''
  emit('reset')
}
</script>
