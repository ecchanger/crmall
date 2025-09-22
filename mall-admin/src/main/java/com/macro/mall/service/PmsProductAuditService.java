package com.macro.mall.service;

import com.macro.mall.model.PmsProductAuditLog;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * 商品审计日志Service接口
 * Created by macro on 2023/12/22.
 */
public interface PmsProductAuditService {
    
    /**
     * 记录操作日志
     */
    @Transactional
    int logOperation(Long productId, Integer operationType, String operationDesc, 
                     String beforeData, String afterData);
    
    /**
     * 获取商品操作历史
     */
    List<Map<String, Object>> getProductOperationHistory(Long productId);
    
    /**
     * 获取操作人历史记录
     */
    List<PmsProductAuditLog> getOperatorHistory(Long operatorId, 
                                                java.util.Date startTime, 
                                                java.util.Date endTime);
    
    /**
     * 获取操作统计
     */
    List<Map<String, Object>> getOperationStatistics(java.util.Date startTime, 
                                                      java.util.Date endTime);
    
    /**
     * 清理过期日志
     */
    @Transactional
    int cleanExpiredLogs(Integer expireDays);
}