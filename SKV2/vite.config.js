/**
 * Vite 配置文件
 * 功能：集成 Tailwind CSS 到 uni-app 项目（HBuilderX 环境）
 */
import path from 'path'
import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'
import { UnifiedViteWeappTailwindcssPlugin as uvwt } from 'weapp-tailwindcss/vite'

const isH5 = process.env.UNI_PLATFORM === 'h5'
const isApp = process.env.UNI_PLATFORM === 'app'
const WeappTailwindcssDisabled = isH5 || isApp

const resolve = (p) => path.resolve(__dirname, p)

export default defineConfig({
  plugins: [
    uni(),
    uvwt({
      rem2rpx: true,
      disabled: WeappTailwindcssDisabled,
      tailwindcssBasedir: __dirname
    })
  ],
  css: {
    postcss: {
      plugins: [
        require('tailwindcss')({ config: resolve('./tailwind.config.js') }),
        require('autoprefixer')
      ]
    }
  }
})
