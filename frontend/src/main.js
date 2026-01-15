import Vue from 'vue';
import App from './App';
import qs from 'qs';
import router from './router';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import JsonExcel from 'vue-json-excel'
import ExportJsonExcel from "js-export-excel";
import echarts from "echarts"
import '../static/js/webVideoCtrl'
import $ from 'jquery';
import { getApiBaseURL, getBestApiURL } from './config/api'
import { checkNetworkConfig } from './utils/networkCheck'
import { startApiMonitoring } from './utils/apiMonitor'
// import VueAxios from 'vue-axios'

import './assets/font_8nkoq6t8imk/iconfont.css'
import './assets/font_lf6omvgdibc/iconfont.css'
//导入全局样式表
import './assets/css/global.css'
import './assets/theme.css'



Vue.prototype.$echarts = echarts


//使用路由和UI
Vue.use(router);
Vue.use(ElementUI);
// Vue.use(VueAxios, axios);

Vue.component('downloadExcel', JsonExcel)

// 智能配置API地址
const initApiConfig = async () => {
  try {
    console.log('开始智能选择API地址...');
    const apiBaseURL = await getBestApiURL();
    console.log('✅ 智能选择的API Base URL:', apiBaseURL);
    console.log('📍 当前访问地址:', window.location.href);
    console.log('🌐 主机名:', window.location.hostname);
    console.log('🔌 端口:', window.location.port);
    axios.defaults.baseURL = apiBaseURL;

    // 显示成功消息
    console.log('🎉 API配置成功，系统已自动选择最佳API地址');

    // 启动API监控
    console.log('🚀 启动API监控...');
    startApiMonitoring();
  } catch (error) {
    console.error('❌ API配置失败，使用默认配置:', error);
    const fallbackURL = getApiBaseURL();
    console.log('🔄 使用备用API Base URL:', fallbackURL);
    axios.defaults.baseURL = fallbackURL;

    // 即使失败也启动监控
    console.log('🚀 启动API监控...');
    startApiMonitoring();
  }
};

// 立即初始化API配置，并在完成后再挂载应用，避免刷新时 baseURL 未就绪导致接口走错
const bootstrap = async () => {
  await initApiConfig();
  /* 挂载应用 */
  new Vue({
    el: '#app',
    router,
    render: h => h(App)
  })
  // 启动网络配置检查
  checkNetworkConfig();
}

bootstrap();
//配置请求头
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'

// 添加请求拦截器
axios.interceptors.request.use(config => {
  console.log('API请求:', (config.method || 'GET').toUpperCase(), config.url);
  console.log('请求头:', config.headers);
  config.headers.Authorization = window.sessionStorage.getItem('token')
  return config
}, error => {
  console.error('请求错误:', error);
  return Promise.reject(error);
})

// 添加响应拦截器
axios.interceptors.response.use(response => {
  console.log('API响应:', response.status, response.config.url);
  return response;
}, error => {
  console.error('API响应错误:', error.response ? error.response.status : 'unknown', error.response ? error.response.data : 'unknown');
  console.error('错误详情:', error.message);
  return Promise.reject(error);
})
Vue.prototype.$http = axios
Vue.prototype.$qs = qs

export const baseURL = getApiBaseURL();
