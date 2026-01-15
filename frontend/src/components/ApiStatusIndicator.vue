<template>
  <div class="api-status-indicator" v-if="showIndicator">
    <div class="status-dot" :class="statusClass"></div>
    <span class="status-text">{{ statusText }}</span>
  </div>
</template>

<script>
import { getApiMonitorStatus } from '@/utils/apiMonitor'

export default {
  name: 'ApiStatusIndicator',
  data() {
    return {
      showIndicator: false,
      status: 'unknown', // unknown, healthy, unhealthy
      updateInterval: null
    }
  },
  computed: {
    statusClass() {
      return {
        'status-unknown': this.status === 'unknown',
        'status-healthy': this.status === 'healthy',
        'status-unhealthy': this.status === 'unhealthy'
      }
    },
    statusText() {
      switch (this.status) {
        case 'healthy': return 'API正常'
        case 'unhealthy': return 'API异常'
        default: return 'API检测中'
      }
    }
  },
  mounted() {
    // 延迟显示指示器，避免页面加载时闪烁
    setTimeout(() => {
      this.showIndicator = true;
      this.updateStatus();
      
      // 每10秒更新一次状态
      this.updateInterval = setInterval(() => {
        this.updateStatus();
      }, 10000);
    }, 2000);
  },
  beforeDestroy() {
    if (this.updateInterval) {
      clearInterval(this.updateInterval);
    }
  },
  methods: {
    updateStatus() {
      try {
        const monitorStatus = getApiMonitorStatus();
        if (monitorStatus.failureCount === 0) {
          this.status = 'healthy';
        } else if (monitorStatus.failureCount >= monitorStatus.maxFailures) {
          this.status = 'unhealthy';
        } else {
          this.status = 'unknown';
        }
      } catch (error) {
        console.error('更新API状态失败:', error);
      }
    }
  }
}
</script>

<style scoped>
.api-status-indicator {
  position: fixed;
  bottom: 20px;
  right: 20px;
  display: flex;
  align-items: center;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 8px 12px;
  border-radius: 20px;
  font-size: 12px;
  z-index: 1000;
  backdrop-filter: blur(10px);
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 8px;
  animation: pulse 2s infinite;
}

.status-unknown {
  background: #909399;
}

.status-healthy {
  background: #67c23a;
}

.status-unhealthy {
  background: #f56c6c;
}

.status-text {
  font-size: 12px;
  font-weight: 500;
}

@keyframes pulse {
  0% { opacity: 1; }
  50% { opacity: 0.5; }
  100% { opacity: 1; }
}
</style> 