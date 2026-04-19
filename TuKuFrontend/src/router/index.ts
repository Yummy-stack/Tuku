import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/pages/HomePage.vue')
    },
    {
      path: '/user/login',
      name: 'userLogin',
      component: () => import('@/pages/user/UserLoginPage.vue')
    },
    {
      path: '/user/register',
      name: 'userRegister',
      component: () => import('@/pages/user/UserRegisterPage.vue')
    },
    {
      path: '/user/register',
      name: 'userRegister',
      component: () => import('@/pages/user/UserRegisterPage.vue')
    },
    {
      path: '/user/center',
      name: 'userCenter',
      component: () => import('@/pages/user/UserCenterPage.vue')
    },
    {
      path: '/admin/manage',
      name: 'adminManage',
      component: () => import('@/pages/manage/AdminManagePage.vue')
    },
    {
      path: '/add/picture',
      name: 'addPicture',
      component: () => import('@/pages/picture/AddPicturePage.vue')
    },
    {
      path: '/picture/management',
      name: 'pictureManagement',
      component: () => import('@/pages/manage/PictureManagementPage.vue')
    },
    {
      path: '/picture/all',
      name: 'allPicture',
      component: () => import('@/pages/picture/AllPicturePage.vue')
    },
    {
      path: '/picture/:id(\\d+)',
      name: 'pictureDetail',
      component: () => import('@/pages/picture/DetailPicturePage.vue'),
      props: true
    },
    {
      path: '/picture-space/management',
      name: 'pictureSpaceManagement',
      component: () => import('@/pages/manage/PictureSpaceManagementPage.vue')
    },
    {
      path: '/add/picture-space',
      name: 'addPicSpa',
      component: () => import('@/pages/pictureSpace/AddPicSpaPage.vue')
    },
  ]
})

export default router
