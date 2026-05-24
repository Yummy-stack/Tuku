package com.tuku.controller;

import com.tuku.es.document.PictureEsDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
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

    @Test
    void test1() {
        // 创建 PictureEsDTO 对象并设置每个字段的测试值
        PictureEsDTO pictureEsDTO = new PictureEsDTO();
        pictureEsDTO.setId(1L);
        pictureEsDTO.setPicName("示例图片");
        pictureEsDTO.setPicUrl("https://example.com/sample.jpg");
        pictureEsDTO.setPicIntroduction("这是一张用于测试的示例图片介绍。");
        pictureEsDTO.setPicCategory("风景");
        pictureEsDTO.setPicTags(Arrays.asList("自然", "森林", "河流"));
        pictureEsDTO.setPicSize(204800L);          // 200KB
        pictureEsDTO.setPicWidth(1920);
        pictureEsDTO.setPicHeight(1080);
        pictureEsDTO.setPicScale(1.7778);          // 16:9
        pictureEsDTO.setPicFormat("jpg");
        pictureEsDTO.setCreateUser(10001L);
        pictureEsDTO.setUpdateUser(10001L);
        pictureEsDTO.setCreateTime(new Date());
        pictureEsDTO.setUpdateTime(new Date());
        pictureEsDTO.setIsDelete(0);               // 0-未删除
        pictureEsDTO.setEditTime(new Date());
        pictureEsDTO.setReviewStatus(1);           // 1-审核通过
        pictureEsDTO.setReviewMessage("审核通过，内容合规");
        pictureEsDTO.setReviewUser(20001L);
        pictureEsDTO.setReviewTime(new Date());
        pictureEsDTO.setPicColor("#3A7B5A");

        PictureEsDTO esSaveResult = elasticsearchRestTemplate.save(pictureEsDTO);
        SearchRequest searchRequest = new SearchRequest();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        
        System.out.println(esSaveResult);
    }
}
