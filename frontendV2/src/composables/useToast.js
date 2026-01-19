/**
 * useToast Composable
 * 功能：统一的消息提示功能
 * 遵循原则：KISS - 简洁实现，DRY - 避免重复代码
 * Source: 抽取自 warning 模块的重复代码
 */

/**
 * 显示提示信息
 * @param {string} message - 提示消息
 * @param {string} type - 提示类型: success | error | warning | info
 * @param {number} duration - 显示时长（毫秒），默认 3000
 */
export function useToast() {
  const showToast = (message, type = 'info', duration = 3000) => {
    // 类型对应的背景色
    const bgColorMap = {
      success: 'bg-green-500',
      error: 'bg-red-500',
      warning: 'bg-yellow-500',
      info: 'bg-blue-500'
    }

    // 类型对应的图标
    const iconMap = {
      success: 'fa-check-circle',
      error: 'fa-times-circle',
      warning: 'fa-exclamation-triangle',
      info: 'fa-info-circle'
    }

    const bgColor = bgColorMap[type] || bgColorMap.info
    const icon = iconMap[type] || iconMap.info

    // 创建 Toast 元素（居中上方显示，更醒目）
    const toast = document.createElement('div')
    toast.className = `fixed top-20 left-1/2 -translate-x-1/2 ${bgColor} text-white px-6 py-3 rounded-lg shadow-lg z-50 transition-opacity duration-300 opacity-0 flex items-center gap-2`
    
    // 添加图标和文本
    toast.innerHTML = `
      <i class="fa ${icon}" aria-hidden="true"></i>
      <span>${message}</span>
    `
    
    document.body.appendChild(toast)

    // 动画淡入
    requestAnimationFrame(() => {
      toast.classList.remove('opacity-0')
      toast.classList.add('opacity-100')
    })

    // 定时移除
    setTimeout(() => {
      toast.classList.remove('opacity-100')
      toast.classList.add('opacity-0')
      setTimeout(() => toast.remove(), 300)
    }, duration)
  }

  return {
    showToast
  }
}
