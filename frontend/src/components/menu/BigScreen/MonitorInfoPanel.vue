<template>
  <el-card class="monitor-panel" shadow="hover">
    <div slot="header">测点信息</div>
    <el-table :data="mock ? mockData : tableData" border stripe size="medium" class="monitor-table">
      <el-table-column prop="siteName" label="测点名称" align="center" />
      <el-table-column prop="status" label="状态" align="center" />
      <el-table-column prop="value" label="当前值" align="center" />
    </el-table>
  </el-card>
</template>
<script>
import axios from 'axios'
export default {
  name: 'MonitorInfoPanel',
  props: { mock: { type: Boolean, default: false } },
  data() {
    return {
      tableData: [],
      mockData: [
        { siteName: '坝体1号', status: '正常', value: 12.3 },
        { siteName: '坝体2号', status: '正常', value: 11.8 },
        { siteName: '坝体3号', status: '预警', value: 15.2 },
        { siteName: '坝体4号', status: '正常', value: 12.7 }
      ]
    }
  },
  mounted() { if (!this.mock) this.fetchData() },
  methods: {
    fetchData() {
      axios.get('/monitorsite').then(res => {
        this.tableData = res.data.data || res.data || []
      })
    }
  }
}
</script>
<style scoped>
.monitor-panel { background: #12203a; color: #fff; }
.monitor-table >>> .el-table__header th { background: #1a2a3a; color: #00eaff; font-size: 18px; }
.monitor-table >>> .el-table__body td { font-size: 18px; }
</style> 