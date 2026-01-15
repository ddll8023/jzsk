<template>
  <div class="waterreport-container">
    <el-card class="box-card">
      <!-- 顶部工具栏 -->
      <div class="toolbar">
        <div class="left-tools">
          <span class="title">水情报表</span>
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
                <div class="report-category">
                  <div class="category-title">水库水情</div>
                  <el-radio v-for="item in reservoirReports" 
                    :key="item.value" 
                    :label="item.value">{{ item.label }}</el-radio>
                </div>
                <div class="report-category">
                  <div class="category-title">河道水情</div>
                  <el-radio v-for="item in riverReports" 
                    :key="item.value" 
                    :label="item.value">{{ item.label }}</el-radio>
                </div>
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
                  v-for="station in filteredStations" 
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
  name: 'WaterReport',
  data() {
    return {
      currentReportType: 'reservoir_daily',
      reservoirReports: [
        { value: 'reservoir_daily', label: '水库水情逐时日报表' },
        { value: 'reservoir_monthly', label: '水库水情逐日月报表' },
        { value: 'reservoir_yearly', label: '水库水情逐月年报表' },
        { value: 'reservoir_warning', label: '水库超汛限水位统计表' }
      ],
      riverReports: [
        { value: 'river_water_daily', label: '河道水位逐时日报表' },
        { value: 'river_flow_daily', label: '河道流量逐时日报表' },
        { value: 'river_water_monthly', label: '河道水位逐日月报表' },
        { value: 'river_flow_monthly', label: '河道流量逐日月报表' },
        { value: 'river_water_yearly', label: '河道水位逐月年报表' },
        { value: 'river_flow_yearly', label: '河道流量逐月年报表' },
        { value: 'river_warning', label: '河道超警戒水位统计表' }
      ],
      searchDate: new Date().toISOString().split('T')[0],
      selectedStations: [],
      reservoirStations: [
        { id: 1, name: '八里湖水库', type: 'reservoir' },
        { id: 2, name: '六溪冲水库', type: 'reservoir' },
        { id: 3, name: '华山水库', type: 'reservoir' }
      ],
      riverStations: [
        { id: 4, name: '八里湖(水文)', type: 'river' },
        { id: 5, name: '六新冲(水文)', type: 'river' },
        { id: 6, name: '刘河(水文)', type: 'river' }
      ],
      tableData: [],
      timeList: {
        daily: [
          '18时', '19时', '20时', '21时', '22时', '23时',
          '00时', '01时', '02时', '03时', '04时', '05时',
          '06时', '07时', '08时', '09时', '10时', '11时',
          '12时', '13时', '14时', '15时', '16时', '17时'
        ],
        monthly: Array.from({length: 31}, (_, i) => `${i + 1}日`),
        yearly: ['1月', '2月', '3月', '4月', '5月', '6月', 
                '7月', '8月', '9月', '10月', '11月', '12月']
      },
      pageSize: 10,
      currentPage: 1
    }
  },
  computed: {
    datePickerType() {
      if (this.currentReportType.includes('yearly')) return 'year'
      if (this.currentReportType.includes('monthly')) return 'month'
      return 'date'
    },
    datePickerFormat() {
      if (this.currentReportType.includes('yearly')) return 'yyyy年'
      if (this.currentReportType.includes('monthly')) return 'yyyy年MM月'
      return 'yyyy-MM-dd'
    },
    dateValueFormat() {
      if (this.currentReportType.includes('yearly')) return 'yyyy'
      if (this.currentReportType.includes('monthly')) return 'yyyy-MM'
      return 'yyyy-MM-dd'
    },
    datePickerPlaceholder() {
      if (this.currentReportType.includes('yearly')) return '选择年份'
      if (this.currentReportType.includes('monthly')) return '选择月份'
      return '选择日期'
    },
    filteredStations() {
      if (this.currentReportType.startsWith('reservoir')) {
        return this.reservoirStations
      } else {
        return this.riverStations
      }
    },
    selectedStationsList() {
      return this.filteredStations.filter(station => this.selectedStations.includes(station.id))
    },
    timeColumnLabel() {
      if (this.currentReportType.includes('yearly')) return '月份'
      if (this.currentReportType.includes('monthly')) return '日期'
      return '时间'
    },
    timeColumnProp() {
      return 'time'
    },
    currentTimeList() {
      if (this.currentReportType.includes('yearly')) return this.timeList.yearly
      if (this.currentReportType.includes('monthly')) return this.timeList.monthly
      return this.timeList.daily
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
      if (this.currentReportType.includes('yearly')) {
        this.searchDate = new Date().getFullYear().toString()
      } else if (this.currentReportType.includes('monthly')) {
        const date = new Date()
        this.searchDate = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`
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
      // TODO: 实现导出功能
      this.$message.success('导出功能开发中')
    },
    fetchData() {
      // 生成模拟数据
      this.tableData = this.currentTimeList.map(time => {
        const row = { time }
        this.selectedStationsList.forEach(station => {
          row['value_' + station.id] = (Math.random() * 10).toFixed(1)
        })
        return row
      })
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
.waterreport-container {
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
  gap: 15px;
}

.report-category {
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  padding: 10px;
  background-color: #F8F9FB;
}

.category-title {
  font-weight: bold;
  margin-bottom: 10px;
  color: #409EFF;
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