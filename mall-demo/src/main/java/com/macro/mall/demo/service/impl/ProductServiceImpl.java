package com.macro.mall.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.demo.mbg.mapper.PmsProductMapper;
import com.macro.mall.demo.mbg.model.PmsProduct;
import com.macro.mall.demo.model.Product;
import com.macro.mall.demo.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private PmsProductMapper productMapper;

    @Override
    public Product create(Product product) {
        PmsProduct pmsProduct = new PmsProduct();
        BeanUtils.copyProperties(product, pmsProduct);
        pmsProduct.setCreateTime(new Date());
        pmsProduct.setUpdateTime(new Date());
        productMapper.insert(pmsProduct);
        product.setId(pmsProduct.getId());
        return product;
    }

    @Override
    public Product update(Long id, Product product) {
        PmsProduct pmsProduct = new PmsProduct();
        BeanUtils.copyProperties(product, pmsProduct);
        pmsProduct.setId(id);
        pmsProduct.setUpdateTime(new Date());
        productMapper.updateByPrimaryKeySelective(pmsProduct);
        return product;
    }

    @Override
    public void delete(Long id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Product getItem(Long id) {
        PmsProduct pmsProduct = productMapper.selectByPrimaryKey(id);
        if (pmsProduct != null) {
            Product product = new Product();
            BeanUtils.copyProperties(pmsProduct, product);
            return product;
        }
        return null;
    }

    @Override
    public List<Product> list(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PmsProduct> productList = productMapper.selectByKeyword(keyword);
        return productList.stream().map(item -> {
            Product product = new Product();
            BeanUtils.copyProperties(item, product);
            return product;
        }).collect(Collectors.toList());
    }

    @Override
    public int updatePublishStatus(Long id, Integer status) {
        return productMapper.updatePublishStatus(id, status);
    }
}
