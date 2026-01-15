<template>
  <div>
    <el-card>
      <div style="margin-bottom: 16px; display: flex; align-items: center;">
        <el-button type="primary" @click="openDialog">新增</el-button>
      </div>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="equipmentId" label="设备ID" width="80"/>
        <el-table-column prop="equipmentName" label="设备名称"/>
        <el-table-column prop="equipmentType" label="设备类型"/>
        <el-table-column prop="installationDate" label="安装日期" :formatter="formatDate"/>
        <el-table-column prop="maintenanceDate" label="上次维护日期" :formatter="formatDate"/>
        <el-table-column prop="nextMaintenanceDate" label="下次维护日期" :formatter="formatDate"/>
        <el-table-column prop="status" label="设备状态"/>
        <el-table-column prop="location" label="设备位置"/>
        <el-table-column prop="operator" label="操作员"/>
        <el-table-column prop="createTime" label="创建时间" :formatter="formatDateTime"/>
        <el-table-column prop="updateTime" label="更新时间" :formatter="formatDateTime"/>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button size="mini" @click="editRow(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deleteRow(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 16px;"
        background
        layout="total, prev, pager, next, jumper"
        :total="total"
        :page-size="pageSize"
        :current-page.sync="currentPage"
        @current-change="fetchData"
      />
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
      <el-form :model="form" label-width="120px">
        <el-form-item label="设备名称">
          <el-input v-model="form.equipmentName" />
        </el-form-item>
        <el-form-item label="设备类型">
          <el-input v-model="form.equipmentType" />
        </el-form-item>
        <el-form-item label="安装日期">
          <el-date-picker v-model="form.installationDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="上次维护日期">
          <el-date-picker v-model="form.maintenanceDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="下次维护日期">
          <el-date-picker v-model="form.nextMaintenanceDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="设备状态">
          <el-input v-model="form.status" />
        </el-form-item>
        <el-form-item label="设备位置">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="操作员">
          <el-input v-model="form.operator" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
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
      total: 0,
      pageSize: 10,
      currentPage: 1,
      dialogVisible: false,
      dialogTitle: '新增',
      form: {}
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData(page = this.currentPage) {
      const params = { current: page, size: this.pageSize }
      axios.get('/flood-equipment/page', { params }).then(res => {
        this.tableData = res.data.data.records
        this.total = res.data.data.total
        this.currentPage = page
      })
    },
    openDialog() {
      this.dialogTitle = '新增'
      this.form = {}
      this.dialogVisible = true
    },
    editRow(row) {
      this.dialogTitle = '编辑'
      this.form = { ...row }
      this.dialogVisible = true
    },
    submitForm() {
      if (this.form.equipmentId) {
        axios.put(`/flood-equipment/${this.form.equipmentId}`, this.form).then(() => {
          this.dialogVisible = false
          this.fetchData()
        })
      } else {
        axios.post('/flood-equipment', this.form).then(() => {
          this.dialogVisible = false
          this.fetchData()
        })
      }
    },
    deleteRow(row) {
      this.$confirm('确定删除？').then(() => {
        axios.delete(`/flood-equipment/${row.equipmentId}`).then(() => this.fetchData())
      })
    },
    formatDate(row, column, cellValue) {
      if (!cellValue) return ''
      if (typeof cellValue === 'string') return cellValue.slice(0, 10)
      if (cellValue instanceof Date) return cellValue.toISOString().slice(0, 10)
      if (typeof cellValue === 'object' && cellValue.year && cellValue.monthValue && cellValue.dayOfMonth) {
        // LocalDate对象
        return `${cellValue.year}-${String(cellValue.monthValue).padStart(2, '0')}-${String(cellValue.dayOfMonth).padStart(2, '0')}`
      }
      return cellValue
    },
    formatDateTime(row, column, cellValue) {
      if (!cellValue) return ''
      if (typeof cellValue === 'string') return cellValue.replace('T', ' ').slice(0, 19)
      if (cellValue instanceof Date) return cellValue.toISOString().replace('T', ' ').slice(0, 19)
      if (typeof cellValue === 'object' && cellValue.year && cellValue.monthValue && cellValue.dayOfMonth) {
        // LocalDateTime对象
        const h = cellValue.hour || '00', m = cellValue.minute || '00', s = cellValue.second || '00'
        return `${cellValue.year}-${String(cellValue.monthValue).padStart(2, '0')}-${String(cellValue.dayOfMonth).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
      }
      return cellValue
    }
  }
}
</script> 