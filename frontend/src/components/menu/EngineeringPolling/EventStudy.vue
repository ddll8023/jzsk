<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb
        separator-class="el-icon-arrow-right"
        style="padding-top:5px; padding-left:10px"
      >
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>工程巡检</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/eventstudy' }"
          >事件中心</el-breadcrumb-item
        >
      </el-breadcrumb>
    </div>

    <!-- <div id="div-header">
      <div class="div-header1">
        <div class="div-header1-top">类型一</div>
        <div class="div-header1-main">
          <el-descriptions class="margin-top" :column="1" size="medium" border>
            <el-descriptions-item
              label="次数(7天)"
              label-style="width:30%;"
              content-style="width:70%;"
            >
              5
            </el-descriptions-item>
            <el-descriptions-item
              label="次数(30天)"
              label-style="width:30%;"
              content-style="width:70%;"
            >
              10
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      <div class="div-header1">
        <div class="div-header1-top">类型二</div>
        <div class="div-header1-main">
          <el-descriptions class="margin-top" :column="1" size="medium" border>
            <el-descriptions-item
              label="次数(7天)"
              label-style="width:30%;"
              content-style="width:70%;"
            >
              5
            </el-descriptions-item>
            <el-descriptions-item
              label="次数(30天)"
              label-style="width:30%;"
              content-style="width:70%;"
            >
              10
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      <div class="div-header1">
        <div class="div-header1-top">类型三</div>
        <div class="div-header1-main">
          <el-descriptions class="margin-top" :column="1" size="medium" border>
            <el-descriptions-item
              label="次数(7天)"
              label-style="width:30%;"
              content-style="width:70%;"
            >
              5
            </el-descriptions-item>
            <el-descriptions-item
              label="次数(30天)"
              label-style="width:30%;"
              content-style="width:70%;"
            >
              10
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      <div class="div-header1">
        <div class="div-header1-top">类型四</div>
        <div class="div-header1-main">
          <el-descriptions class="margin-top" :column="1" size="medium" border>
            <el-descriptions-item
              label="次数(7天)"
              label-style="width:30%;"
              content-style="width:70%;"
            >
              5
            </el-descriptions-item>
            <el-descriptions-item
              label="次数(30天)"
              label-style="width:30%;"
              content-style="width:70%;"
            >
              10
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </div> -->

    <div id="div-header2">
        <span style="align-self:center; margin-left:20px;font-size:15px">上报时间：</span>
        <el-date-picker
          v-model="dataselect"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          align="right"
          style="align-self:center;"
          @change="getESListDateEvent"
          @clear="getESListDateEvent"
        >
        </el-date-picker>


      <span style="align-self:center; margin-left:20px;font-size:15px"
        >事件状态：</span
      >
      <el-select
        v-model="eventselect"
        clearable
        placeholder="请选择"
        style="align-self:center;"
        @change="getESListDateEvent"
        @clear="getESListDateEvent"
      >
        <el-option
          v-for="item in eventStatus"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>

      <el-button
        type="primary"
        round
        icon="el-icon-plus"
        style="margin-left:auto; align-self: center; "
        @click="addDialogVisible = true"
        >新增</el-button
      >

      <el-button
        type="primary"
        round
        icon="iconfont icon-icon-test"
        style="align-self:center"
        @click="importDialogVisible = true"
        >导入</el-button
      >

      <download-excel
        class="export-excel-wrapper"
        :data="ESList"
        :fields="json_fields"
        name="事件中心数据.xls"
        style="align-self:center; margin-right:10px;margin-left:10px"
      >
        <!-- 上面可以自定义自己的样式，还可以引用其他组件button -->
        <el-button
          type="primary"
          round
          icon="iconfont icon-icon-test"
          style="align-self:center; margin-right:10px"
          >导出</el-button
        >
      </download-excel>

    </div>

    <div id="div-main">
      <el-table
        :data="ESList"
        border
        stripe
        style="width: 100%"
        height="100%"
        v-loading="loading"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }"
      >
        <el-table-column type="index" label="序号" width="100px" align="center">
        </el-table-column>
        <el-table-column prop="level" label="事件级别" align="center">
        </el-table-column>
        <el-table-column prop="object" label="事件对象" align="center">
        </el-table-column>
        <el-table-column prop="situation" label="异常情况" align="center">
        </el-table-column>
        <el-table-column prop="time" label="上报时间" align="center">
        </el-table-column>
        <el-table-column prop="status" label="事件状态" align="center">
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditDialog(scope.row.id)"
              >编辑</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="removeById(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div id="div-footer">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog
      title="新增预警信息"
      :visible.sync="addDialogVisible"
      width="40%"
      @close="addDialogClosed"
      center
      :close-on-press-escape="false"
    >
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="addFormRef"
        label-width="100px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="事件级别:" prop="level">
              <el-input v-model="addForm.level"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件对象:" prop="object">
              <el-input v-model="addForm.object"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="异常情况:" prop="situation">
              <el-input v-model="addForm.situation"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上报时间:" prop="time">
              <!-- <el-input v-model="addForm.time"></el-input> -->
              <el-date-picker
                v-model="addForm.time"
                type="datetime"
                placeholder="选择上报时间"
                align="right"
                :picker-options="pickerOptions"
                value-format="yyyy-MM-dd HH:mm:ss"
                :style="{width:'100%'}"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="事件状态:" prop="status">
              <!-- <el-input v-model="addForm.status"></el-input> -->
              <el-select
                v-model="addForm.status"
                clearable
                placeholder="请选择"
                style="align-self:center;"
              >
                <el-option
                  v-for="item in eventStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addES">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog
      title="编辑角色信息"
      :visible.sync="editDialogVisible"
      width="50%"
      @close="editDialogClosed"
      center
      :close-on-press-escape="false"
    >
      <el-form
        :model="editForm"
        :rules="editFormRules"
        ref="editFormRef"
        label-width="150px"
        class="demo-ruleForm"
        size="small "
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="事件级别:" prop="level">
              <el-input v-model="editForm.level"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件对象:" prop="object">
              <el-input v-model="editForm.object"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="异常情况:" prop="situation">
              <el-input v-model="editForm.situation"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上报时间:" prop="time">
              <el-input disabled v-model="editForm.time"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="事件状态:" prop="status">
              <!-- <el-input v-model="editForm.status"></el-input> -->
              <el-select
                v-model="editForm.status"
                clearable
                placeholder="请选择"
                style="align-self:center;"
              >
                <el-option
                  v-for="item in eventStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editES">确 定</el-button>
        <el-button @click="editDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 导入框 -->
    <el-dialog
      :visible.sync="importDialogVisible"
      width="800px"
      :modal="false"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
    >
      <template slot="title">
        <div
          style="border-bottom: 1px solid #ebebeb;font-size:20px;padding-bottom:10px"
        >
          导入Excel
        </div>
      </template>
      <el-upload
        class="upload-demo"
        drag
        ref="upload"
        accept=".xlsx,.xls"
        name="file"
        action="/events/import-excel"
        :file-list="fileList"
        :auto-upload="false"
        :on-change="fileChange"
        :on-remove="handleRemove"
        :before-upload="beforeUpload"
        :http-request="httpRequest"
        multiple
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          只能上传xls/xlsx文件
        </div>
      </el-upload>
      <span slot="footer">
        <el-button
          type="info"
          icon="el-icon-download"
          style="margin-right:450px"
          @click="exportFormwork"
          >下载模板</el-button
        >
        <el-button type="success" @click="submitUpload">上 传</el-button>
        <el-button @click="importDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

     <!-- 导出数据模板 -->
    <el-table
      id="tableFormwork"
      :data="ExamplePI"
      border
      stripe
      height="100%"
      style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }"
    >
      <el-table-column
        type="index"
        label="序号"
        width="80px"
        align="center"
        :index="table_index"
      >
      </el-table-column>
      
      <el-table-column prop="level" label="事件级别" align="center">
      </el-table-column>
      <el-table-column prop="object" label="事件对象" align="center">
      </el-table-column>
      <el-table-column prop="situation" label="异常情况" align="center">
      </el-table-column>
      <el-table-column prop="time" label="上报时间" align="center">
      </el-table-column>
      <el-table-column prop="status" label="事件状态" align="center">
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
      dataselect:['',''],
      eventselect:'',
      // 事件状态
      eventStatus:[
        {
          label:"待解决",
          value:"待解决"
        },
        {
          label:"已解决",
          value:"已解决"
        }
      ],
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      //数据列表
      ESList: [],
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
          level:"事件级别",
          object:"事件对象",
          situation:"异常情况",
          time:"上报时间【2022-03-17 18:19:07】",
          status:"事件状态"
     
        }
      ],
      //添加表单数据
      addForm: {
        level: '',
        object: '',
        situation: '',
        time: '',
        status: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        level: [{ required: true, message: '请输入事件级别', trigger: 'blur' }],
        object: [
          { required: true, message: '请输入事件对象', trigger: 'blur' }
        ],
        situation: [
          { required: true, message: '请输入异常情况', trigger: 'blur' }
        ],
        time: [{ required: true, message: '请输入上报时间', trigger: 'blur' }],
        status: [{ required: true, message: '请输入事件状态', trigger: 'blur' }]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        level: [{ required: true, message: '请输入事件级别', trigger: 'blur' }],
        object: [
          { required: true, message: '请输入事件对象', trigger: 'blur' }
        ],
        situation: [
          { required: true, message: '请输入异常情况', trigger: 'blur' }
        ],
        time: [{ required: true, message: '请输入上报时间', trigger: 'blur' }],
        status: [{ required: true, message: '请输入事件状态', trigger: 'blur' }]
      }
    }
  },
  methods: {
    //获取数据列表
    async getESList() {
      const { data: res } = await this.$http.get('/events/list', {
        params: this.queryinfo
      })
      console.log(res)
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.ESList = res.data.records
      this.total = res.data.total
    },
    //获取数据列表,时间和事件状态
    async getESListDateEvent(){
      if(this.dataselect===null){
        this.dataselect=['','']
      }
      if(this.eventselect==null){
        this.eventselect=''
      }
       const { data: res } = await this.$http.get('/events/list', {
        params:{
          currentPage:this.queryinfo.currentPage,
          pageSize:this.queryinfo.pageSize,
          startTime:this.dataselect[0],
          endTime:this.dataselect[1],
          eventstatus:this.eventselect
        }
      })
      console.log(res)
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.ESList = res.data.records
      this.total = res.data.total
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get('/events/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getESListDateEvent()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getESListDateEvent()
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
    addES() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/events/save',
          this.addForm
        )
        console.log(res)
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getESListDateEvent()
      })
    },
    //编辑角色信息表单
    editES() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/events/update', {
          id: this.editForm.id,
          level: this.editForm.level,
          object: this.editForm.object,
          situation: this.editForm.situation,
          time: this.editForm.time,
          status: this.editForm.status
        })
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getESListDateEvent()
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
      const { data: res } = await this.$http.post('/events/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getESListDateEvent()
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
          '事件中心数据列表模板.xlsx'
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
        this.getESListDateEvent()
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
      let url = '/events/import-excel'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const { data: res } = await this.$http.post(url, formData, config)
      if (res.code === 200) {
        this.$message.success('导入成功')
      }else{
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
    this.getESListDateEvent()
  },
  mounted() {}
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
#div-header {
  height:15%;
  width: 100%;
  margin-top: 0px;
  display: flex;
  background-color: rgb(253, 242, 228);
}
.div-header1 {
  height: 81%;
  width: 23%;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  border-radius: 10px 10px 0px 0px;
  margin: 10px 25px;
}
.div-header1-top {
  height: 20%;
  width: 100%;
  background-color: rgb(0, 183, 255);
  border-radius: 10px 10px 0px 0px;
  text-align: center;
}
.div-header1-main {
  height: 80%;
  width: 100%;
}
#div-header2 {
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
