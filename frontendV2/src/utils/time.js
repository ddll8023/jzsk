/**
 * 时间格式化工具
 * 功能：提供统一的时间格式化函数，确保显示一致性
 */

/**
 * 格式化时间显示（精确到分钟，分钟个位数四舍五入）
 * 规则：12:21 → 12:20，12:29 → 12:30
 * @param {string|number|Date} val - 时间值
 * @returns {string} 格式化后的时间字符串
 */
export function formatMinute(val) {
  if (!val) return ''
  let d
  if (typeof val === 'number') {
    d = new Date(val < 1e12 ? val * 1000 : val)
  } else {
    d = new Date(String(val).replace(/-/g, '/'))
  }
  if (isNaN(d.getTime())) return ''
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')

  // 分钟个位数四舍五入，十位数保持不变
  let minutes = d.getMinutes()
  const tens = Math.floor(minutes / 10) * 10
  const ones = minutes % 10
  const roundedOnes = Math.round(ones / 10) * 10
  minutes = tens + roundedOnes

  d.setMinutes(minutes)
  const h = String(d.getHours()).padStart(2, '0')
  const min = String(d.getMinutes()).padStart(2, '0')
  return `${y}-${m}-${dd} ${h}:${min}`
}
