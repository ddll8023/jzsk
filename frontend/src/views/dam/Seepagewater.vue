<template>
  <div class="seepagewater-container">
    <el-card class="filter-card" shadow="hover">
      <div slot="header">渗流量与库水位对比分析</div>
      <el-form :inline="true" :model="query" class="mb-2">
        <el-form-item label="选择站点">
          <el-select v-model="query.stationId" filterable clearable placeholder="请选择站点" style="min-width: 180px" @change="fetchData" @clear="onStationClear">
            <el-option v-for="s in stationList" :key="s.id" :label="s.name || s.id" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择时间段">
          <el-date-picker v-model="query.dateRange" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss" @change="fetchData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="fetchData">查询</el-button>
          <el-button icon="el-icon-printer" @click="printReport">打印报表</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="chart-card" shadow="hover">
      <div slot="header">渗流量与库水位趋势对比</div>
      <div ref="chart" style="width:100%;height:350px;"></div>
    </el-card>
    <el-card class="table-card" shadow="hover">
      <div slot="header">原始数据</div>
      <el-table :data="tableData" border stripe style="width:100%;margin-top:10px;">
        <el-table-column prop="recordId" label="记录ID" align="center" width="80" />
        <el-table-column prop="stationId" label="站点编号" align="center" width="100" />
        <el-table-column prop="recordTime" label="记录时间" align="center">
          <template slot-scope="scope">
            {{ formatTime(scope.row.recordTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="seepageFlow" label="渗流量" align="center" />
        <el-table-column prop="waterLevel" label="库水位" align="center" />
        <el-table-column prop="remarks" label="备注" align="center" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'
import echarts from 'echarts'
export default {
  data() {
    return {
      stationList: [],
      query: {
        stationId: '',
        dateRange: [],
      },
      tableData: [],
      chart: null
    }
  },
  mounted() {
    this.fetchStations()
  },
  methods: {
    async fetchStations() {
      // 直接查所有渗流水位数据，提取所有station_id
      const res = await axios.get('/seepage-water-level')
      let all = res.data.data || res.data || []
      // 提取所有station_id去重，作为下拉选项
      const ids = Array.from(new Set(all.map(i => i.stationId))).filter(Boolean)
      this.stationList = ids.map(id => ({ id, name: String(id) }))
      // 优先选中id=1，否则第一个
      let defaultStation = this.stationList.find(s => s.id == 1) || this.stationList[0]
      if (defaultStation) {
        this.query.stationId = defaultStation.id
        this.fetchData()
      }
    },
    async fetchData() {
      // 如果没有站点，直接清空数据
      if (!this.stationList || this.stationList.length === 0) {
        this.tableData = []
        if (this.chart) this.chart.clear()
        return
      }
      // 如果未选站点，自动用id=1
      if (!this.query.stationId) {
        let defaultStation = this.stationList.find(s => s.id == 1) || this.stationList[0]
        if (defaultStation) this.query.stationId = defaultStation.id
      }
      // 查所有渗流水位数据，前端筛选
      const res = await axios.get('/seepage-water-level')
      let all = res.data.data || res.data || []
      if (this.query.stationId) {
        all = all.filter(i => i.stationId == this.query.stationId)
      }
      if (this.query.dateRange && this.query.dateRange.length === 2) {
        const [start, end] = this.query.dateRange
        all = all.filter(i => i.recordTime >= start && i.recordTime <= end)
      }
      this.tableData = all
      this.$nextTick(this.renderChart)
    },
    formatTime(val) {
      if (!val) return ''
      // 2025-05-18 10:03:29 直接返回
      if (typeof val === 'string' && val.length === 19 && val[4] === '-' && val[7] === '-' && val[10] === ' ') {
        return val
      }
      // 2025-05-18T10:03:29
      if (typeof val === 'string' && val.length === 19 && val[10] === 'T') {
        return val.replace('T', ' ')
      }
      // [2025,5,18,10,3,29] 数组
      if (Array.isArray(val) && val.length >= 3) {
        const [y, m, d, h = 0, min = 0, s = 0] = val
        const pad = n => n.toString().padStart(2, '0')
        return `${y}-${pad(m)}-${pad(d)} ${pad(h)}:${pad(min)}:${pad(s)}`
      }
      // 202551810329 => 2025-05-18 10:03:29
      if (typeof val === 'string' && val.length === 12 && /^\d{12}$/.test(val)) {
        return `${val.substr(0,4)}-${val.substr(4,2)}-${val.substr(6,2)} ${val.substr(8,2)}:${val.substr(10,2)}:00`
      }
      // 13位时间戳
      if (typeof val === 'number' || (typeof val === 'string' && /^\d{13}$/.test(val))) {
        const d = new Date(Number(val))
        const pad = n => n.toString().padStart(2, '0')
        return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
      }
      // 其他情况尝试用Date
      const d = new Date(val)
      if (!isNaN(d.getTime())) {
        const pad = n => n.toString().padStart(2, '0')
        return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
      }
      return val
    },
    renderChart() {
      if (!this.$refs.chart) return
      if (!this.chart) {
        this.chart = echarts.init(this.$refs.chart)
      }
      const data = this.tableData.slice().sort((a, b) => new Date(this.formatTime(a.recordTime)) - new Date(this.formatTime(b.recordTime)))
      // 只取时分秒
      const times = data.map(i => {
        const t = this.formatTime(i.recordTime)
        return t.length >= 8 ? t.slice(-8) : t
      })
      const flows = data.map(i => i.seepageFlow)
      const levels = data.map(i => i.waterLevel)
      this.chart.setOption({
        title: { text: '渗流量与库水位趋势对比', left: 'center', top: 10 },
        tooltip: { trigger: 'axis' },
        legend: { data: ['渗流量', '库水位'], top: 40 },
        grid: { left: 40, right: 30, top: 70, bottom: 40 },
        xAxis: { type: 'category', data: times, axisLabel: { rotate: 0 } },
        yAxis: [
          { type: 'value', name: '渗流量', position: 'left' },
          { type: 'value', name: '库水位', position: 'right' }
        ],
        series: [
          {
            name: '渗流量',
            type: 'line',
            yAxisIndex: 0,
            data: flows,
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: { width: 3, color: '#409EFF' },
            itemStyle: { color: '#409EFF' },
            areaStyle: { color: 'rgba(64,158,255,0.1)' }
          },
          {
            name: '库水位',
            type: 'line',
            yAxisIndex: 1,
            data: levels,
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: { width: 3, color: '#67C23A' },
            itemStyle: { color: '#67C23A' },
            areaStyle: { color: 'rgba(103,194,58,0.1)' }
          }
        ]
      })
    },
    onStationClear() {
      let defaultStation = this.stationList.find(s => s.id == 1) || this.stationList[0]
      if (defaultStation) {
        this.query.stationId = defaultStation.id
        this.fetchData()
      }
    },
    printReport() {
      window.print()
    }
  }
}
</script>

<style scoped>
.seepagewater-container { padding: 30px; background: #f4f6fa; min-height: 100vh; }
.filter-card, .chart-card, .table-card { margin-bottom: 20px; }
.mb-2 { margin-bottom: 16px; }
</style> 