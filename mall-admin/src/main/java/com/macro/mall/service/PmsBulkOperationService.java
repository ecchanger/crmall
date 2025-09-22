package com.macro.mall.service;

import com.macro.mall.dto.PmsBulkOperationParam;
import com.macro.mall.dto.PmsBulkOperationResult;
import com.macro.mall.common.api.CommonPage;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * 批量操作管理Service接口
 * Created by macro on 2023/12/22.
 */
public interface PmsBulkOperationService {
    
    /**
     * 批量更新商品
     */
    @Transactional
    PmsBulkOperationResult bulkUpdateProducts(PmsBulkOperationParam operationParam);
    
    /**
     * 批量修改商品状态
     */
    @Transactional
    PmsBulkOperationResult bulkChangeProductStatus(List<Long> productIds, 
                                                  String statusType, 
                                                  Integer statusValue, 
                                                  String description);
    
    /**
     * 批量删除商品
     */
    @Transactional
    PmsBulkOperationResult bulkDeleteProducts(List<Long> productIds, String reason);
    
    /**
     * 批量复制商品
     */
    @Transactional
    PmsBulkOperationResult bulkCopyProducts(List<Long> productIds, String nameSuffix);
    
    /**
     * 批量移动商品分类
     */
    @Transactional
    PmsBulkOperationResult bulkMoveToCategory(List<Long> productIds, Long targetCategoryId);
    
    /**
     * 批量设置商品品牌
     */
    @Transactional
    PmsBulkOperationResult bulkSetBrand(List<Long> productIds, Long brandId);
    
    /**
     * 批量调整价格
     */
    @Transactional
    PmsBulkOperationResult bulkAdjustPrices(List<Long> productIds, 
                                           String adjustType, 
                                           java.math.BigDecimal adjustValue);
    
    /**
     * 批量更新库存
     */
    @Transactional
    PmsBulkOperationResult bulkUpdateStock(Map<Long, Integer> productStockMap);
    
    /**
     * 验证批量操作
     */
    Map<String, Object> validateBulkOperation(PmsBulkOperationParam operationParam);
    
    /**
     * 获取操作进度
     */
    PmsBulkOperationResult getOperationProgress(Long operationId);
    
    /**
     * 取消批量操作
     */
    int cancelOperation(Long operationId);
    
    /**
     * 获取操作历史
     */
    CommonPage<PmsBulkOperationResult> getOperationHistory(Long userId, 
                                                          Integer operationType, 
                                                          Integer pageNum, 
                                                          Integer pageSize);
    
    /**
     * 重试失败的操作
     */
    @Transactional
    PmsBulkOperationResult retryFailedOperation(Long operationId);
    
    /**
     * 获取操作统计
     */
    Map<String, Object> getOperationStatistics(Long userId);
    
    /**
     * 清理过期操作记录
     */
    @Transactional
    int cleanExpiredOperations(Integer expireDays);
}