// API配置文件
const getApiBaseURL = () => {
  const hostname = window.location.hostname;
  const port = window.location.port;
  
  // 检查是否有手动设置的API地址
  const manualApiURL = sessionStorage.getItem('manual_api_url');
  if (manualApiURL) {
    console.log('使用手动设置的API地址:', manualApiURL);
    return manualApiURL;
  }
  
  // 如果是本地开发环境
  if (hostname === 'localhost' || hostname === '127.0.0.1') {
    return 'http://localhost:8081';
  }
  
  // 如果是192.168.20开头的IP，使用内网API
  if (hostname.indexOf('192.168.20.') === 0) {
    return 'http://192.168.20.3:8081';
  }
  
  // 如果是公网IP访问，直接使用公网API
  if (hostname === '111.4.68.108') {
    return 'http://111.4.68.108:8081';
  }
  
  // 其他情况都使用公网API
  return 'http://111.4.68.108:8081';
};

// 测试API地址连通性
export const testApiConnection = async (apiUrl) => {
  try {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 2000); // 减少超时时间到2秒
    
    console.log(`🔍 测试API地址: ${apiUrl}`);
    
    const response = await fetch(`${apiUrl}/actuator/health`, {
      method: 'GET',
      signal: controller.signal,
      mode: 'cors',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    clearTimeout(timeoutId);
    
    if (response.ok) {
      console.log(`✅ API地址 ${apiUrl} 连接成功`);
      return true;
    } else {
      console.log(`❌ API地址 ${apiUrl} 响应异常: ${response.status}`);
      return false;
    }
  } catch (error) {
    console.log(`❌ API地址 ${apiUrl} 连接失败:`, error.message);
    return false;
  }
};

// 智能API地址选择
export const getBestApiURL = async () => {
  const hostname = window.location.hostname;
  
  // 检查是否有手动设置的API地址
  const manualApiURL = sessionStorage.getItem('manual_api_url');
  if (manualApiURL) {
    console.log('使用手动设置的API地址:', manualApiURL);
    return manualApiURL;
  }
  
  // 如果是本地开发环境
  if (hostname === 'localhost' || hostname === '127.0.0.1') {
    return 'http://localhost:8081';
  }
  
  // 如果是192.168.20开头的IP，直接使用内网API
  if (hostname.indexOf('192.168.20.') === 0) {
    return 'http://192.168.20.3:8081';
  }
  
  // 如果是公网IP访问，直接使用公网API
  if (hostname === '111.4.68.108') {
    console.log('检测到公网访问，使用公网API...');
    return 'http://111.4.68.108:8081';
  }
  
  // 其他情况，智能测试选择
  console.log('未知访问方式，智能测试API地址...');
  const apiUrls = [
    'http://192.168.20.3:8081',  // 内网API
    'http://111.4.68.108:8081',  // 公网API
    'http://localhost:8081'      // 本地API
  ];
  
  for (const apiUrl of apiUrls) {
    console.log(`测试API地址: ${apiUrl}`);
    const isAvailable = await testApiConnection(apiUrl);
    if (isAvailable) {
      console.log(`API地址 ${apiUrl} 可用，将使用此地址`);
      return apiUrl;
    }
  }
  
  // 都不可用，使用内网API作为默认
  console.log('所有API地址都不可用，使用默认内网地址');
  return 'http://192.168.20.3:8081';
};

// 手动设置API地址的函数
export const setManualApiURL = (url) => {
  sessionStorage.setItem('manual_api_url', url);
  console.log('手动设置API地址:', url);
  
  // 重新加载页面以应用新配置
  window.location.reload();
};

// 清除手动设置的API地址
export const clearManualApiURL = () => {
  sessionStorage.removeItem('manual_api_url');
  console.log('清除手动设置的API地址');
  
  // 重新加载页面以应用新配置
  window.location.reload();
};







// 视频监控API配置
const getVideoApiConfig = () => {
  const hostname = window.location.hostname;
  
  // 如果是本地开发环境
  if (hostname === 'localhost' || hostname === '127.0.0.1') {
    return {
      loginIp: '192.168.20.250',
      port: '80'
    };
  }
  
  // 如果是192.168.20开头的IP，使用内网视频服务
  if (hostname.indexOf('192.168.20.') === 0) {
    return {
      loginIp: '192.168.20.250',
      port: '80'
    };
  }
  
  // 其他情况都使用公网视频服务
  return {
    loginIp: '111.4.68.108',
    port: '80'
  };
};

export {
  getApiBaseURL,
  getVideoApiConfig
}; 