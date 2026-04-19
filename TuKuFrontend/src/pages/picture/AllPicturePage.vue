<template>

  <div class="allPicturePage">
    <div class="allPicturePage-pictures">
      <div class="search-bar">
        <a-input-search
          v-model:value="searchParams.searchText"
          placeholder="请输入图片名称或者简介"
          enter-button
          @search="doSearch"
          style="margin-bottom: 10px"
        />
      </div>

      <a-tabs v-model:activeKey=selectCategory @change="doSearchClearInput">
        <a-tab-pane key="all" tab="全部"></a-tab-pane>
        <a-tab-pane v-for="(category, index) in categoryList" :key="index" :tab="category"></a-tab-pane>
      </a-tabs>

      <div class="tag-bar">
        <span style="margin-right: 8px">标签： </span>
        <a-space :size="[0, 8]" wrap>
          <a-checkable-tag
            v-for="(tag, index) in tagList"
            :key="tag"
            v-model:checked="selectTags[index]"
            @change="doSearchClearInput"
          >
            {{ tag }}
          </a-checkable-tag>
        </a-space>
      </div>

      <a-list
        :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6}"
        :data-source="data"
        :pagination="pagination"
      >
        <template #renderItem="{ item }">
          <a-list-item @click.stop="catDetail(item.id)" style="padding: 0">
            <a-card hoverable>
              <template #cover>
                <img
                  alt="CAN NOT LOAD Driver"
                  :src="item.picUrl"
                  style="max-height: 300px; object-fit: contain"
                />
              </template>
              <a-card-meta :title="item.picName">
                <!--              <template #description>{{ item.picIntroduction }}</template>-->
                <template #description>
                  <a-space>
                    <a-tag v-if="item.picCategory" color="blue">{{ item.picCategory }}</a-tag>
                    <a-tag v-for="(tag, index) in item.picTags" color="green"> {{ tag }}</a-tag>
                  </a-space>
                </template>
              </a-card-meta>

            </a-card>
          </a-list-item>
        </template>
      </a-list>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  listPictureTagCategoryUsingGet,
  listPictureVoByPageUsingPost,
  listPictureVoByPageV2UsingPost
} from '@/api/pictureApi'
import { message } from 'ant-design-vue'
import router from '@/router'
import GlobalSider from '@/components/GlobalSider.vue'

const selectTags = ref<boolean[]>([])
const selectCategory = ref<string>('all')

const tagList = ref<string[]>([])
const categoryList = ref<string[]>([])

const data = ref<API.PictureVO[]>([])
const total = ref<number>(0)

const searchParams = reactive<API.PictureQueryRequest>({
  pageNum: 1,
  pageSize: 10,
  category: '',
  tags: []
})

const pagination = computed(() => {
  return {
    pageNum: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    onChange: (pageNum: number, pageSize: number) => {
      searchParams.pageNum = pageNum
      searchParams.pageSize = pageSize
      fetchData()
    }
  }
})

const doSearch = () => {
  searchParams.pageNum = 1
  fetchData()
}

const doSearchClearInput = () => {
  searchParams.searchText = ''
  fetchData()
}

const catDetail = (id: number) => {
  router.push({
    path: `/picture/${id}`,
    replace: false
  })
}

const handleChange = (tag: string, checked: boolean) => {
  console.log(tag, checked)
}

const fetchData = async () => {
  if (selectCategory.value === 'all') {
    searchParams.category = ''
  } else {
    const index = Number(selectCategory.value)
    searchParams.category = categoryList.value[index]
  }
  selectTags.value.forEach((selectTag, index) => {
    if (selectTag === true && index < tagList.value.length) {
      searchParams.tags?.push(tagList.value[index])
    }
  })
  const res = await listPictureVoByPageV2UsingPost({
    ...searchParams
  })
  searchParams.tags = []
  if (res.data.data) {
    data.value = res.data.data.records || []
    total.value = res.data.data.total || 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}

const getTagsAndCategories = async () => {
  const res = await listPictureTagCategoryUsingGet()
  if (res.data.code === 0 && res.data.data) {
    tagList.value = res.data.data.tagList ?? []
    categoryList.value = res.data.data.categoryList ?? []
  } else {
    message.error(res.data.message)
  }
}

onMounted(() => {
  fetchData()
  getTagsAndCategories()
})
</script>

<style scoped>
.tag-bar {
  margin-bottom: 20px;
}

.search-bar {
  max-width: 550px;
  margin: 0 auto;
}
</style>
