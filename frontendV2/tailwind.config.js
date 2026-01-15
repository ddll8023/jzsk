/**
 * Tailwind CSS 配置文件
 * 功能：主题定制、响应式断点配置
 * Source: Tailwind CSS 官方文档
 */

/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{vue,js,jsx}'
  ],
  theme: {
    extend: {
      // 主题色配置
      colors: {
        primary: {
          50: '#eff6ff',
          100: '#dbeafe',
          200: '#bfdbfe',
          300: '#93c5fd',
          400: '#60a5fa',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8',
          800: '#1e40af',
          900: '#1e3a8a'
        }
      },
      // 自定义间距
      spacing: {
        '18': '4.5rem',
        '88': '22rem',
        '128': '32rem'
      },
      // 字体配置
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif']
      },
      // 自定义阴影
      boxShadow: {
        'custom': '0 2px 8px rgba(0, 0, 0, 0.1)',
        'custom-lg': '0 4px 16px rgba(0, 0, 0, 0.1)'
      }
    }
  },
  plugins: []
}
