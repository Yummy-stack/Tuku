<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
        v-model:selectedKeys="current"
        mode="horizontal"
        :items="originItems"
        :style="{ height: '100%', borderRight: 0 }"
      >
        <a-sub-menu key="sub1">
          <template #title>
              <span>
                <user-outlined />
                subnav 1
              </span>
          </template>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
  </a-layout>
</template>

<script setup lang="ts">
import { h, ref } from 'vue'
import { UserOutlined, LaptopOutlined, NotificationOutlined, HomeOutlined } from '@ant-design/icons-vue'
import type { MenuProps } from 'ant-design-vue'

const selectedKeys1 = ref<string[]>(['2'])
const selectedKeys2 = ref<string[]>(['1'])
const openKeys = ref<string[]>(['sub1'])
const current = ref<string[]>([])

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
])
</script>


<style scoped>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>
