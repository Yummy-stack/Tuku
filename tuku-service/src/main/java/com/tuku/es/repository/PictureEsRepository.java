package com.tuku.es.repository;


import com.tuku.es.document.PictureEsDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * 图片 ES 操作
 */
@Component
public interface PictureEsRepository extends ElasticsearchRepository<PictureEsDTO, Long> {

}
