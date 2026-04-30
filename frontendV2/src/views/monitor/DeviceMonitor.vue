<template>
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-6">
      <h1 class="text-xl font-semibold text-gray-900">设备监控</h1>
      <p class="mt-1 text-sm text-gray-500">
        实时监控 GNSS 地表位移、雨水情、渗流渗压设备的运行状态
      </p>
    </header>

    <!-- 加载状态 -->
    <div v-if="loading && !overview" class="flex items-center justify-center py-20">
      <svg class="animate-spin h-8 w-8 text-primary-500" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
      </svg>
      <span class="ml-3 text-gray-500">正在检测设备状态...</span>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-lg p-6 mb-6">
      <div class="flex items-center gap-3">
        <i class="fa fa-exclamation-circle text-red-500 text-lg" />
        <div>
          <h3 class="text-sm font-medium text-red-800">数据加载失败</h3>
          <p class="mt-1 text-sm text-red-600">{{ error }}</p>
        </div>
        <button
          class="ml-auto px-3 py-1.5 text-sm text-red-700 hover:text-red-800 border border-red-300 rounded-lg hover:bg-red-100 transition-colors"
          @click="fetchData"
        >
          重新加载
        </button>
      </div>
    </div>

    <!-- 正常内容 -->
    <template v-else-if="overview">
      <!-- 总览统计条 -->
      <section class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <DeviceTypeCard
          type="gnss"
          :stats="overview.gnss"
          :active="activeType === 'gnss'"
          @click="setTypeFilter('gnss')"
        />
        <DeviceTypeCard
          type="rain"
          :stats="overview.rain"
          :active="activeType === 'rain'"
          @click="setTypeFilter('rain')"
        />
        <DeviceTypeCard
          type="seepage"
          :stats="overview.seepage"
          :active="activeType === 'seepage'"
          @click="setTypeFilter('seepage')"
        />
      </section>

      <!-- 设备列表 -->
      <section>
        <DeviceStatusTable
          :devices="filteredDevices"
          :loading="loading"
          :active-status="activeStatus"
          @status-filter="setStatusFilter"
        />
      </section>
    </template>
  </div>
</template>

<script setup>
/**
 * 设备监控页面
 * 功能：展示三种设备类型（GNSS/雨水情/渗流渗压）的实时运行状态
 * 依赖：DeviceTypeCard, DeviceStatusTable, useDeviceMonitor
 */
import { useDeviceMonitor } from '@/composables/useDeviceMonitor'
import DeviceTypeCard from '@/components/business/monitor/DeviceTypeCard.vue'
import DeviceStatusTable from '@/components/business/monitor/DeviceStatusTable.vue'

const {
  loading,
  error,
  overview,
  filteredDevices,
  activeType,
  activeStatus,
  setTypeFilter,
  setStatusFilter,
  fetchData
} = useDeviceMonitor()
</script>
