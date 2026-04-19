<template>
  <div id="pictureManagementPage">
    <a-flex justify="space-between">
      <h2>图片管理</h2>
      <a-button type="primary" href="/add/picture">+ 创建图片</a-button>
    </a-flex>

    <a-form layout="inline" :model="searchParams" @finish="doSearch" style="margin-bottom: 24px">
      <a-form-item label="关键词搜索">
        <a-input v-model:value="searchParams.searchText" placeholder="输入图片名称或者简介" />
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
        <template v-if="column.dataIndex === 'picCategory'">
          <a-tag v-if="record.picCategory" color="blue">{{ record.picCategory }}</a-tag>
        </template>
        <template v-if="column.title === '图片标签'">
          <a-space>
            <a-tag color="green" v-for="(picTag, index) in record.picTags">
              {{ picTag }}
            </a-tag>
          </a-space>
        </template>
        <template v-if="column.key === 'reviewAction'">
          <a-space>
            <a-button
              v-if="(record.reviewStatusText === '不通过' || record.reviewStatusText === '待审核')"
              type="primary" ghost
              @click="doReview(record.id,1)">
              通过
            </a-button>
            <a-button
              v-if="(record.reviewStatusText === '通过' || record.reviewStatusText === '待审核')" danger
              @click="doReview(record.id,2)">
              不通过
            </a-button>
          </a-space>
        </template>

        <template v-if="column.key === 'pictureAction'">
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
import { useRouter } from 'vue-router'
import { listUserVoByPageUsingPost } from '@/api/yonghumokuaideApi'
import dayjs from 'dayjs'
import { listPictureVoByPageUsingPost, reviewPictureUsingPost } from '@/api/pictureApi'

// --------------------  变量 --------------------
// 路由
const router = useRouter()
// 表头数据
const columns = [
  {
    title: 'id',
    dataIndex: 'id'
  },
  // {
  //   tile: '图片',
  //   dataIndex: 'picUrl'
  // },
  {
    title: '图片名称',
    dataIndex: 'picName'
  },
  {
    title: '图片简介',
    dataIndex: 'picIntroduction'
  },
  {
    title: '图片种类',
    dataIndex: 'picCategory'
  },
  {
    title: '图片标签',
    dataIndex: 'picTags'
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
    title: '审核状态',
    dataIndex: 'reviewStatusText'
  },
  {
    title: '审核信息',
    dataIndex: 'reviewMessage'
  },
  {
    title: '审核人',
    dataIndex: 'reviewUser'
  },
  {
    title: '审核时间',
    dataIndex: 'reviewTime'
  },
  {
    title: '审核操作',
    key: 'reviewAction'
  },
  {
    title: '图片操作',
    key: 'pictureAction'
  }
]

const dataList = ref<any>([])
const total = ref<number>(0)
const searchParams = reactive<API.PictureQueryRequest>({
  pageNum: 1,
  pageSize: 10,
  name: ''
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
  const res = await listPictureVoByPageUsingPost({
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
