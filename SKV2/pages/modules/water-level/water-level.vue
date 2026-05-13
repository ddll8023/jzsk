<template>
  <view class="min-h-screen bg-gray-50">
    <view class="p-4">
      <!-- 加载状态 -->
      <SkLoading v-if="loading" />

      <template v-else>
        <!-- 实时监测卡片 -->
        <SkCard title="实时监测">
          <view class="py-3">
            <view class="flex items-center justify-between mb-4">
              <view class="flex items-baseline">
                <text class="text-4xl font-bold text-primary">{{
                  currentLevel
                }}</text>
                <text class="text-sm text-gray-400 ml-2">m</text>
              </view>
              <view class="flex items-center" @click="refreshData">
                <view
                  class="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center"
                  :class="{ 'animate-spin': refreshing }"
                >
                  <image
                    src="/static/icons/monitor/refresh.svg"
                    mode="aspectFit"
                    class="w-4 h-4"
                  ></image>
                </view>
              </view>
            </view>
            <view class="flex items-center text-xs text-gray-400">
              <text>更新时间: {{ updateTime }}</text>
            </view>
          </view>
          <view class="grid grid-cols-2 gap-3">
            <MonitorStatCard label="当前水位" :value="currentLevel" unit="m" />
            <MonitorStatCard label="当前流量" :value="currentFlow" unit="m³/s" />
          </view>
        </SkCard>

        <!-- 趋势图卡片 -->
        <SkCard title="水位趋势">
          <TrendChart
            :data="trendData"
            x-field="tm"
            y-field="z1"
            unit="m"
            :height="200"
          />
        </SkCard>

        <!-- 历史记录卡片 -->
        <SkCard title="历史记录">
          <view class="space-y-3 py-2">
            <view
              v-for="item in historyList"
              :key="item.tm"
              class="flex items-center justify-between py-3 border-b border-gray-100 last:border-b-0"
            >
              <view>
                <text class="text-sm text-gray-900 block">{{
                  formatTime(item.tm)
                }}</text>
                <text class="text-xs text-gray-400 mt-1 block"
                  >水位: {{ formatNum(item.z1) }} m</text
                >
              </view>
              <view class="text-right">
                <text class="text-sm font-medium text-gray-700"
                  >{{ formatNum(item.z1) }} m</text
                >
                <text class="text-xs text-gray-400 block mt-1"
                  >流量: {{ formatNum(item.q1) }} m³/s</text
                >
              </view>
            </view>

            <!-- 加载更多 -->
            <view v-if="historyList.length > 0" class="py-3 text-center">
              <text
                v-if="hasMore"
                class="text-sm text-primary"
                @click="loadMore"
                >加载更多</text
              >
              <text v-else class="text-sm text-gray-400">没有更多数据了</text>
            </view>

            <!-- 空数据 -->
            <SkEmpty v-if="historyList.length === 0 && !historyLoading" text="暂无历史记录" />
          </view>
        </SkCard>
      </template>
    </view>
  </view>
</template>

<script setup>
/**
 * 水位监测页面
 * 功能：当前水位、更新时间、趋势图、历史记录
 */
import { ref, computed } from 'vue'
import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
import { formatDate, formatNum, getDefaultDateRange } from '@/utils/format.js'
import { getWaterLevelList, getWaterLevelPage } from '@/services/water.js'
import SkCard from '@/components/common/SkCard.vue'
import SkLoading from '@/components/common/SkLoading.vue'
import SkEmpty from '@/components/common/SkEmpty.vue'
import TrendChart from '@/components/business/TrendChart.vue'
import MonitorStatCard from '@/components/business/MonitorStatCard.vue'

const loading = ref(false)
const refreshing = ref(false)
const latestData = ref(null)
const trendData = ref([])

const historyList = ref([])
const historyLoading = ref(false)
const hasMore = ref(true)
const currentPage = ref(1)
const pageSize = 10

const currentLevel = computed(() => formatNum(latestData.value?.z1))
const currentFlow = computed(() => formatNum(latestData.value?.q1))
const updateTime = computed(() =>
  latestData.value?.tm
    ? formatDate(latestData.value.tm, 'YYYY-MM-DD HH:mm')
    : '--',
)

function formatTime(val) {
  return val ? formatDate(val, 'MM-DD HH:mm') : '--'
}



onLoad(() => {
  loadData()
})

onPullDownRefresh(() => {
  refreshData().then(() => uni.stopPullDownRefresh())
})

async function loadData() {
  loading.value = true
  try {
    await Promise.all([loadLatestAndTrend(), loadHistory(true)])
  } finally {
    loading.value = false
  }
}

async function refreshData() {
  refreshing.value = true
  try {
    await Promise.all([loadLatestAndTrend(), loadHistory(true)])
  } finally {
    refreshing.value = false
  }
}

async function loadLatestAndTrend() {
  const { startDate, endDate } = getDefaultDateRange()
  const res = await getWaterLevelList({ startDate, endDate })
  const list = res.data || []
  if (list.length > 0) {
    latestData.value = list[list.length - 1]
  }
  // 趋势图取最近最多 50 条
  trendData.value = list.slice(-50)
}

async function loadHistory(reset = false) {
  if (historyLoading.value) return
  if (reset) {
    currentPage.value = 1
    hasMore.value = true
  }
  if (!hasMore.value) return

  historyLoading.value = true
  try {
    const { startDate, endDate } = getDefaultDateRange()
    const res = await getWaterLevelPage({
      page: currentPage.value,
      size: pageSize,
      startDate,
      endDate,
    })
    const records = res.data?.list || []
    if (reset) {
      historyList.value = records
    } else {
      historyList.value = [...historyList.value, ...records]
    }
    if (records.length < pageSize) {
      hasMore.value = false
    } else {
      currentPage.value++
    }
  } finally {
    historyLoading.value = false
  }
}

function loadMore() {
  loadHistory(false)
}
</script>

<style scoped>
</style>
