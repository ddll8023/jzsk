// 获取客户端IP地址
const getClientIP = async () => {
  try {
    const response = await fetch('https://api.ipify.org?format=json');
    const data = await response.json();
    const clientIP = data.ip;
    
    // 存储客户端IP
    sessionStorage.setItem('client_ip', clientIP);
    console.log('检测到客户端IP:', clientIP);
    
    return clientIP;
  } catch (error) {
    console.log('无法获取客户端IP:', error.message);
    return '无法获取';
  }
};

// 网络配置检查工具
export const checkNetworkConfig = async () => {
  const hostname = window.location.hostname;
  const port = window.location.port;
  const protocol = window.location.protocol;
  
  console.log('=== 网络配置检查 ===');
  console.log('当前访问地址:', protocol + '//' + hostname + ':' + port);
  console.log('主机名:', hostname);
  console.log('端口:', port);
  console.log('协议:', protocol);
  
  // 获取客户端IP
  await getClientIP();
  
  // 检查API地址
  const apiBaseURL = getApiBaseURL();
  console.log('API地址:', apiBaseURL);
  
  return {
    hostname,
    port,
    protocol,
    apiBaseURL
  };
};





// 获取API地址的函数（从api.js导入）
const getApiBaseURL = () => {
  const hostname = window.location.hostname;
  
  // 如果是本地开发环境
  if (hostname === 'localhost' || hostname === '127.0.0.1') {
    return 'http://localhost:8081';
  }
  
  // 如果是192.168.20开头的IP，使用内网API
  if (hostname.indexOf('192.168.20.') === 0) {
    return 'http://192.168.20.3:8081';
  }
  
  // 其他情况都使用公网API
  return 'http://111.4.68.108:8081';
}; 