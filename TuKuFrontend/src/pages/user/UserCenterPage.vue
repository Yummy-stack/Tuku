<template>
  <div class="userCenterPage">
    <!-- 顶部英雄横幅 -->
    <section class="hero-banner">
      <div class="hero-inner">
        <a-avatar :size="88" :src="loginUser.userAvatar" class="hero-avatar" />
        <div class="hero-info">
          <h1 class="hero-name">{{ loginUser.userName }}</h1>
          <p class="hero-bio">{{ loginUser.userProfile || '探索无限，分享每个精彩瞬间。' }}</p>
        </div>
        <div class="hero-actions">
          <a-space>
            <a-button type="primary" shape="round">上传图片</a-button>
            <a-button shape="round">编辑资料</a-button>
          </a-space>
        </div>
      </div>
    </section>

    <!-- 统计卡片 -->
    <section class="stats-row">
      <a-row :gutter="[16, 16]">
        <a-col :xs="24" :sm="8">
          <a-card class="stat-card" :bordered="false">
            <div class="stat">
              <div class="stat-title">图片</div>
              <div class="stat-value">{{ pictureList.length }}</div>
            </div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="8">
          <a-card class="stat-card" :bordered="false">
            <div class="stat">
              <div class="stat-title">点赞</div>
              <div class="stat-value">256</div>
            </div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="8">
          <a-card class="stat-card" :bordered="false">
            <div class="stat">
              <div class="stat-title">收藏</div>
              <div class="stat-value">128</div>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </section>

    <!-- 工具栏：搜索 / 排序 / 标签 -->
    <a-card class="toolbar-card">
      <div class="toolbar">
        <a-input-search placeholder="搜索我的图片" allow-clear style="max-width: 320px" />
        <div class="toolbar-right">
          <a-radio-group size="small" :value="'latest'">
            <a-radio-button value="latest">最新</a-radio-button>
            <a-radio-button value="popular">最热</a-radio-button>
          </a-radio-group>
          <a-space wrap>
            <a-tag>风景</a-tag>
            <a-tag>城市</a-tag>
            <a-tag>动物</a-tag>
            <a-tag>美食</a-tag>
          </a-space>
        </div>
      </div>
    </a-card>

    <!-- 我的图库 -->
    <a-card title="我的图库" class="gallery-card">
      <template #extra>
        <span class="gallery-count">共 {{ pictureList.length }} 张</span>
      </template>

      <div v-if="pictureList.length === 0" class="empty-wrap">
        <a-empty description="还没有图片，快去上传吧～" />
      </div>

      <a-skeleton active :loading="!pictureList.length" :paragraph="{ rows: 3 }">
        <a-list
          :grid="{ gutter: 24, xs: 1, sm: 2, md: 3, lg: 4, xl: 5, xxl: 6 }"
          :data-source="pictureList"
          :pagination="pagination"
        >
          <template #renderItem="{ item }">
            <a-list-item>
              <a-card hoverable class="picture-card">
                <template #cover>
                  <div class="image-container">
                    <img :alt="item.title" :src="item.url" class="picture-img fade-in" />
                  </div>
                </template>
                <a-card-meta :title="item.title">
                  <template #description>{{ item.description }}</template>
                </a-card-meta>
                <template #actions>
                  <div class="card-actions">
                    <a-button size="small" shape="round">查看</a-button>
                    <a-button size="small" shape="round">收藏</a-button>
                    <a-button size="small" shape="round">点赞</a-button>
                  </div>
                </template>
              </a-card>
            </a-list-item>
          </template>
        </a-list>
      </a-skeleton>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useLoginUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'

const userStore = useLoginUserStore()
const { loginUser } = storeToRefs(userStore)

