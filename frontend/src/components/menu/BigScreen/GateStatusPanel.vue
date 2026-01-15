<template>
  <el-card class="gate-panel" shadow="hover">
    <div slot="header">闸门实时状态</div>
    <el-table :data="mock ? mockData : tableData" border stripe size="medium" class="gate-table">
      <el-table-column prop="gateName" label="闸门" align="center" />
      <el-table-column prop="status" label="状态" align="center" />
      <el-table-column prop="openDegree" label="开度(%)" align="center" />
    </el-table>
  </el-card>
</template>
<script>
import axios from 'axios'
export default {
  name: 'GateStatusPanel',
  props: { mock: { type: Boolean, default: false } },
  data() {
    return {
      tableData: [],
      mockData: [
        { gateName: '1号闸门', status: '开启', openDegree: 80 },
        { gateName: '2号闸门', status: '关闭', openDegree: 0 },
        { gateName: '3号闸门', status: '部分开启', openDegree: 45 }
      ]
    }
  },
  mounted() { if (!this.mock) this.fetchData() },
  methods: {
    fetchData() {
      axios.get('/gate_status').then(res => {
        this.tableData = res.data.data || res.data || []
      })
    }
  }
}
</script>
<style scoped>
.gate-panel { background: #12203a; color: #fff; }
.gate-table >>> .el-table__header th { background: #1a2a3a; color: #00eaff; font-size: 18px; }
.gate-table >>> .el-table__body td { font-size: 18px; }
</style> 