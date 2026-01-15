<template>
  <div>
    <el-card>
      <div style="margin-bottom: 16px; display: flex; align-items: center;">
        <el-date-picker
          v-model="searchDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          style="margin-right: 16px;"
        />
        <el-button type="primary" @click="fetchData">搜索</el-button>
        <el-button type="primary" @click="openDialog" style="margin-left: auto;">新增</el-button>
        <el-button type="danger" @click="batchDelete" :disabled="!multipleSelection.length" style="margin-left: 8px;">批量删除</el-button>
      </div>
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="值班日志id" label="值班日志ID" width="100"/>
        <el-table-column prop="值班日期" label="值班日期" :formatter="formatDate"/>
        <el-table-column prop="天气" label="天气"/>
        <el-table-column prop="雨量" label="雨量"/>
        <el-table-column prop="带班领导" label="带班领导"/>
        <el-table-column prop="白班值班人员" label="白班值班人员"/>
        <el-table-column prop="晚班值班人员" label="晚班值班人员"/>
        <el-table-column prop="日志内容" label="日志内容"/>
        <el-table-column prop="日志填写时间" label="日志填写时间" :formatter="formatDateTime"/>
        <el-table-column prop="日志状态" label="日志状态"/>
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
        <el-form-item label="值班日期">
          <el-date-picker v-model="form.值班日期" type="date" value-format="yyyy-MM-dd" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="天气">
          <el-input v-model="form.天气" />
        </el-form-item>
        <el-form-item label="雨量">
          <el-input v-model="form.雨量" type="number" />
        </el-form-item>
        <el-form-item label="带班领导">
          <el-input v-model="form.带班领导" />
        </el-form-item>
        <el-form-item label="白班值班人员">
          <el-input v-model="form.白班值班人员" />
        </el-form-item>
        <el-form-item label="晚班值班人员">
          <el-input v-model="form.晚班值班人员" />
        </el-form-item>
        <el-form-item label="日志内容">
          <el-input v-model="form.日志内容" type="textarea" />
        </el-form-item>
        <el-form-item label="日志状态">
          <el-input v-model="form.日志状态" />
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
      form: {},
      multipleSelection: [],
      searchDateRange: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      let params = { current: this.currentPage, size: this.pageSize }
      if (this.searchDateRange && this.searchDateRange.length === 2) {
        params.startDate = this.searchDateRange[0]
        params.endDate = this.searchDateRange[1]
      }
      axios.get('/duty-log/page', { params }).then(res => {
        this.tableData = res.data.data.records
        this.total = res.data.data.total
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
      if (this.form.值班日志id) {
        axios.put(`/duty-log/${this.form.值班日志id}`, this.form).then(() => {
          this.dialogVisible = false
          this.fetchData()
        })
      } else {
        axios.post('/duty-log', this.form).then(() => {
          this.dialogVisible = false
          this.fetchData()
        })
      }
    },
    deleteRow(row) {
      this.$confirm('确定删除？').then(() => {
        axios.delete(`/duty-log/${row.值班日志id}`).then(() => this.fetchData())
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    batchDelete() {
      const ids = this.multipleSelection.map(i => i.值班日志id)
      this.$confirm('确定批量删除？').then(() => {
        axios.delete('/duty-log/batch', { data: ids }).then(() => this.fetchData())
      })
    },
    formatDate(row, column, cellValue) {
      if (!cellValue) return ''
      let d
      if (Array.isArray(cellValue)) {
        const [y, M, D] = cellValue
        d = new Date(y, M - 1, D)
      } else if (typeof cellValue === 'object' && cellValue.year) {
        const { year, monthValue, dayOfMonth } = cellValue
        d = new Date(year, monthValue - 1, dayOfMonth)
      } else if (typeof cellValue === 'string') {
        d = new Date(cellValue.replace('T', ' '))
      } else if (typeof cellValue === 'number') {
        d = new Date(cellValue)
      } else {
        return String(cellValue)
      }
      if (isNaN(d.getTime())) return String(cellValue)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    formatDateTime(row, column, cellValue) {
      if (!cellValue) return ''
      let d
      if (Array.isArray(cellValue)) {
        const [y, M, D, h = 0, m = 0, s = 0] = cellValue;
        d = new Date(y, M - 1, D, h, m, s);
      } else if (typeof cellValue === 'object' && cellValue.year) {
        const { year, monthValue, dayOfMonth, hour = 0, minute = 0, second = 0 } = cellValue;
        d = new Date(year, monthValue - 1, dayOfMonth, hour, minute, second);
      } else if (typeof cellValue === 'string') {
        d = new Date(cellValue.replace('T', ' '));
      } else if (typeof cellValue === 'number') {
        d = new Date(cellValue);
      } else {
        return String(cellValue);
      }
      if (isNaN(d.getTime())) return String(cellValue);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const hours = String(d.getHours()).padStart(2, '0');
      const minutes = String(d.getMinutes()).padStart(2, '0');
      const seconds = String(d.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }
  }
}
</script> 