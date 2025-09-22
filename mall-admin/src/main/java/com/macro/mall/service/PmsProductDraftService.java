package com.macro.mall.service;

import com.macro.mall.dto.PmsProductDraftParam;
import com.macro.mall.dto.PmsProductDraftResult;
import com.macro.mall.model.PmsProductDraft;
import com.macro.mall.common.api.CommonPage;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 商品草稿管理Service接口
 * Created by macro on 2023/12/22.
 */
public interface PmsProductDraftService {
    
    /**
     * 创建或更新草稿
     */
    @Transactional
    int createOrUpdateDraft(PmsProductDraftParam draftParam);
    
    /**
     * 获取草稿详情
     */
    PmsProductDraftResult getDraft(Long id);
    
    /**
     * 根据商品ID获取最新草稿
     */
    PmsProductDraftResult getLatestDraftByProductId(Long productId);
    
    /**
     * 获取用户的草稿列表
     */
    CommonPage<PmsProductDraftResult> getUserDrafts(Long userId, Integer status, Integer pageSize, Integer pageNum);
    
    /**
     * 提交草稿进行审核
     */
    @Transactional
    int submitDraftForReview(Long draftId, String notes);
    
    /**
     * 删除草稿
     */
    @Transactional
    int deleteDraft(Long id);
    
    /**
     * 批量删除草稿
     */
    @Transactional
    int deleteDrafts(List<Long> ids);
    
    /**
     * 复制草稿
     */
    @Transactional
    int copyDraft(Long draftId, String newTitle);
    
    /**
     * 自动保存草稿
     */
    @Transactional
    int autoSaveDraft(PmsProductDraftParam draftParam);
    
    /**
     * 清理过期草稿
     */
    @Transactional
    int cleanExpiredDrafts(Integer expireDays);
    
    /**
     * 获取草稿统计信息
     */
    java.util.Map<String, Object> getDraftStatistics(Long userId);
    
    /**
     * 根据商品ID获取所有草稿版本
     */
    List<PmsProductDraftResult> getDraftVersions(Long productId);
    
    /**
     * 更新草稿状态
     */
    @Transactional
    int updateDraftStatus(Long draftId, Integer status);
}