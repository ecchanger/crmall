package com.macro.mall.service;

import com.macro.mall.enums.ProductLifecycleStatus;
import com.macro.mall.model.ProductLifecycle;

import java.util.List;

/**
 * 商品生命周期管理服务接口
 * Created for Product Management Module Enhancement
 */
public interface ProductLifecycleService {

    /**
     * 创建商品生命周期记录
     */
    ProductLifecycle createLifecycle(Long productId);

    /**
     * 转换商品生命周期状态
     */
    ProductLifecycle transitionStatus(Long productId, ProductLifecycleStatus targetStatus, String notes, Long operatorId);

    /**
     * 提交商品审核
     */
    ProductLifecycle submitForReview(Long productId, Long operatorId);

    /**
     * 审核通过
     */
    ProductLifecycle approve(Long productId, Long reviewerId, String notes);

    /**
     * 审核拒绝
     */
    ProductLifecycle reject(Long productId, Long reviewerId, String notes);

    /**
     * 发布商品
     */
    ProductLifecycle publish(Long productId, Long operatorId);

    /**
     * 下架商品进入维护模式
     */
    ProductLifecycle enterMaintenance(Long productId, Long operatorId, String reason);

    /**
     * 从维护模式恢复
     */
    ProductLifecycle exitMaintenance(Long productId, Long operatorId);

    /**
     * 停产商品
     */
    ProductLifecycle discontinue(Long productId, Long operatorId, String reason);

    /**
     * 归档商品
     */
    ProductLifecycle archive(Long productId, Long operatorId);

    /**
     * 获取商品当前生命周期状态
     */
    ProductLifecycle getCurrentLifecycle(Long productId);

    /**
     * 获取商品生命周期历史
     */
    List<ProductLifecycle> getLifecycleHistory(Long productId);

    /**
     * 获取指定状态的商品列表
     */
    List<Long> getProductIdsByStatus(ProductLifecycleStatus status);

    /**
     * 批量转换状态
     */
    List<ProductLifecycle> batchTransitionStatus(List<Long> productIds, ProductLifecycleStatus targetStatus, String notes, Long operatorId);

    /**
     * 检查是否可以转换状态
     */
    boolean canTransition(Long productId, ProductLifecycleStatus targetStatus);

    /**
     * 获取下一个可能的状态
     */
    List<ProductLifecycleStatus> getNextPossibleStates(Long productId);

    /**
     * 自动处理过期的审核
     */
    void processExpiredReviews();

    /**
     * 获取待审核的商品数量
     */
    long getPendingReviewCount();

    /**
     * 分配审核员
     */
    ProductLifecycle assignReviewer(Long productId, Long reviewerId, Long operatorId);
}