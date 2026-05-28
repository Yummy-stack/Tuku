package com.tuku.es.repository;

import com.tuku.es.document.QuestionEsDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * 题目 ES 操作
 */
@Component
public interface QuestionEsRepository extends ElasticsearchRepository<QuestionEsDoc, Long> {

}
