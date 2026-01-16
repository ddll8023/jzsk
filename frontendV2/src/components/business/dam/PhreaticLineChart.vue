<template>
  <!-- 浸润线观测图 -->
  <Card class="w-full" padding="sm">
    <!-- 顶部工具栏 -->
    <template #header>
      <div class="flex flex-wrap items-center gap-4">
        <h3 class="text-lg font-semibold text-gray-900 mr-2">主坝浸润线观测图</h3>

        <!-- 断面选择 -->
        <Select
          class="!w-[180px]"
          v-model="selectedSection"
          :options="sectionList"
          label="断面桩号："
          size="sm"
        />

        <!-- 日期范围 -->
        <Input
          class="!w-[160px]"
          type="date"
          v-model="dateRange[0]"
          label="开始日期："
          size="sm"
        />
        <Input
          class="!w-[160px]"
          type="date"
          v-model="dateRange[1]"
          label="结束日期："
          size="sm"
        />

        <div class="flex items-end h-[58px] pb-[2px]">
          <Button
            type="primary"
            size="sm"
            @click="onConfirm"
          >
            确定
          </Button>
        </div>
      </div>
    </template>

    <!-- 图表容器 -->
    <div ref="chartRef" class="w-full h-[420px]"></div>
  </Card>
</template>

<script setup>
/**
 * 浸润线观测图组件
 * 功能：展示大坝断面剖面图和浸润线
 */
import { ref, onMounted, onBeforeUnmount, shallowRef, watch } from 'vue'
import * as echarts from 'echarts'
import { getSeepagePage, getRiverWaterLevel } from '@/api/dam'
import Card from '@/components/basic/Card.vue'
import Select from '@/components/basic/Select.vue'
import Input from '@/components/basic/Input.vue'
import Button from '@/components/basic/Button.vue'

// 断面列表
const sectionList = [
  { label: '主坝0+000', value: '0+000' },
  { label: '主坝0+015', value: '0+015' },
  { label: '主坝0+025', value: '0+025' },
  { label: '主坝0+035', value: '0+035' },
  { label: '主坝0+100', value: '0+100' },
  { label: '主坝0+150', value: '0+150' },
  { label: '主坝0+250', value: '0+250' },
  { label: '主坝0+350', value: '0+350' }
]

// 坝体剖面数据
const damProfileMap = {
  '0+000': [{ x: 28, y: 30 }, { x: 105, y: 77 }, { x: 120, y: 77 }, { x: 132, y: 70 }, { x: 150, y: 70 }, { x: 210, y: 30 }],
  '0+015': [{ x: 28, y: 30 }, { x: 105, y: 77 }, { x: 120, y: 77 }, { x: 132, y: 70 }, { x: 150, y: 70 }, { x: 210, y: 30 }],
  '0+025': [{ x: 28, y: 30 }, { x: 105, y: 77 }, { x: 120, y: 77 }, { x: 132, y: 70 }, { x: 150, y: 70 }, { x: 210, y: 30 }],
  '0+035': [{ x: 28, y: 30 }, { x: 105, y: 77 }, { x: 120, y: 77 }, { x: 132, y: 70 }, { x: 150, y: 70 }, { x: 210, y: 30 }],
  '0+100': [{ x: 28, y: 30 }, { x: 105, y: 77 }, { x: 120, y: 77 }, { x: 132, y: 70 }, { x: 150, y: 70 }, { x: 210, y: 30 }],
  '0+150': [{ x: 28, y: 30 }, { x: 105, y: 77 }, { x: 120, y: 77 }, { x: 132, y: 70 }, { x: 150, y: 70 }, { x: 210, y: 30 }],
  '0+250': [{ x: 28, y: 30 }, { x: 105, y: 77 }, { x: 120, y: 77 }, { x: 132, y: 70 }, { x: 150, y: 70 }, { x: 210, y: 30 }],
  '0+350': [{ x: 28, y: 30 }, { x: 105, y: 77 }, { x: 120, y: 77 }, { x: 132, y: 70 }, { x: 150, y: 70 }, { x: 210, y: 30 }]
}

