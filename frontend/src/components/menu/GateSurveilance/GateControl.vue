<template>
  <div class="gate-control-container">
    <el-card shadow="hover" class="control-card">
      <div slot="header" class="control-title">闸门控制</div>
      <el-form :inline="true" class="mb-2">
        <el-form-item label="选择闸门">
          <el-select v-model="selectedGates" multiple filterable placeholder="请选择闸门" style="min-width: 220px">
            <el-option v-for="gate in gateList" :key="gate.id" :label="gate.name" :value="gate.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标开度(%)">
          <el-input-number v-model="targetPosition" :min="0" :max="100" :step="1" placeholder="请输入" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-caret-right" @click="handleControl">执行控制</el-button>
        </el-form-item>
      </el-form>
      <el-alert v-if="controlMsg" :title="controlMsg" type="success" show-icon :closable="false" class="mb-2" />
      <el-table :data="gateList" border stripe highlight-current-row style="width: 100%;">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="闸门名称" align="center" />
        <el-table-column prop="position" label="当前开度(%)" align="center" />
        <el-table-column prop="upstreamLevel" label="闸前水位(m)" align="center" />
        <el-table-column prop="downstreamLevel" label="闸后水位(m)" align="center" />
        <el-table-column prop="flowRate" label="流量(m³/s)" align="center" />
        <el-table-column prop="machineStatus" label="启闭机状态" align="center" />
        <el-table-column label="单独控制" align="center" width="180">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row._target" :min="0" :max="100" :step="1" size="small" style="width:90px" />
            <el-button size="small" type="primary" @click="handleSingleControl(scope.row)">控制</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-card shadow="hover" class="table-card">
      <div slot="header" class="table-title">闸门控制记录</div>
      <el-table :data="controlTable" border stripe highlight-current-row style="width: 100%; margin-top: 10px;">
        <el-table-column prop="operationTime" label="操作时间" align="center">
          <template slot-scope="scope">{{ formatTime(scope.row.operationTime) }}</template>
        </el-table-column>
        <el-table-column prop="gateId" label="闸门名称" align="center">
          <template slot-scope="scope">{{ getGateName(scope.row.gateId) }}</template>
        </el-table-column>
        <el-table-column prop="targetPosition" label="目标开度(%)" align="center" />
        <el-table-column prop="status" label="操作状态" align="center" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      gateList: [],
      selectedGates: [],
      targetPosition: null,
      controlMsg: '',
      controlTable: [],
      gateInfoMap: {}, // id->name
    }
  },
  mounted() {
    this.fetchAll()
  },
  methods: {
    async fetchAll() {
      // 获取闸门基础信息和实时状态
      const [infoRes, statusRes, controlRes] = await Promise.all([
        axios.get('/gate-info'),
        axios.get('/gate-status'),
        axios.get('/gate-control')
      ])
      const infoList = infoRes.data.data || []
      const statusList = statusRes.data.data || []
      // 构建id->name映射
      this.gateInfoMap = {}
      infoList.forEach(g => { this.gateInfoMap[g.id] = g.name })
      // 合并name到statusList
      this.gateList = statusList.map(s => ({ ...s, name: this.gateInfoMap[s.gateId], _target: null }))
      this.controlTable = controlRes.data.data || []
    },
    getGateName(id) {
      return this.gateInfoMap[id] || id
    },
    fetchGates() { this.fetchAll() }, // 兼容原有调用
    fetchControlRecords() { this.fetchAll() },
    handleControl() {
      if (!this.selectedGates.length || this.targetPosition === null) {
        this.$message.warning('请选择闸门并输入目标开度')
        return
      }
      const reqs = this.selectedGates.map(gateId =>
        axios.post('/gate-control', {
          gate_id: gateId,
          target_position: this.targetPosition
        })
      )
      Promise.all(reqs).then(() => {
        this.controlMsg = '批量控制指令已下发！'
        setTimeout(() => (this.controlMsg = ''), 3000)
        this.fetchAll()
      })
    },
    handleSingleControl(row) {
      if (row._target === null) {
        this.$message.warning('请输入目标开度')
        return
      }
      axios.post('/gate-control', {
        gate_id: row.gateId,
        target_position: row._target
      }).then(() => {
        this.$message.success('控制指令已下发！')
        this.fetchAll()
      })
    },
    formatTime(arr) {
      // arr: [2025,5,16,14,25,21]
      if (!Array.isArray(arr) || arr.length < 5) return ''
      const pad = n => n.toString().padStart(2, '0')
      return `${arr[0]}-${pad(arr[1])}-${pad(arr[2])} ${pad(arr[3])}:${pad(arr[4])}${arr[5]!==undefined?':'+pad(arr[5]):''}`
    }
  }
}
</script>

<style scoped>
.gate-control-container { padding: 30px; background: #f4f6fa; min-height: 100vh; }
.control-card, .table-card { margin-bottom: 20px; }
.control-title, .table-title { font-size: 16px; font-weight: bold; color: #222; }
.mb-2 { margin-bottom: 16px; }
</style> 