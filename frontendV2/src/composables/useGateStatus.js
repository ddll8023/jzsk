/**
 * 闸门实时状态 Composable
 * 功能：封装闸门状态查询、数据处理、分页、导出等业务逻辑
 * 遵循 KISS/YAGNI 原则：只实现实际需要的功能
 */
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import request from '@/utils/request'

// 闸门列表配置
const GATE_LIST = [
  { code: 'dgq', name: '东干渠' },
  { code: 'dzdf', name: '电站蝶阀' },
  { code: 'qst', name: '取水塔' },
  { code: 'xgq', name: '西干渠' },
  { code: 'yhd', name: '溢洪道' }
]

// 字段映射表
const FIELD_LABEL_MAP = {
  'tm': '时间', 'TM': '时间',
  // 东干渠
  'dgq_M1_Ua': 'M1A相电压', 'dgq_M1_Ub': 'M1B相电压', 'dgq_M1_Uc': 'M1C相电压',
  'dgq_M1_Uab': 'M1AB线电压', 'dgq_M1_Ubc': 'M1BC线电压', 'dgq_M1_Uca': 'M1CA线电压',
  'dgq_M1_Ia': 'M1A相电流', 'dgq_M1_Ib': 'M1B相电流', 'dgq_M1_Ic': 'M1C相电流',
  'dgq_M1_KD': 'M1开度', 'dgq_M1_KDSD': 'M1开度设定',
  // 电站蝶阀
  'dzdf_M1_Ua': 'A相电压', 'dzdf_M1_Ub': 'B相电压', 'dzdf_M1_Uc': 'C相电压',
  'dzdf_M1_Uab': 'AB线电压', 'dzdf_M1_Ubc': 'BC线电压', 'dzdf_M1_Uca': 'CA线电压',
  'dzdf_M1_Ia': 'A相电流', 'dzdf_M1_Ib': 'B相电流', 'dzdf_M1_Ic': 'C相电流',
  'dzdf_M1_FIT': '流量', 'dzdf_M1_FIT_TOL': '累计流量', 'dzdf_M1_YW': '液位',
  // 取水塔M1/M2
  'qst_M1_Ua': 'M1A相电压', 'qst_M1_Ub': 'M1B相电压', 'qst_M1_Uc': 'M1C相电压',
  'qst_M1_Uab': 'M1AB线电压', 'qst_M1_Ia': 'M1A相电流', 'qst_M1_Ib': 'M1B相电流',
  'qst_M1_Ic': 'M1C相电流', 'qst_M1_KD': 'M1开度', 'qst_M1_KDSD': 'M1开度设定',
  'qst_M2_Ua': 'M2A相电压', 'qst_M2_Ub': 'M2B相电压', 'qst_M2_Uc': 'M2C相电压',
  'qst_M2_Uab': 'M2AB线电压', 'qst_M2_Ia': 'M2A相电流', 'qst_M2_Ib': 'M2B相电流',
  'qst_M2_Ic': 'M2C相电流', 'qst_M2_KD': 'M2开度', 'qst_M2_KDSD': 'M2开度设定',
  // 西干渠M1/M2
  'xgq_M1_Ua': 'M1A相电压', 'xgq_M1_Ub': 'M1B相电压', 'xgq_M1_Uc': 'M1C相电压',
  'xgq_M1_Uab': 'M1AB线电压', 'xgq_M1_Ubc': 'M1BC线电压', 'xgq_M1_Uca': 'M1CA线电压',
  'xgq_M1_Ia': 'M1A相电流', 'xgq_M1_Ib': 'M1B相电流', 'xgq_M1_Ic': 'M1C相电流',
  'xgq_M1_KD': 'M1开度', 'xgq_M1_KDSD': 'M1开度设定',
  'xgq_M2_Ua': 'M2A相电压', 'xgq_M2_Ub': 'M2B相电压', 'xgq_M2_Uc': 'M2C相电压',
  'xgq_M2_Uab': 'M2AB线电压', 'xgq_M2_Ia': 'M2A相电流', 'xgq_M2_Ib': 'M2B相电流',
  'xgq_M2_Ic': 'M2C相电流', 'xgq_M2_KD': 'M2开度', 'xgq_M2_KDSD': 'M2开度设定',
  // 溢洪道M1/M2/M3
  'yhd_M1_Ua': 'M1A相电压', 'yhd_M1_Ub': 'M1B相电压', 'yhd_M1_Uc': 'M1C相电压',
  'yhd_M1_Uab': 'M1AB线电压', 'yhd_M1_Ubc': 'M1BC线电压', 'yhd_M1_Uca': 'M1CA线电压',
  'yhd_M1_Ia': 'M1A相电流', 'yhd_M1_Ib': 'M1B相电流', 'yhd_M1_Ic': 'M1C相电流',
  'yhd_M1_KD': 'M1开度', 'yhd_M1_KDSD': 'M1开度设定',
  'yhd_M2_Ua': 'M2A相电压', 'yhd_M2_Ub': 'M2B相电压', 'yhd_M2_Uc': 'M2C相电压',
  'yhd_M2_Uab': 'M2AB线电压', 'yhd_M2_Ia': 'M2A相电流', 'yhd_M2_Ib': 'M2B相电流',
  'yhd_M2_Ic': 'M2C相电流', 'yhd_M2_KD': 'M2开度', 'yhd_M2_KDSD': 'M2开度设定',
  'yhd_M3_Ua': 'M3A相电压', 'yhd_M3_Ub': 'M3B相电压', 'yhd_M3_Uc': 'M3C相电压',
  'yhd_M3_Uab': 'M3AB线电压', 'yhd_M3_Ubc': 'M3BC线电压', 'yhd_M3_Uca': 'M3CA线电压',
  'yhd_M3_Ia': 'M3A相电流', 'yhd_M3_Ib': 'M3B相电流', 'yhd_M3_Ic': 'M3C相电流',
  'yhd_M3_KD': 'M3开度', 'yhd_M3_KDSD': 'M3开度设定'
}

