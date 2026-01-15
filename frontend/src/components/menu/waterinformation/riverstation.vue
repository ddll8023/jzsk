<template>
  <div id="div1">
    <el-row>
      <!-- 左侧筛选区 -->
      <el-col :span="5" class="left-panel">
        <el-card shadow="hover" class="search-card">
          <div slot="header" class="search-title">河道站筛选</div>
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="测站">
              <el-select v-model="searchForm.stationName" filterable placeholder="请选择" clearable>
                <el-option v-for="item in stationList" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
            <el-form-item label="日期">
              <el-date-picker v-model="searchForm.date" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd" style="width: 180px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
              <el-button @click="resetFilter">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <!-- 右侧内容区 -->
      <el-col :span="19" class="right-panel">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-title">河道站水位过程线</div>
          <div ref="chart" style="width: 100%; height: 320px;"></div>
        </el-card>
        <el-card shadow="hover" class="table-card">
          <div slot="header" class="table-title">河道站水位数据</div>
          <el-table :data="pagedData" border stripe highlight-current-row style="width: 100%; margin-top: 10px;">
            <el-table-column prop="stationCode" label="测站代码" align="center"></el-table-column>
            <el-table-column prop="stationName" label="测站名称" align="center"></el-table-column>
            <el-table-column prop="location" label="位置" align="center"></el-table-column>
            <el-table-column prop="recordTimeStr" label="记录时间" align="center">
              <template slot-scope="scope">
                {{ formatTime(scope.row.recordTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="waterLevel" label="水位(m)" align="center"></el-table-column>
            <el-table-column prop="flowRate" label="流量(m³/s)" align="center"></el-table-column>
            <el-table-column prop="waterSituation" label="水情态势" align="center"></el-table-column>
          </el-table>
          <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="tableData.length"
            :page-size="pageSize"
            :current-page="currentPage"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            style="margin-top: 16px; text-align: right;"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from 'axios'
import * as echarts from 'echarts'

export default {
  data() {
    return {
      searchForm: {
        stationName: '',
        date: ''
      },
      stationList: [],
      tableData: [],
      chart: null,
      pageSize: 10,
      currentPage: 1,
      showChart: false
    }
  },
  computed: {
    pagedData() {
      const start = (this.currentPage - 1) * this.pageSize
      return this.tableData.slice(start, start + this.pageSize)
    }
  },
  mounted() {
    this.fetchStations()
  },
  methods: {
    fetchStations() {
      axios.get('/river-station').then(res => {
        const all = res.data.data || res.data
        this.stationList = [...new Set(all.map(i => i.stationName))]
      })
    },
    handleSearch() {
      axios.get('/river-station').then(res => {
        let data = res.data.data || res.data
        // 前端根据 stationName 和 date 精确过滤
        if (this.searchForm.stationName) {
          data = data.filter(item => item.stationName === this.searchForm.stationName)
        }
        if (this.searchForm.date) {
          data = data.filter(item => this.formatTime(item.recordTime).slice(0, 10) === this.searchForm.date)
        }
        this.tableData = data
        this.showChart = !!this.searchForm.stationName && data.length > 0
        this.$nextTick(() => this.renderChart(this.showChart ? data : []))
      })
    },
    resetFilter() {
      this.searchForm.stationName = ''
      this.searchForm.date = ''
      this.handleSearch()
    },
    renderChart(data) {
      if (!this.$refs.chart) return
      if (!this.chart) {
        this.chart = echarts.init(this.$refs.chart)
      }
      if (!this.showChart || !data.length) {
        this.chart.clear()
        return
      }
      // 只取时分秒
      const times = data.map(i => {
        const t = this.formatTime(i.recordTime)
        return t.length >= 8 ? t.slice(-8) : t
      })
      const levels = data.map(i => i.waterLevel)
      // 计算y轴范围和分度
      let min = Math.min(...levels)
      let max = Math.max(...levels)
      if (min === max) {
        min = min - 1
        max = max + 1
      } else {
        min = Math.floor(min - (max-min)*0.2)
        max = Math.ceil(max + (max-min)*0.2)
      }
      const interval = ((max - min) / 5) || 1
      this.chart.setOption({
        title: { text: '', left: 'center' },
        tooltip: { trigger: 'axis' },
        grid: { left: 40, right: 30, top: 40, bottom: 40 },
        xAxis: { type: 'category', data: times, axisLabel: { rotate: 0, fontSize: 16 } },
        yAxis: {
          type: 'value',
          name: '水位(m)',
          nameTextStyle: { fontSize: 18 },
          axisLabel: { fontSize: 18 },
          min,
          max,
          interval,
        },
        series: [{
          name: '水位',
          type: 'line',
          data: levels,
          smooth: true,
          symbol: 'circle',
          symbolSize: 16,
          lineStyle: { width: 6, color: '#409EFF' },
          itemStyle: { color: '#409EFF' },
          areaStyle: { color: 'rgba(64,158,255,0.1)' }
        }]
      })
    },
    formatTime(timeVal) {
      if (!timeVal) return ''
      // 数组格式 [2024,6,1,8,0]
      if (Array.isArray(timeVal)) {
        const [y, m, d, h = 0, min = 0, s = 0] = timeVal
        return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}:${String(s).padStart(2, '0')}`
      }
      // 13位时间戳转字符串
      if (typeof timeVal === 'number' || (typeof timeVal === 'string' && /^\d{13}$/.test(timeVal))) {
        const d = new Date(Number(timeVal))
        const pad = n => n.toString().padStart(2, '0')
        return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
      }
      // 2024-06-01T12:34:56 或 2024-06-01 12:34:56
      if (typeof timeVal === 'string' && timeVal.length >= 19) {
        return timeVal.replace('T', ' ').slice(0, 19)
      }
      return timeVal
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
    },
    handleCurrentChange(page) {
      this.currentPage = page
    }
  }
}
</script>

<style scoped>
#div1 { height: 100%; width: 100%; background: #f4f6fa; }
.left-panel { background: #f5f7fa; height: 100vh; border-right: 1px solid #e4e7ed; padding: 30px 10px 0 10px; }
.right-panel { padding: 30px 30px 0 30px; height: 100vh; overflow: auto; background: #f4f6fa; }
.search-card { margin-bottom: 20px; }
.search-title { font-size: 18px; font-weight: bold; color: #409EFF; }
.chart-card, .table-card { margin-bottom: 20px; }
.chart-title, .table-title { font-size: 16px; font-weight: bold; color: #222; }
.el-table th { background: #eaf3fc !important; color: #222; }
.el-table .el-table__row:hover td { background: #f0faff !important; }
</style>
