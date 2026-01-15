<template>
  <el-card class="waterlevel-panel" shadow="hover">
    <div slot="header">水库水位</div>
    <el-table :data="mock ? mockData : tableData" border stripe size="medium" class="waterlevel-table">
      <el-table-column prop="stationName" label="水库" align="center" />
      <el-table-column prop="waterLevel" label="水位(m)" align="center" />
    </el-table>
    <div ref="chart" style="height:180px;width:100%;margin-top:10px;"></div>
  </el-card>
</template>
<script>
import axios from 'axios'
import * as echarts from 'echarts'
export default {
  name: 'WaterLevelPanel',
  props: { mock: { type: Boolean, default: false } },
  data() {
    return {
      tableData: [],
      chart: null,
      mockData: [
        { stationName: '荆竹水库', waterLevel: 53.76 },
        { stationName: '南湖水库', waterLevel: 52.34 },
        { stationName: '北湖水库', waterLevel: 51.98 },
        { stationName: '东湖水库', waterLevel: 54.12 },
        { stationName: '西湖水库', waterLevel: 53.21 }
      ]
    }
  },
  mounted() {
    if (!this.mock) this.fetchData()
    this.$nextTick(() => this.renderChart(this.mock ? this.mockData : this.tableData))
  },
  watch: {
    tableData(val) { this.renderChart(this.mock ? this.mockData : val) }
  },
  methods: {
    fetchData() {
      axios.get('/water-storage').then(res => {
        const data = res.data.data || res.data || []
        this.tableData = data.slice(0, 5)
      })
    },
    renderChart(data) {
      if (!this.$refs.chart) return
      if (!this.chart) this.chart = echarts.init(this.$refs.chart)
      this.chart.setOption({
        title: { text: '水位趋势', left: 'center', textStyle: { color: '#fff', fontSize: 18 } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: data.map(i => i.stationName), axisLabel: { color: '#fff', fontSize: 16 } },
        yAxis: { type: 'value', name: '水位(m)', axisLabel: { color: '#fff', fontSize: 16 } },
        series: [{ name: '水位', type: 'bar', data: data.map(i => i.waterLevel), itemStyle: { color: '#409EFF' }, barWidth: 32 }]
      })
    }
  }
}
</script>
<style scoped>
.waterlevel-panel { background: #12203a; color: #fff; }
.waterlevel-table >>> .el-table__header th { background: #1a2a3a; color: #00eaff; font-size: 18px; }
.waterlevel-table >>> .el-table__body td { font-size: 18px; }
</style> 