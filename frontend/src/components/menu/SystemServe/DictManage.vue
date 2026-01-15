<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/prewarninginformation' }">字典管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header1">
      <div style="align-self: center; font-size: 14px; margin-left: 20px;">
        <el-input v-model="name" clearable @clear="selectDictMenu" placeholder="请输入数据项名称"
          class="input-with-select"><el-button slot="append" icon="el-icon-search"
            @click="selectDictMenu"></el-button></el-input>
      </div>

      <el-button type="primary" round icon="el-icon-plus"
        style="margin-left: auto; align-self: center; margin-right: 20px;"
        @click="addDialogVisible = true">新增</el-button>
    </div>
    <div id="div-main">
      <el-table :data="DIList" border stripe style="width: 100%" height="100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }"
        :tree-props="{ children: 'dictDetails', hasChildren: row => row.dictDetails && row.dictDetails.length > 0 }"
        :row-key="getRowKey"> <!-- 使用自定义的row-key方法 -->

        <!-- 序号列，仅计算一级目录 -->
        <el-table-column prop="number" label="序号" width="80px" align="center">
          <!-- <template slot-scope="scope">
            <span v-if="scope.row.dictDetails && scope.row.dictDetails.length > 0">{{ getIndex(index) }}</span>
          </template> -->
        </el-table-column>
        <el-table-column prop="name" label="数据项名称" align="center">
        </el-table-column>
        <el-table-column prop="description" label="描述" align="center">
        </el-table-column>
        <el-table-column prop="label" label="标签" align="center">
        </el-table-column>
        <el-table-column prop="value" label="值" align="center">
        </el-table-column>
        <el-table-column prop="dictSort" label="排序" align="center">
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="270" align="center">
          <template slot-scope="scope">
            <!-- 根据是否有 dictDetails 来判断是字典还是字典详情 -->
            <el-button v-if="scope.row.dictDetails" size="mini" @click="showEditDialog(scope.row.id)">
              编辑
            </el-button>
            <el-button v-else size="mini" @click="showEditDialogDetail(scope.row.id)">
              编辑
            </el-button>
            <el-button v-if="scope.row.dictDetails" type="primary" size="mini"
              @click="showAddDialogDetail(scope.row.id)">
              新增
            </el-button>
            <el-button v-if="scope.row.dictDetails" size="mini" type="danger" @click="removeById(scope.row.id)">
              删除
            </el-button>
            <el-button v-else size="mini" type="danger" @click="removeByIdDetail(scope.row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div id="div-footer">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage" :page-sizes="[5, 10, 20, 50]" :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增字典 -->
    <el-dialog append-to-body title="新增字典" :visible.sync="addDialogVisible" width="500px" @close="addDialogClosed"
      center :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="90px" size="small" center>
        <el-form-item label="字典名称:" prop="name">
          <el-input v-model="addForm.name" style="width:350px;"></el-input>
        </el-form-item>
        <el-form-item label="描述:" prop="description">
          <el-input v-model="addForm.description" style="width:350px;"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addDI">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑字典 -->
    <el-dialog append-to-body title="编辑字典" :visible.sync="editDialogVisible" width="500px" @close="editDialogClosed"
      center :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="90px" size="small" center>
        <el-form-item label="字典名称:" prop="name">
          <el-input v-model="editForm.name" style="width:350px;"></el-input>
        </el-form-item>
        <el-form-item label="描述:" prop="description">
          <el-input v-model="editForm.description" style="width:350px;"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="editDI">确 定</el-button>
        <el-button @click="editDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 新增字典详情 -->
    <el-dialog append-to-body title="新增字典详情" :visible.sync="addDialogVisibleDetail" width="500px"
      @close="addDialogClosedDetail" center :close-on-click-modal="false">
      <el-form :model="addFormDetail" :rules="addFormRulesDetail" ref="addFormRefDetail" label-width="90px" size="small"
        center>
        <el-form-item label="字典标签" prop="label">
          <el-input v-model="addFormDetail.label" style="width: 350px;" />
        </el-form-item>
        <el-form-item label="字典值" prop="value">
          <el-input v-model="addFormDetail.value" style="width: 350px;" />
        </el-form-item>
        <el-form-item label="排序" prop="dictSort">
          <el-input-number v-model.number="addFormDetail.dictSort" :min="0" :max="999" controls-position="right"
            style="width: 370px;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addDIDetail">确认</el-button>
        <el-button @click="addDialogVisibleDetail = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 编辑字典详情 -->
    <el-dialog append-to-body title="编辑字典详情" :visible.sync="editDialogVisibleDetail" width="500px"
      @close="editDialogClosedDetail" center :close-on-click-modal="false">
      <el-form :model="editFormDetail" :rules="editFormRulesDetail" ref="editFormRefDetail" label-width="90px"
        size="small" center>
        <el-form-item label="字典标签" prop="label">
          <el-input v-model="editFormDetail.label" style="width: 350px;" />
        </el-form-item>
        <el-form-item label="字典值" prop="value">
          <el-input v-model="editFormDetail.value" style="width: 350px;" />
        </el-form-item>
        <el-form-item label="排序" prop="dictSort">
          <el-input-number v-model.number="editFormDetail.dictSort" :min="0" :max="999" controls-position="right"
            style="width: 370px;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="editDIDetail">确认</el-button>
        <el-button @click="editDialogVisibleDetail = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import FileSaver from 'file-saver'
import XLSX from 'xlsx'
import { baseURL } from '@/main.js'


