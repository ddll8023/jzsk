// API监控工具
import axios from 'axios'
import { testApiConnection, getBestApiURL } from '@/config/api'

class ApiMonitor {
  constructor() {
    this.currentApiUrl = null;
    this.isMonitoring = false;
    this.monitorInterval = null;
    this.failureCount = 0;
    this.maxFailures = 3; // 连续失败3次后切换API
  }

  // 开始监控
  startMonitoring() {
    if (this.isMonitoring) return;
    
    this.isMonitoring = true;
    this.currentApiUrl = axios.defaults.baseURL;
    
    console.log('🔍 开始API监控...');
    console.log('📡 当前API地址:', this.currentApiUrl);
    
    // 每30秒检查一次API状态
    this.monitorInterval = setInterval(() => {
      this.checkApiHealth();
    }, 30000);
  }

  // 停止监控
  stopMonitoring() {
    if (this.monitorInterval) {
      clearInterval(this.monitorInterval);
      this.monitorInterval = null;
    }
    this.isMonitoring = false;
    console.log('⏹️ 停止API监控');
  }

  // 检查API健康状态
  async checkApiHealth() {
    if (!this.currentApiUrl) return;
    
    try {
      const isHealthy = await testApiConnection(this.currentApiUrl);
      
      if (isHealthy) {
        // API正常，重置失败计数
        if (this.failureCount > 0) {
          console.log('✅ API恢复正常');
        }
        this.failureCount = 0;
      } else {
        // API异常，增加失败计数
        this.failureCount++;
        console.log(`⚠️ API异常 (${this.failureCount}/${this.maxFailures})`);
        
        // 连续失败达到阈值，尝试切换API
        if (this.failureCount >= this.maxFailures) {
          await this.switchToBestApi();
        }
      }
    } catch (error) {
      console.error('❌ API监控检查失败:', error);
      this.failureCount++;
      
      if (this.failureCount >= this.maxFailures) {
        await this.switchToBestApi();
      }
    }
  }

  // 切换到最佳API
  async switchToBestApi() {
    console.log('🔄 检测到API故障，尝试切换到最佳API...');
    
    try {
      const bestApiUrl = await getBestApiURL();
      
      if (bestApiUrl !== this.currentApiUrl) {
        console.log(`🔄 切换API地址: ${this.currentApiUrl} -> ${bestApiUrl}`);
        axios.defaults.baseURL = bestApiUrl;
        this.currentApiUrl = bestApiUrl;
        this.failureCount = 0;
        
        // 显示切换通知
        if (window.Vue && window.Vue.prototype.$message) {
          window.Vue.prototype.$message.info('系统已自动切换到最佳API地址');
        }
      } else {
        console.log('ℹ️ 当前API已是最佳选择');
      }
    } catch (error) {
      console.error('❌ API切换失败:', error);
    }
  }

  // 手动触发API检查
  async manualCheck() {
    console.log('🔍 手动检查API状态...');
    await this.checkApiHealth();
  }

  // 获取监控状态
  getStatus() {
    return {
      isMonitoring: this.isMonitoring,
      currentApiUrl: this.currentApiUrl,
      failureCount: this.failureCount,
      maxFailures: this.maxFailures
    };
  }
}

// 创建全局API监控实例
const apiMonitor = new ApiMonitor();

// 导出监控实例和工具函数
export default apiMonitor;

// 启动监控的函数
export const startApiMonitoring = () => {
  apiMonitor.startMonitoring();
};

// 停止监控的函数
export const stopApiMonitoring = () => {
  apiMonitor.stopMonitoring();
};

// 手动检查API状态的函数
export const checkApiHealth = () => {
  return apiMonitor.manualCheck();
};

// 获取监控状态的函数
export const getApiMonitorStatus = () => {
  return apiMonitor.getStatus();
}; 