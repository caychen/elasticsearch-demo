package com.caychen.elasticsearch.service.impl;

import com.caychen.elasticsearch.dao.IProductDao;
import com.caychen.elasticsearch.entity.Product;
import com.caychen.elasticsearch.service.IProductService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: Caychen
 * @Date: 2021/4/24 15:27
 * @Description:
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductDao productDao;

    @Override
    public Product create(Product product) {
        return productDao.save(product);
    }

    @Override
    public Product update(Product product) {
        return productDao.save(product);
    }

    @Override
    public List<Product> getAll() {
        Iterable<Product> products = productDao.findAll();
        return getProductList(products);
    }

    @Override
    public Product getById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    @Override
    public Page<Product> pageSelect(Integer pageIndex, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);
        Page<Product> page = productDao.findAll(pageRequest);
        return page;
    }

    @Override
    public Product delete(Long id) {
        Product product = productDao.findById(id).orElse(null);
        productDao.deleteById(id);

        return product;
    }

    @Override
    public List<Product> search(Product product) {
        Iterable<Product> products =
                productDao.search(QueryBuilders.termQuery("title", product.getTitle()));

        return getProductList(products);
    }

    private List<Product> getProductList(Iterable<Product> products) {
        Iterator<Product> iterator = products.iterator();

        List<Product> productList = new ArrayList<>();
        while (iterator.hasNext()) {
            Product pro = iterator.next();
            productList.add(pro);
        }
        return productList;
    }
}
