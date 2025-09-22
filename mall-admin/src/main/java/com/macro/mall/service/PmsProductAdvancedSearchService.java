package com.macro.mall.service;

import com.macro.mall.dto.PmsProductAdvancedSearchParam;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.model.PmsProduct;
import java.util.List;
import java.util.Map;

/**
 * 商品高级搜索服务接口
 * Created by macro on 2023/12/22.
 */
public interface PmsProductAdvancedSearchService {
    
    /**
     * 高级搜索商品
     */
    CommonPage<PmsProduct> advancedSearch(PmsProductAdvancedSearchParam searchParam, 
                                         Integer pageNum, Integer pageSize);
    
    /**
     * 模糊搜索商品
     */
    List<PmsProduct> fuzzySearch(String keyword, Integer limit);
    
    /**
     * 搜索建议
     */
    List<String> getSearchSuggestions(String keyword, Integer limit);
    
    /**
     * 获取搜索筛选选项
     */
    Map<String, Object> getSearchFilters(PmsProductAdvancedSearchParam baseParam);
    
    /**
     * 搜索热词统计
     */
    List<Map<String, Object>> getSearchHotWords(Integer limit);
    
    /**
     * 根据属性值搜索
     */
    List<PmsProduct> searchByAttributes(Map<Long, List<String>> attributes, 
                                       Integer pageNum, Integer pageSize);
    
    /**
     * 多字段搜索
     */
    CommonPage<PmsProduct> multiFieldSearch(Map<String, Object> searchFields, 
                                           Integer pageNum, Integer pageSize);
    
    /**
     * 保存搜索记录
     */
    void saveSearchRecord(String keyword, Long userId, Integer resultCount);
    
    /**
     * 获取用户搜索历史
     */
    List<String> getUserSearchHistory(Long userId, Integer limit);
    
    /**
     * 清空搜索索引
     */
    void clearSearchIndex();
    
    /**
     * 重建搜索索引
     */
    void rebuildSearchIndex();
    
    /**
     * 增量更新搜索索引
     */
    void updateSearchIndex(List<Long> productIds);
}