<template>
  <div id="pictureSpaceManagementPage">
    <a-flex justify="space-between">
      <h2>空间管理</h2>
      <a-button type="primary" href="/add/picture-space">+ 创建空间</a-button>
    </a-flex>

    <a-form
      layout="inline"
      :model="searchParams"
      @finish="doSearch"
      style="margin-bottom: 24px"
    >
      <a-form-item label="关键词搜索">
        <a-input v-model:value="searchParams.spaceName" placeholder="输入空间名称" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>

    <a-table
      :columns="columns"
      :data-source="dataList"
      :pagination="pagination"
      @change="doTableChange"
      :scroll="{ x: 'max-content' }"
    >
      <template #headerCell="{ column }"></template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'spaceLevel'">
          <a-tag color="blue"> {{ SPACE_LEVEL_MAP[record.spaceLevel] }}</a-tag>
        </template>

        <template v-if="column.dataIndex === 'picSpaUsage'">
          <div>当前空间下图片的总大小：{{ record.currentSize }} / {{ record.spaceMaxSize }}</div>
          <div>当前空间下图片的总数量： {{ record.currentCount }} / {{ record.spaceMaxNumber }}</div>
        </template>

        <template v-if="column.key === 'picSpaAction'">
          <a-space>
            <a-button type="primary"> 编辑</a-button>
            <a-button type="primary" danger> 删除</a-button>
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { SmileOutlined, DownOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { listUserVoByPageUsingPost } from '@/api/yonghumokuaideApi'
import dayjs from 'dayjs'
import { listPictureVoByPageUsingPost, reviewPictureUsingPost } from '@/api/pictureApi'
import { listPictureSpaceByPageUsingPost } from '@/api/pictureSpaceApi'
import { SPACE_LEVEL_MAP } from '@/constants/picSpaLevelEnum'

// --------------------  变量 --------------------
// 路由
const router = useRouter()
const route = useRoute()
// 表头数据
const columns = [
  {
    title: 'id',
    dataIndex: 'id'
  },
  {
    title: '空间名称',
    dataIndex: 'spaceName'
  },
  {
    title: '空间级别',
    dataIndex: 'spaceLevel'
  },
  {
    title: '使用情况',
    dataIndex: 'picSpaUsage'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime'
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime'
  },
  {
    title: '操作空间',
    key: 'picSpaAction'
  }
]

const dataList = ref<any>([])
const total = ref<number>(0)

const searchParams = reactive<API.PicSpaPageListDto>({
  pageNum: 1,
  pageSize: 10,
  spaceName: '',
  leftCreateTime: '',
  rightCreateTime: ''
})

const pagination = computed(() => {
  return {
    pageNum: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`
  }
})

const fetchData = async () => {
  const res = await listPictureSpaceByPageUsingPost({
    ...searchParams
  })
  if (res.data.data) {
    dataList.value = res.data.data.records || []
    total.value = res.data.data.total || 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}

const doTableChange = (page: any) => {
  searchParams.pageNum = page.pageNum
  searchParams.pageSize = page.pageSize
  fetchData()
}

const doSearch = () => {
  searchParams.pageNum = 1
  fetchData()
}

const doReview = async (value: number, reviewStatus: number) => {
  const reviewMessage = reviewStatus === 1 ? '管理员审核通过' : '管理员审核不通过'
  const params = {
    id: value,
    reviewStatus: reviewStatus,
    reviewMessage: reviewMessage
  }
  const res = await reviewPictureUsingPost(params)
  if (res.data.code === 0 && res.data.data) {
    fetchData()
  } else {
    message.error('审核失败')
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>

</style>
