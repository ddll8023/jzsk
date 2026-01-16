/**
 * 地表位移监测 Composable
 * 功能：管理地表位移数据查询、图表、表格的状态和逻辑
 */
import { ref, computed, reactive } from 'vue'
import { getDisplacementHistory } from '@/api/dam'

/**
 * 站点配置（固定8个站点）
 */
const stationOptions = [
  { stationId: 33210, name: 'LJ1-1' },
  { stationId: 33214, name: 'LJ1-2' },
  { stationId: 33216, name: 'LJ1-3' },
  { stationId: 33212, name: 'LJ1-4' },
  { stationId: 33215, name: 'LT2-1' },
  { stationId: 33211, name: 'LT2-2' },
  { stationId: 33217, name: 'LT2-3' },
  { stationId: 33213, name: 'LT2-4' }
]

/**
 * 格式化日期为 yyyy-MM-dd HH:mm:ss
 */
export function formatDateTime(date) {
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  const h = String(date.getHours()).padStart(2, '0')
  const min = String(date.getMinutes()).padStart(2, '0')
  const s = String(date.getSeconds()).padStart(2, '0')
  return `${y}-${m}-${d} ${h}:${min}:${s}`
}

/**
 * 计算统计值
 */
function calcStats(arr) {
  if (!arr.length) return { mean: '-', max: '-', min: '-', std: '-' }
  const n = arr.length
  const mean = arr.reduce((a, b) => a + b, 0) / n
  const max = Math.max(...arr)
  const min = Math.min(...arr)
  const std = Math.sqrt(arr.reduce((a, b) => a + Math.pow(b - mean, 2), 0) / n)
  return {
    mean: mean.toFixed(4),
    max: max.toFixed(4),
    min: min.toFixed(4),
    std: std.toFixed(4)
  }
}

/**
 * 地表位移数据查询 Composable
 */
