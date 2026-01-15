<template>
  <div id="div1">
    <div id="div-header1">
      <div style="align-self:center;margin-left:10px">
        <el-input placeholder="请输入水库名称搜索" v-model="input" class="input-with-select" clearable @clear="getReservoirList">
          <el-button slot="append" icon="el-icon-search" @click="searchReservoir"></el-button>
        </el-input>
      </div>

      <el-button type="primary" round icon="iconfont icon-icon-test" style="margin-left: auto;align-self:center;margin-right:10px"
        @click="exportExcel">导出</el-button>

    </div>

    <div id="div-main">
      <el-table :data="ReservoirList" border stripe height="100%" style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
        </el-table-column>
        <el-table-column prop="waterSupply" label="所属供水工程" align="center" width="120px">
        </el-table-column>
        <el-table-column prop="code" label="水库编码" align="center" width="100px">
        </el-table-column>
        <el-table-column prop="name" label="水库名称" align="center">
        </el-table-column>
        <el-table-column prop="longitude" label="经度" align="center" width="100px">
        </el-table-column>
        <el-table-column prop="latitude" label="纬度" align="center" width="100px">
        </el-table-column>
        <el-table-column prop="locate" label="水库所在位置" align="center" width="150px">
        </el-table-column>
        <el-table-column prop="registrationNumber" label="水库注册登记号" align="center" width="150px">
        </el-table-column>
        <el-table-column prop="administrationDivision" label="水库注册所在行政区划" align="center" width="180px">
        </el-table-column>
        <el-table-column prop="level" label="水库工程等级" align="center" width="120px">
        </el-table-column>
        <el-table-column prop="scale" label="水库工程规模" align="center" width="120px">
        </el-table-column>
        <el-table-column prop="totalStorageCapacity" label="总库容(万m3)" align="center" width="80px">
        </el-table-column>
        <el-table-column prop="regulatingStorageCapacity" label="调节库容(万m3)" align="center" width="80px">
        </el-table-column>
        <el-table-column prop="deadStorage" label="死库容(万m3)" align="center" width="80px">
        </el-table-column>
        <el-table-column prop="designFloodLevel" label="设计洪水位(m)" align="center" width="80px">
        </el-table-column>
        <el-table-column prop="normalStorageLevel" label="正常蓄水位(m)" align="center" width="80px">
        </el-table-column>
        <el-table-column prop="deadWaterLevel" label="死水位(m)" align="center" width="80px">
        </el-table-column>
        <el-table-column prop="date" label="建站年月" align="center" width="100px">
        </el-table-column>
        <el-table-column prop="manageUnit" label="管理单位" align="center" width="100px">
        </el-table-column>
        <el-table-column prop="waterSupplyArea" label="供水范围" align="center" width="100px">
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
    <el-dialog title="新增水库" :visible.sync="addDialogVisible" width="850px" @close="addDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="170px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属供水工程:" prop="waterSupply">
              <el-input v-model="addForm.waterSupply"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水库编码:" prop="code">
              <el-input v-model="addForm.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水库名称:" prop="name">
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
            <el-form-item label="水库所在位置:" prop="locate">
              <el-input v-model="addForm.locate"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水库注册登记号:" prop="registrationNumber">
              <el-input v-model="addForm.registrationNumber"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水库注册所在行政区划:" prop="administrationDivision">
              <el-input v-model="addForm.administrationDivision"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水库工程等级:" prop="level">
              <el-select v-model="addForm.level" placeholder="请选择水库工程等级">
                <el-option label="Ⅰ" value="Ⅰ"></el-option>
                <el-option label="Ⅱ" value="Ⅱ"></el-option>
                <el-option label="Ⅲ" value="Ⅲ"></el-option>
                <el-option label="Ⅳ" value="Ⅳ"></el-option>
                <el-option label="Ⅴ" value="Ⅴ"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水库工程规模:" prop="scale">
              <el-select v-model="addForm.scale" placeholder="请选择水库工程规模">
                <el-option label="大(1)型" value="大(1)型"></el-option>
                <el-option label="大(2)型" value="大(2)型"></el-option>
                <el-option label="中型" value="中型"></el-option>
                <el-option label="小(1)型" value="小(1)型"></el-option>
                <el-option label="小(2)型" value="小(2)型"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="总库容(万m3):" prop="totalStorageCapacity">
              <el-input v-model="addForm.totalStorageCapacity"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调节库容(万m3):" prop="regulatingStorageCapacity">
              <el-input v-model="addForm.regulatingStorageCapacity"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="死库容(万m3):" prop="deadStorage">
              <el-input v-model="addForm.deadStorage"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设计洪水位(m):" prop="designFloodLevel">
              <el-input v-model="addForm.designFloodLevel"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="正常蓄水位(m):" prop="normalStorageLevel">
              <el-input v-model="addForm.normalStorageLevel"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="死水位(m):" prop="deadWaterLevel">
              <el-input v-model="addForm.deadWaterLevel"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="建站年月:" prop="date">
              <el-date-picker v-model="addForm.date" type="month" placeholder="选择月" value-format="yyyy-MM">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管理单位:" prop="manageUnit">
              <el-input v-model="addForm.manageUnit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供水范围:" prop="waterSupplyArea">
              <el-input v-model="addForm.waterSupplyArea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addReservoir">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑水库信息" :visible.sync="editDialogVisible" width="850px" @close="editDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="170px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属供水工程:" prop="waterSupply">
              <el-input v-model="editForm.waterSupply"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水库编码:" prop="code">
              <el-input v-model="editForm.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水库名称:" prop="name">
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
            <el-form-item label="水库所在位置:" prop="locate">
              <el-input v-model="editForm.locate"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水库注册登记号:" prop="registrationNumber">
              <el-input v-model="editForm.registrationNumber"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水库注册所在行政区划:" prop="administrationDivision">
              <el-input v-model="editForm.administrationDivision"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水库工程等级:" prop="level">
              <el-select v-model="editForm.level" placeholder="请选择水库工程等级">
                <el-option label="Ⅰ" value="Ⅰ"></el-option>
                <el-option label="Ⅱ" value="Ⅱ"></el-option>
                <el-option label="Ⅲ" value="Ⅲ"></el-option>
                <el-option label="Ⅳ" value="Ⅳ"></el-option>
                <el-option label="Ⅴ" value="Ⅴ"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水库工程规模:" prop="scale">
              <el-select v-model="editForm.scale" placeholder="请选择水库工程规模">
                <el-option label="大(1)型" value="大(1)型"></el-option>
                <el-option label="大(2)型" value="大(2)型"></el-option>
                <el-option label="中型" value="中型"></el-option>
                <el-option label="小(1)型" value="小(1)型"></el-option>
                <el-option label="小(2)型" value="小(2)型"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="总库容(万m3):" prop="totalStorageCapacity">
              <el-input v-model="editForm.totalStorageCapacity"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调节库容(万m3):" prop="regulatingStorageCapacity">
              <el-input v-model="editForm.regulatingStorageCapacity"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="死库容(万m3):" prop="deadStorage">
              <el-input v-model="editForm.deadStorage"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设计洪水位(m):" prop="designFloodLevel">
              <el-input v-model="editForm.designFloodLevel"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="正常蓄水位(m):" prop="normalStorageLevel">
              <el-input v-model="editForm.normalStorageLevel"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="死水位(m):" prop="deadWaterLevel">
              <el-input v-model="editForm.deadWaterLevel"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="建站年月:" prop="date">
              <el-date-picker v-model="editForm.date" type="month" placeholder="选择月" value-format="yyyy-MM">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管理单位:" prop="manageUnit">
              <el-input v-model="editForm.manageUnit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供水范围:" prop="waterSupplyArea">
              <el-input v-model="editForm.waterSupplyArea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editReservoir">确 定</el-button>
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
      <el-upload class="upload-demo" drag ref="upload" accept=".xlsx,.xls" name="file" action="/reservoir/import-excel"
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

    <!-- 导出表格 -->
    <el-table id="table" :data="AllReservoirList" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="waterSupply" label="所属供水工程" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="code" label="水库编码" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="name" label="水库名称" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="locate" label="水库所在位置" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="registrationNumber" label="水库注册登记号" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="administrationDivision" label="水库注册所在行政区划" align="center" width="180px">
      </el-table-column>
      <el-table-column prop="level" label="水库工程等级" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="scale" label="水库工程规模" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="totalStorageCapacity" label="总库容(万m3)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="regulatingStorageCapacity" label="调节库容(万m3)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="deadStorage" label="死库容(万m3)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="designFloodLevel" label="设计洪水位(m)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="normalStorageLevel" label="正常蓄水位(m)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="deadWaterLevel" label="死水位(m)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="date" label="建站年月" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="manageUnit" label="管理单位" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="waterSupplyArea" label="供水范围" align="center" width="100px">
      </el-table-column>
    </el-table>

    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExampleR" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="waterSupply" label="所属供水工程" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="code" label="水库编码" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="name" label="水库名称" align="center">
      </el-table-column>
      <el-table-column prop="longitude" label="经度" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="latitude" label="纬度" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="locate" label="水库所在位置" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="registrationNumber" label="水库注册登记号" align="center" width="150px">
      </el-table-column>
      <el-table-column prop="administrationDivision" label="水库注册所在行政区划" align="center" width="180px">
      </el-table-column>
      <el-table-column prop="level" label="水库工程等级" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="scale" label="水库工程规模" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="totalStorageCapacity" label="总库容(万m3)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="regulatingStorageCapacity" label="调节库容(万m3)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="deadStorage" label="死库容(万m3)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="designFloodLevel" label="设计洪水位(m)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="normalStorageLevel" label="正常蓄水位(m)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="deadWaterLevel" label="死水位(m)" align="center" width="80px">
      </el-table-column>
      <el-table-column prop="date" label="建站年月" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="manageUnit" label="管理单位" align="center" width="100px">
      </el-table-column>
      <el-table-column prop="waterSupplyArea" label="供水范围" align="center" width="100px">
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
      cb(new Error('请输入9位数泵站编码'))
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
      ExampleR: [
        {
          waterSupply: '荆竹水资源供水工程',
          code: '123545689(9位阿拉伯数字)',
          name: 'XXXX水库',
          longitude: '105.672284(-180到180)',
          latitude: '37.185668(-90到90)',
          locate: 'XX省XX市XX县XX镇XX村XX位置',
          registrationNumber: '4209815XXXX-XX(根据实际情况)',
          administrationDivision: 'XX省XX市XX县XX镇XX村',
          level: 'Ⅰ',
          scale: '小(2)型',
          totalStorageCapacity: '10',
          regulatingStorageCapacity: '20',
          deadStorage: '30',
          designFloodLevel: '40',
          normalStorageLevel: '50',
          deadWaterLevel: '60',
          date: '2022-3',
          manageUnit: 'XX管理单位',
          waterSupplyArea: 'XXX乡镇XXX村、XXX乡镇XXX村'
        }
      ],
      ReservoirList: [],
      AllReservoirList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //控制导入框
      importDialogVisible: false,
      //添加表单数据
      addForm: {
        waterSupply: '',
        code: '',
        name: '',
        locate: '',
        longitude: '',
        latitude: '',
        registrationNumber: '',
        administrationDivision: '',
        level: '',
        scale: '',
        totalStorageCapacity: '',
        regulatingStorageCapacity: '',
        deadStorage: '',
        designFloodLevel: '',
        normalStorageLevel: '',
        deadWaterLevel: '',
        date: '',
        manageUnit: '',
        waterSupplyArea: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        waterSupply: [
          { required: true, message: '请输入所属供水工程', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入水库编码', trigger: 'blur' },
        ],
        name: [
          { required: true, message: '请输入水库名称', trigger: 'blur' },
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
        locate: [
          { required: true, message: '请输入水库所在位置', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        registrationNumber: [
          {
            required: true,
            message: '请输入水库注册登记号',
            trigger: 'blur'
          }
        ],
        administrationDivision: [
          {
            required: true,
            message: '请输入水库注册所在行政区划',
            trigger: 'blur'
          },
          { validator: checkname, trigger: 'blur' }
        ],
        level: [
          { required: true, message: '请输入水库工程等级', trigger: 'blur' }
        ],
        scale: [
          { required: true, message: '请输入水库工程规模', trigger: 'blur' }
        ],
        totalStorageCapacity: [
          { required: true, message: '请输入总库容', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        regulatingStorageCapacity: [
          { required: true, message: '请输入调节库容', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        deadStorage: [
          { required: true, message: '请输入死库容', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        designFloodLevel: [
          { required: true, message: '请输入设计洪水位', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        normalStorageLevel: [
          { required: true, message: '请输入正常蓄水位', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        deadWaterLevel: [
          { required: true, message: '请输入死水位', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        date: [{ required: true, message: '请输入建站年月', trigger: 'blur' }],
        manageUnit: [
          { required: true, message: '请输入管理单位', trigger: 'blur' }
        ],
        waterSupplyArea: [
          { required: true, message: '请输入供水范围', trigger: 'blur' }
        ]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        waterSupply: [
          { required: true, message: '请输入所属供水工程', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入水库编码', trigger: 'blur' },
        ],
        name: [
          { required: true, message: '请输入水库名称', trigger: 'blur' },
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
        locate: [
          { required: true, message: '请输入水库所在位置', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        registrationNumber: [
          {
            required: true,
            message: '请输入水库注册登记号',
            trigger: 'blur'
          }
        ],
        administrationDivision: [
          {
            required: true,
            message: '请输入水库注册所在行政区划',
            trigger: 'blur'
          },
          { validator: checkname, trigger: 'blur' }
        ],
        level: [
          { required: true, message: '请输入水库工程等级', trigger: 'blur' }
        ],
        scale: [
          { required: true, message: '请输入水库工程规模', trigger: 'blur' }
        ],
        totalStorageCapacity: [
          { required: true, message: '请输入总库容', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        regulatingStorageCapacity: [
          { required: true, message: '请输入调节库容', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        deadStorage: [
          { required: true, message: '请输入死库容', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        designFloodLevel: [
          { required: true, message: '请输入设计洪水位', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        normalStorageLevel: [
          { required: true, message: '请输入正常蓄水位', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        deadWaterLevel: [
          { required: true, message: '请输入死水位', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: "请输入非负数", trigger: 'blur' }
        ],
        date: [{ required: true, message: '请输入建站年月', trigger: 'blur' }],
        manageUnit: [
          { required: true, message: '请输入管理单位', trigger: 'blur' }
        ],
        waterSupplyArea: [
          { required: true, message: '请输入供水范围', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    //获取数据列表
    async getReservoirList() {
      const { data: res } = await this.$http.get('/reservoir/list', {
        params: this.queryinfo
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.ReservoirList = res.data.records
      this.total = res.data.total
    },
    //获取不分页数据列表
    async getAllReservoirList() {
      const { data: res } = await this.$http.get('/reservoir/export-excel')
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.AllReservoirList = res.data
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get('/reservoir/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getReservoirList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getReservoirList()
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
    addReservoir() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/reservoir/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getReservoirList()
        this.getAllReservoirList()
      })
    },
    //编辑角色信息表单
    editReservoir() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/reservoir/update', {
          id: this.editForm.id,
          waterSupply: this.editForm.waterSupply,
          code: this.editForm.code,
          name: this.editForm.name,
          longitude: this.editForm.longitude,
          latitude: this.editForm.latitude,
          locate: this.editForm.locate,
          registrationNumber: this.editForm.registrationNumber,
          administrationDivision: this.editForm.administrationDivision,
          level: this.editForm.level,
          scale: this.editForm.scale,
          totalStorageCapacity: this.editForm.totalStorageCapacity,
          regulatingStorageCapacity: this.editForm.regulatingStorageCapacity,
          deadStorage: this.editForm.deadStorage,
          designFloodLevel: this.editForm.designFloodLevel,
          normalStorageLevel: this.editForm.normalStorageLevel,
          deadWaterLevel: this.editForm.deadWaterLevel,
          date: this.editForm.date,
          manageUnit: this.editForm.manageUnit,
          waterSupplyArea: this.editForm.waterSupplyArea
        })
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getReservoirList()
        this.getAllReservoirList()
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
      const { data: res } = await this.$http.post('/reservoir/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getReservoirList()
      this.getAllReservoirList()
    },
    //搜索
    async searchReservoir() {
      const { data: res } = await this.$http.get(
        '/reservoir/name',
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
      this.ReservoirList = res.data.records
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
          '水库数据列表报告.xlsx'
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
          '水库数据列表模板.xlsx'
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
      //关闭dialog
      this.importDialogVisible = false
      this.$refs.upload.submit()
      setTimeout(() => {
        this.getReservoirList()
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
      let url = '/reservoir/import-excel'
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const { data: res } = await this.$http.post(url, formData, config)
      if (res.code === 200) {
        this.$message.success('导入成功')
        this.getAllReservoirList()
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
    this.getReservoirList()
    this.getAllReservoirList()
  },
  mounted() { }
}
</script>

<style scoped>
#div1 {
  height: 100%;
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