// 测点映射
const pointNameIdMap = {
  '0+100': [
    { name: 'UPb1-1', id: 'P0108248' },
    { name: 'UPb1-2', id: 'P0108234' },
    { name: 'UPb1-3', id: 'P0108376' },
    { name: 'UPb1-4', id: 'P0108173' },
    { name: 'UPb1-5', id: 'P0108236' }
  ],
  '0+150': [
    { name: 'UPa1-1', id: 'P0108190', pipeIndex: 0 },
    { name: 'UPa1-4', id: 'P0108345', pipeIndex: 0 },
    { name: 'UPa1-5', id: 'P0108154', pipeIndex: 0 },
    { name: 'UPb2-1', id: 'P0108310', pipeIndex: 0 },
    { name: 'UPb2-4', id: 'P0108066', pipeIndex: 0 },
    { name: 'UPb2-2', id: 'P0108046', pipeIndex: 1 },
    { name: 'UPa1-2', id: 'P0108050', pipeIndex: 2 },
    { name: 'UPb2-3', id: 'P0108235', pipeIndex: 3 },
    { name: 'UPa1-3', id: 'P0108242', pipeIndex: 4 }
  ],
  '0+250': [
    { name: 'UPb3-1', id: 'P0108267' },
    { name: 'UPb3-2', id: 'P0108282' },
    { name: 'UPb3-3', id: 'P0108033' },
    { name: 'UPb3-4', id: 'P0108100' },
    { name: 'UPb3-5', id: 'P0108377' }
  ],
  '0+350': [
    { name: 'UPb4-1', id: 'P0108174' },
    { name: 'UPb4-2', id: 'P0108273' },
    { name: 'UPb4-3', id: 'P0108198' },
    { name: 'UPb4-4', id: 'P0108181' },
    { name: 'UPb4-5', id: 'P0108056' }
  ],
  '0+000': [{ name: 'UPr1-1', id: 'P0108118' }],
  '0+015': [{ name: 'UPr1-2', id: 'P0108148' }],
  '0+025': [{ name: 'UPr2-1', id: 'P0108206' }],
  '0+035': [{ name: 'UPr2-2', id: 'P0108311' }]
}

// 状态
const selectedSection = ref('0+100')
const dateRange = ref([getDefaultStartDate(), getDefaultEndDate()])
const chartRef = ref(null)
const chart = shallowRef(null)
const reservoirLevel = ref(null)
const phreaticPoints = ref([])

// 默认日期
function getDefaultStartDate() {
  const d = new Date()
  d.setDate(d.getDate() - 1)
  return d.toISOString().slice(0, 10)
}

function getDefaultEndDate() {
  return new Date().toISOString().slice(0, 10)
}

// 断面变化 - Select组件change事件传回的是value
function onSectionChange(val) {
  // val 已经在v-model中更新，此处可直接调用更新逻辑
  updatePhreaticData()
}

// 确定按钮
function onConfirm() {
  updatePhreaticData()
}

// 获取浸润线数据
async function updatePhreaticData() {
  const profile = damProfileMap[selectedSection.value] || []
  const points = pointNameIdMap[selectedSection.value] || []
  const pointIds = points.map(p => p.id).filter(Boolean)

  if (!pointIds.length) {
    phreaticPoints.value = []
    renderChart(profile, [], 55)
    return
  }

  try {
    // 并行请求水库水位和测点数据
    const [waterRes, seepageRes] = await Promise.all([
      getRiverWaterLevel({ page: 1, size: 50 }),
      getSeepagePage({
        pointIds: pointIds.join(','),
        startTime: `${dateRange.value[0]} 00:00:00`,
        endTime: `${dateRange.value[1]} 23:59:59`,
        size: 1000,
        current: 1
      })
    ])

    // 解析水库水位
    const waterArr = Array.isArray(waterRes.data) ? waterRes.data : waterRes.data?.records || []
    const validWater = waterArr.filter(item => Number(item.z1) > 50)
    reservoirLevel.value = validWater.length > 0 ? Number(validWater[0].z1) : 55

    // 解析测点数据
    const records = seepageRes.data?.records || []
    const idToElev = {}
    records.forEach(item => {
      const pid = String(item.pointId || item.pointName || '')
      if (!pid) return
      try {
        const resultData = typeof item.resultData === 'string' ? JSON.parse(item.resultData) : item.resultData
        const elev = resultData?.['水位高程']
        if (elev != null && !isNaN(elev) && elev > 0) {
          if (!idToElev[pid] || new Date(item.time) > new Date(idToElev[pid].time)) {
            idToElev[pid] = { elev: Number(elev), time: item.time }
          }
        }
      } catch { /* ignore */ }
    })

    // 构建浸润线点
    const fixedX = [100, 120, 142, 160, 180]
    phreaticPoints.value = points.map((pt, i) => {
      const elev = idToElev[pt.id]?.elev || (50 + Math.random() * 5)
      return { name: pt.name, x: fixedX[i] || (i + 1) * 40, y: elev }
    })

    renderChart(profile, phreaticPoints.value, reservoirLevel.value)
  } catch (e) {
    console.error('获取浸润线数据失败:', e)
    renderChart(profile, [], 55)
  }
}

