<template>
  <a-button @click="backTopicSpaManage" :icon="h(LeftOutlined)"> 返回</a-button>
  <div id="addPicSpaPage">
    <h1 style="margin-bottom: 20px">创建图片空间</h1>
    <!--    <PictureUpload :picture="picture" :onSuccess="onSuccess" />-->
    <a-form
      layout="vertical"
      :model="picSpaEditForm"
      @finish="picSpaCreateFormSubmit"
    >
      <a-form-item label="空间名称" name="spaceName">
        <a-input v-model:value="picSpaEditForm.spaceName" placeholder="请输入名称" allowClear />
      </a-form-item>
      <a-form-item label="空间级别" name="spaceLevel">
        <a-select
          v-model:value="picSpaEditForm.spaceLevel"
          :options="spaceLevelOptions"
          placeholder="请选择空间级别"
          allowClear
        />
      </a-form-item>
      <a-form-item>
        <a-button
          type="primary"
          html-type="submit"
          style="width: 100%"
          :icon="h(PlusOutlined)"
        >
          创建图片空间
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { editPictureUsingPost, listPictureTagCategoryUsingGet } from '@/api/pictureApi'
import { message } from 'ant-design-vue'
import { LeftOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { createPictureSpaceUsingPost, pictureSpaceLevelsUsingPost } from '@/api/pictureSpaceApi'
import { SPACE_LEVEL_MAP_V2 } from '@/constants/picSpaLevelEnum'

const router = useRouter()
const route = useRoute()

const tagOptions = ref<{ label: string; value: string }[]>([])
const categoryOptions = ref<{ label: string; value: string }[]>([])
const spaceLevelOptions = ref<{ label: string; value: number }[]>([])

const picture = ref<API.PictureVO>({})
const pictureEditForm = reactive<API.PictureEditRequest>({})
const picSpaEditForm = reactive<API.PicSpaCreateDto>({})

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

const picSpaCreateFormSubmit = async (values: any) => {
  const res = await createPictureSpaceUsingPost(values)
  if (res.data.code === 0 && res.data.data) {
    message.success('空间创建成功')
    await router.push({
      path: '/picture-space/management',
      replace: true
    })
  } else {
    message.error('空间创建失败')
  }

}

const getTagsAndCategories = async () => {
  const res = await listPictureTagCategoryUsingGet()
  if (res.data.code === 0 && res.data.data) {
    tagOptions.value = res.data.data.tagList?.map((t: string) => ({ label: t, value: t })) ?? []
    categoryOptions.value = res.data.data.categoryList?.map((t: string) => ({ label: t, value: t })) ?? []
  } else {
    message.error(res.data.message)
  }
}

const getSpaceLevels = async () => {
  const res = await pictureSpaceLevelsUsingPost()
  if (res.data.code === 0 && res.data.data) {
    spaceLevelOptions.value = res.data.data?.map((t: any) => ({
      label: t.text,
      value: SPACE_LEVEL_MAP_V2[t.text]
    })) ?? []
  } else {
    message.error(res.data.message)
  }
}

const backTopicSpaManage = async () => {
  router.push({
      path: `/picture-space/management`,
      replace: true
    }
  )
}

onMounted(() => {
  getTagsAndCategories()
  getSpaceLevels()
})

</script>

<style scoped>
#addPicSpaPage {
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
