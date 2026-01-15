<template>
  <div class="stpextremumb-page">
    <div class="filter-bar">
      <el-form :inline="true" size="small" @submit.native.prevent>
        <el-form-item label="监测站代码">
          <el-select v-model="searchForm.stcd" filterable placeholder="请选择" clearable style="width: 180px">
            <el-option v-for="item in stcdList" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-bar">
      <el-card shadow="hover" class="table-card">
        <div slot="header" class="table-title">雨量极值信息数据
          <el-button type="primary" round icon="el-icon-plus" style="float:right;" @click="showAddDialog = true">新增</el-button>
        </div>
        <el-table :data="pagedData" border stripe highlight-current-row style="width: 100%; margin-top: 10px;" :header-cell-style="{ background: '#eaf3fc', color: '#222' }">
          <el-table-column type="index" label="序号" width="60px" align="center"></el-table-column>
          <el-table-column prop="stcd" label="监测站代码" align="center"></el-table-column>
          <el-table-column prop="maxdrp1h" label="1小时最大雨量" align="center"></el-table-column>
          <el-table-column prop="tm1h" label="1小时最大雨量发生时间" align="center">
            <template slot-scope="scope">
              {{ formatTime(scope.row.tm1h) }}
            </template>
          </el-table-column>
          <el-table-column prop="maxdrp3h" label="3小时最大雨量" align="center"></el-table-column>
          <el-table-column prop="tm3h" label="3小时最大雨量发生时间" align="center">
            <template slot-scope="scope">
              {{ formatTime(scope.row.tm3h) }}
            </template>
          </el-table-column>
          <el-table-column prop="maxdrp6h" label="6小时最大雨量" align="center"></el-table-column>
          <el-table-column prop="tm6h" label="6小时最大雨量发生时间" align="center">
            <template slot-scope="scope">
              {{ formatTime(scope.row.tm6h) }}
            </template>
          </el-table-column>
          <el-table-column prop="maxdrp12h" label="12小时最大雨量" align="center"></el-table-column>
          <el-table-column prop="tm12h" label="12小时最大雨量发生时间" align="center">
            <template slot-scope="scope">
              {{ formatTime(scope.row.tm12h) }}
            </template>
          </el-table-column>
          <el-table-column prop="maxdrp24h" label="24小时最大雨量" align="center"></el-table-column>
          <el-table-column prop="tm24h" label="24小时最大雨量发生时间" align="center">
            <template slot-scope="scope">
              {{ formatTime(scope.row.tm24h) }}
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" align="center"></el-table-column>
          <el-table-column fixed="right" label="操作" width="180" align="center">
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
      </el-card>
    </div>
    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="showAddDialog">
      <el-form :model="form" :rules="rules" ref="form" label-width="140px">
        <el-form-item label="监测站代码" prop="stcd">
          <el-input v-model="form.stcd" maxlength="8" />
        </el-form-item>
        <el-form-item label="1小时最大雨量" prop="maxdrp1h">
          <el-input v-model.number="form.maxdrp1h" type="number" />
        </el-form-item>
        <el-form-item label="1小时最大雨量发生时间" prop="tm1h">
          <el-date-picker v-model="form.tm1h" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择时间" />
        </el-form-item>
        <el-form-item label="3小时最大雨量" prop="maxdrp3h">
          <el-input v-model.number="form.maxdrp3h" type="number" />
        </el-form-item>
        <el-form-item label="3小时最大雨量发生时间" prop="tm3h">
          <el-date-picker v-model="form.tm3h" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择时间" />
        </el-form-item>
        <el-form-item label="6小时最大雨量" prop="maxdrp6h">
          <el-input v-model.number="form.maxdrp6h" type="number" />
        </el-form-item>
        <el-form-item label="6小时最大雨量发生时间" prop="tm6h">
          <el-date-picker v-model="form.tm6h" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择时间" />
        </el-form-item>
        <el-form-item label="12小时最大雨量" prop="maxdrp12h">
          <el-input v-model.number="form.maxdrp12h" type="number" />
        </el-form-item>
        <el-form-item label="12小时最大雨量发生时间" prop="tm12h">
          <el-date-picker v-model="form.tm12h" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择时间" />
        </el-form-item>
        <el-form-item label="24小时最大雨量" prop="maxdrp24h">
          <el-input v-model.number="form.maxdrp24h" type="number" />
        </el-form-item>
        <el-form-item label="24小时最大雨量发生时间" prop="tm24h">
          <el-date-picker v-model="form.tm24h" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择时间" />
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
        stcd: '',
        maxdrp1h: '',
        maxdrp3h: '',
        maxdrp6h: '',
        maxdrp12h: '',
        maxdrp24h: '',
        tm1h: '',
        tm3h: '',
        tm6h: '',
        tm12h: '',
        tm24h: '',
        remark: ''
      },
      rules: {
        stcd: [{ required: true, message: '请输入监测站代码', trigger: 'blur' }],
        maxdrp1h: [{ required: true, message: '请输入1小时最大雨量', trigger: 'blur' }],
        tm1h: [{ required: true, message: '请选择1小时最大雨量发生时间', trigger: 'change' }],
        maxdrp3h: [{ required: true, message: '请输入3小时最大雨量', trigger: 'blur' }],
        tm3h: [{ required: true, message: '请选择3小时最大雨量发生时间', trigger: 'change' }],
        maxdrp6h: [{ required: true, message: '请输入6小时最大雨量', trigger: 'blur' }],
        tm6h: [{ required: true, message: '请选择6小时最大雨量发生时间', trigger: 'change' }],
        maxdrp12h: [{ required: true, message: '请输入12小时最大雨量', trigger: 'blur' }],
        tm12h: [{ required: true, message: '请选择12小时最大雨量发生时间', trigger: 'change' }],
        maxdrp24h: [{ required: true, message: '请输入24小时最大雨量', trigger: 'blur' }],
        tm24h: [{ required: true, message: '请选择24小时最大雨量发生时间', trigger: 'change' }]
      },
      searchForm: {
        stcd: ''
      },
      stcdList: [],
      pageSize: 10,
      currentPage: 1
    }
  },
  computed: {
    pagedData() {
      const start = (this.currentPage - 1) * this.pageSize
      return this.tableData.slice(start, start + this.pageSize)
    }
  },
  mounted() {
    this.fetchStcdList()
    this.handleSearch()
  },
  methods: {
    fetchStcdList() {
      // 假设接口返回所有极值监测站代码
      axios.get('/st-pextremum-b').then(res => {
        const all = res.data
        this.stcdList = [...new Set(all.map(i => i.stcd))]
      })
    },
    handleSearch() {
      let url = '/st-pextremum-b'
      if (this.searchForm.stcd) {
        url = `/st-pextremum-b?stcd=${encodeURIComponent(this.searchForm.stcd)}`
      }
      axios.get(url).then(res => {
        // 后端返回数组
        this.tableData = Array.isArray(res.data) ? res.data : []
        this.currentPage = 1
      })
    },
    formatTime(timeArr) {
      if (!timeArr || !Array.isArray(timeArr)) return ''
      const [y, m, d, h, min] = timeArr
      if (!y) return ''
      return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')} ${String(h).padStart(2, '0')}:${String(min).padStart(2, '0')}`
    },
    editRow(row) {
      this.dialogTitle = '编辑'
      // 时间字段转字符串，el-date-picker 才能正常显示
      const toStr = arr => Array.isArray(arr) && arr.length >= 5
        ? `${arr[0]}-${String(arr[1]).padStart(2, '0')}-${String(arr[2]).padStart(2, '0')} ${String(arr[3]).padStart(2, '0')}:${String(arr[4]).padStart(2, '0')}:${arr[5] !== undefined ? String(arr[5]).padStart(2, '0') : '00'}`
        : arr || ''
      this.form = {
        ...row,
        tm1h: toStr(row.tm1h),
        tm3h: toStr(row.tm3h),
        tm6h: toStr(row.tm6h),
        tm12h: toStr(row.tm12h),
        tm24h: toStr(row.tm24h)
      }
      this.showAddDialog = true
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        let data = { ...this.form }
        // 时间字段格式化为 yyyy-MM-ddTHH:mm:ss
        function toLocalDateTimeStr(val) {
          if (!val) return ''
          if (val.includes('T')) return val
          return val.replace(' ', 'T')
        }
        data.tm1h = toLocalDateTimeStr(data.tm1h)
        data.tm3h = toLocalDateTimeStr(data.tm3h)
        data.tm6h = toLocalDateTimeStr(data.tm6h)
        data.tm12h = toLocalDateTimeStr(data.tm12h)
        data.tm24h = toLocalDateTimeStr(data.tm24h)
        data.maxdrp1h = data.maxdrp1h !== '' ? Number(data.maxdrp1h) : null
        data.maxdrp3h = data.maxdrp3h !== '' ? Number(data.maxdrp3h) : null
        data.maxdrp6h = data.maxdrp6h !== '' ? Number(data.maxdrp6h) : null
        data.maxdrp12h = data.maxdrp12h !== '' ? Number(data.maxdrp12h) : null
        data.maxdrp24h = data.maxdrp24h !== '' ? Number(data.maxdrp24h) : null
        if (this.dialogTitle === '编辑') {
          axios.put(`/st-pextremum-b/${data.stcd}`, data)
            .then(() => {
              this.$message.success({ message: '更新成功', duration: 3000 })
              this.showAddDialog = false
              this.handleSearch()
            })
            .catch(err => {
              this.$message.error('更新失败: ' + ((err.response && err.response.data && err.response.data.message) ? err.response.data.message : err.message))
            })
        } else {
          axios.post('/st-pextremum-b', data)
            .then(() => {
              this.$message.success({ message: '新增成功', duration: 3000 })
              this.showAddDialog = false
              this.handleSearch()
            })
            .catch(err => {
              this.$message.error('新增失败: ' + ((err.response && err.response.data && err.response.data.message) ? err.response.data.message : err.message))
            })
        }
      })
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
#div1 { height: 100%; width: 100%; background: #f4f6fa; }
.left-panel { background: #f5f7fa; height: 100vh; border-right: 1px solid #e4e7ed; padding: 30px 10px 0 10px; }
.right-panel { padding: 30px 30px 0 30px; height: 100vh; overflow: auto; background: #f4f6fa; }
.search-card { margin-bottom: 20px; }
.search-title { font-size: 18px; font-weight: bold; color: #409EFF; }
.table-card { margin-bottom: 20px; }
.table-title { font-size: 16px; font-weight: bold; color: #222; }
.el-table th { background: #eaf3fc !important; color: #222; }
.el-table .el-table__row:hover td { background: #f0faff !important; }
.stpextremumb-page {
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
.table-bar {
  background: none;
}
.table-card {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
  margin-bottom: 20px;
}
.table-title {
  font-size: 16px;
  font-weight: bold;
  color: #222;
}
.el-table th { background: #eaf3fc !important; color: #222; }
.el-table .el-table__row:hover td { background: #f0faff !important; }
</style>