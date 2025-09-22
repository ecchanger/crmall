package com.macro.mall.dao;

import com.macro.mall.model.PmsProductAuditLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 商品审计日志数据访问接口
 * Created by macro on 2023/12/22.
 */
public interface PmsProductAuditLogDao {
    
    /**
     * 插入审计日志
     */
    int insert(PmsProductAuditLog auditLog);
    
    /**
     * 批量插入审计日志
     */
    int insertList(@Param("list") List<PmsProductAuditLog> auditLogList);
    
    /**
     * 根据商品ID获取操作历史
     */
    List<PmsProductAuditLog> getProductOperationHistory(
        @Param("productId") Long productId,
        @Param("operationType") Integer operationType,
        @Param("pageSize") Integer pageSize,
        @Param("pageNum") Integer pageNum
    );
    
    /**
     * 根据操作人获取操作历史
     */
    List<PmsProductAuditLog> getOperatorHistory(
        @Param("operatorId") Long operatorId,
        @Param("startTime") java.util.Date startTime,
        @Param("endTime") java.util.Date endTime,
        @Param("pageSize") Integer pageSize,
        @Param("pageNum") Integer pageNum
    );
    
    /**
     * 获取操作统计信息
     */
    List<Map<String, Object>> getOperationStatistics(
        @Param("startTime") java.util.Date startTime,
        @Param("endTime") java.util.Date endTime,
        @Param("groupBy") String groupBy
    );
    
    /**
     * 删除过期日志
     */
    int deleteExpiredLogs(@Param("expireDays") Integer expireDays);
    
    /**
     * 根据时间范围获取日志数量
     */
    int countLogsByTimeRange(
        @Param("startTime") java.util.Date startTime,
        @Param("endTime") java.util.Date endTime
    );
}