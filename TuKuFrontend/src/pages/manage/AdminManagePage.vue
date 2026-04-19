<template>
  <div class="adminManage">
    <a-form layout="inline" :model="searchParams" @finish="doSearch" style="margin-bottom: 24px">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
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
      <template #headerCell="{ column }">
        <template v-if="column.title === '账号'">
        <span>
          <smile-outlined />
          {{ column.title }}
        </span>
        </template>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image :src="record.userAvatar" :width="120" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="record.userRole === 'admin'">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.dataIndex === 'updateTime'">
          {{ dayjs(record.updateTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-button type="primary">编辑</a-button>
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

// --------------------  变量 --------------------
// 路由
const router = useRouter()
// 表头数据
const columns = [
  {
    title: 'id',
    dataIndex: 'id'
  },
  {
    title: '图片名称',
    dataIndex: 'picName'
  },
  {
    title: '用户名',
    dataIndex: 'userName'
  },
  {
    title: '头像',
    dataIndex: 'userAvatar'
  },
  {
    title: '简介',
    dataIndex: 'userProfile'
  },
  {
    title: '用户角色',
    dataIndex: 'userRole'
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
    title: '操作',
    key: 'action'
  }
]

const dataList = ref<any>([])
const total = ref<number>(0)
const searchParams = reactive<API.UserQueryDto>({
  pageNum: 1,
  pageSize: 10,
  userAccount: '',
  userName: ''
})

const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`
  }
})

// --------------------  函数 --------------------
// 获取数据
const fetchData = async () => {
  const res = await listUserVoByPageUsingPost({
    ...searchParams
  })
  if (res.data.data) {
    dataList.value = res.data.data.records ?? []
    total.value = res.data.data.total || 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}
// 表格变化处理
const doTableChange = (page: any) => {
  searchParams.pageNum = page.pageNum
  searchParams.pageSize = page.pageSize
  fetchData()
}
//搜索
const doSearch = () => {
  searchParams.pageNum = 1
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>

</style>
