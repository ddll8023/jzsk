<template>
  <div class="horizontal-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="search-card" shadow="never">
          <div class="search-header">
            <i class="el-icon-search"></i>
            <span>数据筛选</span>
          </div>
          <el-form :model="search" label-width="90px" class="search-form">
            <el-form-item label="大坝名称：">
              <el-select 
                v-model="search.projectName" 
                placeholder="请选择大坝" 
                filterable 
                clearable 
                @change="onProjectChange"
                class="dam-select">
                <el-option 
                  v-for="name in projectNames" 
                  :key="name" 
                  :label="name" 
                  :value="name" />
              </el-select>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="content-row">
      <el-col :span="24">
        <el-card class="chart-card" shadow="never">
          <div class="card-header">
            <i class="el-icon-data-line"></i>
            <span>水平位移趋势图</span>
          </div>
          <div ref="chartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="table-card" shadow="never">
          <div class="card-header">
            <i class="el-icon-document"></i>
            <span>水平位移数据</span>
          </div>
          <el-table 
            :data="filteredTableData" 
            border 
            stripe 
            class="custom-table">
            <el-table-column prop="recordId" label="记录ID" align="center" width="100" />
            <el-table-column prop="projectName" label="项目名称" align="center" width="120" />
            <el-table-column prop="monitoringType" label="监测类型" align="center" width="120" />
            <el-table-column prop="xaxisDisplacement" label="水平位移(mm)" align="center" />
            <el-table-column prop="recordTimeStr" label="记录时间" align="center" width="180" />
            <el-table-column prop="uploadTimeStr" label="上传时间" align="center" width="180" />
          </el-table>
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
      tableData: [],
      projectNames: [],
      search: {
        projectName: ''
      },
      chart: null
    }
  },
  computed: {
    filteredTableData() {
      if (!this.search.projectName) return this.tableData
      return this.tableData.filter(item => item.projectName === this.search.projectName)
    }
  },
  mounted() {
    this.fetchData()
  },
  watch: {
    filteredTableData() {
      this.updateChart()
    }
  },
  methods: {
    formatTime(arr) {
      if (!Array.isArray(arr) || arr.length < 3) return ''
      const pad = n => n.toString().padStart(2, '0')
      return `${arr[0]}-${pad(arr[1])}-${pad(arr[2])}` +
        (arr.length >= 6 ? ` ${pad(arr[3])}:${pad(arr[4])}:${pad(arr[5])}` : '')
    },
    async fetchData() {
      try {
        const res = await axios.get('/horizontal-displacement')
        let raw = res.data.data || res.data || []
        // 格式化时间字段
        this.tableData = raw.map(item => ({
          ...item,
          recordTimeStr: this.formatTime(item.recordTime),
          uploadTimeStr: this.formatTime(item.uploadTime)
        }))
        // 提取所有大坝名称
        this.projectNames = Array.from(new Set(this.tableData.map(i => i.projectName))).filter(Boolean)
        // 默认选中第一个大坝
        if (!this.search.projectName && this.projectNames.length) {
          this.search.projectName = this.projectNames[0]
        }
        this.$nextTick(() => {
          this.initChart()
          this.updateChart()
        })
      } catch (error) {
        this.$message.error('获取数据失败')
      }
    },
    onProjectChange() {
      this.updateChart()
    },
    initChart() {
      if (!this.chart) {
        this.chart = echarts.init(this.$refs.chartRef)
      }
      const option = {
        title: {
          text: '水平位移趋势'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          name: '时间',
          data: []
        },
        yAxis: {
          type: 'value',
          name: '水平位移(mm)'
        },
        series: [{
          name: '水平位移',
          type: 'line',
          smooth: true,
          data: []
        }]
      }
      this.chart.setOption(option)
    },
    updateChart() {
      if (!this.chart) return
      const data = this.filteredTableData
      const xData = data.map(item => item.recordTimeStr)
      const yData = data.map(item => Number(item.xaxisDisplacement))
      this.chart.setOption({
        xAxis: { data: xData },
        series: [{ data: yData }]
      })
    }
  }
}
</script>

<style scoped>
.horizontal-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.search-card, .chart-card, .table-card {
  margin-bottom: 20px;
  border-radius: 8px;
  transition: all 0.3s;
  border: none;
}

.search-card:hover, .chart-card:hover, .table-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.search-header, .card-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: 500;
  color: #1a1a1a;
}

.search-header i, .card-header i {
  margin-right: 8px;
  font-size: 18px;
  color: #409EFF;
}

.search-form {
  display: flex;
  align-items: center;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

.dam-select {
  width: 240px;
}

.chart-container {
  width: 100%;
  height: 400px;
  margin-top: 10px;
}

.content-row {
  margin-top: 20px;
}

.custom-table {
  margin-top: 10px;
}

:deep(.el-table) {
  border-radius: 4px;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #1a1a1a;
  font-weight: 500;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background: #fafbfc;
}

:deep(.el-select .el-input__inner) {
  border-radius: 4px;
}

:deep(.el-card__body) {
  padding: 20px;
}
</style> 