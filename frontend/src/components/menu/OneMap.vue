<template>
  <div :id="divId">
    <div :id="mapId" class="map"></div>
    <div :id="popupId" class="ol-popup">
      <a href="#" :id="closerId" class="ol-popup-closer">X</a>
      <div :id="contentId" class="popup-content"></div>
    </div>
    <div class="aside" @click="drawer = true">水务总览</div>
    <!-- <el-button @click="drawer = true" class="aside"></el-button> -->

    <div id="buttonDiv">
      <!-- 数据展示1 -->
      <el-popover
        placement="bottom"
        width="400"
        trigger="click"
        style="margin-left:5%;"
      >
        <el-table :data="gridData" height="200px">
          <el-table-column
            width="150"
            property="date"
            label="日期"
          ></el-table-column>
          <el-table-column
            width="100"
            property="name"
            label="姓名"
          ></el-table-column>
          <el-table-column
            width="150"
            property="address"
            label="地址"
          ></el-table-column>
        </el-table>
        <el-button slot="reference" class="button" round>数据1</el-button>
      </el-popover>

      <!-- 数据展示2 -->
      <el-popover
        placement="bottom"
        width="400"
        trigger="click"
        style="margin-left:10px;"
      >
        <el-table :data="gridData" height="200px">
          <el-table-column
            width="150"
            property="date"
            label="日期"
          ></el-table-column>
          <el-table-column
            width="100"
            property="name"
            label="姓名"
          ></el-table-column>
          <el-table-column
            width="150"
            property="address"
            label="地址"
          ></el-table-column>
        </el-table>
        <el-button slot="reference" class="button" round>数据2</el-button>
      </el-popover>

      <!-- 数据展示3 -->
      <el-popover
        placement="bottom"
        width="400"
        trigger="click"
        style="margin-left:10px;"
      >
        <el-table :data="gridData" height="200px">
          <el-table-column
            width="150"
            property="date"
            label="日期"
          ></el-table-column>
          <el-table-column
            width="100"
            property="name"
            label="姓名"
          ></el-table-column>
          <el-table-column
            width="150"
            property="address"
            label="地址"
          ></el-table-column>
        </el-table>
        <el-button slot="reference" class="button" round>数据3</el-button>
      </el-popover>

      <!-- 数据展示4 -->
      <el-popover
        placement="bottom"
        width="400"
        trigger="click"
        style="margin-left:10px;"
      >
        <el-table :data="gridData" height="200px">
          <el-table-column
            width="150"
            property="date"
            label="日期"
          ></el-table-column>
          <el-table-column
            width="100"
            property="name"
            label="姓名"
          ></el-table-column>
          <el-table-column
            width="150"
            property="address"
            label="地址"
          ></el-table-column>
        </el-table>
        <el-button slot="reference" class="button" round>数据4</el-button>
      </el-popover>
    </div>

    <!-- 图层切换 -->
    <el-dropdown style="position:absolute;left:1700px;top:80px">
      <div id="layer1">底图切换</div>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="change_vec">矢量图</el-dropdown-item>
        <el-dropdown-item @click.native="change_ter">地形图</el-dropdown-item>
        <el-dropdown-item @click.native="change_img">影像图</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <!-- 图层选择 -->
    <el-dropdown style="position:absolute;left:1600px;top:80px">
      <div id="layer2">图层选择</div>
      <el-dropdown-menu
        slot="dropdown"
        style="width:320px;box-shadow:0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);"
      >
        <div id="layers">
          <div style="margin-top: 10px">
            <el-checkbox
              v-model="checked1"
              label="泵站"
              border
              size="small"
              style="width:120px;"
              @change="changePumpStation"
            ></el-checkbox>
            <el-checkbox
              v-model="checked2"
              label="地下水"
              border
              size="small"
              style="width:120px"
              @change="changeGroundSourceWater"
            ></el-checkbox>
          </div>
          <div style="margin-top: 10px">
            <el-checkbox
              v-model="checked3"
              label="单独流量测站"
              border
              size="small"
              style="width:120px"
              @change="changeIndividualFlowSites"
            ></el-checkbox>
            <el-checkbox
              v-model="checked4"
              label="蓄水池"
              border
              size="small"
              style="width:120px"
              @change="changeImpoundment"
            ></el-checkbox>
          </div>
          <div style="margin-top: 10px">
            <el-checkbox
              v-model="checked5"
              label="单独压力测站"
              border
              size="small"
              style="width:120px"
              @change="changeIndividualPressureSites"
            ></el-checkbox>
            <el-checkbox
              v-model="checked6"
              label="巡检"
              border
              size="small"
              style="width:120px"
              @change="changeInspectionRecords"
            ></el-checkbox>
          </div>
          <div style="margin-top: 10px">
            <el-checkbox
              v-model="checked7"
              label="水库"
              border
              size="small"
              style="width:120px"
              @change="changeReservoir"
            ></el-checkbox>
            <el-checkbox
              v-model="checked8"
              label="管道"
              border
              size="small"
              style="width:120px"
              @change="changeLines"
            ></el-checkbox>
          </div>
        </div>
      </el-dropdown-menu>
    </el-dropdown>

    <!-- 水务总览 -->
    <el-drawer
      :visible.sync="drawer"
      :modal="false"
      direction="ltr"
      size="20%"
      @close="drawerClosed"
      :wrapperClosable="false"
      style="margin-top:100px;height:800px;"
    >
    </el-drawer>

    <!-- 工程简介按钮 -->
    <div class="project-intro" @click="showProjectIntro">
      <i class="el-icon-document"></i>
      <span>工程简介</span>
    </div>

    <!-- 工程简介弹窗 -->
    <el-dialog
      title="荆竹水库工程简介"
      :visible.sync="projectIntroVisible"
      width="50%"
      :before-close="handleClose"
    >
      <div class="project-intro-content">
        荆竹水库是武穴市余川镇大坝村的重要水利设施，距武穴市43公里，余川镇7公里，地理位置为东经115°41′28″，北纬30°07′45″。水库始建于1958年11月，1963年1月基本竣工，1975年冬季对主坝进行了加高培厚，2005年至2009年完成除险加固工程。水库控制流域面积56.7平方公里，多年平均降雨量1489.0毫米，总库容7710万立方米，正常蓄水位71.89米，死水位47.89米。枢纽工程包括主坝、副坝、溢洪道、输水隧洞、输水管、电站及水厂等主要建筑物。主坝为粘土心墙代料坝，最大坝高39.19米，副坝为粘土斜墙代料坝，最大坝高8.1米。主要建筑物防洪标准为100年一遇洪水设计，校核洪水位76.02米。荆竹水库设计灌溉面积7.5万亩，供水涉及3个乡镇、8万人口，电站装机2台1260千瓦，年发电量130万度。近年来，水库实施了信息化建设，包括水雨情测报、大坝安全监测、视频监视等系统，提升了管理水平和运行安全性。
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { FullScreen } from 'ol/control'
import 'ol/ol.css'
import { Tile as TileLayer } from 'ol/layer'
import Overlay from 'ol/Overlay'
import XYZ from 'ol/source/XYZ'
import Map from 'ol/Map.js'
import View from 'ol/View.js'
import TileWMS from 'ol/source/TileWMS'
import { Point, LineString } from 'ol/geom'
import Feature from 'ol/Feature'
import { Icon, Style, Stroke } from 'ol/style'
import { toStringHDMS } from "ol/coordinate"
import { toLonLat } from "ol/proj"

