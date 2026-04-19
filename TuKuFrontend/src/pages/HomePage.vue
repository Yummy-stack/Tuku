<template>
  <div class="home-page">
    <!-- Hero 欢迎区 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">
          <span class="gradient-text">Yummy’ Home</span>
        </h1>
        <p class="hero-subtitle">专业的云端图片存储与管理平台</p>
        <p class="hero-description">安全可靠 · 高速上传 · 智能分类 · 随时访问</p>
        <div class="hero-actions">
          <a-button type="primary" size="large" @click="goToUpload">
            <UploadOutlined />
            开始上传
          </a-button>
          <a-button size="large" @click="goToExplore">
            <PictureOutlined />
            浏览图库
          </a-button>
        </div>
      </div>
      <div class="hero-decoration">
        <div class="floating-card card-1">
          <PictureOutlined style="font-size: 32px; color: #1890ff" />
        </div>
        <div class="floating-card card-2">
          <CloudUploadOutlined style="font-size: 32px; color: #52c41a" />
        </div>
        <div class="floating-card card-3">
          <FolderOutlined style="font-size: 32px; color: #fa8c16" />
        </div>
      </div>
    </section>

    <!-- 功能特性 -->
    <section class="features-section">
      <h2 class="section-title">核心功能</h2>
      <a-row :gutter="[24, 24]">
        <a-col :xs="24" :sm="12" :lg="6" v-for="feature in features" :key="feature.title">
          <a-card hoverable class="feature-card">
            <div class="feature-icon" :style="{ background: feature.color }">
              <component :is="feature.icon" />
            </div>
            <h3 class="feature-title"> {{ feature.title }}</h3>
            <p class="feature-desc">{{ feature.description }}</p>
          </a-card>
        </a-col>
      </a-row>
    </section>

    <!-- 数据统计 -->
    <section class="stats-section">
      <a-row :gutter="24">
        <a-col :xs="24" :sm="12" :lg="6" v-for="stat in stats" :key="stat.label">
          <div class="stat-card">
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </a-col>
      </a-row>
    </section>

    <!-- 快速开始 -->
    <section class="quick-start-section">
      <h2 class="section-title">快速开始</h2>
      <a-steps :current="currentStep" class="steps-container">
        <a-step v-for="(step, index) in quickStartSteps" :key="index">
          <template #title>{{ step.title }}</template>
          <template #description>{{ step.description }}</template>
          <template #icon>
            <component :is="step.icon" />
          </template>
        </a-step>
      </a-steps>
      <div class="step-actions">
        <a-button @click="currentStep = Math.max(0, currentStep - 1)" :disabled="currentStep === 0">
          上一步
        </a-button>
        <a-button
          type="primary"
          @click="currentStep = Math.min(quickStartSteps.length - 1, currentStep + 1)"
          :disabled="currentStep === quickStartSteps.length - 1"
        >
          下一步
        </a-button>
      </div>
    </section>

    <!-- 最新图片展示 -->
    <section class="gallery-section">
      <div class="section-header">
        <h2 class="section-title">最新上传</h2>
        <a-button type="link" @click="goToExplore"> 查看更多
          <RightOutlined />
        </a-button>
      </div>
      <a-row :gutter="[16, 16]">
        <a-col :xs="12" :sm="8" :md="6" v-for="item in mockImages" :key="item.id">
          <div class="image-card">
            <a-skeleton-image v-if="loading" />
            <div v-else class="image-placeholder">
              <PictureOutlined style="font-size: 48px; color: #d9d9d9" />
              <p>示例图片 {{ item.id }}</p>
            </div>
          </div>
        </a-col>
      </a-row>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  UploadOutlined,
  PictureOutlined,
  CloudUploadOutlined,
  FolderOutlined,
  RightOutlined,
  SafetyOutlined,
  ThunderboltOutlined,
  TeamOutlined,
  UserOutlined,
  LoginOutlined,
  FileImageOutlined,
  CheckCircleOutlined
} from '@ant-design/icons-vue'
import { useLoginUserStore } from '@/stores/user'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const loading = ref(false)
const currentStep = ref(0)

// 功能特性
const features = [
  {
    icon: CloudUploadOutlined,
    title: '高速上传',
    description: '支持批量上传，断点续传，上传速度提升300%',
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    icon: SafetyOutlined,
    title: '安全可靠',
    description: '多重加密保护，定期备份，数据安全有保障',
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    icon: ThunderboltOutlined,
    title: '智能管理',
    description: 'AI智能分类，快速检索，让管理更轻松',
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  },
  {
    icon: TeamOutlined,
    title: '团队协作',
    description: '支持多人协作，权限管理，提升团队效率',
    color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
  }
]

