package com.caychen.elasticsearch.service;

import com.caychen.elasticsearch.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: Caychen
 * @Date: 2021/4/24 15:27
 * @Description:
 */
public interface IProductService {

    Product create(Product product);

    Product update(Product product);

    List<Product> getAll();

    Product getById(Long id);

    Page<Product> pageSelect(Integer pageIndex, Integer pageSize);

    Product delete(Long id);

    List<Product> search(Product product);
}