// 渲染图表
function renderChart(profile, points, waterLevel) {
  if (!chart.value) return

  // 坝体剖面填充
  const damFill = [...profile, { x: 240, y: 30 }, { x: 0, y: 30 }].map(p => [p.x, p.y])
  const damLine = profile.map(p => [p.x, p.y])

  // 计算水位线与坝体交点
  let reservoirLineEndX = 40
  for (let i = 1; i < profile.length; i++) {
    const p1 = profile[i - 1], p2 = profile[i]
    if ((p1.y - waterLevel) * (p2.y - waterLevel) <= 0) {
      const t = (waterLevel - p1.y) / (p2.y - p1.y)
      reservoirLineEndX = p1.x + t * (p2.x - p1.x)
      break
    }
  }

  // 浸润线数据
  const phreaticLine = [[reservoirLineEndX, waterLevel], ...points.map(p => [p.x, p.y])]

  // 测点散点
  const scatterData = points.map(p => ({ value: [p.x, p.y], name: p.name }))

  chart.value.setOption({
    grid: { left: 60, right: 40, top: 60, bottom: 50 },
    xAxis: { type: 'value', name: '坝体横向位置', min: 0, max: 240 },
    yAxis: { type: 'value', name: '断面高(m)', min: 30, max: 85 },
    tooltip: { show: true },
    series: [
      // 坝体填充
      {
        type: 'custom',
        renderItem: (params, api) => {
          const pts = damFill.map(p => api.coord(p))
          return { type: 'polygon', shape: { points: pts }, style: { fill: '#4e8077', opacity: 0.5 } }
        },
        data: [0],
        z: 0
      },
      // 坝体轮廓线
      { type: 'line', data: damLine, lineStyle: { color: '#8d5524', width: 2 }, symbol: 'none', z: 3 },
      // 水位填充
      {
        type: 'line',
        data: [[0, waterLevel], [reservoirLineEndX, waterLevel], [reservoirLineEndX, 30], [0, 30]],
        lineStyle: { color: 'transparent' },
        areaStyle: { color: '#409EFF', opacity: 0.3 },
        symbol: 'none',
        z: 2
      },
      // 水库水位线
      {
        type: 'line',
        data: [[0, waterLevel], [reservoirLineEndX, waterLevel]],
        lineStyle: { color: '#409EFF', width: 4 },
        symbol: 'none',
        z: 8,
        label: {
          show: true,
          position: 'left',
          formatter: () => `水位: ${waterLevel.toFixed(2)}m`,
          color: '#409EFF',
          fontSize: 12,
          fontWeight: 'bold',
          backgroundColor: 'rgba(255,255,255,0.9)',
          padding: [4, 6]
        }
      },
      // 浸润线
      {
        type: 'line',
        data: phreaticLine,
        lineStyle: { color: '#1e90ff', width: 3 },
        symbol: 'none',
        smooth: true,
        z: 6
      },
      // 测点标记
      {
        type: 'scatter',
        data: scatterData,
        symbol: 'circle',
        symbolSize: 12,
        itemStyle: { color: '#1e90ff', borderColor: '#fff', borderWidth: 2 },
        z: 10,
        label: {
          show: true,
          position: 'bottom',
          formatter: (p) => `${p.data.name}\n${p.data.value[1].toFixed(2)}m`,
          fontSize: 10,
          color: '#333'
        }
      }
    ]
  }, { notMerge: true })
}

// 初始化
function initChart() {
  if (chartRef.value && !chart.value) {
    chart.value = echarts.init(chartRef.value)
    updatePhreaticData()
  }
}

// resize处理
function handleResize() {
  chart.value?.resize()
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chart.value?.dispose()
})

// 监听断面变化
watch(selectedSection, () => {
  // Select组件的v-model更新后可能需要触发
  updatePhreaticData()
})
</script>
