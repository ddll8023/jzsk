<template>
  <view class="min-h-screen bg-gray-50">
    <view class="p-4">
      <!-- 加载状态 -->
      <SkLoading v-if="loading" />

      <template v-else>
        <!-- 测点选择 + 实时数据卡片 -->
        <SkCard title="实时监测">
          <view class="py-3">
            <!-- 测点选择器 -->
            <view class="mb-4">
              <picker
                mode="selector"
                :range="pointList"
                range-key="name"
                :value="currentPointIndex"
                @change="handlePointChange"
              >
                <view
                  class="flex items-center justify-between bg-gray-50 rounded-lg px-4 py-3"
                >
                  <view class="flex items-center">
                    <view class="w-1 h-4 bg-primary rounded mr-2"></view>
                    <text class="text-sm font-medium text-gray-700">{{
                      (currentPoint && currentPoint.name) || '选择测点'
                    }}</text>
                  </view>
                  <text class="text-xs text-gray-400">▼ 切换测点</text>
                </view>
              </picker>
            </view>

            <!-- 实时数据大字 + 刷新 -->
            <view class="flex items-center justify-between mb-4">
              <view class="flex items-baseline">
                <text class="text-4xl font-bold text-primary">{{
                  latestPressure
                }}</text>
                <text class="text-sm text-gray-400 ml-2">KPa</text>
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
          <view class="grid grid-cols-3 gap-3">
            <MonitorStatCard
              label="水压"
              :value="latestParsed.pressure"
              unit="KPa"
            />
            <MonitorStatCard
              label="温度"
              :value="latestParsed.temperature"
              unit="℃"
            />
            <MonitorStatCard
              label="模数"
              :value="latestParsed.modulus"
              unit="f"
            />
          </view>
        </SkCard>

        <!-- 趋势图卡片 -->
        <SkCard title="指标趋势">
          <template #header>
            <view class="flex bg-gray-100 rounded-lg p-1">
              <view
                v-for="tab in chartTabs"
                :key="tab.key"
                class="px-3 py-1 text-xs rounded-md transition-all"
                :class="
                  currentTab === tab.key
                    ? 'bg-white text-primary shadow-sm font-medium'
                    : 'text-gray-500'
                "
                @click="switchTab(tab.key)"
              >
                {{ tab.label }}
              </view>
            </view>
          </template>
          <TrendChart
            :data="trendData"
            x-field="time"
            y-field="value"
            :unit="currentTabUnit"
            :height="200"
          />
        </SkCard>

        <!-- 历史记录卡片 -->
        <SkCard title="历史记录">
          <view class="space-y-3 py-2">
            <view
              v-for="item in historyList"
              :key="item.time"
              class="flex items-center justify-between py-3 border-b border-gray-100 last:border-b-0"
            >
              <view>
                <text class="text-sm text-gray-900 block">{{
                  formatTime(item.time)
                }}</text>
                <text class="text-xs text-gray-400 mt-1 block"
                  >测点: {{ item.pointName || '--' }}</text
                >
              </view>
              <view class="text-right">
                <text class="text-sm font-medium text-primary block"
                  >水压: {{ parseItem(item).pressure }} KPa</text
                >
                <text class="text-xs text-gray-400 block mt-1"
                  >温度: {{ parseItem(item).temperature }} ℃ | 模数:
                  {{ parseItem(item).modulus }} f</text
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
 * 渗压监测页面
 * 功能：测点选择、实时水压/温度/模数、指标切换趋势图、历史记录
 */
import { ref, computed } from 'vue'
import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
import { formatDate, formatNum, getDefaultDateRange } from '@/utils/format.js'
import {
  getDamPoints,
  getSeepagePage,
  getTimeWaterPressure,
  getTimeTemperature,
} from '@/services/dam.js'
import SkCard from '@/components/common/SkCard.vue'
import SkLoading from '@/components/common/SkLoading.vue'
import SkEmpty from '@/components/common/SkEmpty.vue'
import TrendChart from '@/components/business/TrendChart.vue'
import MonitorStatCard from '@/components/business/MonitorStatCard.vue'

const chartTabs = [
  { key: 'pressure', label: '水压', unit: 'KPa' },
  { key: 'temperature', label: '温度', unit: '℃' },
]

