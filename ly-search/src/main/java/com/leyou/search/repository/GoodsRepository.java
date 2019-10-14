package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/27 0027 - 下午 13:52
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long>{
}
