/**
 * 数值处理工具函数
 * 功能：数值空值处理、小数位格式化、单位展示辅助
 */

/**
 * 格式化数值为指定小数位字符串
 * @param {number|string|null|undefined} val - 待格式化的值
 * @param {number} digits - 小数位数，默认 2
 * @returns {string} 格式化后的字符串，无效值返回 '--'
 */
export function formatNum(val, digits = 2) {
  if (val === null || val === undefined || val === '') return '--'
  const num = Number(val)
  return isNaN(num) ? '--' : num.toFixed(digits)
}