const loading = ref(false)
const refreshing = ref(false)
const pointList = ref([])
const currentPointIndex = ref(0)
const currentTab = ref('pressure')
const latestData = ref(null)
const trendData = ref([])

const historyList = ref([])
const historyLoading = ref(false)
const hasMore = ref(true)
const currentPage = ref(1)
const pageSize = 10

const currentPoint = computed(
  () => pointList.value[currentPointIndex.value] || null,
)
const currentTabUnit = computed(
  () => chartTabs.find((t) => t.key === currentTab.value)?.unit || '',
)

const latestParsed = computed(() => parseSeepageItem(latestData.value))

const latestPressure = computed(() => latestParsed.value.pressure)

const updateTime = computed(() =>
  latestData.value?.time
    ? formatDate(latestData.value.time, 'YYYY-MM-DD HH:mm')
    : '--',
)

function formatTime(val) {
  return val ? formatDate(val, 'MM-DD HH:mm') : '--'
}



function parseSeepageItem(item) {
  const empty = { pressure: '--', temperature: '--', modulus: '--' }
  if (!item) return empty
  let pressure = null
  let temperature = null
  let modulus = null

  // 解析 resultData（水压）
  if (item.resultData) {
    try {
      const obj =
        typeof item.resultData === 'string'
          ? JSON.parse(item.resultData)
          : item.resultData
      pressure = obj['水压'] ?? obj['P'] ?? null
    } catch (e) {
      /* ignore */
    }
  }

  // 解析 originalData（模数、温度）
  if (item.originalData) {
    try {
      const obj =
        typeof item.originalData === 'string'
          ? JSON.parse(item.originalData)
          : item.originalData
      modulus = obj['模数'] ?? obj['F'] ?? null
      temperature = obj['温度'] ?? obj['T'] ?? null
    } catch (e) {
      /* ignore */
    }
  }

  return {
    pressure: formatNum(pressure),
    temperature: formatNum(temperature),
    modulus: formatNum(modulus),
  }
}

function parseItem(item) {
  return parseSeepageItem(item)
}

onLoad(async () => {
  await loadPoints()
})

onPullDownRefresh(() => {
  refreshData().then(() => uni.stopPullDownRefresh())
})

async function loadPoints() {
  loading.value = true
  try {
    const res = await getDamPoints()
    pointList.value = res.data || []
    if (pointList.value.length > 0) {
      currentPointIndex.value = 0
      await loadAllData()
    }
  } finally {
    loading.value = false
  }
}

function handlePointChange(e) {
  currentPointIndex.value = e.detail.value
  loadAllData()
}

function switchTab(key) {
  currentTab.value = key
  loadTrend()
}

async function loadAllData() {
  await Promise.all([loadLatest(), loadTrend(), loadHistory(true)])
}

async function refreshData() {
  refreshing.value = true
  try {
    await loadAllData()
  } finally {
    refreshing.value = false
  }
}

async function loadLatest() {
  const pointId = currentPoint.value?.pointId
  if (!pointId) return
  try {
    const res = await getSeepagePage({
      page: 1,
      size: 1,
      pointId,
      ...getDefaultDateRange({ startKey: 'startTime', endKey: 'endTime' }),
    })
    const list = res.data?.list || []
    latestData.value = list.length > 0 ? list[0] : null
  } catch (e) {
    latestData.value = null
  }
}

async function loadTrend() {
  const pointId = currentPoint.value?.pointId
  if (!pointId) {
    trendData.value = []
    return
  }
  const { startTime, endTime } = getDefaultDateRange({ startKey: 'startTime', endKey: 'endTime' })
  const params = { pointId, startTime, endTime }
  try {
    const fn =
      currentTab.value === 'temperature'
        ? getTimeTemperature
        : getTimeWaterPressure
    const res = await fn(params)
    trendData.value = res.data || []
  } catch (e) {
    trendData.value = []
  }
}

async function loadHistory(reset = false) {
  if (historyLoading.value) return
  const pointId = currentPoint.value?.pointId
  if (!pointId) return
  if (reset) {
    currentPage.value = 1
    hasMore.value = true
  }
  if (!hasMore.value) return

  historyLoading.value = true
  try {
    const { startTime, endTime } = getDefaultDateRange({ startKey: 'startTime', endKey: 'endTime' })
    const res = await getSeepagePage({
      page: currentPage.value,
      size: pageSize,
      pointId,
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
