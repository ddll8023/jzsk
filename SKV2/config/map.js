/**
 * 地图配置模块
 * 功能：天地图配置、水库坐标、地图初始参数
 */

export const TIANDITU_TOKEN = '53e8dc8dc6768eae964528e059c5bca3'

export const RESERVOIR_CENTER = {
  latitude: 30.128589,
  longitude: 115.698966,
}

export const MAP_CONFIG = {
  center: RESERVOIR_CENTER,
  zoom: 14,
  minZoom: 5,
  maxZoom: 18,
}

export const RESERVOIR_MARKER = {
  id: 1,
  ...RESERVOIR_CENTER,
  title: '武穴市荆竹水库',
  width: 32,
  height: 32,
}

export const RESERVOIR_INFO = {
  name: '武穴市荆竹水库',
  location: '北纬29°51′18″，东经115°33′36″',
  capacity: '5609万立方米',
  irrigationArea: '3.24万亩',
  buildYear: '1962年',
  functions: '防洪灌溉、生态供水',
}

export default {
  TIANDITU_TOKEN,
  RESERVOIR_CENTER,
  MAP_CONFIG,
  RESERVOIR_MARKER,
  RESERVOIR_INFO,
}
