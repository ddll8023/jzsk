// 网络环境检测工具
export const detectNetworkEnvironment = () => {
  const hostname = window.location.hostname;
  const protocol = window.location.protocol;
  
  // 检测是否在内网环境
  const isIntranet = () => {
    // 检查是否是内网IP
    if (hostname.startsWith('192.168.') || 
        hostname.startsWith('10.') || 
        hostname.startsWith('172.')) {
      return true;
    }
    
    // 检查是否是localhost
    if (hostname === 'localhost' || hostname === '127.0.0.1') {
      return true;
    }
    
    return false;
  };
  
  // 检测是否在公网环境
  const isPublicNetwork = () => {
    return hostname === '111.4.68.108';
  };
  
  // 获取合适的API地址
  const getApiBaseURL = () => {
    if (isIntranet()) {
      // 内网环境使用内网IP
      return 'http://192.168.20.3:8081';
    } else if (isPublicNetwork()) {
      // 公网环境，尝试检测是否能访问内网
      return 'http://192.168.20.3:8081';
    } else {
      // 本地开发环境
      return 'http://localhost:8081';
    }
  };
  
  // 测试API连接
  const testApiConnection = async (url) => {
    try {
      const response = await fetch(`${url}/actuator/health`, {
        method: 'GET',
        timeout: 3000
      });
      return response.ok;
    } catch (error) {
      return false;
    }
  };
  
  // 智能选择API地址
  const getOptimalApiBaseURL = async () => {
    const primaryURL = getApiBaseURL();
    
    // 测试主要地址
    const isPrimaryAvailable = await testApiConnection(primaryURL);
    if (isPrimaryAvailable) {
      return primaryURL;
    }
    
    // 如果主要地址不可用，尝试备用地址
    if (isPublicNetwork()) {
      const fallbackURL = 'http://111.4.68.108:8081';
      const isFallbackAvailable = await testApiConnection(fallbackURL);
      if (isFallbackAvailable) {
        return fallbackURL;
      }
    }
    
    // 如果都不可用，返回主要地址
    return primaryURL;
  };
  
  return {
    isIntranet,
    isPublicNetwork,
    getApiBaseURL,
    testApiConnection,
    getOptimalApiBaseURL
  };
}; 