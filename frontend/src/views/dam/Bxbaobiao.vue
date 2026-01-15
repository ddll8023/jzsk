<template>
  <div class="bxbaobiao-container">
    <el-card class="filter-card" shadow="hover">
      <div slot="header">变形报表</div>
      <el-form :inline="true" :model="query" class="mb-2">
        <el-form-item label="选择大坝">
          <el-select v-model="query.projectName" filterable clearable placeholder="请选择大坝" style="min-width: 180px" @change="fetchData" @clear="onProjectClear">
            <el-option v-for="s in projectList" :key="s" :label="s" :value="s" />
          </el-select>
        </el-form-item>
        <el-form-item label="报表类型">
          <el-select v-model="query.type" placeholder="请选择报表类型" style="min-width: 160px" @change="onTypeChange">
            <el-option label="逐时变形日报表" value="day" />
            <el-option label="逐日变形月报表" value="month" />
            <el-option label="逐月变形年报表" value="year" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="query.type === 'day'" label="选择日期">
          <el-date-picker v-model="query.date" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" @change="fetchData" />
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
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="table-card" shadow="hover">
      <div slot="header">变形{{ typeLabelMap[query.type] || '' }}</div>
      <el-table :data="tableData" border stripe style="width:100%;margin-top:10px;">
        <el-table-column v-if="query.type === 'day'" prop="hour" label="小时" align="center" width="80" />
        <el-table-column v-if="query.type === 'month'" prop="day" label="日期" align="center" width="120" />
        <el-table-column v-if="query.type === 'year'" prop="month" label="月份" align="center" width="100" />
        <el-table-column prop="avgHorizontal" label="平均水平位移(mm)" align="center" />
        <el-table-column prop="avgVertical" label="平均垂直位移(mm)" align="center" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      projectList: [],
      query: {
        projectName: '',
        type: 'day',
        date: ''
      },
      tableData: [],
      typeLabelMap: {
        day: '日报表',
        month: '月报表',
        year: '年报表'
      }
    }
  },
  mounted() {
    this.fetchProjects()
  },
  methods: {
    async fetchProjects() {
      // 获取所有大坝名称（合并水平和垂直位移数据）
      const [hRes, vRes] = await Promise.all([
        axios.get('/horizontal-displacement'),
        axios.get('/vertical-displacement')
      ])
      const hList = hRes.data.data || hRes.data || []
      const vList = vRes.data.data || vRes.data || []
      const names = Array.from(new Set([
        ...hList.map(i => i.projectName),
        ...vList.map(i => i.projectName)
      ])).filter(Boolean)
      this.projectList = names
      let defaultProject = this.projectList[0]
      if (defaultProject) {
        this.query.projectName = defaultProject
        this.fetchData()
      }
    },
    async fetchData() {
      if (!this.query.projectName) {
        let defaultProject = this.projectList[0]
        if (defaultProject) this.query.projectName = defaultProject
      }
      // 获取水平和垂直位移数据
      const [hRes, vRes] = await Promise.all([
        axios.get('/horizontal-displacement'),
        axios.get('/vertical-displacement')
      ])
      let hList = hRes.data.data || hRes.data || []
      let vList = vRes.data.data || vRes.data || []
      // 只筛选当前大坝
      hList = hList.filter(i => i.projectName === this.query.projectName)
      vList = vList.filter(i => i.projectName === this.query.projectName)
      // 合并时间字段格式
      hList = hList.map(i => ({ ...i, recordTimeStr: this.formatTime(i.recordTime) }))
      vList = vList.map(i => ({ ...i, recordTimeStr: this.formatTime(i.recordTime) }))
      // 按报表类型分组统计
      if (this.query.type === 'day' && this.query.date) {
        // 逐时日报表
        const day = this.query.date
        const hRows = hList.filter(i => (i.recordTimeStr || '').startsWith(day))
        const vRows = vList.filter(i => (i.recordTimeStr || '').startsWith(day))
        // 按小时分组
        const hourMap = {}
        for (let i = 0; i < 24; i++) {
          const hour = i.toString().padStart(2, '0')
          const hVals = hRows.filter(r => (r.recordTimeStr || '').substr(11, 2) === hour).map(r => Number(r.xaxisDisplacement))
          const vVals = vRows.filter(r => (r.recordTimeStr || '').substr(11, 2) === hour).map(r => Number(r.yaxisDisplacement))
          if (hVals.length || vVals.length) {
            hourMap[hour] = {
              hour,
              avgHorizontal: this.avg(hVals),
              avgVertical: this.avg(vVals)
            }
          }
        }
        this.tableData = Object.values(hourMap)
      } else if (this.query.type === 'month' && this.query.date) {
        // 逐日月报表
        const month = this.query.date
        const hRows = hList.filter(i => (i.recordTimeStr || '').startsWith(month))
        const vRows = vList.filter(i => (i.recordTimeStr || '').startsWith(month))
        // 按天分组
        const dayMap = {}
        for (let i = 1; i <= 31; i++) {
          const day = i.toString().padStart(2, '0')
          const hVals = hRows.filter(r => (r.recordTimeStr || '').substr(8, 2) === day).map(r => Number(r.xaxisDisplacement))
          const vVals = vRows.filter(r => (r.recordTimeStr || '').substr(8, 2) === day).map(r => Number(r.yaxisDisplacement))
          if (hVals.length || vVals.length) {
            dayMap[day] = {
              day,
              avgHorizontal: this.avg(hVals),
              avgVertical: this.avg(vVals)
            }
          }
        }
        this.tableData = Object.values(dayMap)
      } else if (this.query.type === 'year' && this.query.date) {
        // 逐月年报表
        const year = this.query.date
        const hRows = hList.filter(i => (i.recordTimeStr || '').startsWith(year))
        const vRows = vList.filter(i => (i.recordTimeStr || '').startsWith(year))
        // 按月分组
        const monthMap = {}
        for (let i = 1; i <= 12; i++) {
          const month = i.toString().padStart(2, '0')
          const hVals = hRows.filter(r => (r.recordTimeStr || '').substr(5, 2) === month).map(r => Number(r.xaxisDisplacement))
          const vVals = vRows.filter(r => (r.recordTimeStr || '').substr(5, 2) === month).map(r => Number(r.yaxisDisplacement))
          if (hVals.length || vVals.length) {
            monthMap[month] = {
              month,
              avgHorizontal: this.avg(hVals),
              avgVertical: this.avg(vVals)
            }
          }
        }
        this.tableData = Object.values(monthMap)
      } else {
        this.tableData = []
      }
    },
    avg(arr) {
      if (!arr || arr.length === 0) return 0
      return (arr.reduce((a, b) => a + Number(b), 0) / arr.length).toFixed(2)
    },
    formatTime(str) {
      if (!str) return ''
      if (typeof str === 'string') return str.replace('T', ' ').slice(0, 19)
      if (Array.isArray(str) && str.length >= 3) {
        const pad = n => n.toString().padStart(2, '0')
        return `${str[0]}-${pad(str[1])}-${pad(str[2])}` +
          (str.length >= 6 ? ` ${pad(str[3])}:${pad(str[4])}:${pad(str[5])}` : '')
      }
      return ''
    },
    onProjectClear() {
      let defaultProject = this.projectList[0]
      if (defaultProject) {
        this.query.projectName = defaultProject
        this.fetchData()
      }
    },
    onTypeChange() {
      this.query.date = ''
      this.tableData = []
    },
    printReport() {
      window.print()
    }
  }
}
</script>

<style scoped>
.bxbaobiao-container { padding: 30px; background: #f4f6fa; min-height: 100vh; }
.filter-card, .table-card { margin-bottom: 20px; }
.mb-2 { margin-bottom: 16px; }
</style> 