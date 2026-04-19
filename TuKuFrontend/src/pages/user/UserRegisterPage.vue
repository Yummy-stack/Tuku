<template>
  <div id="userRegisterPage">
    <h2 class="title">Yummy's 图库</h2>
    <div class="desc">用于存储图片的云平台</div>
    <a-form
      :model="formState"
      :rules="rules"
      name="basic"
      label-align="left"
      autocomplete="off"
      @finish="handleSubmit"
    >
      <a-form-item name="userAccount">
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item name="userPassword">
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>
      <a-form-item name="userConfirmPassword">
        <a-input-password
          v-model:value="formState.userConfirmPassword"
          placeholder="请输入确认密码"
        />
      </a-form-item>
      <div class="tips">
        已有账号？
        <router-link to="/user/login">去登录</router-link>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%" :loading="loading"
        >注册
        </a-button
        >
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { userRegisterUsingPost } from '@/api/yonghumokuaideApi'
import type { Rule } from 'ant-design-vue/es/form'

const router = useRouter()
const formState = reactive<API.UserRegisterDto>({
  userAccount: '',
  userPassword: '',
  userConfirmPassword: ''
})
const loading = ref(false)

// 自定义确认密码校验
const validateConfirmPassword = async (_rule: Rule, value: string) => {
  if (value !== formState.userPassword) {
    return Promise.reject('两次输入的密码不一致')
  }
  return Promise.resolve()
}

const rules: Record<string, Rule[]> = {
  userAccount: [{ required: true, message: '请输入账号' }],
  userPassword: [
    { required: true, message: '请输入密码' },
    { min: 8, message: '密码不能小于 8 位' }
  ],
  userConfirmPassword: [
    { required: true, message: '请输入确认密码' },
    { validator: validateConfirmPassword, trigger: 'change' } // 使用自定义校验
  ]
}

const handleSubmit = async (values: any) => {
  loading.value = true
  try {
    const res = await userRegisterUsingPost(values)
    if (res.data.code === 0 && res.data.data) {
      message.success('注册成功')
      await router.push({
        path: '/user/login',
        replace: true
      })
    } else {
      message.error('注册失败，' + res.data.message)
    }
  } catch (error) {
    message.error('注册失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 页面容器（玻璃拟态 + 轻微浮动） */
#userRegisterPage {
  max-width: 380px;
  margin: 40px auto;
  padding: 32px;
  background: rgba(255, 255, 255, 0.86);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.35);
  box-shadow: 0 20px 50px rgba(51, 60, 140, 0.25),
  0 8px 20px rgba(102, 126, 234, 0.2);
  backdrop-filter: saturate(180%) blur(22px);
  z-index: 2;
  animation: cardFloat 6s ease-in-out infinite;
  will-change: transform;
}

/* 标题样式 */
.title {
  text-align: center;
  margin-bottom: 12px;
  font-weight: 800;
  font-size: 28px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  transition: all 0.3s ease;
}

/* 描述样式 */
.desc {
  text-align: center;
  color: #666;
  margin-bottom: 36px;
  font-size: 14px;
  font-weight: 400;
  transition: all 0.3s ease;
}

/* 提示文字样式 */
.tips {
  margin-bottom: 24px;
  color: #bbb;
  font-size: 13px;
  text-align: right;
}

/* 表单输入框样式 */
:deep(.ant-input-affix-wrapper),
:deep(.ant-input) {
  border-radius: 8px;
  border-color: #e8e8e8;
  transition: all 0.3s ease;
}

:deep(.ant-input-affix-wrapper:hover),
:deep(.ant-input:hover) {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

:deep(.ant-input-affix-wrapper:focus),
:deep(.ant-input-affix-wrapper-focused),
:deep(.ant-input:focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

/* 表单域样式 */
:deep(.ant-form-item) {
  margin-bottom: 20px;
}

/* 登录按钮样式 */
:deep(.ant-btn-primary) {
  width: 100%;
  height: 40px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

:deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

:deep(.ant-btn-primary:active) {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

/* 动画定义 */

@keyframes cardFloat {
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-6px);
  }
  100% {
    transform: translateY(0);
  }
}

/* 无障碍与性能：用户偏好减少动画时关闭动效 */
@media (prefers-reduced-motion: reduce) {
  #userRegisterPage {
    animation: none;
  }

  :deep(.ant-btn-primary:hover) {
    transform: none;
  }
}
</style>