export function useSurfaceDisplacement() {
  // 状态
  const loading = ref(false)
  const tableData = ref([])
  const chartData = ref([])
  const statRows = ref([])
  const latestData = ref(null)

  // 查询参数
  const query = reactive({
    stationId: 33210,
    quickType: 'today',
    dateRange: [],
    displayMode: 'chart'
  })

  // 当前站点名称
  const currentStationName = computed(() => {
    const station = stationOptions.find(s => s.stationId === query.stationId)
    return station?.name || ''
  })

  // 最新采集时间
  const latestCollectTime = computed(() => latestData.value?.collectTime || '')

  // 统计卡片数据
  const statCards = computed(() => {
    if (query.quickType === 'today' && latestData.value) {
      return [
        { label: '最新X位移', value: latestData.value.gpsTotalX ?? '-', unit: 'mm' },
        { label: '最新Y位移', value: latestData.value.gpsTotalY ?? '-', unit: 'mm' },
        { label: '最新Z位移', value: latestData.value.gpsTotalZ ?? '-', unit: 'mm' },
        { label: '最新合位移', value: latestData.value.displacement3d ?? '-', unit: 'mm' },
        { label: '最新水平位移', value: latestData.value.displacement2d ?? '-', unit: 'mm' }
      ]
    }
    // 非今天显示平均值
    const stat = {}
    statRows.value.forEach(row => {
      if (row.attr.includes('X位移')) stat.x = row.mean
      if (row.attr.includes('Y位移')) stat.y = row.mean
      if (row.attr.includes('Z位移')) stat.z = row.mean
      if (row.attr.includes('合位移')) stat.d3 = row.mean
      if (row.attr.includes('水平位移')) stat.d2 = row.mean
    })
    return [
      { label: '平均X位移', value: stat.x !== undefined ? Number(stat.x).toFixed(1) : '-', unit: 'mm' },
      { label: '平均Y位移', value: stat.y !== undefined ? Number(stat.y).toFixed(1) : '-', unit: 'mm' },
      { label: '平均Z位移', value: stat.z !== undefined ? Number(stat.z).toFixed(1) : '-', unit: 'mm' },
      { label: '平均合位移', value: stat.d3 !== undefined ? Number(stat.d3).toFixed(1) : '-', unit: 'mm' },
      { label: '平均水平位移', value: stat.d2 !== undefined ? Number(stat.d2).toFixed(1) : '-', unit: 'mm' }
    ]
  })


  /**
   * 设置快捷时间范围
   */
  function setQuickDateRange(type) {
    query.quickType = type
    const now = new Date()
    let start, end

    switch (type) {
      case 'today':
        start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0)
        end = now
        break
      case 'yesterday':
        start = new Date(now.getFullYear(), now.getMonth(), now.getDate() - 1, 0, 0, 0)
        end = new Date(now.getFullYear(), now.getMonth(), now.getDate() - 1, 23, 59, 59)
        break
      case 'last15':
        start = new Date(now.getTime() - 14 * 24 * 60 * 60 * 1000)
        end = now
        break
      case 'month':
        start = new Date(now.getFullYear(), now.getMonth(), 1, 0, 0, 0)
        end = now
        break
      default:
        return
    }
    query.dateRange = [formatDateTime(start), formatDateTime(end)]
  }

  /**
   * 获取数据
   */
  async function fetchData() {
    if (!query.dateRange?.length) return

    loading.value = true
    try {
      const params = {
        startTime: query.dateRange[0],
        endTime: query.dateRange[1],
        sensor: 'L1_GP',
        stationIds: query.stationId,
        projectId: 1681,
        page: 1,
        size: 1000
      }

      const res = await getDisplacementHistory(params)
      const records = res.data?.records || []

      // 转换数据格式
      tableData.value = records.map(r => {
        const row = {
          collectTime: r.collectTime,
          stationName: r.stationName,
          deviceSn: r.deviceSn,
          gpsTotalX: null,
          gpsTotalY: null,
          gpsTotalZ: null,
          displacement3d: null,
          displacement2d: null
        }
        ;(r.keyValues || []).forEach(kv => {
          if (kv.key === 'gpsTotalX') row.gpsTotalX = parseFloat(kv.value)
          if (kv.key === 'gpsTotalY') row.gpsTotalY = parseFloat(kv.value)
          if (kv.key === 'gpsTotalZ') row.gpsTotalZ = parseFloat(kv.value)
          if (kv.key === 'displacement3d') row.displacement3d = parseFloat(kv.value)
          if (kv.key === 'displacement2d') row.displacement2d = parseFloat(kv.value)
        })
        return row
      })

      // 最新数据
      latestData.value = tableData.value[0] || null

      // 图表数据（时间正序）
      chartData.value = tableData.value.slice().reverse().map(d => ({
        time: d.collectTime,
        x: d.gpsTotalX,
        y: d.gpsTotalY,
        z: d.gpsTotalZ,
        d3: d.displacement3d,
        d2: d.displacement2d
      }))

      // 计算统计
      calcStatsTable()
    } catch (err) {
      console.error('[fetchData] 获取位移数据失败:', err)
      tableData.value = []
      chartData.value = []
      latestData.value = null
      statRows.value = []
    } finally {
      loading.value = false
    }
  }

  /**
   * 计算统计表格
   */
  function calcStatsTable() {
    const arrX = tableData.value.map(d => d.gpsTotalX).filter(v => typeof v === 'number')
    const arrY = tableData.value.map(d => d.gpsTotalY).filter(v => typeof v === 'number')
    const arrZ = tableData.value.map(d => d.gpsTotalZ).filter(v => typeof v === 'number')
    const arr3d = tableData.value.map(d => d.displacement3d).filter(v => typeof v === 'number')
    const arr2d = tableData.value.map(d => d.displacement2d).filter(v => typeof v === 'number')

    statRows.value = [
      { attr: 'X位移(mm)', ...calcStats(arrX) },
      { attr: 'Y位移(mm)', ...calcStats(arrY) },
      { attr: 'Z位移(mm)', ...calcStats(arrZ) },
      { attr: '合位移(mm)', ...calcStats(arr3d) },
      { attr: '水平位移(mm)', ...calcStats(arr2d) }
    ]
  }

  /**
   * 导出Excel
   */
  function exportData() {
    if (!tableData.value?.length) {
      alert('没有数据可导出！')
      return
    }

    const headers = ['时间', '站点名称', '设备SN', 'X位移(mm)', 'Y位移(mm)', 'Z位移(mm)', '合位移(mm)', '水平位移(mm)']
    const rows = tableData.value.map(item => [
      item.collectTime,
      item.stationName,
      item.deviceSn,
      item.gpsTotalX,
      item.gpsTotalY,
      item.gpsTotalZ,
      item.displacement3d,
      item.displacement2d
    ])

    let csvContent = '\ufeff' + headers.join(',') + '\n'
    rows.forEach(row => {
      csvContent += row.map(e => `"${e ?? ''}"`).join(',') + '\n'
    })

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = `地表位移数据_${currentStationName.value}.csv`
    link.click()
  }

  return {
    // 常量
    stationOptions,
    // 状态
    loading,
    tableData,
    chartData,
    statRows,
    latestData,
    query,
    // 计算属性
    currentStationName,
    latestCollectTime,
    statCards,
    // 方法
    setQuickDateRange,
    fetchData,
    exportData,
    formatDateTime
  }
}
