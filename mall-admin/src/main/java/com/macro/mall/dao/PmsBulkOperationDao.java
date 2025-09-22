package com.macro.mall.dao;

import com.macro.mall.model.PmsBulkOperation;
import com.macro.mall.dto.PmsBulkOperationResult;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 批量操作数据访问接口
 * Created by macro on 2023/12/22.
 */
public interface PmsBulkOperationDao {
    
    /**
     * 插入批量操作记录
     */
    int insert(PmsBulkOperation operation);
    
    /**
     * 批量插入操作记录
     */
    int insertList(@Param("list") List<PmsBulkOperation> operationList);
    
    /**
     * 更新操作进度
     */
    int updateProgress(
        @Param("id") Long id,
        @Param("progress") Integer progress,
        @Param("successCount") Integer successCount,
        @Param("failureCount") Integer failureCount
    );
    
    /**
     * 更新操作状态
     */
    int updateStatus(
        @Param("id") Long id,
        @Param("status") Integer status,
        @Param("errorMessage") String errorMessage
    );
    
    /**
     * 根据创建人获取操作历史
     */
    List<PmsBulkOperationResult> getOperationHistory(
        @Param("createdBy") Long createdBy,
        @Param("operationType") Integer operationType,
        @Param("pageSize") Integer pageSize,
        @Param("pageNum") Integer pageNum
    );
    
    /**
     * 获取正在执行的操作
     */
    List<PmsBulkOperationResult> getRunningOperations();
    
    /**
     * 根据ID获取操作详情
     */
    PmsBulkOperationResult getOperationDetail(@Param("id") Long id);
    
    /**
     * 删除过期操作记录
     */
    int deleteExpiredOperations(@Param("expireDays") Integer expireDays);
    
    /**
     * 获取操作统计信息
     */
    java.util.Map<String, Object> getOperationStatistics(@Param("createdBy") Long createdBy);
}