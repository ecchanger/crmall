package com.macro.mall.dao;

import com.macro.mall.model.PmsProductDraft;
import com.macro.mall.dto.PmsProductDraftResult;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 商品草稿数据访问接口
 * Created by macro on 2023/12/22.
 */
public interface PmsProductDraftDao {
    
    /**
     * 批量插入草稿
     */
    int insertList(@Param("list") List<PmsProductDraft> draftList);
    
    /**
     * 根据商品ID获取最新草稿
     */
    PmsProductDraftResult getLatestDraftByProductId(@Param("productId") Long productId);
    
    /**
     * 根据创建人ID获取草稿列表
     */
    List<PmsProductDraftResult> getDraftsByCreator(
        @Param("createdBy") Long createdBy,
        @Param("status") Integer status,
        @Param("pageSize") Integer pageSize, 
        @Param("pageNum") Integer pageNum
    );
    
    /**
     * 根据状态获取草稿数量
     */
    int countDraftsByStatus(@Param("status") Integer status);
    
    /**
     * 获取草稿详细信息（包含用户名）
     */
    PmsProductDraftResult getDraftDetail(@Param("id") Long id);
    
    /**
     * 更新草稿状态
     */
    int updateDraftStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 删除过期草稿
     */
    int deleteExpiredDrafts(@Param("expireDays") Integer expireDays);
    
    /**
     * 根据商品ID获取所有草稿版本
     */
    List<PmsProductDraftResult> getDraftVersionsByProductId(@Param("productId") Long productId);
    
    /**
     * 插入草稿记录
     */
    int insert(PmsProductDraft draft);
    
    /**
     * 根据主键删除
     */
    int deleteByPrimaryKey(Long id);
    
    /**
     * 根据主键选择性更新
     */
    int updateByPrimaryKeySelective(PmsProductDraft draft);
}