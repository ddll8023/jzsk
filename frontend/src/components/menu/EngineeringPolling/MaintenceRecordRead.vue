<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>工程巡检</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/maintencerecord' }">维护记录</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header">
      <span style="align-self:center; font-size:14px">发生时间：</span>
      <el-date-picker v-model="datepickers" type="datetimerange" range-separator="-" start-placeholder="开始日期"
        end-placeholder="结束日期" align="right" value-format="yyyy-MM-dd HH:mm:ss" style="align-self:center;"
        @change="getMRList" @clear="getMRList">
      </el-date-picker>

      <div style="align-self:center;margin-left:10px">
        <el-input placeholder="请输入工程名称搜索" v-model="inputWord" class="input-with-select" clearable @clear="getMRList">
          <el-button slot="append" icon="el-icon-search" @click="getMRList"></el-button>
        </el-input>
      </div>

      <!-- <el-button type="primary" round icon="el-icon-plus" style="margin-left:auto; align-self: center;"
        @click="addDialogVisible = true">新增</el-button>

      <el-button type="primary" round icon="iconfont icon-icon-test" style="align-self:center"
        @click="importDialogVisible = true">导入</el-button> -->

      <el-button type="primary" round icon="iconfont icon-icon-test" style="margin-left:auto; align-self:center;margin-right:10px"
        @click="exportExcel">导出</el-button>
    </div>

    <div id="div-main">
      <el-table :data="MRList" border stripe style="width: 100%" height="100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="name" label="工程名称" align="center">
        </el-table-column>
        <el-table-column prop="code" label="工程编码" align="center">
        </el-table-column>
        <el-table-column prop="note" label="备注" align="center">
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" align="center">
        </el-table-column>
        <el-table-column prop="phone" label="负责人电话" align="center">
        </el-table-column>
        <el-table-column prop="startTime" label="开始维护时间" align="center">
        </el-table-column>
        <el-table-column prop="overTime" label="结束维护时间" align="center">
        </el-table-column>
        <!-- <el-table-column fixed="right" label="操作" align="center" width="160">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditDialog(scope.row.id)">编辑</el-button>
            <el-button size="mini" type="danger" @click="removeById(scope.row.id)">删除</el-button>
          </template>
        </el-table-column> -->
      </el-table>
    </div>

    <div id="div-footer">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 1000000]" :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增维护记录" :visible.sync="addDialogVisible" width="40%" @close="addDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工程名称:" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工程编码:" prop="code">
              <el-input v-model="addForm.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注:" prop="note">
              <el-input v-model="addForm.note"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人:" prop="responsiblePerson">
              <el-input v-model="addForm.responsiblePerson"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人电话:" prop="phone">
              <el-input v-model="addForm.phone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始维护时间:" prop="startTime">
              <el-date-picker v-model="addForm.startTime" type="datetime" placeholder="选择日期"
                value-format="yyyy-MM-dd HH:mm:ss" :style="{ width: '100%' }">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束维护时间:" prop="overTime">
              <el-date-picker v-model="addForm.overTime" type="datetime" placeholder="选择日期"
                value-format="yyyy-MM-dd HH:mm:ss" :style="{ width: '100%' }">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addPR">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑维护记录" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工程名称:" prop="name">
              <el-input v-model="editForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工程编码:" prop="code">
              <el-input v-model="editForm.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注:" prop="note">
              <el-input v-model="editForm.note"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人:" prop="responsiblePerson">
              <el-input v-model="editForm.responsiblePerson"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人电话:" prop="phone">
              <el-input v-model="editForm.phone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始维护时间:" prop="startTime">
              <el-date-picker v-model="editForm.startTime" type="datetime" placeholder="选择日期"
                value-format="yyyy-MM-dd HH:mm:ss" :style="{ width: '100%' }">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束维护时间:" prop="overTime">
              <el-date-picker v-model="editForm.overTime" type="datetime" placeholder="选择日期"
                value-format="yyyy-MM-dd HH:mm:ss" :style="{ width: '100%' }">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editPR">确 定</el-button>
        <el-button @click="editDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 导入框 -->
    <el-dialog :visible.sync="importDialogVisible" width="800px" :modal="false" :close-on-press-escape="false"
      :close-on-click-modal="false">
      <template slot="title">
        <div style="border-bottom: 1px solid #ebebeb;font-size:20px;padding-bottom:10px">
          导入Excel
        </div>
      </template>
      <el-upload class="upload-demo" drag ref="upload" accept=".xlsx,.xls" name="file"
        action="/maintence-records/import-excel" :file-list="fileList" :auto-upload="false" :on-change="fileChange"
        :on-remove="handleRemove" :before-upload="beforeUpload" :http-request="httpRequest" multiple>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          只能上传xls/xlsx文件
        </div>
      </el-upload>
      <span slot="footer">
        <el-button type="info" icon="el-icon-download" style="margin-right:450px"
          @click="exportFormwork">下载模板</el-button>
        <el-button type="success" @click="submitUpload">上 传</el-button>
        <el-button @click="importDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 导出表格 -->
    <el-table id="table" :data="AllMRList" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="50px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="name" label="工程名称" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="code" label="工程编码" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="note" label="备注" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="responsiblePerson" label="负责人" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="phone" label="负责人电话" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="startTime" label="开始维护时间" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="overTime" label="结束维护时间" align="center" width="150px">
      </el-table-column>
    </el-table>
    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExamplePI" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
      </el-table-column>

      <!-- </el-table-column> -->
      <el-table-column prop="name" label="工程名称" align="center">
      </el-table-column>
      <el-table-column prop="code" label="工程代码" align="center">
      </el-table-column>
      <el-table-column prop="note" label="备注" align="center">
      </el-table-column>
      <el-table-column prop="responsiblePerson" label="负责人" align="center">
      </el-table-column>
      <el-table-column prop="phone" label="负责人电话" align="center">
      </el-table-column>
      <el-table-column prop="startTime" label="开始维护时间" align="center">
      </el-table-column>
      <el-table-column prop="overTime" label="结束维护时间" align="center">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import FileSaver from 'file-saver'
