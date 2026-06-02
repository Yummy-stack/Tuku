package com.tuku.controller;

import com.google.gson.Gson;
import com.tuku.es.document.PictureEsDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

@SpringBootTest(classes = TukuWebApplication.class)
@Slf4j
public class ElasticSearchTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private ElasticsearchClient client;

    @Test
    void test1() {
        // ==================== 数据 1：风景类（日出） ====================
        PictureEsDTO dto1 = new PictureEsDTO();
        dto1.setId(1L);
        dto1.setPicName("高山日出金顶");
        dto1.setPicUrl("https://example.com/images/sunrise_mountain.jpg");
        dto1.setPicIntroduction("展现壮丽的高山日出景观，阳光洒在山尖，金光闪闪。");
        dto1.setPicCategory("风景");
        dto1.setPicTags(Arrays.asList("自然", "日出", "雪山", "壮丽"));
        dto1.setPicSize(524288L);          // 512KB
        dto1.setPicWidth(1920);
        dto1.setPicHeight(1080);
        dto1.setPicScale(1.7778);          // 16:9
        dto1.setPicFormat("jpg");
        dto1.setCreateUser(10001L);
        dto1.setUpdateUser(10001L);
        dto1.setCreateTime(new Date());
        dto1.setUpdateTime(new Date());
        dto1.setIsDelete(0);
        dto1.setEditTime(new Date());
        dto1.setReviewStatus(1);           // 1-审核通过
        dto1.setReviewMessage("审核通过，内容合规");
        dto1.setReviewUser(20001L);
        dto1.setReviewTime(new Date());
        dto1.setPicColor("#FF7F50");       // 珊瑚橙色调

// ==================== 数据 2：城市类（夜景） ====================
        PictureEsDTO dto2 = new PictureEsDTO();
        dto2.setId(2L);
        dto2.setPicName("繁华都市霓虹夜景");
        dto2.setPicUrl("https://example.com/images/city_night.png");
        dto2.setPicIntroduction("大都市市中心的繁华夜景，车流汇聚成光带，霓虹闪烁。");
        dto2.setPicCategory("城市");
        dto2.setPicTags(Arrays.asList("城市", "夜景", "建筑", "赛博朋克"));
        dto2.setPicSize(2097152L);         // 2MB
        dto2.setPicWidth(3840);
        dto2.setPicHeight(2160);
        dto2.setPicScale(1.7778);          // 4K 16:9
        dto2.setPicFormat("png");
        dto2.setCreateUser(10002L);
        dto2.setUpdateUser(10002L);
        dto2.setCreateTime(new Date());
        dto2.setUpdateTime(new Date());
        dto2.setIsDelete(0);
        dto2.setEditTime(new Date());
        dto2.setReviewStatus(1);
        dto2.setReviewMessage("自动审核通过");
        dto2.setReviewUser(20002L);
        dto2.setReviewTime(new Date());
        dto2.setPicColor("#0A1128");       // 深邃夜空蓝

// ==================== 数据 3：动物类（猫咪） ====================
        PictureEsDTO dto3 = new PictureEsDTO();
        dto3.setId(3L);
        dto3.setPicName("午后晒太阳的橘猫");
        dto3.setPicUrl("https://example.com/images/lazy_cat.webp");
        dto3.setPicIntroduction("一只可爱的橘猫躺在窗台边享受温暖的午后阳光，十分惬意。");
        dto3.setPicCategory("动物");
        dto3.setPicTags(Arrays.asList("宠物", "猫咪", "可爱", "治愈系"));
        dto3.setPicSize(102400L);          // 100KB
        dto3.setPicWidth(1200);
        dto3.setPicHeight(1200);
        dto3.setPicScale(1.0000);          // 1:1 正方形
        dto3.setPicFormat("webp");
        dto3.setCreateUser(10003L);
        dto3.setUpdateUser(10003L);
        dto3.setCreateTime(new Date());
        dto3.setUpdateTime(new Date());
        dto3.setIsDelete(0);
        dto3.setEditTime(new Date());
        dto3.setReviewStatus(1);
        dto3.setReviewMessage("内容优质，准予上线");
        dto3.setReviewUser(20001L);
        dto3.setReviewTime(new Date());
        dto3.setPicColor("#C19A6B");       // 温暖猫毛黄

// ==================== 数据 4：美食类（甜点） ====================
        PictureEsDTO dto4 = new PictureEsDTO();
        dto4.setId(4L);
        dto4.setPicName("精致法式草莓蛋糕");
        dto4.setPicUrl("https://example.com/images/strawberry_cake.jpg");
        dto4.setPicIntroduction("令人垂涎欲滴的法式草莓奶油蛋糕，细节清晰，色彩诱人。");
        dto4.setPicCategory("美食");
        dto4.setPicTags(Arrays.asList("美食", "甜点", "蛋糕", "草莓"));
        dto4.setPicSize(409600L);          // 400KB
        dto4.setPicWidth(1080);
        dto4.setPicHeight(1440);
        dto4.setPicScale(0.7500);          // 3:4 竖图
        dto4.setPicFormat("jpg");
        dto4.setCreateUser(10001L);
        dto4.setUpdateUser(10004L);
        dto4.setCreateTime(new Date());
        dto4.setUpdateTime(new Date());
        dto4.setIsDelete(0);
        dto4.setEditTime(new Date());
        dto4.setReviewStatus(1);
        dto4.setReviewMessage("符合美食板块规范");
        dto4.setReviewUser(20003L);
        dto4.setReviewTime(new Date());
        dto4.setPicColor("#E5A93A");       // 诱人烘焙色

// ==================== 数据 5：人像类（街拍） ====================
        PictureEsDTO dto5 = new PictureEsDTO();
        dto5.setId(5L);
        dto5.setPicName("复古街头时尚人像");
        dto5.setPicUrl("https://example.com/images/vintage_portrait.jpg");
        dto5.setPicIntroduction("模特身着复古风格服饰，在秋日街头漫步的时尚抓拍。");
        dto5.setPicCategory("人像");
        dto5.setPicTags(Arrays.asList("时尚", "街拍", "美女", "复古", "胶片"));
        dto5.setPicSize(819200L);          // 800KB
        dto5.setPicWidth(2048);
        dto5.setPicHeight(2732);
        dto5.setPicScale(0.7496);          // 竖图壁纸比例
        dto5.setPicFormat("jpg");
        dto5.setCreateUser(10005L);
        dto5.setUpdateUser(10005L);
        dto5.setCreateTime(new Date());
        dto5.setUpdateTime(new Date());
        dto5.setIsDelete(0);
        dto5.setEditTime(new Date());
        dto5.setReviewStatus(1);
        dto5.setReviewMessage("肖像授权齐全，审核通过");
        dto5.setReviewUser(20001L);
        dto5.setReviewTime(new Date());
        dto5.setPicColor("#4A4A4A");       // 高级复古灰

// ==================== 数据 6：动漫类（二次元） ====================
        PictureEsDTO dto6 = new PictureEsDTO();
        dto6.setId(6L);
        dto6.setPicName("幻想星空下的少女");
        dto6.setPicUrl("https://example.com/images/anime_starry_sky.png");
        dto6.setPicIntroduction("二次元插画，少女站在浩瀚星空下，充满梦幻和唯美感。");
        dto6.setPicCategory("动漫");
        dto6.setPicTags(Arrays.asList("插画", "二次元", "星空", "梦幻", "壁纸"));
        dto6.setPicSize(1572864L);         // 1.5MB
        dto6.setPicWidth(1920);
        dto6.setPicHeight(1080);
        dto6.setPicScale(1.7778);
        dto6.setPicFormat("png");
        dto6.setCreateUser(10006L);
        dto6.setUpdateUser(10006L);
        dto6.setCreateTime(new Date());
        dto6.setUpdateTime(new Date());
        dto6.setIsDelete(0);
        dto6.setEditTime(new Date());
        dto6.setReviewStatus(1);
        dto6.setReviewMessage("原创版权校验通过");
        dto6.setReviewUser(20002L);
        dto6.setReviewTime(new Date());
        dto6.setPicColor("#9D00FF");       // 梦幻紫罗兰

// ==================== 数据 7：建筑类（极简） ====================
        PictureEsDTO dto7 = new PictureEsDTO();
        dto7.setId(7L);
        dto7.setPicName("极简主义白墙建筑");
        dto7.setPicUrl("https://example.com/images/minimalist_building.jpg");
        dto7.setPicIntroduction("白色现代几何建筑在蓝天下的投影，线条干净，极具艺术感。");
        dto7.setPicCategory("建筑");
        dto7.setPicTags(Arrays.asList("建筑", "极简", "几何", "空间", "美学"));
        dto7.setPicSize(307200L);          // 300KB
        dto7.setPicWidth(1600);
        dto7.setPicHeight(1200);
        dto7.setPicScale(1.3333);          // 4:3
        dto7.setPicFormat("jpg");
        dto7.setCreateUser(10002L);
        dto7.setUpdateUser(10002L);
        dto7.setCreateTime(new Date());
        dto7.setUpdateTime(new Date());
        dto7.setIsDelete(0);
        dto7.setEditTime(new Date());
        dto7.setReviewStatus(1);
        dto7.setReviewMessage("审核通过");
        dto7.setReviewUser(20003L);
        dto7.setReviewTime(new Date());
        dto7.setPicColor("#EAEAEA");       // 极简纯净白

// ==================== 数据 8：运动类（冲浪） ====================
        PictureEsDTO dto8 = new PictureEsDTO();
        dto8.setId(8L);
        dto8.setPicName("浪尖上的巨浪冲浪者");
        dto8.setPicUrl("https://example.com/images/surfing_challenge.jpg");
        dto8.setPicIntroduction("抓拍冲浪运动员在巨大的蓝色海浪中穿梭的惊险瞬间。");
        dto8.setPicCategory("运动");
        dto8.setPicTags(Arrays.asList("极限运动", "冲浪", "海洋", "冒险", "抓拍"));
        dto8.setPicSize(614400L);          // 600KB
        dto8.setPicWidth(2560);
        dto8.setPicHeight(1440);
        dto8.setPicScale(1.7778);          // 2K 16:9
        dto8.setPicFormat("jpg");
        dto8.setCreateUser(10008L);
        dto8.setUpdateUser(10008L);
        dto8.setCreateTime(new Date());
        dto8.setUpdateTime(new Date());
        dto8.setIsDelete(0);
        dto8.setEditTime(new Date());
        dto8.setReviewStatus(1);
        dto8.setReviewMessage("审核通过，充满正能量");
        dto8.setReviewUser(20001L);
        dto8.setReviewTime(new Date());
        dto8.setPicColor("#00A8E8");       // 活力海洋蓝

// ==================== 数据 9：汽车类（超跑） ====================
        PictureEsDTO dto9 = new PictureEsDTO();
        dto9.setId(9L);
        dto9.setPicName("红色超跑赛道风驰电掣");
        dto9.setPicUrl("https://example.com/images/supercar_track.jpg");
        dto9.setPicIntroduction("一辆红色超级跑车在赛道上高速过弯，轮胎摩擦冒出轻烟。");
        dto9.setPicCategory("汽车");
        dto9.setPicTags(Arrays.asList("汽车", "超跑", "赛道", "速度与激情"));
        dto9.setPicSize(921600L);          // 900KB
        dto9.setPicWidth(1920);
        dto9.setPicHeight(1200);
        dto9.setPicScale(1.6000);          // 16:10
        dto9.setPicFormat("jpg");
        dto9.setCreateUser(10009L);
        dto9.setUpdateUser(10009L);
        dto9.setCreateTime(new Date());
        dto9.setUpdateTime(new Date());
        dto9.setIsDelete(0);
        dto9.setEditTime(new Date());
        dto9.setReviewStatus(1);
        dto9.setReviewMessage("审核通过");
        dto9.setReviewUser(20002L);
        dto9.setReviewTime(new Date());
        dto9.setPicColor("#D00000");       // 激情烈焰红

// ==================== 数据 10：星空类（银河） ====================
        PictureEsDTO dto10 = new PictureEsDTO();
        dto10.setId(10L);
        dto10.setPicName("无人区璀璨银河拱桥");
        dto10.setPicUrl("https://example.com/images/milky_way_arch.webp");
        dto10.setPicIntroduction("在远离城市光污染的无人区，长时间曝光拍摄的绝美银河拱桥全景图。");
        dto10.setPicCategory("风景");
        dto10.setPicTags(Arrays.asList("星空", "银河", "夜空", "天文", "摄影"));
        dto10.setPicSize(1258291L);         // 约1.2MB
        dto10.setPicWidth(4096);
        dto10.setPicHeight(2160);
        dto10.setPicScale(1.8963);          // 电影宽银幕比例
        dto10.setPicFormat("webp");
        dto10.setCreateUser(10010L);
        dto10.setUpdateUser(10010L);
        dto10.setCreateTime(new Date());
        dto10.setUpdateTime(new Date());
        dto10.setIsDelete(0);
        dto10.setEditTime(new Date());
        dto10.setReviewStatus(1);
        dto10.setReviewMessage("绝美星空推荐作品，审核通过");
        dto10.setReviewUser(20001L);
        dto10.setReviewTime(new Date());
        dto10.setPicColor("#03001C");       // 深邃星空黑蓝

        List<PictureEsDTO> pictureList = new ArrayList<>(Arrays.asList(
                dto1, dto2, dto3, dto4, dto5, dto6, dto7, dto8, dto9, dto10
        ));
        List<PictureEsDTO> pictureEsDTOS = new ArrayList<>();
        for (PictureEsDTO pictureEsDTO : pictureList) {
            PictureEsDTO esSaveResult = elasticsearchRestTemplate.save(pictureEsDTO);
            pictureEsDTOS.add(esSaveResult);
        }
        for (PictureEsDTO pictureEsDTO : pictureEsDTOS) {
            System.out.println(pictureEsDTO);
            System.out.println("\n");
        }
    }

    @Test
    void test2() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        boolQueryBuilder.must(QueryBuilders.matchQuery("picName", "人像"));
        FieldSortBuilder createTimeSort = SortBuilders.fieldSort("createTime").order(SortOrder.ASC);
        FieldSortBuilder updateTimeSort = SortBuilders.fieldSort("updateTime").order(SortOrder.DESC);

        HighlightBuilder.Field highLightFiled = new HighlightBuilder.Field("picName")
                .preTags("<span style='color:red'>")
                .postTags("</font>");
        HighlightBuilder.Field highLightFiled2 = new HighlightBuilder.Field("picIntroduction")
                .preTags("<span style='color:red'>")
                .postTags("</font>");

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(PageRequest.of(0, 10))
                .withSorts(createTimeSort, updateTimeSort)
                .withHighlightFields(highLightFiled, highLightFiled2)
                .build();

        SearchHits<PictureEsDTO> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, PictureEsDTO.class);

        System.out.println(searchHits.getTotalHits());
        Gson gson = new Gson();
        List<String> jsonPicList = new ArrayList<>();
        List<SearchHit<PictureEsDTO>> hitsSearchHits = searchHits.getSearchHits();
        for (SearchHit<PictureEsDTO> searchHits1 : hitsSearchHits) {
            String json = gson.toJson(searchHits1);
            jsonPicList.add(json);
        }
        for (String jsonPic : jsonPicList) {
            System.out.println(jsonPic);
            System.out.println("");
        }
    }

    @Test
    void test3() {

    }

}
