<template>
  <div class="detailPicturePage">
    <a-button @click="backAll" :icon="h(LeftOutlined)"> 返回</a-button>
    <a-row :gutter="[16,16]">
      <a-col :sm="24" :md="16" :xl="18">
        <a-card hoverable title="图片内容" style="width: 500px">
          <template #cover>
            <a-image
              alt="CAN NOT LOAD Driver"
              :src="pictureData?.picUrl"
            />
          </template>
        </a-card>
      </a-col>
      <a-col :sm="24" :md="8" :xl="6">
        <a-card hoverable title="图片信息" style="width: 300px" class="info-card">
          <a-descriptions :column="1">
            <a-descriptions-item label="图片名称">{{ pictureData?.picName ?? '-' }}</a-descriptions-item>
            <a-descriptions-item label="图片简介">{{ pictureData?.picIntroduction ?? '-' }}</a-descriptions-item>
<!--            <a-descriptions-item label="图片url路径">{{ pictureData?.picUrl ?? '-' }}</a-descriptions-item>-->
            <a-descriptions-item label="图片种类">
              <a-tag v-if="pictureData?.picCategory" color="blue"> {{ pictureData?.picCategory }}</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="图片标签">
              <a-tag v-for="(tag,index) in pictureData?.picTags" color="green"> {{ tag }}</a-tag>
            </a-descriptions-item>
<!--            <a-descriptions-item label="图片宽度">{{ pictureData?.picWidth ?? '-' }}</a-descriptions-item>-->
<!--            <a-descriptions-item label="图片高度">{{ pictureData?.picHeight ?? '-' }}</a-descriptions-item>-->
<!--            <a-descriptions-item label="图片宽高比例">{{ pictureData?.picScale ?? '-' }}</a-descriptions-item>-->
            <a-descriptions-item label="图片大小">{{ formatSize(pictureData?.picSize) ?? '-' }}</a-descriptions-item>
            <a-descriptions-item label="图片格式">{{ pictureData?.picFormat ?? '-' }}</a-descriptions-item>
            <a-descriptions-item label="图片创建人">{{ pictureData?.createUser ?? '-' }}</a-descriptions-item>
            <a-descriptions-item label="图片下载">
              <a-button type="primary" shape="round" @click="doDownload">
                <template #icon>
                  <DownloadOutlined />
                </template>
                图片下载
              </a-button>
            </a-descriptions-item>
            <a-descriptions-item label="图片操作">
              <a-space wrap>
                <a-button type="primary">
                  编辑
                  <template #icon>
                    <EditOutlined />
                  </template>
                </a-button>
                <a-button type="primary" danger>
                  删除
                  <template #icon>
                    <DeleteOutlined />
                  </template>
                </a-button>
              </a-space>
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { LeftOutlined, EditOutlined, DeleteOutlined, DownloadOutlined } from '@ant-design/icons-vue'
import { h, onMounted, reactive, ref } from 'vue'
import { getPictureVoByIdUsingGet } from '@/api/pictureApi'
import { message } from 'ant-design-vue'
import { downloadImage, formatSize } from '@/utils'

interface Props {
  id: number
}

const props = defineProps<Props>()

const router = useRouter()
const route = useRoute()

const pictureData = ref<API.PictureVO>()

const getDetailPicture = async () => {
  try {
    const res = await getPictureVoByIdUsingGet({
      id: props.id
    })
    if (res.data.code === 0 && res.data.data) {
      pictureData.value = res.data.data || null
    } else {
      message.error('获取图片详情失败')
    }
  } catch (e: any) {
    message.error(props.id)
  }
}

const backAll = () => {
  router.push({
    path:'/picture/all',
    replace: true,
  })
}

const doDownload = ()=> {
  downloadImage(pictureData.value?.picUrl)
}

onMounted(() => {
  getDetailPicture()
})

</script>

<style scoped>
.info-card :deep(.ant-descriptions-item-label),
.info-card :deep(.ant-descriptions-item-content) {
  white-space: normal;
  word-break: break-word;
  overflow-wrap: anywhere;
}

.info-card :deep(.ant-descriptions-view) {
  width: 100%;
}
</style>
