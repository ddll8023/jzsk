<template>
  <div class="annualwaterstation-page">
    <div class="filter-bar">
      <el-form :inline="true" size="small" @submit.native.prevent>
        <el-form-item label="测站ID">
          <el-select v-model="filter.stationId" placeholder="请选择测站ID" clearable style="width: 120px">
            <el-option v-for="id in stationIdOptions" :key="id" :label="id" :value="id" />
          </el-select>
        </el-form-item>
        <el-form-item label="年份">
          <el-select v-model="filter.year" placeholder="请选择年份" clearable style="width: 120px">
            <el-option v-for="year in yearOptions" :key="year" :label="year" :value="year" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="header-bar">
      <el-button type="primary" icon="el-icon-plus" @click="showAddDialog = true">新增</el-button>
    </div>
    <el-table :data="pagedData" border stripe class="main-table" :header-cell-style="{ background: '#f8f8f9', color: '#222' }">
      <el-table-column prop="recordId" label="记录ID" align="center" width="80" />
      <el-table-column prop="stationId" label="测站ID" align="center" />
      <el-table-column prop="yearStr" label="年份" align="center" />
      <el-table-column prop="waterLevel" label="水位(m)" align="center" />
      <el-table-column prop="flowRate" label="流量(m³/s)" align="center" />
      <el-table-column prop="maxWaterLevel" label="最大水位(m)" align="center" />
      <el-table-column prop="minWaterLevel" label="最小水位(m)" align="center" />
      <el-table-column prop="maxFlowRate" label="最大流量(m³/s)" align="center" />
      <el-table-column prop="minFlowRate" label="最小流量(m³/s)" align="center" />
      <el-table-column prop="remarks" label="备注" align="center" />
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
    <el-dialog :title="dialogTitle" :visible.sync="showAddDialog">
      <el-form :model="form" ref="form" label-width="100px">
        <el-form-item label="测站ID" prop="stationId">
          <el-input v-model.number="form.stationId" type="number" />
        </el-form-item>
        <el-form-item label="年份" prop="year">
          <el-date-picker v-model="form.year" type="year" value-format="yyyy" placeholder="选择年份" />
        </el-form-item>
        <el-form-item label="水位(m)" prop="waterLevel">
          <el-input v-model.number="form.waterLevel" type="number" />
        </el-form-item>
        <el-form-item label="流量(m³/s)" prop="flowRate">
          <el-input v-model.number="form.flowRate" type="number" />
        </el-form-item>
        <el-form-item label="最大水位(m)" prop="maxWaterLevel">
          <el-input v-model.number="form.maxWaterLevel" type="number" />
        </el-form-item>
        <el-form-item label="最小水位(m)" prop="minWaterLevel">
          <el-input v-model.number="form.minWaterLevel" type="number" />
        </el-form-item>
        <el-form-item label="最大流量(m³/s)" prop="maxFlowRate">
          <el-input v-model.number="form.maxFlowRate" type="number" />
        </el-form-item>
        <el-form-item label="最小流量(m³/s)" prop="minFlowRate">
          <el-input v-model.number="form.minFlowRate" type="number" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  data() {
    return {
      tableData: [],
      filteredData: [],
      showAddDialog: false,
      dialogTitle: '新增',
      form: {
        recordId: '',
        stationId: '',
        year: '',
        waterLevel: '',
        flowRate: '',
        maxWaterLevel: '',
        minWaterLevel: '',
        maxFlowRate: '',
        minFlowRate: '',
        remarks: ''
      },
      filter: {
        stationId: '',
        year: ''
      },
      pageSize: 10,
      currentPage: 1
    }
  },
  computed: {
    stationIdOptions() {
      const set = new Set(this.tableData.map(item => item.stationId))
      return Array.from(set)
    },
    yearOptions() {
      const set = new Set(this.tableData.map(item => item.yearStr))
      return Array.from(set)
    },
    pagedData() {
      const start = (this.currentPage - 1) * this.pageSize
      return this.filteredData.slice(start, start + this.pageSize)
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    formatYear(str) {
      if (!str) return ''
      if (typeof str === 'string') return str.slice(0, 4)
      if (Array.isArray(str) && str.length > 0) return str[0]
      return ''
    },
    fetchData() {
      axios.get('/annual-water-situation').then(res => {
        let raw = res.data.data || res.data || []
        this.tableData = raw.map(item => ({
          ...item,
          yearStr: this.formatYear(item.year)
        }))
        this.applyFilter()
      }).catch(err => {
        this.$message.error('获取数据失败: ' + ((err.response && err.response.data && err.response.data.message) ? err.response.data.message : err.message))
      })
    },
    applyFilter() {
      let data = this.tableData
      if (this.filter.stationId) {
        data = data.filter(item => item.stationId == this.filter.stationId)
      }
      if (this.filter.year) {
        data = data.filter(item => item.yearStr == this.filter.year)
      }
      this.filteredData = data
      this.currentPage = 1
    },
    handleFilter() {
      this.applyFilter()
    },
    resetFilter() {
      this.filter.stationId = ''
      this.filter.year = ''
      this.applyFilter()
    },
    editRow(row) {
      this.dialogTitle = '编辑'
      this.form = { ...row }
      this.showAddDialog = true
    },
    deleteRow(id) {
      this.$confirm('确定删除该记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`/annual-water-situation/${id}`).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        }).catch(err => {
          this.$message.error('删除失败: ' + ((err.response && err.response.data && err.response.data.message) ? err.response.data.message : err.message))
        })
      }).catch(() => {})
    },
    submitForm() {
      let data = { ...this.form }
      if (this.dialogTitle === '编辑') {
        axios.put(`/annual-water-situation/${data.recordId}`, data)
          .then(() => {
            this.$message.success({ message: '更新成功', duration: 3000 })
            this.showAddDialog = false
            this.fetchData()
          })
          .catch(err => {
            this.$message.error('更新失败: ' + ((err.response && err.response.data && err.response.data.message) ? err.response.data.message : err.message))
          })
      } else {
        axios.post('/annual-water-situation', data)
          .then(() => {
            this.$message.success({ message: '新增成功', duration: 3000 })
            this.showAddDialog = false
            this.fetchData()
          })
          .catch(err => {
            this.$message.error('新增失败: ' + ((err.response && err.response.data && err.response.data.message) ? err.response.data.message : err.message))
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
.annualwaterstation-page {
  padding: 24px 24px 0 24px;
  background: #f5f6fa;
  min-height: calc(100vh - 56px);
}
.filter-bar {
  background: #fff;
  padding: 18px 18px 2px 18px;
  border-radius: 4px;
  margin-bottom: 12px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.header-bar {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 16px;
}
.main-table {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
</style>
