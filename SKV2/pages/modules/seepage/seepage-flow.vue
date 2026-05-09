<template>
  <view class="min-h-screen bg-gray-50">
    <view class="p-4">
      <!-- 加载状态 -->
      <SkLoading v-if="loading" />

      <template v-else>
        <!-- 测站选择 + 实时数据卡片 -->
        <SkCard title="实时监测">
          <view class="py-3">
            <!-- 测站选择器 -->
            <view class="mb-4">
              <picker
                mode="selector"
                :range="stationList"
                range-key="name"
                :value="currentStationIndex"
                @change="handleStationChange"
              >
                <view
                  class="flex items-center justify-between bg-gray-50 rounded-lg px-4 py-3"
                >
                  <view class="flex items-center">
                    <view class="w-1 h-4 bg-primary rounded mr-2"></view>
                    <text class="text-sm font-medium text-gray-700">{{
                      stationList[currentStationIndex]?.name || '选择测站'
                    }}</text>
                  </view>
                  <text class="text-xs text-gray-400">▼ 切换测站</text>
                </view>
              </picker>
            </view>

            <!-- 实时数据大字 + 刷新 -->
            <view class="flex items-center justify-between mb-4">
              <view class="flex items-baseline">
                <text class="text-4xl font-bold text-primary">{{
                  currentFlow
                }}</text>
                <text class="text-sm text-gray-400 ml-2">L/s</text>
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
          <view class="grid grid-cols-1 gap-3">
            <MonitorStatCard
              label="当前渗流量"
              :value="currentFlow"
              unit="L/s"
            />
          </view>
        </SkCard>

        <!-- 趋势图卡片 -->
        <SkCard title="渗流量趋势">
          <TrendChart
            :data="trendData"
            x-field="tm"
            y-field="flowDisplay"
            unit="L/s"
            :height="200"
          />
        </SkCard>

        <!-- 历史记录卡片 -->
        <SkCard title="历史记录">
          <view class="space-y-3 py-2">
            <view
              v-for="item in historyList"
              :key="item.id + '-' + item.tm"
              class="flex items-center justify-between py-3 border-b border-gray-100 last:border-b-0"
            >
              <view>
                <text class="text-sm text-gray-900 block">{{
                  formatTime(item.tm)
                }}</text>
                <text class="text-xs text-gray-400 mt-1 block"
                  >测站: {{ item.stcd || '--' }}</text
                >
              </view>
              <view class="text-right">
                <text class="text-sm font-medium text-primary block"
                  >{{ formatFlow(item.q1) }} L/s</text
                >
                <text v-if="item.remarks" class="text-xs text-gray-400 block mt-1"
                  >{{ item.remarks }}</text
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
              <text v-else class="text-sm text-gray-400"
                >没有更多数据了</text
              >
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
 * 渗流量监测页面
 * 功能：测站选择、当前渗流量、趋势图、历史记录
 */
import { ref, computed } from 'vue'
import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
import { formatDate, getDefaultDateRange } from '@/utils/format.js'
import { getSeepageFlowPage } from '@/services/dam.js'
import SkCard from '@/components/common/SkCard.vue'
import SkLoading from '@/components/common/SkLoading.vue'
import SkEmpty from '@/components/common/SkEmpty.vue'
import TrendChart from '@/components/business/TrendChart.vue'
import MonitorStatCard from '@/components/business/MonitorStatCard.vue'

const STATION_LIST = [
  { stcd: '4211823043', name: '主坝0+200坝脚量水堰' },
  { stcd: '4211822043', name: '主坝0+400坝脚量水堰' },
]

const loading = ref(false)
const refreshing = ref(false)
const stationList = ref(STATION_LIST)
const currentStationIndex = ref(0)
const latestData = ref(null)
const trendData = ref([])

const historyList = ref([])
const historyLoading = ref(false)
const hasMore = ref(true)
const currentPage = ref(1)
const pageSize = 10

const currentStcd = computed(
  () => stationList.value[currentStationIndex.value]?.stcd || '',
)

const currentFlow = computed(() => {
  if (!latestData.value?.q1) return '--'
  return formatFlow(latestData.value.q1)
})

const updateTime = computed(() =>
  latestData.value?.tm
    ? formatDate(latestData.value.tm, 'YYYY-MM-DD HH:mm')
    : '--',
)

function formatTime(val) {
  return val ? formatDate(val, 'MM-DD HH:mm') : '--'
}

function formatFlow(val) {
  if (val === null || val === undefined || val === '') return '--'
  const num = Number(val)
  if (isNaN(num)) return '--'
  // 后端 q1 单位 m³/s，展示转为 L/s
  const flowLs = num * 1000
  return flowLs.toFixed(2)
}


function toFlowDisplay(q1) {
  if (q1 === null || q1 === undefined || q1 === '') return null
  const num = Number(q1)
  if (isNaN(num)) return null
  return num * 1000
}

onLoad(() => {
  loadAllData()
})

onPullDownRefresh(() => {
  refreshData().then(() => uni.stopPullDownRefresh())
})

function handleStationChange(e) {
  currentStationIndex.value = e.detail.value
  loadAllData()
}

async function loadAllData() {
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
  const stcd = currentStcd.value
  if (!stcd) return
  const { startTime, endTime } = getDefaultDateRange({ startKey: 'startTime', endKey: 'endTime' })
  try {
    const res = await getSeepageFlowPage({
      page: 1,
      size: 50,
      stcd,
      startTime,
      endTime,
    })
    const list = res.data?.list || []
    if (list.length > 0) {
      latestData.value = list[0]
    } else {
      latestData.value = null
    }
    // 趋势图数据：转为 flowDisplay 字段供 TrendChart 使用
    trendData.value = list
      .filter((item) => item.q1 !== null && item.q1 !== undefined)
      .map((item) => ({
        ...item,
        flowDisplay: toFlowDisplay(item.q1),
      }))
      .reverse()
  } catch (e) {
    latestData.value = null
    trendData.value = []
  }
}

async function loadHistory(reset = false) {
  if (historyLoading.value) return
  const stcd = currentStcd.value
  if (!stcd) return
  if (reset) {
    currentPage.value = 1
    hasMore.value = true
  }
  if (!hasMore.value) return

  historyLoading.value = true
  try {
    const { startTime, endTime } = getDefaultDateRange({ startKey: 'startTime', endKey: 'endTime' })
    const res = await getSeepageFlowPage({
      page: currentPage.value,
      size: pageSize,
      stcd,
      startTime,
      endTime,
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