// 添加矢量底图
const tdtVECLayer = new TileLayer({
  source: new XYZ({
    url:
      'http://t0.tianditu.com/DataServer?T=vec_c&x={x}&y={y}&l={z}&tk=53e8dc8dc6768eae964528e059c5bca3',
    projection: 'EPSG:4326'
  }),
  zIndex: 0
})
// 添加矢量注记
const tdtCVALayer = new TileLayer({
  source: new XYZ({
    url:
      'http://t0.tianditu.com/DataServer?T=cva_c&x={x}&y={y}&l={z}&tk=53e8dc8dc6768eae964528e059c5bca3',
    projection: 'EPSG:4326'
  }),
  zIndex: 1
})
// 添加影像底图
const tdtIMGLayer = new TileLayer({
  source: new XYZ({
    url:
      'http://t0.tianditu.com/DataServer?T=img_c&x={x}&y={y}&l={z}&tk=53e8dc8dc6768eae964528e059c5bca3',
    projection: 'EPSG:4326'
  }),
  zIndex: 0
})
// 添加影像注记
const tdtCIALayer = new TileLayer({
  source: new XYZ({
    url:
      'http://t0.tianditu.com/DataServer?T=cia_c&x={x}&y={y}&l={z}&tk=53e8dc8dc6768eae964528e059c5bca3',
    projection: 'EPSG:4326'
  }),
  zIndex: 1
})
// 添加地形底图
const tdtTERLayer = new TileLayer({
  source: new XYZ({
    url:
      'http://t0.tianditu.com/DataServer?T=ter_c&x={x}&y={y}&l={z}&tk=53e8dc8dc6768eae964528e059c5bca3',
    projection: 'EPSG:4326'
  }),
  zIndex: 0
})
// 添加地形注记
const tdtCTALayer = new TileLayer({
  source: new XYZ({
    url:
      'http://t0.tianditu.com/DataServer?T=cta_c&x={x}&y={y}&l={z}&tk=53e8dc8dc6768eae964528e059c5bca3',
    projection: 'EPSG:4326'
  }),
  zIndex: 1
})

