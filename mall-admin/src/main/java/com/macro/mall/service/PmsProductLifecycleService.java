package com.macro.mall.service;

import com.macro.mall.dto.PmsProductDraftParam;
import com.macro.mall.dto.PmsProductDraftResult;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

/**
 * 商品生命周期管理Service接口
 * Created by macro on 2023/12/22.
 */
public interface PmsProductLifecycleService {
    
    /**
     * 创建新商品草稿
     */
    @Transactional
    Long createProductDraft(PmsProductDraftParam draftParam);
    
    /**
     * 提交商品审核
     */
    @Transactional
    int submitProductForReview(Long draftId, String submissionNotes);
    
    /**
     * 发布商品（审核通过后）
     */
    @Transactional
    int publishProduct(Long draftId, Date publishTime);
    
    /**
     * 计划发布商品
     */
    @Transactional
    int scheduleProductPublication(Long draftId, Date scheduleTime);
    
    /**
     * 下架商品
     */
    @Transactional
    int unpublishProduct(Long productId, String reason);
    
    /**
     * 重新发布商品
     */
    @Transactional
    int republishProduct(Long productId);
    
    /**
     * 归档商品
     */
    @Transactional
    int archiveProduct(Long productId);
    
    /**
     * 获取商品生命周期状态
     */
    java.util.Map<String, Object> getProductLifecycleStatus(Long productId);
    
    /**
     * 获取商品操作历史
     */
    java.util.List<java.util.Map<String, Object>> getProductOperationHistory(Long productId);
    
    /**
     * 检查商品是否可以执行指定操作
     */
    boolean canPerformOperation(Long productId, String operation);
    
    /**
     * 批量处理计划发布的商品
     */
    @Transactional
    int processScheduledPublications();
    
    /**
     * 获取生命周期统计信息
     */
    java.util.Map<String, Object> getLifecycleStatistics();
}