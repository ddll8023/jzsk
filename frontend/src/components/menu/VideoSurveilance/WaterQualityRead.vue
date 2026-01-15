<template>
    <div id="div1">
        <div id="bread">
            <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
                <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>实时监测</el-breadcrumb-item>
                <el-breadcrumb-item :to="{ path: '/home/waterqualityread' }">水质监测</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <div id="div-header">
            <span style="align-self:center;font-size:15px; margin-left:20px">监测点：</span>
            <el-select v-model="position" clearable placeholder="请选择" style="align-self:center;"
                @change="selectWaterQualityStatistics">
                <el-option v-for="item in reservoirsList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
            </el-select>

            <span style="margin-left:10px ;align-self:center; font-size:14px">监测时间：</span>
            <el-date-picker v-model="datepickers" type="datetimerange" range-separator="-" start-placeholder="开始日期"
                end-placeholder="结束日期" align="right" value-format="yyyy-MM-dd HH:mm:ss"
                style="margin-left: 10px;align-self:center;" @change="selectWaterQualityStatistics"
                @clear="selectWaterQualityStatistics">
            </el-date-picker>

            <el-button type="primary" round icon="iconfont icon-icon-test" style="margin-left:auto; align-self:center;margin-right:10px"
                @click="exportExcel">导出</el-button>
        </div>

        <div id="div-main">
            <div id="div-main1">
                <!-- 设置interval来设置自动切换的时间间隔，单位为毫秒 -->
                <el-carousel type="card" height="300px" :autoplay="true" :interval="5000">
                    <!-- 动态生成走马灯项目 -->
                    <el-carousel-item v-for="(chartId, index) in charts" :key="index">
                        <div :id="'chart' + index" class="div-index"></div>
                    </el-carousel-item>
                </el-carousel>
            </div>
            <div id="div-main2">
                <el-table :data="WQList" border stripe height="calc(100% - 20px)" style="width: 100%"
                    :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
                    <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
                    </el-table-column>
                    <el-table-column prop="monitorTime" label="监测时间" width="160" align="center">
                    </el-table-column>
                    <el-table-column prop="position" label="监测点" width="80" align="center">
                    </el-table-column>
                    <el-table-column prop="code" label="站码" width="120" align="center">
                    </el-table-column>
                    <el-table-column prop="swt" label="水温(℃)" align="center">
                    </el-table-column>
                    <el-table-column prop="zd" label="浊度(NTU)" align="center">
                    </el-table-column>
                    <el-table-column prop="ph" label="PH" align="center">
                    </el-table-column>
                    <el-table-column prop="ddl" label="电导率(uS/cm)" align="center">
                    </el-table-column>
                    <el-table-column prop="rjy" label="溶解氧(mg/L)" align="center">
                    </el-table-column>
                    <el-table-column prop="ad" label="氨氮(mg/L)" align="center">
                    </el-table-column>
                    <el-table-column prop="cod" label="化学需氧量(mg/L)" align="center">
                    </el-table-column>
                    <el-table-column prop="yl" label="余氯(mg/L)" align="center">
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
                action="/water-quality/import-excel" :file-list="fileList" :auto-upload="false" :on-change="fileChange"
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
        <el-table id="table" :data="ALLWQList" border stripe height="100%" style="width: 100%;display:none;"
            :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
            <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
            </el-table-column>
            <el-table-column prop="monitorTime" label="监测时间" width="160" align="center">
            </el-table-column>
            <el-table-column prop="position" label="监测点" align="center">
            </el-table-column>
            <el-table-column prop="code" label="站码" align="center">
            </el-table-column>
            <el-table-column prop="swt" label="水温(℃)" align="center">
            </el-table-column>
            <el-table-column prop="zd" label="浊度(NTU)" align="center">
            </el-table-column>
            <el-table-column prop="ph" label="PH" align="center">
            </el-table-column>
            <el-table-column prop="ddl" label="电导率(uS/cm)" align="center">
            </el-table-column>
            <el-table-column prop="rjy" label="溶解氧(mg/L)" align="center">
            </el-table-column>
            <el-table-column prop="ad" label="氨氮(mg/L)" align="center">
            </el-table-column>
            <el-table-column prop="cod" label="化学需氧量(mg/L)" align="center">
            </el-table-column>
            <el-table-column prop="yl" label="余氯(mg/L)" align="center">
            </el-table-column>
            
        </el-table>

        <!-- 导出数据模板 -->
        <el-table id="tableFormwork" :data="ExamplePI" border stripe height="100%" style="width: 100%;display:none;"
            :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
            <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
            </el-table-column>
            <el-table-column prop="monitorTime" label="监测时间" width="160" align="center">
            </el-table-column>
            <el-table-column prop="position" label="监测点" align="center">
            </el-table-column>
            <el-table-column prop="code" label="站码" align="center">
            </el-table-column>
            <el-table-column prop="swt" label="水温(℃)" align="center">
            </el-table-column>
            <el-table-column prop="zd" label="浊度(NTU)" align="center">
            </el-table-column>
            <el-table-column prop="ph" label="PH" align="center">
            </el-table-column>
            <el-table-column prop="ddl" label="电导率(uS/cm)" align="center">
            </el-table-column>
            <el-table-column prop="rjy" label="溶解氧(mg/L)" align="center">
            </el-table-column>
            <el-table-column prop="ad" label="氨氮(mg/L)" align="center">
            </el-table-column>
            <el-table-column prop="cod" label="化学需氧量(mg/L)" align="center">
            </el-table-column>
            <el-table-column prop="yl" label="余氯(mg/L)" align="center">
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
            charts: [
                'chart1', 'chart2', 'chart3', 'chart4',
                'chart5', 'chart6', 'chart7', 'chart8'
            ], // 图表的DOM元素ID数组
            dateTimeList: [],
            temperatureList: [],
            turbidityList: [],
            phList: [],
            conductivityList: [],
            oxygenList: [],
            nitrogenList: [],
            codList: [],
            chlorineList: [],
            //水库名称列表
            reservoirs: [],
            reservoirsList: [],
            //数据列表
            WQList: [],
            ALLWQList: [],
            total: 0,
            //导入导出数据
            importDialogVisible: false,
            fileList: [],
            ExamplePI: [
                {
                    position: "水库1",
                    code: "00000001",
                    swt: '35.6',
                    zd: '5.00',
                    ph: '7.50',
                    ddl: '500.00',
                    rjy: '7.50',
                    ad: '0.30',
                    cod: '50.00',
                    yl: '0.20',
                    monitorTime: '2023-02-24 10:43:17'
                }
            ],
        }
    },
    methods: {
        //查询所有水库名称，提供下拉框选项
        async selectReservior() {
            const { data: res } = await this.$http.get('/dict/kinds', {
                params: {
                    name: '水质监测点'
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
            this.selectWaterQualityStatistics()
        },
        //获取数据列表
        async selectWaterQualityStatistics() {
            if (!this.datepickers || this.datepickers.length === 0) {
                this.initDate()
            }
            const { data: res } = await this.$http.get('/water-quality/statistics', {
                params: {
                    position: this.position,
                    startTime: this.datepickers[0],
                    endTime: this.datepickers[1],
                }
            })
            if (res.code !== 200) {
                return this.$message.error('获取数据列表失败')
            }
            this.temperatureList = res.data.temperatureList
            this.turbidityList = res.data.turbidityList
            this.phList = res.data.phList
            this.conductivityList = res.data.conductivityList
            this.oxygenList = res.data.oxygenList
            this.nitrogenList = res.data.nitrogenList
            this.codList = res.data.codList
            this.chlorineList = res.data.chlorineList
            this.setDateList()
            this.selectWaterQualityList()
            this.initCharts();
            this.selectAllWaterQualities()
        },
        //查询监测记录
        async selectWaterQualityList() {
            const { data: res } = await this.$http.get(
                '/water-quality/list',
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
            this.WQList = res.data.records
            this.total = res.data.total
        },
        //获取不分页数据列表
        async selectAllWaterQualities() {
            const { data: res } = await this.$http.get('/water-quality/export-excel',
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
            this.ALLWQList = res.data
        },
        //监听pageSize
        handleSizeChange(newSize) {
            this.queryinfo.pageSize = newSize
            this.selectWaterQualityList()
        },
        //监听currentPage
        handleCurrentChange(newPage) {
            this.queryinfo.currentPage = newPage
            this.selectWaterQualityList()
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
            const temperatureListLength = this.temperatureList.length;
            if (temperatureListLength === 0) {
                return;
            }
            const interval = timeDiff / (temperatureListLength - 1);
            this.dateTimeList = [];
            function* generateTimes(start, end, interval) {
                let currentTime = start;
                while (currentTime <= end) {
                    yield currentTime;
                    currentTime = new Date(currentTime.getTime() + interval);
                }
            }
            const timesGenerator = generateTimes(startTime, endTime, interval);
            this.dateTimeList = Array.from({ length: temperatureListLength }, (_, index) => {
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
        // 初始化所有图表
        initCharts() {
            this.charts.forEach((chartId, index) => {
                if (index == 7) {
                    chartId = 'chart0';
                    var myChart = echarts.init(document.getElementById(chartId));
                    var option = this.getChartOption(index); // 使用正确的索引值
                    myChart.setOption(option);
                }
                var myChart = echarts.init(document.getElementById(chartId));
                var option = this.getChartOption(index); // 使用正确的索引值
                myChart.setOption(option);
            });
            window.addEventListener('resize', () => {
                myChart.resize();
            });
        },
        // 根据图表索引获取图表配置项
        getChartOption(index) {
            const seriesData = this.getChartDataByIndex(index);
            const titleText = this.getChartTitleByIndex(index);
            const unitMap = {
                '水温统计': '°C',
                '浊度统计': 'NTU',
                'pH统计': '',
                '电导率统计': 'uS/cm',
                '溶解氧统计': 'mg/L',
                '氨氮统计': 'mg/L',
                '化学需氧量统计': 'mg/L',
                '余氯统计': 'mg/L'
            };
            const unit = unitMap[titleText];
            const legendName = titleText.replace('统计', ''); // 去除图例中的“统计”

            return {
                title: {
                    text: titleText // 保留标题中的“统计”
                },
                legend: {
                    data: [legendName] // 图例显示的数据列表，去除“统计”
                },
                tooltip: {
                    trigger: 'axis',
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
                    name: (unit ? `（单位：${unit}）` : '') // 为y轴名称添加单位，去除“统计”
                },
                series: [{
                    name: legendName, // 系列名称，去除“统计”
                    type: 'line', // 使用折线图
                    data: seriesData,
                    smooth: true
                }]
            };
        },
        // 根据索引获取图表数据
        getChartDataByIndex(index) {
            const dataList = [
                this.temperatureList, this.turbidityList, this.phList,
                this.conductivityList, this.oxygenList, this.nitrogenList,
                this.codList, this.chlorineList
            ];
            return dataList[index];
        },
        // 根据索引获取图表标题
        getChartTitleByIndex(index) {
            const titleList = [
                '水温统计', '浊度统计', 'pH统计', '电导率统计',
                '溶解氧统计', '氨氮统计', '化学需氧量统计', '余氯统计'
            ];
            return titleList[index];
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
                    '水质监测数据模板.xlsx'
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
                this.selectAllWaterQualities()
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
                    '水质监测数据纪录报告.xlsx'
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
            let url = '/water-quality/import-excel'
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
        // this.selectAllWaterQualities()
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
    display: block;
    /* 修改为 block，因为 carousel 已经是 flex 布局 */
    overflow: hidden;
}

#div-main2 {
  background-color: rgb(255, 255, 255);
  height: calc(100% - 300px);
  width: 100%;
  display: flex;
}

/* 为走马灯容器设置基本样式 */
#div-main1 .el-carousel__container {
    overflow: hidden;
}

/* 为走马灯的幻灯片设置基本样式 */
.div-index {
    width: 100%;
    height: 300px;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
}

/* 为ECharts容器设置样式，确保其在.div-index内居中 */
.echarts-container {
    width: 80%;
    /* 或其他宽度，根据需要调整 */
    height: 80%;
    /* 或其他高度，根据需要调整 */
    transition: transform 0.5s ease, opacity 0.5s ease;
}

/* 给非激活的轮播项添加轻微的模糊效果 */
.el-carousel__item {
    opacity: 0.8;
    /* 降低透明度，但不要太低 */
    filter: blur(2px);
    /* 轻微的模糊效果 */
    transform: scale(0.95);
    /* 轻微的缩放效果 */
    transition: opacity 0.5s ease, filter 0.5s ease, transform 0.5s ease;
}

/* 给激活的轮播项添加样式，取消模糊和透明度效果 */
.el-carousel__item.is-active {
    opacity: 1;
    filter: blur(0);
    transform: scale(1);
}

#bread {
    height: 25px;
    width: 100%;
}

.upload-demo {
    margin: 10px 200px;
}

#div-footer {
    height: 35px;
    width: 100%;
    float: left;
    text-align: center;
    background-color: rgb(245, 237, 230);
}
</style>