<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>管理信息服务</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/manageorganizationread' }">管理人员信息</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header">
      <div style="align-self:center;margin-left:10px">
        <el-input placeholder="请输入管理人员姓名搜索" v-model="inputWord" class="input-with-select" clearable @clear="getMPList">
          <el-button slot="append" icon="el-icon-search" @click="getMPList"></el-button>
        </el-input>
      </div>

      <download-excel class="export-excel-wrapper" :data="MPList" :fields="json_fields" name="管理人员信息.xls"
        style="align-self:center; margin-right:10px;margin-left:auto;">
        <!-- 上面可以自定义自己的样式，还可以引用其他组件button -->
        <el-button type="primary" round icon="iconfont icon-icon-test"
          style="margin-left: auto; align-self:center; margin-right:10px">导出</el-button>
      </download-excel>

    </div>

    <div id="div-main">
      <el-table :data="MPList" border stripe style="width: 100%" height="100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="name" label="姓名" align="center">
        </el-table-column>
        <el-table-column prop="age" label="年龄" align="center">
        </el-table-column>
        <el-table-column prop="gender" label="性别" align="center">
        </el-table-column>
        <el-table-column prop="phone" label="电话" align="center">
        </el-table-column>
        <el-table-column prop="organization" label="所属机构" align="center">
        </el-table-column>
        <el-table-column prop="position" label="职位" align="center">
        </el-table-column>
        <el-table-column prop="duty" label="职责" align="center">
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
        :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增人员" :visible.sync="addDialogVisible" width="40%" @close="addDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名:" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄:" prop="age">
              <el-input v-model="addForm.age"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别:" prop="gender">
              <!-- <el-input v-model="addForm.gender"></el-input> -->
              <el-select v-model="addForm.gender" clearable placeholder="请选择" style="align-self:center;">
                <el-option label="男" value="男">男</el-option>
                <el-option label="女" value="女">女</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话:" prop="phone">
              <el-input v-model="addForm.phone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属机构:" prop="organization">
              <el-input v-model="addForm.organization"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位:" prop="position">
              <el-input v-model="addForm.position"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职责:" prop="duty">
              <el-input v-model="addForm.duty"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addMP">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑人员信息" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名:" prop="name">
              <el-input v-model="editForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄:" prop="age">
              <el-input v-model="editForm.age"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别:" prop="gender">
              <!-- <el-input v-model="editForm.gender"></el-input> -->
              <el-select v-model="editForm.gender" clearable placeholder="请选择" style="align-self:center;">
                <el-option label="男" value="男">男</el-option>
                <el-option label="女" value="女">女</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话:" prop="phone">
              <el-input v-model="editForm.phone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属机构:" prop="organization">
              <el-input v-model="editForm.organization"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位:" prop="position">
              <el-input v-model="editForm.position"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职责:" prop="duty">
              <el-input v-model="editForm.duty"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editMP">确 定</el-button>
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
      <el-upload class="upload-demo" drag ref="upload" accept=".xlsx,.xls" name="file" action="/person/import-excel"
        :file-list="fileList" :auto-upload="false" :on-change="fileChange" :on-remove="handleRemove"
        :before-upload="beforeUpload" :http-request="httpRequest" multiple>
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

    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExamplePI" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="name" label="姓名" align="center">
      </el-table-column>
      <el-table-column prop="age" label="年龄" align="center">
      </el-table-column>
      <el-table-column prop="gender" label="性别" align="center">
      </el-table-column>
      <el-table-column prop="phone" label="电话" align="center">
      </el-table-column>
      <el-table-column prop="organization" label="所属机构" align="center">
      </el-table-column>
      <el-table-column prop="position" label="职位" align="center">
      </el-table-column>
      <el-table-column prop="duty" label="职责" align="center">
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
    return {
      //自定义导出字段
      json_fields: {
        "姓名": "name",
        "年龄": "age",
        "性别": "gender",
        "电话": "phone",
        "所属机构": "organization",
        "职位": "position",
        "职责": "duty",
      },
      json_meta: [
        [
          {
            " key ": " charset ",
            " value ": " utf- 8 "
          }
        ]
      ],
      inputWord: '',
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      //数据列表
      MPList: [],
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
          name: "姓名",
          age: "30",
          gender: "女",
          phone: "13945611234",
          organization: "所属机构",
          position: "职位",
          duty: "职责"

        }
      ],
      //添加表单数据
      addForm: {
        name: '',
        age: '',
        gender: '',
        phone: '',
        organization: '',
        position: '',
        duty: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        age: [{ required: true, message: '请输入年龄', trigger: 'blur' }, {
          pattern: /^(?:[1-9][0-9]?|1[01][0-9]|120)$/, message: '请输入正确的年龄', trigger: 'blur'
        }],
        gender: [{ required: true, message: '请输入性别', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入电话', trigger: 'blur' }, {
          pattern: /^1[3-9]\d{9}$/, message: '请输入正确的电话号码', trigger: 'blur'
        }],
        organization: [{ required: true, message: '请输入所属机构', trigger: 'blur' }],
        position: [{ required: true, message: '请输入职位', trigger: 'blur' }],
        duty: [{ required: true, message: '请输入职责', trigger: 'blur' }],
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        age: [{ required: true, message: '请输入年龄', trigger: 'blur' }, {
          pattern: /^(?:[1-9][0-9]?|1[01][0-9]|120)$/, message: '请输入正确的年龄', trigger: 'blur'
        }],
        gender: [{ required: true, message: '请输入性别', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入电话', trigger: 'blur' }, {
          pattern: /^1[3-9]\d{9}$/, message: '请输入正确的电话号码', trigger: 'blur'
        }],
        organization: [{ required: true, message: '请输入所属机构', trigger: 'blur' }],
        position: [{ required: true, message: '请输入职位', trigger: 'blur' }],
        duty: [{ required: true, message: '请输入职责', trigger: 'blur' }],
      }
    }
  },
  methods: {
    //获取数据列表
    async getMPList() {
      if (this.inputWord == null) {
        this.inputWord = ''
      }
      const { data: res } = await this.$http.get('/person/list', {
        params: {
          currentPage: this.queryinfo.currentPage,
          pageSize: this.queryinfo.pageSize,
          name: this.inputWord
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.MPList = res.data.records
      this.total = res.data.total
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get('/person/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getMPList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getMPList()
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
    addMP() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/person/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getMPList()
      })
    },
    //编辑角色信息表单
    editMP() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/person/update', {
          id: this.editForm.id,
          name: this.editForm.name,
          age: this.editForm.age,
          gender: this.editForm.gender,
          phone: this.editForm.phone,
          organization: this.editForm.organization,
          position: this.editForm.position,
          duty: this.editForm.duty
        })
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getMPList()
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
      const { data: res } = await this.$http.post('/person/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getMPList()
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
          '管理人员信息模板.xlsx'
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
        this.getMPList()
      }, 1000)

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
      let url = '/person/import-excel'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const { data: res } = await this.$http.post(url, formData, config)
      if (res.code === 200) {
        this.$message.success('导入成功')
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
    this.getMPList()
  },
  mounted() { }
}
</script>

<style lang="less" scoped>
#div1 {
  height: 100%;
  width: 100%;
}

#bread {
  height: 25px;
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

.upload-demo {
  margin: 10px 200px;
}
</style>
