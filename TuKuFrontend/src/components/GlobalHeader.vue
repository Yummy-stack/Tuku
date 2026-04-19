<template>
  <div class="global-header">
    <a-row :wrap="false" align="middle" class="header-content">
      <a-col flex="200px">
        <div class="title-bar" @click="goHome">
          <div class="logo-container">
            <img class="logo" src="../assets/pic_002.png" alt="logo" />
            <div class="logo-shine"></div>
          </div>
          <div class="title-wrapper">
            <!--            <div class="title">Yummy'Home</div>-->
          </div>
        </div>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
          class="custom-menu"
        />
      </a-col>
      <a-col flex="120px" class="user-section">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id !== '' && loginUserStore.loginUser.id != null">
            <a-dropdown placement="bottomRight">
              <a-space class="user-info">
                <a-avatar :src="loginUserStore.loginUser.userAvatar" class="user-avatar">
                  <template #icon>
                    <UserOutlined />
                  </template>
                </a-avatar>
                <span class="user-name">{{ loginUserStore.loginUser.userName || '无名' }}</span>
                <DownOutlined class="dropdown-icon" />
              </a-space>
              <template #overlay>
                <a-menu class="user-dropdown">
                  <a-menu-item @click="doCatPerson" class="dropdown-item">
                    <UserOutlined />
                    <span>个人主页</span>
                  </a-menu-item>
                  <a-menu-divider />
                  <a-menu-item @click="doLogout" class="dropdown-item logout">
                    <LogoutOutlined />
                    <span>退出登录</span>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else class="login-btn-wrapper">
            <a-button type="primary" size="middle" @click="goToLogin" class="login-btn">
              <span style="position: relative; z-index: 1">
                <LoginOutlined />
                登录
              </span>
            </a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import { computed, h, ref } from 'vue'
import {
  HomeOutlined,
  LogoutOutlined,
  UserOutlined,
  DownOutlined,
  LoginOutlined
} from '@ant-design/icons-vue'
import type { MenuProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/user'
import { userLogoutUsingPost } from '@/api/yonghumokuaideApi'

// ---------------------- 变量 -------------------------
const loginUserStore = useLoginUserStore()
// 路由
const router = useRouter()
const route = useRouter()

const originItems = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页'
  },
  {
    key: '/picture/all',
    label: '图库',
    title: '图库'
  },
  {
    key: '/admin/manage',
    label: '用户管理',
    title: '用户管理'
  },
  {
    key: '/picture/management',
    label: '图片管理',
    title: '图片管理'
  },
  {
    key: '/picture-space/management',
    label: '空间管理',
    title: '空间管理'
  }
  // {
  //   key: '/add/picture',
  //   label: '操作图片',
  //   title: '操作图片'
  // }
])
// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    if (typeof menu?.key === 'string' && menu?.key?.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        return false
      }
    }
    return true
  })
}

// 展示在菜单的路由数组
const items = computed<MenuProps['items']>(() => filterMenus(originItems.value))

// 当前选中菜单
const current = ref<string[]>([])
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  current.value = [to.path]
})

// ---------------------- 函数 -------------------------
// 路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key
  })
}

// 跳转到首页
const goHome = () => {
  router.push('/')
}

// 跳转到登录页
const goToLogin = () => {
  router.push('/user/login')
}

// 用户退出登录
const doLogout = async () => {
  const res = await userLogoutUsingPost()
  console.log(res)
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: '未登录'
    })
    message.success('退出登录成功')
    await router.push({
      path: '/user/login',
      replace: true
    })
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}

// 个人主页
const doCatPerson = async () => {
  await router.push({
    path: '/user/center'
  })
}
</script>

<style scoped>
/* 全局导航栏 */
.global-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  border-bottom: 1px solid #f0f0f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  margin-bottom: 16px;
}

.header-content {
  padding: 0 24px;
  height: 60px;
}

/* Logo 区域 */
.title-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.title-bar:hover {
  opacity: 0.85;
}

.logo-container {
  position: relative;
  width: 44px;
  height: 44px;
  border-radius: 10px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.logo-container:hover {
  transform: scale(1.05);
}

.logo {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* Logo 光效 - 简化 */
.logo-shine {
  display: none;
}

/* 标题区域 */
.title-wrapper {
  display: flex;
  flex-direction: column;
}

.title {
  color: #1a1a2e;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 0.5px;
  transition: color 0.3s ease;
}

.title-bar:hover .title {
  color: #1890ff;
}

/* 菜单样式 */
.custom-menu {
  background: transparent;
  border-bottom: none;
  line-height: 60px;
}

.custom-menu :deep(.ant-menu-item) {
  color: #595959;
  font-weight: 500;
  font-size: 15px;
  margin: 0 8px;
  padding: 0 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.custom-menu :deep(.ant-menu-item:hover) {
  color: #1890ff;
  background: rgba(24, 144, 255, 0.06);
}

.custom-menu :deep(.ant-menu-item-selected) {
  color: #1890ff !important;
  background: rgba(24, 144, 255, 0.1);
  font-weight: 600;
}

.custom-menu :deep(.ant-menu-item-selected::after) {
  display: none;
}

.custom-menu :deep(.ant-menu-item-icon) {
  font-size: 16px;
}

/* 用户区域 */
.user-section {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.user-login-status {
  display: flex;
  align-items: center;
  height: 100%;
}

.user-info {
  padding: 6px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.user-info:hover {
  background: #f5f5f5;
}

.user-avatar {
  border: 2px solid #f0f0f0;
  transition: all 0.3s ease;
}

.user-info:hover .user-avatar {
  border-color: #1890ff;
  transform: scale(1.05);
}

.user-name {
  color: #262626;
  font-weight: 500;
  font-size: 14px;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-icon {
  color: #8c8c8c;
  font-size: 12px;
  transition: transform 0.3s ease;
}

.user-info:hover .dropdown-icon {
  color: #1890ff;
  transform: rotate(180deg);
}

/* 下拉菜单 */
.user-dropdown {
  margin-top: 8px;
  border-radius: 8px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.dropdown-item {
  padding: 10px 16px;
  font-size: 14px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.dropdown-item:hover {
  background: #f5f5f5;
  color: #1890ff;
}

.dropdown-item.logout {
  color: #ff4d4f;
}

.dropdown-item.logout:hover {
  background: #fff1f0;
  color: #ff4d4f;
}

/* 登录按钮 */
.login-btn-wrapper {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-btn {
  height: 40px;
  padding: 0 24px;
  border-radius: 20px;
  font-weight: 600;
  font-size: 14px;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;
}

.login-btn::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transform: rotate(45deg);
  animation: shine 3s infinite;
}

@keyframes shine {
  0% {
    transform: translateX(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) rotate(45deg);
  }
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
}

.login-btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
  }

  .title {
    font-size: 16px;
  }

  .user-name {
    display: none;
  }

  .custom-menu :deep(.ant-menu-item) {
    font-size: 14px;
    padding: 0 12px;
  }

  .login-btn {
    height: 36px;
    padding: 0 16px;
    font-size: 13px;
  }
}
</style>
