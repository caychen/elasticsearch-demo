package com.caychen.elasticsearch.dao;

import com.caychen.elasticsearch.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Caychen
 * @Date: 2021/4/24 15:29
 * @Description:
 */
@Repository
public interface IProductDao extends ElasticsearchRepository<Product, Long> {
}
