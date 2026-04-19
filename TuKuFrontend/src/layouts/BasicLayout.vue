<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <!-- <a-layout-footer class="footer">
        <div class="footer-content">
          <div class="footer-links">
            <a href="https://gitee.com/Yummyzz/projects" target="_blank" rel="noopener noreferrer"
              >Gitee开源地址</a
            >
          </div>
        </div>
      </a-layout-footer> -->
    </a-layout>
  </div>
</template>

<script lang="ts" setup>
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalSider from '@/components/GlobalSider.vue'
</script>

<style scoped>
#basicLayout .footer {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 12px 40px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
  box-shadow: 0 -2px 12px rgba(102, 126, 234, 0.3);
}

#basicLayout .footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: center;
}

#basicLayout .footer-links a {
  color: white;
  font-size: 13px;
  text-decoration: none;
  transition: opacity 0.3s ease;
}

#basicLayout .footer-links a:hover {
  opacity: 0.8;
  text-decoration: underline;
}

#basicLayout .footer-info p {
  margin: 0;
  font-size: 12px;
  opacity: 0.9;
}

#basicLayout .content {
  background: transparent;
  margin-bottom: 50px;
  padding: 20px;
  position: relative;
  z-index: 1;
}

#basicLayout .header {
  padding-inline: 0;
  margin-bottom: 16px;
  color: unset;
  background: white;
  position: relative;
  z-index: 1;
}
</style>
