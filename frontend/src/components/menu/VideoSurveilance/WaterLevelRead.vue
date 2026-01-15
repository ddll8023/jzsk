<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>实时监测</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/waterlevelread' }">水位监测</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-header">
      <span style="align-self:center;font-size:15px; margin-left:20px">监测点：</span>
      <el-select v-model="position" clearable placeholder="请选择" style="align-self:center;"
        @change="selectWaterLevelStatistics">
        <el-option v-for="item in reservoirsList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <span style="margin-left:10px ;align-self:center; font-size:14px">监测时间：</span>
      <el-date-picker v-model="datepickers" type="datetimerange" range-separator="-" start-placeholder="开始日期"
        end-placeholder="结束日期" align="right" value-format="yyyy-MM-dd HH:mm:ss"
        style="margin-left: 10px;align-self:center;" @change="selectWaterLevelStatistics"
        @clear="selectWaterLevelStatistics">
      </el-date-picker>


      <el-button type="primary" round icon="iconfont icon-icon-test" style="margin-left:auto;align-self:center;margin-right:10px"
        @click="exportExcel">导出</el-button>
    </div>

    <div id="div-main">
      <div id="div-main1">
        <div id="chart1" class="div-index"></div>
      </div>
      <div id="div-main2">
        <el-table :data="WLList" border stripe height="calc(100% - 20px)" style="width: 100%"
          :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
          <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
          </el-table-column>
          <el-table-column prop="monitorTime" label="监测时间" align="center">
          </el-table-column>
          <el-table-column prop="position" label="监测点" align="center">
          </el-table-column>
          <el-table-column prop="code" label="站码" align="center">
          </el-table-column>
          <el-table-column prop="value" label="监测值(m)" align="center">
          </el-table-column>
        </el-table>
      </div>
    </div>

    <div id="div-footer">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 导入框 -->
    <el-dialog :visible.sync="importDialogVisible" width="800px" :modal="false" :close-on-press-escape="false"
      :close-on-click-modal="false">
      <template slot="title">
        <div style="border-bottom: 1px solid #ebebeb;font-size:20px;padding-bottom:10px">
          导入Excel
        </div>
      </template>
      <el-upload class="upload-demo" drag ref="upload" accept=".xlsx,.xls" name="file"
        action="/water-level/import-excel" :file-list="fileList" :auto-upload="false" :on-change="fileChange"
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
    <el-table id="table" :data="ALLWLList" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="50px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="monitorTime" label="监测时间" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="position" label="监测点" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="code" label="站码" align="center" width="120px">
      </el-table-column>
      <el-table-column prop="value" label="监测值(m)" align="center" width="120px">
      </el-table-column>
    </el-table>

    <!-- 导出数据模板 -->
    <el-table id="tableFormwork" :data="ExamplePI" border stripe height="100%" style="width: 100%;display:none;"
      :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
      <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index">
      </el-table-column>
      <el-table-column prop="monitorTime" label="监测时间" align="center">
      </el-table-column>
      <el-table-column prop="position" label="监测点" align="center">
      </el-table-column>
      <el-table-column prop="code" label="站码" align="center">
      </el-table-column>
      <el-table-column prop="value" label="监测值(m)" align="center">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import echarts from 'echarts'
import FileSaver from 'file-saver'
import XLSX from 'xlsx'

