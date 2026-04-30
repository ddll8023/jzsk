<template>
  <div class="min-h-full bg-gray-50 p-6 lg:p-8">
    <!-- 页面标题 -->
    <header class="mb-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-xl font-semibold text-gray-900">设备监控</h1>
          <p class="mt-1 text-sm text-gray-500">
            实时监控 GNSS 地表位移、雨水情、渗流渗压设备的运行状态
          </p>
        </div>
        <div v-if="loading" class="flex items-center gap-2 text-sm text-gray-400">
          <i class="fa fa-spinner fa-spin" aria-hidden="true" />
          <span>刷新中</span>
        </div>
      </div>
    </header>

    <!-- 错误状态 -->
    <div v-if="error && !overview" class="bg-red-50 border border-red-200 rounded-lg p-6 mb-6">
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

    <!-- 总览统计条 -->
    <section class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
      <!-- Rain 卡片 -->
      <DeviceTypeCard
        v-if="rain.stats.value"
        type="rain"
        :stats="rain.stats.value"
        :active="activeType === 'rain'"
        @click="setTypeFilter('rain')"
      />
      <div v-else class="bg-white rounded-xl border border-gray-200 shadow-md overflow-hidden">
        <div class="h-1 bg-gray-100 animate-pulse" />
        <div class="p-5 space-y-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <div class="w-8 h-8 rounded-lg bg-gray-100 animate-pulse" />
              <div class="h-4 w-20 rounded bg-gray-100 animate-pulse" />
            </div>
            <div class="h-8 w-10 rounded bg-gray-100 animate-pulse" />
          </div>
          <div class="h-4 rounded bg-gray-100 animate-pulse" />
          <div class="flex items-center gap-4">
            <div class="h-3 w-16 rounded bg-gray-100 animate-pulse" />
            <div class="h-3 w-16 rounded bg-gray-100 animate-pulse" />
            <div class="h-3 w-16 rounded bg-gray-100 animate-pulse" />
          </div>
        </div>
      </div>

      <!-- GNSS 卡片 -->
      <DeviceTypeCard
        v-if="gnss.stats.value"
        type="gnss"
        :stats="gnss.stats.value"
        :active="activeType === 'gnss'"
        @click="setTypeFilter('gnss')"
      />
      <div v-else class="bg-white rounded-xl border border-gray-200 shadow-md overflow-hidden">
        <div class="h-1 bg-gray-100 animate-pulse" />
        <div class="p-5 space-y-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <div class="w-8 h-8 rounded-lg bg-gray-100 animate-pulse" />
              <div class="h-4 w-20 rounded bg-gray-100 animate-pulse" />
            </div>
            <div class="h-8 w-10 rounded bg-gray-100 animate-pulse" />
          </div>
          <div class="h-4 rounded bg-gray-100 animate-pulse" />
          <div class="flex items-center gap-4">
            <div class="h-3 w-16 rounded bg-gray-100 animate-pulse" />
            <div class="h-3 w-16 rounded bg-gray-100 animate-pulse" />
            <div class="h-3 w-16 rounded bg-gray-100 animate-pulse" />
          </div>
        </div>
      </div>

      <!-- Seepage 卡片 -->
      <DeviceTypeCard
        v-if="seepage.stats.value"
        type="seepage"
        :stats="seepage.stats.value"
        :active="activeType === 'seepage'"
        @click="setTypeFilter('seepage')"
      />
      <div v-else class="bg-white rounded-xl border border-gray-200 shadow-md overflow-hidden">
        <div class="h-1 bg-gray-100 animate-pulse" />
        <div class="p-5 space-y-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <div class="w-8 h-8 rounded-lg bg-gray-100 animate-pulse" />
              <div class="h-4 w-20 rounded bg-gray-100 animate-pulse" />
            </div>
            <div class="h-8 w-10 rounded bg-gray-100 animate-pulse" />
          </div>
          <div class="h-4 rounded bg-gray-100 animate-pulse" />
          <div class="flex items-center gap-4">
            <div class="h-3 w-16 rounded bg-gray-100 animate-pulse" />
            <div class="h-3 w-16 rounded bg-gray-100 animate-pulse" />
            <div class="h-3 w-16 rounded bg-gray-100 animate-pulse" />
          </div>
        </div>
      </div>
    </section>

    <!-- 设备列表 -->
    <section>
      <DeviceStatusTable
        :devices="filteredDevices"
        :loading="tableLoading"
        :active-status="activeStatus"
        :active-type="activeType"
        @status-filter="setStatusFilter"
      />
    </section>
  </div>
</template>

<script setup>
/**
 * 设备监控页面
 * 功能：展示三种设备类型（GNSS/雨水情/渗流渗压）的实时运行状态
 * 依赖：DeviceTypeCard, DeviceStatusTable, useDeviceMonitor
 * 特性：三路接口并发加载、渐进式渲染
 */
import { useDeviceMonitor } from '@/composables/useDeviceMonitor'
import DeviceTypeCard from '@/components/business/monitor/DeviceTypeCard.vue'
import DeviceStatusTable from '@/components/business/monitor/DeviceStatusTable.vue'

const {
  loading,
  error,
  overview,
  filteredDevices,
  tableLoading,
  activeType,
  activeStatus,
  gnss,
  rain,
  seepage,
  setTypeFilter,
  setStatusFilter,
  fetchData
} = useDeviceMonitor()
</script>
