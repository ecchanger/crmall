package com.macro.mall.demo.service;

import com.macro.mall.demo.model.Product;
import java.util.List;

public interface ProductService {
    /**
     * 创建商品
     */
    Product create(Product product);

    /**
     * 更新商品
     */
    Product update(Long id, Product product);

    /**
     * 删除商品
     */
    void delete(Long id);

    /**
     * 获取商品详情
     */
    Product getItem(Long id);

    /**
     * 获取商品列表
     */
    List<Product> list(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 更新商品上架状态
     */
    int updatePublishStatus(Long id, Integer status);
}
