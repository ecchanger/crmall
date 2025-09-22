package com.macro.mall.dao;

import com.macro.mall.model.PmsProductReview;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 商品审核数据访问接口
 * Created by macro on 2023/12/22.
 */
public interface PmsProductReviewDao {
    
    /**
     * 插入审核记录
     */
    int insert(PmsProductReview review);
    
    /**
     * 根据主键查询
     */
    PmsProductReview selectByPrimaryKey(Long id);
    
    /**
     * 根据主键更新
     */
    int updateByPrimaryKeySelective(PmsProductReview review);
    
    /**
     * 根据主键删除
     */
    int deleteByPrimaryKey(Long id);
    
    /**
     * 获取待审核列表
     */
    List<PmsProductReview> getPendingReviews(@Param("reviewerId") Long reviewerId);
    
    /**
     * 根据商品ID获取审核记录
     */
    List<PmsProductReview> getReviewsByProductId(@Param("productId") Long productId);
    
    /**
     * 根据状态统计审核数量
     */
    int countReviewsByStatus(@Param("reviewerId") Long reviewerId, @Param("status") Integer status);
    
    /**
     * 批量插入审核记录
     */
    int insertList(@Param("list") List<PmsProductReview> reviewList);
}