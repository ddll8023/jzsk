<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb
        separator-class="el-icon-arrow-right"
        style="padding-top: 5px; padding-left: 10px"
      >
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>工程信息服务</el-breadcrumb-item>
        
        <el-breadcrumb-item :to="{ path: '/home/individualflowsites' }"
          >单独流量站点</el-breadcrumb-item
        >
      </el-breadcrumb>
    </div>

    <div id="div-header1">
      <div style="align-self:center;margin-left:10px">
        <el-input
          placeholder="请输入单独流量站点名搜索"
          v-model="input"
          class="input-with-select"
          clearable
          @clear="getIFSList"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="searchIFS"
          ></el-button>
        </el-input>
      </div>

      <el-button
        type="primary"
        round
        icon="el-icon-plus"
        style="margin-left:auto;margin-right:10px; align-self: center;"
        @click="addDialogVisible = true"
        >新增</el-button
      >

      <el-button
        type="primary"
        round
        icon="iconfont icon-icon-test"
        style="align-self:center;margin-right:10px"
        @click="importDialogVisible = true"
        >导入</el-button
      >

      <el-button
        type="primary"
        round
        icon="iconfont icon-icon-test"
        style="align-self:center;margin-right:10px"
        @click="exportExcel"
        >导出</el-button
      >
    </div>


    
    <div id="div-main">
      <el-table
        :data="IFSList"
        border
        stripe
        height="100%"
        style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }"
      >
        <el-table-column
          type="index"
          label="序号"
          width="60px"
          align="center"
          :index="table_index"
        >
        </el-table-column>
        <el-table-column prop="name" label="流量测站名称" align="center" min-width="120px">
        </el-table-column>
        <el-table-column prop="stationNumber" label="测点编号" align="center" min-width="120px">
        </el-table-column>
        <el-table-column prop="rtuCode" label="RTU编号" align="center" min-width="120px">
        </el-table-column>
        <el-table-column
          prop="measuringStationsElements"
          label="测站要素"
          align="center"
          min-width="100px"
        >
        </el-table-column>
        <el-table-column
          prop="splitSiteCode"
          label="拆分站点编码"
          align="center"
          min-width="120px"
        >
        </el-table-column>
        <el-table-column prop="address" label="所在地址" align="center" min-width="120px">
        </el-table-column>
        <el-table-column prop="longitude" label="站点经度" align="center" min-width="100px">
        </el-table-column>
        <el-table-column prop="latitude" label="站点纬度" align="center" min-width="100px">
        </el-table-column>
        <el-table-column label="预警指标范围" class="column" align="center">
          <el-table-column
            prop="upUpLimit"
            label="上上限"
            width="100"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="upLimit"
            label="上限"
            width="100"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="lowLimit"
            label="下限"
            width="100"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="lowerLimit"
            label="下下限"
            width="100"
            align="center"
          >
          </el-table-column>
        </el-table-column>
        <el-table-column prop="note" label="备注" align="center" min-width="200px">
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="200">
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
        style="padding-left:40%"
      >
        >
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog
      title="新增单独流量站点"
      :visible.sync="addDialogVisible"
      width="800px"
      @close="addDialogClosed"
      center
      :close-on-press-escape="false"
      :close-on-click-modal="false"
    >
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="addFormRef"
        label-width="150px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="流量测站名称:" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测点编号:" prop="stationNumber">
              <el-input v-model="addForm.stationNumber"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="RTU编号:" prop="rtuCode">
              <el-input v-model="addForm.rtuCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测站要素:" prop="measuringStationsElements">
              <el-input v-model="addForm.measuringStationsElements"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="拆分站点编码:" prop="splitSiteCode">
              <el-input v-model="addForm.splitSiteCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在地址:" prop="address">
              <el-input v-model="addForm.address"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="站点经度:" prop="longitude">
              <el-input v-model="addForm.longitude"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="站点纬度:" prop="latitude">
              <el-input v-model="addForm.latitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警上上限:" prop="upUpLimit">
              <el-input v-model="addForm.upUpLimit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警上限:" prop="upLimit">
              <el-input v-model="addForm.upLimit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警下限:" prop="lowLimit">
              <el-input v-model="addForm.lowLimit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警下下限:" prop="lowerLimit">
              <el-input v-model="addForm.lowerLimit"></el-input>
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
        <el-button type="primary" @click="addIFS">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog
      title="编辑单独流量站点"
      :visible.sync="editDialogVisible"
      width="800px"
      @close="editDialogClosed"
      center
      :close-on-press-escape="false"
      :close-on-click-modal="false"
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
            <el-form-item label="流量测站名称:" prop="name">
              <el-input v-model="editForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测点编号:" prop="stationNumber">
              <el-input v-model="editForm.stationNumber"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="RTU编号:" prop="rtuCode">
              <el-input v-model="editForm.rtuCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测站要素:" prop="measuringStationsElements">
              <el-input v-model="editForm.measuringStationsElements"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="拆分站点编码:" prop="splitSiteCode">
              <el-input v-model="editForm.splitSiteCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在地址:" prop="address">
              <el-input v-model="editForm.address"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="站点经度:" prop="longitude">
              <el-input v-model="editForm.longitude"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="站点纬度:" prop="latitude">
              <el-input v-model="editForm.latitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警上上限:" prop="upUpLimit">
              <el-input v-model="editForm.upUpLimit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警上限:" prop="upLimit">
              <el-input v-model="editForm.upLimit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警下限:" prop="lowLimit">
              <el-input v-model="editForm.lowLimit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警下下限:" prop="lowerLimit">
              <el-input v-model="editForm.lowerLimit"></el-input>
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
        <el-button type="primary" @click="editIFS">确 定</el-button>
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
        action="/individual-flow-sites/import-excel"
        :file-list="fileList"
        :auto-upload="false"
        :on-change="fileChange"
        :on-remove="handleRemove"
        :before-upload="beforeUpload"
        :http-request="httpRequest"
        multiple
        style="margin: 10px 200px;"
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

    <!-- 导出表格 -->
    <el-table
      id="table"
      :data="AllIFSList"
      border
      stripe
      height="100%"
      style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }"
    >
      <el-table-column
        type="index"
        label="序号"
        width="60px"
        align="center"
        :index="table_index"
      >
      </el-table-column>
      <el-table-column prop="name" label="流量测站名称" align="center">
      </el-table-column>
      <el-table-column prop="stationNumber" label="测点编号" align="center">
      </el-table-column>
      <el-table-column prop="rtuCode" label="RTU编号" align="center">
      </el-table-column>
      <el-table-column
        prop="measuringStationsElements"
        label="测站要素"
        align="center"
      >
      </el-table-column>
      <el-table-column prop="splitSiteCode" label="拆分站点编码" align="center">
      </el-table-column>
      <el-table-column prop="address" label="所在地址" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="站点经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="站点纬度" align="center">
      </el-table-column>
      <el-table-column label="预警指标范围" class="column" align="center">
        <el-table-column
          prop="upUpLimit"
          label="上上限"
          width="120"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="upLimit" label="上限" width="120" align="center">
        </el-table-column>
        <el-table-column
          prop="lowLimit"
          label="下限"
          width="120"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="lowerLimit"
          label="下下限"
          width="120"
          align="center"
        >
        </el-table-column>
      </el-table-column>
      <el-table-column prop="note" label="备注" align="center">
      </el-table-column>
    </el-table>

    <!-- 导出数据模板 -->
    <el-table
      id="tableFormwork"
      :data="ExampleIFS"
      border
      stripe
      height="100%"
      style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }"
    >
      <el-table-column
        type="index"
        label="序号"
        width="60px"
        align="center"
        :index="table_index"
      >
      </el-table-column>
      <el-table-column prop="name" label="流量测站名称" align="center">
      </el-table-column>
      <el-table-column prop="stationNumber" label="测点编号" align="center">
      </el-table-column>
      <el-table-column prop="rtuCode" label="RTU编号" align="center">
      </el-table-column>
      <el-table-column
        prop="measuringStationsElements"
        label="测站要素"
        align="center"
      >
      </el-table-column>
      <el-table-column prop="splitSiteCode" label="拆分站点编码" align="center">
      </el-table-column>
      <el-table-column prop="address" label="所在地址" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="站点经度" align="center">
      </el-table-column>
      <el-table-column prop="latitude" label="站点纬度" align="center">
      </el-table-column>
      <el-table-column label="预警指标范围" class="column" align="center">
        <el-table-column
          prop="upUpLimit"
          label="上上限"
          width="120"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="upLimit" label="上限" width="120" align="center">
        </el-table-column>
        <el-table-column
          prop="lowLimit"
          label="下限"
          width="120"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="lowerLimit"
          label="下下限"
          width="120"
          align="center"
        >
        </el-table-column>
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
      ExampleIFS:[{
        name: '测站1',
        stationNumber: '111111',
        rtuCode: '123456789',
        measuringStationsElements: '',
        splitSiteCode: '',
        address: '',
        longitude: '',
        latitude: '',
        upUpLimit: '',
        upLimit: '',
        lowLimit: '',
        lowerLimit: '',
        note: ''
      }],
      IFSList: [],
      AllIFSList:[],
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
        stationNumber: '',
        rtuCode: '',
        measuringStationsElements: '',
        splitSiteCode: '',
        address: '',
        longitude: '',
        latitude: '',
        upUpLimit: '',
        upLimit: '',
        lowLimit: '',
        lowerLimit: '',
        note: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        name: [
          { required: true, message: '请输入流量测站名称', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        stationNumber: [
          { required: true, message: '请输入测点编号', trigger: 'blur' }
        ],
        rtuCode: [
          { required: true, message: '请输入RTU编号', trigger: 'blur' },
          { validator: checknumber, trigger: 'blur' }
        ],
        measuringStationsElements: [
          { required: true, message: '请输入测站要素', trigger: 'blur' }
        ],
        splitSiteCode: [
          { required: true, message: '请输入拆分站点编码', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入所在地址', trigger: 'blur' }
        ],
        longitude: [
          {
            required: true,
            message: '请输入站点经度',
            trigger: 'blur'
          },
          {
          pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
          message: '请输入正确的经度',
          trigger: 'blur'
        }
        ],
        latitude: [
          { required: true, message: '请输入站点纬度', trigger: 'blur' },
          {
          pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
          message: '请输入正确的纬度',
          trigger: 'blur'
        }
        ],
        upUpLimit: [
          {
            required: true,
            message: '请输入预警上上限',
            trigger: 'blur'
          },
          {pattern:/^\d+(\.\d+)?$/,message:"请输入非负数",trigger:'blur'}
        ],
        upLimit: [
          { required: true, message: '请输入预警上限', trigger: 'blur' },
          {pattern:/^\d+(\.\d+)?$/,message:"请输入非负数",trigger:'blur'}
        ],
        lowLimit: [
          { required: true, message: '请输入预警下限', trigger: 'blur' },
          {pattern:/^\d+(\.\d+)?$/,message:"请输入非负数",trigger:'blur'}
        ],
        lowerLimit: [
          { required: true, message: '请输入预警下下限', trigger: 'blur' },
          {pattern:/^\d+(\.\d+)?$/,message:"请输入非负数",trigger:'blur'}
        ],
        note: [{ required: true, message: '请输入备注', trigger: 'blur' }]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        name: [
          { required: true, message: '请输入流量测站名称', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        stationNumber: [
          { required: true, message: '请输入测点编号', trigger: 'blur' }
        ],
        rtuCode: [
          { required: true, message: '请输入RTU编号', trigger: 'blur' },
          { validator: checknumber, trigger: 'blur' }
        ],
        measuringStationsElements: [
          { required: true, message: '请输入测站要素', trigger: 'blur' }
        ],
        splitSiteCode: [
          { required: true, message: '请输入拆分站点编码', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入所在地址', trigger: 'blur' }
        ],
        longitude: [
          {
            required: true,
            message: '请输入站点经度',
            trigger: 'blur'
          },
          {
          pattern: /^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10})$/,
          message: '请输入正确的经度',
          trigger: 'blur'
        }
        ],
        latitude: [
          { required: true, message: '请输入站点纬度', trigger: 'blur' },
          {
          pattern: /^[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
          message: '请输入正确的纬度',
          trigger: 'blur'
        }
        ],
        upUpLimit: [
          {
            required: true,
            message: '请输入预警上上限',
            trigger: 'blur'
          },
          {pattern:/^\d+(\.\d+)?$/,message:"请输入非负数",trigger:'blur'}
        ],
        upLimit: [
          { required: true, message: '请输入预警上限', trigger: 'blur' },
          {pattern:/^\d+(\.\d+)?$/,message:"请输入非负数",trigger:'blur'}
        ],
        lowLimit: [
          { required: true, message: '请输入预警下限', trigger: 'blur' },
          {pattern:/^\d+(\.\d+)?$/,message:"请输入非负数",trigger:'blur'}
        ],
        lowerLimit: [
          { required: true, message: '请输入预警下下限', trigger: 'blur' },
          {pattern:/^\d+(\.\d+)?$/,message:"请输入非负数",trigger:'blur'}
        ],
        note: [{ required: true, message: '请输入备注', trigger: 'blur' }]
      }
    }
  },
  methods: {
    //获取数据列表
    async getIFSList() {
      const { data: res } = await this.$http.get(
        '/individual-flow-sites/list',
        {
          params: this.queryinfo
        }
      )
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.IFSList = res.data.records
      this.total = res.data.total
    },
    //获取不分页数据列表
    async getAllIFSList() {
      const { data: res } = await this.$http.get('/individual-flow-sites/export-excel')
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.AllIFSList = res.data
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get(
        '/individual-flow-sites/info/' + id
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
      this.getIFSList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getIFSList()
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
    addIFS() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/individual-flow-sites/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getIFSList()
        this.getAllIFSList()
      })
    },
    //编辑角色信息表单
    editIFS() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          '/individual-flow-sites/update',
          {
            id: this.editForm.id,
            name: this.editForm.name,
            stationNumber: this.editForm.stationNumber,
            rtuCode: this.editForm.rtuCode,
            measuringStationsElements: this.editForm.measuringStationsElements,
            splitSiteCode: this.editForm.splitSiteCode,
            address: this.editForm.address,
            longitude: this.editForm.longitude,
            latitude: this.editForm.latitude,
            upUpLimit: this.editForm.upUpLimit,
            upLimit: this.editForm.upLimit,
            lowLimit: this.editForm.lowLimit,
            lowerLimit: this.editForm.lowerLimit,
            note: this.editForm.note
          }
        )
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getIFSList()
        this.getAllIFSList()
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
        '/individual-flow-sites/delete/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getIFSList()
      this.getAllIFSList()
    },
    //搜索
    async searchIFS() {
      const { data: res } = await this.$http.get(
        '/individual-flow-sites/search-list-by-name',
        {
          params: {
            name: this.input,
            currentPage: this.queryinfo.currentPage,
            pageSize: this.queryinfo.pageSize
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.IFSList = res.data.records
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
          '单独流量站点数据列表报告.xlsx'
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
          '单独流量站点数据列表模板.xlsx'
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
      this.importDialogVisible=false
      this.$refs.upload.submit()
      setTimeout(() => {
        this.getIFSList()
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
      let url = '/individual-flow-sites/import-excel'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const { data: res } = await this.$http.post(url, formData, config)
      if (res.code === 200) {
        this.$message.success('导入成功')
        this.getAllIFSList()
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
    this.getIFSList()
    this.getAllIFSList()
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
