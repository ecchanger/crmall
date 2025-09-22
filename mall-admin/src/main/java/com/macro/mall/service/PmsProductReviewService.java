package com.macro.mall.service;

import com.macro.mall.model.PmsProductReview;
import com.macro.mall.common.api.CommonPage;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 商品审核管理Service接口
 * Created by macro on 2023/12/22.
 */
public interface PmsProductReviewService {
    
    /**
     * 创建审核记录
     */
    @Transactional
    int createReview(Long productId, Long draftId, Integer level);
    
    /**
     * 审核通过
     */
    @Transactional
    int approveReview(Long reviewId, String comments);
    
    /**
     * 审核拒绝
     */
    @Transactional
    int rejectReview(Long reviewId, String comments);
    
    /**
     * 需要修改
     */
    @Transactional
    int requireModification(Long reviewId, String comments);
    
    /**
     * 获取待审核列表
     */
    CommonPage<PmsProductReview> getPendingReviews(Long reviewerId, Integer pageSize, Integer pageNum);
    
    /**
     * 获取商品审核历史
     */
    List<PmsProductReview> getProductReviewHistory(Long productId);
    
    /**
     * 获取审核详情
     */
    PmsProductReview getReviewDetail(Long reviewId);
    
    /**
     * 批量审核
     */
    @Transactional
    int batchReview(List<Long> reviewIds, Integer status, String comments);
    
    /**
     * 获取审核统计
     */
    java.util.Map<String, Object> getReviewStatistics(Long reviewerId);
    
    /**
     * 分配审核人
     */
    @Transactional
    int assignReviewer(Long reviewId, Long reviewerId);
}