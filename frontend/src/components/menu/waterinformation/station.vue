<template>
  <div id="div1">
    <el-row>
      <!-- 筛选区 -->
      <el-col :span="24" class="search-panel">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="选择时间">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="datetimerange"
              :picker-options="pickerOptions"
              range-separator="至"
              start-placeholder="开始日期时间"
              end-placeholder="结束日期时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              align="right"
            />
      </el-form-item>
      <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
            <el-button type="success" icon="el-icon-download" @click="exportData">导出</el-button>
      </el-form-item>
    </el-form>
      </el-col>
      <!-- 右侧内容区 -->
      <el-col :span="24" class="right-panel">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-title">
            <el-radio-group v-model="rainChartType" size="small" style="margin-right: 16px;">
              <el-radio-button label="hourly">小时降雨量柱状图</el-radio-button>
              <el-radio-button label="daily">每日累计降雨量柱状图</el-radio-button>
            </el-radio-group>
            <span>{{ rainChartType === 'hourly' ? '小时降雨量柱状图' : '每日累计降雨量柱状图' }}</span>
          </div>
          <div ref="chart" style="width: 100%; height: 320px;"></div>
        </el-card>
        <el-card shadow="hover" class="table-card">
          <div slot="header" class="table-title">降雨数据</div>
          <el-table :data="pagedData" border stripe highlight-current-row style="width: 100%; margin-top: 10px;">
            <el-table-column prop="stationName" label="测站名称" align="center"></el-table-column>
            <el-table-column prop="formattedTime" label="时间" align="center"></el-table-column>
            <el-table-column prop="drp" label="降雨量(mm)" align="center"></el-table-column>
      </el-table>
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="filteredData.length"
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
      allTableData: [], // 存储所有从后端获取的原始数据
      tableData: [], // 存储当前过滤后的数据
      chart: null,
      pageSize: 10,
      currentPage: 1,
      searchForm: {
        dateRange: [] // 存储日期范围，格式 [开始时间, 结束时间]
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一天',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24); // 24小时前
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      rainChartType: 'hourly', // 新增：图表类型切换
    }
  },
  computed: {
    filteredData() {
      if (!this.searchForm.dateRange || this.searchForm.dateRange.length === 0) {
        return this.allTableData; // 不再在这里排序
      }
      const [startDateStr, endDateStr] = this.searchForm.dateRange;
      const startDate = new Date(startDateStr);
      const endDate = new Date(endDateStr);

      return this.allTableData.filter(item => {
        const itemTime = this.parseTimeArrayToDate(item.tm);
        return itemTime >= startDate && itemTime <= endDate;
      }); // 不再在这里排序
    },
    pagedData() {
      // 对过滤后的数据进行降序排序，然后分页
      const sortedData = [...this.filteredData].sort((a, b) => this.parseTimeArrayToDate(b.tm) - this.parseTimeArrayToDate(a.tm));
      const start = (this.currentPage - 1) * this.pageSize;
      return sortedData.slice(start, start + this.pageSize);
    }
  },
  mounted() {
    // 默认显示最近一天的数据
    this.setQuickDateRange('最近一天');
    this.fetchData();
  },
  methods: {
    // 新增方法：设置快速日期范围
    setQuickDateRange(type) {
      const end = new Date();
      const start = new Date();
      if (type === '最近一天') {
        start.setTime(start.getTime() - 3600 * 1000 * 24);
      } else if (type === '最近一周') {
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      } else if (type === '最近一个月') {
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      }
      // 将日期范围赋值给 searchForm.dateRange，注意格式化
      this.searchForm.dateRange = [
        this.formatDateForPicker(start),
        this.formatDateForPicker(end)
      ];
    },
    formatDateForPicker(date) {
      const y = date.getFullYear();
      const m = String(date.getMonth() + 1).padStart(2, '0');
      const d = String(date.getDate()).padStart(2, '0');
      const h = String(date.getHours()).padStart(2, '0');
      const min = String(date.getMinutes()).padStart(2, '0');
      const s = String(date.getSeconds()).padStart(2, '0');
      return `${y}-${m}-${d} ${h}:${min}:${s}`;
    },
    fetchData() {
      axios.get('/st-pptn-hour/list').then(res => {
        const data = res.data || []
        this.allTableData = data.map(item => ({
          ...item,
          formattedTime: this.formatTime(item.tm), // Keep this for table display
          stationName: '坝前雨量水位站（新站）' // 新增字段，用于显示统一的测站名称
        }));
        // 直接使用 filteredData，它内部已包含排序逻辑
        this.tableData = this.filteredData;
        this.$nextTick(() => this.renderChart(this.filteredData))
      }).catch(error => {
        console.error("Error fetching precipitation data:", error);
        this.$message.error('获取降雨数据失败！');
        this.allTableData = []; // 清空数据
        this.tableData = [];
        if (this.chart) { // 清空图表
          this.chart.clear();
        }
      })
    },
    handleSearch() {
      this.currentPage = 1;
      this.tableData = this.filteredData;
      this.$nextTick(() => this.renderChart(this.filteredData));
    },
    exportData() {
      if (this.filteredData.length === 0) {
        this.$message.warning('没有数据可导出！');
        return;
      }

      const headers = ['测站名称', '时间', '降雨量(mm)'];
      const rows = this.filteredData.map(item => [
        item.stationName, // 使用新的 stationName 字段
        item.formattedTime,
        item.drp
      ]);

      let csvContent = '\ufeff' + headers.join(',') + '\n'; // Add BOM for UTF-8
      rows.forEach(row => {
        csvContent += row.map(e => `"${e}"`).join(',') + '\n';
      });

      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement("a");
      if (link.download !== undefined) { // feature detection
        const url = URL.createObjectURL(blob);
        link.setAttribute("href", url);
        link.setAttribute("download", "降雨数据.csv");
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } else {
        window.open('data:text/csv;charset=utf-8,' + escape(csvContent));
      }
      this.$message.success('数据已导出！');
    },
    renderChart(data) {
      if (!this.$refs.chart) return;
      if (!this.chart) {
        this.chart = echarts.init(this.$refs.chart);
      }
      if (!data.length) {
        this.chart.clear();
        return;
      }
      if (this.rainChartType === 'hourly') {
        // 小时降雨量柱状图（原有逻辑）
        const chartData = [...data].sort((a, b) => this.parseTimeArrayToDate(a.tm) - this.parseTimeArrayToDate(b.tm));
        const times = chartData.map(item => this.formatTime(item.tm));
        const drpLevels = chartData.map(i => i.drp);
        // Determine x-axis label format based on time range
        let xAxisLabelFormatter;
        const dateRangeDurationMs = this.searchForm.dateRange.length === 2 
          ? new Date(this.searchForm.dateRange[1]).getTime() - new Date(this.searchForm.dateRange[0]).getTime()
          : 0;
        const oneDayMs = 24 * 3600 * 1000;
        if (dateRangeDurationMs <= oneDayMs) {
          xAxisLabelFormatter = function(value) {
            const parts = value.split(' ');
            if (parts.length > 1) {
              return parts[1].substring(0, 5);
            }
            return value;
          };
        } else {
          xAxisLabelFormatter = function(value) {
            const parts = value.split('-');
            if (parts.length >= 3) {
              return `${parts[1]}-${parts[2].split(' ')[0]}`;
            }
            return value;
          };
        }
        let min = Math.min(...drpLevels);
        let max = Math.max(...drpLevels);
        min = Math.max(0, min);
        if (min === max) {
          min = min > 0 ? min - 1 : 0;
          max = max + 1;
        } else {
          const padding = (max - min) * 0.2;
          min = Math.max(0, Math.floor(min - padding));
          max = Math.ceil(max + padding);
        }
        const interval = ((max - min) / 5) || 1;
        this.chart.setOption({
          title: { text: '', left: 'center' },
          tooltip: {
            trigger: 'axis',
            formatter: function (params) {
              let result = params[0].name + '<br/>';
              params.forEach(function (item) {
                result += item.marker + item.seriesName + ': ' + item.value + 'mm<br/>';
              });
              return result;
            }
          },
          grid: { left: 40, right: 30, top: 40, bottom: 40 },
          xAxis: { 
            type: 'category', 
            data: times, 
            axisLabel: { 
              rotate: 0,
              fontSize: 12,
              formatter: xAxisLabelFormatter
            }
          },
          yAxis: {
            type: 'value',
            name: '降雨量(mm)',
            nameTextStyle: { fontSize: 14 },
            axisLabel: { fontSize: 12 },
            min: 0,
            max: max,
            interval: interval,
          },
          series: [{
            name: '降雨量',
            type: 'bar',
            data: drpLevels,
            itemStyle: {
              color: '#409EFF'
            }
          }]
        });
      } else if (this.rainChartType === 'daily') {
        // 每日累计降雨量柱状图
        // 1. 按天分组累加
        const dailyMap = {};
        data.forEach(item => {
          const dateStr = this.formatTime(item.tm).split(' ')[0]; // 只取日期部分
          if (!dailyMap[dateStr]) {
            dailyMap[dateStr] = 0;
          }
          dailyMap[dateStr] += Number(item.drp) || 0;
        });
        const days = Object.keys(dailyMap).sort();
        const dailyTotals = days.map(day => dailyMap[day]);
        let min = Math.min(...dailyTotals);
        let max = Math.max(...dailyTotals);
        min = Math.max(0, min);
        if (min === max) {
          min = min > 0 ? min - 1 : 0;
          max = max + 1;
        } else {
          const padding = (max - min) * 0.2;
          min = Math.max(0, Math.floor(min - padding));
          max = Math.ceil(max + padding);
        }
        const interval = ((max - min) / 5) || 1;
        this.chart.setOption({
          title: { text: '', left: 'center' },
          tooltip: {
            trigger: 'axis',
            formatter: function (params) {
              let result = params[0].name + '<br/>';
              params.forEach(function (item) {
                result += item.marker + item.seriesName + ': ' + item.value + 'mm<br/>';
              });
              return result;
            }
          },
          grid: { left: 40, right: 30, top: 40, bottom: 40 },
          xAxis: { 
            type: 'category', 
            data: days, 
            axisLabel: { 
              rotate: 0,
              fontSize: 12,
              formatter: function(value) { return value; }
            }
          },
          yAxis: {
            type: 'value',
            name: '累计降雨量(mm)',
            nameTextStyle: { fontSize: 14 },
            axisLabel: { fontSize: 12 },
            min: 0,
            max: max,
            interval: interval,
          },
          series: [{
            name: '累计降雨量',
            type: 'bar',
            data: dailyTotals,
            itemStyle: {
              color: '#67C23A'
            }
          }]
        });
      }
    },
    formatTime(timeArr) {
      if (Array.isArray(timeArr) && timeArr.length >= 5) {
        const [y, m, d, h, min] = timeArr;
        return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}`;
      }
      return '';
    },
    parseTimeArrayToDate(timeArr) {
      if (Array.isArray(timeArr) && timeArr.length >= 5) {
        return new Date(timeArr[0], timeArr[1] - 1, timeArr[2], timeArr[3], timeArr[4], timeArr.length > 5 ? timeArr[5] : 0);
      }
      return new Date(0);
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.currentPage = 1;
    },
    handleCurrentChange(page) {
      this.currentPage = page;
    }
  },
  watch: {
    rainChartType() {
      // 切换图表类型时，重新渲染图表
      this.$nextTick(() => this.renderChart(this.filteredData));
    }
  }
}
</script>

<style scoped>
#div1 { height: 100%; width: 100%; background: #f4f6fa; padding: 20px; box-sizing: border-box;}
.search-panel {
  background: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  margin-bottom: 20px;
}
.search-form .el-form-item {
  margin-bottom: 0;
  margin-right: 15px;
}
.right-panel { height: calc(100% - 100px); overflow: auto; background: #f4f6fa; width: 100%; }
.chart-card, .table-card { margin-bottom: 20px; box-shadow: 0 2px 8px #e4e7ed22; border-radius: 8px;}
.chart-title, .table-title { font-size: 16px; font-weight: bold; color: #222; padding: 10px 0; border-bottom: 1px solid #eee; margin-bottom: 10px;}
.el-table th { background: #eaf3fc !important; color: #222; }
.el-table .el-table__row:hover td { background: #f0faff !important; }
</style>
