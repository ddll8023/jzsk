<template>
  <div class="api-switcher">
    <div class="switcher-panel">
      <h4>API地址切换</h4>
      <div class="current-api">
        <strong>当前API地址:</strong> {{ currentApiURL }}
      </div>
      <div class="buttons">
        <button @click="switchToIntranet" class="btn-intranet">
          切换到内网API
        </button>
        <button @click="switchToPublic" class="btn-public">
          切换到公网API
        </button>
        <button @click="clearManual" class="btn-clear">
          清除手动设置
        </button>
      </div>
      <div class="info">
        <p><strong>内网API:</strong> http://192.168.20.3:8081</p>
        <p><strong>公网API:</strong> http://111.4.68.108:8081</p>
      </div>
    </div>
  </div>
</template>

<script>
import { setManualApiURL, clearManualApiURL } from '../config/api';

export default {
  name: 'ApiSwitcher',
  data() {
    return {
      currentApiURL: '检测中...'
    };
  },
  mounted() {
    this.updateCurrentApiURL();
  },
  methods: {
    updateCurrentApiURL() {
      const manualURL = sessionStorage.getItem('manual_api_url');
      const autoURL = sessionStorage.getItem('api_base_url');
      this.currentApiURL = manualURL || autoURL || '未设置';
    },
    switchToIntranet() {
      setManualApiURL('http://192.168.20.3:8081');
    },
    switchToPublic() {
      setManualApiURL('http://111.4.68.108:8081');
    },
    clearManual() {
      clearManualApiURL();
    }
  }
};
</script>

<style scoped>
.api-switcher {
  position: fixed;
  top: 10px;
  right: 10px;
  z-index: 9999;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 15px;
  border-radius: 8px;
  font-size: 12px;
  max-width: 300px;
}

.switcher-panel h4 {
  margin: 0 0 10px 0;
  color: #fff;
}

.current-api {
  margin-bottom: 10px;
  padding: 5px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  word-break: break-all;
}

.buttons {
  display: flex;
  flex-direction: column;
  gap: 5px;
  margin-bottom: 10px;
}

button {
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 11px;
  transition: background-color 0.3s;
}

.btn-intranet {
  background: #28a745;
  color: white;
}

.btn-intranet:hover {
  background: #218838;
}

.btn-public {
  background: #007bff;
  color: white;
}

.btn-public:hover {
  background: #0056b3;
}

.btn-clear {
  background: #6c757d;
  color: white;
}

.btn-clear:hover {
  background: #545b62;
}

.info {
  font-size: 10px;
  opacity: 0.8;
}

.info p {
  margin: 2px 0;
}
</style> 