/**
 * 用户状态管理
 * 功能：token、用户信息、登录/登出、权限判断
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { post, get } from '@/utils/request.js'
import { getStorage, setStorage, removeStorage } from '@/utils/storage.js'

export const useUserStore = defineStore(
  'user',
  () => {
    const token = ref(getStorage('token') || '')
    const userInfo = ref(null)
    const redirectPath = ref('')

    const isLoggedIn = computed(() => !!token.value)
    const displayName = computed(() => userInfo.value?.displayName || userInfo.value?.name || '')
    const roleNames = computed(() => {
      if (!userInfo.value?.roles) return []
      return userInfo.value.roles.map((r) => r.name)
    })

    async function login(credentials) {
      const res = await post('/api/auth/login', credentials)
      const { token: jwt, user } = res.data
      token.value = jwt
      userInfo.value = user
      setStorage('token', jwt)
      return res
    }

    async function fetchCurrentUser() {
      const res = await get('/api/auth/current-user')
      userInfo.value = res.data
      return res
    }

    function setToken(newToken) {
      token.value = newToken
      setStorage('token', newToken)
    }

    function logout() {
      token.value = ''
      userInfo.value = null
      redirectPath.value = ''
      removeStorage('token')
      uni.reLaunch({ url: '/pages/login/login' })
    }

    function setRedirectPath(path) {
      redirectPath.value = path
    }

    function clearRedirectPath() {
      redirectPath.value = ''
    }

    return {
      token,
      userInfo,
      redirectPath,
      isLoggedIn,
      displayName,
      roleNames,
      login,
      fetchCurrentUser,
      setToken,
      logout,
      setRedirectPath,
      clearRedirectPath,
    }
  },
  {
    persist: {
      key: 'user',
      storage: {
        getItem: (key) => uni.getStorageSync(key),
        setItem: (key, value) => uni.setStorageSync(key, value),
      },
    },
  },
)