const Url = 'http://172.27.25.88:8083/geoserver/szy/wms'  //后端IP

export default {
  name: '',
  props: {
    mapId: {
      type: String,
      default: 'map'
    },
    divId: {
      type: String,
      default: 'div1'
    },
    popupId: {
      type: String,
      default: 'popup'
    },
    closerId: {
      type: String,
      default: 'popup-closer'
    },
    contentId: {
      type: String,
      default: 'popup-content'
    }
  },
  data() {
    return {
      overlay: null,
      container: null,  //弹框
      checked1: false,
      checked2: false,
      checked3: false,
      checked4: false,
      checked5: false,
      checked6: false,
      checked7: false,
      checked8: false,
      drawer: false,
      drawerDiv: false,
      map: null,
      parser: null,
      imagemap: null,
      gridData: [
        {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        },
        {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        },
        {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }
      ],
      projectIntroVisible: false,
    }
  },
  methods: {
    //初始地图
    initMap() {
      //弹框窗体 内容 关闭叉
      var container = document.getElementById(this.popupId);
      var content = document.getElementById(this.contentId);
      var closer = document.getElementById(this.closerId);


      //初始化地图对象
      this.map = new Map({
        //地图容器
        target: this.mapId,
        //视图
        view: new View({
          center: [113.37377, 31.717497], //中心点经纬度
          zoom: 14, //图层缩放大小
          maxZoom: 18,
          minZoom: 5,
          projection: 'EPSG:4326' //配置投影坐标系 'EPSG:4326'是百度地图的坐标系
        })
      })

      this.overlay = new Overlay({
          element: container, //绑定 Overlay 对象和 DOM 对象的
          autoPan: true, // 定义弹出窗口在边缘点击时候可能不完整 设置自动平移效果
          autoPanAnimation: {
              duration: 250 //自动平移效果的动画时间 9毫秒
          }
      });

      this.map.addOverlay(this.overlay);
      let _that = this;
      // 监听singleclick事件  整个地图单击展示坐标信息弹框
      this.map.on('singleclick', function(e) {
        let coordinate = e.coordinate
        // 点击尺 （这里是尺(米)，并不是经纬度）;
        let hdms = toStringHDMS(toLonLat(e.coordinate)); // 转换为经纬度显示
        content.innerHTML = `
        <p>你点击了这里：</p>
        <p>经纬度：<p><code> ${hdms}  </code> <p>
        <p>坐标：</p>X:${coordinate[0]} &nbsp;&nbsp; Y: ${coordinate[1]}`;
        _that.overlay.setPosition(coordinate); //把 overlay 显示到指定的 x,y坐标
        })
      //弹窗关闭事件
      closer.onclick=function(){
        _that.overlay.setPosition(undefined);
        closer.blur(); //失焦
        return false; //return false 防止关闭事件默认关闭窗口
      };
      //将图层添加到地图上
      this.map.addLayer(tdtVECLayer)
      this.map.addLayer(tdtCVALayer)
      //添加全屏组件
      this.map.addControl(new FullScreen())
    },
    //切换影像图
    change_img() {
      //清除底图
      this.map.removeLayer(tdtVECLayer)
      this.map.removeLayer(tdtCVALayer)
      this.map.removeLayer(tdtIMGLayer)
      this.map.removeLayer(tdtCIALayer)
      this.map.removeLayer(tdtTERLayer)
      this.map.removeLayer(tdtCTALayer)
      //将图层添加到地图上
      this.map.addLayer(tdtIMGLayer)
      this.map.addLayer(tdtCIALayer)
    },
    //切换地形图
    change_ter() {
      //清除底图
      this.map.removeLayer(tdtVECLayer)
      this.map.removeLayer(tdtCVALayer)
      this.map.removeLayer(tdtIMGLayer)
      this.map.removeLayer(tdtCIALayer)
      this.map.removeLayer(tdtTERLayer)
      this.map.removeLayer(tdtCTALayer)
      // var layersArray = map.getLayersArray()
      // layersArray.insertAt(0, tdtTERLayer)
      // layersArray.insertAt(1, tdtCTALayer)
      //将图层添加到地图上
      this.map.addLayer(tdtTERLayer)
      this.map.addLayer(tdtCTALayer)
    },
    //切换矢量图
    change_vec() {
      //清除底图
      this.map.removeLayer(tdtVECLayer)
      this.map.removeLayer(tdtCVALayer)
      this.map.removeLayer(tdtIMGLayer)
      this.map.removeLayer(tdtCIALayer)
      this.map.removeLayer(tdtTERLayer)
      this.map.removeLayer(tdtCTALayer)
      //将图层添加到地图上
      this.map.addLayer(tdtVECLayer)
      this.map.addLayer(tdtCVALayer)
    },
    //泵站注记
    changePumpStation: function(checked1, e) {
      // console.log(checked1)
      if (checked1) {
        this.pumpstation = new TileLayer({
          source: new TileWMS({
            url: Url,  //后端提供的wms服务
            wrapX: false,
            params: {
              FORMAT: 'image/png',  //指定返回图像格式
              VERSION: '1.1.1',   //wms版本
              tiled: true,
              STYLES: '',
              LAYERS: 'szy:pump_station',  //指定要加载的图层名称
              exceptions: 'application/vnd.ogc.se_inimage',  //返回错误格式的信息
              tilesOrigin: 73.33 + ',' + 3.51  //瓦片原地的位置
            }
          }),
          zIndex: 9999
        })
        this.map.addLayer(this.pumpstation, 1)
      } else {
        this.map.removeLayer(this.pumpstation)
      }
    },
    //地下水注记
    changeGroundSourceWater: function(checked2, e) {
      if (checked2) {
        this.groundSourceWater = new TileLayer({
          source: new TileWMS({
            url: Url,
            wrapX: false,
            params: {
              LAYERS: 'szy:ground_source_water',
              STYLES: '',
              VERSION: '1.1.1',
              FORMAT: 'image/png'
            },
            serverType: 'geoserver'
          }),
          zIndex: 9999
        })
        this.map.addLayer(this.groundSourceWater)
      } else {
        this.map.removeLayer(this.groundSourceWater)
      }
    },
    //单独流量测站注记
    changeIndividualFlowSites: function(checked3, e) {
      if (checked3) {
        this.individualFlowSites = new TileLayer({
          source: new TileWMS({
            url: Url,
            wrapX: false,
            params: {
              FORMAT: 'image/png',
              VERSION: '1.1.1',
              tiled: true,
              STYLES: '',
              LAYERS: 'szy:individual_flow_sites',
              exceptions: 'application/vnd.ogc.se_inimage',
              tilesOrigin: 73.33 + ',' + 3.51
            }
          }),
          zIndex: 9999
        })
        this.map.addLayer(this.individualFlowSites)
      } else {
        this.map.removeLayer(this.individualFlowSites)
      }
    },
    //蓄水池注记
    changeImpoundment: function(checked4, e) {
      if (checked4) {
        this.impoundment = new TileLayer({
          source: new TileWMS({
            url: Url,
            wrapX: false,
            params: {
              FORMAT: 'image/png',
              VERSION: '1.1.1',
              tiled: true,
              STYLES: '',
              LAYERS: 'szy:impoundment',
              exceptions: 'application/vnd.ogc.se_inimage',
              tilesOrigin: 73.33 + ',' + 3.51
            }
          }),
          zIndex: 9999
        })
        this.map.addLayer(this.impoundment)
      } else {
        this.map.removeLayer(this.impoundment)
      }
    },
    //单独压力测站
    changeIndividualPressureSites: function(checked5, e) {
      if (checked5) {
        this.individualPressureSites = new TileLayer({
          source: new TileWMS({
            url: Url,
            wrapX: false,
            params: {
              FORMAT: 'image/png',
              VERSION: '1.1.1',
              tiled: true,
              STYLES: '',
              LAYERS: 'szy:individual_pressure_sites',
              exceptions: 'application/vnd.ogc.se_inimage',
              tilesOrigin: 73.33 + ',' + 3.51
            }
          }),
          zIndex: 9999
        })
        this.map.addLayer(this.individualPressureSites)
      } else {
        this.map.removeLayer(this.individualPressureSites)
      }
    },
    //巡检
    changeInspectionRecords: function(checked6, e) {
      if (checked6) {
        this.inspectionRecords = new TileLayer({
          source: new TileWMS({
            url: Url,
            wrapX: false,
            params: {
              FORMAT: 'image/png',
              VERSION: '1.1.1',
              tiled: true,
              STYLES: '',
              LAYERS: 'szy:inspection_records',
              exceptions: 'application/vnd.ogc.se_inimage',
              tilesOrigin: 73.33 + ',' + 3.51
            }
          }),
          zIndex: 9999
        })
        this.map.addLayer(this.inspectionRecords)
      } else {
        this.map.removeLayer(this.inspectionRecords)
      }
    },
    //水库
    changeReservoir: function(checked7, e) {
      if (checked7) {
        this.reservoir = new TileLayer({
          source: new TileWMS({
            url: Url,
            wrapX: false,
            params: {
              FORMAT: 'image/png',
              VERSION: '1.1.1',
              tiled: true,
              STYLES: '',
              LAYERS: 'szy:reservoir',
              exceptions: 'application/vnd.ogc.se_inimage',
              tilesOrigin: 73.33 + ',' + 3.51
            }
          }),
          zIndex: 9999
        })
        this.map.addLayer(this.reservoir)
      } else {
        this.map.removeLayer(this.reservoir)
      }
    },
    //管道
    changeLines: function(checked8, e) {
      if (checked8) {
        this.lines = new TileLayer({
          source: new TileWMS({
            url: Url,
            wrapX: false,
            params: {
              FORMAT: 'image/png',
              VERSION: '1.1.1',
              tiled: true,
              STYLES: '',
              LAYERS: 'szy:lines',
              exceptions: 'application/vnd.ogc.se_inimage',
              tilesOrigin: 73.33 + ',' + 3.51
            }
          }),
          zIndex: 9999
        })
        this.map.addLayer(this.lines)
      } else {
        this.map.removeLayer(this.lines)
      }
    },
    drawerClosed() {},
    showProjectIntro() {
      this.projectIntroVisible = true;
    },
    handleClose(done) {
      done();
    }
  },
  created() {},
  mounted() {
    this.initMap()
  }
}
</script>

