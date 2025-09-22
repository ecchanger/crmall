package com.macro.mall.repository;

import com.macro.mall.domain.AdminProductSearchDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 管理端商品搜索Repository
 * Created by macro on 2023/12/22.
 */
@Repository
public interface AdminProductSearchRepository extends ElasticsearchRepository<AdminProductSearchDocument, Long> {
    
    /**
     * 根据名称或关键字搜索
     */
    Page<AdminProductSearchDocument> findByNameContainingOrKeywordsContainingOrDescriptionContaining(
        String name, String keywords, String description, Pageable pageable);
    
    /**
     * 根据品牌搜索
     */
    Page<AdminProductSearchDocument> findByBrandId(Long brandId, Pageable pageable);
    
    /**
     * 根据分类搜索
     */
    Page<AdminProductSearchDocument> findByProductCategoryId(Long categoryId, Pageable pageable);
    
    /**
     * 根据发布状态搜索
     */
    Page<AdminProductSearchDocument> findByPublishStatus(Integer publishStatus, Pageable pageable);
    
    /**
     * 根据审核状态搜索
     */
    Page<AdminProductSearchDocument> findByVerifyStatus(Integer verifyStatus, Pageable pageable);
    
    /**
     * 根据价格范围搜索
     */
    Page<AdminProductSearchDocument> findByPriceBetween(
        java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, Pageable pageable);
    
    /**
     * 复合条件搜索
     */
    Page<AdminProductSearchDocument> findByNameContainingAndBrandIdAndProductCategoryId(
        String name, Long brandId, Long categoryId, Pageable pageable);
}