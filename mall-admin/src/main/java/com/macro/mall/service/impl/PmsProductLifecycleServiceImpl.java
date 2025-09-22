package com.macro.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.macro.mall.dto.PmsProductDraftParam;
import com.macro.mall.dto.PmsProductDraftResult;
import com.macro.mall.dto.PmsProductParam;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 商品生命周期管理Service实现类
 * Created by macro on 2023/12/22.
 */
@Service
public class PmsProductLifecycleServiceImpl implements PmsProductLifecycleService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductLifecycleServiceImpl.class);
    
    @Autowired
    private PmsProductDraftService draftService;
    
    @Autowired
    private PmsProductReviewService reviewService;
    
    @Autowired
    private PmsProductService productService;
    
    @Autowired
    private PmsProductAuditService auditService;

    @Override
    public Long createProductDraft(PmsProductDraftParam draftParam) {
        try {
            // 创建草稿
            int result = draftService.createOrUpdateDraft(draftParam);
            if (result > 0) {
                // 记录操作日志
                auditService.logOperation(null, 1, "创建商品草稿", null, JSONUtil.toJsonStr(draftParam));
                
                // 获取创建的草稿ID（实际项目中应该从插入操作返回）
                PmsProductDraftResult draft = draftService.getLatestDraftByProductId(draftParam.getProductId());
                return draft != null ? draft.getId() : null;
            }
            return null;
            
        } catch (Exception e) {
            LOGGER.error("创建商品草稿失败", e);
            throw new RuntimeException("创建商品草稿失败: " + e.getMessage());
        }
    }

    @Override
    public int submitProductForReview(Long draftId, String submissionNotes) {
        try {
            // 提交草稿审核
            int result = draftService.submitDraftForReview(draftId, submissionNotes);
            
            if (result > 0) {
                // 获取草稿信息
                PmsProductDraftResult draft = draftService.getDraft(draftId);
                
                // 创建审核记录
                reviewService.createReview(draft.getProductId(), draftId, 1);
                
                // 记录操作日志
                auditService.logOperation(draft.getProductId(), 6, "提交商品审核", null, submissionNotes);
            }
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("提交商品审核失败", e);
            throw new RuntimeException("提交商品审核失败: " + e.getMessage());
        }
    }

    @Override
    public int publishProduct(Long draftId, Date publishTime) {
        try {
            PmsProductDraftResult draft = draftService.getDraft(draftId);
            if (draft == null) {
                throw new RuntimeException("草稿不存在");
            }
            
            if (draft.getStatus() != 2) {
                throw new RuntimeException("只有审核通过的草稿才能发布");
            }
            
            // 解析草稿内容
            PmsProductParam productParam = JSONUtil.toBean(draft.getContent(), PmsProductParam.class);
            
            int result;
            if (draft.getProductId() == null) {
                // 新商品发布
                result = productService.create(productParam);
            } else {
                // 更新现有商品
                result = productService.update(draft.getProductId(), productParam);
            }
            
            if (result > 0) {
                // 更新商品发布状态
                List<Long> productIds = Arrays.asList(draft.getProductId());
                productService.updatePublishStatus(productIds, 1);
                
                // 记录操作日志
                auditService.logOperation(draft.getProductId(), 4, "发布商品", null, 
                    "发布时间: " + (publishTime != null ? publishTime : new Date()));
            }
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("发布商品失败", e);
            throw new RuntimeException("发布商品失败: " + e.getMessage());
        }
    }

    @Override
    public int scheduleProductPublication(Long draftId, Date scheduleTime) {
        try {
            if (scheduleTime == null || scheduleTime.before(new Date())) {
                throw new RuntimeException("计划发布时间不能为空或早于当前时间");
            }
            
            PmsProductDraftResult draft = draftService.getDraft(draftId);
            if (draft == null) {
                throw new RuntimeException("草稿不存在");
            }
            
            if (draft.getStatus() != 2) {
                throw new RuntimeException("只有审核通过的草稿才能计划发布");
            }
            
            // TODO: 实现计划发布功能（可以使用定时任务或消息队列）
            // schedulePublicationTask(draftId, scheduleTime);
            
            // 记录操作日志
            auditService.logOperation(draft.getProductId(), 4, "计划发布商品", null, 
                "计划发布时间: " + scheduleTime);
            
            return 1;
            
        } catch (Exception e) {
            LOGGER.error("计划发布商品失败", e);
            throw new RuntimeException("计划发布商品失败: " + e.getMessage());
        }
    }

    @Override
    public int unpublishProduct(Long productId, String reason) {
        try {
            // 更新商品发布状态为下架
            List<Long> productIds = Arrays.asList(productId);
            int result = productService.updatePublishStatus(productIds, 0);
            
            if (result > 0) {
                // 记录操作日志
                auditService.logOperation(productId, 5, "下架商品", null, "下架原因: " + reason);
            }
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("下架商品失败", e);
            throw new RuntimeException("下架商品失败: " + e.getMessage());
        }
    }

    @Override
    public int republishProduct(Long productId) {
        try {
            // 更新商品发布状态为上架
            List<Long> productIds = Arrays.asList(productId);
            int result = productService.updatePublishStatus(productIds, 1);
            
            if (result > 0) {
                // 记录操作日志
                auditService.logOperation(productId, 4, "重新发布商品", null, null);
            }
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("重新发布商品失败", e);
            throw new RuntimeException("重新发布商品失败: " + e.getMessage());
        }
    }

    @Override
    public int archiveProduct(Long productId) {
        try {
            // 软删除商品（设置删除状态）
            List<Long> productIds = Arrays.asList(productId);
            int result = productService.updateDeleteStatus(productIds, 1);
            
            if (result > 0) {
                // 记录操作日志
                auditService.logOperation(productId, 3, "归档商品", null, null);
            }
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("归档商品失败", e);
            throw new RuntimeException("归档商品失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProductLifecycleStatus(Long productId) {
        Map<String, Object> status = new HashMap<>();
        
        try {
            // TODO: 获取商品信息
            // PmsProduct product = productService.getById(productId);
            
            // 获取最新草稿
            PmsProductDraftResult latestDraft = draftService.getLatestDraftByProductId(productId);
            
            // 获取审核历史
            List<com.macro.mall.model.PmsProductReview> reviewHistory = reviewService.getProductReviewHistory(productId);
            
            status.put("productId", productId);
            status.put("latestDraft", latestDraft);
            status.put("reviewHistory", reviewHistory);
            // status.put("currentStatus", product != null ? getProductStatusName(product) : "未知");
            status.put("canEdit", canPerformOperation(productId, "edit"));
            status.put("canPublish", canPerformOperation(productId, "publish"));
            status.put("canUnpublish", canPerformOperation(productId, "unpublish"));
            
        } catch (Exception e) {
            LOGGER.error("获取商品生命周期状态失败", e);
            status.put("error", e.getMessage());
        }
        
        return status;
    }

    @Override
    public List<Map<String, Object>> getProductOperationHistory(Long productId) {
        // TODO: 从审计日志获取操作历史
        return auditService.getProductOperationHistory(productId);
    }

    @Override
    public boolean canPerformOperation(Long productId, String operation) {
        try {
            // TODO: 根据商品当前状态和用户权限判断是否可以执行操作
            // PmsProduct product = productService.getById(productId);
            // if (product == null) return false;
            
            switch (operation.toLowerCase()) {
                case "edit":
                    return true; // 简化处理，实际应根据状态判断
                case "publish":
                    // 只有草稿状态或下架状态的商品可以发布
                    return true;
                case "unpublish":
                    // 只有已发布的商品可以下架
                    return true;
                case "delete":
                    // 只有下架状态的商品可以删除
                    return true;
                default:
                    return false;
            }
            
        } catch (Exception e) {
            LOGGER.error("检查操作权限失败", e);
            return false;
        }
    }

    @Override
    public int processScheduledPublications() {
        // TODO: 实现定时发布任务处理
        // 1. 查询所有计划发布时间已到的草稿
        // 2. 批量发布这些商品
        // 3. 更新发布状态
        
        LOGGER.info("处理计划发布任务");
        return 0;
    }

    @Override
    public Map<String, Object> getLifecycleStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            // 获取草稿统计
            Map<String, Object> draftStats = draftService.getDraftStatistics(getCurrentUserId());
            
            // 获取审核统计
            Map<String, Object> reviewStats = reviewService.getReviewStatistics(getCurrentUserId());
            
            statistics.put("drafts", draftStats);
            statistics.put("reviews", reviewStats);
            
            // TODO: 添加更多统计信息
            // 发布商品数量、待发布数量、下架数量等
            
        } catch (Exception e) {
            LOGGER.error("获取生命周期统计失败", e);
            statistics.put("error", e.getMessage());
        }
        
        return statistics;
    }
    
    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        // TODO: 从Spring Security获取
        return 1L;
    }
}