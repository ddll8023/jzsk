<template>
  <div class="absolute top-4 left-1/2 -translate-x-1/2 z-10">
    <!-- 预警信息按钮 -->
    <div class="relative group cursor-pointer" @click="handleButtonClick">
      <Button
        type="primary"
        size="lg"
        class="min-w-[160px] shadow-lg backdrop-blur-sm bg-red-600/90 hover:bg-red-700/90"
      >
        <i class="fa fa-exclamation-triangle mr-2" aria-hidden="true"></i>
        预警信息
        <span v-if="warningStats.unresolved > 0" class="ml-2 bg-white text-red-600 px-2 py-0.5 rounded-full text-xs font-bold">
          {{ warningStats.unresolved }}
        </span>
      </Button>

      <!-- 悬浮显示最新预警 -->
      <Card
        variant="default"
        padding="sm"
        shadow="xl"
        class="absolute top-full mt-2 w-[360px] opacity-0 invisible translate-y-2 group-hover:opacity-100 group-hover:visible group-hover:translate-y-0 transition-all duration-200"
      >
        <div v-if="panelLoading" class="text-center py-4 text-gray-500">
          <i class="fa fa-spinner fa-spin mr-2"></i>加载中...
        </div>
        <div v-else-if="warningStats.latest" class="space-y-3">
          <div class="flex items-center justify-between border-b pb-2">
            <span class="text-sm font-semibold text-gray-700">最新预警</span>
            <span class="text-xs text-gray-500">{{ formatTime(warningStats.latest.startTime) }}</span>
          </div>
          
          <div class="space-y-2">
            <div class="flex justify-between items-center">
              <span class="text-gray-600 text-sm">预警地点</span>
              <span class="font-medium text-gray-900">{{ warningStats.latest.position }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-600 text-sm">预警类型</span>
              <span class="font-medium text-gray-900">{{ warningStats.latest.type }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-600 text-sm">预警等级</span>
              <LevelBadge :level="warningStats.latest.level" />
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-600 text-sm">预警状态</span>
              <StatusBadge :status="warningStats.latest.status" />
            </div>
            <div class="pt-2 border-t">
              <span class="text-gray-600 text-sm">预警内容</span>
              <p class="text-gray-900 text-sm mt-1">{{ warningStats.latest.content }}</p>
            </div>
          </div>

          <div class="pt-2 border-t text-center">
            <span class="text-xs text-blue-600">点击按钮查看全部预警</span>
          </div>
        </div>
        <div v-else class="text-center py-6 text-gray-500">
          <i class="fa fa-check-circle text-3xl text-green-500 mb-2"></i>
          <p class="text-sm">暂无预警信息</p>
        </div>
      </Card>
    </div>
  </div>
</template>

<script setup>
/**
 * 预警信息展示面板
 * 功能：地图顶部预警按钮，悬浮显示最新预警，点击打开预警详情弹窗
 * 依赖：Button、Card、LevelBadge、StatusBadge、usePrewarning
 * 遵循原则：KISS, YAGNI, SOLID-SRP
 * Source: 基于 MapDataPanel.vue 改造
 * 修复：简化为单按钮设计（2025-01-19）
 * 修复：添加独立loading状态，避免状态共享冲突（2025-01-19）
 */
import { ref, onMounted, defineEmits } from 'vue'
import Button from '@/components/basic/Button.vue'
import Card from '@/components/basic/Card.vue'
import LevelBadge from '@/components/business/warning/LevelBadge.vue'
import StatusBadge from '@/components/business/warning/StatusBadge.vue'
import { usePrewarning } from '@/composables/usePrewarning'

// 事件发射
const emit = defineEmits(['open-modal'])

// 独立的加载状态（不使用composable的loading，避免状态共享冲突）
const panelLoading = ref(false)

// 使用预警数据管理
const {
  loading,  // 保留但不使用，避免解构错误
  warningStats,
  loadWarningStats
} = usePrewarning()

/**
 * 格式化时间显示
 * 修复：兼容多种时间格式
 */
const formatTime = (time) => {
  if (!time) return '-'
  
  try {
    let date
    
    if (Array.isArray(time)) {
      const [year, month, day, hour = 0, minute = 0, second = 0] = time
      date = new Date(year, month - 1, day, hour, minute, second)
    } else {
      date = new Date(time)
    }
    
    if (isNaN(date.getTime())) {
      return String(time)
    }
    
    return date.toLocaleString('zh-CN', { 
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false 
    })
  } catch (e) {
    console.error('时间格式化失败:', time, e)
    return String(time)
  }
}

/**
 * 点击按钮打开预警弹窗
 */
const handleButtonClick = () => {
  emit('open-modal')
}

// 组件挂载时获取统计数据
onMounted(async () => {
  panelLoading.value = true
  try {
    await loadWarningStats()
  } finally {
    panelLoading.value = false
  }
  
  // 每5分钟刷新一次统计数据
  setInterval(async () => {
    panelLoading.value = true
    try {
      await loadWarningStats()
    } finally {
      panelLoading.value = false
    }
  }, 5 * 60 * 1000)
})
</script>
