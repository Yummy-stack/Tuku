package com.tuku.es.repository;


import com.tuku.es.document.PictureEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 图片 ES 操作
 */
public interface PictureEsRepository extends ElasticsearchRepository<PictureEsDTO, Long> {
}