// 更丰富的模拟数据
const pictureList = ref([
  {
    title: '静谧山谷',
    url: 'https://pic.code-nav.cn/user_avatar/1716249269226303490/thumbnail/RscW7QLbEMyRjRk0.jpg',
    description: '晨雾中的宁静山谷',
  },
  {
    title: '都市脉搏',
    url: 'https://pic.code-nav.cn/user_avatar/1716249269226303490/thumbnail/RscW7QLbEMyRjRk0.jpg',
    description: '霓虹闪烁的城市夜景',
  },
  {
    title: '慵懒午后',
    url: 'https://pic.code-nav.cn/user_avatar/1716249269226303490/thumbnail/RscW7QLbEMyRjRk0.jpg',
    description: '阳光下打盹的猫咪',
  },
  {
    title: '甜蜜诱惑',
    url: 'https://pic.code-nav.cn/user_avatar/1716249269226303490/thumbnail/RscW7QLbEMyRjRk0.jpg',
    description: '色彩缤纷的马卡龙',
  },
  {
    title: '光影之舞',
    url: 'https://pic.code-nav.cn/user_avatar/1716249269226303490/thumbnail/RscW7QLbEMyRjRk0.jpg',
    description: '抽象的线条与色彩',
  },
  {
    title: '极限飞跃',
    url: 'https://pic.code-nav.cn/user_avatar/1716249269226303490/thumbnail/RscW7QLbEMyRjRk0.jpg',
    description: '滑雪者在雪山之巅',
  },
  {
    title: '星河璀璨',
    url: 'https://pic.code-nav.cn/user_avatar/1716249269226303490/thumbnail/RscW7QLbEMyRjRk0.jpg',
    description: '银河下的壮丽夜空',
  },
  {
    title: '未来之城',
    url: 'https://pic.code-nav.cn/user_avatar/1716249269226303490/thumbnail/RscW7QLbEMyRjRk0.jpg',
    description: '设计前卫的现代建筑',
  },
])

const pagination = {
  onChange: (page: number) => {
    console.log(page)
  },
  pageSize: 8,
}

onMounted(() => {
  userStore.fetchLoginUser()
})
</script>

<style scoped>
.userCenterPage {
  padding: 24px;
  background: linear-gradient(180deg, #f7f9fc 0%, #f0f2f5 100%);
  min-height: calc(100vh - 88px);
}

/* 英雄横幅 */
.hero-banner {
  margin-bottom: 24px;
  border-radius: 16px;
  padding: 24px;
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: saturate(180%) blur(12px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}
.hero-inner {
  display: flex;
  align-items: center;
  gap: 16px;
}
.hero-avatar {
  border: 3px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}
.hero-info {
  display: flex;
  flex-direction: column;
}
.hero-name {
  font-size: 28px;
  font-weight: 800;
  margin: 0;
  color: #1f1f1f;
}
.hero-bio {
  margin-top: 6px;
  color: #666;
}
.hero-actions {
  margin-left: auto;
}

/* 统计卡片 */
.stats-row {
  margin-bottom: 16px;
}
.stat-card {
  border-radius: 12px;
  background: linear-gradient(135deg, #ffffff 0%, #f7faff 100%);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
}
.stat {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
}
.stat-title {
  color: #666;
}
.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #333;
}

/* 工具栏 */
.toolbar-card {
  border-radius: 12px;
  margin: 16px 0 24px;
}
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}
.toolbar-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 图库 */
.gallery-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}
.gallery-count {
  color: #888;
}

.picture-card {
  border-radius: 14px;
  overflow: hidden;
  background: #fff;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}
.picture-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
}
.image-container {
  width: 100%;
  padding-top: 75%;
  position: relative;
  overflow: hidden;
}
.picture-img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition:
    transform 0.4s ease,
    filter 0.4s ease,
    opacity 0.4s ease;
}
.picture-card:hover .picture-img {
  transform: scale(1.06);
  filter: saturate(1.05) contrast(1.02);
}

/* 动画与无障碍 */
.fade-in {
  opacity: 0;
  animation: fadeIn 0.5s ease-out forwards;
}
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(1.02);
  }
  to {
    opacity: 1;
    transform: none;
  }
}
@media (prefers-reduced-motion: reduce) {
  .fade-in {
    animation: none;
    opacity: 1;
  }
  .picture-img {
    transition: none;
  }
}

.card-actions {
  display: flex;
  gap: 8px;
  padding: 8px 16px 16px;
  justify-content: flex-end;
}

.empty-wrap {
  text-align: center;
  padding: 40px 0;
}

:deep(.ant-card-meta-title) {
  font-weight: 600;
  margin-bottom: 4px !important;
}
:deep(.ant-card-meta-description) {
  color: #888;
}
</style>