export default {
  data() {
    return {
      name: '',
      queryinfo: {
        currentPage: 1,
        pageSize: 5
      },
      DIList: [],
      ExampleDI: [
        {
          name: "字典名称",
          description: '描述',
        }
      ],
      total: 0,
      addDialogVisible: false,
      editDialogVisible: false,
      importDialogVisible: false,
      addDialogVisibleDetail: false,
      editDialogVisibleDetail: false,
      fileList: [],
      addForm: {
        name: '',
        description: ''
      },
      addFormRules: {
        name: [
          { required: true, message: '请输入字典名称', trigger: 'blur' }
        ],
        description: [
          { required: false, message: '请输入描述', trigger: 'blur' }
        ]
      },
      editForm: {},
      editFormRules: {
        name: [
          { required: true, message: '请输入字典名称', trigger: 'blur' }
        ],
        description: [
          { required: false, message: '请输入描述', trigger: 'blur' }
        ]
      },
      addFormDetail: {},
      addFormRulesDetail: {
        label: [
          { required: true, message: '请输入字典名称', trigger: 'blur' }
        ],
        value: [
          { required: true, message: '请输入字典值', trigger: 'blur' }
        ],
        dictSort: [
          { required: true, message: '请输入排序', trigger: 'blur', type: 'number' }
        ]
      },
      editFormDetail: {},
      editFormRulesDetail: {
        label: [
          { required: true, message: '请输入字典名称', trigger: 'blur' }
        ],
        value: [
          { required: true, message: '请输入字典值', trigger: 'blur' }
        ],
        dictSort: [
          { required: true, message: '请输入排序', trigger: 'blur', type: 'number' }
        ]
      },
      message: '',
    }
  },
  methods: {
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get(
        '/dict/info/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    async showEditDialogDetail(id) {
      const { data: res } = await this.$http.get(
        '/dict-detail/info/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editFormDetail = res.data
      this.editDialogVisibleDetail = true
    },
    // 将字典id传给中间变量，并且打开新增字典详情的窗口
    showAddDialogDetail(id) {
      this.addFormDetail.dictId = id
      this.addDialogVisibleDetail = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.selectDictMenu()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.selectDictMenu()
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
    //监听新增对话框关闭重置事件
    addDialogClosedDetail() {
      this.$refs.addFormRefDetail.resetFields()
    },
    //监听编辑对话框关闭重置事件
    editDialogClosedDetail() {
      this.$refs.editFormRefDetail.resetFields()
    },
    //新增字典信息
    addDI() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/dict/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.message = res.message;
          this.$message.error(this.message);
        } else {
          this.$message.success('添加成功')
        }
        this.addDialogVisible = false
        this.selectDictMenu()
      })
    },
    //编辑字典信息
    editDI() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          '/dict/update',
          this.editForm
        )
        if (res.code !== 200) {
          this.$message.error('编辑失败');
        } else {
          this.$message.success('编辑成功');
        }
        this.editDialogVisible = false;
        this.selectDictMenu();
      })
    },
    //新增字典详情信息
    addDIDetail(id) {
      this.$refs.addFormRefDetail.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/dict-detail/save',
          this.addFormDetail
        )
        if (res.code !== 200) {
          this.message = res.message;
          this.$message.error(this.message);
        } else {
          this.$message.success('添加成功')
        }
        this.addDialogVisibleDetail = false
        this.selectDictMenu()
      })
    },
    //编辑字典详情信息
    editDIDetail() {
      this.$refs.editFormRefDetail.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/dict-detail/update',
          this.editFormDetail
        )
        if (res.code !== 200) {
          this.$message.error('更新失败');
        } else {
          this.$message.success('更新成功')
        }
        this.editDialogVisibleDetail = false
        this.selectDictMenu()
      })
    },
    //删除字典信息
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
        '/dict/delete/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.selectDictMenu()
    },
    //删除字典详情信息
    async removeByIdDetail(id) {
      const confirmResult = await this.$confirm(
        '此操作将永久删除该数据, 是否继续?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).catch(err => err)
      if (confirmResult !== 'confirm') return
      const { data: res } = await this.$http.post(
        '/dict-detail/delete/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.selectDictMenu()
    },
    //查询所有菜单
    async selectDictMenu() {
      const params = {
        blurry: this.name || '',
        currentPage: this.queryinfo.currentPage || 0,
        pageSize: this.queryinfo.pageSize || 10,
      }
      const { data: res } = await this.$http.get(
        '/dict/list',
        {
          params: params
        }
      );
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.DIList = res.data.content;
      this.total = res.data.totalElements;
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
          '泵站数据列表报告.xlsx'
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
          '字典信息数据列表模板.xlsx'
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
        this.selectDictMenu()
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
      let url = `${baseURL}/dict/import-excel`
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
    },
    // 获取一级目录序号（忽略子节点）
    getIndex(index) {
      return index + 1;
    },
    // 根据行数据生成唯一的row-key，避免父子级id冲突
    getRowKey(row) {
      if (row.dictDetails) {
        return `parent-${row.id}`;
      } else {
        return `child-${row.id}`;
      }
    },
    // 切换树形结构展开/折叠
    toggleTree(row) {
      this.$refs.table.toggleRowExpansion(row);
      this.$set(row, '_expanded', !row._expanded); // 控制展开状态
    }
  },
  created() {
    this.selectDictMenu()
  },
  mounted() { }
}
</script>

<!-- 代码格式 -->
<style lang="less" scoped>
#div1 {
  height: 100%;
  width: 100%;
}

#bread {
  height: 25px;
  width: 100%;
}

#div-header1 {
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

.block {
  align-self: center;
  margin-left: 10px;
  font-size: 14px;
}

.upload-demo {
  margin: 10px 200px;
}
</style>