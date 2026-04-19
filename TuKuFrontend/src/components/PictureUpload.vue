<template>
  <div id="pictureUpload">
    <a-upload
      name="avatar"
      list-type="picture-card"
      :show-upload-list="false"
      :before-upload="beforeUpload"
      @change="handleChange"
    >
      <img v-if="imageUrl" :src="imageUrl" alt="avatar" />
      <div v-else>
        <loading-outlined v-if="loading"></loading-outlined>
        <plus-outlined v-else></plus-outlined>
        <div class="ant-upload-text">点击上传图片</div>
      </div>
    </a-upload>
  </div>
</template>

<script lang="ts" setup>
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { message, type UploadProps } from 'ant-design-vue'
import { uploadPictureUsingPost1, uploadPictureV2UsingPost } from '@/api/pictureApi'
import { useRouter } from 'vue-router'
import { ref } from 'vue'

function getBase64(img: Blob, callback: (base64Url: string) => void) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result as string))
  reader.readAsDataURL(img)
}

interface Props {
  picture?: API.PictureVO | null
  onSuccess?: (newPicture: API.PictureVO) => void
}

const props = defineProps<Props>()
const loading = ref<boolean>(false)
const imageUrl = ref<string>('')
const router = useRouter()

const handleChange_before20260201 = async ({ file }: any) => {
  loading.value = true
  const params = props.picture ? { id: props.picture.id } : {}
  try {
    // 确保传递的是原始的File对象
    const originalFile = file.originFileObj || file.file || file

    // 先使用FileReader将文件转换为base64进行本地预览
    getBase64(originalFile, (base64Url) => {
      imageUrl.value = base64Url
    })

    const res = await uploadPictureUsingPost1(params, {}, originalFile)
    if (res.data.code === 0 && res.data.data) {
      message.success('图片上传成功')
      // 上传成功后，我们已经通过base64显示了预览图
      // 这里仍然保存后端返回的picUrl，以便后续使用
      props.onSuccess?.(res.data.data)
    } else {
      message.error('图片上传失败')
      // 上传失败时清空预览
      imageUrl.value = ''
    }
  } catch (e) {
    message.error('图片上传失败')
    // 上传失败时清空预览
    imageUrl.value = ''
  } finally {
    loading.value = false
  }
}


const handleChange = async ({ file }: any) => {
  loading.value = true
  const params = props.picture ? { id: props.picture.id } : {}
  try {
    // 确保传递的是原始的File对象
    const originalFile = file.originFileObj || file.file || file

    // 先使用FileReader将文件转换为base64进行本地预览
    getBase64(originalFile, (base64Url) => {
      imageUrl.value = base64Url
    })

    const res = await uploadPictureV2UsingPost(params, {}, originalFile)
    if (res.data.code === 0 && res.data.data) {
      message.success('图片上传成功')
      // 上传成功后，我们已经通过base64显示了预览图
      // 这里仍然保存后端返回的picUrl，以便后续使用
      props.onSuccess?.(res.data.data)
    } else {
      message.error('图片上传失败')
      // 上传失败时清空预览
      imageUrl.value = ''
    }
  } catch (e) {
    message.error('图片上传失败')
    // 上传失败时清空预览
    imageUrl.value = ''
  } finally {
    loading.value = false
  }
}

const beforeUpload = (file: UploadProps['fileList'][number]) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJpgOrPng) {
    message.error('You can only upload JPG file!')
    return false
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    message.error('Image must smaller than 2MB!')
    return false
  }
  return false // 阻止默认上传行为，只使用自定义上传逻辑
}
</script>

<style scoped>
#pictureUpload {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

/* 上传组件容器样式 */
#pictureUpload .ant-upload {
  width: 160px;
  height: 160px;
  border-radius: 12px;
  border: 2px dashed #d9d9d9;
  background-color: #fafafa;
  transition: all 0.3s ease;
  overflow: hidden;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

#pictureUpload .ant-upload:hover {
  border-color: #1890ff;
  background-color: #f0f9ff;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

#pictureUpload .ant-upload-select-picture-card i {
  font-size: 40px;
  color: #999;
  transition: all 0.3s ease;
  display: block;
  margin-bottom: 8px;
}

#pictureUpload .ant-upload:hover .ant-upload-select-picture-card i {
  color: #1890ff;
  transform: scale(1.1);
}

#pictureUpload .ant-upload-select-picture-card .ant-upload-text {
  margin-top: 12px;
  color: #666;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  text-align: center;
  line-height: 1.4;
}

#pictureUpload .ant-upload:hover .ant-upload-select-picture-card .ant-upload-text {
  color: #1890ff;
  font-weight: 600;
}

/* 加载状态样式 */
#pictureUpload .ant-upload .anticon-loading {
  animation: spin 1s linear infinite;
  color: #1890ff;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 已上传图片样式 */
#pictureUpload .ant-upload img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 10px;
  transition: transform 0.3s ease;
}

#pictureUpload .ant-upload:hover img {
  transform: scale(1.02);
}

/* 响应式调整 */
@media (max-width: 768px) {
  #pictureUpload .ant-upload {
    width: 140px;
    height: 140px;
  }

  #pictureUpload .ant-upload-select-picture-card i {
    font-size: 36px;
  }

  #pictureUpload .ant-upload-select-picture-card .ant-upload-text {
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  #pictureUpload .ant-upload {
    width: 120px;
    height: 120px;
  }

  #pictureUpload .ant-upload-select-picture-card i {
    font-size: 32px;
  }

  #pictureUpload .ant-upload-select-picture-card .ant-upload-text {
    font-size: 12px;
    margin-top: 8px;
  }
}

.picture-upload img {
  max-width: 100%;
  max-height: 480px;
}
</style>
