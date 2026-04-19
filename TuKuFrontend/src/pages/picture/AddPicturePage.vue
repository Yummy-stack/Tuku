<template>
  <a-button @click="backToPicManage" :icon="h(LeftOutlined)"> 返回</a-button>

  <div id="addPicturePage">
    <h1 style="margin-bottom: 20px">图片上传</h1>
    <PictureUpload :picture="picture" :onSuccess="onSuccess" />
    <a-form
      v-if="picture"
      layout="vertical"
      :model="pictureEditForm"
      @finish="pictureEditFormSubmit"
    >
      <a-form-item label="名称" name="name">
        <a-input v-model:value="pictureEditForm.name" placeholder="请输入名称" allowClear />
      </a-form-item>
      <a-form-item label="简介" name="introduction">
        <a-textarea
          v-model:value="pictureEditForm.introduction"
          placeholder="请输入简介"
          :rows="2"
          autoSize
          allowClear
        />
      </a-form-item>
      <a-form-item label="分类" name="category">
        <a-auto-complete
          :options="categoryOptions"
          v-model:value="pictureEditForm.category"
          placeholder="请输入分类"
          allowClear
        />
      </a-form-item>
      <a-form-item label="标签" name="tags">
        <a-select
          v-model:value="pictureEditForm.tags"
          :options="tagOptions"
          mode="multiple"
          placeholder="请输入标签"
          allowClear
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">上传图片</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import PictureUpload from '@/components/PictureUpload.vue'
import { h, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { editPictureUsingPost, listPictureTagCategoryUsingGet } from '@/api/pictureApi'
import { message } from 'ant-design-vue'
import { LeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const tagOptions = ref<{ label: string; value: string }[]>([])
const categoryOptions = ref<{ label: string; value: string }[]>([])

const picture = ref<API.PictureVO>({})
const pictureEditForm = reactive<API.PictureEditRequest>({})

const onSuccess = async (newPicture: API.PictureVO) => {
  picture.value = newPicture
  pictureEditForm.name = newPicture.picName
}

const pictureEditFormSubmit = async (values: any) => {
  if (!picture.value.id) {
    message.error('请先上传图片')
    return
  }

  try {
    const res = await editPictureUsingPost({
      id: picture.value.id,
      ...values
    })

    if (res.data.code === 0) {
      message.success('图片上传成功')
      await router.push({
        path: `/picture/${picture.value.id}`
      })
    } else {
      message.error(res.data.message || '上传失败')
    }
  } catch (error) {
    message.error('上传失败，请稍后重试')
    console.error('上传失败:', error)
  }
}

const getTagsAndCategories = async () => {
  const res = await listPictureTagCategoryUsingGet()
  if (res.data.code === 0 && res.data.data) {
    tagOptions.value = res.data.data.tagList?.map((t: string) => ({ label: t, value: t })) ?? []
    categoryOptions.value =
      res.data.data.categoryList?.map((t: string) => ({ label: t, value: t })) ?? []
  } else {
    message.error(res.data.message)
  }
}

const backToPicManage = async () => {
  router.push({
    path: '/picture/management',
    replace:true,
  })
}

onMounted(() => {
  getTagsAndCategories()
})
</script>

<style scoped>
#addPicturePage {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px 0;
}

/* 标题样式 */
h1 {
  color: #1890ff;
  font-size: 36px;
  font-weight: 700;
  text-align: center;
  margin-bottom: 30px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
  letter-spacing: 2px;
  background: linear-gradient(135deg, #1890ff, #69c0ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  position: relative;
  display: inline-block;
  left: 50%;
  transform: translateX(-50%);
}

h1::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 10%;
  width: 80%;
  height: 3px;
  background: linear-gradient(135deg, #1890ff, #69c0ff);
  border-radius: 2px;
}

#addPicturePage {
  max-width: 720px;
  margin: 0 auto;
}

.tag-select,
.category-autocomplete {
  width: 320px;
}
</style>
