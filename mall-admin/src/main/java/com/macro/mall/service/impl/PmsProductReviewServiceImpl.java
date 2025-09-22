package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.dao.PmsProductReviewDao;
import com.macro.mall.model.PmsProductReview;
import com.macro.mall.service.PmsProductReviewService;
import com.macro.mall.service.PmsProductDraftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品审核管理Service实现类
 * Created by macro on 2023/12/22.
 */
@Service
public class PmsProductReviewServiceImpl implements PmsProductReviewService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductReviewServiceImpl.class);
    
    @Autowired
    private PmsProductReviewDao productReviewDao;
    
    @Autowired
    private PmsProductDraftService productDraftService;

    @Override
    public int createReview(Long productId, Long draftId, Integer level) {
        try {
            PmsProductReview review = new PmsProductReview();
            review.setProductId(productId);
            review.setDraftId(draftId);
            review.setLevel(level != null ? level : 1);
            review.setStatus(0); // 待审核
            review.setCreateTime(new Date());
            
            // TODO: 根据规则分配审核人
            // review.setReviewerId(assignReviewer(level));
            
            return productReviewDao.insert(review);
            
        } catch (Exception e) {
            LOGGER.error("创建审核记录失败", e);
            throw new RuntimeException("创建审核记录失败: " + e.getMessage());
        }
    }

    @Override
    public int approveReview(Long reviewId, String comments) {
        try {
            PmsProductReview review = productReviewDao.selectByPrimaryKey(reviewId);
            if (review == null) {
                throw new RuntimeException("审核记录不存在");
            }
            
            if (review.getStatus() != 0) {
                throw new RuntimeException("审核记录状态不正确");
            }
            
            // 更新审核记录
            review.setStatus(1); // 审核通过
            review.setComments(comments);
            review.setReviewerId(getCurrentUserId());
            review.setReviewerName(getCurrentUserName());
            review.setReviewTime(new Date());
            
            int result = productReviewDao.updateByPrimaryKeySelective(review);
            
            // 如果是最后一级审核，更新草稿状态为审核通过
            if (isLastLevelReview(review)) {
                productDraftService.updateDraftStatus(review.getDraftId(), 2);
                
                // TODO: 触发商品发布流程
                // publishProduct(review.getDraftId());
            } else {
                // 创建下一级审核
                createReview(review.getProductId(), review.getDraftId(), review.getLevel() + 1);
            }
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("审核通过操作失败", e);
            throw new RuntimeException("审核通过操作失败: " + e.getMessage());
        }
    }

    @Override
    public int rejectReview(Long reviewId, String comments) {
        try {
            PmsProductReview review = productReviewDao.selectByPrimaryKey(reviewId);
            if (review == null) {
                throw new RuntimeException("审核记录不存在");
            }
            
            if (review.getStatus() != 0) {
                throw new RuntimeException("审核记录状态不正确");
            }
            
            // 更新审核记录
            review.setStatus(2); // 审核拒绝
            review.setComments(comments);
            review.setReviewerId(getCurrentUserId());
            review.setReviewerName(getCurrentUserName());
            review.setReviewTime(new Date());
            
            int result = productReviewDao.updateByPrimaryKeySelective(review);
            
            // 更新草稿状态为审核拒绝
            productDraftService.updateDraftStatus(review.getDraftId(), 3);
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("审核拒绝操作失败", e);
            throw new RuntimeException("审核拒绝操作失败: " + e.getMessage());
        }
    }

    @Override
    public int requireModification(Long reviewId, String comments) {
        try {
            PmsProductReview review = productReviewDao.selectByPrimaryKey(reviewId);
            if (review == null) {
                throw new RuntimeException("审核记录不存在");
            }
            
            if (review.getStatus() != 0) {
                throw new RuntimeException("审核记录状态不正确");
            }
            
            // 更新审核记录
            review.setStatus(3); // 需要修改
            review.setComments(comments);
            review.setReviewerId(getCurrentUserId());
            review.setReviewerName(getCurrentUserName());
            review.setReviewTime(new Date());
            
            int result = productReviewDao.updateByPrimaryKeySelective(review);
            
            // 更新草稿状态为编辑中
            productDraftService.updateDraftStatus(review.getDraftId(), 0);
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("要求修改操作失败", e);
            throw new RuntimeException("要求修改操作失败: " + e.getMessage());
        }
    }

    @Override
    public CommonPage<PmsProductReview> getPendingReviews(Long reviewerId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<PmsProductReview> reviews = productReviewDao.getPendingReviews(reviewerId);
        return CommonPage.restPage(reviews);
    }

    @Override
    public List<PmsProductReview> getProductReviewHistory(Long productId) {
        return productReviewDao.getReviewsByProductId(productId);
    }

    @Override
    public PmsProductReview getReviewDetail(Long reviewId) {
        return productReviewDao.selectByPrimaryKey(reviewId);
    }

    @Override
    public int batchReview(List<Long> reviewIds, Integer status, String comments) {
        if (CollectionUtils.isEmpty(reviewIds)) {
            return 0;
        }
        
        int count = 0;
        for (Long reviewId : reviewIds) {
            try {
                switch (status) {
                    case 1:
                        count += approveReview(reviewId, comments);
                        break;
                    case 2:
                        count += rejectReview(reviewId, comments);
                        break;
                    case 3:
                        count += requireModification(reviewId, comments);
                        break;
                    default:
                        LOGGER.warn("不支持的审核状态: {}", status);
                }
            } catch (Exception e) {
                LOGGER.warn("批量审核处理审核记录 {} 失败: {}", reviewId, e.getMessage());
            }
        }
        return count;
    }

    @Override
    public Map<String, Object> getReviewStatistics(Long reviewerId) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 获取审核统计数据
        int pendingCount = productReviewDao.countReviewsByStatus(reviewerId, 0);
        int approvedCount = productReviewDao.countReviewsByStatus(reviewerId, 1);
        int rejectedCount = productReviewDao.countReviewsByStatus(reviewerId, 2);
        int modificationCount = productReviewDao.countReviewsByStatus(reviewerId, 3);
        
        statistics.put("pendingCount", pendingCount);
        statistics.put("approvedCount", approvedCount);
        statistics.put("rejectedCount", rejectedCount);
        statistics.put("modificationCount", modificationCount);
        statistics.put("totalCount", pendingCount + approvedCount + rejectedCount + modificationCount);
        
        // 计算审核效率
        if (approvedCount + rejectedCount + modificationCount > 0) {
            double efficiency = (double) approvedCount / (approvedCount + rejectedCount + modificationCount) * 100;
            statistics.put("approvalRate", Math.round(efficiency * 100.0) / 100.0);
        } else {
            statistics.put("approvalRate", 0.0);
        }
        
        return statistics;
    }

    @Override
    public int assignReviewer(Long reviewId, Long reviewerId) {
        try {
            PmsProductReview review = productReviewDao.selectByPrimaryKey(reviewId);
            if (review == null) {
                throw new RuntimeException("审核记录不存在");
            }
            
            review.setReviewerId(reviewerId);
            // TODO: 获取审核人姓名
            // review.setReviewerName(getUserName(reviewerId));
            
            return productReviewDao.updateByPrimaryKeySelective(review);
            
        } catch (Exception e) {
            LOGGER.error("分配审核人失败", e);
            throw new RuntimeException("分配审核人失败: " + e.getMessage());
        }
    }
    
    /**
     * 判断是否为最后一级审核
     */
    private boolean isLastLevelReview(PmsProductReview review) {
        // TODO: 根据配置判断最大审核级别
        return review.getLevel() >= 3; // 假设最多3级审核
    }
    
    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        // TODO: 从Spring Security获取
        return 1L;
    }
    
    /**
     * 获取当前用户名
     */
    private String getCurrentUserName() {
        // TODO: 从Spring Security获取
        return "admin";
    }
}