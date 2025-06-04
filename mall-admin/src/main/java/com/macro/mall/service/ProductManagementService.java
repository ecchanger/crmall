package com.macro.mall.service;

import com.macro.mall.dto.ProductManagementDashboard;
import com.macro.mall.dto.ProductManagementParam;
import com.macro.mall.dto.ProductManagementQueryParam;
import com.macro.mall.dto.ProductManagementResult;
import com.macro.mall.model.ProductManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductManagementService {
    @Transactional
    int create(ProductManagementParam param);

    @Transactional
    int update(Long id, ProductManagementParam param);

    List<ProductManagement> list(ProductManagementQueryParam param, Integer pageSize, Integer pageNum);

    ProductManagementResult getById(Long id);

    ProductManagementDashboard getDashboard(String assignee);

    @Transactional
    int batchAssign(List<Long> ids, String assignee);

    @Transactional
    int batchUpdatePriority(List<Long> ids, Integer priority);

    @Transactional
    int batchUpdateStatus(List<Long> ids, Integer status);

    List<ProductManagement> getOverdueItems();

    @Transactional
    int delete(Long id);
}
