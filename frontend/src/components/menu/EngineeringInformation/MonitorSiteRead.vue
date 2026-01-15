<template>
  <div id="div1">
    <div id="div-header">
      <span style="align-self:center; margin-left:20px; font-size:14px;">监测站名称：</span>
      <el-select v-model="name" clearable placeholder="请选择" style="align-self:center;"
        @change="getMSList" @clear="getMSList">
        <el-option v-for="item in nameList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <el-button type="primary" round icon="iconfont icon-icon-test"
        @click="exportExcel" style="margin-left:auto; align-self:center; margin-right:10px">导出</el-button>
    </div>

    <div id="div-main">
      <el-table :data="MSList" border stripe height="100%" style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="code" label="站码"  align="center">
        </el-table-column>
        <el-table-column prop="name" label="站名"  align="center">
        </el-table-column>
        <el-table-column prop="waterName" label="水系名称"  align="center">
        </el-table-column>
        <el-table-column prop="riverName" label="河流名称"  align="center">
        </el-table-column>
        <el-table-column prop="monitorCode" label="施测项目码"  align="center">
        </el-table-column>
        <el-table-column prop="addressCode" label="行政区划码"  align="center">
        </el-table-column>
        <el-table-column prop="establishDate" label="设站年月"  align="center">
        </el-table-column>
        <el-table-column prop="longitude" label="经度"  align="center">
        </el-table-column>
        <el-table-column prop="latitude" label="纬度"  align="center">
        </el-table-column>
        <el-table-column prop="note" label="备注"  align="center">
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
    <el-dialog title="新增监测站点" :visible.sync="addDialogVisible" width="900px" @close="addDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="站码:" prop="code">
              <el-input v-model="addForm.code"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="站名:" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水系名称:" prop="waterName">
              <el-input v-model="addForm.waterName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="河流名称:" prop="riverName">
              <el-input v-model="addForm.riverName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="施测项目码:" prop="monitorCode">
              <el-input v-model="addForm.monitorCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行政区划码:" prop="addressCode">
              <el-input v-model="addForm.addressCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="经度:" prop="longitude">
              <el-input v-model="addForm.longitude"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度:" prop="latitude">
              <el-input v-model="addForm.latitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设站年月:" prop="establishDate">
              <el-date-picker v-model="addForm.establishDate" type="month" placeholder="选择月" value-format="yyyy-MM">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注:" prop="note">
              <el-input type="textarea" v-model="addForm.note"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addMS">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑监测站点" :visible.sync="editDialogVisible" width="900px" @close="editDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="站码:" prop="code">
              <el-input v-model="editForm.code"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="站名:" prop="name">
              <el-input v-model="editForm.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水系名称:" prop="waterName">
              <el-input v-model="editForm.waterName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="河流名称:" prop="riverName">
              <el-input v-model="editForm.riverName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="施测项目码:" prop="monitorCode">
              <el-input v-model="editForm.monitorCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行政区划码:" prop="addressCode">
              <el-input v-model="editForm.addressCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="经度:" prop="longitude">
              <el-input v-model="editForm.longitude"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度:" prop="latitude">
              <el-input v-model="editForm.latitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设站年月:" prop="establishDate">
              <el-date-picker v-model="editForm.establishDate" type="month" placeholder="选择月" value-format="yyyy-MM">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注:" prop="note">
              <el-input type="textarea" v-model="editForm.note"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editMS">确 定</el-button>
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
      <el-upload class="upload-demo" drag ref="upload" accept=".xlsx,.xls" name="file" action="/measuring-station/import-excel"
        :file-list="fileList" :auto-upload="false" :on-change="fileChange" :on-remove="handleRemove"
        :before-upload="beforeUpload" :http-request="httpRequest" multiple style="margin: 10px 200px;">
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
    <el-table id="table" :data="AllMSList" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="50px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="code" label="站码" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="name" label="站名" align="center" width="140px">
      </el-table-column>
      <el-table-column prop="waterName" label="水系名称" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="riverName" label="河流名称" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="monitorCode" label="施测项目码" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="addressCode" label="行政区划码" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="establishDate" label="设站年月" align="center" width="130px">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center" width="130px">
      </el-table-column>
      <el-table-column prop="note" label="备注" align="center" width="120px">
      </el-table-column>
    </el-table>

    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExampleMS" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="50px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="code" label="站码" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="name" label="站名" align="center" width="140px">
      </el-table-column>
      <el-table-column prop="waterName" label="水系名称" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="riverName" label="河流名称" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="monitorCode" label="施测项目码" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="addressCode" label="行政区划码" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="establishDate" label="设站年月" align="center" width="130px">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center" width="130px">
      </el-table-column>
      <el-table-column prop="note" label="备注" align="center" width="120px">
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
      file: '',
      fileList: [],
      name: '',
      names: [],
      nameList: [],
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      //数据列表
      MSList: [],
      AllMSList: [],
      total: 0,
      addDialogVisible: false,
      editDialogVisible: false,
      importDialogVisible: false,
      ExampleMS: [
        {
          code: '00000001(8位阿拉伯数字)',
          name: '水库1监测站点1',
          waterName: 'AA水系',
          riverName: 'AA河流',
          monitorCode: 'Z(Z代表水位,Q代表流量,W代表水质)',
          addressCode: '123456(行政区划代码)',
          establishDate: '2024-03',
          longitude: '113.369485(-180到180)',
          latitude: '31.716004(-90到90)',
          note: '备注信息(可不填)',
        }
      ],
      //添加表单数据
      addForm: {
        code: '',
        name: '',
        waterName: '',
        riverName: '',
        monitorCode: '',
        addressCode: '',
        establishDate: '',
        longitude: '',
        latitude: '',
        note: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        code: [{ required: true, message: '请输入站码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入站名', trigger: 'blur' }],
        waterName: [{ required: false}],
        riverName: [{ required: false}],
        monitorCode: [{ required: false}],
        addressCode: [{ required: false}],
        establishDate: [{ required: false}],
        longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }],
        latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }],
        note: [{ required: false}]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        code: [{ required: true, message: '请输入站码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入站名', trigger: 'blur' }],
        waterName: [{ required: false}],
        riverName: [{ required: false}],
        monitorCode: [{ required: false}],
        addressCode: [{ required: false}],
        establishDate: [{ required: false}],
        longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }],
        latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }],
        note: [{ required: false}]
      }
    }
  },
  methods: {
    //获取数据列表
    async getMSList() {
      if (this.name === null) {
        this.name = ''
      }
      const { data: res } = await this.$http.get('/measuring-station/list', {
        params: {
          currentPage: this.queryinfo.currentPage,
          pageSize: this.queryinfo.pageSize,
          name: this.name
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.MSList = res.data.records
      this.total = res.data.total
    },
    //获取所有监测站名称
    async getAllNames() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '监测站名称'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.names = res.data
      this.nameList = this.names.map(name => ({
        value: name,
        label: name
      }))
    },
    //获取不分页数据列表
    async getAllMSList() {
      const { data: res } = await this.$http.get('/measuring-station/export-excel')
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.AllMSList = res.data
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get(
        '/measuring-station/info/' + id
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
      this.getMSList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getMSList()
    },
    //序号连续
    table_index(index) {
      return (
        (this.queryinfo.currentPage - 1) * this.queryinfo.pageSize + index + 1
      )
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
    addMS() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/measuring-station/save',
          this.addForm
        )
        console.log(res)
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getMSList()
        this.getAllMSList()
      })
    },
    //编辑角色信息表单
    editMS() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          '/measuring-station/update',
          {
            id: this.editForm.id,
            code: this.editForm.code,
            name: this.editForm.name,
            waterName: this.editForm.waterName,
            riverName: this.editForm.riverName,
            monitorCode: this.editForm.monitorCode,
            addressCode: this.editForm.addressCode,
            establishDate: this.editForm.establishDate,
            longitude: this.editForm.longitude,
            latitude: this.editForm.latitude,
            note: this.editForm.note
          }
        )
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getMSList()
        this.getAllMSList()
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
        '/measuring-station/delete/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getMSList()
      this.getAllMSList()
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
          '监测站点数据列表报告.xlsx'
        )
      } catch (e) {
        if (typeof console !== 'undefined') {
          console.log(e, wbout)
        }
      }
      return wbout
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
          '监测站点数据列表模板.xlsx'
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
      this.importDialogVisible = false
      this.$refs.upload.submit()
      setTimeout(() => {
        this.getMSList()
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
      let url = '/measuring-station/import-excel'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const { data: res } = await this.$http.post(url, formData, config)
      if (res.code === 200) {
        this.$message.success('导入成功')
        this.getAllMSList()
      }
      this.submitForm()
    },
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
    this.getMSList(),
    this.getAllMSList()
    this.getAllNames()
  },
  mounted() {
  }
}
</script>

<style lang="less" scoped>
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
</style>
