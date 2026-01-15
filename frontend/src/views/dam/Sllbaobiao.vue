<template>
  <div class="sllbaobiao-container">
    <el-card class="filter-card" shadow="hover">
      <div slot="header">渗流量报表</div>
      <el-form :inline="true" :model="query" class="mb-2">
        <el-form-item label="选择站点">
          <el-select v-model="query.stationId" filterable clearable placeholder="请选择站点" style="min-width: 180px" @change="fetchData" @clear="onStationClear">
            <el-option v-for="s in stationList" :key="s.id" :label="s.name || s.id" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="报表类型">
          <el-select v-model="query.type" placeholder="请选择报表类型" style="min-width: 160px" @change="onTypeChange">
            <el-option label="逐时渗流量日报表" value="day" />
            <el-option label="逐日渗流量月报表" value="month" />
            <el-option label="逐日渗流量年报表" value="year" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="query.type === 'day'" label="选择日期">
          <el-date-picker
            v-model="query.date"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            @change="fetchData"
            :cell-class-name="dateCellClass"
          />
        </el-form-item>
        <el-form-item v-if="query.type === 'month'" label="选择月份">
          <el-date-picker v-model="query.date" type="month" placeholder="选择月份" value-format="yyyy-MM" @change="fetchData" />
        </el-form-item>
        <el-form-item v-if="query.type === 'year'" label="选择年份">
          <el-date-picker v-model="query.date" type="year" placeholder="选择年份" value-format="yyyy" @change="fetchData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="fetchData">查询</el-button>
          <el-button icon="el-icon-printer" @click="printReport">打印报表</el-button>
          <el-button icon="el-icon-download" @click="exportReport">导出报表</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="table-card" shadow="hover">
      <div slot="header">渗流量{{ typeLabelMap[query.type] || '' }}</div>
      <el-table :data="tableData" border stripe style="width:100%;margin-top:10px;">
        <el-table-column v-if="query.type === 'day'" prop="hour" label="小时" align="center" width="80" />
        <el-table-column v-if="query.type === 'month'" prop="day" label="日期" align="center" width="120" />
        <el-table-column v-if="query.type === 'year'" prop="month" label="月份" align="center" width="100" />
        <el-table-column prop="seepageFlow" label="渗流量" align="center" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'
