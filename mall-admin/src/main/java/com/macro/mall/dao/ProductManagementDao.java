package com.macro.mall.dao;

import com.macro.mall.dto.ProductManagementDashboard;
import com.macro.mall.dto.ProductManagementQueryParam;
import com.macro.mall.dto.ProductManagementResult;
import com.macro.mall.model.ProductManagement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductManagementDao {
    List<ProductManagementResult> getManagementList(@Param("param") ProductManagementQueryParam param);

    ProductManagementDashboard getDashboardData(@Param("assignee") String assignee);

    List<ProductManagement> getOverdueItems();

    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    int batchUpdateAssignee(@Param("ids") List<Long> ids, @Param("assignee") String assignee);

    int batchUpdatePriority(@Param("ids") List<Long> ids, @Param("priority") Integer priority);

    int insertProductManagement(ProductManagement productManagement);

    int updateProductManagement(ProductManagement productManagement);

    ProductManagementResult getById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);
}
