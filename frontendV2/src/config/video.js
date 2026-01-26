/**
 * 视频监控配置
 * 功能：海康SDK连接参数配置
 * 遵循原则：KISS
 */

/**
 * 检测当前是否为内网环境
 * 内网环境：访问地址为 192.168.20.x 网段
 */
export function isIntranetEnvironment() {
  const hostname = window.location.hostname
  // 允许本地开发环境和内网IP
  return hostname === 'localhost' ||
         hostname === '127.0.0.1' ||
         hostname.startsWith('192.168.') ||
         hostname.startsWith('10.')
}

/**
 * 获取视频服务配置
 * 仅内网环境可用，公网环境返回 null
 */
export function getVideoConfig() {
  // 仅内网环境可用
  if (!isIntranetEnvironment()) {
    return null
  }
  
  return {
    loginIp: '192.168.20.250',
    port: '80',  // 海康 SDK I_Login 的 szPort 参数需要字符串类型
    username: 'admin',
    password: 'wx147369'
  }
}

/**
 * 图片服务地址
 */
export function getPhotoBaseUrl() {
  const hostname = window.location.hostname
  
  if (hostname === 'localhost' || hostname === '127.0.0.1' || hostname.startsWith('192.168.20.')) {
    return 'http://192.168.20.250:8081/pic/'
  }
  
  return 'http://111.4.68.108:8081/pic/'
}

/**
 * 窗口分割选项
 */
export const WINDOW_LAYOUTS = [
  { value: 1, label: '1×1', grid: 1 },
  { value: 2, label: '2×2', grid: 4 },
  { value: 3, label: '3×3', grid: 9 },
  { value: 4, label: '4×4', grid: 16 }
]

/**
 * 码流类型选项
 */
export const STREAM_TYPES = [
  { value: 1, label: '高清（主码流）' },
  { value: 2, label: '标清（子码流）' }
]

/**
 * 云台控制方向映射
 */
export const PTZ_DIRECTIONS = {
  UP: 1,
  DOWN: 2,
  LEFT: 3,
  RIGHT: 4,
  ZOOM_IN: 10,
  ZOOM_OUT: 11
}