import XLSX from 'xlsx'
import FileSaver from 'file-saver'
export default {
  data() {
    return {
      stationList: [],
      query: {
        stationId: '',
        type: 'day',
        date: ''
      },
      tableData: [],
      typeLabelMap: {
        day: '日报表',
        month: '月报表',
        year: '年报表'
      },
      availableDates: []
    }
  },
  mounted() {
    this.fetchStations()
    this.fetchAvailableDates()
  },
  methods: {
    formatDate(val, type = 'date') {
      // type: 'date'|'month'|'year'
      if (!val) return ''
      // 字符串格式
      if (typeof val === 'string') {
        if (val.length >= 10) {
          if (type === 'date') return val.slice(0, 10)
          if (type === 'month') return val.slice(0, 7)
          if (type === 'year') return val.slice(0, 4)
        }
      }
      // 数组格式 [2025,5,18,10,3,29]
      if (Array.isArray(val) && val.length >= 3) {
        const [y, m, d] = val
        if (type === 'date') return `${y}-${String(m).padStart(2,'0')}-${String(d).padStart(2,'0')}`
        if (type === 'month') return `${y}-${String(m).padStart(2,'0')}`
        if (type === 'year') return `${y}`
      }
      // 13位时间戳
      if (typeof val === 'number' || (typeof val === 'string' && /^\d{13}$/.test(val))) {
        const d = new Date(Number(val))
        const y = d.getFullYear()
        const m = String(d.getMonth()+1).padStart(2,'0')
        const dd = String(d.getDate()).padStart(2,'0')
        if (type === 'date') return `${y}-${m}-${dd}`
        if (type === 'month') return `${y}-${m}`
        if (type === 'year') return `${y}`
      }
      // 其他情况尝试用Date
      const d = new Date(val)
      if (!isNaN(d.getTime())) {
        const y = d.getFullYear()
        const m = String(d.getMonth()+1).padStart(2,'0')
        const dd = String(d.getDate()).padStart(2,'0')
        if (type === 'date') return `${y}-${m}-${dd}`
        if (type === 'month') return `${y}-${m}`
        if (type === 'year') return `${y}`
      }
      return ''
    },
    async fetchAvailableDates() {
      const res = await axios.get('/seepage-data')
      let all = res.data.data || res.data || []
      if (this.query.stationId) {
        all = all.filter(i => i.stationId == this.query.stationId)
      }
      this.availableDates = Array.from(new Set(all.map(i => this.formatDate(i.recordTime, 'date')))).filter(Boolean)
    },
    async fetchStations() {
      const res = await axios.get('/seepage-data')
      let all = res.data.data || res.data || []
      const ids = Array.from(new Set(all.map(i => i.stationId))).filter(Boolean)
      this.stationList = ids.map(id => ({ id, name: String(id) }))
      let defaultStation = this.stationList.find(s => s.id == 1) || this.stationList[0]
      if (defaultStation) {
        this.query.stationId = defaultStation.id
        await this.fetchAvailableDates()
        this.fetchData()
      }
    },
    async fetchData() {
      if (!this.stationList || this.stationList.length === 0) {
        this.tableData = []
        return
      }
      if (!this.query.stationId) {
        let defaultStation = this.stationList.find(s => s.id == 1) || this.stationList[0]
        if (defaultStation) this.query.stationId = defaultStation.id
      }
      const res = await axios.get('/seepage-data')
      let all = res.data.data || res.data || []
      if (this.query.stationId) {
        all = all.filter(i => i.stationId == this.query.stationId)
      }
      await this.fetchAvailableDates()
      if (this.query.type === 'day' && this.query.date) {
        const day = this.query.date
        const rows = all.filter(i => this.formatDate(i.recordTime, 'date') === day)
        this.tableData = rows.map(i => {
          let hour = ''
          if (typeof i.recordTime === 'string') {
            const t = i.recordTime.replace('T', ' ').slice(11, 13)
            hour = String(Number(t))
          } else {
            const d = new Date(i.recordTime)
            if (!isNaN(d.getTime())) {
              hour = String(d.getHours())
            }
          }
          return {
            hour,
            seepageFlow: i.seepageFlow
          }
        })
      } else if (this.query.type === 'month' && this.query.date) {
        const month = this.query.date
        const rows = all.filter(i => this.formatDate(i.recordTime, 'month') === month)
        const dayMap = {}
        rows.forEach(i => {
          const day = this.formatDate(i.recordTime, 'date').slice(-2)
          if (!dayMap[day]) dayMap[day] = []
          dayMap[day].push(i.seepageFlow)
        })
        this.tableData = Object.keys(dayMap).sort().map(day => ({ day, seepageFlow: this.avg(dayMap[day]) }))
      } else if (this.query.type === 'year' && this.query.date) {
        const year = this.query.date
        const rows = all.filter(i => this.formatDate(i.recordTime, 'year') === year)
        const monthMap = {}
        rows.forEach(i => {
          const month = this.formatDate(i.recordTime, 'month').slice(-2)
          if (!monthMap[month]) monthMap[month] = []
          monthMap[month].push(i.seepageFlow)
        })
        this.tableData = Object.keys(monthMap).sort().map(month => ({ month, seepageFlow: this.avg(monthMap[month]) }))
      } else {
        this.tableData = []
      }
    },
    exportReport() {
      if (!this.tableData.length) {
        this.$message.warning('无可导出的数据')
        return
      }
      const ws = XLSX.utils.json_to_sheet(this.tableData)
      const wb = XLSX.utils.book_new()
      XLSX.utils.book_append_sheet(wb, ws, '渗流量报表')
      const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
      FileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '渗流量报表.xlsx')
    },
    avg(arr) {
      if (!arr || arr.length === 0) return 0
      return (arr.reduce((a, b) => a + Number(b), 0) / arr.length).toFixed(2)
    },
    onStationClear() {
      let defaultStation = this.stationList.find(s => s.id == 1) || this.stationList[0]
      if (defaultStation) {
        this.query.stationId = defaultStation.id
        this.fetchAvailableDates()
        this.fetchData()
      }
    },
    onTypeChange() {
      this.query.date = ''
      this.tableData = []
      this.fetchAvailableDates()
    },
    printReport() {
      window.print()
    },
    dateCellClass({ date }) {
      const ymd = `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`
      return this.availableDates.includes(ymd) ? 'has-data-date' : ''
    }
  }
}
</script>

<style scoped>
.sllbaobiao-container { padding: 30px; background: #f4f6fa; min-height: 100vh; }
.filter-card, .table-card { margin-bottom: 20px; }
.mb-2 { margin-bottom: 16px; }
.has-data-date > .el-date-table-cell {
  background: #ffe58f !important;
  border-radius: 50%;
}
</style> 