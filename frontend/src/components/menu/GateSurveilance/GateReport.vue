<template>
  <div class="gate-report-container">
    <el-card shadow="hover" class="filter-card">
      <div slot="header" class="filter-title">闸门报表查询</div>
      <el-form :inline="true" :model="query" class="mb-2">
        <el-form-item label="选择闸门">
          <el-select v-model="query.gateCode" filterable placeholder="请选择闸门" style="min-width: 180px" @change="handleGateChange">
            <el-option v-for="gate in gateList" :key="gate.code" :label="gate.name" :value="gate.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择日期">
          <el-date-picker v-model="query.dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="fetchReport">查询</el-button>
          <el-button icon="el-icon-download" @click="exportReport">导出报表</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="hover" class="table-card">
      <div slot="header" class="table-title">闸门报表</div>
      <el-table :data="pagedTable" border stripe highlight-current-row style="width: 100%; margin-top: 10px;">
        <el-table-column v-for="col in tableColumns" :key="col.prop" :prop="col.prop" :label="fieldToLabel(col.prop)" align="center" />
      </el-table>
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="reportTable.length"
        :page-size="pageSize"
        :current-page="currentPage"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 16px; text-align: right;"
      />
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      gateList: [
        { code: 'dgq', name: '东干渠' },
        { code: 'dzdf', name: '电站蝶阀' },
        { code: 'qst', name: '取水塔' },
        { code: 'xgq', name: '西干渠' },
        { code: 'yhd', name: '溢洪道' }
      ],
      query: {
        gateCode: 'dgq',
        dateRange: []
      },
      reportTable: [],
      tableColumns: [],
      pageSize: 10,
      currentPage: 1
    }
  },
  computed: {
    pagedTable() {
      const start = (this.currentPage - 1) * this.pageSize
      return this.reportTable.slice(start, start + this.pageSize)
    }
  },
  methods: {
    handleGateChange() {
      // 切换闸门后立即加载对应数据
      this.fetchReport()
    },
    async fetchReport() {
      if (!this.query.gateCode) {
        this.reportTable = []
        this.tableColumns = []
        return
      }
      const url = `/zkxt/${this.query.gateCode}`
      const { data: res } = await axios.get(url)
      const records = Array.isArray(res) ? res : (res && res.records) || []
      // 只保留选中日期范围内的数据
      let filtered = records
        if (this.query.dateRange && this.query.dateRange.length === 2) {
          const [start, end] = this.query.dateRange
        const startDate = new Date(start)
        const endDate = new Date(end)
        filtered = records.filter(item => {
          const itemTime = this.parseTime(item.TM)
          // 只比较年月日，忽略时分秒
          return this.isDateInRange(itemTime, startDate, endDate)
        })
      }
      this.reportTable = filtered
      this.currentPage = 1
      if (filtered.length > 0) {
        // 时间字段最左侧
        const keys = Object.keys(filtered[0])
        let timeKey = keys.find(k => k.toLowerCase() === 'tm' || k.toLowerCase() === 'time')
        let otherKeys = keys.filter(k => k !== timeKey)
        this.tableColumns = []
        if (timeKey) this.tableColumns.push({ prop: timeKey })
        this.tableColumns.push(...otherKeys.map(k => ({ prop: k })))
          } else {
        this.tableColumns = []
      }
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
    },
    handleCurrentChange(page) {
      this.currentPage = page
    },
    exportReport() {
      if (this.reportTable.length === 0) {
        this.$message.warning('没有数据可导出！')
        return
      }
      const headers = this.tableColumns.map(col => this.fieldToLabel(col.prop))
      const rows = this.reportTable.map(item => this.tableColumns.map(col => item[col.prop]))
      let csvContent = '\ufeff' + headers.join(',') + '\n'
      rows.forEach(row => {
        csvContent += row.map(e => `"${e}"`).join(',') + '\n'
      })
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      if (link.download !== undefined) {
        const url = URL.createObjectURL(blob)
        link.setAttribute('href', url)
        link.setAttribute('download', 'gate-report.csv')
        link.style.visibility = 'hidden'
        document.body.appendChild(link)
      link.click()
        document.body.removeChild(link)
      } else {
        window.open('data:text/csv;charset=utf-8,' + escape(csvContent))
      }
      this.$message.success('数据已导出！')
    },
    parseTime(tm) {
      if (Array.isArray(tm) && tm.length >= 3) {
        return new Date(...tm)
      }
      if (typeof tm === 'string' && tm.length >= 10) {
        // 兼容所有浏览器，将 '2025-06-22 14:54:50' 替换为 '2025/06/22 14:54:50'
        const safeStr = tm.replace(/-/g, '/').replace('T', ' ')
        return new Date(safeStr)
      }
      if (tm instanceof Date) return tm
      return new Date(tm)
    },
    isDateInRange(date, start, end) {
      // 只比较年月日，忽略时分秒
      const d = new Date(date.getFullYear(), date.getMonth(), date.getDate())
      const s = new Date(start.getFullYear(), start.getMonth(), start.getDate())
      const e = new Date(end.getFullYear(), end.getMonth(), end.getDate())
      return d >= s && d <= e
    },
    fieldToLabel(key) {
      const map = {
        // 东干渠M1
        'dgq_M1_Ua': 'M闸门1A相电压',
        'dgq_M1_Ub': 'M闸门1B相电压',
        'dgq_M1_Uc': 'M闸门1C相电压',
        'dgq_M1_Uab': 'M闸门1AB线电压',
        'dgq_M1_Ubc': 'M闸门1BC线电压',
        'dgq_M1_Uca': 'M闸门1CA线电压',
        'dgq_M1_Ia': 'M闸门1A相电流',
        'dgq_M1_Ib': 'M闸门1B相电流',
        'dgq_M1_Ic': 'M闸门1C相电流',
        'dgq_M1_KD': 'M闸门1开度',
        'dgq_M1_KDSD': 'M闸门1开度设定',
        // 电站蝶阀M1
        'dzdf_M1_Ua': '电站蝶阀A相电压',
        'dzdf_M1_Ub': '电站蝶阀B相电压',
        'dzdf_M1_Uc': '电站蝶阀C相电压',
        'dzdf_M1_Uab': '电站蝶阀AB线电压',
        'dzdf_M1_Ubc': '电站蝶阀BC线电压',
        'dzdf_M1_Uca': '电站蝶阀CA线电压',
        'dzdf_M1_Ia': '电站蝶阀A相电流',
        'dzdf_M1_Ib': '电站蝶阀B相电流',
        'dzdf_M1_Ic': '电站蝶阀C相电流',
        'dzdf_M1_FIT': '电站蝶阀流量',
        'dzdf_M1_FIT_TOL': '电站蝶阀累计流量',
        'dzdf_M1_YW': '电站蝶阀液位',
        // 取水塔M1
        'qst_M1_Ia': '取水塔M1A相电流',
        'qst_M1_Ib': '取水塔M1B相电流',
        'qst_M1_Ic': '取水塔M1C相电流',
        'qst_M1_Ua': '取水塔M1A相电压',
        'qst_M1_Ub': '取水塔M1B相电压',
        'qst_M1_Uc': '取水塔M1C相电压',
        'qst_M1_Uab': '取水塔M1AB线电压',
        'qst_M1_Ubc': '取水塔M1BC线电压',
        'qst_M1_Uca': '取水塔M1CA线电压',
        'qst_M1_KD': '取水塔M1开度',
        'qst_M1_KDSD': '取水塔M1开度设定',
        // 取水塔M2
        'qst_M2_Ia': '取水塔M2A相电流',
        'qst_M2_Ib': '取水塔M2B相电流',
        'qst_M2_Ic': '取水塔M2C相电流',
        'qst_M2_Ua': '取水塔M2A相电压',
        'qst_M2_Ub': '取水塔M2B相电压',
        'qst_M2_Uc': '取水塔M2C相电压',
        'qst_M2_Uab': '取水塔M2AB线电压',
        'qst_M2_KD': '取水塔M2开度',
        'qst_M2_KDSD': '取水塔M2开度设定',
        // 西干渠M1
        'xgq_M1_Ia': '西干渠M1A相电流',
        'xgq_M1_Ib': '西干渠M1B相电流',
        'xgq_M1_Ic': '西干渠M1C相电流',
        'xgq_M1_Ua': '西干渠M1A相电压',
        'xgq_M1_Ub': '西干渠M1B相电压',
        'xgq_M1_Uc': '西干渠M1C相电压',
        'xgq_M1_Uab': '西干渠M1AB线电压',
        'xgq_M1_Ubc': '西干渠M1BC线电压',
        'xgq_M1_Uca': '西干渠M1CA线电压',
        'xgq_M1_KD': '西干渠M1开度',
        'xgq_M1_KDSD': '西干渠M1开度设定',
        // 西干渠M2
        'xgq_M2_Ia': '西干渠M2A相电流',
        'xgq_M2_Ib': '西干渠M2B相电流',
        'xgq_M2_Ic': '西干渠M2C相电流',
        'xgq_M2_Ua': '西干渠M2A相电压',
        'xgq_M2_Ub': '西干渠M2B相电压',
        'xgq_M2_Uc': '西干渠M2C相电压',
        'xgq_M2_Uab': '西干渠M2AB线电压',
        'xgq_M2_KD': '西干渠M2开度',
        'xgq_M2_KDSD': '西干渠M2开度设定',
        // 溢洪道M1
        'yhd_M1_Ia': '溢洪道M1A相电流',
        'yhd_M1_Ib': '溢洪道M1B相电流',
        'yhd_M1_Ic': '溢洪道M1C相电流',
        'yhd_M1_Ua': '溢洪道M1A相电压',
        'yhd_M1_Ub': '溢洪道M1B相电压',
        'yhd_M1_Uc': '溢洪道M1C相电压',
        'yhd_M1_Uab': '溢洪道M1AB线电压',
        'yhd_M1_Ubc': '溢洪道M1BC线电压',
        'yhd_M1_Uca': '溢洪道M1CA线电压',
        'yhd_M1_KD': '溢洪道M1开度',
        'yhd_M1_KDSD': '溢洪道M1开度设定',
        // 溢洪道M2
        'yhd_M2_Ia': '溢洪道M2A相电流',
        'yhd_M2_Ib': '溢洪道M2B相电流',
        'yhd_M2_Ic': '溢洪道M2C相电流',
        'yhd_M2_Ua': '溢洪道M2A相电压',
        'yhd_M2_Ub': '溢洪道M2B相电压',
        'yhd_M2_Uc': '溢洪道M2C相电压',
        'yhd_M2_Uab': '溢洪道M2AB线电压',
        'yhd_M2_Ubc': '溢洪道M2BC线电压',
        'yhd_M2_Uca': '溢洪道M2CA线电压',
        'yhd_M2_KD': '溢洪道M2开度',
        'yhd_M2_KDSD': '溢洪道M2开度设定',
        // 溢洪道M3
        'yhd_M3_Ia': '溢洪道M3A相电流',
        'yhd_M3_Ib': '溢洪道M3B相电流',
        'yhd_M3_Ic': '溢洪道M3C相电流',
        'yhd_M3_Ua': '溢洪道M3A相电压',
        'yhd_M3_Ub': '溢洪道M3B相电压',
        'yhd_M3_Uc': '溢洪道M3C相电压',
        'yhd_M3_Uab': '溢洪道M3AB线电压',
        'yhd_M3_Ubc': '溢洪道M3BC线电压',
        'yhd_M3_Uca': '溢洪道M3CA线电压',
        'yhd_M3_KD': '溢洪道M3开度',
        'yhd_M3_KDSD': '溢洪道M3开度设定',
        // 通用
        'tm': '时间', 'TM': '时间', 'time': '时间', 'Time': '时间',
        'KD': '开度(%)',
        'IA_ZM1': 'A相电流(A)', 'UA_ZM1': 'A相电压(V)',
        'IA_ZM2': 'A相电流2(A)', 'UA_ZM2': 'A相电压2(V)',
        'IA_ZM3': 'A相电流3(A)', 'UA_ZM3': 'A相电压3(V)'
      }
      return map[key] || key
    }
  },
  mounted() {
    this.fetchReport()
  }
}
</script>

<style scoped>
.gate-report-container { padding: 30px; background: #f4f6fa; min-height: 100vh; }
.filter-card, .table-card { margin-bottom: 20px; }
.filter-title, .table-title { font-size: 16px; font-weight: bold; color: #222; }
.mb-2 { margin-bottom: 16px; }
</style> 