<style lang="less" scoped>
#div1 {
  width: 100%;
  height: 100%;
}
.map {
  width: 100%;
  height: 100%;
}

.ol-popup {
  position: absolute;
  background-color: white;
  -webkit-filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
  filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
  padding: 15px;
  border-radius: 10px;
  border: 1px solid #cccccc;
  bottom: 12px;
  left: -50px;
}
.popup-content {
  width: 400px;
}
.ol-popup-closer {
  text-decoration: none;
  position: absolute;
  top: 2px;
  right: 8px;
}
#map {
  width: 100%;
  height: 100%;
}
.aside {
  position: absolute;
  top: 50%;
  height: 100px;
  width: 30px;
  font-size: 18px;
  padding: 10px 10px 10px 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #409eff;
  border-radius: 5px;
  box-shadow: 0px 6px 10px #888888;
  cursor: pointer;
}
.aside:hover {
  color: #e5ecf3;
}
#span1 {
  font-size: 16px;
  width: 16px;
}
.button {
  height: 80px;
  width: 160px;
  opacity: 0.8;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}
#buttonDiv {
  position: absolute;
  left: 30%;
  top: 80px;
  width: 800px;
  height: 80px;
}
#layer1 {
  height: 30px;
  line-height: 30px;
  width: 80px;
  background-color: rgb(255, 255, 255);
  border: 1px solid #fff;
  box-shadow: 0px 6px 10px #888888;
  border-radius: 5px;
  cursor: pointer;
  text-align: center;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
}
#layer2 {
  height: 30px;
  line-height: 30px;
  width: 80px;
  background-color: rgb(255, 255, 255);
  border: 1px solid #fff;
  box-shadow: 0px 6px 10px #888888;
  border-radius: 5px;
  cursor: pointer;
  text-align: center;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
}
#layers {
  margin-left: 20px;
}
#layer:hover {
  color: #409eff;
}
.project-intro {
  position: absolute;
  right: 20px;
  top: 20px;
  background: rgba(255, 255, 255, 0.9);
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
  
  &:hover {
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
  }
  
  i {
    color: #409EFF;
    font-size: 18px;
  }
  
  span {
    color: #303133;
    font-size: 14px;
  }
}

.project-intro-content {
  line-height: 1.8;
  text-indent: 2em;
  color: #606266;
  font-size: 14px;
}
</style>
