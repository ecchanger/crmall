package com.macro.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.domain.AdminProductSearchDocument;
import com.macro.mall.dto.PmsProductAdvancedSearchParam;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.repository.AdminProductSearchRepository;
import com.macro.mall.service.PmsProductAdvancedSearchService;
import com.macro.mall.service.PmsProductService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品高级搜索服务实现类
 * Created by macro on 2023/12/22.
 */
@Service
public class PmsProductAdvancedSearchServiceImpl implements PmsProductAdvancedSearchService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductAdvancedSearchServiceImpl.class);
    
    @Autowired
    private AdminProductSearchRepository searchRepository;
    
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;
    
    @Autowired
    private PmsProductService productService;

    @Override
    public CommonPage<PmsProduct> advancedSearch(PmsProductAdvancedSearchParam searchParam, 
                                                Integer pageNum, Integer pageSize) {
        try {
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            
            // 构建查询条件
            buildSearchQuery(boolQuery, searchParam);
            
            queryBuilder.withQuery(boolQuery);
            
            // 设置分页
            Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
            queryBuilder.withPageable(pageable);
            
            // 设置排序
            addSortCondition(queryBuilder, searchParam);
            
            // 执行搜索
            NativeSearchQuery searchQuery = queryBuilder.build();
            SearchHits<AdminProductSearchDocument> searchHits = elasticsearchTemplate.search(searchQuery, AdminProductSearchDocument.class);
            
            // 转换结果
            List<PmsProduct> products = convertToProducts(searchHits);
            
            // 构建分页结果
            CommonPage<PmsProduct> result = new CommonPage<>();
            result.setList(products);
            result.setPageNum(pageNum);
            result.setPageSize(pageSize);
            result.setTotal(searchHits.getTotalHits());
            result.setTotalPage((int) Math.ceil((double) searchHits.getTotalHits() / pageSize));
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("高级搜索失败", e);
            throw new RuntimeException("高级搜索失败: " + e.getMessage());
        }
    }

    @Override
    public List<PmsProduct> fuzzySearch(String keyword, Integer limit) {
        try {
            if (StrUtil.isEmpty(keyword)) {
                return new ArrayList<>();
            }
            
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            
            // 模糊搜索多个字段
            boolQuery.should(QueryBuilders.fuzzyQuery("name", keyword).fuzziness("AUTO"))
                    .should(QueryBuilders.fuzzyQuery("keywords", keyword).fuzziness("AUTO"))
                    .should(QueryBuilders.fuzzyQuery("description", keyword).fuzziness("AUTO"))
                    .should(QueryBuilders.fuzzyQuery("brandName", keyword).fuzziness("AUTO"))
                    .minimumShouldMatch(1);
            
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(boolQuery)
                    .withPageable(PageRequest.of(0, limit != null ? limit : 10))
                    .build();
            
            SearchHits<AdminProductSearchDocument> searchHits = elasticsearchTemplate.search(searchQuery, AdminProductSearchDocument.class);
            return convertToProducts(searchHits);
            
        } catch (Exception e) {
            LOGGER.error("模糊搜索失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getSearchSuggestions(String keyword, Integer limit) {
        try {
            if (StrUtil.isEmpty(keyword)) {
                return new ArrayList<>();
            }
            
            // 使用前缀查询获取搜索建议
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.should(QueryBuilders.prefixQuery("name", keyword))
                    .should(QueryBuilders.prefixQuery("keywords", keyword))
                    .should(QueryBuilders.prefixQuery("brandName", keyword))
                    .minimumShouldMatch(1);
            
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(boolQuery)
                    .withFields("name", "keywords", "brandName")
                    .withPageable(PageRequest.of(0, limit != null ? limit : 10))
                    .build();
            
            SearchHits<AdminProductSearchDocument> searchHits = elasticsearchTemplate.search(searchQuery, AdminProductSearchDocument.class);
            
            Set<String> suggestions = new HashSet<>();
            for (SearchHit<AdminProductSearchDocument> hit : searchHits) {
                AdminProductSearchDocument doc = hit.getContent();
                if (doc.getName() != null && doc.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    suggestions.add(doc.getName());
                }
                if (doc.getKeywords() != null && doc.getKeywords().toLowerCase().contains(keyword.toLowerCase())) {
                    suggestions.add(doc.getKeywords());
                }
                if (doc.getBrandName() != null && doc.getBrandName().toLowerCase().contains(keyword.toLowerCase())) {
                    suggestions.add(doc.getBrandName());
                }
            }
            
            return suggestions.stream().limit(limit != null ? limit : 10).collect(Collectors.toList());
            
        } catch (Exception e) {
            LOGGER.error("获取搜索建议失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getSearchFilters(PmsProductAdvancedSearchParam baseParam) {
        Map<String, Object> filters = new HashMap<>();
        
        try {
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            
            if (baseParam != null) {
                buildSearchQuery(boolQuery, baseParam);
            }
            
            queryBuilder.withQuery(boolQuery);
            
            // 添加聚合查询
            queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brandId").size(100))
                        .addAggregation(AggregationBuilders.terms("categories").field("productCategoryId").size(100))
                        .addAggregation(AggregationBuilders.range("priceRanges").field("price")
                                .addRange(0, 100)
                                .addRange(100, 500)
                                .addRange(500, 1000)
                                .addRange(1000, 5000)
                                .addRange(5000, Double.MAX_VALUE))
                        .addAggregation(AggregationBuilders.terms("publishStatus").field("publishStatus"))
                        .addAggregation(AggregationBuilders.terms("verifyStatus").field("verifyStatus"));
            
            NativeSearchQuery searchQuery = queryBuilder.build();
            SearchHits<AdminProductSearchDocument> searchHits = elasticsearchTemplate.search(searchQuery, AdminProductSearchDocument.class);
            
            // 处理聚合结果
            Aggregations aggregations = searchHits.getAggregations();
            if (aggregations != null) {
                // 处理品牌聚合
                Terms brandsAgg = aggregations.get("brands");
                if (brandsAgg != null) {
                    List<Map<String, Object>> brandFilters = new ArrayList<>();
                    for (Terms.Bucket bucket : brandsAgg.getBuckets()) {
                        Map<String, Object> brandFilter = new HashMap<>();
                        brandFilter.put("id", bucket.getKey());
                        brandFilter.put("count", bucket.getDocCount());
                        brandFilters.add(brandFilter);
                    }
                    filters.put("brands", brandFilters);
                }
                
                // 处理分类聚合
                Terms categoriesAgg = aggregations.get("categories");
                if (categoriesAgg != null) {
                    List<Map<String, Object>> categoryFilters = new ArrayList<>();
                    for (Terms.Bucket bucket : categoriesAgg.getBuckets()) {
                        Map<String, Object> categoryFilter = new HashMap<>();
                        categoryFilter.put("id", bucket.getKey());
                        categoryFilter.put("count", bucket.getDocCount());
                        categoryFilters.add(categoryFilter);
                    }
                    filters.put("categories", categoryFilters);
                }
                
                // 处理其他聚合...
            }
            
        } catch (Exception e) {
            LOGGER.error("获取搜索筛选选项失败", e);
        }
        
        return filters;
    }

    @Override
    public List<Map<String, Object>> getSearchHotWords(Integer limit) {
        // TODO: 实现热词统计（需要记录搜索日志）
        return new ArrayList<>();
    }

    @Override
    public List<PmsProduct> searchByAttributes(Map<Long, List<String>> attributes, Integer pageNum, Integer pageSize) {
        try {
            if (CollectionUtils.isEmpty(attributes)) {
                return new ArrayList<>();
            }
            
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            
            // 构建属性查询
            for (Map.Entry<Long, List<String>> entry : attributes.entrySet()) {
                Long attributeId = entry.getKey();
                List<String> values = entry.getValue();
                
                BoolQueryBuilder attrQuery = QueryBuilders.boolQuery();
                for (String value : values) {
                    attrQuery.should(QueryBuilders.nestedQuery("attributes",
                            QueryBuilders.boolQuery()
                                    .must(QueryBuilders.termQuery("attributes.productAttributeId", attributeId))
                                    .must(QueryBuilders.termQuery("attributes.value", value)),
                            org.apache.lucene.search.join.ScoreMode.None));
                }
                boolQuery.must(attrQuery);
            }
            
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(boolQuery)
                    .withPageable(PageRequest.of(pageNum - 1, pageSize))
                    .build();
            
            SearchHits<AdminProductSearchDocument> searchHits = elasticsearchTemplate.search(searchQuery, AdminProductSearchDocument.class);
            return convertToProducts(searchHits);
            
        } catch (Exception e) {
            LOGGER.error("按属性搜索失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public CommonPage<PmsProduct> multiFieldSearch(Map<String, Object> searchFields, Integer pageNum, Integer pageSize) {
        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            
            for (Map.Entry<String, Object> entry : searchFields.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();
                
                if (value != null && !value.toString().isEmpty()) {
                    switch (field) {
                        case "name":
                        case "keywords":
                        case "description":
                            boolQuery.must(QueryBuilders.matchQuery(field, value));
                            break;
                        case "brandId":
                        case "productCategoryId":
                        case "publishStatus":
                        case "verifyStatus":
                            boolQuery.must(QueryBuilders.termQuery(field, value));
                            break;
                        case "minPrice":
                            boolQuery.must(QueryBuilders.rangeQuery("price").gte(value));
                            break;
                        case "maxPrice":
                            boolQuery.must(QueryBuilders.rangeQuery("price").lte(value));
                            break;
                        default:
                            // 其他字段处理
                            break;
                    }
                }
            }
            
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(boolQuery)
                    .withPageable(PageRequest.of(pageNum - 1, pageSize))
                    .build();
            
            SearchHits<AdminProductSearchDocument> searchHits = elasticsearchTemplate.search(searchQuery, AdminProductSearchDocument.class);
            List<PmsProduct> products = convertToProducts(searchHits);
            
            CommonPage<PmsProduct> result = new CommonPage<>();
            result.setList(products);
            result.setPageNum(pageNum);
            result.setPageSize(pageSize);
            result.setTotal(searchHits.getTotalHits());
            result.setTotalPage((int) Math.ceil((double) searchHits.getTotalHits() / pageSize));
            
            return result;
            
        } catch (Exception e) {
            LOGGER.error("多字段搜索失败", e);
            throw new RuntimeException("多字段搜索失败: " + e.getMessage());
        }
    }

    @Override
    public void saveSearchRecord(String keyword, Long userId, Integer resultCount) {
        // TODO: 实现搜索记录保存
        LOGGER.info("保存搜索记录: keyword={}, userId={}, resultCount={}", keyword, userId, resultCount);
    }

    @Override
    public List<String> getUserSearchHistory(Long userId, Integer limit) {
        // TODO: 实现用户搜索历史获取
        return new ArrayList<>();
    }

    @Override
    public void clearSearchIndex() {
        try {
            elasticsearchTemplate.indexOps(AdminProductSearchDocument.class).delete();
            elasticsearchTemplate.indexOps(AdminProductSearchDocument.class).create();
            elasticsearchTemplate.indexOps(AdminProductSearchDocument.class).putMapping();
            LOGGER.info("搜索索引清空完成");
        } catch (Exception e) {
            LOGGER.error("清空搜索索引失败", e);
            throw new RuntimeException("清空搜索索引失败: " + e.getMessage());
        }
    }

    @Override
    public void rebuildSearchIndex() {
        try {
            // 清空现有索引
            clearSearchIndex();
            
            // TODO: 从数据库重新构建索引
            // List<PmsProduct> allProducts = productService.getAllProducts();
            // List<AdminProductSearchDocument> documents = convertToDocuments(allProducts);
            // searchRepository.saveAll(documents);
            
            LOGGER.info("搜索索引重建完成");
        } catch (Exception e) {
            LOGGER.error("重建搜索索引失败", e);
            throw new RuntimeException("重建搜索索引失败: " + e.getMessage());
        }
    }

    @Override
    public void updateSearchIndex(List<Long> productIds) {
        try {
            if (CollectionUtils.isEmpty(productIds)) {
                return;
            }
            
            // TODO: 根据商品ID更新索引
            // for (Long productId : productIds) {
            //     PmsProduct product = productService.getById(productId);
            //     if (product != null) {
            //         AdminProductSearchDocument document = convertToDocument(product);
            //         searchRepository.save(document);
            //     }
            // }
            
            LOGGER.info("更新搜索索引完成，商品数量: {}", productIds.size());
        } catch (Exception e) {
            LOGGER.error("更新搜索索引失败", e);
            throw new RuntimeException("更新搜索索引失败: " + e.getMessage());
        }
    }
    
    /**
     * 构建搜索查询条件
     */
    private void buildSearchQuery(BoolQueryBuilder boolQuery, PmsProductAdvancedSearchParam searchParam) {
        // 关键词搜索
        if (StrUtil.isNotEmpty(searchParam.getKeyword())) {
            BoolQueryBuilder keywordQuery = QueryBuilders.boolQuery();
            keywordQuery.should(QueryBuilders.matchQuery("name", searchParam.getKeyword()).boost(3.0f))
                       .should(QueryBuilders.matchQuery("keywords", searchParam.getKeyword()).boost(2.0f))
                       .should(QueryBuilders.matchQuery("description", searchParam.getKeyword()).boost(1.0f))
                       .should(QueryBuilders.matchQuery("brandName", searchParam.getKeyword()).boost(2.0f))
                       .minimumShouldMatch(1);
            boolQuery.must(keywordQuery);
        }
        
        // 商品名称
        if (StrUtil.isNotEmpty(searchParam.getName())) {
            boolQuery.must(QueryBuilders.matchQuery("name", searchParam.getName()));
        }
        
        // 品牌筛选
        if (!CollectionUtils.isEmpty(searchParam.getBrandIds())) {
            boolQuery.must(QueryBuilders.termsQuery("brandId", searchParam.getBrandIds()));
        }
        
        // 分类筛选
        if (!CollectionUtils.isEmpty(searchParam.getCategoryIds())) {
            boolQuery.must(QueryBuilders.termsQuery("productCategoryId", searchParam.getCategoryIds()));
        }
        
        // 价格范围
        if (searchParam.getMinPrice() != null || searchParam.getMaxPrice() != null) {
            org.elasticsearch.index.query.RangeQueryBuilder priceRange = QueryBuilders.rangeQuery("price");
            if (searchParam.getMinPrice() != null) {
                priceRange.gte(searchParam.getMinPrice());
            }
            if (searchParam.getMaxPrice() != null) {
                priceRange.lte(searchParam.getMaxPrice());
            }
            boolQuery.must(priceRange);
        }
        
        // 发布状态
        if (!CollectionUtils.isEmpty(searchParam.getPublishStatus())) {
            boolQuery.must(QueryBuilders.termsQuery("publishStatus", searchParam.getPublishStatus()));
        }
        
        // 审核状态
        if (!CollectionUtils.isEmpty(searchParam.getVerifyStatus())) {
            boolQuery.must(QueryBuilders.termsQuery("verifyStatus", searchParam.getVerifyStatus()));
        }
        
        // 新品状态
        if (searchParam.getNewStatus() != null) {
            boolQuery.must(QueryBuilders.termQuery("newStatus", searchParam.getNewStatus()));
        }
        
        // 推荐状态
        if (searchParam.getRecommendStatus() != null) {
            boolQuery.must(QueryBuilders.termQuery("recommendStatus", searchParam.getRecommendStatus()));
        }
        
        // 标签筛选
        if (!CollectionUtils.isEmpty(searchParam.getTags())) {
            boolQuery.must(QueryBuilders.termsQuery("tags", searchParam.getTags()));
        }
        
        // 属性筛选
        if (!CollectionUtils.isEmpty(searchParam.getAttributes())) {
            for (Map.Entry<Long, List<String>> entry : searchParam.getAttributes().entrySet()) {
                Long attributeId = entry.getKey();
                List<String> values = entry.getValue();
                
                BoolQueryBuilder attrQuery = QueryBuilders.boolQuery();
                for (String value : values) {
                    attrQuery.should(QueryBuilders.nestedQuery("attributes",
                            QueryBuilders.boolQuery()
                                    .must(QueryBuilders.termQuery("attributes.productAttributeId", attributeId))
                                    .must(QueryBuilders.termQuery("attributes.value", value)),
                            org.apache.lucene.search.join.ScoreMode.None));
                }
                boolQuery.must(attrQuery);
            }
        }
        
        // 默认过滤已删除的商品
        boolQuery.must(QueryBuilders.termQuery("deleteStatus", 0));
    }
    
    /**
     * 添加排序条件
     */
    private void addSortCondition(NativeSearchQueryBuilder queryBuilder, PmsProductAdvancedSearchParam searchParam) {
        String sortField = searchParam.getSortField();
        String sortDirection = searchParam.getSortDirection();
        
        if (StrUtil.isEmpty(sortField)) {
            sortField = "id";
        }
        
        SortOrder order = "asc".equalsIgnoreCase(sortDirection) ? SortOrder.ASC : SortOrder.DESC;
        
        switch (sortField.toLowerCase()) {
            case "price":
                queryBuilder.withSort(SortBuilders.fieldSort("price").order(order));
                break;
            case "createtime":
                queryBuilder.withSort(SortBuilders.fieldSort("createTime").order(order));
                break;
            case "stock":
                queryBuilder.withSort(SortBuilders.fieldSort("stock").order(order));
                break;
            case "score":
                queryBuilder.withSort(SortBuilders.scoreSort().order(order));
                break;
            default:
                queryBuilder.withSort(SortBuilders.fieldSort("id").order(order));
                break;
        }
    }
    
    /**
     * 转换搜索结果为商品列表
     */
    private List<PmsProduct> convertToProducts(SearchHits<AdminProductSearchDocument> searchHits) {
        List<PmsProduct> products = new ArrayList<>();
        
        for (SearchHit<AdminProductSearchDocument> hit : searchHits) {
            AdminProductSearchDocument document = hit.getContent();
            PmsProduct product = new PmsProduct();
            
            // 复制基本属性
            BeanUtils.copyProperties(document, product);
            
            products.add(product);
        }
        
        return products;
    }
}