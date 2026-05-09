/** @type {import('tailwindcss').Config} */
const path = require('path')
const resolve = (p) => path.resolve(__dirname, p)

module.exports = {
  content: [
    './pages/**/*.{vue,js}',
    './components/**/*.{vue,js}',
    './composables/**/*.{vue,js}',
    './App.vue',
  ].map(resolve),
  theme: {
    extend: {
      colors: {
        primary: '#00b783',
        success: '#4cd964',
        warning: '#f0ad4e',
        error: '#dd524d',
      },
      spacing: {
        'safe-top': 'var(--status-bar-height)',
        'safe-bottom': 'var(--window-bottom)',
      },
    },
  },
  corePlugins: {
    preflight: false,
  },
}