// 数据统计
const stats = ref([
  { value: '10,000+', label: '注册用户' },
  { value: '500,000+', label: '图片存储' },
  { value: '99.9%', label: '服务可用性' },
  { value: '100TB+', label: '存储容量' }
])

// 快速开始步骤
const quickStartSteps = [
  {
    icon: UserOutlined,
    title: '注册账号',
    description: '免费注册，即刻拥有5GB存储空间'
  },
  {
    icon: LoginOutlined,
    title: '登录系统',
    description: '使用账号密码登录到图库平台'
  },
  {
    icon: UploadOutlined,
    title: '上传图片',
    description: '支持拖拽上传，批量上传多张图片'
  },
  {
    icon: CheckCircleOutlined,
    title: '管理图片',
    description: '分类整理、编辑、分享您的图片'
  }
]

// 模拟图片数据
const mockImages = ref(
  Array.from({ length: 8 }, (_, i) => ({
    id: i + 1,
    url: '',
    title: `图片 ${i + 1}`
  }))
)

// 跳转到上传页面
const goToUpload = () => {
  if (!loginUserStore.loginUser?.id) {
    message.warning('请先登录后再上传图片')
    router.push('/user/login')
    return
  }
  router.push('/add/picture')
}

// 跳转到图库浏览页面
const goToExplore = () => {
  message.info('图库浏览功能开发中...')
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #f0f2f5 0%, #ffffff 100%);
}

/* Hero 欢迎区 */
.hero-section {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 500px;
  padding: 60px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: hidden;
}

.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  color: white;
}

.hero-title {
  font-size: 56px;
  font-weight: 700;
  margin-bottom: 16px;
  animation: fadeInUp 0.8s ease;
}

.gradient-text {
  background: linear-gradient(90deg, #fff 0%, #f0f0f0 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 24px;
  margin-bottom: 12px;
  opacity: 0.95;
  animation: fadeInUp 0.8s ease 0.2s both;
}

.hero-description {
  font-size: 16px;
  margin-bottom: 32px;
  opacity: 0.85;
  animation: fadeInUp 0.8s ease 0.4s both;
}

.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  animation: fadeInUp 0.8s ease 0.6s both;
}

.hero-actions :deep(.ant-btn) {
  height: 48px;
  padding: 0 32px;
  font-size: 16px;
  border-radius: 24px;
  transition: all 0.3s;
}

.hero-actions :deep(.ant-btn-primary) {
  background: white;
  color: #667eea;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.hero-actions :deep(.ant-btn-primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.hero-actions :deep(.ant-btn:not(.ant-btn-primary)) {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
}

/* 装饰元素 */
.hero-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1;
  pointer-events: none;
}

.floating-card {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 20px;
  animation: floating 3s ease-in-out infinite;
}

.card-1 {
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.card-2 {
  top: 60%;
  right: 15%;
  animation-delay: 1s;
}

.card-3 {
  bottom: 15%;
  left: 20%;
  animation-delay: 2s;
}

@keyframes floating {
  0%,
  100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 功能特性区 */
.features-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 24px;
}

.section-title {
  font-size: 36px;
  font-weight: 700;
  text-align: center;
  margin-bottom: 48px;
  color: #262626;
}

.feature-card {
  text-align: center;
  border-radius: 12px;
  transition: all 0.3s;
  height: 100%;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: white;
}

.feature-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #262626;
}

.feature-desc {
  font-size: 14px;
  color: #8c8c8c;
  line-height: 1.6;
}

/* 数据统计区 */
.stats-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 60px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
}

.stat-card {
  text-align: center;
  color: white;
  padding: 24px;
}

.stat-value {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 16px;
  opacity: 0.9;
}

/* 快速开始区 */
.quick-start-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 24px;
}

.steps-container {
  margin-top: 48px;
  margin-bottom: 32px;
}

.step-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

/* 图片展示区 */
.gallery-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.image-card {
  aspect-ratio: 1;
  border-radius: 12px;
  overflow: hidden;
  background: #f5f5f5;
  transition: all 0.3s;
  cursor: pointer;
}

.image-card:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #bfbfbf;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .hero-title {
    font-size: 36px;
  }

  .hero-subtitle {
    font-size: 18px;
  }

  .section-title {
    font-size: 28px;
  }

  .stat-value {
    font-size: 32px;
  }

  .hero-actions {
    flex-direction: column;
    width: 100%;
    max-width: 300px;
    margin: 0 auto;
  }

  .hero-actions :deep(.ant-btn) {
    width: 100%;
  }
}
</style>
