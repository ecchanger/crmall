package com.macro.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.dao.PmsProductDraftDao;
import com.macro.mall.dto.PmsProductDraftParam;
import com.macro.mall.dto.PmsProductDraftResult;
import com.macro.mall.model.PmsProductDraft;
import com.macro.mall.service.PmsProductDraftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品草稿管理Service实现类
 * Created by macro on 2023/12/22.
 */
@Service
public class PmsProductDraftServiceImpl implements PmsProductDraftService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductDraftServiceImpl.class);
    
    @Autowired
    private PmsProductDraftDao productDraftDao;

    @Override
    public int createOrUpdateDraft(PmsProductDraftParam draftParam) {
        try {
            PmsProductDraft draft = new PmsProductDraft();
            
            // 如果是新草稿，设置基本信息
            if (draftParam.getProductId() == null) {
                draft.setTitle(draftParam.getTitle());
                draft.setStatus(0); // 编辑中
                draft.setVersion(1);
                draft.setCreatedBy(getCurrentUserId());
                draft.setCreateTime(new Date());
            } else {
                // 更新现有草稿
                PmsProductDraftResult existingDraft = productDraftDao.getLatestDraftByProductId(draftParam.getProductId());
                if (existingDraft != null) {
                    draft.setId(existingDraft.getId());
                    draft.setVersion(existingDraft.getVersion() + 1);
                }
                draft.setProductId(draftParam.getProductId());
            }
            
            // 设置草稿内容
            if (draftParam.getProductParam() != null) {
                draft.setContent(JSONUtil.toJsonStr(draftParam.getProductParam()));
            }
            
            draft.setTitle(draftParam.getTitle());
            draft.setNotes(draftParam.getNotes());
            draft.setLastModifiedBy(getCurrentUserId());
            draft.setLastModifiedTime(new Date());
            
            if (draft.getId() == null) {
                return productDraftDao.insert(draft);
            } else {
                return productDraftDao.updateByPrimaryKeySelective(draft);
            }
            
        } catch (Exception e) {
            LOGGER.error("创建或更新草稿失败", e);
            throw new RuntimeException("创建或更新草稿失败: " + e.getMessage());
        }
    }

    @Override
    public PmsProductDraftResult getDraft(Long id) {
        return productDraftDao.getDraftDetail(id);
    }

    @Override
    public PmsProductDraftResult getLatestDraftByProductId(Long productId) {
        return productDraftDao.getLatestDraftByProductId(productId);
    }

    @Override
    public CommonPage<PmsProductDraftResult> getUserDrafts(Long userId, Integer status, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<PmsProductDraftResult> drafts = productDraftDao.getDraftsByCreator(userId, status, pageSize, pageNum);
        return CommonPage.restPage(drafts);
    }

    @Override
    public int submitDraftForReview(Long draftId, String notes) {
        try {
            PmsProductDraftResult draft = productDraftDao.getDraftDetail(draftId);
            if (draft == null) {
                throw new RuntimeException("草稿不存在");
            }
            
            if (draft.getStatus() != 0) {
                throw new RuntimeException("只有编辑中的草稿才能提交审核");
            }
            
            // 验证草稿内容完整性
            if (StrUtil.isEmpty(draft.getContent())) {
                throw new RuntimeException("草稿内容不能为空");
            }
            
            // 更新草稿状态为已提交审核
            int result = productDraftDao.updateDraftStatus(draftId, 1);
            
            // TODO: 触发审核流程
            // createReviewProcess(draftId, notes);
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("提交草稿审核失败", e);
            throw new RuntimeException("提交草稿审核失败: " + e.getMessage());
        }
    }

    @Override
    public int deleteDraft(Long id) {
        try {
            PmsProductDraftResult draft = productDraftDao.getDraftDetail(id);
            if (draft == null) {
                throw new RuntimeException("草稿不存在");
            }
            
            // 检查权限：只有创建者或管理员可以删除
            if (!draft.getCreatedBy().equals(getCurrentUserId()) && !isCurrentUserAdmin()) {
                throw new RuntimeException("无权限删除此草稿");
            }
            
            return productDraftDao.deleteByPrimaryKey(id);
            
        } catch (Exception e) {
            LOGGER.error("删除草稿失败", e);
            throw new RuntimeException("删除草稿失败: " + e.getMessage());
        }
    }

    @Override
    public int deleteDrafts(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        
        int count = 0;
        for (Long id : ids) {
            try {
                count += deleteDraft(id);
            } catch (Exception e) {
                LOGGER.warn("删除草稿 {} 失败: {}", id, e.getMessage());
            }
        }
        return count;
    }

    @Override
    public int copyDraft(Long draftId, String newTitle) {
        try {
            PmsProductDraftResult sourceDraft = productDraftDao.getDraftDetail(draftId);
            if (sourceDraft == null) {
                throw new RuntimeException("源草稿不存在");
            }
            
            PmsProductDraft newDraft = new PmsProductDraft();
            BeanUtils.copyProperties(sourceDraft, newDraft);
            
            newDraft.setId(null);
            newDraft.setTitle(newTitle);
            newDraft.setStatus(0); // 编辑中
            newDraft.setVersion(1);
            newDraft.setCreatedBy(getCurrentUserId());
            newDraft.setCreateTime(new Date());
            newDraft.setLastModifiedBy(getCurrentUserId());
            newDraft.setLastModifiedTime(new Date());
            
            return productDraftDao.insert(newDraft);
            
        } catch (Exception e) {
            LOGGER.error("复制草稿失败", e);
            throw new RuntimeException("复制草稿失败: " + e.getMessage());
        }
    }

    @Override
    public int autoSaveDraft(PmsProductDraftParam draftParam) {
        // 自动保存不抛出异常，记录日志即可
        try {
            draftParam.setAutoSave(true);
            return createOrUpdateDraft(draftParam);
        } catch (Exception e) {
            LOGGER.warn("自动保存草稿失败", e);
            return 0;
        }
    }

    @Override
    public int cleanExpiredDrafts(Integer expireDays) {
        if (expireDays == null || expireDays <= 0) {
            expireDays = 30; // 默认30天
        }
        return productDraftDao.deleteExpiredDrafts(expireDays);
    }

    @Override
    public Map<String, Object> getDraftStatistics(Long userId) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 获取各状态草稿数量
        int editingCount = productDraftDao.countDraftsByStatus(0);
        int reviewingCount = productDraftDao.countDraftsByStatus(1);
        int approvedCount = productDraftDao.countDraftsByStatus(2);
        int rejectedCount = productDraftDao.countDraftsByStatus(3);
        
        statistics.put("editingCount", editingCount);
        statistics.put("reviewingCount", reviewingCount);
        statistics.put("approvedCount", approvedCount);
        statistics.put("rejectedCount", rejectedCount);
        statistics.put("totalCount", editingCount + reviewingCount + approvedCount + rejectedCount);
        
        return statistics;
    }

    @Override
    public List<PmsProductDraftResult> getDraftVersions(Long productId) {
        return productDraftDao.getDraftVersionsByProductId(productId);
    }
    
    @Override
    public int updateDraftStatus(Long draftId, Integer status) {
        return productDraftDao.updateDraftStatus(draftId, status);
    }
    
    /**
     * 获取当前用户ID（实际项目中从SecurityContext获取）
     */
    private Long getCurrentUserId() {
        // TODO: 从Spring Security获取当前用户ID
        return 1L; // 临时返回
    }
    
    /**
     * 判断当前用户是否为管理员
     */
    private boolean isCurrentUserAdmin() {
        // TODO: 从Spring Security获取当前用户角色
        return true; // 临时返回
    }
}