export default {
  name: '',
  data() {
    return {
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      position: '',
      datepickers: ['', ''],
      //统计数据
      dateTimeList: [],
      designFloodList: [],
      normalStorageList: [],
      deadWaterList: [],
      valueList: [],
      //水库名称列表
      reservoirs: [],
      reservoirsList: [],
      //数据列表
      WLList: [],
      ALLWLList: [],
      total: 0,
      //导入导出数据
      importDialogVisible: false,
      fileList: [],
      ExamplePI: [
        {
          position: "水库1",
          code: "00000001",
          value: '10.24',
          monitorTime: '2023-02-24 10:43:17'
        }
      ],
    }
  },
  methods: {
    //查询所有水库名称，提供下拉框选项
    async selectReservior() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params:{
          name:'水位监测点'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.reservoirs = res.data
      if (this.reservoirs && this.reservoirs.length > 0) {
        this.position = this.reservoirs[0]; // 假设reservoirs[0]是一个对象或字符串等可以直接赋值的值  
      }
      this.reservoirsList = this.reservoirs.map(name => ({
        value: name,
        label: name
      }))
      this.selectWaterLevelStatistics()
    },
    //获取数据列表
    async selectWaterLevelStatistics() {
      if (!this.datepickers || this.datepickers.length === 0) {
        this.initDate()
      }
      const { data: res } = await this.$http.get('/water-level/statistics', {
        params: {
          position: this.position,
          startTime: this.datepickers[0],
          endTime: this.datepickers[1],
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.designFloodList = res.data.designFloodList
      this.normalStorageList = res.data.normalStorageList
      this.deadWaterList = res.data.deadWaterList
      this.valueList = res.data.valueList
      this.setDateList()
      this.setChart1()
      this.selectWaterLevelList()
      this.selectAllWaterLevels()
    },
    //查询监测记录
    async selectWaterLevelList() {
      const { data: res } = await this.$http.get(
        '/water-level/list',
        {
          params: {
            currentPage: this.queryinfo.currentPage,
            pageSize: this.queryinfo.pageSize,
            position: this.position,
            startTime: this.datepickers[0],
            endTime: this.datepickers[1],
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.WLList = res.data.records
      this.total = res.data.total
      console.log(this.total)
    },
    //获取不分页数据列表
    async selectAllWaterLevels() {
      const { data: res } = await this.$http.get('/water-level/export-excel',
        {
          params: {
            position: this.position,
            startTime: this.datepickers[0],
            endTime: this.datepickers[1],
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.ALLWLList = res.data
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.selectWaterLevelList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.selectWaterLevelList()
    },
    //序号连续
    table_index(index) {
      return (
        (this.queryinfo.currentPage - 1) * this.queryinfo.pageSize + index + 1
      )
    },
    //得到横坐标的日期
    setDateList() {
      // 假设 datepickers 是一个包含两个日期字符串的数组  
      const startTime = new Date(this.datepickers[0]); // 开始时间  
      let endTime = new Date(this.datepickers[1]); // 结束时间  
      // 格式化日期和时间到特定格式  
      function formatDateTime(date) {
        // 这里使用 'yyyy-MM-dd HH:mm:ss' 格式作为示例  
        const year = date.getFullYear().toString().padStart(4, '0');
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        const seconds = date.getSeconds().toString().padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      }
      // 判断是否应该使用当前时间作为 endTime  
      function shouldUseCurrentTime() {
        return endTime > new Date(); // 比较 endTime 是否大于当前时间  
      }
      // 如果 endTime 大于当前时间，则使用当前时间作为 endTime  
      if (shouldUseCurrentTime()) {
        endTime = new Date();
      }
      // 计算时间差（以毫秒为单位）  
      const timeDiff = endTime - startTime;
      // 假设 valueList 已经存在且其长度是已知的  
      // 如果没有 valueList 或其长度为 0，则需要相应的处理  
      const valueListLength = this.valueList.length;
      // 如果 valueListLength 为 0，则不执行后续操作  
      if (valueListLength === 0) {
        return;
      }
      // 计算每个时间点之间的时间间隔（以毫秒为单位）  
      const interval = timeDiff / (valueListLength - 1);
      // 初始化 dateTimeList  
      this.dateTimeList = [];
      // 使用生成器函数和 Array.from 创建时间点数组  
      function* generateTimes(start, end, interval) {
        let currentTime = start;
        while (currentTime <= end) {
          yield currentTime;
          currentTime = new Date(currentTime.getTime() + interval);
        }
      }
      const timesGenerator = generateTimes(startTime, endTime, interval);
      this.dateTimeList = Array.from({ length: valueListLength }, (_, index) => {
        // 注意：由于生成器可能产生超过valueListLength的时间点，我们需要手动控制索引  
        const currentTime = timesGenerator.next().value;
        return formatDateTime(currentTime);
      });
    },
    //初始化起止时间为当前时间48之前-当前时间
    initDate() {
      const now = new Date();
      const twoDaysAgo = new Date(now.getTime() - 2 * 24 * 60 * 60 * 1000);

      // 定义一个函数来格式化日期和时间  
      function formatDateTime(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      }
      this.datepickers = [formatDateTime(twoDaysAgo), formatDateTime(now)];
    },
    //画图
    setChart1() {
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById('chart1'))
      // 指定图表的配置项和数据
      var option = {
        title: {
          text: '水位统计'
        },
        legend: { // 添加图例  
          data: ['监测值', '设计洪水位', '正常蓄水位', '死水位'] // 图例的数据列表，应与 series 中的 name 对应  
        }, 
        tooltip: {
          trigger: 'axis', // 触发类型，可选为：'item'、'axis'  
          formatter: function (params) { // 自定义提示框内容  
            let item = params[0]; // 获取第一个（也是唯一的一个）数据点
            // 构建 tooltip 的内容
            let content = `监测时间: ${item.axisValue}<br/>`;
            // 添加系列名称和值
            content += `${item.seriesName}: ${item.value}`;
            return content; // 返回构建好的 tooltip 内容
          }
        },
        xAxis: {
          type: 'category',
          data: this.dateTimeList,
          name: '时间'
        },
        yAxis: {
          type: 'value',
          name: '水位(m)'
        },
        series: [
          {
            name: '监测值',
            data: this.valueList,
            type: 'line',
            smooth: true
          },
          {
            name: '设计洪水位',
            data: this.designFloodList,
            type: 'line',
            smooth: true
          },
          {
            name: '正常蓄水位',
            data: this.normalStorageList,
            type: 'line',
            smooth: true
          },
          {
            name: '死水位',
            data: this.deadWaterList,
            type: 'line',
            smooth: true
          }
        ]
      };
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option)
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
          '水位监测数据模板.xlsx'
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
        this.initDate()
        this.selectReservior()
        this.selectAllWaterLevels()
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
          '水位监测数据纪录报告.xlsx'
        )
      } catch (e) {
        if (typeof console !== 'undefined') {
          console.log(e, wbout)
        }
      }
      return wbout
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
      let url = '/water-level/import-excel'
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
    this.initDate()
    this.selectReservior()
    // this.selectAllWaterLevels()
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

#div-main1 {
  background-color: rgb(255, 255, 255);
  height: 300px;
  width: 100%;
  display: flex;
}

#div-main2 {
  background-color: rgb(255, 255, 255);
  height: calc(100% - 300px);
  width: 100%;
  display: flex;
}

#div-footer {
  height: 35px;
  width: 100%;
  float: left;
  text-align: center;
  background-color: rgb(245, 237, 230);
}

.div-index {
  height: 280px;
  width: 100%;
  margin: 10px 20px 10px 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}

#bread {
  height: 25px;
  width: 100%;
}
.upload-demo {
  margin: 10px 200px;
}
</style>