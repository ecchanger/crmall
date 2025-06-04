package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.dao.ProductManagementDao;
import com.macro.mall.dto.ProductManagementDashboard;
import com.macro.mall.dto.ProductManagementParam;
import com.macro.mall.dto.ProductManagementQueryParam;
import com.macro.mall.dto.ProductManagementResult;
import com.macro.mall.model.ProductManagement;
import com.macro.mall.service.ProductManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductManagementServiceImpl implements ProductManagementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductManagementServiceImpl.class);

    @Autowired
    private ProductManagementDao productManagementDao;

    @Override
    public int create(ProductManagementParam param) {
        ProductManagement productManagement = new ProductManagement();
        BeanUtils.copyProperties(param, productManagement);
        productManagement.setCreateTime(new Date());
        productManagement.setUpdateTime(new Date());
        return productManagementDao.insertProductManagement(productManagement);
    }

    @Override
    public int update(Long id, ProductManagementParam param) {
        ProductManagement productManagement = new ProductManagement();
        BeanUtils.copyProperties(param, productManagement);
        productManagement.setId(id);
        productManagement.setUpdateTime(new Date());
        return productManagementDao.updateProductManagement(productManagement);
    }

    @Override
    public List<ProductManagement> list(ProductManagementQueryParam param, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return productManagementDao.getManagementList(param);
    }

    @Override
    public ProductManagementResult getById(Long id) {
        return productManagementDao.getById(id);
    }

    @Override
    public ProductManagementDashboard getDashboard(String assignee) {
        return productManagementDao.getDashboardData(assignee);
    }

    @Override
    public int batchAssign(List<Long> ids, String assignee) {
        return productManagementDao.batchUpdateAssignee(ids, assignee);
    }

    @Override
    public int batchUpdatePriority(List<Long> ids, Integer priority) {
        return productManagementDao.batchUpdatePriority(ids, priority);
    }

    @Override
    public int batchUpdateStatus(List<Long> ids, Integer status) {
        return productManagementDao.batchUpdateStatus(ids, status);
    }

    @Override
    public List<ProductManagement> getOverdueItems() {
        return productManagementDao.getOverdueItems();
    }

    @Override
    public int delete(Long id) {
        return productManagementDao.deleteById(id);
    }
}