import XLSX from 'xlsx'

export default {
  name: '',
  data() {
    var checkCode = (rule, value, cb) => {
      const regvillageCode = /^\d{9}$/
      if (regvillageCode.test(value)) {
        return cb()
      }
      cb(new Error('请输入9位数工程编码'))
    }
    return {
      inputWord: '',
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      // 时间选择器
      datepickers: ['', ''],
      //数据列表
      MRList: [],
      AllMRList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //控制导入框
      importDialogVisible: false,
      //上传的文件
      fileList: [],
      //数据列表
      ExamplePI: [
        {
          name: "荆竹水资源供水工程",
          code: '111111',
          note: '对水厂，水库进行了一次大的维护',
          responsiblePerson: '张三',
          phone: "13812357895",
          startTime: "2023-02-24 10:43:17",
          overTime: "2023-03-24 10:43:17"
        }
      ],
      //添加表单数据
      addForm: {
        name: "",
        code: '',
        note: '',
        responsiblePerson: '',
        phone: "",
        startTime: "",
        overTime: ""
      },
      //添加表单的验证规则对象
      addFormRules: {
        name: [{ required: true, message: '请输入工程名称', trigger: 'blur' }],
        code: [{ required: false, message: '请输入工程编码', trigger: 'blur' }],
        responsiblePerson: [{ required: false, message: '请输入负责人', trigger: 'blur' }],
        phone: [{ required: false, message: '请输入负责人电话', trigger: 'blur' }],
        startTime: [{ required: true, message: '请输入开始维护日期', trigger: 'blur' }]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        name: [{ required: true, message: '请输入工程名称', trigger: 'blur' }],
        code: [{ required: false, message: '请输入工程编码', trigger: 'blur' }],
        responsiblePerson: [{ required: false, message: '请输入负责人', trigger: 'blur' }],
        phone: [{ required: false, message: '请输入负责人电话', trigger: 'blur' }],
        startTime: [{ required: true, message: '请输入开始维护日期', trigger: 'blur' }]
      }
    }
  },
  methods: {
    //获取数据列表
    async getMRList() {
      if (this.datepickers === null) {
        this.datepickers = ['', '']
      }
      const { data: res } = await this.$http.get('/maintence-records/list', {
        params: {
          pageSize: this.queryinfo.pageSize,
          currentPage: this.queryinfo.currentPage,
          name: this.inputWord,
          startTime: this.datepickers[0],
          overTime: this.datepickers[1],
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.MRList = res.data.records
      this.total = res.data.total
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get(
        '/maintence-records/info/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getMRList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getMRList()
    },
    //序号连续
    table_index(index) {
      return (this.queryinfo.currentPage - 1) * this.queryinfo.pageSize + index + 1
    },
    //监听新增对话框关闭重置事件
    addDialogClosed() {
      this.$refs.addFormRef.resetFields()
    },
    //监听编辑对话框关闭重置事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields()
    },
    //新增枢纽供水工程
    addPR() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/maintence-records/save',
          this.addForm,
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getMRList()
        this.getAllMRList()
      })
    },
    //编辑角色信息表单
    editPR() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          '/maintence-records/update',
          {
            id: this.editForm.id,
            name: this.editForm.name,
            code: this.editForm.code,
            note: this.editForm.note,
            responsiblePerson: this.editForm.responsiblePerson,
            phone: this.editForm.phone,
            startTime: this.editForm.startTime,
            overTime: this.editForm.overTime,
          }
        )
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getMRList()
        this.getAllMRList()
      })
    },
    //删除
    async removeById(id) {
      const confirmResult = await this.$confirm(
        '此操作将永久删除该数据, 是否继续?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).catch(err => err)
      //如果确认删除，返回值为字符串confirm,如果取消删除，则返回值为cancel
      if (confirmResult !== 'confirm') return
      const { data: res } = await this.$http.post(
        '/maintence-records/delete/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getMRList()
      this.getAllMRList()
    },
    //导出模板
    exportFormwork() {
      var xlsxParam = { raw: true }
      var wb = XLSX.utils.table_to_book(
        document.querySelector('#tableFormwork'),
        xlsxParam
      )
      var wbout = XLSX.write(wb, {
        bookType: 'xlsx',
        bookSST: true,
        type: 'array'
      })
      try {
        FileSaver.saveAs(
          new Blob([wbout], { type: 'application/octet-stream' }),
          '工程维护记录数据模板.xlsx'
        )
      } catch (e) {
        if (typeof console !== 'undefined') {
          console.log(e, wbout)
        }
      }
      return wbout
    },
    //导入Excel
    submitUpload() {
      //关闭表单
      this.importDialogVisible = false
      this.$refs.upload.submit()
      setTimeout(() => {
        this.getMRList()
      }, 1000)
    },
    //导出Excel
    exportExcel() {
      var xlsxParam = { raw: true }
      var wb = XLSX.utils.table_to_book(
        document.querySelector('#table'),
        xlsxParam
      )
      var wbout = XLSX.write(wb, {
        bookType: 'xlsx',
        bookSST: true,
        type: 'array'
      })
      try {
        FileSaver.saveAs(
          new Blob([wbout], { type: 'application/octet-stream' }),
          '工程维护纪录报告.xlsx'
        )
      } catch (e) {
        if (typeof console !== 'undefined') {
          console.log(e, wbout)
        }
      }
      return wbout
    },
    //获取不分页数据列表
    async getAllMRList() {
      const { data: res } = await this.$http.get('/maintence-records/export-excel')
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.AllMRList = res.data
    },
    fileChange(file, fileList) {
      this.fileList = fileList
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    async httpRequest(param) {
      let fileObj = param.file
      let formData = new FormData()
      formData.append('file', fileObj)
      let url = '/maintence-records/import-excel'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const { data: res } = await this.$http.post(url, formData, config)
      if (res.code === 200) {
        this.$message.success('导入成功')
        this.getAllMRList()
      } else {
        this.$message.error(res.message)
      }
    },
    //上传文件之前进行文件类型判断
    beforeUpload(file) {
      // 允许上传的文件格式列表
      let acceptList = ['xlsx', 'xls']
      // 根据文件名获取文件的后缀名
      let fileType = file.name
        .split('.')
        .pop()
        .toLowerCase()
      // 判断文件格式是否符合要求
      if (acceptList.indexOf(fileType) === -1) {
        this.$message.error('只能上传 xlsx/xls 格式的文件 !')
        return false
      }
    }
  },
  created() {
    this.getMRList(),
    this.getAllMRList()
  },
  mounted() { }
}
</script>

<style scoped>
#div1 {
  height: 100%;
  width: 100%;
}

#div-header {
  height: 60px;
  width: 100%;
  margin-top: 0px;
  display: flex;
  background-color: rgb(253, 242, 228);
}

#div-main {
  height: calc(100% - 120px);
  width: 100%;
}

#div-footer {
  height: 35px;
  width: 100%;
  float: left;
  text-align: center;
  background-color: rgb(245, 237, 230);
}

#bread {
  height: 25px;
  width: 100%;
}
.upload-demo {
  margin: 10px 200px;
}
</style>