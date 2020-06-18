package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author: oyyb
 * @data: 2020-04-02
 * @version: 1.0.0
 * @descript:
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
