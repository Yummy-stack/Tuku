import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getLoginUserUsingGet } from '@/api/yonghumokuaideApi'

export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref<any>({
    id: '',
    userName: '未登录',
    userAvatar: '',
  })

  async function fetchLoginUser() {
    try {
      const res = await getLoginUserUsingGet()
      if (res.data.code === 0 && res.data.data) {
        loginUser.value = res.data.data
        console.log('getLoginUserUsingGet请求的响应结果为：' + res)
      }
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }

  // async function fetchLoginUser() {
  //   try {
  //     setTimeout(()=>{
  //       loginUser.value = {
  //         userName: "测试用户1",
  //         id:"123456789",
  //         userAvatar:"https://pic.code-nav.cn/user_avatar/1716249269226303490/thumbnail/RscW7QLbEMyRjRk0.jpg",
  //       }
  //     })
  //   } catch (error) {
  //     console.error("获取用户信息失败",error)
  //   }
  // }

  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser
  }

  return { loginUser, setLoginUser, fetchLoginUser }
})
