<template>
  <!-- 地表位移数据表格 -->
  <div class="bg-white rounded-lg shadow p-4">
    <h3 class="text-base font-medium text-gray-800 mb-4">监测数据</h3>

    <!-- 加载状态 -->
    <div v-if="loading" class="flex items-center justify-center py-12">
      <i class="fa fa-spinner fa-spin text-2xl text-primary-500" aria-hidden="true"></i>
      <span class="ml-3 text-gray-500">加载中...</span>
    </div>

    <!-- 空状态 -->
    <div v-else-if="!tableData.length" class="flex items-center justify-center py-12 text-gray-400">
      <i class="fa fa-inbox text-3xl mr-3" aria-hidden="true"></i>
      <span>暂无数据</span>
    </div>

    <!-- 数据表格 -->
    <div v-else class="overflow-x-auto">
      <table class="w-full text-sm">
        <thead>
          <tr class="bg-gray-50">
            <th class="px-4 py-3 text-left font-medium text-gray-600 whitespace-nowrap">时间</th>
            <th class="px-4 py-3 text-left font-medium text-gray-600 whitespace-nowrap">站点名称</th>
            <th class="px-4 py-3 text-left font-medium text-gray-600 whitespace-nowrap">设备SN</th>
            <th class="px-4 py-3 text-right font-medium text-gray-600 whitespace-nowrap">X位移(mm)</th>
            <th class="px-4 py-3 text-right font-medium text-gray-600 whitespace-nowrap">Y位移(mm)</th>
            <th class="px-4 py-3 text-right font-medium text-gray-600 whitespace-nowrap">Z位移(mm)</th>
            <th class="px-4 py-3 text-right font-medium text-gray-600 whitespace-nowrap">合位移(mm)</th>
            <th class="px-4 py-3 text-right font-medium text-gray-600 whitespace-nowrap">水平位移(mm)</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(row, index) in tableData"
            :key="index"
            class="border-b border-gray-100 hover:bg-gray-50 transition-colors"
          >
            <td class="px-4 py-3 text-gray-800 whitespace-nowrap">{{ row.collectTime }}</td>
            <td class="px-4 py-3 text-gray-600">{{ row.stationName }}</td>
            <td class="px-4 py-3 text-gray-600">{{ row.deviceSn }}</td>
            <td class="px-4 py-3 text-right text-gray-600">{{ formatNum(row.gpsTotalX) }}</td>
            <td class="px-4 py-3 text-right text-gray-600">{{ formatNum(row.gpsTotalY) }}</td>
            <td class="px-4 py-3 text-right text-gray-600">{{ formatNum(row.gpsTotalZ) }}</td>
            <td class="px-4 py-3 text-right text-gray-600">{{ formatNum(row.displacement3d) }}</td>
            <td class="px-4 py-3 text-right text-gray-600">{{ formatNum(row.displacement2d) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
/**
 * 地表位移数据表格组件
 * 功能：展示位移监测数据列表
 */

defineProps({
  tableData: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false }
})

// 格式化数值
function formatNum(val) {
  if (val === null || val === undefined) return '-'
  return typeof val === 'number' ? val.toFixed(4) : val
}
</script>
