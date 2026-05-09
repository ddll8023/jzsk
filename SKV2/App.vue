<script setup>
/**
 * 应用根组件
 * 功能：引入全局样式、权限拦截
 */
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/modules/user.js'

const WHITE_LIST = ['/pages/login/login']

onShow(() => {
  const userStore = useUserStore()
  const pages = getCurrentPages()
  const currentPath = pages.length > 0 ? `/${pages[pages.length - 1].route}` : ''

  if (!userStore.isLoggedIn && !WHITE_LIST.includes(currentPath)) {
    userStore.setRedirectPath(currentPath)
    uni.reLaunch({ url: '/pages/login/login' })
  }
})
</script>

<style>
@tailwind base;
@tailwind components;
@tailwind utilities;

page {
  background-color: #f8f8f8;
  font-size: 28rpx;
  color: #333333;
}
</style>
