<template>
  <div id="userLoginPage">
    <h2 class="title">Yummy's 图库</h2>
    <div class="desc">用于存储图片的云平台</div>
    <a-form
      :model="formState"
      name="basic"
      label-align="left"
      autocomplete="off"
      @finish="onFinish"
      @finishFailed="onFinishFailed"
    >
      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号!' }]">
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>

      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          // { min: 8, message: '密码不能小于 8 位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>

      <div class="tips">
        没有账号?
        <router-link to="/user/register">去注册</router-link>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/user'
import { userLoginUsingPost } from '@/api/yonghumokuaideApi'
import { message } from 'ant-design-vue'

// ------------------------ 变量 ------------------------
const router = useRouter()
const loginUser = useLoginUserStore()
const formState = reactive<API.UserLoginDto>({
  userAccount: '',
  userPassword: '',
})

const remember = ref<boolean>(true)

// ------------------------ 函数 ------------------------
// 登录
const onFinish = async (values: any) => {
  try {
    const loginResult = await userLoginUsingPost(values)
    if (loginResult.data.code === 0 && loginResult.data.data) {
      await loginUser.fetchLoginUser()

      await router.push({
        path: '/',
        replace: true,
      })
    } else {
      message.error('登录失败:' + loginResult.data.message)
    }
  } catch (e: any) {
    message.error(e.message || '登录失败,请稍后重试')
  }
}
// 未填写登录信息
const onFinishFailed = (errorInfo: any) => {
  alert('请填写登录信息')
}
</script>

<style scoped>
#userLoginPage {
  max-width: 360px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 16px;
}

.desc {
  text-align: center;
  color: #bbb;
  margin-bottom: 16px;
}

.tips {
  margin-bottom: 16px;
  color: #bbb;
  font-size: 13px;
  text-align: right;
}
</style>
