'use strict'
// 模板版本：1.3.1
// 详见 http://vuejs-templates.github.io/webpack 文档
// Webpack 构建配置文件 - 用于 dev/build 配置项

const path = require('path')

module.exports = {
  // 开发环境配置
  dev: {
    // 路径配置
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    // 代理配置 - 可在此处配置 API 代理
    proxyTable: {},

    // 开发服务器配置
    host: 'localhost', // 可被 process.env.HOST 覆盖
    port: 8080, // 可被 process.env.PORT 覆盖，端口被占用时会自动切换
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

    /**
     * Source Maps
     */
    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'cheap-module-eval-source-map',

    // 如果在 devtools 中调试 vue-files 有问题，
    // 将此设置为 false - 这可能有帮助
    // https://vue-loader.vuejs.org/en/options.html#cachecachevueloader
    cacheBusting: true,

    cssSourceMap: true
  },

  // 生产环境配置
  build: {
    // Template for index.html
    index: path.resolve(__dirname, '../dist/index.html'),

    // 路径配置
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',

    /**
     * Source Maps
     */
    productionSourceMap: true,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
