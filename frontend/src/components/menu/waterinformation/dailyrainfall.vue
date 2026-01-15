<template>
  <div id="div1">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="search-form" style="margin: 0 0 12px 0;">
      <el-form-item label="日期">
        <el-date-picker v-model="searchDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 180px;" />
      </el-form-item>
      <el-form-item label="测站">
        <el-select v-model="searchStation" placeholder="全部测站" clearable filterable style="width: 180px;">
          <el-option v-for="item in stationList" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="fetchData">查询</el-button>
        <el-button icon="el-icon-download" @click="exportExcel">导出</el-button>
      </el-form-item>
      <el-form-item style="float:right;">
        <el-button type="primary" round icon="el-icon-plus" @click="showAddDialog = true">新增</el-button>
      </el-form-item>
    </el-form>
    <div id="div-main">
      <el-table :data="pagedData" border stripe height="100%" style="width: 100%" :header-cell-style="headerCellStyle">
        <el-table-column type="index" label="序号" width="60px" align="center" />
        <el-table-column prop="stationName" label="测站名称" align="center" />
        <el-table-column prop="rainfallDate" label="日期" align="center" :formatter="formatDate" />
        <el-table-column prop="period811" label="8-11时" align="center" />
        <el-table-column prop="period1114" label="11-14时" align="center" />
        <el-table-column prop="period1417" label="14-17时" align="center" />
        <el-table-column prop="period1720" label="17-20时" align="center" />
        <el-table-column prop="period2023" label="20-23时" align="center" />
        <el-table-column prop="period232" label="23-2时" align="center" />
        <el-table-column prop="period25" label="2-5时" align="center" />
        <el-table-column prop="period58" label="5-8时" align="center" />
        <el-table-column prop="remark" label="备注" align="center" />
        <el-table-column fixed="right" label="操作" width="80" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="editRow(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
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
    </div>
    <el-dialog :title="dialogTitle" :visible.sync="showAddDialog">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="测站名称" prop="stationName">
          <el-input v-model="form.stationName" />
        </el-form-item>
        <el-form-item label="日期" prop="rainfallDate">
          <el-date-picker v-model="form.rainfallDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="8-11时" prop="period811">
          <el-input v-model.number="form.period811" type="number" />
        </el-form-item>
        <el-form-item label="11-14时" prop="period1114">
          <el-input v-model.number="form.period1114" type="number" />
        </el-form-item>
        <el-form-item label="14-17时" prop="period1417">
          <el-input v-model.number="form.period1417" type="number" />
        </el-form-item>
        <el-form-item label="17-20时" prop="period1720">
          <el-input v-model.number="form.period1720" type="number" />
        </el-form-item>
        <el-form-item label="20-23时" prop="period2023">
          <el-input v-model.number="form.period2023" type="number" />
        </el-form-item>
        <el-form-item label="23-2时" prop="period232">
          <el-input v-model.number="form.period232" type="number" />
        </el-form-item>
        <el-form-item label="2-5时" prop="period25">
          <el-input v-model.number="form.period25" type="number" />
        </el-form-item>
        <el-form-item label="5-8时" prop="period58">
          <el-input v-model.number="form.period58" type="number" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" maxlength="200" />
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
      showAddDialog: false,
      dialogTitle: '新增',
      form: {
        id: '',
        stationName: '',
        rainfallDate: '',
        period811: '',
        period1114: '',
        period1417: '',
        period1720: '',
        period2023: '',
        period232: '',
        period25: '',
        period58: '',
        remark: ''
      },
      rules: {
        stationName: [{ required: true, message: '请输入测站名称', trigger: 'blur' }],
        rainfallDate: [{ required: true, message: '请选择日期', trigger: 'change' }]
      },
      searchDate: '',
      searchStation: '',
      stationList: [],
      pageSize: 10,
      currentPage: 1
    }
  },
  computed: {
    pagedData() {
      const start = (this.currentPage - 1) * this.pageSize
      return this.filteredData.slice(start, start + this.pageSize)
    },
    filteredData() {
      // 按日期和测站名称过滤，rainfallDate 统一格式化为 yyyy-MM-dd
      return this.tableData.filter(row => {
        let match = true
        if (this.searchDate) {
          let dateStr = ''
          if (typeof row.rainfallDate === 'number' && row.rainfallDate > 1000000000000) {
            const d = new Date(row.rainfallDate)
            dateStr = d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0')
          } else if (typeof row.rainfallDate === 'string' && row.rainfallDate.length >= 10) {
            dateStr = row.rainfallDate.slice(0, 10)
          } else {
            const d = new Date(row.rainfallDate)
            if (!isNaN(d.getTime())) {
              dateStr = d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0')
            }
          }
          match = match && dateStr === this.searchDate
        }
        if (this.searchStation) {
          match = match && row.stationName && row.stationName.includes(this.searchStation)
        }
        return match
      })
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      axios.get('/daily-rainfall').then(res => {
        this.tableData = res.data.data || res.data
        this.stationList = [...new Set((this.tableData || []).map(row => row.stationName).filter(Boolean))]
      }).catch(err => {
        this.$message.error('获取数据失败: ' + ((err.response && err.response.data && err.response.data.message) ? err.response.data.message : err.message))
      })
    },
    exportExcel() {
      this.$message.info('导出功能开发中...')
    },
    editRow(row) {
      this.dialogTitle = '编辑'
      this.form = { ...row }
      this.showAddDialog = true
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        let data = { ...this.form }
        data.period811 = data.period811 !== '' ? Number(data.period811) : null
        data.period1114 = data.period1114 !== '' ? Number(data.period1114) : null
        data.period1417 = data.period1417 !== '' ? Number(data.period1417) : null
        data.period1720 = data.period1720 !== '' ? Number(data.period1720) : null
        data.period2023 = data.period2023 !== '' ? Number(data.period2023) : null
        data.period232 = data.period232 !== '' ? Number(data.period232) : null
        data.period25 = data.period25 !== '' ? Number(data.period25) : null
        data.period58 = data.period58 !== '' ? Number(data.period58) : null
        if (this.dialogTitle === '编辑') {
          axios.put(`/daily-rainfall/${data.id}`, data)
            .then(() => {
              this.$message.success({ message: '更新成功', duration: 3000 })
              this.showAddDialog = false
              this.fetchData()
            })
            .catch(err => {
              this.$message.error('更新失败: ' + ((err.response && err.response.data && err.response.data.message) ? err.response.data.message : err.message))
            })
        } else {
          axios.post('/daily-rainfall', data)
            .then(() => {
              this.$message.success({ message: '新增成功', duration: 3000 })
              this.showAddDialog = false
              this.fetchData()
            })
            .catch(err => {
              this.$message.error('新增失败: ' + ((err.response && err.response.data && err.response.data.message) ? err.response.data.message : err.message))
            })
        }
      })
    },
    headerCellStyle() {
      return {
        background: '#eaf6ff',
        color: '#333',
        fontWeight: 'bold',
        border: '2px solid #e4e7ed',
        fontSize: '18px',
        height: '38px',
        padding: '0',
      }
    },
    cellStyle() {
      return {
        border: '2px solid #e4e7ed',
        fontSize: '16px',
        padding: '10px 0',
        background: '#fff',
      }
    },
    formatDate(row, column, cellValue) {
      if (!cellValue) return ''
      // 处理时间戳和字符串
      if (typeof cellValue === 'number' && cellValue > 1000000000000) {
        const d = new Date(cellValue)
        return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0')
      }
      if (/^\d{4}-\d{2}-\d{2}$/.test(cellValue)) return cellValue
      // 其它情况直接返回
      return cellValue
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
#div1 {
  height: 100%;
  width: 100%;
}
#bread {
  height: 25px;
  width: 100%;
}
#div-main {
  height: calc(100% - 120px);
  width: 100%;
}
.search-form {
  background: #fff;
  padding: 12px 24px 0 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px #e4e7ed22;
  margin-bottom: 12px;
}
/* 表格字体大小与 station.vue 保持一致 */
::v-deep .el-table th {
  background: #cfe2f3 !important;
  color: #606266 !important;
  font-weight: bold;
  font-size: 16px !important;
  height: 38px;
  padding: 0;
}
::v-deep .el-table td {
  font-size: 15px !important;
  padding: 10px 0;
  background: #fff !important;
}
</style> 