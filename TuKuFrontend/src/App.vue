<template>
  <BasicLayout></BasicLayout>
</template>

<script setup lang="ts">
import { RouterLink, RouterView, useRouter } from 'vue-router'
import BasicLayout from '@/layouts/BasicLayout.vue'
import { useLoginUserStore } from '@/stores/user'
import { onMounted } from 'vue'

const loginUserStore = useLoginUserStore()
loginUserStore.fetchLoginUser()
// const router = useRouter()
// onMounted(() => {
//   loginUserStore.fetchLoginUser()
//   const loginUser = loginUserStore.loginUser
//   if (loginUser === null) {
//     router.push({
//       path:'/user/login',
//       replace: true,
//     })
//   }
// })
</script>

<style scoped>

</style>
