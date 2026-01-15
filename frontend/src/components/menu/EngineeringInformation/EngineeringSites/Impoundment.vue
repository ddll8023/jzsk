<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top: 5px; padding-left: 10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>工程信息服务</el-breadcrumb-item>
        
        <el-breadcrumb-item :to="{ path: '/home/impoundment' }">蓄水池</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header1">
      <div style="align-self:center;margin-left:10px">
        <el-input placeholder="请输入蓄水池名称搜索" v-model="input" class="input-with-select" clearable
          @clear="getImpoundmentList">
          <el-button slot="append" icon="el-icon-search" @click="searchImpoundment"></el-button>
        </el-input>
      </div>

      <el-button type="primary" round icon="el-icon-plus"
        style="margin-left:auto; margin-right:10px; align-self: center;" @click="addDialogVisible = true">新增</el-button>

      <el-button type="primary" round icon="iconfont icon-icon-test" style="align-self:center;margin-right:10px"
        @click="importDialogVisible = true">导入</el-button>

      <el-button type="primary" round icon="iconfont icon-icon-test" style="align-self:center;margin-right:10px"
        @click="exportExcel">导出</el-button>
    </div>


    <div id="div-main">
      <el-table :data="ImpoundmentList" border stripe height="100%" style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="company" label="所属公司" align="center" min-width="100px">
        </el-table-column>
        <el-table-column prop="name" label="蓄水池站点名称" align="center" min-width="100px">
        </el-table-column>
        <el-table-column prop="address" label="地址" align="center" min-width="100px">
        </el-table-column>
        <el-table-column prop="longitude" label="经度" align="center" min-width="100px">
        </el-table-column>
        <el-table-column prop="latitude" label="纬度" align="center" min-width="100px">
        </el-table-column>
        <el-table-column prop="date" label="建站年月" align="center" min-width="80px">
        </el-table-column>
        <el-table-column prop="manageUnit" label="管理单位" align="center" min-width="100px">
        </el-table-column>
        <el-table-column prop="volume" label="蓄水池容积(m³)" align="center" width="80px">
        </el-table-column>
        <el-table-column prop="controlWaterLevel" label="蓄水池控制水位(m)" align="center" width="100px">
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" align="center" width="100px">
        </el-table-column>
        <el-table-column prop="phone" label="负责人电话" align="center" width="120px">
        </el-table-column>
        <el-table-column prop="rtuCode" label="蓄水池RTU编码" align="center" width="140px">
        </el-table-column>
        <el-table-column prop="note" label="备注" align="center" min-width="160px">
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="160">
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
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增蓄水池" :visible.sync="addDialogVisible" width="800px" @close="addDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属公司:" prop="company">
              <el-input v-model="addForm.company"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="蓄水池站点名称:" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="地址:" prop="address">
              <el-input v-model="addForm.address"></el-input>
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
            <el-form-item label="建站年月:" prop="date">
              <el-date-picker v-model="addForm.date" type="month" placeholder="选择月" value-format="yyyy-MM">
              </el-date-picker>
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
            <el-form-item label="蓄水池容积(m³):" prop="volume">
              <el-input v-model="addForm.volume"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="蓄水池控制水位(m):" prop="controlWaterLevel">
              <el-input v-model="addForm.controlWaterLevel"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人:" prop="responsiblePerson">
              <el-input v-model="addForm.responsiblePerson"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人电话:" prop="phone">
              <el-input v-model="addForm.phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="蓄水池RTU编码:" prop="rtuCode">
              <el-input v-model="addForm.rtuCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="备注:" prop="note">
              <el-input v-model="addForm.note" type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addImpoundment">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑蓄水池信息" :visible.sync="editDialogVisible" width="800px" @close="editDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属公司:" prop="company">
              <el-input v-model="editForm.company"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="蓄水池站点名称:" prop="name">
              <el-input v-model="editForm.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="地址:" prop="address">
              <el-input v-model="editForm.address"></el-input>
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
            <el-form-item label="建站年月:" prop="date">
              <el-date-picker v-model="editForm.date" type="month" placeholder="选择月" value-format="yyyy-MM">
              </el-date-picker>
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
            <el-form-item label="蓄水池容积(m³):" prop="volume">
              <el-input v-model="editForm.volume"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="蓄水池控制水位(m):" prop="controlWaterLevel">
              <el-input v-model="editForm.controlWaterLevel"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人:" prop="responsiblePerson">
              <el-input v-model="editForm.responsiblePerson"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人电话:" prop="phone">
              <el-input v-model="editForm.phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="蓄水池RTU编码:" prop="rtuCode">
              <el-input v-model="editForm.rtuCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="备注:" prop="note">
              <el-input v-model="editForm.note" type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editImpoundment">确 定</el-button>
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
        action="/impoundment/import-excel" :file-list="fileList" :auto-upload="false" :on-change="fileChange"
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
    <el-table id="table" :data="AllImpoundmentList" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="company" label="所属公司" align="center">
      </el-table-column>
      <el-table-column prop="name" label="蓄水池站点名称" align="center">
      </el-table-column>
      <el-table-column prop="address" label="地址" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center">
      </el-table-column>
      <el-table-column prop="date" label="建站年月" align="center">
      </el-table-column>
      <el-table-column prop="manageUnit" label="管理单位" align="center">
      </el-table-column>
      <el-table-column prop="volume" label="蓄水池容积(m³)" align="center">
      </el-table-column>
      <el-table-column prop="controlWaterLevel" label="蓄水池控制水位(m)" align="center">
      </el-table-column>
      <el-table-column prop="responsiblePerson" label="负责人" align="center">
      </el-table-column>
      <el-table-column prop="phone" label="负责人电话" align="center">
      </el-table-column>
      <el-table-column prop="rtuCode" label="蓄水池RTU编码" align="center">
      </el-table-column>
      <el-table-column prop="note" label="备注" align="center">
      </el-table-column>
    </el-table>

    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExampleImpoundment" border stripe height="100%"
      style="width: 100%;display:none;" :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="company" label="所属公司" align="center">
      </el-table-column>
      <el-table-column prop="name" label="蓄水池站点名称" align="center">
      </el-table-column>
      <el-table-column prop="address" label="地址" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center">
      </el-table-column>
      <el-table-column prop="date" label="建站年月" align="center">
      </el-table-column>
      <el-table-column prop="manageUnit" label="管理单位" align="center">
      </el-table-column>
      <el-table-column prop="volume" label="蓄水池容积(m³)" align="center">
      </el-table-column>
      <el-table-column prop="controlWaterLevel" label="蓄水池控制水位(m)" align="center">
      </el-table-column>
      <el-table-column prop="responsiblePerson" label="负责人" align="center">
      </el-table-column>
      <el-table-column prop="phone" label="负责人电话" align="center">
      </el-table-column>
      <el-table-column prop="rtuCode" label="蓄水池RTU编码" align="center">
      </el-table-column>
      <el-table-column prop="note" label="备注" align="center">
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
      const regvillageCode = /^[0-9]{0,20}$/
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
      ExampleImpoundment: [{
        company: '',
        name: '',
        address: '',
        longitude: '',
        latitude: '',
        date: '',
        manageUnit: '',
        volume: '',
        controlWaterLevel: '',
        responsiblePerson: '',
        phone: '',
        rtuCode: '',
        note: ''
      }],
      ImpoundmentList: [],
      AllImpoundmentList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //控制导入框
      importDialogVisible: false,
      //添加表单数据
      addForm: {
        company: '',
        name: '',
        address: '',
        longitude: '',
        latitude: '',
        date: '',
        manageUnit: '',
        volume: '',
        controlWaterLevel: '',
        responsiblePerson: '',
        phone: '',
        rtuCode: '',
        note: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        company: [
          { required: true, message: '请输入所属公司', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入蓄水池站点名称', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        longitude: [
          { required: true, message: '请输入经度', trigger: 'blur' }, ,
          {
            pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
            message: '请输入正确的经度',
            trigger: 'blur'
          }
        ],
        latitude: [
          { required: true, message: '请输入纬度', trigger: 'blur' },
          {
            pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
            message: '请输入正确的纬度',
            trigger: 'blur'
          }
        ],
        date: [{ required: true, message: '请输入建站年月', trigger: 'blur' }],
        manageUnit: [
          {
            required: true,
            message: '管理单位',
            trigger: 'blur'
          },
          { validator: checkname, trigger: 'blur' }
        ],
        volume: [
          { required: true, message: '请输入蓄水池容积(m³)', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        controlWaterLevel: [
          {
            required: true,
            message: '请输入蓄水池控制水位(m)',
            trigger: 'blur'
          },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        responsiblePerson: [
          { required: true, message: '请输入负责人', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入负责人电话', trigger: 'blur' },
          { pattern: /^1[3456789]\d{9}$/, message: "请输入正确电话号码", trigger: 'blur' }
        ],
        rtuCode: [
          { required: true, message: '请输入蓄水池RTU编码', trigger: 'blur' },
          { pattern: /^[1-9]\d*$/, message: "请输入正整数", trigger: 'blur' }
        ]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        company: [
          { required: true, message: '请输入所属公司', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入蓄水池站点名称', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' },
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
          { required: true, message: '请输入纬度', trigger: 'blur' },
          {
            pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
            message: '请输入正确的纬度',
            trigger: 'blur'
          }
        ],
        date: [{ required: true, message: '请输入建站年月', trigger: 'blur' }],
        manageUnit: [
          {
            required: true,
            message: '管理单位',
            trigger: 'blur'
          },
          { validator: checkname, trigger: 'blur' }
        ],
        volume: [
          { required: true, message: '请输入蓄水池容积(m³)', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        controlWaterLevel: [
          {
            required: true,
            message: '请输入蓄水池控制水位(m)',
            trigger: 'blur'
          },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        responsiblePerson: [
          { required: true, message: '请输入负责人', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入负责人电话', trigger: 'blur' },
          { pattern: /^1[3456789]\d{9}$/, message: "请输入正确电话号码", trigger: 'blur' }
        ],
        rtuCode: [
          { required: true, message: '请输入蓄水池RTU编码', trigger: 'blur' },
          { pattern: /^[1-9]\d*$/, message: "请输入正整数", trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    //获取数据列表
    async getImpoundmentList() {
      const { data: res } = await this.$http.get('/impoundment/list', {
        params: this.queryinfo
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.ImpoundmentList = res.data.records
      this.total = res.data.total
    },
    //获取不分页数据列表
    async getAllImpoundmentList() {
      const { data: res } = await this.$http.get('/impoundment/export-excel')
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.AllImpoundmentList = res.data
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get('/impoundment/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getImpoundmentList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getImpoundmentList()
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
    addImpoundment() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/impoundment/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getImpoundmentList()
        this.getAllImpoundmentList()
      })
    },
    //编辑角色信息表单
    editImpoundment() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/impoundment/update', {
          id: this.editForm.id,
          company: this.editForm.company,
          name: this.editForm.name,
          address: this.editForm.address,
          longitude: this.editForm.longitude,
          latitude: this.editForm.latitude,
          date: this.editForm.date,
          manageUnit: this.editForm.manageUnit,
          volume: this.editForm.volume,
          controlWaterLevel: this.editForm.controlWaterLevel,
          responsiblePerson: this.editForm.responsiblePerson,
          phone: this.editForm.phone,
          rtuCode: this.editForm.rtuCode,
          note: this.editForm.note
        })
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getImpoundmentList()
        this.getAllImpoundmentList()
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
      const { data: res } = await this.$http.post('/impoundment/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getImpoundmentList()
      this.getAllImpoundmentList()
    },
    //搜索
    async searchImpoundment() {
      const { data: res } = await this.$http.get('/impoundment/name', {
        params: {
          name: this.input,
          currentPage: this.queryinfo.currentPage,
          pageSize: this.queryinfo.pageSize
        }
      })
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.ImpoundmentList = res.data.records
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
          '蓄水池数据列表报告.xlsx'
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
          '蓄水池数据列表模板.xlsx'
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
        this.getImpoundmentList()
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
      let url = '/impoundment/import-excel'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const { data: res } = await this.$http.post(url, formData, config)
      if (res.code === 200) {
        this.$message.success('导入成功')
        this.getAllImpoundmentList()
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
    this.getImpoundmentList()
    this.getAllImpoundmentList()
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
  float: left;
  text-align: center;
  background-color: rgb(245, 237, 230);
}

.upload-demo {
  margin: 10px 200px;
}
</style>
