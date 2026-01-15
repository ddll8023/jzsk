<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top: 5px; padding-left: 10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>工程信息服务</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/surfacewatersources' }">地表水源水</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header1">
      <div style="align-self:center;margin-left:10px">
        <el-input placeholder="请输入水源地名称搜索" v-model="input" class="input-with-select" clearable @clear="getSWSList">
          <el-button slot="append" icon="el-icon-search" @click="searchSWS"></el-button>
        </el-input>
      </div>

      <el-button type="primary" round icon="el-icon-plus"
        style="margin-left:auto;margin-right:10px; align-self: center;" @click="addDialogVisible = true">新增</el-button>

      <el-button type="primary" round icon="iconfont icon-icon-test" style="align-self:center;margin-right:10px"
        @click="importDialogVisible = true">导入</el-button>

      <el-button type="primary" round icon="iconfont icon-icon-test" style="align-self:center;margin-right:10px"
        @click="exportExcel">导出</el-button>
    </div>



    <div id="div-main">
      <el-table :data="SWSList" border stripe height="100%" style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="name" label="水源地名称" align="center">
        </el-table-column>
        <el-table-column prop="longitude" label="经度" align="center">
        </el-table-column>
        <el-table-column prop="latitude" label="纬度" align="center">
        </el-table-column>
        <el-table-column prop="type" label="站点类型" align="center">
        </el-table-column>
        <el-table-column prop="area" label="水面面积" align="center">
        </el-table-column>
        <el-table-column prop="waterQualityObjectives" label="水质目标" align="center">
        </el-table-column>
        <el-table-column prop="waterSupplyContinuity" label="水资源供水持续状况" align="center">
        </el-table-column>
        <el-table-column prop="object" label="供水对象" align="center">
        </el-table-column>
        <el-table-column prop="manageUnit" label="管理单位" align="center">
        </el-table-column>
        <el-table-column prop="waterSupplyProject" label="所属供水工程" align="center">
        </el-table-column>
        <el-table-column prop="whetherEmergencySource" label="是否为应急水源地" align="center">
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditDialog(scope.row.id)">编辑</el-button>
            <el-button size="mini" type="danger" @click="removeById(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div id="div-footer">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total" style="padding-left:40%">
        >
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增地表水源水" :visible.sync="addDialogVisible" width="800px" @close="addDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水源地名称:" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度:" prop="longitude">
              <el-input v-model="addForm.longitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="纬度:" prop="latitude">
              <el-input v-model="addForm.latitude"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="站点类型:" prop="type">
              <el-input v-model="addForm.type"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水面面积:" prop="area">
              <el-input v-model="addForm.area"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水质目标:" prop="waterQualityObjectives">
              <el-input v-model="addForm.waterQualityObjectives"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水资源供水持续状况:" prop="waterSupplyContinuity">
              <el-input v-model="addForm.waterSupplyContinuity"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供水对象:" prop="object">
              <el-input v-model="addForm.object"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="管理单位:" prop="manageUnit">
              <el-input v-model="addForm.manageUnit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属供水工程:" prop="waterSupplyProject">
              <el-input v-model="addForm.waterSupplyProject"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否为应急水源地:" prop="whetherEmergencySource">
              <el-select v-model="addForm.whetherEmergencySource">
                <el-option label="是" value="是"></el-option>
                <el-option label="否" value="否"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addSWS">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑地表水源水信息" :visible.sync="editDialogVisible" width="800px" @close="editDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水源地名称:" prop="name">
              <el-input v-model="editForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度:" prop="longitude">
              <el-input v-model="editForm.longitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="纬度:" prop="latitude">
              <el-input v-model="editForm.latitude"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="站点类型:" prop="type">
              <el-input v-model="editForm.type"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水面面积:" prop="area">
              <el-input v-model="editForm.area"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水质目标:" prop="waterQualityObjectives">
              <el-input v-model="editForm.waterQualityObjectives"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水资源供水持续状况:" prop="waterSupplyContinuity">
              <el-input v-model="editForm.waterSupplyContinuity"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供水对象:" prop="object">
              <el-input v-model="editForm.object"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="管理单位:" prop="manageUnit">
              <el-input v-model="editForm.manageUnit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属供水工程:" prop="waterSupplyProject">
              <el-input v-model="editForm.waterSupplyProject"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否为应急水源地:" prop="whetherEmergencySource">
              <el-select v-model="editForm.whetherEmergencySource">
                <el-option label="是" value="是"></el-option>
                <el-option label="否" value="否"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editSWS">确 定</el-button>
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
        action="/surface-water-sources/import-excel" :file-list="fileList" :auto-upload="false" :on-change="fileChange"
        :on-remove="handleRemove" :before-upload="beforeUpload" :http-request="httpRequest" multiple
        style="margin: 10px 200px;">
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
    <el-table id="table" :data="AllSWSList" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="name" label="水源地名称" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center">
      </el-table-column>
      <el-table-column prop="type" label="站点类型" align="center">
      </el-table-column>
      <el-table-column prop="area" label="水面面积" align="center">
      </el-table-column>
      <el-table-column prop="waterQualityObjectives" label="水质目标" align="center">
      </el-table-column>
      <el-table-column prop="waterSupplyContinuity" label="水资源供水持续状况" align="center">
      </el-table-column>
      <el-table-column prop="object" label="供水对象" align="center">
      </el-table-column>
      <el-table-column prop="manageUnit" label="管理单位" align="center">
      </el-table-column>
      <el-table-column prop="waterSupplyProject" label="所属供水工程" align="center">
      </el-table-column>
      <el-table-column prop="whetherEmergencySource" label="是否为应急水源地" align="center">
      </el-table-column>
    </el-table>

    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExampleSWS" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="name" label="水源地名称" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center">
      </el-table-column>
      <el-table-column prop="type" label="站点类型" align="center">
      </el-table-column>
      <el-table-column prop="area" label="水面面积" align="center">
      </el-table-column>
      <el-table-column prop="waterQualityObjectives" label="水质目标" align="center">
      </el-table-column>
      <el-table-column prop="waterSupplyContinuity" label="水资源供水持续状况" align="center">
      </el-table-column>
      <el-table-column prop="object" label="供水对象" align="center">
      </el-table-column>
      <el-table-column prop="manageUnit" label="管理单位" align="center">
      </el-table-column>
      <el-table-column prop="waterSupplyProject" label="所属供水工程" align="center">
      </el-table-column>
      <el-table-column prop="whetherEmergencySource" label="是否为应急水源地" align="center">
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
    //自定义表单验证规则
    var checkname = (rule, value, cb) => {
      const regtownCode = /^[\u4e00-\u9fa5_a-zA-Z0-9]{0,99}$/
      if (regtownCode.test(value)) {
        return cb()
      }
      cb(new Error('请合法输入'))
    }
    var checkCode = (rule, value, cb) => {
      const regvillageCode = /^\d{9}$/
      if (regvillageCode.test(value)) {
        return cb()
      }
      cb(new Error('请输入合法的泵站编码'))
    }
    var checklongitude = (rule, value, cb) => {
      const regvillageCode = /^([0-9]{1,3}[.][0-9]*)$/
      if (regvillageCode.test(value)) {
        return cb()
      }
      cb(new Error('请输入合法的经纬度'))
    }
    var checknumber = (rule, value, cb) => {
      const regvillageCode = /^[0-9]{0,9}$/
      if (regvillageCode.test(value)) {
        return cb()
      }
      cb(new Error('请输入合法的数字'))
    }
    return {
      file: '',
      fileList: [],
      input: '',
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      //数据列表
      ExampleSWS: [{
        name: '水源地名称',
        longitude: '113.369734',
        latitude: '31.71528',
        type: '类型1',
        area: '100',
        waterQualityObjectives: '水质目标1',
        waterSupplyContinuity: 'xxx',
        object: 'xxx对象',
        manageUnit: 'xxx单位',
        waterSupplyProject: '荆竹水资源供水工程',
        whetherEmergencySource: '是',
      }],
      AllSWSList: [],
      SWSList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //控制导入框
      importDialogVisible: false,
      //添加表单数据
      addForm: {
        name: '',
        longitude: '',
        latitude: '',
        type: '',
        area: '',
        waterQualityObjectives: '',
        waterSupplyContinuity: '',
        object: '',
        manageUnit: '',
        waterSupplyProject: '',
        whetherEmergencySource: '',
      },
      //添加表单的验证规则对象
      addFormRules: {
        name: [
          { required: true, message: '请输入所属水源地名称', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        longitude: [
          { required: true, message: '请输入经度', trigger: 'blur' },
          {
            pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
            message: '请输入正确的经度',
            trigger: 'blur'
          }
        ],
        latitude: [
          { required: true, message: '请输入纬度', trigger: 'blur' }, {
            pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
            message: '请输入正确的纬度',
            trigger: 'blur'
          }
        ],
        type: [
          { required: true, message: '请输入站点类型', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        area: [
          { required: true, message: '请输入水面面积', trigger: 'blur' },
          { validator: checknumber, trigger: 'blur' }
        ],
        waterQualityObjectives: [
          { required: true, message: '请输入水质目标', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        waterSupplyContinuity: [
          { required: true, message: '请输入水资源供水持续状况', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        object: [
          {
            required: true,
            message: '请输入供水对象',
            trigger: 'blur'
          },
          { validator: checkname, trigger: 'blur' }
        ],
        manageUnit: [
          { required: true, message: '请输入管理单位', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        waterSupplyProject: [
          { required: true, message: '请输入所属供水工程', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        whetherEmergencySource: [
          { required: true, message: '请输入是否为应急水源地', trigger: 'blur' },
        ],
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        name: [
          { required: true, message: '请输入所属水源地名称', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        longitude: [
          { required: true, message: '请输入经度', trigger: 'blur' },
          {
            pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
            message: '请输入正确的经度',
            trigger: 'blur'
          }
        ],
        latitude: [
          { required: true, message: '请输入纬度', trigger: 'blur' }, {
            pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
            message: '请输入正确的纬度',
            trigger: 'blur'
          }
        ],
        type: [
          { required: true, message: '请输入站点类型', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        area: [
          { required: true, message: '请输入水面面积', trigger: 'blur' },
          { validator: checknumber, trigger: 'blur' }
        ],
        waterQualityObjectives: [
          { required: true, message: '请输入水质目标', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        waterSupplyContinuity: [
          { required: true, message: '请输入水资源供水持续状况', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        object: [
          {
            required: true,
            message: '请输入供水对象',
            trigger: 'blur'
          },
          { validator: checkname, trigger: 'blur' }
        ],
        manageUnit: [
          { required: true, message: '请输入管理单位', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        waterSupplyProject: [
          { required: true, message: '请输入所属供水工程', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        whetherEmergencySource: [
          { required: true, message: '请输入是否为应急水源地', trigger: 'blur' },
        ],
      }
    }
  },
  methods: {
    //获取数据列表
    async getSWSList() {
      const { data: res } = await this.$http.get('/surface-water-sources/list', {
        params: this.queryinfo
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.SWSList = res.data.records
      this.total = res.data.total
    },
    //获取不分页数据列表
    async getAllSWSList() {
      const { data: res } = await this.$http.get(
        '/surface-water-sources/export-excel'
      )
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.AllSWSList = res.data
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get('/surface-water-sources/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getSWSList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getSWSList()
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
    addSWS() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/surface-water-sources/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getSWSList()
        this.getAllSWSList()
      })
    },
    //编辑角色信息表单
    editSWS() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/surface-water-sources/update', {
          id: this.editForm.id,
          name: this.editForm.name,
          longitude: this.editForm.longitude,
          latitude: this.editForm.latitude,
          type: this.editForm.type,
          area: this.editForm.area,
          waterQualityObjectives: this.editForm.waterQualityObjectives,
          waterSupplyContinuity: this.editForm.waterSupplyContinuity,
          object: this.editForm.object,
          manageUnit: this.editForm.manageUnit,
          waterSupplyProject: this.editForm.waterSupplyProject,
          whetherEmergencySource: this.editForm.whetherEmergencySource,
        })
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getSWSList()
        this.getAllSWSList()
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
      const { data: res } = await this.$http.post('/surface-water-sources/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getSWSList()
      this.getAllSWSList()
    },
    //搜索
    async searchSWS() {
      const { data: res } = await this.$http.get('/surface-water-sources/search-list-by-name', {
        params: {
          name: this.input,
          currentPage: this.queryinfo.currentPage,
          pageSize: this.queryinfo.pageSize
        }
      })
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.SWSList = res.data.records
      this.total = res.data.total
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
          '地表水源水数据列表报告.xlsx'
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
          '地表水源水数据列表模板.xlsx'
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
        this.getSWSList()
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
      let url = '/surface-water-sources/import-excel'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const { data: res } = await this.$http.post(url, formData, config)
      if (res.code === 200) {
        this.$message.success('导入成功')
        this.getAllSWSList()
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
    this.getSWSList()
    this.getAllSWSList()
  },
  mounted() { }
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

#div-header1 {
  height: 60px;
  width: 100%;
  margin-top: 0px;
  display: flex;
  background-color: rgb(253, 242, 228);
}

#div-header2 {
  height: 6%;
  width: 100%;
  margin-top: 0px;
  display: flex;
}

#div-main {
  height: calc(100% - 120px);
  width: 100%;
}

#div-footer {
  height: 35px;
  width: 100%;
  background-color: rgb(245, 237, 230);
}

.upload-demo {
  margin: 10px 200px;
}
</style>