// 各闸门固定字段顺序
const GATE_FIELD_ORDER = {
  dgq: ['TM', 'tm', 'dgq_M1_Ua', 'dgq_M1_Ub', 'dgq_M1_Uc', 'dgq_M1_Uab', 'dgq_M1_Ubc', 'dgq_M1_Uca', 'dgq_M1_Ia', 'dgq_M1_Ib', 'dgq_M1_Ic', 'dgq_M1_KD', 'dgq_M1_KDSD'],
  dzdf: ['tm', 'dzdf_M1_Ua', 'dzdf_M1_Ub', 'dzdf_M1_Uc', 'dzdf_M1_Uab', 'dzdf_M1_Ubc', 'dzdf_M1_Uca', 'dzdf_M1_Ia', 'dzdf_M1_Ib', 'dzdf_M1_Ic', 'dzdf_M1_FIT', 'dzdf_M1_FIT_TOL', 'dzdf_M1_YW'],
  qst: ['tm', 'qst_M1_Ua', 'qst_M1_Ub', 'qst_M1_Uc', 'qst_M1_Uab', 'qst_M1_Ia', 'qst_M1_Ib', 'qst_M1_Ic', 'qst_M1_KD', 'qst_M1_KDSD', 'qst_M2_Ua', 'qst_M2_Ub', 'qst_M2_Uc', 'qst_M2_Uab', 'qst_M2_Ia', 'qst_M2_Ib', 'qst_M2_Ic', 'qst_M2_KD', 'qst_M2_KDSD'],
  xgq: ['tm', 'xgq_M1_Ua', 'xgq_M1_Ub', 'xgq_M1_Uc', 'xgq_M1_Uab', 'xgq_M1_Ubc', 'xgq_M1_Uca', 'xgq_M1_Ia', 'xgq_M1_Ib', 'xgq_M1_Ic', 'xgq_M1_KD', 'xgq_M1_KDSD', 'xgq_M2_Ua', 'xgq_M2_Ub', 'xgq_M2_Uc', 'xgq_M2_Uab', 'xgq_M2_Ia', 'xgq_M2_Ib', 'xgq_M2_Ic', 'xgq_M2_KD', 'xgq_M2_KDSD'],
  yhd: ['tm', 'yhd_M1_Ua', 'yhd_M1_Ub', 'yhd_M1_Uc', 'yhd_M1_Uab', 'yhd_M1_Ubc', 'yhd_M1_Uca', 'yhd_M1_Ia', 'yhd_M1_Ib', 'yhd_M1_Ic', 'yhd_M1_KD', 'yhd_M1_KDSD', 'yhd_M2_Ua', 'yhd_M2_Ub', 'yhd_M2_Uc', 'yhd_M2_Uab', 'yhd_M2_Ia', 'yhd_M2_Ib', 'yhd_M2_Ic', 'yhd_M2_KD', 'yhd_M2_KDSD', 'yhd_M3_Ua', 'yhd_M3_Ub', 'yhd_M3_Uc', 'yhd_M3_Uab', 'yhd_M3_Ubc', 'yhd_M3_Uca', 'yhd_M3_Ia', 'yhd_M3_Ib', 'yhd_M3_Ic', 'yhd_M3_KD', 'yhd_M3_KDSD']
}

