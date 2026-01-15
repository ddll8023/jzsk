<template>
  <el-card class="dam-panel" shadow="hover">
    <div slot="header">大坝渗流量与变形分析</div>
    <el-row :gutter="10">
      <el-col :span="12">
        <div class="stat-card">
          <div>渗流量</div>
          <div class="stat-value">{{ mock ? 120 : seepage }}</div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="stat-card">
          <div>水平位移</div>
          <div class="stat-value">{{ mock ? 3.2 : horizontal }}</div>
        </div>
      </el-col>
    </el-row>
    <div ref="chart" style="height:180px;width:100%;margin-top:10px;"></div>
  </el-card>
</template>
<script>
import axios from 'axios'
import * as echarts from 'echarts'
export default {
  name: 'DamAnalysisPanel',
  props: { mock: { type: Boolean, default: false } },
  data() {
    return {
      seepage: 0,
      horizontal: 0,
      chart: null,
      mockData: [
        { time: '08:00', value: 110 },
        { time: '10:00', value: 120 },
        { time: '12:00', value: 125 },
        { time: '14:00', value: 118 },
        { time: '16:00', value: 122 }
      ]
    }
  },
  mounted() { if (!this.mock) this.fetchData(); this.$nextTick(() => this.renderChart(this.mock ? this.mockData : [])) },
  methods: {
    fetchData() {
      axios.get('/dam/seepage').then(res => {
        const data = res.data.data || res.data || []
        this.seepage = data.length ? data[0].value : 0
      })
      axios.get('/dam/horizontal').then(res => {
        const data = res.data.data || res.data || []
        this.horizontal = data.length ? data[0].value : 0
      })
    },
    renderChart(data) {
      if (!this.$refs.chart) return
      if (!this.chart) this.chart = echarts.init(this.$refs.chart)
      const chartData = this.mock ? this.mockData : data
      this.chart.setOption({
        title: { text: '渗流量趋势', left: 'center', textStyle: { color: '#fff', fontSize: 18 } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: chartData.map(i => i.time), axisLabel: { color: '#fff', fontSize: 16 } },
        yAxis: { type: 'value', name: '渗流量', axisLabel: { color: '#fff', fontSize: 16 } },
        series: [{ name: '渗流量', type: 'line', data: chartData.map(i => i.value), itemStyle: { color: '#67C23A' }, lineStyle: { width: 4 } }]
      })
    }
  }
}
</script>
<style scoped>
.dam-panel { background: #12203a; color: #fff; }
.stat-card { background: #1a2a3a; padding: 12px; border-radius: 8px; margin-bottom: 12px; text-align: center; }
.stat-value { font-size: 28px; font-weight: bold; color: #67C23A; }
</style> 