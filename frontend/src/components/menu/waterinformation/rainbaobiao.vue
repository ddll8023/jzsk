<template>
  <div class="rainreport-container">
    <el-card class="box-card">
      <!-- 顶部工具栏 -->
      <div class="toolbar">
        <div class="left-tools">
          <span class="title">雨情报表</span>
        </div>
        <div class="right-tools">
          <el-button type="primary" size="small" icon="el-icon-printer" @click="handlePrint">打印</el-button>
          <el-button type="success" size="small" icon="el-icon-download" @click="handleExport">导出</el-button>
        </div>
      </div>

      <div class="content-wrapper">
        <!-- 左侧筛选区 -->
        <div class="filter-section">
          <el-card class="filter-card">
            <!-- 报表类型选择 -->
            <div class="report-type-filter">
              <div class="filter-title">报表类型</div>
              <el-radio-group v-model="currentReportType" class="report-type-group">
                <el-radio v-for="item in reportTypes" 
                  :key="item.value" 
                  :label="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </div>

            <!-- 时间选择 -->
            <div class="time-filter">
              <div class="filter-title">时间范围</div>
              <el-date-picker
                v-model="searchDate"
                :type="datePickerType"
                :placeholder="datePickerPlaceholder"
                :format="datePickerFormat"
                :value-format="dateValueFormat"
                @change="handleDateChange">
              </el-date-picker>
            </div>

            <!-- 站点选择 -->
            <div class="station-filter">
              <div class="filter-title">站点选择</div>
              <el-checkbox-group v-model="selectedStations" @change="handleStationChange">
                <el-checkbox 
                  v-for="station in stations" 
                  :key="station.id" 
                  :label="station.id">
                  {{ station.name }}
                </el-checkbox>
              </el-checkbox-group>
            </div>

            <div class="button-group">
              <el-button type="primary" @click="handleSearch">查询</el-button>
              <el-button @click="resetFilters">重置</el-button>
            </div>
          </el-card>
        </div>

        <!-- 右侧表格区 -->
        <div class="table-section">
          <el-table
            :data="pagedData"
            border
            style="width: 100%"
            :header-cell-style="{ background: '#f5f7fa' }"
          >
            <el-table-column 
              :prop="timeColumnProp" 
              :label="timeColumnLabel" 
              width="100">
            </el-table-column>
            <el-table-column 
              v-for="station in selectedStationsList" 
              :key="station.id"
              :prop="'value_' + station.id"
              :label="station.name">
              <template slot-scope="scope">
                {{ scope.row['value_' + station.id] }} mm
              </template>
            </el-table-column>
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
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'RainReport',
  data() {
    return {
      currentReportType: 'hourly',
      reportTypes: [
        { value: 'hourly', label: '各站逐时降雨日报表' },
        { value: 'daily', label: '各站逐日降雨月报表' },
        { value: 'monthly', label: '各站逐月降雨年报表' },
        { value: 'yearly', label: '各站逐年降雨报表' },
        { value: 'warning', label: '各站降雨报表' },
        { value: 'accumulate', label: '各站累计降雨报表' },
        { value: 'monthly_total', label: '每月雨量报表' }
      ],
      searchDate: new Date().toISOString().split('T')[0],
      selectedStations: [],
      stations: [
        { id: 1, name: '八里湖站' },
        { id: 2, name: '六溪冲站' },
        { id: 3, name: '华山站' },
        { id: 4, name: '刘河站' },
        { id: 5, name: '新港站' },
        { id: 6, name: '南湖站' }
      ],
      tableData: [],
      timeList: {
        hourly: [
          '18时', '19时', '20时', '21时', '22时', '23时',
          '00时', '01时', '02时', '03时', '04时', '05时',
          '06时', '07时', '08时', '09时', '10时', '11时',
          '12时', '13时', '14时', '15时', '16时', '17时'
        ],
        daily: Array.from({length: 31}, (_, i) => `${i + 1}日`),
        monthly: ['1月', '2月', '3月', '4月', '5月', '6月', 
                 '7月', '8月', '9月', '10月', '11月', '12月']
      },
      pageSize: 10,
      currentPage: 1
    }
  },
  computed: {
    datePickerType() {
      if (this.currentReportType === 'yearly' || this.currentReportType === 'monthly_total') return 'year'
      if (this.currentReportType === 'monthly') return 'month'
      if (this.currentReportType === 'daily') return 'month'
      if (this.currentReportType === 'accumulate') return 'daterange'
      return 'date'
    },
    datePickerFormat() {
      if (this.currentReportType === 'yearly' || this.currentReportType === 'monthly_total') return 'yyyy年'
      if (this.currentReportType === 'monthly') return 'yyyy年MM月'
      if (this.currentReportType === 'daily') return 'yyyy年MM月'
      return 'yyyy-MM-dd'
    },
    dateValueFormat() {
      if (this.currentReportType === 'yearly' || this.currentReportType === 'monthly_total') return 'yyyy'
      if (this.currentReportType === 'monthly') return 'yyyy-MM'
      if (this.currentReportType === 'daily') return 'yyyy-MM'
      return 'yyyy-MM-dd'
    },
    datePickerPlaceholder() {
      if (this.currentReportType === 'yearly' || this.currentReportType === 'monthly_total') return '选择年份'
      if (this.currentReportType === 'monthly' || this.currentReportType === 'daily') return '选择月份'
      if (this.currentReportType === 'accumulate') return '选择时间范围'
      return '选择日期'
    },
    selectedStationsList() {
      return this.stations.filter(station => this.selectedStations.includes(station.id))
    },
    timeColumnLabel() {
      if (this.currentReportType === 'yearly') return '年份'
      if (this.currentReportType === 'monthly') return '月份'
      if (this.currentReportType === 'daily') return '日期'
      return '时间'
    },
    timeColumnProp() {
      return 'time'
    },
    currentTimeList() {
      if (this.currentReportType === 'monthly') return this.timeList.monthly
      if (this.currentReportType === 'daily') return this.timeList.daily
      return this.timeList.hourly
    },
    pagedData() {
      const start = (this.currentPage - 1) * this.pageSize
      return this.tableData.slice(start, start + this.pageSize)
    }
  },
  watch: {
    currentReportType: {
      handler(newType) {
        this.selectedStations = []
        this.resetFilters()
      },
      immediate: true
    }
  },
  methods: {
    handleDateChange(date) {
      this.searchDate = date
      this.fetchData()
    },
    handleStationChange() {
      this.fetchData()
    },
    handleSearch() {
      this.fetchData()
    },
    resetFilters() {
      if (this.currentReportType === 'yearly' || this.currentReportType === 'monthly_total') {
        this.searchDate = new Date().getFullYear().toString()
      } else if (this.currentReportType === 'monthly' || this.currentReportType === 'daily') {
        const date = new Date()
        this.searchDate = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`
      } else if (this.currentReportType === 'accumulate') {
        const end = new Date()
        const start = new Date()
        start.setDate(start.getDate() - 7)
        this.searchDate = [start.toISOString().split('T')[0], end.toISOString().split('T')[0]]
      } else {
        this.searchDate = new Date().toISOString().split('T')[0]
      }
      this.selectedStations = []
      this.tableData = []
    },
    handlePrint() {
      window.print()
    },
    handleExport() {
      this.$message.success('导出功能开发中')
    },
    fetchData() {
      // 生成模拟数据
      if (this.currentReportType === 'accumulate') {
        // 累计降雨报表特殊处理
        this.tableData = [{
          time: '累计降雨量',
          ...Object.fromEntries(this.selectedStationsList.map(station => [
            `value_${station.id}`,
            (Math.random() * 100 + 50).toFixed(1)
          ]))
        }]
      } else {
        // 其他报表通用处理
        this.tableData = this.currentTimeList.map(time => {
          const row = { time }
          this.selectedStationsList.forEach(station => {
            row['value_' + station.id] = (Math.random() * 10).toFixed(1)
          })
          return row
        })
      }
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
.rainreport-container {
  padding: 20px;
  height: calc(100vh - 120px);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.content-wrapper {
  display: flex;
  gap: 20px;
  height: calc(100% - 60px);
}

.filter-section {
  width: 300px;
  flex-shrink: 0;
}

.filter-card {
  height: 100%;
  overflow-y: auto;
}

.table-section {
  flex: 1;
  overflow: auto;
}

.report-type-filter,
.time-filter,
.station-filter {
  margin-bottom: 20px;
}

.filter-title {
  margin-bottom: 10px;
  font-weight: bold;
  color: #606266;
}

.report-type-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.el-radio {
  margin-right: 0;
  margin-bottom: 8px;
  display: block;
  white-space: normal;
  line-height: 1.5;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}

.el-checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th) {
  background-color: #f5f7fa !important;
}

@media print {
  .filter-section,
  .toolbar {
    display: none;
  }
  
  .content-wrapper {
    display: block;
  }
  
  .table-section {
    width: 100%;
  }
}
</style>