export function useGateStatus(options = {}) {
  // 响应式状态
  const loading = ref(false)
  const tableData = ref([])
  const tableColumns = ref([])
  const latestData = ref(null)
  const hasError = ref(false)  // 区分"无数据"和"请求失败"
  let refreshTimer = null

  const query = reactive({
    selectedGate: 'dgq',
    dateRange: [],
    quickType: 'all'  // 默认显示全部数据
  })

  // 分页状态
  const pagination = reactive({
    current: 1,
    size: 10,
    total: 0,
    totalPages: 0
  })

  // 计算属性：当前闸门名称
  const currentGateName = computed(() => {
    const gate = GATE_LIST.find(g => g.code === query.selectedGate)
    return gate ? gate.name : ''
  })

  // 计算属性：最新开度
  const latestKD = computed(() => {
    if (!latestData.value) return 0
    const d = latestData.value
    const code = query.selectedGate
    let kd = 0
    if (code === 'dgq') kd = d.dgq_M1_KD || 0
    else if (code === 'qst') kd = d.qst_M1_KD || d.qst_M2_KD || 0
    else if (code === 'dzdf') kd = d.dzdf_M1_YW || 0
    else if (code === 'xgq') kd = d.xgq_M1_KD || d.xgq_M2_KD || 0
    else if (code === 'yhd') kd = d.yhd_M1_KD || d.yhd_M2_KD || d.yhd_M3_KD || 0
    return Number(kd) || 0
  })

  // 计算属性：水位百分比（与开度成反比）
  const waterPercent = computed(() => Math.max(0, Math.min(100, 100 - latestKD.value)))

  // 计算属性：最新采集时间
  const latestCollectTime = computed(() => {
    if (!latestData.value) return ''
    return latestData.value.TM || latestData.value.tm || ''
  })


  // 获取状态颜色
  function getStatusColor(kd = latestKD.value) {
    if (kd === 0) return 'red'
    if (kd < 30) return 'orange'
    if (kd < 70) return 'yellow'
    return 'green'
  }

  // 获取状态文本
  function getStatusText(kd = latestKD.value) {
    if (kd === 0) return '关闭'
    if (kd < 30) return '微开'
    if (kd < 70) return '半开'
    return '全开'
  }

  // 字段转标签
  function fieldToLabel(key) {
    return FIELD_LABEL_MAP[key] || key
  }

  // 格式化日期时间
  function formatDateTime(date) {
    const y = date.getFullYear()
    const m = String(date.getMonth() + 1).padStart(2, '0')
    const d = String(date.getDate()).padStart(2, '0')
    const h = String(date.getHours()).padStart(2, '0')
    const min = String(date.getMinutes()).padStart(2, '0')
    const s = String(date.getSeconds()).padStart(2, '0')
    return `${y}-${m}-${d} ${h}:${min}:${s}`
  }

  // 设置快捷时间范围
  function setQuickDateRange(type) {
    query.quickType = type
    
    // 如果选择"全部"，清空日期范围
    if (type === 'all') {
      query.dateRange = []
      return
    }
    
    const now = new Date()
    let start, end = now

    switch (type) {
      case 'day':
        start = new Date(now.getTime() - 24 * 60 * 60 * 1000)
        break
      case 'week':
        start = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
        break
      case 'month':
        start = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
        break
      default:
        return
    }
    query.dateRange = [formatDateTime(start), formatDateTime(end)]
  }

  // 解析时间字符串或数组
  function parseTime(tm) {
    if (Array.isArray(tm) && tm.length >= 5) {
      return new Date(tm[0], tm[1] - 1, tm[2], tm[3], tm[4], tm[5] || 0)
    }
    if (typeof tm === 'string') {
      return new Date(tm.replace(/-/g, '/').replace('T', ' '))
    }
    return new Date(tm)
  }

  /**
   * 判断日期是否在指定范围内（只比较年月日）
   * @param {Date} date - 要判断的日期
   * @param {Date} start - 开始日期
   * @param {Date} end - 结束日期
   * @returns {boolean}
   */
  function isDateInRange(date, start, end) {
    const d = new Date(date.getFullYear(), date.getMonth(), date.getDate())
    const s = new Date(start.getFullYear(), start.getMonth(), start.getDate())
    const e = new Date(end.getFullYear(), end.getMonth(), end.getDate())
    return d >= s && d <= e
  }

  /**
   * 根据日期范围过滤数据
   * @param {Array} records - 原始数据
   * @returns {Array} 过滤后的数据
   */
  function filterByDateRange(records) {
    if (!query.dateRange || query.dateRange.length !== 2) {
      return records
    }

    const [startStr, endStr] = query.dateRange
    if (!startStr || !endStr) return records

    const startDate = new Date(startStr.replace(/-/g, '/').replace('T', ' '))
    const endDate = new Date(endStr.replace(/-/g, '/').replace('T', ' '))

    return records.filter(item => {
      const itemTime = parseTime(item.TM || item.tm)
      return isDateInRange(itemTime, startDate, endDate)
    })
  }

  // 获取闸门数据（后端分页）
  async function fetchGateData() {
    if (!query.selectedGate) return
    loading.value = true
    hasError.value = false  // 重置错误状态

    try {
      // 构建查询参数
      const params = {
        page: pagination.current,
        size: pagination.size
      }

      // 如果有日期范围，添加到 time-range 接口
      let url
      if (query.dateRange && query.dateRange.length === 2 && query.dateRange[0] && query.dateRange[1]) {
        url = `/api/gates/${query.selectedGate}/time-range`
        params.startTime = query.dateRange[0]
        params.endTime = query.dateRange[1]
      } else {
        url = `/api/gates/${query.selectedGate}`
      }

      console.log('获取闸门数据:', { selectedGate: query.selectedGate, url, params })

      const response = await request.get(url, { params })
      // request 拦截器虽然处理了 response，但为了保险起见，这里做完整的防御性编程
      // 后端返回结构: { code: 200, data: { list: [...], total: N, page: N, size: N, totalPages: N }, msg: "..." }

      const res = response.data // Axios 的 response.data 是服务器返回的 JSON body
      const pageResult = res.data // 分页结果

      // 更新分页信息
      if (pageResult) {
        pagination.total = pageResult.total || 0
        pagination.totalPages = pageResult.totalPages || 0
        // 后端返回的 list（已按时间倒序）
        tableData.value = pageResult.list || []
        latestData.value = tableData.value[0] || null
      } else {
        pagination.total = 0
        pagination.totalPages = 0
        tableData.value = []
        latestData.value = null
      }

      console.log('解析后的记录:', tableData.value)

      // 生成表格列配置
      if (tableData.value.length > 0) {
        const has = Object.keys(tableData.value[0])
        console.log('数据字段:', has)

        const fixedOrder = GATE_FIELD_ORDER[query.selectedGate] || []
        let keys = fixedOrder.filter(k => has.includes(k))

        // 如果固定顺序字段不匹配，使用动态字段
        if (keys.length === 0) {
          console.log('固定字段不匹配，使用动态字段')
          keys = has
        }

        // 确保时间字段在最左侧
        const timeKey = keys.find(k => k.toLowerCase() === 'tm')
        if (timeKey) {
          keys = [timeKey, ...keys.filter(k => k !== timeKey)]
        }

        tableColumns.value = keys.map(key => ({
          key,
          title: fieldToLabel(key)
        }))
        console.log('表格列配置:', tableColumns.value)
        console.log('最新数据:', latestData.value)
      } else {
        tableColumns.value = []
      }
    } catch (error) {
      console.error('获取闸门数据失败:', error)
      hasError.value = true
      tableData.value = []
      latestData.value = null
      tableColumns.value = []
      pagination.total = 0
      pagination.totalPages = 0
    } finally {
      loading.value = false
    }
  }

  // 导出数据
  function exportData() {
    if (!tableData.value?.length) {
      alert('没有数据可导出！')
      return
    }

    const headers = tableColumns.value.map(col => col.title)
    const rows = tableData.value.map(item =>
      tableColumns.value.map(col => item[col.key] ?? '')
    )

    let csvContent = '\ufeff' + headers.join(',') + '\n'
    rows.forEach(row => {
      csvContent += row.map(e => `"${e}"`).join(',') + '\n'
    })

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = `闸门数据_${currentGateName.value}.csv`
    link.click()
  }

  // 启动自动刷新
  function startAutoRefresh() {
    stopAutoRefresh()
    refreshTimer = setInterval(() => {
      fetchGateData()
    }, 30000) // 30秒刷新一次
  }

  // 停止自动刷新
  function stopAutoRefresh() {
    if (refreshTimer) {
      clearInterval(refreshTimer)
      refreshTimer = null
    }
  }

  // 生命周期
  onMounted(() => {
    // 初始化时不设置日期范围，显示全部数据
    query.dateRange = []
    fetchGateData()
    startAutoRefresh()
  })

  onBeforeUnmount(() => {
    stopAutoRefresh()
  })

  // 计算属性：过滤后的数据（用于前端导出/打印时显示全部）
  const filteredData = computed(() => {
    // 注意：现在数据由后端分页，前端不再需要前端过滤
    return tableData.value
  })

  // 计算属性：过滤后的总数
  const totalFiltered = computed(() => {
    return pagination.total
  })

  // 计算属性：当前页数据（与 tableData 相同，由后端分页）
  const pagedData = computed(() => {
    return tableData.value
  })

  // 切换分页
  function handlePageChange(page) {
    pagination.current = page
    fetchGateData()
  }

  // 切换每页大小
  function handleSizeChange(size) {
    pagination.size = size
    pagination.current = 1 // 重置到第一页
    fetchGateData()
  }

  return {
    // 常量
    gateList: GATE_LIST,
    // 状态
    loading,
    tableData,
    tableColumns,
    latestData,
    hasError,
    query,
    pagination,
    // 计算属性
    currentGateName,
    latestKD,
    waterPercent,
    latestCollectTime,
    filteredData,
    totalFiltered,
    pagedData,
    // 方法
    getStatusColor,
    getStatusText,
    fieldToLabel,
    setQuickDateRange,
    fetchGateData,
    exportData,
    handlePageChange,
    handleSizeChange
  }
}
